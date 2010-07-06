package org.bestgrid.virtscreen.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.bestgrid.virtscreen.model.GoldConfFile;
import org.bestgrid.virtscreen.model.GoldJob;
import org.vpac.grisu.control.ServiceInterface;
import org.vpac.grisu.control.exceptions.JobPropertiesException;
import org.vpac.grisu.control.exceptions.JobSubmissionException;
import org.vpac.grisu.frontend.control.clientexceptions.FileTransactionException;
import org.vpac.grisu.frontend.model.job.JobObject;
import org.vpac.grisu.frontend.view.swing.jobcreation.JobCreationPanel;
import org.vpac.grisu.frontend.view.swing.jobcreation.widgets.AbstractWidget;
import org.vpac.grisu.frontend.view.swing.jobcreation.widgets.Cpus;
import org.vpac.grisu.frontend.view.swing.jobcreation.widgets.SingleInputFile;
import org.vpac.grisu.frontend.view.swing.jobcreation.widgets.SubmissionLogPanel;
import org.vpac.grisu.frontend.view.swing.jobcreation.widgets.TextCombo;
import org.vpac.grisu.frontend.view.swing.jobcreation.widgets.Walltime;

import au.org.arcs.jcommons.constants.Constants;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class GoldJobInputPanel extends JPanel implements JobCreationPanel {

	private ServiceInterface si;
	private SingleInputFile confFileInput;
	private TextCombo proteinFileCombo;
	private TextCombo ligandFileCombo;
	private TextCombo paramsFile;
	private Cpus cpus;
	private Walltime walltime;
	private SubmissionLogPanel submissionLogPanel;
	private JButton btnSubmit;

	private final String HISTORY_KEY = "virtScreenGoldJob";

	private final List<AbstractWidget> widgets = new LinkedList<AbstractWidget>();

	/**
	 * Create the panel.
	 */
	public GoldJobInputPanel() {
		super();
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC, }, new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC, }));
		add(getConfFileInput(), "2, 2, 3, 1, fill, fill");
		add(getProteinFileCombo(), "2, 4, 3, 1, fill, fill");
		add(getLigandFileCombo(), "2, 6, 3, 1, fill, fill");
		add(getTextCombo_1(), "2, 8, 3, 1, fill, fill");
		add(getCpus(), "2, 10, fill, fill");
		add(getWalltime(), "4, 10, fill, fill");
		add(getBtnSubmit(), "4, 12, right, default");
		add(getSubmissionLogPanel(), "2, 14, 3, 1, fill, fill");
	}

	public boolean createsBatchJob() {
		return false;
	}

	public boolean createsSingleJob() {
		return true;
	}

	public JPanel getPanel() {
		return this;
	}

	public String getPanelName() {
		return "Gold";
	}

	public String getSupportedApplication() {
		return Constants.GENERIC_APPLICATION_NAME;
	}

	public void setServiceInterface(ServiceInterface si) {
		this.si = si;
		for (AbstractWidget w : widgets) {
			w.setServiceInterface(si);
		}
	}

	private SingleInputFile getConfFileInput() {
		if (confFileInput == null) {
			confFileInput = new SingleInputFile();
			confFileInput.setTitle(".conf file");
			confFileInput.setHistoryKey(HISTORY_KEY + "_conf_file");
			widgets.add(confFileInput);
		}
		return confFileInput;
	}

	private TextCombo getProteinFileCombo() {
		if (proteinFileCombo == null) {
			proteinFileCombo = new TextCombo();
			proteinFileCombo.setTitle("Protein file");
			proteinFileCombo
					.setText("/home/grid-vs/virtScreen/test/alpha_correct.mol2");
			proteinFileCombo.setHistoryKey(HISTORY_KEY + "_protein_file");
			widgets.add(proteinFileCombo);
		}
		return proteinFileCombo;
	}

	private TextCombo getLigandFileCombo() {
		if (ligandFileCombo == null) {
			ligandFileCombo = new TextCombo();
			ligandFileCombo.setTitle("Ligand file");
			ligandFileCombo
					.setText("/home/grid-vs/virtScreen/test/sn_inter_single_clean.mol2");
			ligandFileCombo.setHistoryKey(HISTORY_KEY + "_ligand_file");
			widgets.add(ligandFileCombo);
		}
		return ligandFileCombo;
	}

	private TextCombo getTextCombo_1() {
		if (paramsFile == null) {
			paramsFile = new TextCombo();
			paramsFile.setTitle(".params file");
			paramsFile
					.setText("/home/grid-vs/virtScreen/test/chemscore_kin.params");
			paramsFile.setHistoryKey(HISTORY_KEY + "_params_file");
			widgets.add(paramsFile);
		}
		return paramsFile;
	}

	private Cpus getCpus() {
		if (cpus == null) {
			cpus = new Cpus();
			cpus.setHistoryKey(HISTORY_KEY + "_cpus");
			widgets.add(cpus);
		}
		return cpus;
	}

	private Walltime getWalltime() {
		if (walltime == null) {
			walltime = new Walltime();
			walltime.setHistoryKey(HISTORY_KEY + "_walltime");
			widgets.add(walltime);
		}
		return walltime;
	}

	private SubmissionLogPanel getSubmissionLogPanel() {
		if (submissionLogPanel == null) {
			submissionLogPanel = new SubmissionLogPanel();
		}
		return submissionLogPanel;
	}

	private void lockUI(final boolean lock) {

		SwingUtilities.invokeLater(new Thread() {
			@Override
			public void run() {
				getBtnSubmit().setEnabled(!lock);
				for (AbstractWidget w : widgets) {
					w.lockIUI(lock);
				}
			}
		});

	}

	private void submit() {

		GoldJob job = null;
		;
		try {
			job = new GoldJob(si, getConfFileInput().getInputFileUrl());
		} catch (FileTransactionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

		String proteinPath = getProteinFileCombo().getText();
		String ligandPath = getLigandFileCombo().getText();
		String paramsPath = getTextCombo_1().getText();
		String resultsDir = "./Results";
		String concOut = "./Results/test.sdf";

		job.setParameter(GoldConfFile.PARAMETER.protein_datafile, proteinPath);
		job.setParameter(GoldConfFile.PARAMETER.ligand_data_file, ligandPath);
		job.setParameter(GoldConfFile.PARAMETER.score_param_file, paramsPath);
		job.setParameter(GoldConfFile.PARAMETER.directory, resultsDir);
		job.setParameter(GoldConfFile.PARAMETER.concatenated_output, concOut);

		job.setCpus(getCpus().getCpus());
		job.setWalltime(getWalltime().getWalltimeInSeconds());

		try {
			lockUI(true);

			JobObject jobObject = job.createJobObject();

			getSubmissionLogPanel().setJobObject(jobObject);

			job.createAndSubmitJob();

			for (AbstractWidget w : widgets) {
				w.saveItemToHistory();
			}
		} catch (JobSubmissionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JobPropertiesException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			lockUI(false);
		}

	}

	private JButton getBtnSubmit() {
		if (btnSubmit == null) {
			btnSubmit = new JButton("Submit");
			btnSubmit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					new Thread() {
						@Override
						public void run() {
							submit();
						}
					}.start();
				}
			});
		}
		return btnSubmit;
	}
}
