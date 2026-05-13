
if env exists partition; then echo Booting from mmcblk0p${partition}; else setenv partition 3; echo partition not set, default to ${partition}; fi
setenv upgrade_available 1
setenv altbootcmd 'if test ${partition} = 4; then setenv partition 3; else setenv partition 4; fi; run bootcmd'

load mmc 0:1 ${loadaddr} bitstream.itb
fpga loadmk 0 ${loadaddr}:fpga-core-1
bridge enable

load mmc 0:${partition} ${loadaddr} boot/fitImage
setenv bootargs "earlycon console=ttyS0,115200 rw rootwait root=/dev/mmcblk0p${partition}"
saveenv

bootm ${loadaddr}#conf-enclustra-user.dtb#conf-socfpga_enclustra_mercury_@@UBOOT_CONFIG@@_overlay.dtbo
