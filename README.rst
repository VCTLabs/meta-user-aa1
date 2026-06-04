Meta-user-aa1
=============

This repository contains a Yocto layer to generate the Linux reference design
for the following Enclustra SoC module family:

- Enclustra Mercury+ AA1 product series: https://www.enclustra.com/en/products/system-on-chip-modules/mercury-aa1/

The reference design is compatible with following base board:

- Enclustra Mercury+ ST1: https://www.enclustra.com/en/products/base-boards/mercury-st1

The HW reference design files for Mercury+ AA1 ST1 module and baseboard live here:

- Mercury+ AA1 ST1 Reference Design: https://github.com/enclustra/Mercury_AA1_ST1_Reference_Design


Dependencies
============

This layer depends on meta-intel-fpga and OE core:

* URI: https://git.yoctoproject.org/meta-intel-fpga
* branch: mickledore
* layer: meta-intel-fpga

* URI: https://git.openembedded.org/openembedded-core
* branch: mickledore
* layer: meta

* URI: http://git.openembedded.org/meta-openembedded
* branch: mickledore
* layer: meta-oe

This layer also depends on meta-swupdate for SW updates on target devices.

* URI: https://github.com/sbabic/meta-swupdate
* branch: mickledore
* layer: meta-swupdate

This layer also still depends on the meta-enclustra-socfpga module layer for user
machine compatibility, eg, the initial ``me-aa1-270-2i2-d11e-nfx3`` machine and
related recipes:

* URI: https://github.com/enclustra/meta-enclustra-socfpga
* branch: v2023.1
* layer: meta-enclustra-module

The primary indirect dependency is Quartus XX Std/Pro, where XX and type
depends on the SoC and u-boot version. Other versions may work with a given
project, but vendor support will most likely expect their published reqs,
eg, the versions mentioned here require the following:

=======================  ==============   =================   =================
Processor                SOCFPGA Device   Intel Quartus Pro   Intel Quartus Std
-----------------------  --------------   -----------------   -----------------
Dual-core ARM Cortex-A9   Cyclone V        N/A                 22.1
|                         Arria 10         23.1                N/A
=======================  ==============   =================   =================

All arm64 devices require Intel Quartus Pro.

Images and Uboot config
=======================

The default build image target should be set in at least one kas config file
and can be affected by the ``UBOOT_CONFIG`` setting. The default artifact is
a WIC file for sdmmc and emmc or a separate set of artifacts for qspi boot.

The default *kernel* image type is normally set in the vendor BSP, in this
case, in the intel-fpga layer and again in the enclustra module layer. The
kas config now contains additional overrides to enable FIT images, which
may contain several artifacts, in this case a kernel, one or more devicetree
blobs, an initramfs, a uboot script, etc. Note there is already a FIT image
containing the split FPGA bitstream files.

The FIT image is the "new style" image format intended to replace the legacy
(uImage) format to facilitate handling of both multiple artifact types *and*
metadata for things like cryptographic hashes, signatures, and trusted boot.

FIT image changes
-----------------

* load addresses in u-boot/kernel args may need to be modified and/or specified
  in config variables
* boot script commands
* replace qspi (initrd) ramdisk with FIT image (initramfs) ramdisk
* supplement the use of "dev" signing keys with production key processes


QSPI address changes
--------------------

The following table contains both the original QSPI layout vs the new FIT
layout with fewer artifacts (since the FIT image format contains all the
required boot artifacts). There are now two flash scripts for QSPI, both
legacy artifacts and FIT artifacts are supported.


.. csv-table:: QSPI addresses and sizes
   :delim: ;
   :header: "Name", "Offset", "Script address", "Size", "Name"
   :widths: 25, 15, 15, 15, 15

   "qspi_offset_addr_spl";"0x0";"0x10000000";"0x100000";"size_spl"
   "qspi_offset_addr_u-boot";"0x100000";"0x10100000";"0x80000";"size_u-boot"
   "qspi_offset_addr_u-boot-env";"0x180000";;"0x80000";"size_u-boot-env"
   "qspi_offset_addr_boot-script";"0x200000";"0x10200000";"0x80000";"size_boot-script"
   "qspi_offset_addr_devicetree";"0x280000";"0x10300000";"0x40000";"size_devicetree"
   "qspi_offset_addr_dtoverlay";"0x2c0000";"0x10400000";"0x40000";"size_dtoverlay"
   "qspi_offset_addr_bitstream";"0x300000";"0x11000000";"0xD00000";"size_bitstream"
   "qspi_offset_addr_kernel";"0x1000000";"0x12000000";"0x1000000";"size_kernel"
   "qspi_offset_addr_rootfs";"0x2000000";"0x13000000";"0x2000000";"size_rootfs"
   ;;;;
   ;;;;
   "qspi_offset_addr_spl";"0x0";"0x10000000";"0x100000";"size_spl"
   "qspi_offset_addr_u-boot";"0x100000";"0x10100000";"0x80000";"size_u-boot"
   "qspi_offset_addr_u-boot-env";"0x180000";;"0x80000";"size_u-boot-env"
   "qspi_offset_addr_boot-script";"0x200000";"0x10200000";"0x80000";"size_boot-script"
   "qspi_offset_addr_bitstream";"0x300000";"0x10300000";"0xD00000";"size_bitstream"
   "qspi_offset_addr_kernel";"0x1000000";"0x11000000";"0x3000000";"size_kernel"


