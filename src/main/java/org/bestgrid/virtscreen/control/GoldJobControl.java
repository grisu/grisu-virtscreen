package org.bestgrid.virtscreen.control;

import org.vpac.grisu.control.ServiceInterface;
import org.vpac.grisu.frontend.control.login.LoginManager;

public class GoldJobControl {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		ServiceInterface si = LoginManager.loginCommandline("BeSTGRID");

	}

}
