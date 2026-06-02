echo "PRE: $0 $*"
echo "possibly mounting boot for u-boot-env..."
IS_MOUNTED=$(mount -l -t vfat)
test -z "$IS_MOUNTED" && mount /boot
echo "PRE: $?"
