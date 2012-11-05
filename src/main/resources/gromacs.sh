#!/bin/bash

#grompp -f md_relax_2 -c 4a55new_loopall_p110a_md_1_pme.gro -p 4a55new_loopall_p110a -o 4a55new_loopall_p110a_md_2_pme 1>grompp.out 2>grompp.err

#mpirun mdrun_mpi -s 4a55new_loopall_p110a_md_2_pme -o 4a55new_loopall_p110a_md_2_pme -c 4a55new_loopall_p110a_md2_pme -e 4a55new_loopall_p110a_md_2_pme -g 4a55new_loopall_p110a_md_2_pme -v


/share/apps/gromacs/gromacs_workflow_top.sh -d . -i 4a55new_loopall_p110a_after_equilmd_pme.gro -t 4a55new_loopall_p110a.top -mpi