echo "SWU: $0 $*"

E2FSCK=$(which e2fsck)
E2RESIZE=$(which resize2fs)
E2TUNE=$(which tune2fs)

NEWROOT=$(fw_printenv -n next_part)
NEWDEV="/dev/mmcblk0p${NEWROOT}"

if [ "$NEWROOT" = 5 ]; then
    NEWLBL="root"
else
    NEWLBL="root2"
fi

echo "SWU: Applying fs checks and resize to ${NEWDEV}"
$E2FSCK -fp $NEWDEV
$E2RESIZE $NEWDEV 2>&1
$E2TUNE -O ^metadata_csum -L $NEWLBL $NEWDEV

echo "SWU: copying host keys and machine-id to ${NEWDEV}"
mount $NEWDEV /mnt/
cp -a /etc/ssh/ssh_host_*_key* /mnt/etc/ssh/
cp -a /etc/machine-id /mnt/etc/
sync
umount /mnt

if [ "$?" = 0 ]; then
    echo "SWU: Image update success!!"
else
    echo "SWU: Something went wrong!!"
fi
