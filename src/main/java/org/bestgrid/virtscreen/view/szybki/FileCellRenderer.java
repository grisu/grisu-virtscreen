package org.bestgrid.virtscreen.view.szybki;

import grisu.model.dto.GridFile;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import org.bestgrid.virtscreen.model.szybki.ParameterValue;

public class FileCellRenderer implements TableCellRenderer {

	private final FileSelectorPanelMini panel = new FileSelectorPanelMini();

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		panel.setInputFile((GridFile) (((ParameterValue) value).getValue()));
		return panel;

	}



}
