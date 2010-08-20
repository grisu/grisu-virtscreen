package org.vpac.grisu.frontend.view.swing.jobmonitoring.single.appSpecific;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.apache.commons.lang.StringUtils;
import org.jmol.adapter.smarter.SmarterJmolAdapter;
import org.jmol.api.JmolAdapter;
import org.jmol.api.JmolSimpleViewer;
import org.vpac.grisu.control.JobConstants;
import org.vpac.grisu.control.ServiceInterface;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class Gold extends AppSpecificViewerPanel {
	static class JmolPanel extends JPanel {
		private static final long serialVersionUID = -3661941083797644242L;
		JmolSimpleViewer viewer;
		JmolAdapter adapter;

		final Dimension currentSize = new Dimension();

		final Rectangle rectClip = new Rectangle();

		JmolPanel() {
			adapter = new SmarterJmolAdapter();
			viewer = JmolSimpleViewer.allocateSimpleViewer(this, adapter);

		}

		public void executeCmd(String rasmolScript) {
			viewer.evalString(rasmolScript);
		}

		public JmolSimpleViewer getViewer() {
			return viewer;
		}

		public void setFile(String path) {
			viewer.openFile(path);
		}

		@Override
		public void paint(Graphics g) {
			getSize(currentSize);
			g.getClipBounds(rectClip);
			viewer.renderScreenImage(g, currentSize, rectClip);
		}
	}

	private JLabel label;
	private JProgressBar progressBar;
	private JLabel label_1;

	private JTextField textField;

	private String ligandFileNoPath = null;
	private String goldFilePath = null;
	private Thread ligandUpdateThread = null;
	private JmolPanel jmolPanel;
	private Timer timer;
	private final int PROGRESS_CHECK_INTERVALL = 120;

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
		// TODO Auto-generated constructor stub
	}

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
			progressBar.setMinimum(0);
			progressBar.setMaximum(20000);
			// progressBar.setMaximum(20);
		}
		return progressBar;
	}

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

		ligandFileNoPath = getJob().getJobProperty("result_directory") + "/"
				+ "number_of_ligands_in_library";
		goldFilePath = getJob().getJobProperty("result_directory") + "/"
				+ "gold.out";
		updateProgress();

		if (getJob().getStatus(false) < JobConstants.FINISHED_EITHER_WAY) {
			timer = new Timer();
			timer.scheduleAtFixedRate(new UpdateProgressTask(),
					PROGRESS_CHECK_INTERVALL * 1000,
					PROGRESS_CHECK_INTERVALL * 1000);

		}

		// if (getJob().getStatus(false) == JobConstants.DONE) {
		// downloadAndDisplayMolecules();
		// }
	}

	@Override
	public void jobUpdated(PropertyChangeEvent evt) {

		System.out.println("Property: " + evt.getPropertyName());
		if ("status".equals(evt.getPropertyName())) {
			updateProgress();

			int status = getJob().getStatus(false);
			if (status >= JobConstants.FINISHED_EITHER_WAY) {
				if (status == JobConstants.DONE) {
					// downloadAndDisplayMolecules();
				}

				timer.cancel();
			}
		}
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

	private void updateProgress() {

		if (getJob() == null) {
			System.out.println("Job not set yet...");
			return;
		}

		if (ligandUpdateThread != null && ligandUpdateThread.isAlive()) {
			System.out.println("Updating already...");
			return;
		}

		ligandUpdateThread = new Thread() {
			@Override
			public void run() {

				calculateCurrentLigandNo();
			}
		};

		ligandUpdateThread.start();
	}

	private void calculateCurrentLigandNo() {

		if (StringUtils.isBlank(goldFilePath)) {
			return;
		}

		long size = getJob().getFileSize(goldFilePath);
		System.out.println("Size: " + size);

		if (size <= 520) {
			return;
		}

		size = size - 520L;
		Integer ligands = new Double(size / 117.5).intValue();
		getTextField().setText(ligands.toString());
		getProgressBar().setValue(ligands);

	}

	class UpdateProgressTask extends TimerTask {

		@Override
		public void run() {

			calculateCurrentLigandNo();
		}

	}
}
