# if the boot reaches this stage and passes all the checks, we
# disable the rollback and set the boot counter to zero

SUMMARY = "A/B update validation service"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit systemd

SRC_URI = " \
    file://swu-ab-validation.sh \
    file://swu-ab-validation.service \
"

S = "${WORKDIR}"

RDEPENDS:${PN} += "libubootenv-bin"

SYSTEMD_SERVICE:${PN} = "swu-ab-validation.service"
SYSTEMD_AUTO_ENABLE = "enable"

do_install() {
    install -d ${D}${sbindir}
    install -m 0755 ${WORKDIR}/swu-ab-validation.sh ${D}${sbindir}/swu-ab-validation.sh

    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/swu-ab-validation.service ${D}${systemd_system_unitdir}/swu-ab-validation.service
}

FILES:${PN} = " \
    ${sbindir}/swu-ab-validation.sh \
    ${systemd_system_unitdir}/swu-ab-validation.service \
"
