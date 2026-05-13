require initramfs-common.inc

# alternative to debug-tweaks or empty-root-password
#require initramfs-image-harden.inc

DESCRIPTION = "initramfs devel image"
LICENSE = "MIT"

LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

# try to avoid dependency loops
EXTRA_IMAGEDEPENDS = ""

# Do not pollute the initrd image with the usual rootfs features
IMAGE_FEATURES = "${@bb.utils.contains('EXTRA_IMAGE_FEATURES', 'debug-tweaks', 'empty-root-password allow-empty-password allow-root-login', '', d)}"

IMAGE_INSTALL:append = " \
    packagegroup-core-ssh-openssh \
    ${CORE_IMAGE_EXTRA_INSTALL} \
"

IMAGE_LINGUAS = " "

inherit image

export IMAGE_BASENAME = "devel-initramfs"
IMAGE_NAME_SUFFIX ?= ""

# don't actually generate an image, just the artifacts needed for one
IMAGE_FSTYPES = "${INITRAMFS_FSTYPES}"
IMAGE_FSTYPES:remove = " wic wic.* wic.xz wic.bmap tar.xz ext4"

IMAGE_OVERHEAD_FACTOR = "1.0"

# Don't allow the initramfs to contain a kernel
PACKAGE_EXCLUDE += "kernel-image-* resize-helper"

IMAGE_ROOTFS_SIZE = "16384"
IMAGE_ROOTFS_EXTRA_SPACE = "${@bb.utils.contains("DISTRO_FEATURES", "systemd", "4096", "", d)}"
BAD_RECOMMENDATIONS += "busybox-syslog"

PACKAGE_ARCH = "${MACHINE_ARCH}"

ROOTFS_POSTPROCESS_COMMAND += "remove_file_cruft;"

remove_file_cruft() {
    # boot dir should be empty in ramdisk root
    rm -rf ${IMAGE_ROOTFS}/boot/*
}
