require devel-common.inc

DESCRIPTION = "DEVEL minimal"

# allowed to build for baseboard and user machines
COMPATIBLE_MACHINE = "|me-aa1-270-2i2-d11e-nfx3|me-st1-generic"

IMAGE_LINGUAS = " "

LICENSE = "MIT"

inherit core-image

IMAGE_INSTALL:append = "\
    ${CORE_IMAGE_EXTRA_INSTALL} \
"

WKS_FILE ??= "devel-image-data.wks"

create_data_dir () {
    #!/bin/sh -e

    # create non-volatile rw partition mount point
    mkdir -p ${IMAGE_ROOTFS}/data
    # uncomment data mount options
    sed -i -e "s|##||" ${IMAGE_ROOTFS}/etc/fstab
}

ROOTFS_POSTPROCESS_COMMAND += "create_data_dir;"
