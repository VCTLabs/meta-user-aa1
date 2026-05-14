# include file for devel images
# packages added to CORE_IMAGE_EXTRA_INSTALL are installed in every image

IMAGE_FSTYPES:append = " ext4.gz"

IMAGE_INSTALL = "\
    packagegroup-core-boot \
    kernel-modules \
    bash-completion-extra \
    iproute2 \
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
    dosfstools \
    libuio-ng \
"

ROOTFS_POSTPROCESS_COMMAND:append = " update_boot_fitimage; remove_sysvinit_files; set_image_name;"

update_boot_fitimage() {
    #!/bin/sh -e
    # workaround for packaged fitImage kernel lagging behind the deployed
    # fitImage kernel signature

    if echo ${KERNEL_IMAGETYPES} | grep -wq "fitImage"; then
        install -m 0644 ${DEPLOY_DIR_IMAGE}/fitImage ${IMAGE_ROOTFS}/boot/
    fi
}

remove_sysvinit_files() {
    #!/bin/sh -e
    # Remove init scripts from /etc/init.d directory

    if [ "${@bb.utils.contains('INIT_MANAGER', 'systemd', 'yes', 'no', d)}" = "yes" ]; then
        rm -f ${IMAGE_ROOTFS}${sysconfdir}/init.d/resize-helper
        rm -f ${IMAGE_ROOTFS}${sysconfdir}/init.d/sshd
    fi
}

set_image_name () {
    #!/bin/sh -e
    # this is part of the device identity in swupdate.cfg
    sed -i 's|@@IMAGE@@|${IMAGE_BASENAME}|g' ${IMAGE_ROOTFS}${sysconfdir}/swupdate.cfg
}

do_rootfs[depends] += "virtual/kernel:do_deploy"
