/*
 * Copyright (C) 2015 The CyanogenMod Project
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

#define LOG_TAG "SAM_SHIM1"
#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>

#include <utils/Log.h>

//library on-load and on-unload handlers (to help us set things up and tear them down)
void direct_mixer_set_value(void) __attribute__((constructor));

/*
 * FUNCTION: direct_mixer_set_value()
 * USE:
 * NOTES:
 */
void direct_mixer_set_value(void)
{
    ALOGV("direct_mixer_set_value...");
}
