# Inherit some common CM stuff.
$(call inherit-product, vendor/lineage/config/common_full_phone.mk)
$(call inherit-product, $(SRC_TARGET_DIR)/product/core_64_bit.mk)
$(call inherit-product, $(SRC_TARGET_DIR)/product/full_base_telephony.mk)
$(call inherit-product, $(SRC_TARGET_DIR)/product/product_launched_with_m.mk)
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

# Use the latest approved GMS identifiers
PRODUCT_BUILD_PROP_OVERRIDES += \
    PRODUCT_NAME=c5ltezh \
    PRIVATE_BUILD_DESC="c5ltezh-user 7.0 NRD90M C5000ZHU1BRA1 release-keys"

BUILD_FINGERPRINT := "samsung/c5ltezh/c5ltechn:7.0/NRD90M/C5000ZHU1BRA1:user/release-keys"
