package org.bestgrid.virtscreen.view.szybki;

import grisu.control.ServiceInterface;
import grisu.model.dto.GridFile;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import org.bestgrid.virtscreen.model.szybki.ParameterValue;

public class FileCellRenderer implements TableCellRenderer {

	private final FileSelectorPanelMini panel = new FileSelectorPanelMini();

	public FileCellRenderer(ServiceInterface si) {
		panel.setServiceInterface(si);
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		panel.setConfiguration(((ParameterValue) value).getParameter().config);
		final GridFile f = (GridFile) (((ParameterValue) value).getValue());
		panel.setInputFile(f);

		panel.setToolTipText(f.getUrl());

		return panel;

	}

}
