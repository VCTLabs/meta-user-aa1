require devel-common.inc

DESCRIPTION = "PROD data partition with swupdate for mmc"

# allowed to build for baseboard and user machines
COMPATIBLE_MACHINE = "|me-aa1-270-2i2-d11e-nfx3|me-st1-generic"

IMAGE_LINGUAS = " "

LICENSE = "MIT"

inherit core-image

SWU_PKGS = "swupdate swupdate-usb u-boot-fw-utils swu-ab-validation"

IMAGE_NAME_SUFFIX = ""

IMAGE_INSTALL:append = "\
    ${CORE_IMAGE_EXTRA_INSTALL} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'swupdate', '${SWU_PKGS}', '', d)} \
    ${@bb.utils.contains_any('UBOOT_CONFIG', 'emmc sdmmc', 'resize-helper', '', d)} \
"

WKS_FILE ?= "prod-image-data.wks"

setup_data_dir () {
    #!/bin/sh -e
    # create non-volatile rw partition mount point
    mkdir -p ${IMAGE_ROOTFS}/data
    FSTAB="LABEL=data           /data                auto       defaults              0  1"
    echo ${FSTAB} >> ${IMAGE_ROOTFS}/etc/fstab
}

ROOTFS_POSTPROCESS_COMMAND += "setup_data_dir;"
