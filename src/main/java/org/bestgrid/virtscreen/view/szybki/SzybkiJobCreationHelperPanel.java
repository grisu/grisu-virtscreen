package org.bestgrid.virtscreen.view.szybki;

import grisu.X;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class SzybkiJobCreationHelperPanel extends JPanel {
	private JCheckBox chckbxDisplayAllParameters;
	private JButton btnSubmit;

	private final SzybkiInputFileGrid table;

	/**
	 * Create the panel.
	 */
	public SzybkiJobCreationHelperPanel(SzybkiInputFileGrid table) {
		this.table = table;
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

					X.p("Submit.");

				}
			});
		}
		return btnSubmit;
	}

}
