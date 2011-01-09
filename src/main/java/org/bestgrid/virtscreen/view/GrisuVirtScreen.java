package org.bestgrid.virtscreen.view;

import java.awt.EventQueue;
import java.util.LinkedList;
import java.util.List;

import org.vpac.grisu.control.ServiceInterface;
import org.vpac.grisu.frontend.view.swing.GrisuApplicationWindow;
import org.vpac.grisu.frontend.view.swing.jobcreation.JobCreationPanel;
import org.vpac.grisu.model.GrisuRegistryManager;
import org.vpac.grisu.model.dto.GridFile;
import org.vpac.security.light.Init;

public class GrisuVirtScreen extends GrisuApplicationWindow {

	public static final String VIRTSCREEN_ROOTS = "virtscreen_roots";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		Init.initBouncyCastle();

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					final GrisuApplicationWindow appWindow = new GrisuVirtScreen();
					appWindow.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private JobCreationPanel gold;

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
		return new JobCreationPanel[] { getGoldJobCreationPanel() };
	}

	@Override
	public String getName() {
		return "Virtual screening client";
	}

	@Override
	protected void initOptionalStuff(ServiceInterface si) {

		GridFile p = new GridFile(
				"grid://groups/ARCS/BeSTGRID/Drug_discovery/Local//");
		p.setIsVirtual(false);
		p.setName("Personal remote files");
		p.setPath("grid://groups/ARCS/BeSTGRID/Drug_discovery/Local//");

		GridFile f = new GridFile(
				"grid://groups/ARCS/BeSTGRID/Drug_discovery//");

		f.setIsVirtual(true);
		f.setPath(f.getUrl());
		GridFile l = GrisuRegistryManager.getDefault(si).getFileManager()
				.getLocalRoot();
		List<GridFile> files = new LinkedList<GridFile>();
		files.add(p);
		files.add(f);
		files.add(l);

		GrisuRegistryManager.getDefault(si).set(VIRTSCREEN_ROOTS, files);
		addGroupFileListPanel(files, files);

	}
}
