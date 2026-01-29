# include file for devel initramfs images

# Do not pollute the initrd image with rootfs features
IMAGE_FEATURES = " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'security', '', 'debug-tweaks', d)} \
"

# try to avoid dependency loops
EXTRA_IMAGEDEPENDS = ""

BASE_DEVEL_INSTALL = "\
    base-files \
    base-passwd \
    busybox \
    kernel-modules \
    procps \
    i2c-tools \
    mtd-utils \
    devmem2 \
    iperf3 \
    usbutils \
    hdparm \
    phytool \
    memtester \
    libuio-ng \
"

IMAGE_LINGUAS = ""

IMAGE_FSTYPES = "${INITRAMFS_FSTYPES}"
IMAGE_FSTYPES:remove = " wic wic.* wic.xz wic.bmap tar.xz"
