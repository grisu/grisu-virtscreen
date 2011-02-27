package org.bestgrid.virtscreen.view.szybki;

import grisu.control.ServiceInterface;
import grisu.frontend.control.login.LoginManager;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class SzybkiTestFrame {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SzybkiTestFrame window = new SzybkiTestFrame();

					ServiceInterface si = LoginManager
					.loginCommandline("BeSTGRID");
					window.setServiceInterface(si);

					window.setInputFile("/home/markus/Desktop/jack/szybki/example2.param");

					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private JFrame frame;

	private SzybkiInputFileGrid szybkiInputFileGrid;
	private JPanel panel;
	private TableFilterControlPanel panel_1;

	/**
	 * Create the application.
	 */
	public SzybkiTestFrame() {
		initialize();
	}

	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			panel.setLayout(new BorderLayout(0, 0));
			panel.add(getSzybkiInputFileGrid(), BorderLayout.CENTER);
			panel.add(getPanel_1(), BorderLayout.SOUTH);
		}
		return panel;
	}

	private TableFilterControlPanel getPanel_1() {
		if (panel_1 == null) {
			panel_1 = new TableFilterControlPanel(getSzybkiInputFileGrid());
		}
		return panel_1;
	}

	private SzybkiInputFileGrid getSzybkiInputFileGrid() {
		if (szybkiInputFileGrid == null) {
			szybkiInputFileGrid = new SzybkiInputFileGrid();
		}
		return szybkiInputFileGrid;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(getPanel(), BorderLayout.CENTER);
		// frame.getContentPane().add(getSzybkiInputFileGrid(),
		// BorderLayout.CENTER);
	}

	public void setInputFile(String inf) {
		getSzybkiInputFileGrid().loadSzybkiInputFile(inf);
	}

	public void setServiceInterface(ServiceInterface si) {
		getSzybkiInputFileGrid().setServiceInterface(si);
		getPanel_1().setServiceInterface(si);
	}
}
