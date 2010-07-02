package org.bestgrid.virtscreen;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventTopicSubscriber;
import org.vpac.grisu.control.JobConstants;
import org.vpac.grisu.control.ServiceInterface;
import org.vpac.grisu.frontend.model.events.JobStatusEvent;
import org.vpac.grisu.frontend.model.job.JobObject;
import org.vpac.grisu.model.GrisuRegistryManager;
import org.vpac.grisu.model.UserEnvironmentManager;
import org.vpac.grisu.model.status.ActionStatusEvent;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import org.vpac.grisu.frontend.view.swing.jobcreation.JobCreationPanel;

public class ExampleJobCreationPanel extends JPanel implements JobCreationPanel, EventTopicSubscriber {

	private JLabel lblDummyJobSubmission;
	private JButton btnSubmit;
	private JScrollPane scrollPane;
	private JTextArea textArea;

	private String currentJobname = null;

	private ServiceInterface si;
	private UserEnvironmentManager em;

	/**
	 * Create the panel.
	 */
	public ExampleJobCreationPanel() {
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,},
				new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,}));
		add(getLblDummyJobSubmission(), "2, 2");
		add(getBtnSubmit(), "2, 5, right, default");
		add(getScrollPane(), "2, 7, fill, fill");

	}

	public void addMessage(final String message) {
		SwingUtilities.invokeLater(new Thread() {

			@Override
			public void run() {
				getStatusTextArea().append(message+"\n");
				getStatusTextArea().setCaretPosition(
						getStatusTextArea().getText().length());
			}

		});
	}

	public boolean createsBatchJob() {
		return false;
	}

	public boolean createsSingleJob() {
		return true;
	}

	private JButton getBtnSubmit() {
		if (btnSubmit == null) {
			btnSubmit = new JButton("Submit");
			btnSubmit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					submitJob();
				}
			});
		}
		return btnSubmit;
	}

	private JLabel getLblDummyJobSubmission() {
		if (lblDummyJobSubmission == null) {
			lblDummyJobSubmission = new JLabel("Dummy job submission");
		}
		return lblDummyJobSubmission;
	}
	public JPanel getPanel() {
		return this;
	}
	public String getPanelName() {
		return "Example";
	}

	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setViewportView(getStatusTextArea());
		}
		return scrollPane;
	}

	private JTextArea getStatusTextArea() {
		if (textArea == null) {
			textArea = new JTextArea();
		}
		return textArea;
	}

	public String getSupportedApplication() {
		return "UnixCommands";
	}
	private void lockUI(final boolean lock) {

		SwingUtilities.invokeLater(new Thread() {

			@Override
			public void run() {
				getBtnSubmit().setEnabled(!lock);
			}

		});

	}

	public void onEvent(String topic, Object data) {

		if (data instanceof JobStatusEvent) {

			String message = "New status: "+JobConstants.translateStatus( ((JobStatusEvent)data).getNewStatus() );

			addMessage(message);
		} else if (data instanceof ActionStatusEvent) {
			ActionStatusEvent d = ((ActionStatusEvent) data);
			addMessage(d.getPrefix() + d.getPercentFinished() + "% finished.\n");
		}

	}

	public void setServiceInterface(ServiceInterface si) {
		System.out.println("Serviceinterface set. DN: "+si.getDN());
		this.si = si;
		this.em = GrisuRegistryManager.getDefault(si).getUserEnvironmentManager();
	}

	private void submitJob() {

		new Thread() {
			@Override
			public void run() {
				try {

					lockUI(true);

					if ( StringUtils.isNotBlank(currentJobname) ) {
						EventBus.unsubscribe(currentJobname, this);
					}

					JobObject job = new JobObject(si);
					job.setTimestampJobname("helloworldJob");

					currentJobname = job.getJobname();
					EventBus.subscribe(currentJobname, ExampleJobCreationPanel.this);

					job.setApplication("UnixCommands");
					job.setCommandline("echo hello gridworld!");

					job.setWalltimeInSeconds(60);

					// this will only work if you are in the StartUp VO.
					job.createJob("/ARCS/StartUp");
					//					job.createJob(em.getCurrentFqan());

					job.submitJob();


				} catch (Exception e) {
					e.printStackTrace();
					addMessage(e.getLocalizedMessage());
				} finally {
					lockUI(false);
				}

			}
		}.start();
	}
}
