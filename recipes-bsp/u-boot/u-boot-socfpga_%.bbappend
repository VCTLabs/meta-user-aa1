FILESEXTRAPATHS:prepend := "${THISDIR}/files/:"
FILESEXTRAPATHS:prepend:me-aa1-270-2i2-d11e-nfx3 := "${THISDIR}/me-aa1-270-2i2-d11e-nfx3:"

inherit deploy

do_compile[deptask] = "do_deploy"

SRC_URI:append = " \
    file://enclustra-user.dts \
"

RAM_ADDR_PATCH = "file://adapt-u-boot-env-for-larger-FIT-kernels.patch"

SRC_URI:append:me-aa1-generic = " \
    file://fdt.cfg \
    file://qspi-aa1-fit.cmd \
    file://sd-aa1-fit.cmd \
    ${@bb.utils.contains('KERNEL_IMAGETYPES', 'fitImage', '${RAM_ADDR_PATCH}', '', d)} \
"

SRC_URI:append:me-aa1-270-2i2-d11e-nfx3 = " \
    file://socfpga_enclustra_mercury_st1.dtsi \
"

do_add_enclustra_files:append:me-aa1-270-2i2-d11e-nfx3() {
    cp ${WORKDIR}/socfpga_enclustra_mercury_st1.dtsi ${S}/arch/arm/dts
}

do_configure:prepend:me-aa1-generic() {
    cp ${WORKDIR}/*aa1-fit.cmd ${S}/board/enclustra/bootscripts/
}

do_compile:prepend:me-aa1-generic() {
    if [ "${UBOOT_CONFIG}" != "qspi" ]; then
        sed -i "s|@@UBOOT_CONFIG@@|${UBOOT_CONFIG}|" ${S}/board/enclustra/bootscripts/sd-aa1-fit.cmd
    fi
}

do_compile:append:me-aa1-generic() {
    if [ "${@bb.utils.contains('KERNEL_IMAGETYPES', 'fitImage', 'yes', 'no', d)}" = "yes" ]; then
        mkimage -A arm -O linux -T script -C none -a 0 -e 0 -n "Uboot start script" -d ${S}/board/enclustra/bootscripts/sd-aa1-fit.cmd boot-sdmmc.scr
        mkimage -A arm -O linux -T script -C none -a 0 -e 0 -n "Uboot start script" -d ${S}/board/enclustra/bootscripts/sd-aa1-fit.cmd boot-emmc.scr
        mkimage -A arm -O linux -T script -C none -a 0 -e 0 -n "Uboot start script" -d ${S}/board/enclustra/bootscripts/qspi-aa1-fit.cmd boot-qspi.scr
        cp boot-${UBOOT_CONFIG}.scr boot.scr
    fi
}
