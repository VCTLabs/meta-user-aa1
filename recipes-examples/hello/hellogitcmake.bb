DESCRIPTION = "Example Hello World application for Yocto build Using git and Cmake."
SECTION = "examples"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3d7054b26bdd0f2f5fc6b2e53f28783d"

# this allows bad checksum with only a warning - use with caution
BB_STRICT_CHECKSUM = "0"
#SRCREV = "bcfb44b523de9a73bdb8316a2607343e75eb9e74"

BRANCH = "main"
SRC_URI = "git://github.com/VCTLabs/libUIO-Hello-World-Cmake.git;protocol=https;branch=${BRANCH} \
          "

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

inherit cmake gitpkgv pkgconfig

PKGV = "${GITPKGVTAG}"

DEPENDS += "libuio-ng"

EXTRA_OECMAKE = ""
