FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append:me-aa1-270-2i2-d11e-nfx3 = " file://add-uio.cfg"
SRC_URI:append:qemuarm = " file://qemuarm_defconfig"

do_configure:prepend:qemuarm() {
    cp ${WORKDIR}/qemuarm_defconfig ${S}/arch/arm/configs/
}
