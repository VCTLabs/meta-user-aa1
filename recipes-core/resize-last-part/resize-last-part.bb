DESCRIPTION = "Resize last partition init script"
LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/GPL-2.0-or-later;md5=fed54355545ffd980b814dab4a3b312c"

SRC_URI = "file://resize-last-part.init"

inherit update-rc.d

INITSCRIPT_NAME = "resize-last-part"
INITSCRIPT_PARAMS = "start 99 S ."

RDEPENDS:${PN} = "e2fsprogs-resize2fs parted"

do_install () {
        install -d ${D}${sysconfdir}/init.d
        install -m 0755 ${WORKDIR}/resize-last-part.init ${D}${sysconfdir}/init.d/${PN}
}

PACKAGE_ARCH = "${MACHINE_ARCH}"
