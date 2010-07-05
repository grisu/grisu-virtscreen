package org.bestgrid.virtscreen.view;

import javax.swing.JPanel;

import org.bestgrid.virtscreen.model.GoldConfFile;
import org.bestgrid.virtscreen.model.GoldJob;
import org.globus.myproxy.GetParams;
import org.vpac.grisu.control.ServiceInterface;
import org.vpac.grisu.control.exceptions.JobPropertiesException;
import org.vpac.grisu.control.exceptions.JobSubmissionException;
import org.vpac.grisu.frontend.control.clientexceptions.FileTransactionException;
import org.vpac.grisu.frontend.view.swing.jobcreation.JobCreationPanel;

import au.org.arcs.jcommons.constants.Constants;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import org.vpac.grisu.frontend.view.swing.jobcreation.widgets.SingleInputFile;
import org.vpac.grisu.frontend.view.swing.jobcreation.widgets.TextCombo;
import org.vpac.grisu.frontend.view.swing.jobcreation.widgets.Cpus;
import org.vpac.grisu.frontend.view.swing.jobcreation.widgets.Walltime;
import org.vpac.grisu.frontend.view.swing.jobcreation.widgets.SubmissionLogPanel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

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
		return "VirtScreen";
	}

	public String getSupportedApplication() {
		return Constants.GENERIC_APPLICATION_NAME;
	}

	public void setServiceInterface(ServiceInterface si) {
		this.si = si;
		getConfFileInput().setServiceInterface(si);
	}

	private SingleInputFile getConfFileInput() {
		if (confFileInput == null) {
			confFileInput = new SingleInputFile();
			confFileInput.setTitle(".conf file");
		}
		return confFileInput;
	}

	private TextCombo getProteinFileCombo() {
		if (proteinFileCombo == null) {
			proteinFileCombo = new TextCombo();
			proteinFileCombo.setTitle("Protein file");
			proteinFileCombo.setText("/home/grid-bestgrid/virtScreen/test/alpha_correct.mol2");
		}
		return proteinFileCombo;
	}

	private TextCombo getLigandFileCombo() {
		if (ligandFileCombo == null) {
			ligandFileCombo = new TextCombo();
			ligandFileCombo.setTitle("Ligand file");
			ligandFileCombo.setText("/home/grid-bestgrid/virtScreen/test/sn_inter_single_clean.mol2");
		}
		return ligandFileCombo;
	}

	private TextCombo getTextCombo_1() {
		if (paramsFile == null) {
			paramsFile = new TextCombo();
			paramsFile.setTitle(".params file");
			paramsFile.setText("/home/grid-bestgrid/virtScreen/test/chemscore_kin.params");
		}
		return paramsFile;
	}

	private Cpus getCpus() {
		if (cpus == null) {
			cpus = new Cpus();
		}
		return cpus;
	}

	private Walltime getWalltime() {
		if (walltime == null) {
			walltime = new Walltime();
		}
		return walltime;
	}

	private SubmissionLogPanel getSubmissionLogPanel() {
		if (submissionLogPanel == null) {
			submissionLogPanel = new SubmissionLogPanel();
		}
		return submissionLogPanel;
	}

	private void submit() {

		GoldJob job = null;;
		try {
			job = new GoldJob(si,
					getConfFileInput().getInputFileUrl());
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
			job.createAndSubmitJob();
		} catch (JobSubmissionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JobPropertiesException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private JButton getBtnSubmit() {
		if (btnSubmit == null) {
			btnSubmit = new JButton("Submit");
			btnSubmit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					submit();
				}
			});
		}
		return btnSubmit;
	}
}
