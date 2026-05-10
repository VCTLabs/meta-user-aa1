SUMMARY = "Rootfs disk resize-helper"
DESCRIPTION = "Resize root filesystem to fit available disk space"
SECTION = "admin"

LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/BSD-2-Clause;md5=cb641bc04cda31daea161b1bc15da69f"

inherit features_check systemd update-rc.d

CONFLICT_DISTRO_FEATURES = "rauc"

RDEPENDS:${PN} += "e2fsprogs-resize2fs gptfdisk parted util-linux udev"

SRC_URI = " \
    file://resize-helper.init \
    file://resize-helper.service \
    file://resize-helper \
"

S = "${WORKDIR}"

do_install () {
	install -d ${D}${sbindir}
	install -m 0755 ${S}/resize-helper ${D}${sbindir}

    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/resize-helper.init ${D}${sysconfdir}/init.d/resize-helper
	install -d ${D}${systemd_system_unitdir}
	install -m 0644 ${S}/resize-helper.service ${D}${systemd_system_unitdir}
}

INITSCRIPT_NAME = "resize-helper"
INITSCRIPT_PARAMS = "start 10 S ."

SYSTEMD_SERVICE:${PN} = "resize-helper.service"
SYSTEMD_AUTO_ENABLE:${PN} = "enable"
