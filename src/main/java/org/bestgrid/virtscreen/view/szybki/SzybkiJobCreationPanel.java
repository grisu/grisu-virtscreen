package org.bestgrid.virtscreen.view.szybki;

import grisu.control.ServiceInterface;
import grisu.frontend.view.swing.jobcreation.JobCreationPanel;
import grisu.frontend.view.swing.jobcreation.widgets.SubmissionLogPanel;
import grisu.model.dto.GridFile;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import org.apache.log4j.Logger;
import org.bestgrid.virtscreen.model.szybki.SzybkiException;
import org.bestgrid.virtscreen.model.szybki.SzybkiInputFile;
import org.bestgrid.virtscreen.model.szybki.SzybkiJob;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class SzybkiJobCreationPanel extends JPanel implements JobCreationPanel,
PropertyChangeListener {

	static final Logger myLogger = Logger
			.getLogger(SzybkiJobCreationPanel.class.getName());

	private ServiceInterface si;
	private SzybkiInputFileGrid szybkiInputFileGrid;
	private SzybkiJobCreationHelperPanel szybkiJobCreationHelperPanel;
	private SubmissionLogPanel submissionLogPanel;
	private TableFilterControlPanel tableFilterControlPanel;
	private JTabbedPane tabbedPane;
	private SzybkiConfigViewerPanel szybkiConfigViewerPanel;

	private SzybkiInputFile inputFile;

	/**
	 * Create the panel.
	 */
	public SzybkiJobCreationPanel() {
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC, }, new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(27dlu;default):grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(50dlu;default)"),
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(95dlu;default)"),
				FormFactory.RELATED_GAP_ROWSPEC, }));
		add(getTabbedPane(), "2, 2, fill, fill");
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

	public SzybkiInputFile getInputFile() {
		return this.inputFile;
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

	private SzybkiConfigViewerPanel getSzybkiConfigViewerPanel() {
		if (szybkiConfigViewerPanel == null) {
			szybkiConfigViewerPanel = new SzybkiConfigViewerPanel();

		}
		return szybkiConfigViewerPanel;
	}

	public SzybkiInputFile getSzybkiInputFile() {
		return this.inputFile;
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
					this);
		}
		return szybkiJobCreationHelperPanel;
	}

	private JTabbedPane getTabbedPane() {
		if (tabbedPane == null) {
			tabbedPane = new JTabbedPane(SwingConstants.BOTTOM);
			tabbedPane.addTab("Parameters", null, getSzybkiInputFileGrid(),
					null);
			tabbedPane.addTab("params file", null,
					getSzybkiConfigViewerPanel(), null);
		}
		return tabbedPane;
	}

	private TableFilterControlPanel getTableFilterControlPanel() {
		if (tableFilterControlPanel == null) {
			tableFilterControlPanel = new TableFilterControlPanel(this);
		}
		return tableFilterControlPanel;
	}

	public void propertyChange(PropertyChangeEvent evt) {

		if ("inputFile".equals(evt.getPropertyName())) {

			if (evt.getNewValue() == null) {
				return;
			}

			try {
				this.inputFile.setInputFile((GridFile) evt.getNewValue());
			} catch (final SzybkiException e) {
				myLogger.error(e.getLocalizedMessage(), e);
			}

		}
	}

	public void setCurrentJob(SzybkiJob job) {

		getSubmissionLogPanel().setJobObject(job.getJob());

	}

	public void setServiceInterface(ServiceInterface si) {
		this.si = si;

		this.inputFile = new SzybkiInputFile(this.si);

		getSzybkiInputFileGrid().setSzybkiInputFile(this.inputFile);
		getSzybkiConfigViewerPanel().setSzybkiInputFile(this.inputFile);
		getSzybkiJobCreationHelperPanel().setServiceInterface(si);

		getTableFilterControlPanel().setServiceInterface(si);

	}

	public void showDisabledParameters(boolean selected) {
		getSzybkiInputFileGrid().showDisabledParameters(selected);
		getSzybkiConfigViewerPanel().showDisabledParameters(selected);
	}
}
