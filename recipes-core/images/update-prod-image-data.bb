DESCRIPTION = "create swupdate package for prod-image-data"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit swupdate

SRC_URI = "\
    file://preinstall.sh \
    file://postinstall.sh \
    file://sw-description \
"

# images to build before building swupdate image
IMAGE_DEPENDS = "prod-image-data"

# images and files that will be included in the .swu image
SWUPDATE_IMAGES = "prod-image-data"

SWUPDATE_IMAGES_FSTYPES[prod-image-data] = ".ext4.gz"

SWUPDATE_IMAGES_ENCRYPTED[prod-image-data] = "1"
SWUPDATE_ENCRYPT_SWDESC ?= "1"
