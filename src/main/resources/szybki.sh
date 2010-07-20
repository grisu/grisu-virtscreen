#!/bin/sh

date

#echo 'quit'|/home/jfla018/Acsrc/Gold/start_pvm.sh &

export OE_DIR=/share/apps/bio/openeye
export OE_LICENSE=/share/apps/bio/openeye/oe_license.txt
export PVM_PATH=/share/apps/bio/openeye/arch/redhat-RHEL4-x86/szybki/1.3.4/bin
INDIR=/home/jfla018/Acsrc/Szybki/Crt_akr1c3

#clean up key files in the working directory
#none

#find node and cpu information
cat $PBS_NODEFILE|python $INDIR/oe_pvm.py > $INDIR/optH_mmpbsa.conf

# kill remainig pvm nodes first
cat $PBS_NODEFILE|sort -u|xargs -I {} -n 1 ssh {} /home/jfla018/Acsrc/Szybki/Crt_akr1c3/halt-pvm.sh

#start Szybki job
$OE_DIR/arch/redhat-RHEL4-x86/szybki/1.3.4/bin/szybki -param $INDIR/s3_snanalog_cmplt_mmpbopt_in.param -pvmconf $INDIR/optH_mmpbsa.conf

date


