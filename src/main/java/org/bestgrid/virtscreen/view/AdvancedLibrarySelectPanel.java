package org.bestgrid.virtscreen.view;

import java.awt.CardLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.bestgrid.virtscreen.model.GoldConfFile;
import org.vpac.grisu.control.ServiceInterface;
import org.vpac.grisu.control.exceptions.RemoteFileSystemException;
import org.vpac.grisu.frontend.view.swing.jobcreation.widgets.AbstractWidget;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class AdvancedLibrarySelectPanel extends AbstractWidget {

	public static final String STANDARD_LIGAND_DATA_FILE = "default";
	public static final String NON_STANDARD_LIGAND_DATA_FILE = "non-standard";

	private JPanel panel;
	private JCheckBox chckbxAdvancedSelection;
	private GoldLibrarySelectPanel goldLibrarySelectPanel;
	private LigandFileInputFile ligandFileInputFile;

	private GoldConfFile confFile = null;

	/**
	 * Create the panel.
	 */
	public AdvancedLibrarySelectPanel() {
		setBorder(new TitledBorder(null, "Ligand data file",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC, }, new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, }));
		add(getPanel(), "2, 2, fill, fill");
		add(getChckbxAdvancedSelection(), "2, 4");
	}

	private JCheckBox getChckbxAdvancedSelection() {
		if (chckbxAdvancedSelection == null) {
			chckbxAdvancedSelection = new JCheckBox("Advanced selection");
			chckbxAdvancedSelection.addItemListener(new ItemListener() {

				public void itemStateChanged(ItemEvent e) {

					if (chckbxAdvancedSelection.isSelected()) {
						switchToPanel(NON_STANDARD_LIGAND_DATA_FILE);
					} else {
						switchToPanel(STANDARD_LIGAND_DATA_FILE);
					}
				}
			});
		}
		return chckbxAdvancedSelection;
	}

	private GoldLibrarySelectPanel getGoldLibrarySelectPanel() {
		if (goldLibrarySelectPanel == null) {
			goldLibrarySelectPanel = new GoldLibrarySelectPanel();
			// goldLibrarySelectPanel.setLayout(new FormLayout(
			// new ColumnSpec[] {}, new RowSpec[] {}));
		}
		return goldLibrarySelectPanel;
	}

	private LigandFileInputFile getLigandFileInputFile() {
		if (ligandFileInputFile == null) {
			ligandFileInputFile = new LigandFileInputFile();
			// ligandFileInputFile.setLayout(new FormLayout(new ColumnSpec[] {
			// ColumnSpec.decode("429px"), ColumnSpec.decode("87px"), },
			// new RowSpec[] { RowSpec.decode("27px"),
			// RowSpec.decode("25px"), }));
		}
		return ligandFileInputFile;
	}

	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			panel.setLayout(new CardLayout(0, 0));
			panel.add(getGoldLibrarySelectPanel(), STANDARD_LIGAND_DATA_FILE);
			panel.add(getLigandFileInputFile(), NON_STANDARD_LIGAND_DATA_FILE);
		}
		return panel;
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void lockUI(final boolean lock) {

		getGoldLibrarySelectPanel().lockUI(lock);
		getLigandFileInputFile().lockUI(lock);
		getChckbxAdvancedSelection().setEnabled(!lock);
	}

	public void reset() {

		boolean found = true;
		for (String s : getGoldLibrarySelectPanel().getSelectedLibraryFiles()) {
			if (GoldLibrarySelectPanel.N_A_MESSAGE.equals(s)) {
				found = false;
				break;
			}
		}

		if (confFile.getLigandDataFiles().length != 1) {
			found = true;
		}
		if (!found) {
			String s = confFile.getLigandDataFiles()[0];
			if (StringUtils.isBlank(s) || !s.equals(FilenameUtils.getName(s))) {
				getLigandFileInputFile().setValue(null);
			} else {
				s = confFile.getConfFileDir() + "/" + s;
				try {
					if (getFileManager().fileExists(s)) {
						getLigandFileInputFile().setValue(s);
						getChckbxAdvancedSelection().setSelected(true);
						return;
					}
				} catch (RemoteFileSystemException e) {
					getLigandFileInputFile().setValue(null);
				}
				getChckbxAdvancedSelection().setSelected(false);
			}
		} else {
			getChckbxAdvancedSelection().setSelected(false);
		}
	}

	public void setGoldConfFile(GoldConfFile confFile) {
		this.confFile = confFile;
		getGoldLibrarySelectPanel().setGoldConfFile(confFile);
		getLigandFileInputFile().setGoldConfFile(confFile);
	}

	@Override
	public void setServiceInterface(ServiceInterface si) {
		super.setServiceInterface(si);
		getGoldLibrarySelectPanel().setServiceInterface(si);
		getLigandFileInputFile().setServiceInterface(si);
	}

	@Override
	public void setValue(String value) {
		// TODO Auto-generated method stub

	}

	private void switchToPanel(String panelname) {

		final CardLayout cl = (CardLayout) getPanel().getLayout();
		cl.show(getPanel(), panelname);

		if (STANDARD_LIGAND_DATA_FILE.equals(panelname)) {
			confFile.setLigandDataFiles(getGoldLibrarySelectPanel()
					.getSelectedLibraryFiles());
		} else if (NON_STANDARD_LIGAND_DATA_FILE.equals(panelname)) {
			confFile.setLigandDataFiles(new String[] { getLigandFileInputFile()
					.getSelectedLigandFile() });
		}

	}
}
