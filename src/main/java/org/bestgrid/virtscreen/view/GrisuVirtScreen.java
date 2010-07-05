package org.bestgrid.virtscreen.view;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.UIManager;

import org.jdesktop.swingx.JXFrame;
import org.vpac.grisu.control.ServiceInterface;
import org.vpac.grisu.frontend.control.login.LoginManager;
import org.vpac.grisu.frontend.model.events.ApplicationEventListener;
import org.vpac.grisu.frontend.view.swing.GrisuMainPanel;
import org.vpac.grisu.frontend.view.swing.WindowSaver;
import org.vpac.grisu.frontend.view.swing.login.LoginPanel;

public class GrisuVirtScreen implements WindowListener {
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		LoginManager.initEnvironment();

		Toolkit tk = Toolkit.getDefaultToolkit();
		tk.addAWTEventListener(WindowSaver.getInstance(),
				AWTEvent.WINDOW_EVENT_MASK);

		new ApplicationEventListener();

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {

		}

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GrisuVirtScreen window = new GrisuVirtScreen();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private ServiceInterface si;

	private GrisuMainPanel mainPanel;

	private LoginPanel lp;

	private JXFrame frame;

	/**
	 * Create the application.
	 */
	public GrisuVirtScreen() {
		initialize();
	}

	private void exit() {
		try {
			System.out.println("Exiting...");

			if (si != null) {
				si.logout();
			}

		} finally {
			WindowSaver.saveSettings();
			System.exit(0);
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JXFrame();
		frame.addWindowListener(this);
		// frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.getContentPane().setLayout(new BorderLayout());
		Set<String> apps = new HashSet<String>();
		apps.add("generic");
		mainPanel = new GrisuMainPanel(true, apps);
		mainPanel.addJobCreationPanel(new GoldJobInputPanel());
		// mainPanel.addJobCreationPanel(new BlenderJobCreationPanel());
		// TODO add creationpanel
		// LoginPanel lp = new LoginPanel(mainPanel, true);
		LoginPanel lp = new LoginPanel(mainPanel);
		frame.getContentPane().add(lp, BorderLayout.CENTER);
	}

	public void setServiceInterface(ServiceInterface si) {

		if (lp == null) {
			throw new IllegalStateException("LoginPanel not initialized.");
		}

		if (si == null) {
			throw new NullPointerException("ServiceInterface can't be null");
		}

		lp.setServiceInterface(si);

	}

	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowClosing(WindowEvent e) {
		exit();
	}

	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

}
