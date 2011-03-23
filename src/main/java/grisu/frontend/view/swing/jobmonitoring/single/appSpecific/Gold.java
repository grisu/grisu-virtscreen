package grisu.frontend.view.swing.jobmonitoring.single.appSpecific;

import grisu.control.ServiceInterface;
import grisu.control.exceptions.RemoteFileSystemException;
import grisu.frontend.control.clientexceptions.FileTransactionException;
import grisu.frontend.view.swing.files.preview.fileViewers.utils.JobStatusFileDialog;
import grisu.model.FileManager;
import grisu.model.GrisuRegistryManager;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

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

	private String job_status_url;
	private JLabel lblWalltime;
	private JTextField walltimeField;

	private int walltime = -1;
	private Long startTimestamp = -1L;
	private Long endTimestamp = -1L;
	private JProgressBar walltimeProgressbar;

	public Gold(ServiceInterface si) {
		super(si);
		fm = GrisuRegistryManager.getDefault(si).getFileManager();
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(12dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(46dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(32dlu;default):grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(62dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(32dlu;default):grow"),
				FormFactory.RELATED_GAP_COLSPEC, }, new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(16dlu;default)"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC, }));
		add(getLabel(), "2, 4, 3, 1, default, top");
		add(getProgressBar(), "6, 4, default, top");
		add(getWalltimeProgressbar(), "10, 4, fill, top");
		add(getLblLigandsFinished(), "2, 6, 3, 1, right, default");
		add(getTextField(), "6, 6, fill, default");
		add(getLblWalltime(), "8, 6, right, default");
		add(getWalltimeField(), "10, 6, fill, default");
		add(getSeparator(), "2, 8, 9, 1, fill, fill");
		add(getLblStatus(), "2, 10");
		add(getLblCpusUsed(), "4, 10, right, default");
		add(getCpusField(), "6, 10, fill, default");
		add(getLblLicensesUsed(), "8, 10, right, default");
		add(getLicensesField(), "10, 10, fill, default");
		add(getSeparator_1(), "2, 12, 9, 1");
		add(getBtnHistory(), "10, 14, right, top");
	}

	protected void calculateCurrentLigandNoAndCpusAndLicenses() {

		if (StringUtils.isBlank(currentStatusPath)) {
			return;
		}

		getCpusField().setText("Updating...");
		getLicensesField().setText("Updating...");
		getWalltimeField().setText("Updating...");
		getTextField().setText("Updating...");

		List<String> lines = null;
		Long timestampTemp = -1L;
		try {
			File currentStatusFile = fm.downloadFile(currentStatusPath);
			lines = FileUtils.readLines(currentStatusFile);
			timestampTemp = fm.getLastModified(currentStatusPath);
		} catch (Exception e) {
			myLogger.error(e);
			getCpusField().setText("n/a");
			getLicensesField().setText("n/a");
			getWalltimeField().setText("n/a");
			getTextField().setText("n/a");
			return;
		}

		Long deltaInSeconds = (timestampTemp - startTimestamp)/1000L;
		getWalltimeProgressbar().setValue(deltaInSeconds.intValue());

		Long hours = deltaInSeconds / 3600L;
		getWalltimeField().setText(hours + "  (of " + walltime / 3600 + ")");

		if (lines.size() != 1) {
			getCpusField().setText("Error...");
			getLicensesField().setText("Error...");
			return;
		}

		String[] tokens = lines.get(0).split(",");

		int cpusTemp = -1;
		try {
			cpusTemp = Integer.parseInt(tokens[1]);
		} catch (Exception e) {
			myLogger.error(e);
		}

		int licensesTemp = -1;
		try {
			licensesTemp = Integer.parseInt(tokens[2]);
		} catch (Exception e) {
			myLogger.error(e);
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
		} catch (Exception e) {
			myLogger.error(e);
			getTextField().setText("n/a");
			getProgressBar().setValue(0);
			return;
		}

		getTextField().setText(ligands.toString() + "  (of " + noLigands + ")");
		getProgressBar().setValue(ligands);
	}

	private File downloadJobStatusFile() throws FileTransactionException {
		File temp = fm.downloadFile(job_status_url);
		return temp;
	}

	private JButton getBtnHistory() {
		if (btnHistory == null) {
			btnHistory = new JButton("History");
			btnHistory.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

					File temp;
					try {
						temp = downloadJobStatusFile();
					} catch (FileTransactionException e1) {
						ErrorInfo ei = new ErrorInfo(
								"Download error",
								"Error while trying to download job status file.",
								e1.getLocalizedMessage(), (String) null, e1,
								Level.SEVERE, (Map) null);
						JXErrorPane.showDialog(Gold.this.getPanel(), ei);
						return;
					}

					JobStatusFileDialog dialog = new JobStatusFileDialog();
					dialog.setFileManagerAndUrl(fm, job_status_url);
					dialog.setFile(null, temp);
					setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					dialog.setVisible(true);

				}
			});
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

	private JLabel getLblWalltime() {
		if (lblWalltime == null) {
			lblWalltime = new JLabel("Walltime:");
		}
		return lblWalltime;
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

	private JTextField getWalltimeField() {
		if (walltimeField == null) {
			walltimeField = new JTextField("n/a");
			walltimeField.setHorizontalAlignment(SwingConstants.CENTER);
			walltimeField.setEditable(false);
		}
		return walltimeField;
	}

	private JProgressBar getWalltimeProgressbar() {
		if (walltimeProgressbar == null) {
			walltimeProgressbar = new JProgressBar();
			walltimeProgressbar.setEnabled(false);
		}
		return walltimeProgressbar;
	}

	@Override
	public void initialize() {

		currentStatusPath = getJob().getJobDirectoryUrl() + "/"
		+ "job_status_latest";
		statusPath = getJob().getJobDirectoryUrl() + "/" + "job_status";

		noCpus = getJob().getCpus();

		walltime = getJob().getWalltimeInSeconds();

		job_status_url = getJob().getJobDirectoryUrl() + "/" + "job_status";

	}

	@Override
	void jobFinished() {
		updateProgress();
		// getBtnArchive().setEnabled(true);
	}

	@Override
	public void jobStarted() {

		File temp = getJob().downloadAndCacheOutputFile("ligands_total");
		List<String> l = null;
		try {
			l = FileUtils.readLines(temp);
		} catch (IOException e) {
			myLogger.error(e);
			return;
		}

		String ligandsTotalUrl = getJob().getJobDirectoryUrl()
		+ "/ligands_total";
		try {
			startTimestamp = fm.getLastModified(ligandsTotalUrl);
			endTimestamp = startTimestamp + (walltime*1000);
			getWalltimeProgressbar().setEnabled(true);
			getWalltimeProgressbar().setMinimum(0);
			getWalltimeProgressbar().setMaximum(walltime);
		} catch (RemoteFileSystemException e1) {
			myLogger.debug(e1);
		}

		if (l.size() != 1) {
			myLogger.error("Can't get total number of ligands...");
			return;
		}
		try {
			noLigands = Integer.parseInt(l.get(0));
		} catch (Exception e) {
			e.printStackTrace();
			myLogger.equals(e);
			return;
		}
		getProgressBar().setEnabled(true);
		getProgressBar().setMaximum(noLigands);

		updateProgress();
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
