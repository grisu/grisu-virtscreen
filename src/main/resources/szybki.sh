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

#clean up key files in the working directory
#none

#find node and cpu information
cat $PBS_NODEFILE|python $INDIR/oe_pvm.py > $INDIR/optH_mmpbsa.conf

# kill remainig pvm nodes first
cat $PBS_NODEFILE|sort -u|xargs -I {} -n 1 ssh {} "bash halt-pvm.sh"

#start Szybki job
${SZYBKI_PATH}/bin/szybki -param $INDIR/s3_snanalog_cmplt_mmpbopt_in.param -pvmconf $INDIR/optH_mmpbsa.conf

date


