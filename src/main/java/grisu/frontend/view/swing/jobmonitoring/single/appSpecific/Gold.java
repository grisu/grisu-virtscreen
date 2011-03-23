package grisu.frontend.view.swing.jobmonitoring.single.appSpecific;

import grisu.control.ServiceInterface;
import grisu.model.FileManager;
import grisu.model.GrisuRegistryManager;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class Gold extends AppSpecificViewerPanel {
	static class JmolPanel extends JPanel {

		private static final long serialVersionUID = -3661941083797644242L;

		final Dimension currentSize = new Dimension();

		final Rectangle rectClip = new Rectangle();

		JmolPanel() {

		}

	}

	class UpdateProgressTask extends TimerTask {

		@Override
		public void run() {

			calculateCurrentLigandNoAndCpusAndLicenses();
		}

	}

	private JLabel label;
	private JProgressBar progressBar;

	private JLabel lblLigandsFinished;

	private JTextField textField;
	// private String goldFilePath = null;
	private String currentStatusPath = null;
	private String statusPath = null;
	private JSeparator separator;
	private JLabel lblCpusUsed;
	private JLabel lblLicensesUsed;
	private JTextField cpusField;
	private JTextField licensesField;
	private JButton btnHistory;
	private JSeparator separator_1;
	private int noCpus = 0;
	private int noLigands = 0;

	private final FileManager fm;
	private JLabel lblStatus;

	public Gold(ServiceInterface si) {
		super(si);
		fm = GrisuRegistryManager.getDefault(si).getFileManager();
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(12dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(62dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(32dlu;default):grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(62dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(32dlu;default):grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC, }, new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(16dlu;default)"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC, }));
		add(getLabel(), "2, 4, 3, 1, default, top");
		add(getProgressBar(), "6, 4, 7, 1, default, top");
		add(getLblLigandsFinished(), "2, 6, 3, 1, right, default");
		add(getTextField(), "6, 6, fill, default");
		add(getSeparator(), "2, 8, 11, 1, fill, fill");
		add(getLblStatus(), "2, 10, 3, 1");
		add(getLblCpusUsed(), "2, 12, 3, 1, right, default");
		add(getCpusField(), "6, 12, fill, default");
		add(getLblLicensesUsed(), "8, 12, right, default");
		add(getLicensesField(), "10, 12, fill, default");
		add(getBtnHistory(), "12, 10, 1, 3, right, top");
		add(getSeparator_1(), "2, 14, 11, 1");
		// add(getBtnArchive(), "4, 10, right, top");
	}

	protected void calculateCurrentLigandNoAndCpusAndLicenses() {

		if (StringUtils.isBlank(currentStatusPath)) {
			return;
		}

		getCpusField().setText("Loading...");
		getLicensesField().setText("Loading...");

		List<String> lines = null;
		try {
			File currentStatusFile = fm.downloadFile(currentStatusPath);
			lines = FileUtils.readLines(currentStatusFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;

		}


		if (lines.size() != 1) {
			getCpusField().setText("Error...");
			getLicensesField().setText("Error...");
			return;
		}

		String[] tokens = lines.get(0).split(",");

		int cpusTemp = -1;
		try {
			cpusTemp = Integer.parseInt(tokens[1]);
		} catch (NumberFormatException e) {
			// do nothing;
		}

		int licensesTemp = -1;
		try {
			licensesTemp = Integer.parseInt(tokens[2]);
		} catch (NumberFormatException e) {
			// do nothing
		}

		if (cpusTemp <= 0) {
			getCpusField().setText("n/a");
		} else {
			getCpusField().setText(cpusTemp + "   (of " + noCpus + ")");
		}
		if (licensesTemp <= 0) {
			getLicensesField().setText("n/a");
		} else {
			getLicensesField().setText(licensesTemp + "   (of " + noCpus + ")");
		}


		Integer ligands = -1;
		try {
			ligands = Integer.parseInt(tokens[3]);
		} catch (NumberFormatException e) {
			// do nothing
			getTextField().setText("n/a");
			getProgressBar().setValue(0);
			return;
		}

		getTextField().setText(ligands.toString() + "  (of " + noLigands + ")");
		getProgressBar().setValue(ligands);
	}

	private JButton getBtnHistory() {
		if (btnHistory == null) {
			btnHistory = new JButton("History");
		}
		return btnHistory;
	}

	private JTextField getCpusField() {
		if (cpusField == null) {
			cpusField = new JTextField();
			cpusField.setHorizontalAlignment(SwingConstants.CENTER);
			cpusField.setText("n/a");
			cpusField.setEditable(false);
		}
		return cpusField;
	}

	private JLabel getLabel() {
		if (label == null) {
			label = new JLabel("Progress");
		}
		return label;
	}

	private JLabel getLblCpusUsed() {
		if (lblCpusUsed == null) {
			lblCpusUsed = new JLabel("Cpus used:");
		}
		return lblCpusUsed;
	}

	private JLabel getLblLicensesUsed() {
		if (lblLicensesUsed == null) {
			lblLicensesUsed = new JLabel("Licenses used:");
		}
		return lblLicensesUsed;
	}

	private JLabel getLblLigandsFinished() {
		if (lblLigandsFinished == null) {
			lblLigandsFinished = new JLabel("Ligands finished:");
		}
		return lblLigandsFinished;
	}

	private JLabel getLblStatus() {
		if (lblStatus == null) {
			lblStatus = new JLabel("Status");
		}
		return lblStatus;
	}

	private JTextField getLicensesField() {
		if (licensesField == null) {
			licensesField = new JTextField();
			licensesField.setHorizontalAlignment(SwingConstants.CENTER);
			licensesField.setText("n/a");
			licensesField.setEditable(false);
		}
		return licensesField;
	}

	private JProgressBar getProgressBar() {
		if (progressBar == null) {
			progressBar = new JProgressBar();
			progressBar.setEnabled(false);
			// progressBar.setMinimum(0);
			// progressBar.setMaximum(20000);
			// progressBar.setMaximum(20);
		}
		return progressBar;
	}

	private JSeparator getSeparator() {
		if (separator == null) {
			separator = new JSeparator();
		}
		return separator;
	}

	private JSeparator getSeparator_1() {
		if (separator_1 == null) {
			separator_1 = new JSeparator();
		}
		return separator_1;
	}

	private JTextField getTextField() {
		if (textField == null) {
			textField = new JTextField();
			textField.setHorizontalAlignment(SwingConstants.CENTER);
			textField.setEditable(false);
			textField.setText("n/a");
		}
		return textField;
	}

	@Override
	public void initialize() {

		// goldFilePath = getJob().getJobProperty("result_directory") + "/"
		// + "gold.out";

		currentStatusPath = getJob().getJobDirectoryUrl() + "/"
		+ "gold_status_latest";
		statusPath = getJob().getJobDirectoryUrl() + "/" + "gold_status";

		noCpus = getJob().getCpus();

		File temp = getJob().downloadAndCacheOutputFile("");
		List<String> l = null;
		try {
			l = FileUtils.readLines(temp);
		} catch (IOException e) {
			myLogger.error(e);
			return;
		}
		if (l.size() != 1) {
			myLogger.error("Can't get total number of ligands...");
			return;
		}
		try {
			noLigands = Integer.parseInt(l.get(1));
		} catch (NumberFormatException e) {
			myLogger.equals(e);
			return;
		}


	}

	@Override
	void jobFinished() {
		updateProgress();
		// getBtnArchive().setEnabled(true);
	}

	@Override
	public void jobStarted() {
		updateProgress();
		getProgressBar().setEnabled(true);
	}

	@Override
	public void jobUpdated(PropertyChangeEvent evt) {

	}

	@Override
	void progressUpdate() {
		updateProgress();
	}

	private void updateProgress() {

		new Thread() {
			@Override
			public void run() {
				calculateCurrentLigandNoAndCpusAndLicenses();
			}
		}.start();

	}
}
