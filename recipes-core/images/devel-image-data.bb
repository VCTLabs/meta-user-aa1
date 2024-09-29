DESCRIPTION = "DEVEL minimal"

# allowed to build for baseboard and user machines
COMPATIBLE_MACHINE = "|me-aa1-270-2i2-d11e-nfx3|me-st1-generic"

IMAGE_LINGUAS = " "

LICENSE = "MIT"

inherit core-image

IMAGE_INSTALL = "packagegroup-core-boot"

IMAGE_INSTALL:append = "\
    procps \
    i2c-tools \
    mtd-utils \
    devmem2 \
    iperf3 \
    usbutils \
    hdparm \
    phytool \
    memtester \
    e2fsprogs \
    e2fsprogs-resize2fs \
    ${CORE_IMAGE_EXTRA_INSTALL} \
"

WKS_FILE ??= "devel-image-data.wks"

create_data_dir () {
    #!/bin/sh -e

    # create non-volatile rw partition mount point
    mkdir -p ${IMAGE_ROOTFS}/data
}

ROOTFS_POSTPROCESS_COMMAND += "create_data_dir;"
