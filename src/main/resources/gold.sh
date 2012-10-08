# kill pvm daemons and remove stale files
killall -9 pvmd3
rm -Rf /tmp/pvm*

#export GOLD_DIR=/share/apps/gold/GOLD
export GOLD_DIR=/share/apps/goldsuite-5.1/GOLD
export GOLD_LICENSE=${GOLD_DIR}/bin/gold_licence
export CCDC_LICENSE_FILE=27004@xcat-p
#export CCDC_LICENSE_FILE=/share/apps/goldsuite-5.1/ccdc_licence.dat
export PVM_ROOT=${GOLD_DIR}/pvm3

export INDIR=`pwd`
export CONF_FILE=$1

# remove stale licenses for user
function release_licenses () {
    ${GOLD_DIR}/bin/gold_licence  statall|
     grep ${USER}|
      awk '{print "${GOLD_DIR}/gold/d_linux_64/bin/lmutil lmremove  -h gold " $2 " 27004 "  substr($6,0, length($6)-2)}'|
      sh
}

function check_license_status () {
    echo "Checking license status"
    ${GOLD_LICENSE}  statall|sed  's/Uncounted/32/g' >> ${INDIR}/gold_license_status
}

function check_job_status () {
    TIMESTAMP=$(date  +'%s')
    echo "Checking job status at time $TIMESTAMP"
    NUMBER_OF_CPUS=$(echo 'ps -a'|pvm|grep gold_parallel.sh|wc -l)
    echo "total number of CPUS is $NUMBER_OF_CPUS"
    NUMBER_OF_LICENSES=$[$(${GOLD_LICENSE} statall \
	|awk 'BEGIN {a = 0}; /Users of silver/ {a = 0}; /floating license/ {a = 1}; //{ if (a == 1) print $0}' \
	|wc -l) - 3]
    echo "total number of licenses $NUMBER_OF_LICENSES"

    NUMBER_OF_LICENSES_USER=$(${GOLD_LICENSE} statall |grep $USER|wc -l)
    echo "number of licenses per user $NUMBER_OF_LICENSES_USER"

    NUMBER_OF_COMPONENTS=$(calculate_components)

    RESULT="$TIMESTAMP,$NUMBER_OF_CPUS,$NUMBER_OF_LICENSES_USER,$NUMBER_OF_LICENSES,$NUMBER_OF_COMPONENTS"

    echo $RESULT >> ${INDIR}/job_status
    echo $RESULT > ${INDIR}/job_status_latest
}

function check_job_status_daemon () {
    while [ 1 == 1 ]; do  check_job_status;  sleep 60; done
}

function ligands_total () {
    LIBRARY_FILE=$(grep ligand_data_file ${INDIR}/${CONF_FILE}|awk '{print $2}')
    grep ZINC $LIBRARY_FILE|wc -l > ${INDIR}/ligands_total
}

function calculate_components () {
  OUTDIR=$(grep 'directory =' $CONF_FILE|
      awk '{print $3}')
  LIGAND_NUMBER=$(cat ${OUTDIR}/gold.out|
      grep 'Ligand counter'|
      awk '{print $8}'|
      sort -n -u|xargs -n 1 echo|tail -n 1)
  LIGAND_NUMBER=${LIGAND_NUMBER:-0}
  echo $LIGAND_NUMBER
}
# > ${OUTDIR}/number_of_ligands_in_library


function start_pvm_daemon () {
    nohup ${PVM_ROOT}/lib/LINUX64/pvmd3  &
}

dos2unix ${CONF_FILE} 

start_pvm_daemon

ligands_total
release_licenses
check_license_status


#clean up key files in the working directory. just in case
rm -f ${INDIR}/gold.hosts

echo " PARALLEL OPTIONS" >> ${INDIR}/${CONF_FILE}
echo "hostfile = gold.hosts" >> ${INDIR}/${CONF_FILE}

cat ${LOADL_HOSTFILE}|python ${INDIR}/gold.py > ${INDIR}/gold.hosts

NO_OF_CPUS=$(cat ${LOADL_HOSTFILE}|wc -l)

trap 'kill $DAEMON_PID; release_licenses; check_license_status;' INT TERM EXIT

check_job_status_daemon &
DAEMON_PID=$!

${GOLD_DIR}/bin/parallel_gold_auto ${NO_OF_CPUS} ${INDIR}/$1 ${INDIR}/gold.hosts ${INDIR}

trap - INT TERM EXIT

kill $DAEMON_PID
release_licenses
check_license_status