Due to Enclustra `known issue number 1`_ we were unable to complete FIT image
testing for all boot modes (see the following section).

.. _known issue number 1: https://github.com/enclustra/meta-enclustra-socfpga#1-protection-bits-are-set-in-qspi-flash

Boot mode FIT test results
--------------------------

FIT image kernel artifacts are intended to replace legacy artifacts for all
available boot modes, namely TFTP, QSPI, EMMC, and SDMMC. The test results
are shown in the following table.

Note in the following table **N/A** indicates "Not Applicable" because that
particular combination is not a supported use case, while "unable to test"
refers to the above known issue with Enclustra eval boards. TFTP from eMMC
u-boot was not meaningful since it only requires u-boot and loads everything
into RAM (identical to TFTP from sdmmc).

.. csv-table:: Boot mode FIT tests
   :delim: ;
   :header: "UBOOT_CONFIG", "tftp load fit with initramfs", "fit with external rootfs", "fit with initramfs"

   "EMMC";"skipped";"PASS";"PASS"
   "SDMMC";"PASS";"PASS";"PASS"
   "QSPI";"PASS";"N/A";"unable to test"


Transition to new QSPI flash layout
-----------------------------------

Before flashing the new QSPI FIT artifacts, the flash MTD partition(s) should
be fully erased once from the Linux side.

* set the boot mode to QSPI and boot to the Linux prompt
* run the following command on the first ``/dev/mtd`` partition

::

    # flash_erase /dev/mtd0 0 0x4000000

* power off the board and change the boot mode to SDMMC
* boot the board and follow the QSPI flash steps


New build artifacts
~~~~~~~~~~~~~~~~~~~

New build artifacts include multiple fitImages and their ``.its`` files, as
well as symlinks (patches are still required to get the short symlinks
in the deploy directory). Short names and descriptions are given below.

+ **fitImage** - FIT image containing kernel and devicetree blob(s)
+ **fitImage-devel-initramfs** - FIT image containing the above assets
  with an initramfs root (ie, ramdisk)

All of the above can be found in the deploy directory, but only the two
primary fitImage files should be deployed to the boot partition.

U-boot load addresses
~~~~~~~~~~~~~~~~~~~~~

On the bootloader side, the fitImage requires a load address and sufficient
free space *above* that address so the fitImage payloads can be loaded at
their own addresses. In the case of enclustra, the smaller fitImage can be
loaded in the usual kernel RAM address, however, the initramfs fitImage
requires more contiguous RAM so needs to be loaded at the default u-boot
${loadaddr} variable.

fitImage introspection
~~~~~~~~~~~~~~~~~~~~~~

From the desktop using ``u-boot-tools`` mkimage command::

  $ mkimage -l path/to/fitimage  # list the contents

From the u-boot prompt after loading the fitimage file::

  => iminfo ${loadaddr}

