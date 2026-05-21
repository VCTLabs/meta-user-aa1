#!/bin/sh
set -eu

BOOTCOUNT=$(fw_printenv -n bootcount 2>/dev/null)
SSHD=$(systemctl status sshd.socket | grep -q "active (listening)")
FAILED=$(systemctl --failed | grep -q "0 loaded units")

if [ -z "${SSHD}" ] && [ -z "${FAILED}" ] ; then
    if [ "${BOOTCOUNT}" -gt 0 ]; then
        echo "Startup checks pass and boot count ${BOOTCOUNT} triggering reset to zero"
        fw_setenv bootcount 0
    fi
    exit 0
fi

[ -n "${SSHD}" ] && echo "sshd socket status: ${SSHD}"
[ -n "${FAILED}" ] && echo "systemd fail status: ${FAILED}"
[ -n "${SSHD}" ] || [ -n "${FAILED}" && exit 1
