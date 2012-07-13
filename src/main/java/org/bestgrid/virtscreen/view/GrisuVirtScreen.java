package org.bestgrid.virtscreen.view;

import grisu.control.ServiceInterface;
import grisu.frontend.control.login.LoginManager;
import grisu.frontend.view.swing.GrisuApplicationWindow;
import grisu.frontend.view.swing.files.virtual.GridFileTreePanel;
import grisu.frontend.view.swing.jobcreation.JobCreationPanel;
import grisu.jcommons.utils.EnvironmentVariableHelpers;

import java.awt.EventQueue;

import org.apache.log4j.Logger;
import org.bestgrid.virtscreen.control.VirtScreenEnvironment;
import org.bestgrid.virtscreen.view.gold.GoldJobInputPanelNew;
import org.bestgrid.virtscreen.view.szybki.SzybkiJobCreationPanel;

public class GrisuVirtScreen extends GrisuApplicationWindow {

	static final Logger myLogger = Logger
			.getLogger(GrisuVirtScreen.class.getName());

	public static final String VIRTSCREEN_ROOTS = "virtscreen_roots";

	public static final String SUBMISSION_VO = "/nz/virtual-screening/jobs";
	// public static final String SUBMISSION_VO =
	// "/ARCS/BeSTGRID/Drug_discovery/Local";

	public static final String SUBMISSION_LOCATION = "gold:gram5.ceres.auckland.ac.nz";
	// public static final String SUBMISSION_LOCATION =
	// "gold@er171.ceres.auckland.ac.nz:ng2.auckland.ac.nz";

	// public static final String GOLD_VERSION = "5.1";
	public static final String GOLD_VERSION = "5.1";

	public static void main(String[] args) throws Exception {
		GrisuVirtScreen app = new GrisuVirtScreen();
		app.run();
	}

	private JobCreationPanel gold;
	private JobCreationPanel szybki;

	/**
	 * Create the application.
	 */
	public GrisuVirtScreen() throws Exception {
		super();
	}

	@Override
	public boolean displayAppSpecificMonitoringItems() {
		return true;
	}

	@Override
	public boolean displayBatchJobsCreationPane() {
		return false;
	}

	@Override
	public boolean displaySingleJobsCreationPane() {
		return true;
	}

	private JobCreationPanel getGoldJobCreationPanel() {
		if (gold == null) {
			gold = new GoldJobInputPanelNew();
		}
		return gold;
	}

	@Override
	public JobCreationPanel[] getJobCreationPanels() {
		// return new JobCreationPanel[] { getGoldJobCreationPanel(),
		// getSzybkiJobCreationPanel() };
		return new JobCreationPanel[] { getGoldJobCreationPanel() };
	}

	@Override
	public String getName() {
		return "Virtual screening client";
	}

	private JobCreationPanel getSzybkiJobCreationPanel() {
		if (szybki == null) {
			szybki = new SzybkiJobCreationPanel();
		}
		return szybki;
	}

	@Override
	protected void initOptionalStuff(ServiceInterface si) {

		GridFileTreePanel.defaultRoots.clear();
		GridFileTreePanel.defaultRoots.put("Data Fabric",
				"grid://groups/nz/nesi//");

		GridFileTreePanel.defaultRoots.put("Virtual Screening",
				"grid://groups/nz/virtual-screening//");
		GridFileTreePanel.defaultRoots.put("Jobs", "grid://jobs");
		GridFileTreePanel.defaultRoots.put(GridFileTreePanel.LOCAL_ALIAS, null);
		addGroupFileListPanel(null, null);


	}

	/**
	 * Launch the application.
	 */
	public void run() {

		Thread.currentThread().setName("main");

		LoginManager.setClientName("virtscreen");

		LoginManager.setClientVersion(grisu.jcommons.utils.Version
				.get("this-client"));

		EnvironmentVariableHelpers.loadEnvironmentVariablesToSystemProperties();

		LoginManager.initEnvironment();

		VirtScreenEnvironment.init();

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					final GrisuApplicationWindow appWindow = new GrisuVirtScreen();
					appWindow.setVisible(true);
				} catch (final Exception e) {
					myLogger.error(e.getLocalizedMessage(), e);
				}
			}
		});
	}
}
