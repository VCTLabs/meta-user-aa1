SUMMARY = "Exported hardware design binaries, both vendor and custom"

inherit deploy

LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Proprietary;md5=0557f9d92cf58f2ccdd50f62f8ac0b28"

PROVIDES = "virtual/bitstream"

SRC_URI:refdes-me-aa1-270-2i2-d11e-nfx3-st1 = "\
    https://github.com/enclustra/Mercury_AA1_ST1_Reference_Design/releases/download/2023.1_v1.0.0/binaries_ME-AA1-270-2I2-D11E-NFX3_ST1.zip;name=ME-AA1-270-2I2-D11E-NFX3_ST1 \
"

SRC_URI:me-aa1-270-2i2-d11e-nfx3 = "\
    https://github.com/VCTLabs/Mercury_AA1_ST1_Reference_Design/releases/download/2023.1_v1.0.0/qspi_ME-AA1-270-2I2-D11E-NFX3.zip;name=ME-AA1-270-2I2-D11E-NFX3 \
"

SRC_URI[ME-AA1-270-2I2-D11E-NFX3_ST1.sha256sum] = "f71ebb2088c7466ddf011e290a570521da11ebfc8cf80a5066a0bbf18d243b18"
SRC_URI[ME-AA1-270-2I2-D11E-NFX3.sha256sum] = "5c1dd6cffa4b7c275b8423a13eb5517c83e40c391e31766166528f61918b3304"

ENCLUSTRA_BASE_NAME:refdes-me-aa1-270-2i2-d11e-nfx3-st1 = "Mercury_AA1_ST1"
ENCLUSTRA_BASE_NAME:me-aa1-270-2i2-d11e-nfx3 = "Mercury_AA1_ST1"

do_deploy[nostamp] = "1"

do_deploy() {
}

do_deploy:append:me-aa1-generic() {
    mkdir -p ${DEPLOY_DIR_IMAGE}/handoff
    cp -r ${WORKDIR}/${UBOOT_CONFIG}/hps_isw_handoff/* ${DEPLOY_DIR_IMAGE}/handoff
    install -D -m 0644 ${WORKDIR}/${UBOOT_CONFIG}/bitstream.core.rbf ${DEPLOY_DIR_IMAGE}/bitstream.core.rbf
    install -D -m 0644 ${WORKDIR}/${UBOOT_CONFIG}/bitstream.periph.rbf ${DEPLOY_DIR_IMAGE}/bitstream.periph.rbf
}

addtask deploy after do_configure
