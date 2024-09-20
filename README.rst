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

  URI: https://github.com/enclustra/meta-enclustra-socfpga/tree/v2023.1
  branch: v2023.1

  URI: git://git.openembedded.org/openembedded-core
  branch: [same one as checked out for this layer]

  URI: git://git.openembedded.org/meta-openembedded/meta-oe
  branch: [same one as checked out for this layer]

Adding this layer to your build
===============================

In order to use this layer, you need to make the build system aware of
it.

Assuming the layer exists at the top-level of your build tree, you can add
it to the build system by adding the location of this layer to
bblayers.conf, along with any other layers needed. e.g.:

  BBLAYERS ?= " \
    /path/to/oe-core/meta \
    /path/to/meta-openembedded/meta-oe \
    /path/to/layer/meta-user-aa1 "

Optional Dynamic layer dependancy
=================================

  URI: git://git.openembedded.org/meta-openembedded/meta-oe

  URI: git://git.openembedded.org/meta-openembedded/meta-perl

  URI: git://git.openembedded.org/meta-openembedded/meta-python

  BBLAYERS += "/path/to/layer/meta-openembedded/meta-oe"
  BBLAYERS += "/path/to/layer/meta-openembedded/meta-perl"
  BBLAYERS += "/path/to/layer/meta-openembedded/meta-python"

This will activate the dynamic-layer mechanism.


License
=======

All metadata is MIT licensed unless otherwise stated. Source code included
in tree for individual recipes is under the LICENSE stated in each recipe
(.bb file) unless otherwise stated.
