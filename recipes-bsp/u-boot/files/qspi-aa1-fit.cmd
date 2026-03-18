bridge enable
sf probe
sf read ${loadaddr} ${qspi_offset_addr_kernel} ${size_kernel}
bootm ${loadaddr}#conf-enclustra-user.dtb#conf-socfpga_enclustra_mercury_qspi_overlay.dtbo
