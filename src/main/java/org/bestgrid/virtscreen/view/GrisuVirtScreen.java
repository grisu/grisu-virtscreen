package org.bestgrid.virtscreen.view;

import grisu.control.ServiceInterface;
import grisu.control.exceptions.RemoteFileSystemException;
import grisu.frontend.view.swing.GrisuApplicationWindow;
import grisu.frontend.view.swing.jobcreation.JobCreationPanel;
import grisu.model.GrisuRegistryManager;
import grisu.model.dto.GridFile;
import grith.jgrith.Init;

import java.awt.EventQueue;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.bestgrid.virtscreen.control.VirtScreenEnvironment;
import org.bestgrid.virtscreen.view.gold.GoldJobInputPanelNew;
import org.bestgrid.virtscreen.view.szybki.SzybkiJobCreationPanel;

public class GrisuVirtScreen extends GrisuApplicationWindow {

	static final Logger myLogger = Logger.getLogger(GrisuVirtScreen.class
			.getName());

	public static final String VIRTSCREEN_ROOTS = "virtscreen_roots";

	public static final String SUBMISSION_VO = "/nz/virtual-screening/jobs";

	public static final String SUBMISSION_LOCATION = "gold:gram5.ceres.auckland.ac.nz";

	public static final String GOLD_VERSION = "5.1";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		Init.initBouncyCastle();

		VirtScreenEnvironment.init();

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					final GrisuApplicationWindow appWindow = new GrisuVirtScreen();
					appWindow.setVisible(true);
				} catch (Exception e) {
					myLogger.error(e);
				}
			}
		});
	}

	private JobCreationPanel gold;
	private JobCreationPanel szybki;

	/**
	 * Create the application.
	 */
	public GrisuVirtScreen() {
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

		GridFile df = null;
		try {
			df = GrisuRegistryManager.getDefault(si).getFileManager()
					.createGridFile("grid://groups/nz/nesi//");
			df.setName("Data Fabric");
		} catch (RemoteFileSystemException e) {
			myLogger.error(e);
			// p = new GridFile(
			// "grid://groups/ARCS/BeSTGRID/Drug_discovery/Local//");
			// p.setIsVirtual(false);
			// p.setName("Personal remote files");
			// p.setPath("grid://groups/ARCS/BeSTGRID/Drug_discovery/Local//");
		}
		// GridFile p = null;
		// try {
		// p = GrisuRegistryManager
		// .getDefault(si)
		// .getFileManager()
		// .createGridFile("grid://groups/nz/nesi//");
		// df.setName("Data Fabric");
		// } catch (RemoteFileSystemException e) {
		// e.printStackTrace();
		// // p = new GridFile(
		// // "grid://groups/ARCS/BeSTGRID/Drug_discovery/Local//");
		// // p.setIsVirtual(false);
		// // p.setName("Personal remote files");
		// // p.setPath("grid://groups/ARCS/BeSTGRID/Drug_discovery/Local//");
		// }

		GridFile f = null;
		try {
			f = GrisuRegistryManager
					.getDefault(si)
					.getFileManager()
					.createGridFile("grid://groups/nz/virtual-screening//");
			f.setName("Virtual Screening");
		} catch (RemoteFileSystemException e) {
			myLogger.error(e);
			// f = new GridFile("grid://groups/ARCS/BeSTGRID/Drug_discovery//");
			//
			// f.setIsVirtual(true);
			// f.setPath(f.getUrl());
		}

		GridFile j = null;
		try {
			j = GrisuRegistryManager.getDefault(si).getFileManager()
					.createGridFile("grid://jobs");
			j.setName("Jobs");
		} catch (RemoteFileSystemException e) {
			myLogger.error(e);
			// f = new GridFile("grid://groups/ARCS/BeSTGRID/Drug_discovery//");
			//
			// f.setIsVirtual(true);
			// f.setPath(f.getUrl());
		}

		GridFile l = GrisuRegistryManager.getDefault(si).getFileManager()
				.getLocalRoot();
		List<GridFile> files = new LinkedList<GridFile>();
		if (df != null) {
			files.add(df);
		}
		// if (p != null) {
		// files.add(p);
		// }
		if (f != null) {
			files.add(f);
		}
		if (j != null) {
			files.add(j);
		}
		if (l != null) {
			files.add(l);
		}
		//
		GrisuRegistryManager.getDefault(si).set(VIRTSCREEN_ROOTS, files);
		addGroupFileListPanel(files, files);

	}
}
