# NFC
PRODUCT_PACKAGES += \
    NfcNci \
    Tag \
    nfc_nci.pn54x.default \
    com.android.nfc_extras

PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/nfc/libnfc-brcm.conf:system/etc/libnfc-brcm.conf \
    $(LOCAL_PATH)/nfc/libnfc-nxp_RF.conf:system/etc/libnfc-nxp_RF.conf \
    $(LOCAL_PATH)/nfc/libnfc-nxp.conf:system/etc/libnfc-nxp.conf \
    $(LOCAL_PATH)/nfc/nfcee_access.xml:system/etc/nfcee_access.xml \
    frameworks/native/data/etc/android.hardware.nfc.xml:system/vendor/etc/permissions/android.hardware.nfc.xml \
    frameworks/native/data/etc/android.hardware.nfc.hce.xml:system/vendor/etc/permissions/android.hardware.nfc.hce.xml \
    frameworks/native/data/etc/com.android.nfc_extras.xml:system/vendor/etc/permissions/com.android.nfc_extras.xml
