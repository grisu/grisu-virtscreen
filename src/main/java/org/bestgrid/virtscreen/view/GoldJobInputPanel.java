package org.bestgrid.virtscreen.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.apache.commons.lang.StringUtils;
import org.bestgrid.virtscreen.model.GoldConfFile;
import org.bestgrid.virtscreen.model.GoldJob;
import org.netbeans.validation.api.ui.ValidationGroup;
import org.netbeans.validation.api.ui.ValidationPanel;
import org.vpac.grisu.control.ServiceInterface;
import org.vpac.grisu.control.exceptions.JobPropertiesException;
import org.vpac.grisu.control.exceptions.JobSubmissionException;
import org.vpac.grisu.frontend.control.clientexceptions.FileTransactionException;
import org.vpac.grisu.frontend.view.swing.jobcreation.JobCreationPanel;
import org.vpac.grisu.frontend.view.swing.jobcreation.widgets.AbstractWidget;
import org.vpac.grisu.frontend.view.swing.jobcreation.widgets.Cpus;
import org.vpac.grisu.frontend.view.swing.jobcreation.widgets.SingleInputFile;
import org.vpac.grisu.frontend.view.swing.jobcreation.widgets.SubmissionLogPanel;
import org.vpac.grisu.frontend.view.swing.jobcreation.widgets.Walltime;

import au.org.arcs.jcommons.constants.Constants;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class GoldJobInputPanel extends JPanel implements JobCreationPanel {

	private ServiceInterface si;
	private ConfFileInputFile confFileInput;
	private Cpus cpus;
	private Walltime walltime;
	private SubmissionLogPanel submissionLogPanel;
	private JButton btnSubmit;

	private final String HISTORY_KEY = "virtScreenGoldJob";

	private final List<AbstractWidget> widgets = new LinkedList<AbstractWidget>();
	private SingleInputFile singleInputFile;
	private final ValidationPanel validationPanel = new ValidationPanel();
	private final ValidationGroup validationGroup;

	/**
	 * Create the panel.
	 */
	public GoldJobInputPanel() {
		super();

		validationPanel.setInnerComponent(this);
		validationGroup = validationPanel.getValidationGroup();

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
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC, }));
		add(getConfFileInput(), "2, 2, 3, 1, fill, fill");
		add(getCpus(), "2, 6, fill, fill");
		add(getWalltime(), "4, 6, fill, fill");
		add(getBtnSubmit(), "4, 8, right, default");
		add(getSubmissionLogPanel(), "2, 10, 3, 1, fill, fill");

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

	private ConfFileInputFile getConfFileInput() {
		if (confFileInput == null) {
			confFileInput = new ConfFileInputFile(this);
			confFileInput.setTitle(".conf file");
			confFileInput.setHistoryKey(HISTORY_KEY + "_conf_file");
			addWidget(confFileInput);
		}
		return confFileInput;
	}

	private void addWidget(AbstractWidget widget) {
		widgets.add(widget);
		widget.setValidationGroup(validationGroup);
	}

	private Cpus getCpus() {
		if (cpus == null) {
			cpus = new Cpus();
			cpus.setHistoryKey(HISTORY_KEY + "_cpus");
			addWidget(cpus);
		}
		return cpus;
	}

	private Walltime getWalltime() {
		if (walltime == null) {
			walltime = new Walltime();
			walltime.setHistoryKey(HISTORY_KEY + "_walltime");
			addWidget(walltime);
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

		final GoldJob job;
		;
		try {
			job = new GoldJob(si, getConfFileInput().getInputFileUrl());
		} catch (FileTransactionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

		job.setCpus(getCpus().getCpus());
		job.setWalltime(getWalltime().getWalltimeInSeconds());

		new Thread() {
			@Override
			public void run() {

				try {
					lockUI(true);

					getSubmissionLogPanel().setJobObject(job.getJobObject());
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
					getConfFileInput().setInputFile(null);
				}
			}
		}.start();

	}

	private JButton getBtnSubmit() {
		if (btnSubmit == null) {
			btnSubmit = new JButton("Submit");
			btnSubmit.setEnabled(false);
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

	public void parseConfig() {

		final String confUrl = getConfFileInput().getInputFileUrl();

		if (StringUtils.isBlank(confUrl)) {
			setParseResult(false, new StringBuffer("No .conf file specified."),
					new StringBuffer("Select .conf file."));
		} else {

			final StringBuffer logMessage = new StringBuffer(
					"Parse log:\n\nLoading conf file...\n");
			final StringBuffer fixes = new StringBuffer();

			lockUI(true);

			getSubmissionLogPanel().setText("Parsing file " + confUrl + "...");

			new Thread() {
				@Override
				public void run() {

					GoldConfFile temp = null;
					try {
						temp = new GoldConfFile(si, confUrl);
					} catch (FileTransactionException e) {
						logMessage.append("Can't access .conf file: "
								+ getConfFileInput().getInputFileUrl());
						fixes.append("Select an existing file.");
						setParseResult(false, logMessage, fixes);
						return;
					} finally {
						lockUI(false);
					}

					boolean success = temp.checkForValidity(logMessage, fixes);
					setParseResult(success, logMessage, fixes);
				}
			}.start();
		}

	}

	private void setParseResult(boolean success, StringBuffer logMessage,
			StringBuffer fixes) {

		getSubmissionLogPanel().setText(logMessage.toString() + "\n" + fixes);
		getBtnSubmit().setEnabled(success);

	}
}
