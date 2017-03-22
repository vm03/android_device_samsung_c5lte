LOCAL_PATH := $(call my-dir)

# camera

include $(CLEAR_VARS)

LOCAL_SRC_FILES := \
    audio_shim.c

LOCAL_SHARED_LIBRARIES := liblog libcutils libbinder libutils

LOCAL_MODULE := lib_audio_shim
LOCAL_MODULE_CLASS := SHARED_LIBRARIES

include $(BUILD_SHARED_LIBRARY)

