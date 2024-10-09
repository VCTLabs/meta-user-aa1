FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

DESCRIPTION = "Useful u-boot scripts for devel images"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

DEPENDS = "u-boot-mkimage-native"

SRC_URI = "file://flash.cmd"

S = "${WORKDIR}"

inherit deploy

FLASHSCRIPT = "${S}/flash.cmd"

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

addtask deploy after do_install before do_build

do_compile[noexec] = "1"

PACKAGE_ARCH = "${MACHINE_ARCH}"
COMPATIBLE_MACHINE = "(me-aa1-270-2i2-d11e-nfx3|refdes-me-aa1-270-2i2-d11e-nfx3-st1)"
