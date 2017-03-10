# Inherit some common CM stuff.
$(call inherit-product, vendor/cm/config/common_full_phone.mk)
$(call inherit-product, $(SRC_TARGET_DIR)/product/full_base_telephony.mk)
$(call inherit-product, device/samsung/c5lte/c5lte.mk)

PRODUCT_NAME := lineage_c5lte
BOARD_VENDOR := samsung
PRODUCT_DEVICE := c5lte

PRODUCT_MANUFACTURER := samsung
PRODUCT_MODEL := SM-C5000

PRODUCT_BRAND := samsung
TARGET_VENDOR := samsung
TARGET_VENDOR_PRODUCT_NAME := C5
TARGET_VENDOR_DEVICE_NAME := c5lte
