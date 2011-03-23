# kill pvm daemons and remove stale files
killall -9 pvmd3
rm -Rf /tmp/pvm*

export GOLD_DIR=/share/apps/gold/GOLD
export CCDC_LICENSE_FILE=27000@er171.ceres.auckland.ac.nz
export PVM_ROOT=${GOLD_DIR}/pvm3

export INDIR=`pwd`
export CONF_FILE=$1

# remove stale licenses for user
function release_licenses () {
    ${GOLD_DIR}/bin/gold_licence  statall|
     grep ${USER}|
      awk '{print "${GOLD_DIR}/gold/d_linux_64/bin/lmutil lmremove  -h gold " $2 " 27000 "  substr($6,0, length($6)-2)}'|
      sh
}

function check_license_status () {
    ${GOLD_DIR}/bin/gold_licence  statall >> ${INDIR}/gold_license_status
}

function check_job_status () {
    TIMESTAMP=$(date  +'%s')
    NUMBER_OF_CPUS=$(echo 'ps -a'|pvm|grep gold_parallel.sh|wc -l)
    NUMBER_OF_LICENSES=$[$(/share/apps/gold/GOLD_Suite/bin/gold_licence statall \
	|awk 'BEGIN {a = 0}; /Users of silver/ {a = 0}; /floating license/ {a = 1}; //{ if (a == 1) print $0}' \
	|wc -l) - 3]
    NUMBER_OF_COMPONENTS=$(calculate_components)

    RESULT="$TIMESTAMP,$NUMBER_OF_CPUS,$NUMBER_OF_LICENSES,$NUMBER_OF_COMPONENTS"

    echo $RESULT >> ${INDIR}/job_status
    echo $RESULT > ${INDIR}/job_status_latest
}

function check_job_status_daemon () {
    while [ 1 == 1 ]; do  check_job_status;  sleep 300; done
}

function ligands_total () {
    LIBRARY_FILE=$(grep ligand_data_file ${INDIR}/${CONF_FILE}|awk '{print $2}')
    grep ZINC $LIBRARY_FILE|wc -l > ${INDIR}/ligands_total
}

function calculate_components () {
  OUTDIR=$(grep 'directory =' $CONF_FILE|
      awk '{print $3}')
  cat ${OUTDIR}/gold.out|
      grep 'Ligand counter'|
      awk '{print $8}'|
      sort -n -u|tail -n 1
}
# > ${OUTDIR}/number_of_ligands_in_library


function start_pvm_daemon () {
    nohup ${PVM_ROOT}/lib/LINUX64/pvmd3  &
}

start_pvm_daemon

ligands_total
release_licenses
check_license_status


#clean up key files in the working directory. just in case
rm -f ${INDIR}/gold.hosts

echo " PARALLEL OPTIONS" >> ${INDIR}/${CONF_FILE}
echo "hostfile = gold.hosts" >> ${INDIR}/${CONF_FILE}

cat ${PBS_NODEFILE}|python ${INDIR}/gold.py > ${INDIR}/gold.hosts

NO_OF_CPUS=$(cat ${PBS_NODEFILE}|wc -l)

trap 'kill $DAEMON_PID; release_licenses; check_license_status;' INT TERM EXIT

check_job_status_daemon &
DAEMON_PID=$!

${GOLD_DIR}/bin/parallel_gold_auto ${NO_OF_CPUS} ${INDIR}/$1 ${INDIR}/gold.hosts ${INDIR}

trap - INT TERM EXIT

kill $DAEMON_PID
release_licenses
check_license_status



