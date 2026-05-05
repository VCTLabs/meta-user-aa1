FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://00-systemd-conf.conf"

do_install:append() {
    install -d ${D}${sysconfdir}/systemd/journald.conf.d
    install -m 0644 ${WORKDIR}/00-systemd-conf.conf ${D}${sysconfdir}/systemd/journald.conf.d/
}

FILES:${PN} += "${sysconfdir}/systemd/journald.conf.d/00-systemd-conf.conf"

