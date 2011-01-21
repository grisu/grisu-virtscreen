package org.vpac.grisu.frontend.view.swing.jobmonitoring.single.appSpecific;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.util.TimerTask;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.apache.commons.lang.StringUtils;
import org.vpac.grisu.control.ServiceInterface;
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

			calculateCurrentLigandNo();
		}

	}

	private JLabel label;
	private JProgressBar progressBar;

	private JLabel label_1;

	private JTextField textField;
	private String goldFilePath = null;
	private JButton btnArchive;

	public Gold(ServiceInterface si) {
		super(si);
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC, }, new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC, }));
		add(getLabel(), "2, 2, 3, 1");
		add(getProgressBar(), "2, 4, 3, 1");
		add(getLabel_1(), "2, 6, right, default");
		add(getTextField(), "4, 6, fill, default");
		// add(getBtnArchive(), "4, 10, right, top");
	}

	private void calculateCurrentLigandNo() {

		if (StringUtils.isBlank(goldFilePath)) {
			return;
		}

		long size = getJob().getFileSize(goldFilePath);

		if (size <= 520) {
			return;
		}

		size = size - 520L;
		Integer ligands = new Double(size / 117.5).intValue();
		getTextField().setText(ligands.toString());
		// getProgressBar().setValue(ligands);

	}

	// private JButton getBtnArchive() {
	// if (btnArchive == null) {
	// btnArchive = new JButton("Archive");
	// btnArchive.setEnabled(false);
	// btnArchive.addActionListener(new ActionListener() {
	// public void actionPerformed(ActionEvent e) {
	//
	// new Thread() {
	// @Override
	// public void run() {
	// try {
	//
	// getBtnArchive().setEnabled(false);
	//
	// BackgroundActionProgressDialogSmall d = new
	// BackgroundActionProgressDialogSmall(
	// "Archiving job:", getJob().getJobname());
	// getJob().archive(null, true);
	// d.close();
	//
	// } catch (JobPropertiesException e1) {
	// ErrorInfo info = new ErrorInfo(
	// "Job archive error",
	// "Can't archive job:\n\n"
	// + e1.getLocalizedMessage(),
	// null, "Error", e1, Level.SEVERE, null);
	//
	// JXErrorPane pane = new JXErrorPane();
	// pane.setErrorInfo(info);
	//
	// JXErrorPane.showDialog(Gold.this.getRootPane(),
	// pane);
	// getBtnArchive().setEnabled(true);
	// } catch (RemoteFileSystemException e1) {
	// ErrorInfo info = new ErrorInfo(
	// "Job archive error",
	// "Can't archive job:\n\n"
	// + e1.getLocalizedMessage(),
	// null, "Error", e1, Level.SEVERE, null);
	//
	// JXErrorPane pane = new JXErrorPane();
	// pane.setErrorInfo(info);
	//
	// JXErrorPane.showDialog(Gold.this.getRootPane(),
	// pane);
	// getBtnArchive().setEnabled(true);
	// }
	// }
	// }.start();
	//
	// }
	// });
	// }
	// return btnArchive;
	// }

	private JLabel getLabel() {
		if (label == null) {
			label = new JLabel("Progress");
		}
		return label;
	}

	private JLabel getLabel_1() {
		if (label_1 == null) {
			label_1 = new JLabel("Ligands finished (approx.):");
		}
		return label_1;
	}

	private JProgressBar getProgressBar() {
		if (progressBar == null) {
			progressBar = new JProgressBar();
			// progressBar.setMinimum(0);
			// progressBar.setMaximum(20000);
			// progressBar.setMaximum(20);
		}
		return progressBar;
	}

	// private void downloadAndDisplayMolecules() {
	//
	// String resDir = getJob().getJobProperty("result_directory");
	// String concOut = getJob().getJobProperty(
	// PARAMETER.concatenated_output.toString());
	//
	// if (StringUtils.isBlank(resDir) || StringUtils.isBlank(concOut)) {
	// return;
	// }
	// final String conc = resDir + "/" + concOut;
	// System.out.println("conc: " + conc);
	//
	// new Thread() {
	// @Override
	// public void run() {
	// File file = getJob().downloadAndCacheOutputFile(conc);
	// getJmolPanel_1().setFile(file.getPath());
	// }
	// }.start();
	//
	// }

	private JTextField getTextField() {
		if (textField == null) {
			textField = new JTextField();
			textField.setHorizontalAlignment(SwingConstants.CENTER);
			textField.setEditable(false);
			textField.setColumns(10);
			textField.setText("n/a");
		}
		return textField;
	}

	@Override
	public void initialize() {

		goldFilePath = getJob().getJobProperty("result_directory") + "/"
				+ "gold.out";
	}

	@Override
	void jobFinished() {
		updateProgress();
		getProgressBar().setIndeterminate(false);
		// getBtnArchive().setEnabled(true);
	}

	@Override
	public void jobStarted() {
		updateProgress();
		getProgressBar().setIndeterminate(true);
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
				calculateCurrentLigandNo();
			}
		}.start();

	}
}
