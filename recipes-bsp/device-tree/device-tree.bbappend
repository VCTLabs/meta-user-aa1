FILESEXTRAPATHS:prepend := "${THISDIR}/files/:"
FILESEXTRAPATHS:prepend:me-aa1-270-2i2-d11e-nfx3 := "${THISDIR}/me-aa1-270-2i2-d11e-nfx3:"

COMPATIBLE_MACHINE += "|me-pe1-generic|me-pe3-generic|me-st1-generic"

SRC_URI:append:me-aa1-270-2i2-d11e-nfx3 = " \
    file://enclustra-user.dts \
"

SRC_URI:append = " \
    file://socfpga_enclustra_mercury_st1.dtsi \
"
