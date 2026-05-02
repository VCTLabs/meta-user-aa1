mmc dev 0

fatload mmc 0:1 0x10000000 qspi/u-boot-splx4.sfp
fatload mmc 0:1 0x10100000 qspi/u-boot.img
fatload mmc 0:1 0x10200000 qspi/boot.scr
fatload mmc 0:1 0x10300000 qspi/bitstream.itb
fatload mmc 0:1 0x11000000 qspi/fitImage-devel-initramfs

altera_set_storage QSPI
sf probe

sf update 0x10000000 ${qspi_offset_addr_spl} ${size_spl}
sf update 0x10100000 ${qspi_offset_addr_u-boot} ${size_u-boot}
sf update 0x10200000 ${qspi_offset_addr_boot-script} ${size_boot-script}
sf update 0x10300000 ${qspi_offset_addr_bitstream} ${size_bitstream}
sf update 0x11000000 ${qspi_offset_addr_kernel} ${size_kernel}
