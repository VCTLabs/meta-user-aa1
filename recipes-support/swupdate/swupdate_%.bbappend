FILESEXTRAPATHS:append := "${THISDIR}/${PN}:"

PACKAGECONFIG_CONFARGS = ""

SRC_URI += " \
    file://09-swupdate-args \
    file://swupdate.cfg \
    ${@bb.utils.contains('INIT_MANAGER','systemd','file://systemd.cfg','',d)} \
    "

RDEPENDS:${PN} += "u-boot-fw-utils libgcc e2fsprogs-resize2fs e2fsprogs-tune2fs util-linux"

UNPACKDIR = "${WORKDIR}"

do_configure:prepend() {
    # fix root home directory in sysv init script
    sed -i -e "s|/home/root/|${ROOT_HOME}/|" ${WORKDIR}/swupdate
}

do_install:append() {
    install -d ${D}${sysconfdir}/swupdate/conf.d/
    install -m 0644 ${WORKDIR}/09-swupdate-args ${D}${sysconfdir}/swupdate/conf.d/
    sed -i "s|@@MACHINE@@|${MACHINE}|g" ${D}${sysconfdir}/swupdate/conf.d/09-swupdate-args

    echo "${MACHINE} 1.0" > ${D}${sysconfdir}/hwrevision

    install -d ${D}${sysconfdir}
    install -m 644 ${WORKDIR}/swupdate.cfg ${D}${sysconfdir}/
    install -m 644 ${DEPLOY_DIR_IMAGE}/${SWU_KEY_DIR}/swu_public.pem ${D}${sysconfdir}/${PN}/
    sed -i 's|@@PUBKEY@@|${SWU_PUB_KEY}|g' ${D}${sysconfdir}/swupdate.cfg
    sed -i 's|@@DEVICE@@|${DEVICE_TYPE}|g' ${D}${sysconfdir}/swupdate.cfg
    sed -i 's|@@CURRENT_VERSION@@|${DISTRO_VERSION}|g' ${D}${sysconfdir}/swupdate.cfg
    # FIXME get a suitable version for the above

    if ${@bb.utils.contains('INIT_MANAGER','systemd','false','true',d)}; then
        # fix root home directory in sysv init script
        sed -i -e "s|/home/root/|${ROOT_HOME}/|" ${D}${sysconfdir}/init.d/swupdate
    fi
}