Example - signed fitimage with ramdisk::

  $ mkimage -l /media/boot/initramfs-image.ub
  FIT description: Kernel fitImage for OpenEmbedded/6.1.38-lts+gitAUTOINC+21b5300ed5/me-aa1-270-2i2-d11e-nfx3
  Created:         Sun Oct  1 23:02:34 2023
   Image 0 (kernel-1)
    Description:  Linux kernel
    Created:      Sun Oct  1 23:02:34 2023
    Type:         Kernel Image
    Compression:  uncompressed
    Data Size:    5859736 Bytes = 5722.40 KiB = 5.59 MiB
    Architecture: ARM
    OS:           Linux
    Load Address: 0x00008000
    Entry Point:  0x00008000
    Hash algo:    sha256
    Hash value:   bac4e35b6c595091124e0d318ad220d3a97050d8cfb9eb5543a954d642d9abf6
   Image 1 (fdt-enclustra-user.dtb)
    Description:  Flattened Device Tree blob
    Created:      Sun Oct  1 23:02:34 2023
    Type:         Flat Device Tree
    Compression:  uncompressed
    Data Size:    37409 Bytes = 36.53 KiB = 0.04 MiB
    Architecture: ARM
    Load Address: 0x10000000
    Hash algo:    sha256
    Hash value:   977370d59d8730ae8d8de649caa2bd15495afd0bc5114b965803b4cb64b1162b
   Image 2 (fdt-socfpga_enclustra_mercury_emmc_overlay.dtbo)
    Description:  Flattened Device Tree blob
    Created:      Sun Oct  1 23:02:34 2023
    Type:         Flat Device Tree
    Compression:  uncompressed
    Data Size:    477 Bytes = 0.47 KiB = 0.00 MiB
    Architecture: ARM
    Load Address: 0x100c0000
    Hash algo:    sha256
    Hash value:   8016e571392b85c3fb0ba38eae83c060302b4d7e4371820e67eb0c4fa333428c
   Image 3 (fdt-socfpga_enclustra_mercury_qspi_overlay.dtbo)
    Description:  Flattened Device Tree blob
    Created:      Sun Oct  1 23:02:34 2023
    Type:         Flat Device Tree
    Compression:  uncompressed
    Data Size:    354 Bytes = 0.35 KiB = 0.00 MiB
    Architecture: ARM
    Load Address: 0x100c0000
    Hash algo:    sha256
    Hash value:   f54c4914dee9cbc33055cc97f830294055fe956615174463a9441d578a8d69ac
   Image 4 (fdt-socfpga_enclustra_mercury_sdmmc_overlay.dtbo)
    Description:  Flattened Device Tree blob
    Created:      Sun Oct  1 23:02:34 2023
    Type:         Flat Device Tree
    Compression:  uncompressed
    Data Size:    355 Bytes = 0.35 KiB = 0.00 MiB
    Architecture: ARM
    Load Address: 0x100c0000
    Hash algo:    sha256
    Hash value:   847dace5bd813913894b73a3920f9f1d51e09a2407ebaedc8091bf9a4c88ee17
   Image 5 (ramdisk-1)
    Description:  devel-initramfs
    Created:      Sun Oct  1 23:02:34 2023
    Type:         RAMDisk Image
    Compression:  uncompressed
    Data Size:    18101636 Bytes = 17677.38 KiB = 17.26 MiB
    Architecture: ARM
    OS:           Linux
    Load Address: 0x12000000
    Entry Point:  unavailable
    Hash algo:    sha256
    Hash value:   3b517db97b83c3f10811edf5697c0ae4bd56c729c18a456e9f74931556297555
   Default Configuration: 'conf-enclustra-user.dtb'
   Configuration 0 (conf-enclustra-user.dtb)
    Description:  1 Linux kernel, FDT blob, ramdisk
    Kernel:       kernel-1
    Init Ramdisk: ramdisk-1
    FDT:          fdt-enclustra-user.dtb
    Hash algo:    sha256
    Hash value:   unavailable
    Sign algo:    sha256,rsa2048:dev
    Sign padding: pkcs-1.5
    Sign value:   0563838352b9d3b1790070d8c243ad973f0d6fd2144c3a48ae1ae08e9ed61eae5d5f732d1ce28af3a7e5a0b824516f40092b174d021b06f7be4c6d21ce5063b9ceb8458ef91f9d093b35b842bc49d56ab9f8818904a00851e662f23a60b0ccb4c5a0c7602469c599c77b2bc75a13e73d9c9c3a65548ec451fabb5ddf8bd74ed30f9fa25513103c98ddc9065ce649664f3a5244d90c90a531a9d1ba29c94ef571a9c6c0f3cd5967131bb17d79d82191be278111374393c8d0a26fa824347159de81b18c8ffceffc86d487c08127a596ab5e74da3edaa4c1e15a9333322b3f9e59b0e25cceca69aebe4f254a479435f026018f8941a0e77f85b86b011f0e95c820
    Timestamp:    Sun Oct  1 23:02:34 2023
   Configuration 1 (conf-socfpga_enclustra_mercury_emmc_overlay.dtbo)
    Description:  0 FDT blob
    Kernel:       unavailable
    FDT:          fdt-socfpga_enclustra_mercury_emmc_overlay.dtbo
    Hash algo:    sha256
    Hash value:   unavailable
    Sign algo:    sha256,rsa2048:dev
    Sign padding: pkcs-1.5
    Sign value:   505d668578e50fea168534d0ccdd6afae79eb388615d99489679222d3b1c94df5bccd6ba60d05958311d1be1e0f17db99a9e9cd851bbe9f4647ea7345a1dcca2265a2bfb435b05d60952212b7533d7ee29b95e04c7be42058c0b5a4aea8bc0ac9c567b285aa98f3664ae8314fe463b2ace5c36b88d23c492bf385285a6648b5766db5bab07c5694a0c6ea78a6f20102f86b721f17f5911fef67e89651fc958d154347eaf4c0fcfcca428713c1ac4b9ac18efb9b4584e45e0562a1368db8c4d143770b8e962a3cdf7c72ea5cf1f7d5e07e0620fea2ce3a07426e7fae971f698e6f663a0af85df302a2b4361f1862e69fb5c7fcd8f6d6c49099bd8606e4ef5cf87
    Timestamp:    Sun Oct  1 23:02:34 2023
   Configuration 2 (conf-socfpga_enclustra_mercury_qspi_overlay.dtbo)
    Description:  0 FDT blob
    Kernel:       unavailable
    FDT:          fdt-socfpga_enclustra_mercury_qspi_overlay.dtbo
    Hash algo:    sha256
    Hash value:   unavailable
    Sign algo:    sha256,rsa2048:dev
    Sign padding: pkcs-1.5
    Sign value:   d1c9ae2c3f21b6d967a409acaa2e441c3232811dcefff0e3f7883b5c390128dbf5d66035f252bf347149db5ccb42aa23762c3964ef6013021fd234bd01289a0552acab58a3cd6db25da4f59343f47d5a480e960e9c240689c40fa1982da483eb9241d8f9e910255735d803727afed92acf0dfe697740aac6c64b0fc217354db284206580dffed3cdb5db957c60735829e02d09c4395ab4dedc120c292ef2db7caa027e7a4de23d7f68ccf957be0e1af0598261d1d01a68b0d4839a4c4236c7d45ba6febf338e34d5f48fcd44680b41e2b3fc6e7e1ad22daa50250b8a4e5db73d1871a3199368da3dc8e2d402121cc9a5e9425701d502a44202c4892167d70b92
    Timestamp:    Sun Oct  1 23:02:34 2023
   Configuration 3 (conf-socfpga_enclustra_mercury_sdmmc_overlay.dtbo)
    Description:  0 FDT blob
    Kernel:       unavailable
    FDT:          fdt-socfpga_enclustra_mercury_sdmmc_overlay.dtbo
    Hash algo:    sha256
    Hash value:   unavailable
    Sign algo:    sha256,rsa2048:dev
    Sign padding: pkcs-1.5
    Sign value:   41971c6854061091d5faa3e03da154422d379c392a5587384928b8d01f8c79afcd11569fcb56a1aad726ecd1a85c5a2847daeebf2eeb3c6c80a6bfaf5b8e2ff14e56575873a2829eeb3dfa17ab509ff2ec504d7f0e3e1f2adbf4519ea187a8e2551c3f8176195a01d1799bd8937f8d85a156ebe5b8d88bc002a48a528a810a0aa290d13f4387a822dcdfb0f2a94fb8886767629663db56d5fbca64c8bade96fdc5ba52386a055a45509961e11e78d65d882443decdb4f6c34b0a9bb75325c99c6534eea376f76e12b6a2247122bdbbc8bc146adbc4c6e3e4aff43d1de87f260e1fc1f9faedffbd5471564cdee1cd88e787ba4bc7d8594a6cdf312b5539925255
    Timestamp:    Sun Oct  1 23:02:34 2023


