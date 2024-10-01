FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

DESCRIPTION = "Useful u-boot scripts for devel images"

LICENSE = "MIT"

DEPENDS = "u-boot-mkimage-native"

SRC_URI = "file://flash.cmd"

S = "${WORKDIR}"

inherit deploy

FLASHSCRIPT = "${S}/flash.cmd"
FILES:${PN} = "/boot"

do_mkimage () {
    uboot-mkimage -A arm -O linux -T script -C none -a 0 -e 0 \
                  -n "boot script" -d ${FLASHSCRIPT} ${S}/flash.scr
}

addtask mkimage after do_compile before do_install

do_deploy () {
    install -d ${DEPLOYDIR}
    install ${S}/flash.scr ${DEPLOYDIR}/flash.scr

    cd ${DEPLOYDIR}
    mv flash.scr flash.scr-${MACHINE}-qspi-${PV}-${PR}
    ln -sf flash.scr-${MACHINE}-qspi-${PV}-${PR} flash.scr-${MACHINE}
    ln -sf flash.scr-${MACHINE}-qspi-${PV}-${PR} flash.scr

}

do_install () {
    install -d ${D}/boot
    install ${S}/flash.scr ${D}/boot
}

addtask deploy after do_install before do_build

do_compile[noexec] = "1"

RPROVIDES:${PN} += "u-boot-script"

PACKAGE_ARCH = "${MACHINE_ARCH}"
COMPATIBLE_MACHINE = "(me-aa1-270-2i2-d11e-nfx3|refdes-me-aa1-270-2i2-d11e-nfx3-st1)"
