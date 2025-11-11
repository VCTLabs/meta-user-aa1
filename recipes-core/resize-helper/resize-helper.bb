SUMMARY = "Resize the last partition, root or not"
DESCRIPTION = "Resize last filesystem to fit available disk space"
SECTION = "admin"

LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://${WORKDIR}/resize-helper;beginline=1;endline=24;md5=c86f62e2fddb47a7dc1f398b2aff4912"

SRC_URI = " \
        file://resize-last.init
        file://resize-helper.service \
        file://resize-helper \
"

inherit features_check systemd update-rc.d

RDEPENDS:${PN} += "e2fsprogs-resize2fs gptfdisk parted util-linux udev"

do_install () {
        install -d ${D}${sysconfdir}/init.d
        install -m 0755 ${WORKDIR}/resize-last.init ${D}${sysconfdir}/init.d/resize-last
        install -d ${D}${systemd_system_unitdir}
        install -m 0644 ${WORKDIR}/resize-helper.service ${D}${systemd_system_unitdir}
        install -d ${D}${sbindir}
        install -m 0755 ${WORKDIR}/resize-helper ${D}${sbindir}
}

PACKAGE_ARCH = "${MACHINE_ARCH}"
SYSTEMD_SERVICE:${PN} = "resize-helper.service"
FILES:${PN} += "${bindir}"