fitImage bootm syntax
~~~~~~~~~~~~~~~~~~~~~

Default config after loading the fitimage file::

  => bootm ${loadaddr}

Default config with overlay (EMMC media)::

  => bootm ${loadaddr}#conf-enclustra-user.dtb#conf-socfpga_enclustra_mercury_emmc_overlay.dtbo

Default config with overlay (QSPI Flash)::

  => bootm ${loadaddr}#conf-enclustra-user.dtb#conf-socfpga_enclustra_mercury_qspi_overlay.dtbo

The last commands above use the FIT configuration names generated by the
``kernel-fitimage.bbclass``.

(pre)Generate SSH host keys
---------------------------

There are some specific situations where (pre)generated SSH host keys are
useful, among them:

* automated testing
* rootfs packed into initrd/initramfs
* ro-rootfs generates keys on every boot

The recipe does not provide default keys, so do not include it in your image
until you have generated some keys (prefer ed25519 or ecdsa over rsa).

To generate keys for OpenSSH, you can run something like the following::

  $ ssh-keygen -t ed25519 -f ssh_host_ed25519_key
  $ ssh-keygen -t rsa -b 4096 -f ssh_host_rsa_key  # for backward compatibility only

Then copy them to the recipe folder::

  $ cp ssh_host_*_key* <RECIPE_SRC_URI_DIR>
  # where RECIPE_SRC_URI_DIR = recipes-connectivity/ssh-pregen-hostkeys/ssh-pregen-hostkeys/

.. important:: **DO NOT** do this for more than a single test image. If you
               really do need to pregenerate keys for a production image,
               then you should absolutely have a proper first-boot or mount
               an overlay where keys can be generated for each device.

SWUpdate support
================

Support for SWUpdate is still WIP, with initial swupdate image support for
the following:

* A/B rootfs updates via ``update-prod-image-data.bb``
* bitstream file updates (with orig file backup) via ``update-bitstream-image.bb``

Currently enabled update mechanisms are limited to on-device, where the update
image (ie, the ``.swu`` file) must be uploaded to the device before the ``swupdate``
command can be issued. Supported update workflows include the following:

* manually upload a ``.swu`` file and run swupdate via console (as shown below)
* use the on-device www server UI to upload and apply the ``.swu`` file(s)

