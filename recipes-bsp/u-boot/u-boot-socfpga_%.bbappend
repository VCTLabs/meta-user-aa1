FILESEXTRAPATHS:prepend := "${THISDIR}/files/:"
FILESEXTRAPATHS:prepend:refdes-me-aa1-270-2i2-d11e-nfx3-st1 := "${THISDIR}/refdes-me-aa1-270-2i2-d11e-nfx3-st1:"

inherit deploy

do_compile[deptask] = "do_deploy"

SRC_URI:append:me-st1-generic = " file://socfpga_enclustra_mercury_st1.dtsi"

do_add_enclustra_files:append:me-st1-generic() {
    cp ${WORKDIR}/socfpga_enclustra_mercury_st1.dtsi ${S}/arch/arm/dts
}

SRC_URI:append = " file://enclustra-user.dts"
