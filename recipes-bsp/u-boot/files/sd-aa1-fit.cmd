load mmc 0:1 ${loadaddr} bitstream.itb
fpga loadmk 0 ${loadaddr}:fpga-core-1

bridge enable
fatload mmc 0:1 ${loadaddr} image.ub
bootm ${loadaddr}#conf-enclustra-user.dtb#conf-socfpga_enclustra_mercury_@@UBOOT_CONFIG@@_overlay.dtbo
