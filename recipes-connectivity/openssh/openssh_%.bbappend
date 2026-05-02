do_install:append () {
    sed -i '/HostKey/d' ${D}${sysconfdir}/ssh/sshd_config_readonly
    sed -i '/host_ecdsa_key/d' ${D}${sysconfdir}/ssh/sshd_config

    echo "HostKey /etc/ssh/ssh_host_rsa_key" >> ${D}${sysconfdir}/ssh/sshd_config_readonly
    echo "HostKey /etc/ssh/ssh_host_ed25519_key" >> ${D}${sysconfdir}/ssh/sshd_config_readonly

    sed -i -e "s|#HostKey|HostKey|g" ${D}${sysconfdir}/ssh/sshd_config
}

RDEPENDS:${PN} += "ssh-pregen-hostkeys"
