# include file for devel images

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
    libuio-ng \
"
