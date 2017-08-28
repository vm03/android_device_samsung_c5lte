# Shims
TARGET_LD_SHIM_LIBS := \
    /system/lib/libcamera_client.so|libcamera_parameters_shim.so \
    /system/vendor/lib64/libflp.so|libshims_flp.so \
    /system/vendor/lib64/libizat_core.so|libshims_get_process_name.so \
    /system/vendor/lib/libflp.so|libshims_flp.so
