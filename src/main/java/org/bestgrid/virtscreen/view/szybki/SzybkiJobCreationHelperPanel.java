package org.bestgrid.virtscreen.view.szybki;

import grisu.X;
import grisu.control.ServiceInterface;
import grisu.control.exceptions.JobPropertiesException;
import grisu.control.exceptions.JobSubmissionException;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.bestgrid.virtscreen.model.szybki.SzybkiJob;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class SzybkiJobCreationHelperPanel extends JPanel {

	static final Logger myLogger = Logger
			.getLogger(SzybkiJobCreationHelperPanel.class.getName());

	private JCheckBox chckbxDisplayAllParameters;
	private JButton btnSubmit;

	private final SzybkiJobCreationPanel parent;

	private ServiceInterface si;

	/**
	 * Create the panel.
	 */
	public SzybkiJobCreationHelperPanel(SzybkiJobCreationPanel parent) {
		this.parent = parent;
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC, }, new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, }));
		add(getBtnSubmit(), "2, 2, right, default");
	}

	private JButton getBtnSubmit() {
		if (btnSubmit == null) {
			btnSubmit = new JButton("Submit");
			btnSubmit.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {

					if ( si == null ) {
						X.p("Serviceinterface not set.");
						return;
					}

					X.p("Submitting...");

					final SzybkiJob job = new SzybkiJob(si, parent
							.getSzybkiInputFile());

					parent.setCurrentJob(job);

					new Thread(){
						@Override
						public void run() {
							try {
								job.createAndSubmitJob();
							} catch (JobSubmissionException e) {
								myLogger.error(e);
							} catch (JobPropertiesException e) {
								myLogger.error(e);
							}
						}
					}.start();

				}
			});
		}
		return btnSubmit;
	}

	public void setServiceInterface(ServiceInterface si) {

		this.si = si;

	}

}
