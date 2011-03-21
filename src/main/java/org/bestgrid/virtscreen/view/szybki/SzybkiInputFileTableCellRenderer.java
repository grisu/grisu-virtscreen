package org.bestgrid.virtscreen.view.szybki;

import grisu.control.ServiceInterface;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import org.bestgrid.virtscreen.model.szybki.ParameterValue;

public class SzybkiInputFileTableCellRenderer implements TableCellRenderer {

	private final DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
	private JCheckBox checkbox = null;

	private final FileCellRenderer fcr;

	public SzybkiInputFileTableCellRenderer(ServiceInterface si) {
		fcr = new FileCellRenderer(si);
	}

	private JCheckBox getCheckBox() {
		if (checkbox == null) {
			checkbox = new JCheckBox();
		}
		return checkbox;
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		ParameterValue pv = (ParameterValue) value;

		switch (pv.getType()) {
		case BOOLEAN:
			getCheckBox().setSelected((Boolean) pv.getValue());
			return getCheckBox();
		case FILE:
			return fcr.getTableCellRendererComponent(table, value, isSelected,
					hasFocus, row, column);
		default:
			dtcr.setText(pv.getStringValue());
			return dtcr;
		}


	}

}
