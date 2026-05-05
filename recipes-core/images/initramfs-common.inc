# include file for initramfs images
# packages added to CORE_IMAGE_EXTRA_INSTALL are installed in every image

IMAGE_INSTALL = "\
    packagegroup-core-boot \
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
    libuio-ng \
"

ROOTFS_POSTPROCESS_COMMAND:append = " remove_sysvinit_files;"

remove_sysvinit_files() {
    #!/bin/sh -e
    # Remove init scripts from /etc/init.d directory

    if [ "${@bb.utils.contains('INIT_MANAGER', 'systemd', 'yes', 'no', d)}" = "yes" ]; then
        rm -f ${IMAGE_ROOTFS}${sysconfdir}/init.d/resize-last
        rm -f ${IMAGE_ROOTFS}${sysconfdir}/init.d/sshd
    fi
}
