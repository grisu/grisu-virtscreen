package org.bestgrid.virtscreen;

import java.awt.EventQueue;

import org.vpac.grisu.control.ServiceInterface;
import org.vpac.grisu.frontend.view.swing.GrisuApplicationWindow;
import org.vpac.grisu.frontend.view.swing.jobcreation.JobCreationPanel;

public class Client extends GrisuApplicationWindow {

	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

					GrisuApplicationWindow appWindow = new Client();
					appWindow.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	public Client() {
		super();
	}

	@Override
	public JobCreationPanel[] getJobCreationPanels() {

		return new JobCreationPanel[] { new ExampleJobCreationPanel() };
	}

	@Override
	public String getName() {
		return "grisu-virtscreen";
	}

	@Override
	public boolean displayAppSpecificMonitoringItems() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean displayBatchJobsCreationPane() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean displaySingleJobsCreationPane() {
		return true;
	}

	@Override
	protected void initOptionalStuff(ServiceInterface si) {
		// TODO Auto-generated method stub

	}

}
