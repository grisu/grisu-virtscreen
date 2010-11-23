package org.bestgrid.virtscreen.view;

import java.awt.EventQueue;

import org.vpac.grisu.control.ServiceInterface;
import org.vpac.grisu.frontend.view.swing.GrisuApplicationWindow;
import org.vpac.grisu.frontend.view.swing.jobcreation.JobCreationPanel;
import org.vpac.security.light.Init;

public class GrisuVirtScreen extends GrisuApplicationWindow {

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

	}

	// private void exit() {
	// try {
	// System.out.println("Exiting...");
	//
	// if (si != null) {
	// si.logout();
	// }
	//
	// } finally {
	// WindowSaver.saveSettings();
	// System.exit(0);
	// }
	// }

	// /**
	// * Initialize the contents of the frame.
	// */
	// private void initialize() {
	// frame = new JXFrame();
	// frame.addWindowListener(this);
	// // frame.setBounds(100, 100, 450, 300);
	// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	//
	// frame.setTitle("Virtual screening / Drug discovery grid client");
	//
	// frame.getContentPane().setLayout(new BorderLayout());
	// Set<String> apps = new HashSet<String>();
	// apps.add("generic");
	// mainPanel = new GrisuMainPanel(true, false, false, apps, false, false,
	// false, null, true);
	// mainPanel.addJobCreationPanel(new GoldJobInputPanel());
	// // mainPanel.addJobCreationPanel(new BlenderJobCreationPanel());
	// // TODO add creationpanel
	// // LoginPanel lp = new LoginPanel(mainPanel, true);
	// LoginPanel lp = new LoginPanel(mainPanel);
	// frame.getContentPane().add(lp, BorderLayout.CENTER);
	// }

	// public void setServiceInterface(ServiceInterface si) {
	//
	// if (lp == null) {
	// throw new IllegalStateException("LoginPanel not initialized.");
	// }
	//
	// if (si == null) {
	// throw new NullPointerException("ServiceInterface can't be null");
	// }
	//
	// lp.setServiceInterface(si);
	//
	// }

}
