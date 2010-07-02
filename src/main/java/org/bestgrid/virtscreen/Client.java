package org.bestgrid.virtscreen;

import org.vpac.grisu.control.ServiceInterface;
import org.vpac.grisu.control.exceptions.JobPropertiesException;
import org.vpac.grisu.control.exceptions.JobSubmissionException;
import org.vpac.grisu.frontend.control.login.LoginException;
import org.vpac.grisu.frontend.control.login.LoginManager;
import org.vpac.grisu.frontend.model.job.JobObject;

import au.org.arcs.jcommons.constants.Constants;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.UIManager;

import org.jdesktop.swingx.JXFrame;
import org.vpac.grisu.control.ServiceInterface;
import org.vpac.grisu.frontend.control.login.LoginManager;
import org.vpac.grisu.frontend.model.events.ApplicationEventListener;
import org.vpac.grisu.frontend.view.swing.login.LoginPanel;
import org.vpac.grisu.frontend.view.swing.GrisuMainPanel;
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

		return new JobCreationPanel[]{new ExampleJobCreationPanel()};
	}

	@Override
	public String getName() {
		return "grisu-virtscreen";
	}


}
