package org.bestgrid.virtscreen.control;

import org.bestgrid.virtscreen.model.GoldJob;
import org.vpac.grisu.control.ServiceInterface;
import org.vpac.grisu.frontend.control.login.LoginManager;

public class GoldJobControl {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		// ServiceInterface si = LoginManager.loginCommandline("BeSTGRID");
		ServiceInterface si = LoginManager.loginCommandline("LOCAL");

		GoldJob job = new GoldJob(si,
				"/home/markus/Desktop/jack/p110a_kcs_inter_6ga_20dvs.conf");

		String proteinPath = "/home/grid-bestgrid/virtScreen/test/alpha_correct.mol2";
		String ligandPath = "/home/grid-bestgrid/virtScreen/test/sn_inter_single_clean.mol2";
		String paramsPath = "/home/grid-bestgrid/virtScreen/test/chemscore_kin.params";
		String resultsDir = "./Results";
		String concOut = "./Results/test.sdf";

		// job.setParameter(GoldConfFile.PARAMETER.protein_datafile,
		// proteinPath);
		// job.setParameter(GoldConfFile.PARAMETER.ligand_data_file,
		// ligandPath);
		// job.setParameter(GoldConfFile.PARAMETER.score_param_file,
		// paramsPath);
		// job.setParameter(GoldConfFile.PARAMETER.directory, resultsDir);
		// job.setParameter(GoldConfFile.PARAMETER.concatenated_output,
		// concOut);

		job.createAndSubmitJob();

	}
}
