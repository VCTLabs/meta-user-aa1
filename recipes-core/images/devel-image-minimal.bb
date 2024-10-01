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
    u-boot-script-vctlabs \
    ${CORE_IMAGE_EXTRA_INSTALL} \
"

WKS_FILE ??= "devel-image-minimal.wks"
