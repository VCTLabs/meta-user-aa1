SUMMARY = "Resize the last partition, root or not"
DESCRIPTION = "Resize last filesystem to fit available disk space"
SECTION = "admin"

LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://${WORKDIR}/resize-last.init;beginline=2;endline=25;md5=3b586a08e84e53fa84d102e9aedd8ae1"

SRC_URI = " \
        file://resize-last.init \
        file://resize-helper.service \
        file://resize-helper \
"

inherit systemd update-rc.d

RDEPENDS:${PN} += "e2fsprogs-resize2fs gptfdisk parted util-linux udev"

do_install () {
        install -d ${D}${sysconfdir}/init.d
        install -m 0755 ${WORKDIR}/resize-last.init ${D}${sysconfdir}/init.d/resize-last
        install -d ${D}${systemd_system_unitdir}
        install -m 0644 ${WORKDIR}/resize-helper.service ${D}${systemd_system_unitdir}
        install -d ${D}${sbindir}
        install -m 0755 ${WORKDIR}/resize-helper ${D}${sbindir}
}

INITSCRIPT_NAME = "resize-last"
INITSCRIPT_PARAMS = "start 10 S ."

PACKAGE_ARCH = "${MACHINE_ARCH}"
SYSTEMD_SERVICE:${PN} = "resize-helper.service"
FILES:${PN} += "${bindir}"
