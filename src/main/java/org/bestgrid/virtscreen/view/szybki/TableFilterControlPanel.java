package org.bestgrid.virtscreen.view.szybki;

import grisu.control.ServiceInterface;
import grisu.frontend.view.swing.jobcreation.widgets.SingleInputGridFile;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

public class TableFilterControlPanel extends JPanel {
	private JCheckBox chckbxDisplayAllParameters;

	private SingleInputGridFile singleInputGridFile;
	private JButton button;
	private SzybkiJobCreationPanel szybkiJobCreationPanel = null;

	/**
	 * Create the panel.
	 */
	public TableFilterControlPanel(SzybkiJobCreationPanel szybkiJobCreationPanel) {
		this.szybkiJobCreationPanel = szybkiJobCreationPanel;
		setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("185px:grow"),
				FormSpecs.RELATED_GAP_COLSPEC, }, new RowSpec[] {
				FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC, }));
		// add(getChckbxDisplayAllParameters(), "2, 2, left, top");
		add(getSingleInputGridFile(), "2, 2, fill, fill");
	}

	private JCheckBox getChckbxDisplayAllParameters() {
		if (chckbxDisplayAllParameters == null) {
			chckbxDisplayAllParameters = new JCheckBox("Display all params");
			chckbxDisplayAllParameters.setSelected(true);
			chckbxDisplayAllParameters.addItemListener(new ItemListener() {

				public void itemStateChanged(ItemEvent e) {

					szybkiJobCreationPanel
							.showDisabledParameters(chckbxDisplayAllParameters
									.isSelected());

				}
			});
		}
		return chckbxDisplayAllParameters;
	}

	private SingleInputGridFile getSingleInputGridFile() {
		if (singleInputGridFile == null) {
			singleInputGridFile = new SingleInputGridFile();
			singleInputGridFile.setHistoryKey("szybki_conf_file");
			singleInputGridFile
					.setExtensionsToDisplay(new String[] { "param" });

			singleInputGridFile.setFoldersSelectable(false);
			singleInputGridFile.setDisplayHiddenFiles(false);
			singleInputGridFile.addWidgetListener(szybkiJobCreationPanel);
		}
		return singleInputGridFile;
	}

	public void setServiceInterface(ServiceInterface si) {
		getSingleInputGridFile().setServiceInterface(si);
	}
}
