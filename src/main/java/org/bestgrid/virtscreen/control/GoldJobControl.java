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

		GoldJob job = new GoldJob(si);

		job.setConfFileTemplate("/home/markus/Desktop/jack/p110a_kcs_inter_6ga_20dvs.conf");

		String proteinPath = "/home/markus/Desktop/jack/alpha_correct.mol2";
		String ligandPath = "/home/markus/Desktop/jack/sn_inter_single_clean.mol2";
		String paramsPath = "/home/markus/Desktop/jack/chemscore_kin.params";

		job.setInputFiles(proteinPath, ligandPath, paramsPath);

		job.submit();

	}
}
