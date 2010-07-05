export PVM_ROOT=/share/apps/gold/GOLD_Suite/GOLD/pvm3
nohup /share/apps/gold/GOLD_Suite/GOLD/pvm3/lib/LINUX64/pvmd3  &

GOLD_DIR=/share/apps/gold/GOLD_Suite
INDIR=.

CONF_FILE=$1

#clean up key files in the working directory. just in case
rm -f $INDIR/gold.hosts

echo " PARALLEL OPTIONS" >> $INDIR/$1
echo "hostfile = gold.hosts" >> $INDIR/$1

cat $PBS_NODEFILE|python $INDIR/gold.py > $INDIR/gold.hosts

$GOLD_DIR/bin/parallel_gold_auto 8 $INDIR/$1 $INDIR/gold.hosts $INDIR
