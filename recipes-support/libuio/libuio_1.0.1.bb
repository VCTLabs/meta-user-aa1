SUMMARY = "Simple libuio application"
SECTION = "libs"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "git://github.com/digilent/libuio.git;protocol=https"

SRCREV = "523d29e5120fe53ff5ec15c9903a31e89c79fbee"

PV .= "+git${SRCPV}"

S = "${WORKDIR}/git"

do_install() {
	install -d ${D}${libdir}
	oe_libinstall -so libuio ${D}${libdir}

	install -d ${D}${includedir}
	install -m 0644 ${S}/libuio.h ${D}${includedir}

	install -m 0644 libuio.so ${D}${libdir}
}

FILES_${PN} = "${libdir} ${includedir}"