To manually apply updates (as root) after copying to the device with
short names::

  # swupdate -v -i update-bitstream.swu
  ...
  [TRACE] : SWUPDATE running :  [__run_cmd] : /tmp/scripts/preinstall.sh   command returned 0
  [TRACE] : SWUPDATE running :  [install_single_image] : Found installer for stream bitstream.itb rawfile
  [TRACE] : SWUPDATE running :  [install_raw_file] : Installing file bitstream.itb on /tmp/datadst//bitstream.itb
  [TRACE] : SWUPDATE running :  [read_lines_notify] : SWU: /tmp/scripts/postinstall.sh
  [TRACE] : SWUPDATE running :  [read_lines_notify] : SWU: Image update success!!
  [TRACE] : SWUPDATE running :  [__run_cmd] : /tmp/scripts/postinstall.sh   command returned 0
  [INFO ] : SWUPDATE successful ! SWUPDATE successful !
  [TRACE] : SWUPDATE running :  [network_initializer] : Main thread sleep again !
  [INFO ] : No SWUPDATE running :  Waiting for requests...
  [INFO ] : SWUPDATE running :  [endupdate] : SWUpdate was successful !
  [DEBUG] : SWUPDATE running :  [postupdate] : Running Post-update command

To apply an A/B update, use something like the following arguments to
update root B from the stable set using "copy2" argument::

  # swupdate -v -l 5 -e stable,copy2 -p 'reboot' -i update-prod.swu
  ...
  [TRACE] : SWUPDATE running :  [extract_file_to_tmp] : Found file
  [TRACE] : SWUPDATE running :  [extract_file_to_tmp] : 	filename sw-description
  [TRACE] : SWUPDATE running :  [extract_file_to_tmp] : 	size 3113
  [TRACE] : SWUPDATE running :  [extract_file_to_tmp] : Found file
  [TRACE] : SWUPDATE running :  [extract_file_to_tmp] : 	filename sw-description.sig
  [TRACE] : SWUPDATE running :  [extract_file_to_tmp] : 	size 256
  [TRACE] : SWUPDATE running :  [swupdate_verify_file] : Verify signed image: Read 3113 bytes
  [TRACE] : SWUPDATE running :  [swupdate_verify_file] : Verified OK
  ...
  [TRACE] : SWUPDATE running :  [check_hw_compatibility] : Hardware me-aa1-270-2i2-d11e-nfx3 Revision: 1.0
  [TRACE] : SWUPDATE running :  [check_hw_compatibility] : Hardware compatibility verified
  [DEBUG] : SWUPDATE running :  [preupdatecmd] : Running Pre-update command
  [TRACE] : SWUPDATE running :  [extract_files] : Found file
  [TRACE] : SWUPDATE running :  [extract_files] : 	filename preinstall.sh
  [TRACE] : SWUPDATE running :  [extract_files] : 	size 19 required
  [TRACE] : SWUPDATE running :  [extract_files] : Found file
  [TRACE] : SWUPDATE running :  [extract_files] : 	filename postinstall.sh
  [TRACE] : SWUPDATE running :  [extract_files] : 	size 127 required
  [TRACE] : SWUPDATE running :  [extract_files] : Found file
  [TRACE] : SWUPDATE running :  [extract_files] : 	filename prod-image-data-me-aa1-270-2i2-d11e-nfx3.ext4.gz
  [TRACE] : SWUPDATE running :  [extract_files] : 	size 43443698 required
  [TRACE] : SWUPDATE running :  [extract_files] : Installing STREAM prod-image-data-me-aa1-270-2i2-d11e-nfx3.ext4.gz, 43443698 bytes
  [TRACE] : SWUPDATE running :  [install_single_image] : Found installer for stream prod-image-data-me-aa1-270-2i2-d11e-nfx3.ext4.gz raw
  [TRACE] : SWUPDATE running :  [extract_files] : END INSTALLING STREAMING
  [TRACE] : SWUPDATE running :  [extract_padding] : Expecting 388 padding bytes at end-of-file
  [TRACE] : SWUPDATE running :  [network_initializer] : Valid image found: copying to FLASH
  [INFO ] : SWUPDATE running :  Installation in progress
  [TRACE] : SWUPDATE running :  [read_lines_notify] : STUB: /tmp/scripts/preinstall.sh
  [TRACE] : SWUPDATE running :  [__run_cmd] : /tmp/scripts/preinstall.sh   command returned 0
  [TRACE] : SWUPDATE running :  [read_lines_notify] : SWU: /tmp/scripts/postinstall.sh
  [TRACE] : SWUPDATE running :  [read_lines_notify] : SWU: Image update success!!
  [TRACE] : SWUPDATE running :  [__run_cmd] : /tmp/scripts/postinstall.sh   command returned 0
  [INFO ] : SWUPDATE successful ! SWUPDATE successful !
  ...

With swupdate encryption enabled and keys in place, the initial debug output
should include a line for loading the AES keys::

  ...
  [TRACE] : SWUPDATE running :  [print_registered_bootloaders] : 	uboot	loaded.
  [INFO ] : SWUPDATE running :  [main] : Using default bootloader interface: uboot
  [DEBUG] : SWUPDATE running :  [load_decryption_key] : Read decryption key and initialization vector from file /etc/swupdate/swu_aes_file.
  [INFO ] : SWUPDATE running :  [main] : Running on me-aa1-270-2i2-d11e-nfx3 Revision 1.0
  ...


swupdate www interface
----------------------

Configure the web server root path and port using the following:

* SWU_WWW_DOC_ROOT (default: /www)
* SWU_WWW_HTTP_PORT (default: 8080)

