FILESEXTRAPATHS:append := "${THISDIR}/${PN}:"

PACKAGECONFIG_CONFARGS = ""

SRC_URI += " \
    file://09-swupdate-args \
    file://swupdate.cfg \
    ${@bb.utils.contains_any('SWUPDATE_SIGNING', 'RSA CMS', 'file://signing.cfg', '', d)} \
    ${@bb.utils.contains('SWUPDATE_ENCRYPTION','1','file://encryption.cfg','',d)} \
    ${@bb.utils.contains('INIT_MANAGER','systemd','file://systemd.cfg','',d)} \
    "

RDEPENDS:${PN} += "u-boot-fw-utils libgcc e2fsprogs-resize2fs e2fsprogs-tune2fs util-linux"

UNPACKDIR = "${WORKDIR}"

wwwdir = "${SWU_WWW_DOC_ROOT}"

do_configure:prepend() {
    # fix root home directory in sysv init script
    sed -i -e "s|/home/root/|${ROOT_HOME}/|" ${WORKDIR}/swupdate
    sed -i "s|@@SWU_WWW_DOC_ROOT@@|${SWU_WWW_DOC_ROOT}|" ${WORKDIR}/09-swupdate-args ${WORKDIR}/swupdate.cfg
    sed -i "s|@@SWU_WWW_HTTP_PORT@@|${SWU_WWW_HTTP_PORT}|" ${WORKDIR}/09-swupdate-args ${WORKDIR}/swupdate.cfg
    sed -i "s|/www|${SWU_WWW_DOC_ROOT}|" ${WORKDIR}/10-mongoose-args
    sed -i "s|8080|${SWU_WWW_HTTP_PORT}|" ${WORKDIR}/10-mongoose-args

    if [ "${@bb.utils.contains('SWUPDATE_ENCRYPTION', '0', 'yes', 'no', d)}" = "yes" ]; then
        sed -i /AESKEY/d ${WORKDIR}/swupdate.cfg
    fi

    if [ "${@bb.utils.contains_any('SWUPDATE_SIGNING', 'RSA CMS', 'yes', 'no', d)}" = "no" ]; then
        sed -i /PUBKEY/d ${WORKDIR}/swupdate.cfg
    fi
}

do_install:append() {
    install -d ${D}${sysconfdir}/swupdate/conf.d/
    install -m 0644 ${WORKDIR}/09-swupdate-args ${D}${sysconfdir}/swupdate/conf.d/
    sed -i "s|@@MACHINE@@|${MACHINE}|g" ${D}${sysconfdir}/swupdate/conf.d/09-swupdate-args

    echo "${MACHINE} 1.0" > ${D}${sysconfdir}/hwrevision

    install -d ${D}${sysconfdir}
    install -m 644 ${WORKDIR}/swupdate.cfg ${D}${sysconfdir}/
    install -m 644 ${SWUPDATE_PUBLIC_KEY} ${D}${sysconfdir}/${PN}/

    # reformat aes file for swupdate: key val and itv val separated by a space
    KEY=$(sed  "s/.*=/ /g" ${SWUPDATE_AES_FILE})
    echo $KEY > ${D}${SWU_AES_FILE}

    sed -i 's|@@PUBKEY@@|${SWU_PUB_KEY}|g' ${D}${sysconfdir}/swupdate.cfg
    sed -i 's|@@AESKEY@@|${SWU_AES_FILE}|g' ${D}${sysconfdir}/swupdate.cfg
    sed -i 's|@@DEVICE@@|${DEVICE_TYPE}|g' ${D}${sysconfdir}/swupdate.cfg
    sed -i 's|@@CURRENT_VERSION@@|${DISTRO_VERSION}|g' ${D}${sysconfdir}/swupdate.cfg
    # FIXME get a suitable version for the above

    if ${@bb.utils.contains('INIT_MANAGER','systemd','false','true',d)}; then
        # fix root home directory in sysv init script
        sed -i -e "s|/home/root/|${ROOT_HOME}/|" ${D}${sysconfdir}/init.d/swupdate
    fi
}
