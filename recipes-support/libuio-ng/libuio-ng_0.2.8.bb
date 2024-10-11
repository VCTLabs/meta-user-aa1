SUMMARY = "Libuio - helper library for UIO subsystem"
SECTION = "base"
LICENSE = "LGPL-2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=4fbd65380cdd255951079008b364516c"

SRC_URI = "git://github.com/missinglinkelectronics/libuio;protocol=https"
SRC_URI += "file://0001-uio_find_by_name-_fix-spurious-failures-from-stale-e.patch"

inherit gettext autotools

SRCREV = "7c18f2b25ec2ba824c483ffa3a5bde16679a25bc"

PV .= "+git${SRCPV}"

S = "${WORKDIR}/git"

EXTRA_OECONF = "--without-glib"
export AUTOMAKE = "automake --foreign"

do_install:append() {
    # rename to be able to install in parallel with uictl for comparison
    for F in ${D}${bindir}/* ; do
        mv ${F} ${F}-ng
    done
}

PACKAGES += "${PN}-tools"

FILES_${PN} = "${libdir}"
FILES_${PN}-tools = "${bindir}"
