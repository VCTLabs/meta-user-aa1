#!/bin/sh
# Get enclustra SoM data from OTP eeprom on the board and output the SoM
# ID unless first arg is present; pass the 'rev' arg to switch output to
# board rev instead of ID.
#
# optional arg: pass "rev" to get board rev output, otherwise SOM id

set -eu

debecho () {
  if [ ! -z "$DEBUG" ]; then
     echo "$1" >&2
  fi
}

REV_ONLY=

if [ $# -eq 1 ] ; then
    REV_ONLY=$1
fi

LINE2=$(find /sys/devices/ -name otp | xargs sed '2q;d')
SOM_ID=$(echo $LINE2 | awk -F' ' '{printf "%s%s\n", $1, $2}')
BRD_REV=$(echo $LINE2 | awk -F' ' '{printf "%s\n", $4}')

DEBUG=
debecho "Byte string: $LINE2"
debecho "SoM family: $SOM_ID"
debecho "Board rev: $BRD_REV"

if test $REV_ONLY ; then
    echo $BRD_REV
else
    echo $SOM_ID
fi
