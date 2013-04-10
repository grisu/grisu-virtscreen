package org.bestgrid.virtscreen.view.gromacs;

import grisu.control.ServiceInterface;
import grisu.control.exceptions.JobPropertiesException;
import grisu.control.exceptions.JobSubmissionException;
import grisu.frontend.view.swing.jobcreation.JobCreationPanel;
import grisu.frontend.view.swing.jobcreation.widgets.Cpus;
import grisu.frontend.view.swing.jobcreation.widgets.Email;
import grisu.frontend.view.swing.jobcreation.widgets.OrderableMultiInputGridFile;
import grisu.frontend.view.swing.jobcreation.widgets.SingleInputGridFile;
import grisu.frontend.view.swing.jobcreation.widgets.SubmissionLogPanel;
import grisu.frontend.view.swing.jobcreation.widgets.Walltime;
import grisu.model.dto.GridFile;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;

import org.bestgrid.virtscreen.model.gromacs.GromacsJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

public class GromacsSubmitPanel extends JPanel implements JobCreationPanel {
	
	public static final Logger myLogger = LoggerFactory.getLogger(GromacsSubmitPanel.class);
	
	private final String HISTORY_KEY = "virtScreenGromacsJob";

	
	
	private SingleInputGridFile groInputFile;
	private JButton btnNewButton;
	
	private ServiceInterface si;
	private SubmissionLogPanel submissionLogPanel;
	private OrderableMultiInputGridFile multiInputGridFile;
	private Email email;
	private Cpus cpus;
	private Walltime walltimeVertical;
	private SingleInputGridFile topInputFile;
	private JSeparator separator;

	/**
	 * Create the panel.
	 */
	public GromacsSubmitPanel() {
		setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(44dlu;default)"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(94dlu;default)"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(65dlu;default):grow"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(81dlu;default):grow"),
				FormSpecs.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_ROWSPEC,}));
		add(getGroInputFile(), "2, 2, 5, 1, fill, fill");
		add(getMultiInputGridFile(), "8, 2, 1, 7, fill, fill");
		add(getTopInputFile(), "2, 4, 5, 1, fill, fill");
		add(getCpus(), "2, 6, fill, fill");
		add(getWalltimeVertical(), "4, 6, 3, 1, fill, fill");
		add(getEmail(), "2, 8, 5, 1, fill, fill");
		add(getSeparator(), "2, 10, 7, 1");
		add(getBtnNewButton(), "8, 12, right, bottom");
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
	
	private void lockUI(final boolean lock) {
		
		SwingUtilities.invokeLater(new Thread() {
			public void run() {
				getBtnNewButton().setEnabled(!lock);
				getMultiInputGridFile().lockUI(lock);
				getCpus().lockUI(lock);
				getWalltimeVertical().lockUI(lock);
				getTopInputFile().lockUI(lock);
				getEmail().lockUI(lock);
				getGroInputFile().lockUI(lock);
			}
		});
		
		
	}
	
	private JButton getBtnNewButton() {
		if (btnNewButton == null) {
			btnNewButton = new JButton("Submit");
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					 
					new Thread() {
						public void run() {
							
							lockUI(true);
							
							try {
								submit();
							} finally {
								lockUI(false);
							}
						}
					}.start();
					
				}
			});
		}
		return btnNewButton;
	}
	
	private void clearPanel() {
		getMultiInputGridFile().clearFiles();
		getTopInputFile().setValue(null);
		getGroInputFile().setValue(null);
	}
	
	private void submit() {
		
		GromacsJob job = new GromacsJob(si);
		
		getSubmissionLogPanel().setJobObject(job.getJobObject());
		
		job.setGroFile(getGroInputFile().getInputFile());
		job.setTopFile(getTopInputFile().getInputFile());
		
		List<GridFile> mdpFiles = getMultiInputGridFile().getInputFiles();
		
		job.setMbpFiles(mdpFiles);
		
		job.setWalltimeInSeconds(getWalltimeVertical().getWalltimeInSeconds());
		job.setCpus(getCpus().getCpus());
		
		job.setEmail(getEmail().getEmailAddress());
		job.setEmail_on_start(getEmail().sendEmailWhenJobIsStarted());
		job.setEmail_on_finish(getEmail().sendEmailWhenJobFinished());
		
		
		try {
			job.createAndSubmitJob();
			clearPanel();
		} catch (JobSubmissionException e) {
			myLogger.debug("Job creation failed", e);
		} catch (JobPropertiesException e) {
			myLogger.debug("Job creation failed", e);
		}
		
	}
	private SubmissionLogPanel getSubmissionLogPanel() {
		if (submissionLogPanel == null) {
			submissionLogPanel = new SubmissionLogPanel();
		}
		return submissionLogPanel;
	}
	private OrderableMultiInputGridFile getMultiInputGridFile() {
		if (multiInputGridFile == null) {
			multiInputGridFile = new OrderableMultiInputGridFile();
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
			cpus.setEditableComboBox(false);
		}
		return cpus;
	}
	private Walltime getWalltimeVertical() {
		if (walltimeVertical == null) {
			walltimeVertical = new Walltime();
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
		}
		return separator;
	}
}
