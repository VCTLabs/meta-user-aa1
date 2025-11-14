require devel-common.inc

DESCRIPTION = "DEVEL data partition"

# allowed to build for baseboard and user machines
COMPATIBLE_MACHINE = "|me-aa1-270-2i2-d11e-nfx3|me-st1-generic"

IMAGE_LINGUAS = " "

LICENSE = "MIT"

inherit core-image

IMAGE_INSTALL:append = "\
    ${CORE_IMAGE_EXTRA_INSTALL} \
"

WKS_FILE ?= "devel-image-data.wks"

create_data_dir () {
    #!/bin/sh -e
    # create non-volatile rw partition mount point
    mkdir -p ${IMAGE_ROOTFS}/data
    # uncomment data mount options if using sysvinit
    if [ "${VIRTUAL-RUNTIME_init_manager}" != "systemd" ]; then
        sed -i -e "s|##||" ${IMAGE_ROOTFS}/etc/fstab
    fi
}

ROOTFS_POSTPROCESS_COMMAND += "create_data_dir;"