where defaults are set in the ``swu.yaml`` config file.

The swupdate web server is installed via the ``swupdate-www`` package and
is not customized (note the FOSS licensing of the existing image files).
See the `SWUpdate docs`_ to get started customizing the web assets and/or
web-app.

Once the web server package is installed and the swupdate args updated,
the web server is controlled by the running swupdate instance. Using the
example port number ``8080`` connect to the device with a web browser
using something like::

  $ firefox http://192.168.1.22:8080

where ``192.168.1.22`` is the IPv4 address of the device running swupdate
and ``8080`` is the configured port number.


.. _SWUpdate docs: https://sbabic.github.io/swupdate/mongoose.html#customize

u-boot bootcount vars
---------------------

:bootlimit: max bootcount allowed before ``altbootcmd`` is executed
:bootcount: set to 1 after a power-on reset, and each reboot will increment
            the value by 1
:upgrade_available: if ``upgrade_available`` is 0, ``bootcount`` is not saved,
                    but if ``upgrade_available`` is 1, ``bootcount`` is saved

  ..important:: If ``bootlimit`` is enabled, but ``altbootcmd`` is not
    defined, then U-Boot will drop into interactive mode and remain there.

states relevant to swupdate
---------------------------

When ``bootlimit=3``:

:good: Running current image: ``bootcount`` undefined - ``upgrade_available=0``
:testing: Trying to boot new image: ``bootcount<=3`` - ``upgrade_available=1``
:fail: New image boot failed to boot more than ``bootlimit`` times:
       ``bootcount>3`` - ``upgrade_available=0`` and run ``altbootcmd``

swupdate keys for signing and crypto
------------------------------------

There is currently no meta-layer support for generating (or managing) the
keys used for swupdate images, therefor, the user must define the (yocto)
variables that point to where the keys are. The initial process is shown
below and the yocto signing variables are defined in ``kas/swukeys.yaml``.
Use the default names while this workflow is still WIP.

* make a build without signing enabled
* generate dev keys and copy the directory into the build deploy dir
* uncomment the swukeys.yaml line in systemd.yaml
* build the update-devel image
* deploy the wic image and copy the update.swu file to the device
* test swupdate can verify the signature and install the update

.. important:: The swupdate configuration fragments to enable both signing
               and encryption are only used when the ``swukeys.yaml`` is
               enabled. This file is enabled only when it is *not* commented
               in either ``systemd.yaml`` or ``sysvinit.yaml``. When enabled,
               the build depends on the existence of the key files shown
               below.

To build swupdate images with signing only (and no encryption) you can
set ``SWUPDATE_ENCRYPTION = "0"`` and provide only the swupdate signing
keys.

generate swupdate keys
~~~~~~~~~~~~~~~~~~~~~~

From the top-level project directory::

  $ mkdir swupdate-dev-keys
  $ nano swupdate-dev-keys/swu_priv.pass  # add a passphrase on a single line
  $ openssl genrsa -aes256 -passout file:swupdate-dev-keys/swu_priv.pass -out swupdate-dev-keys/swu_priv.pem
  $ openssl rsa -in swupdate-dev-keys/swu_priv.pem -passin file:swupdate-dev-keys/swu_priv.pass -out swupdate-dev-keys/swu_public.pem -outform PEM -pubout
  $ openssl enc -aes-256-cbc -k "" -P -md sha1 -nosalt > swupdate-dev-keys/swu_aes_file
  $ mv swupdate-dev-keys build/tmp-glibc/deploy/images/me-aa1-270-2i2-d11e-nfx3/

At least for now, start with changing only YourPassPhraseForKey in the
above *and nothing else*.

swupdate key integration
~~~~~~~~~~~~~~~~~~~~~~~~

RSA signing key notes

* target device requires public key
* build process requires private key

AES symmetric crypto key notes

* both build and target device require aes_file with key and iv

  * at least the IV should be (re)generated for each encryption
  * there is no current support for updateing in the build

swupdate miscellaneous
~~~~~~~~~~~~~~~~~~~~~~

Unpack a ``.swu`` file::

  $ cpio -i -d < $NAME.swu


Custom machine overrides
========================

Upstream "doc" bits:

* `bitbake manual section`_
* `glossary section`_
* `machine groups on SO`_

References on this topic seem pretty thin, so so we still need to include
some example machine overrides that allow the following:

* use generic "platform" overrides to separate debug and hardened images for
  the same hardware
* migrate from the enclustra "starter" machines to custom devel and production
  boards

Working machine overrides depend on refactoring in (forked) upstream repo.

.. _bitbake manual section: https://docs.yoctoproject.org/bitbake/2.10/bitbake-user-manual/bitbake-user-manual-metadata.html#conditional-syntax-overrides
.. _glossary section: https://docs.yoctoproject.org/ref-manual/variables.html#term-MACHINEOVERRIDES
.. _machine groups on SO: https://stackoverflow.com/questions/77667680/how-to-create-groups-of-machines-for-use-in-machine-specific-overrides


