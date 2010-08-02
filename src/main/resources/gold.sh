killall -9 pvmd3
rm -Rf /tmp/pvm*

export PVM_ROOT=/share/apps/gold/GOLD_Suite/GOLD/pvm3
nohup /share/apps/gold/GOLD_Suite/GOLD/pvm3/lib/LINUX64/pvmd3  &

GOLD_DIR=/share/apps/gold/GOLD_Suite

INDIR=`pwd`
CONF_FILE=$1


#clean up key files in the working directory. just in case
rm -f $INDIR/gold.hosts

echo " PARALLEL OPTIONS" >> $INDIR/$CONF_FILE
echo "hostfile = gold.hosts" >> $INDIR/$CONF_FILE

cat $PBS_NODEFILE|python $INDIR/gold.py > $INDIR/gold.hosts

NO_OF_CPUS=$(cat $PBS_NODEFILE|wc -l)

$GOLD_DIR/bin/parallel_gold_auto ${NO_OF_CPUS} $INDIR/$1 $INDIR/gold.hosts $INDIR

# lets calculate number of components in processed library

OUTDIR=$(grep 'directory =' /home/jfla018/grisu-dir/gold_s135261_fragsol_2010.07.27_16.47.916/*.conf|awk '{print $3}')
cat $OUTDIR/gold.out|grep 'Ligand counter'|awk '{print $8}'|sort -n -u > $OUTDIR/number_of_ligands_in_library
