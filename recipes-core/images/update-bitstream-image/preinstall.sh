echo "Running $0 $*"

# make copy of current bitstream file
mount /dev/mmcblk0p1 /mnt
ls -l /mnt
cp -a /mnt/bitstream.itb /mnt/bitstream-previous.itb
umount /mnt
