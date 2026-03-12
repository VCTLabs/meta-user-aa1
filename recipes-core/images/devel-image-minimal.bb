DESCRIPTION = "DEVEL minimal image"

require devel-common.inc

# allowed to build for baseboard and user machines
COMPATIBLE_MACHINE = "|me-aa1-270-2i2-d11e-nfx3|me-st1-generic"

IMAGE_LINGUAS = " "

LICENSE = "MIT"

inherit core-image

IMAGE_INSTALL:append = "\
    ${CORE_IMAGE_EXTRA_INSTALL} \
    ${@bb.utils.contains('UBOOT_CONFIG', 'qspi', '', 'resize-helper', d)} \
"

WKS_FILE ??= "devel-image-minimal.wks"
