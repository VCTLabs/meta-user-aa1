# include file for devel images

IMAGE_INSTALL = "packagegroup-core-boot"

IMAGE_INSTALL:append = "\
    kernel-modules \
    procps \
    i2c-tools \
    mtd-utils \
    sysfsutils \
    spitools \
    devmem2 \
    iperf3 \
    usbutils \
    hdparm \
    phytool \
    memtester \
    e2fsprogs \
    e2fsprogs-resize2fs \
    libuio-ng \
"

IMAGE_INSTALL:append = "${@bb.utils.contains('UBOOT_CONFIG', 'qspi', '', ' resize-helper', d)}"
