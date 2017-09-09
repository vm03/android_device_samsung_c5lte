TARGET_RECOVERY_FSTAB := device/samsung/c5lte/rootdir/etc/fstab.full
ifeq ($(WITH_TWRP),true)
RECOVERY_VARIANT := twrp
TW_THEME := portrait_hdpi
RECOVERY_SDCARD_ON_DATA := true
#TW_TARGET_USES_QCOM_BSP := true
#TW_INCLUDE_CRYPTO := true
TARGET_RECOVERY_QCOM_RTC_FIX := true
RECOVERY_GRAPHICS_USE_LINELENGTH := true
TWHAVE_SELINUX := true
TW_BRIGHTNESS_PATH := /sys/class/leds/lcd-backlight/brightness
TW_CUSTOM_CPU_TEMP_PATH := /sys/class/thermal/thermal_zone4/temp
TW_USE_TOOLBOX := true
endif
