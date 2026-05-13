#!/bin/sh
set -eu

BOOTCOUNT=$(fw_printenv -n bootcount 2>/dev/null)
SSHD=$(systemctl status sshd.socket | grep -q "active (listening)")
FAILED=$(systemctl --failed | grep -q "0 loaded units")

if [ -z "${SSHD}" ] && [ -z "${FAILED}" ] ; then
    if [ "${BOOTCOUNT}" -gt 0 ]; then
        fw_setenv bootcount 0
    fi
fi
