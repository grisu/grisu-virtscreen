package org.bestgrid.virtscreen.view.szybki;

import grisu.control.ServiceInterface;
import grisu.frontend.view.swing.jobcreation.JobCreationPanel;
import grisu.frontend.view.swing.jobcreation.widgets.SubmissionLogPanel;

import javax.swing.JPanel;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class SzybkiJobCreationPanel extends JPanel implements JobCreationPanel {

	private ServiceInterface si;
	private SzybkiInputFileGrid szybkiInputFileGrid;
	private SzybkiJobCreationHelperPanel szybkiJobCreationHelperPanel;
	private SubmissionLogPanel submissionLogPanel;
	private TableFilterControlPanel tableFilterControlPanel;

	/**
	 * Create the panel.
	 */
	public SzybkiJobCreationPanel() {
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC, }, new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(95dlu;default)"),
				FormFactory.RELATED_GAP_ROWSPEC, }));
		add(getSzybkiInputFileGrid(), "2, 2, fill, fill");
		add(getTableFilterControlPanel(), "2, 4, fill, fill");
		add(getSzybkiJobCreationHelperPanel(), "2, 6, fill, fill");
		add(getSubmissionLogPanel(), "2, 8, fill, fill");
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
		return "Szybki";
	}

	private SubmissionLogPanel getSubmissionLogPanel() {
		if (submissionLogPanel == null) {
			submissionLogPanel = new SubmissionLogPanel();
		}
		return submissionLogPanel;
	}

	public String getSupportedApplication() {
		return "Szybki";
	}

	private SzybkiInputFileGrid getSzybkiInputFileGrid() {
		if (szybkiInputFileGrid == null) {
			szybkiInputFileGrid = new SzybkiInputFileGrid();
		}
		return szybkiInputFileGrid;
	}

	private SzybkiJobCreationHelperPanel getSzybkiJobCreationHelperPanel() {
		if (szybkiJobCreationHelperPanel == null) {
			szybkiJobCreationHelperPanel = new SzybkiJobCreationHelperPanel(
					getSzybkiInputFileGrid());
		}
		return szybkiJobCreationHelperPanel;
	}

	private TableFilterControlPanel getTableFilterControlPanel() {
		if (tableFilterControlPanel == null) {
			tableFilterControlPanel = new TableFilterControlPanel(
					getSzybkiInputFileGrid());
		}
		return tableFilterControlPanel;
	}

	public void setServiceInterface(ServiceInterface si) {
		this.si = si;
		getSzybkiInputFileGrid().setServiceInterface(si);
		getTableFilterControlPanel().setServiceInterface(si);


	}
}
