sf probe
sf read ${ram_addr_bitstream} ${qspi_offset_addr_bitstream} ${size_bitstream}
fpga loadmk 0 ${ram_addr_bitstream}:fpga-core-1

bridge enable
sf read ${loadaddr} ${qspi_offset_addr_kernel} ${size_kernel}
bootm ${loadaddr}#conf-enclustra-user.dtb#conf-socfpga_enclustra_mercury_qspi_overlay.dtbo
