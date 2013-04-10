package org.bestgrid.virtscreen.view.szybki;

import grisu.frontend.view.swing.jobcreation.widgets.AbstractInputGridFile;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

public class FileSelectorPanelMini extends AbstractInputGridFile {
	private JComboBox comboBox;
	private JButton button;

	public FileSelectorPanelMini() {
		super();
		setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("212px:grow"),
				FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC, }, new RowSpec[] {
				FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("24px"),
				FormSpecs.RELATED_GAP_ROWSPEC, }));
		add(getInputFileComboBox(), "2, 2, fill, default");
		add(getButton(), "4, 2, center, default");
	}

	private JButton getButton() {
		if (button == null) {
			button = new JButton("...");
			button.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {

					askForFile();

				}
			});
		}
		return button;
	}

}
