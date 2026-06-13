require devel-common.inc

DESCRIPTION = "DEVEL data partition for sdmmc"

# allowed to build for generic baseboard and user machines
COMPATIBLE_MACHINE = "|me-aa1-270-2i2-d11e-nfx3|me-st1-generic"

IMAGE_LINGUAS = " "

LICENSE = "MIT"

inherit core-image

#
IMAGE_INSTALL:append = "\
    bash-completion-extra \
    iproute2 \
    ${CORE_IMAGE_EXTRA_INSTALL} \
    ${@bb.utils.contains('UBOOT_CONFIG', 'qspi', '', 'resize-helper', d)} \
"

WKS_FILE ?= "devel-image-data.wks"

create_data_dir () {
    #!/bin/sh -e
    # create non-volatile rw partition mount point
    mkdir -p ${IMAGE_ROOTFS}/data
}

ROOTFS_POSTPROCESS_COMMAND += "create_data_dir;"