The meta-enclustra-module layer should above provides user "starter" machine
defs for each supported combination of base board and module, eg, the initial
default machine definitions for the Mercury+ AA1 module on ST1 base board:

* me-aa1-270-2i2-d11e-nfx3_

This layer now includes additional (mostly abstract) machine definitions based
on the above baseboard/module combination.

Production/development pipeline machines:

:debug-baseboard: Baseline debug/devel machine compatible with enclustra AA1/ST1
:hardened-baseboard: Baseline hardened/production machine compatible with enclustra AA1/ST1

Platform classification overrides:

:debug-platform: Use to add/set devel feature overrides and SRC_URI appends
:hardened-platform: Use to remove debug features and/or set hardening options
:st1-baseboard: Generic machine override compatible with enclustra AA1/ST1


.. _me-aa1-270-2i2-d11e-nfx3: https://github.com/enclustra/meta-enclustra-socfpga/blob/v2023.1/meta-enclustra-module/conf/machine/me-aa1-270-2i2-d11e-nfx3.conf

Example machine override usage
------------------------------

Use the platform overrides to configure a production image recipe::

  # read-only root filesystem
  IMAGE_FEATURES:append:hardened-platform = " read-only-rootfs stateless-rootfs"
  IMAGE_FEATURES:remove:hardened-platform = " debug-tweaks package-management ssh-server-openssh"

Use a production machine override to include devicetree files for a custom board::

  FILESEXTRAPATHS:prepend:production-board := "${THISDIR}/production-board:"

  COMPATIBLE_MACHINE += "|me-st1-generic|st1-baseboard"

  SRC_URI:append:production-board = " file://stech-board.dtsi"

The "pipeline" machines described above should *replace* current the default user
machine above, however, the simple machines defined here still depend on both
machine defs and recipe overrides defined by enclustra (in their module layer).

Custom overrides for specific machine features or other build settings should be
added as-needed, starting with the the example common machine include file::

  $ cat conf/machine/include/aa1-st1-common.conf
  # Common machine support for enclustra aa1 module and st1 carrier board
  #

  MACHINEOVERRIDES:prepend = "me-aa1-270-2i2-d11e-nfx3:me-st1-generic:"

  require conf/machine/me-aa1-generic.conf

  IMAGE_FSTYPES:append = " wic ext4"

The above built image artifacts are appended to the defaults set in the enclustra
module layer: ``IMAGE_FSTYPES = "cpio.gz.u-boot wic.bmap tar.gz"`` (in this case
the assignment is *not* weak).

The upstream BSP layers in both meta-enclustra-socfpga_ and meta-intel-fpga_ should
be used as the "documented" upstream machine definitions.

.. _meta-enclustra-socfpga: https://github.com/enclustra/meta-enclustra-socfpga
.. _meta-intel-fpga: https://git.yoctoproject.org/meta-intel-fpga


Adding new overrides
--------------------

The above overrides are only a starting point for decoupling and setting desired
groups of "distro" or image features. Given the state of the enclustra hardware
overlap for boot media, another useful approach might be creating separate machine
definitions for each boot method. This would allow dropping the current environment
variables for boot method and setting each one as a "machine feature".

An example machine definition file for ``debug-emmc.conf`` might look like this::

  #@TYPE: Machine
  #@NAME: debug-emmc
  #@DESCRIPTION: Machine overrides for Intel SoCFPGA Arria10 from enclustra
  #

  MACHINEOVERRIDES =. "debug-platform:"

  require conf/machine/include/aa1-st1-common.conf

  UBOOT_CONFIG = "emmc"


Custom exported binaries
========================

Each enclustra (socfpga) reference design gets a Yocto machine definition,
however, user projects should select one of the base machines provided by
the enclustra module layer => meta-enclustra-module_ (one of the layers
provided in meta-enclustra-socfpga_) to get started. Given the current AA1/ST1
hardware, the correct (yocto) user machine is ``me-aa1-270-2i2-d11e-nfx3``.

The user project must provide a zipfile containing the build files from the
desired Quartus project, ie, 1) the bitstream ``.sof`` must be converted to
the split ``.rbf`` files expected by Arria10 devices, and 2) the handoff
directory must contain the ``hps.xml`` project definitions. Each of the
reference design projects contains three file trees, one for each boot
mode, and the corresponding user built projects should mimic this layout
by providing *at least one* of these file trees.

For example, a user project using the QSPI boot mode would use the following
layout::

  $ tree -l 3 qspi/
  qspi/                         # directory name matches boot mode: qspi, emmc, sdmmc
  ├── bitstream.core.rbf        # split bitstream required for Arria10
  ├── bitstream.periph.rbf      # second part
  ├── hps_isw_handoff           # required handoff directory
  │   ├── emif.xml
  │   ├── hps.xml
  │   └── id
  ├── Mercury_AA1_pd.sopcinfo   # additional files are okay
  └── Mercury_AA1_ST1.sof

  2 directories, 7 files

