killall -9 pvmd3
rm -Rf /tmp/pvm*

export PVM_ROOT=/share/apps/gold/GOLD_Suite/GOLD/pvm3
nohup /share/apps/gold/GOLD_Suite/GOLD/pvm3/lib/LINUX64/pvmd3  &

GOLD_DIR=/share/apps/gold/GOLD_Suite
#INDIR=/home/grid-bestgrid/Gold/Alpha_kcs_inter_6ga_20dvs
INDIR=`pwd`

#clean up key files in the working directory
rm -f $INDIR/Results/gold.pid
rm -f $INDIR/gold.hosts

echo " PARALLEL OPTIONS" >> $INDIR/p110a_kcs_inter_6ga_20dvs.conf
echo "hostfile = gold.hosts" >> $INDIR/p110a_kcs_inter_6ga_20dvs.conf

cat $PBS_NODEFILE|python $INDIR/gold.py > $INDIR/gold.hosts

#clean up key files in the working directory
rm -f $INDIR/Results/gold.pid
rm -f $INDIR/gold.hosts

echo " PARALLEL OPTIONS" >> $INDIR/p110a_kcs_inter_6ga_20dvs.conf
echo "hostfile = gold.hosts" >> $INDIR/p110a_kcs_inter_6ga_20dvs.conf

cat $PBS_NODEFILE|python $INDIR/gold.py > $INDIR/gold.hosts

$GOLD_DIR/bin/parallel_gold_auto 8 $INDIR/p110a_kcs_inter_6ga_20dvs.conf $INDIR/gold.hosts $INDIR
