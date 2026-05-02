SUMMARY = "Pre-generated external host keys"

DESCRIPTION = "Use provided script to generate local host keys for yocto images."

SRC_URI = " \
           file://ssh_host_rsa_key \
           file://ssh_host_rsa_key.pub \
           file://ssh_host_ed25519_key \
           file://ssh_host_ed25519_key.pub \
          "

# SRC_URI:append = "file://dropbear_rsa_host_key file://dropbear_ed25519_host_key"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

INHIBIT_DEFAULT_DEPS = "1"

do_install () {
    #install -d ${D}${sysconfdir}/dropbear
    #install ${WORKDIR}/dropbear_*_host_key -m 0600 ${D}${sysconfdir}/dropbear/

    install -d ${D}${sysconfdir}/ssh
    install ${WORKDIR}/ssh_host_*_key* ${D}${sysconfdir}/ssh/
    chmod 0600 ${D}${sysconfdir}/ssh/*
    chmod 0644 ${D}${sysconfdir}/ssh/*.pub
}
