/*
 * Copyright (C) 2017, The LineageOS Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#define LOG_TAG "audio_amplifier"
//#define LOG_NDEBUG 0

#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>

#include <cutils/log.h>

#include <audio_hw.h>
#include <hardware/audio_amplifier.h>
#include <voice.h>
#include <msm8916/platform.h>

#include <system/audio.h>
#include <tinyalsa/asoundlib.h>

#include "tfa.h"

#define UNUSED __attribute__((unused))


typedef struct c5_device {
    amplifier_device_t amp_dev;
    audio_mode_t current_mode;
    tfa_t *tfa;
    bool enable;
    bool initializing;
    bool writing;
    pthread_t write_thread;
    pthread_mutex_t mutex;
    pthread_cond_t cond;
} c5_device_t;

static c5_device_t *c5_dev = NULL;


static void * write_dummy_data(void *param) {
    c5_device_t *t = (c5_device_t *) param;
    uint8_t *buffer;
    int size;
    struct pcm *pcm;
    bool signaled = false;

    struct pcm_config config = {
        .channels = 2,
        .rate = 48000,
        .period_size = 256,
        .period_count = 2,
        .format = PCM_FORMAT_S16_LE,
        .start_threshold = config.period_size * config.period_count - 1,
        .stop_threshold = UINT_MAX,
        .silence_threshold = 0,
        .avail_min = 1,
    };

    pcm = pcm_open(0, 0, PCM_OUT | PCM_MONOTONIC, &config);
    if (!pcm || !pcm_is_ready(pcm)) {
        ALOGE("pcm_open failed: %s", pcm_get_error(pcm));
        if (pcm) {
            goto err_close_pcm;
        }
        goto exit;
    }

    size = 1024 * 8;
    buffer = calloc(1, size);
    if (!buffer) {
        ALOGE("%s: failed to allocate buffer", __func__);
        goto err_close_pcm;
    }

    do {
        if (pcm_write(pcm, buffer, size)) {
            ALOGE("%s: pcm_write failed", __func__);
        }
        if (!signaled) {
            pthread_mutex_lock(&t->mutex);
            t->writing = true;
            pthread_cond_signal(&t->cond);
            pthread_mutex_unlock(&t->mutex);
            signaled = true;
        }
    } while (t->initializing);


err_free:
    free(buffer);
err_close_pcm:
    pcm_close(pcm);
exit:
    if (!signaled) {
        pthread_mutex_lock(&t->mutex);
        t->writing = true;
        pthread_cond_signal(&t->cond);
        pthread_mutex_unlock(&t->mutex);
    }
    return NULL;
}


static int amp_set_mode(amplifier_device_t *device, audio_mode_t mode)
{
    int ret = 0;
    c5_device_t *dev = (c5_device_t *) device;

    dev->current_mode = mode;

    return ret;
}

static int enable_mixers(bool enable)
{
    enum mixer_ctl_type type;
    int i;
    struct mixer_ctl *ctl;
    struct mixer *mixer = mixer_open(0);

    char *mixers[]={"QUAT_MI2S_RX Audio Mixer MultiMedia1",
                    "QUAT_MI2S_RX Audio Mixer MultiMedia2",
                    "QUAT_MI2S_RX Audio Mixer MultiMedia3",
                    "QUAT_MI2S_RX Audio Mixer MultiMedia4",
                    "QUAT_MI2S_RX Audio Mixer MultiMedia5"};

    if (mixer == NULL) {
        ALOGE("%s:%d: Error opening mixer 0\n",
                __func__, __LINE__);
        return -1;
    }

    for (i = 0; i < 5; i++) {
        ctl = mixer_get_ctl_by_name(mixer, mixers[i]);
        if (ctl == NULL) {
            mixer_close(mixer);
            ALOGE("%s:%d Could not find %s\n",
                    __func__, __LINE__, mixers[i]);
            return -ENODEV;
        }
        mixer_ctl_set_value(ctl, 0, enable);
    }

    mixer_close(mixer);
    return 0;

}

static int amp_enable_output_devices(amplifier_device_t *device,
        uint32_t devices, bool enable)
{
    c5_device_t *dev = (c5_device_t *) device;

    switch (devices) {
        case SND_DEVICE_OUT_SPEAKER:
        case SND_DEVICE_OUT_SPEAKER_AND_HEADPHONES:
        case SND_DEVICE_OUT_VOICE_SPEAKER:
        case SND_DEVICE_OUT_VOIP_SPEAKER:
            if(dev->enable != enable)
            {
                enable_mixers(enable);
                dev->enable = enable;
                if (enable) {
                    dev->initializing = true;
                    pthread_create(&dev->write_thread, NULL, write_dummy_data, dev);
                    dev->tfa->tfa_enable (dev->tfa,dev->enable);
                    pthread_mutex_lock(&dev->mutex);
                    dev->initializing = false;
                    while (!dev->writing) pthread_cond_wait(&dev->cond, &dev->mutex);
                    pthread_mutex_unlock(&dev->mutex);
                }
                else
                {
                    dev->tfa->tfa_enable (dev->tfa,dev->enable);
                }
            }
        default:
            ALOGE("%s:%d: Enable = %d Device = %d\n",
                __func__, __LINE__, enable, devices);

            break;
    }

    return 0;
}

static int amp_dev_close(hw_device_t *device)
{
    c5_device_t *dev = (c5_device_t *) device;

    free(dev);

    return 0;
}

static int amp_module_open(const hw_module_t *module, UNUSED const char *name,
        hw_device_t **device)
{
    if (c5_dev) {
        ALOGE("%s:%d: Unable to open second instance of amplifier\n",
                __func__, __LINE__);
        return -EBUSY;
    }

    c5_dev = calloc(1, sizeof(c5_device_t));
    if (!c5_dev) {
        ALOGE("%s:%d: Unable to allocate memory for amplifier device\n",
                __func__, __LINE__);
        return -ENOMEM;
    }

    c5_dev->tfa = calloc(1,sizeof(tfa_t));
    if (!c5_dev->tfa) {
        ALOGE("%s:%d: Unable to allocate memory for tfa device\n",
                __func__, __LINE__);
        return -ENOMEM;
    }

    tfa_device_open(c5_dev->tfa);

    c5_dev->amp_dev.common.tag = HARDWARE_DEVICE_TAG;
    c5_dev->amp_dev.common.module = (hw_module_t *) module;
    c5_dev->amp_dev.common.version = HARDWARE_DEVICE_API_VERSION(1, 0);
    c5_dev->amp_dev.common.close = amp_dev_close;

    c5_dev->amp_dev.set_input_devices = NULL;
    c5_dev->amp_dev.set_output_devices = NULL;
    c5_dev->amp_dev.enable_input_devices = NULL;
    c5_dev->amp_dev.enable_output_devices = amp_enable_output_devices;
    c5_dev->amp_dev.set_mode = amp_set_mode;
    c5_dev->amp_dev.output_stream_start = NULL;
    c5_dev->amp_dev.input_stream_start = NULL;
    c5_dev->amp_dev.output_stream_standby = NULL;
    c5_dev->amp_dev.input_stream_standby = NULL;

    c5_dev->current_mode = AUDIO_MODE_NORMAL;
    c5_dev->enable = 0;

    *device = (hw_device_t *) c5_dev;

    return 0;
}

static struct hw_module_methods_t hal_module_methods = {
    .open = amp_module_open,
};

amplifier_module_t HAL_MODULE_INFO_SYM = {
    .common = {
        .tag = HARDWARE_MODULE_TAG,
        .module_api_version = AMPLIFIER_MODULE_API_VERSION_0_1,
        .hal_api_version = HARDWARE_HAL_API_VERSION,
        .id = AMPLIFIER_HARDWARE_MODULE_ID,
        .name = "C5 audio amplifier HAL",
        .author = "The LineageOS Open Source Project",
        .methods = &hal_module_methods,
    },
};
