DESCRIPTION = "DEVEL minimal image for mmc"

require devel-common.inc

IMAGE_LINGUAS = " "

LICENSE = "MIT"

inherit core-image

IMAGE_NAME_SUFFIX = ""

IMAGE_INSTALL:append = "\
    ${CORE_IMAGE_EXTRA_INSTALL} \
    ${@bb.utils.contains('UBOOT_CONFIG', 'qspi', '', 'resize-helper', d)} \
"

WKS_FILE ??= "devel-image-minimal.wks"

PACKAGE_ARCH = "${MACHINE_ARCH}"
