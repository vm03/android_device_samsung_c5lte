# IRSC
PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/configs/sec_config:system/etc/sec_config

# Permissions
PRODUCT_COPY_FILES += \
    frameworks/native/data/etc/android.hardware.telephony.gsm.xml:system/etc/permissions/android.hardware.telephony.gsm.xml \
    frameworks/native/data/etc/android.hardware.telephony.cdma.xml:system/etc/permissions/android.hardware.telephony.cdma.xml \
    frameworks/native/data/etc/android.software.sip.voip.xml:system/etc/permissions/android.software.sip.voip.xml

# Properties
PRODUCT_PROPERTY_OVERRIDES += \
    persist.data.qmi.adb_logmask=0 \
    persist.radio.apm_sim_not_pwdn=1 \
    ro.telephony.call_ring.multiple=false \
    ro.use_data_netmgrd=true \
    ro.ril.telephony.mqanelements=6 \
    rild.libpath=/system/lib/libsec-ril.so \
    rild.libpath2=/system/lib/libsec-ril-dsds.so \
    persist.radio.multisim.config=dsds \
    persist.cne.feature=0 \
    ro.multisim.simslotcount=2 \
    ro.telephony.ril_class=C5RIL

# RIL
PRODUCT_PACKAGES += \
    librmnetctl \
    libxml2


PRODUCT_PACKAGES += \
    telephony-ext

PRODUCT_BOOT_JARS += \
    telephony-ext