To produce the "source" zipfile for the exported_binaries recipe, zip the
above directory tree using the machine name in upper case prepended with
the bootmode in lower case::

  $ zip -r qspi_ME-AA1-270-2I2-D11E-NFX3.zip qspi/



.. _meta-enclustra-module: https://github.com/enclustra/meta-enclustra-socfpga/tree/v2023.1/meta-enclustra-module
.. _meta-enclustra-socfpga: https://github.com/enclustra/meta-enclustra-socfpga


Adding this layer to your build
===============================

In order to use this layer, you need to make the build system aware of it.

Assuming the layer exists at the top-level of your build tree, you can add
it to the build system by adding the location of this layer to
bblayers.conf, along with any other layers needed. e.g.::

  BBLAYERS ?= " \
    /path/to/oe-core/meta \
    /path/to/meta-openembedded/meta-oe \
    /path/to/layer/meta-user-aa1 "


Arria10 yocto and handoff references
====================================

Note that handoff process and tooling has changed several times in recent
versions of quartus and u-boot-socfpga. The handoff names are different for
different SoCs but this is primarily about Arria10 only.  User configuration
bits have mainly been moved to u-boot and the linux kernel configs. Each
vendor has slightly different usage depending on specific hardware, where
enclustra requires specific filenames for the user DTS files and combines
the split FPGA bitstream files via FIT image.

* meta-enclustra_
* `enclustra user layer`_
* enclustra-refdes_ - see reference design document link

* `Intel handoff bug`_
* `rocketboards bootloader doc`_ - Arria 10, u-boot-socfpga 2024.01, linux-socfpga 6.6.22-lts, Quartus 24.2 Pro

* `U-boot-socfpga`_ - bootloader and handoff tools
* `doc/README.socfpga`_ - device-specific readme

* `Split .sof files`_ - create split bitstream files for Yocto (`also mentioned here`_)
* HWLib_ - low-level SW interface to system HW

.. _meta-enclustra: https://github.com/enclustra/meta-enclustra-socfpga/blob/v2023.1/README.md
.. _enclustra user layer: https://github.com/enclustra/meta-enclustra-socfpga/tree/v2023.1?tab=readme-ov-file#integrate-meta-enclustra-module-layer-into-user-project
.. _enclustra-refdes: https://github.com/enclustra/Mercury_AA1_ST1_Reference_Design
.. _Intel handoff bug: https://www.intel.com/content/www/us/en/support/programmable/articles/000090551.html
.. _rocketboards bootloader doc: https://www.rocketboards.org/foswiki/Documentation/BuildingBootloaderCycloneVAndArria10
.. _U-boot-socfpga: https://github.com/altera-opensource/u-boot-socfpga
.. _doc/README.socfpga: https://github.com/altera-opensource/u-boot-socfpga/blob/HEAD/doc/README.socfpga
.. _Split .sof files: https://www.rocketboards.org/foswiki/Documentation/A10SGMIIRDCompilingHardwareDesignLTS
.. _also mentioned here: https://www.intel.com/content/www/us/en/docs/programmable/683536/current/converting-the-sof-file-into-two-split.html
.. _HWLib: https://www.rocketboards.org/foswiki/Documentation/HWLib


Minimal user layer
==================

For Arria10 the inputs are the following:

* ``hps.xml`` - system definition from quartus project
* ``<prj_name>.sof`` - FPGA bitstream from quartus project
* ``enclustra-user.dts`` - user-defined kernel and u-boot devicetree files
* user-defined kernel and u-boot configs
* uses machine definition from the module layer

Prepare inputs for u-boot
-------------------------

The first input is converted to a u-boot header file using a script from
the u-boot source, whereas the second file must be converted to the ``.rbf``
bitstream format. For Arria10 the latter is split into 2 files for core
and peripheral setup.

To achieve the latter, run the quartus command shell and then something like
the following to generate both ``.rbf`` files::

  $ quartus_cpf -c --hps -o bitstream_compression=on output_files/<prj_name>.sof output_files/<prj_name>.rbf

The above should create two files named ``<prj_name>.core.rbf`` and ``<prj_name>.periph.rbf``


Boot mode switches
------------------

Note this is condensed from the reference design doc:

.. image:: assets/st1_base.png
   :width: 75%

The boot mode switches are shown in the above image as CFG (where only
the first 2 affect boot mode directly). Confirm the ON direction on your
board; use a magnifier if necessary. The following boot mode options are
extracted from the reference design document link.

:sdmmc: CFG = [1: OFF, 2: OFF, 3: ON, 4: ON]  (factory default)
:emmc: CFG = [1: ON, 2: ON, 3: ON, 4: ON]
:qspi: CFG = [1: ON, 2: OFF, 3: ON, 4: ON]

Also note boot mode is used as a configuration variable for both the HW design
build *and* the bootloader images, thus the project must be (re)built for each
boot mode in order to generate the full zipfile for the "exported_binaries"
Yocto recipe.


License
=======

All metadata is MIT licensed unless otherwise stated. Source code included
in tree for individual recipes is under the LICENSE stated in each recipe
(.bb file) unless otherwise stated.
