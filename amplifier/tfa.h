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
#ifndef _TFA_H_
#define _TFA_H_

extern int NXP_I2C_verbose;

typedef struct tfa {
    char buf1[107];
    int tfaMode;
    char buf2[16];
    int (*tfa_enable)(struct tfa* a1, int a2);
    int (*tfa_setvolumestep)(int volume1, int volume2);
    int (*tfa_setvolumeattenuation)(int volume1, int volume2);
    int (*tfa_getImpedance)(struct tfa* a1);
    int (*tfa_calibrateImpedance)(struct tfa* a1);
} tfa_t;

extern int tfa_device_open(tfa_t* a1);

#endif  //_TFA_H_
