GOLD_DIR=/share/apps/gold/GOLD_Suite
#INDIR=/home/grid-bestgrid/Gold/Alpha_kcs_inter_6ga_20dvs
INDIR=.

#clean up key files in the working directory
rm -f $INDIR/Results/gold.pid
rm -f $INDIR/gold.hosts

echo " PARALLEL OPTIONS" >> $INDIR/p110a_kcs_inter_6ga_20dvs.conf
echo "hostfile = gold.hosts" >> $INDIR/p110a_kcs_inter_6ga_20dvs.conf

cat $PBS_NODEFILE|python $INDIR/gold.py > $INDIR/gold.hosts

$GOLD_DIR/bin/parallel_gold_auto 8 $INDIR/p110a_kcs_inter_6ga_20dvs.conf $INDIR/gold.hosts $INDIR
GOLD_DIR=/share/apps/gold/GOLD_Suite
INDIR=/home/grid-bestgrid/Gold/Alpha_kcs_inter_6ga_20dvs

#clean up key files in the working directory
rm -f $INDIR/Results/gold.pid
rm -f $INDIR/gold.hosts

echo " PARALLEL OPTIONS" >> $INDIR/p110a_kcs_inter_6ga_20dvs.conf
echo "hostfile = gold.hosts" >> $INDIR/p110a_kcs_inter_6ga_20dvs.conf

cat $PBS_NODEFILE|python $INDIR/gold.py > $INDIR/gold.hosts

$GOLD_DIR/bin/parallel_gold_auto 8 $INDIR/p110a_kcs_inter_6ga_20dvs.conf $INDIR/gold.hosts $INDIR
