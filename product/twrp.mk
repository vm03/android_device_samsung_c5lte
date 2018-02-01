ifeq ($(WITH_TWRP),true)
PRODUCT_COPY_FILES += \
    device/samsung/c5lte/recovery/twrp.fstab:recovery/root/etc/twrp.fstab
endif
