# IRSC
PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/configs/sec_config:system/etc/sec_config

# Permissions
PRODUCT_COPY_FILES += \
    frameworks/native/data/etc/android.hardware.telephony.gsm.xml:system/vendor/etc/permissions/android.hardware.telephony.gsm.xml \
    frameworks/native/data/etc/android.hardware.telephony.cdma.xml:system/vendor/etc/permissions/android.hardware.telephony.cdma.xml \
    frameworks/native/data/etc/android.software.sip.voip.xml:system/vendor/etc/permissions/android.software.sip.voip.xml

# Properties
PRODUCT_PROPERTY_OVERRIDES += \
    persist.radio.multisim.config=dsds \
    persist.cne.feature=0 \
    ro.use_data_netmgrd=true \
    persist.data.netmgrd.qos.enable=true \
    persist.data.mode=concurrent \
    DEVICE_PROVISIONED=1 \
    ril.subscription.types=NV,RUIM \
    rild.libpath=/system/vendor/lib64/libril-qc-qmi-1.so \
    ro.telephony.default_cdma_sub=0 \
    ro.telephony.default_network=9 \
    telephony.lteOnCdmaDevice=1 \
    persist.radio.apm_sim_not_pwdn=1 \
    persist.radio.custom_ecc=1 \
    persist.radio.sib16_support=1

# RIL
PRODUCT_PACKAGES += \
    librmnetctl \
    libxml2

PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/configs/data/netmgr_config.xml:system/etc/data/netmgr_config.xml \
    $(LOCAL_PATH)/configs/data/qmi_config.xml:system/etc/data/qmi_config.xml \
    $(LOCAL_PATH)/configs/data/dsi_config.xml:system/etc/data/dsi_config.xml

PRODUCT_PACKAGES += \
    telephony-ext

PRODUCT_BOOT_JARS += \
    telephony-ext
