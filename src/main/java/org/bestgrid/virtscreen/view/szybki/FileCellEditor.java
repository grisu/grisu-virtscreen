package org.bestgrid.virtscreen.view.szybki;

import grisu.control.ServiceInterface;
import grisu.model.dto.GridFile;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import org.bestgrid.virtscreen.model.szybki.ParameterValue;

public class FileCellEditor extends AbstractCellEditor implements
TableCellEditor {

	private ServiceInterface si;
	private final FileSelectorPanelMini panel = new FileSelectorPanelMini();

	public Object getCellEditorValue() {

		return panel.getInputFile();
	}

	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean selected, int row, int column) {

		panel.setInputFile((GridFile) (((ParameterValue) value).getValue()));
		return this.panel;
	}

	public void setHistoryKey(String key) {
		panel.setHistoryKey(key);
	}

	public void setServiceInterface(ServiceInterface si) {
		this.si = si;
		this.panel.setServiceInterface(si);
	}


}
