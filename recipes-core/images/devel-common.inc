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

ROOTFS_POSTPROCESS_COMMAND:append = " remove_sysvinit_files;"

remove_sysvinit_files() {
    #!/bin/sh -e
    # Remove init scripts from /etc/init.d directory

    if [ "${@bb.utils.contains('INIT_MANAGER', 'systemd', 'yes', 'no', d)}" = "yes" ]; then
        rm -f ${IMAGE_ROOTFS}${sysconfdir}/init.d/resize-last
    fi
}
