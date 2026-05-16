DESCRIPTION = "DEVEL minimal image for mmc"

require devel-common.inc

IMAGE_LINGUAS = " "

LICENSE = "MIT"

inherit core-image

IMAGE_INSTALL:append = "\
    ${CORE_IMAGE_EXTRA_INSTALL} \
    ${@bb.utils.contains_any('UBOOT_CONFIG', 'emmc sdmmc', 'resize-helper', '', d)} \
"

WKS_FILE ??= "devel-image-minimal.wks"

PACKAGE_ARCH = "${MACHINE_ARCH}"
