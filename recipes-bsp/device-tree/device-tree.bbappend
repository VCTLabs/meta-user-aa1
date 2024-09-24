FILESEXTRAPATHS:prepend := "${THISDIR}/files/:"
FILESEXTRAPATHS:prepend:me-aa1-270-2i2-d11e-nfx3 := "${THISDIR}/me-aa1-270-2i2-d11e-nfx3:"

SRC_URI:append = " file://enclustra-user.dts"
SRC_URI:append:me-st1-generic = " file://socfpga_enclustra_mercury_st1.dtsi"

COMPATIBLE_MACHINE += "|me-pe1-generic|me-pe3-generic|me-st1-generic"
