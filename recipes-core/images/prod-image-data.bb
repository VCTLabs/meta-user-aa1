require devel-common.inc

DESCRIPTION = "PROD dual-root partitions with swupdate for mmc"

# allowed to build for baseboard and user machines
COMPATIBLE_MACHINE = "|me-aa1-270-2i2-d11e-nfx3|me-st1-generic"

IMAGE_LINGUAS = " "

LICENSE = "MIT"

inherit core-image

SWU_PKGS = " \
    swupdate \
    swupdate-usb \
    swupdate-www \
    u-boot-fw-utils \
    swu-ab-validation \
"

IMAGE_NAME_SUFFIX = ""

IMAGE_INSTALL:append = "\
    ${CORE_IMAGE_EXTRA_INSTALL} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'swupdate', '${SWU_PKGS}', '', d)} \
"

IMAGE_OVERHEAD_FACTOR = "1.0"
IMAGE_ROOTFS_EXTRA_SPACE = "0"
IMAGE_ROOTFS_SIZE = "262144"

WKS_FILE = "prod-image-data.wks"

add_boot_to_fstab() {
    #!/bin/sh -e
    echo 'LABEL=boot   /boot            vfat      defaults              0  0' \
        >> ${IMAGE_ROOTFS}/etc/fstab
}

set_image_props () {
    #!/bin/sh -e
    # this is part of the device identity in swupdate.cfg
    sed -i 's|@@IMAGE@@|${IMAGE_BASENAME}|g' ${IMAGE_ROOTFS}${sysconfdir}/swupdate.cfg
}

ROOTFS_POSTPROCESS_COMMAND:append = " set_image_props;"
ROOTFS_POSTPROCESS_COMMAND:append = " ${@bb.utils.contains_any('UBOOT_CONFIG', 'emmc sdmmc', 'add_boot_to_fstab;', '', d)}"
