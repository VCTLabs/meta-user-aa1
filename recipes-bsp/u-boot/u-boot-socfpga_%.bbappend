FILESEXTRAPATHS:prepend := "${THISDIR}/files/:"
FILESEXTRAPATHS:prepend:me-aa1-270-2i2-d11e-nfx3 := "${THISDIR}/me-aa1-270-2i2-d11e-nfx3:"

inherit deploy

do_compile[deptask] = "do_deploy"

SRC_URI:append:me-aa1-270-2i2-d11e-nfx3 = " file://socfpga_enclustra_mercury_st1.dtsi"

do_add_enclustra_files:append:me-aa1-270-2i2-d11e-nfx3() {
    cp ${WORKDIR}/socfpga_enclustra_mercury_st1.dtsi ${S}/arch/arm/dts
}

SRC_URI:append = " file://enclustra-user.dts"
