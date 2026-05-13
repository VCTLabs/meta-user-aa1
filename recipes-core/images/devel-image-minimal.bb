DESCRIPTION = "DEVEL minimal image for mmc"

require devel-common.inc

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

IMAGE_INSTALL:remove = "\
    ${@bb.utils.contains('WKS_FILE', 'mmc-ab-root-vfat-boot.wks', 'resize-helper', '', d)} \
"

WKS_FILE ??= "devel-image-minimal.wks"

PACKAGE_ARCH = "${MACHINE_ARCH}"
