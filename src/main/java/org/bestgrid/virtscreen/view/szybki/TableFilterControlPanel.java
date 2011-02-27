package org.bestgrid.virtscreen.view.szybki;

import grisu.control.ServiceInterface;
import grisu.frontend.view.swing.jobcreation.widgets.SingleInputGridFile;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class TableFilterControlPanel extends JPanel implements
PropertyChangeListener {
	private JCheckBox chckbxDisplayAllParameters;

	private SzybkiInputFileGrid table = null;
	private SingleInputGridFile singleInputGridFile;
	private JButton button;

	/**
	 * Create the panel.
	 */
	public TableFilterControlPanel(SzybkiInputFileGrid table) {
		this.table = table;
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("185px:grow"),
				FormFactory.RELATED_GAP_COLSPEC, }, new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(23dlu;default)"),
				FormFactory.RELATED_GAP_ROWSPEC, }));
		add(getChckbxDisplayAllParameters(), "2, 2, left, top");
		add(getSingleInputGridFile(), "4, 2, 1, 3, fill, fill");
		add(getButton(), "2, 4, left, default");
	}

	private JButton getButton() {
		if (button == null) {
			button = new JButton("?");
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					SzybkiConfigViewerDialog dialog = new SzybkiConfigViewerDialog(
							table.getSzybkiInputFile());

				}
			});
		}
		return button;
	}

	private JCheckBox getChckbxDisplayAllParameters() {
		if (chckbxDisplayAllParameters == null) {
			chckbxDisplayAllParameters = new JCheckBox("Display all params");
			chckbxDisplayAllParameters.addItemListener(new ItemListener() {

				public void itemStateChanged(ItemEvent e) {

					table.showDisabledParameters(chckbxDisplayAllParameters.isSelected());

				}
			});
		}
		return chckbxDisplayAllParameters;
	}

	private SingleInputGridFile getSingleInputGridFile() {
		if (singleInputGridFile == null) {
			singleInputGridFile = new SingleInputGridFile();
			singleInputGridFile.addPropertyChangeListener(this);
		}
		return singleInputGridFile;
	}

	public void propertyChange(PropertyChangeEvent evt) {

		if ("inputFile".equals(evt.getPropertyName())) {
			table.loadSzybkiInputFile(getSingleInputGridFile()
					.getInputFileUrl());
		}

	}

	public void setServiceInterface(ServiceInterface si) {
		getSingleInputGridFile().setServiceInterface(si);
	}
}
