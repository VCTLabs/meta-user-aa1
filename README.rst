Meta-user-aa1
=============

This repository contains a Yocto layer to generate the Linux reference design
for the following Enclustra SoC module family:

- Enclustra Mercury+ AA1 product series: https://www.enclustra.com/en/products/system-on-chip-modules/mercury-aa1/

The reference design is compatible with following base board:

- Enclustra Mercury+ ST1: https://www.enclustra.com/en/products/base-boards/mercury-st1


Dependencies
============

This layer depends on meta-enclustra-socfpga and OE core:

* URI: https://github.com/enclustra/meta-enclustra-socfpga/tree/v2023.1
* branch: v2023.1
* layer: meta-enclustra-module

* URI: git://git.openembedded.org/openembedded-core
* branch: [same one as checked out for this layer]

* URI: git://git.openembedded.org/meta-openembedded/meta-oe
* branch: [same one as checked out for this layer]

The primary indirect dependency is Quartus XX Std/Pro, where XX and type
depends on the SoC and u-boot version. Other versions may work with a given
project, but vendor support will most likely expect their published reqs,
eg, the versions mentioned here require the following::

	Processor                 SOCFPGA Device   Intel Quartus Pro   Intel Quartus Std
	--------------------------------------------------------------------------------
	Dual-core ARM Cortex-A9		 Cyclone V        N/A                 22.1
					                   Arria 10         23.1                N/A

All arm64 devices require Intel Quartus Pro.


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
the split FPGA bistream files via FIT image.

* meta-enclustra_
* `enclustra user layer`_

* `Intel handoff bug`_
* `rocketboards bootloader doc`_ - Arria 10, u-boot-socfpga 2024.01, linux-socfpga 6.6.22-lts, Quartus 24.2 Pro

* `U-boot-socfpga`_ - bootloader and handoff tools
* `doc/README.socfpga`_ - device-specific readme

* `Split .sof files`_ - create split bitstream files for Yocto (`also mentioned here`_)
* HWLib_ - low-level SW interface to system HW

.. _meta-enclustra: https://github.com/enclustra/meta-enclustra-socfpga/blob/v2023.1/README.md
.. _enclustra user layer: https://github.com/enclustra/meta-enclustra-socfpga/tree/v2023.1?tab=readme-ov-file#integrate-meta-enclustra-module-layer-into-user-project
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


License
=======

All metadata is MIT licensed unless otherwise stated. Source code included
in tree for individual recipes is under the LICENSE stated in each recipe
(.bb file) unless otherwise stated.
