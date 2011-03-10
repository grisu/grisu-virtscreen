#!/bin/sh

date




#echo 'quit'|/home/jfla018/Acsrc/Gold/start_pvm.sh &

export SZYBKI_VERSION=1.5.2
export SZYBKI_ARCH=redhat-RHEL-4-x64
export OE_DIR=/share/apps/bio/openeye

export OE_LICENSE=/share/apps/bio/openeye/oe_license.txt

export SZYBKI_PATH=${OE_DIR}/arch/${SZYBKI_ARCH}/${SZYBKI_VERSION}
export PVM_PATH=${SZYBKI_PATH}/bin

INDIR=`pwd`


# ----------------------------------------------------------------
echo "creating optH_mmpbsa.conf..."
cat $PBS_NODEFILE|python $INDIR/szybki.py > $INDIR/optH_mmpbsa.conf

echo "In real life I would have started this command:"
echo "${SZYBKI_PATH}/bin/szybki -param $INDIR/$1 -pvmconf $INDIR/optH_mmpbsa.conf"

# just for now...
sleep 60
exit 0
# -----------------------------------------------------------------

#clean up key files in the working directory
#none

#find node and cpu information
cat $PBS_NODEFILE|python $INDIR/szybki.py > $INDIR/optH_mmpbsa.conf

# kill remainig pvm nodes first
cat $PBS_NODEFILE|sort -u|xargs -I {} -n 1 ssh {} "echo halt|pvm"

#start Szybki job
${SZYBKI_PATH}/bin/szybki -param $INDIR/s3_snanalog_cmplt_mmpbopt_in.param -pvmconf $INDIR/optH_mmpbsa.conf

date


