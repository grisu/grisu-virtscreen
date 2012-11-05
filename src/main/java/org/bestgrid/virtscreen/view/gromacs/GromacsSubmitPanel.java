package org.bestgrid.virtscreen.view.gromacs;

import javax.swing.JPanel;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import grisu.control.ServiceInterface;
import grisu.control.exceptions.JobPropertiesException;
import grisu.control.exceptions.JobSubmissionException;
import grisu.frontend.view.swing.jobcreation.JobCreationPanel;
import grisu.frontend.view.swing.jobcreation.widgets.SingleInputGridFile;
import javax.swing.JButton;

import org.bestgrid.virtscreen.model.gromacs.GromacsJob;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import grisu.frontend.view.swing.jobcreation.widgets.SubmissionLogPanel;
import grisu.frontend.view.swing.jobcreation.widgets.MultiInputGridFile;
import grisu.frontend.view.swing.jobcreation.widgets.Email;
import grisu.frontend.view.swing.jobcreation.widgets.Cpus;
import grisu.frontend.view.swing.jobcreation.widgets.Walltime;
import grisu.frontend.view.swing.jobcreation.widgets.WalltimeVertical;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

public class GromacsSubmitPanel extends JPanel implements JobCreationPanel {
	
	private final String HISTORY_KEY = "virtScreenGromacsJob";

	
	
	private SingleInputGridFile groInputFile;
	private JButton btnNewButton;
	
	private ServiceInterface si;
	private SubmissionLogPanel submissionLogPanel;
	private MultiInputGridFile multiInputGridFile;
	private Email email;
	private Cpus cpus;
	private WalltimeVertical walltimeVertical;
	private SingleInputGridFile topInputFile;
	private JSeparator separator;
	private JSeparator separator_1;

	/**
	 * Create the panel.
	 */
	public GromacsSubmitPanel() {
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(94dlu;default):grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(65dlu;default):grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(31dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,}));
		add(getGroInputFile(), "2, 2, 7, 1, fill, fill");
		add(getTopInputFile(), "2, 4, 7, 1, fill, fill");
		add(getSeparator_1(), "8, 6");
		add(getCpus(), "8, 8, fill, bottom");
		add(getWalltimeVertical(), "8, 10, fill, fill");
		add(getMultiInputGridFile(), "2, 8, 3, 3, fill, fill");
		add(getEmail(), "2, 12, 3, 1, fill, fill");
		add(getSeparator(), "6, 8, 1, 5");
		add(getBtnNewButton(), "8, 12, right, center");
		add(getSubmissionLogPanel(), "2, 14, 7, 1, fill, fill");

	}

	@Override
	public boolean createsBatchJob() {
		return false;
	}

	@Override
	public boolean createsSingleJob() {
		return true;
	}

	@Override
	public JPanel getPanel() {
		return this;
	}

	@Override
	public String getPanelName() {
		return "Gromacs";
	}

	@Override
	public String getSupportedApplication() {
		return "GROMACS";
	}

	@Override
	public void setServiceInterface(ServiceInterface si) {
		
		this.si = si;
		
		getGroInputFile().setServiceInterface(si);
		getTopInputFile().setServiceInterface(si);
		getMultiInputGridFile().setServiceInterface(si);
		
		getTopInputFile().setFileDialog(getGroInputFile().getFileDialog());
		//getMultiInputGridFile().setFileDialog(getGroInputFile().getFileDialog());
		
	}

	private SingleInputGridFile getGroInputFile() {
		if (groInputFile == null) {
			groInputFile = new SingleInputGridFile();
			groInputFile.setHistoryKey(HISTORY_KEY + "_gro_file");
			groInputFile.setTitle(".gro file");
		}
		return groInputFile;
	}
	private JButton getBtnNewButton() {
		if (btnNewButton == null) {
			btnNewButton = new JButton("Submit");
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					 
					new Thread() {
						public void run() {
							submit();
						}
					}.start();
					
				}
			});
		}
		return btnNewButton;
	}
	
	private void submit() {
		
		GromacsJob job = new GromacsJob(si);
		
		getSubmissionLogPanel().setJobObject(job.getJobObject());
		
		job.setGroFile(getGroInputFile().getInputFile());
		job.setTopFile(getTopInputFile().getInputFile());
		job.setMbpFiles(getMultiInputGridFile().getInputFiles());
		
		job.setWalltimeInSeconds(getWalltimeVertical().getWalltimeInSeconds());
		job.setCpus(getCpus().getCpus());
		
		job.setEmail(getEmail().getEmailAddress());
		job.setEmail_on_start(getEmail().sendEmailWhenJobIsStarted());
		job.setEmail_on_finish(getEmail().sendEmailWhenJobFinished());
		
		
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
	private SubmissionLogPanel getSubmissionLogPanel() {
		if (submissionLogPanel == null) {
			submissionLogPanel = new SubmissionLogPanel();
		}
		return submissionLogPanel;
	}
	private MultiInputGridFile getMultiInputGridFile() {
		if (multiInputGridFile == null) {
			multiInputGridFile = new MultiInputGridFile();
			multiInputGridFile.setTitle("mdpFiles");
		}
		return multiInputGridFile;
	}
	private Email getEmail() {
		if (email == null) {
			email = new Email();
		}
		return email;
	}
	private Cpus getCpus() {
		if (cpus == null) {
			cpus = new Cpus();
			cpus.setEditableComboBox(true);
		}
		return cpus;
	}
	private WalltimeVertical getWalltimeVertical() {
		if (walltimeVertical == null) {
			walltimeVertical = new WalltimeVertical();
		}
		return walltimeVertical;
	}
	private SingleInputGridFile getTopInputFile() {
		if (topInputFile == null) {
			topInputFile = new SingleInputGridFile();
			topInputFile.setHistoryKey(HISTORY_KEY + "_top_file");
			topInputFile.setTitle(".top file");
		}
		return topInputFile;
	}
	private JSeparator getSeparator() {
		if (separator == null) {
			separator = new JSeparator();
			separator.setOrientation(SwingConstants.VERTICAL);
		}
		return separator;
	}
	private JSeparator getSeparator_1() {
		if (separator_1 == null) {
			separator_1 = new JSeparator();
		}
		return separator_1;
	}
}
