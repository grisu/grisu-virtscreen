package org.bestgrid.virtscreen.view.szybki;

import grisu.X;
import grisu.control.ServiceInterface;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

import org.bestgrid.virtscreen.model.szybki.ParameterValue;
import org.bestgrid.virtscreen.model.szybki.SzybkiParameter.PARAM;

public class SzybkiInputFileTableCellEditor extends AbstractCellEditor
		implements TableCellEditor {

	private final JCheckBox checkbox = new JCheckBox();
	private final DefaultCellEditor checkBoxEditor = new DefaultCellEditor(
			checkbox);

	private final JComboBox combobox = new JComboBox();
	private final DefaultCellEditor comboBoxEditor = new DefaultCellEditor(
			combobox);

	private final JTextField textField = new JTextField();
	private final DefaultCellEditor textFieldEditor = new DefaultCellEditor(
			textField);

	private final Map<PARAM, FileCellEditor> fces = new HashMap<PARAM, FileCellEditor>();

	private TableCellEditor currentEditor = null;

	private final ServiceInterface si;

	public SzybkiInputFileTableCellEditor(ServiceInterface si) {
		this.si = si;
	}

	public Object getCellEditorValue() {

		return currentEditor.getCellEditorValue();
	}

	private FileCellEditor getFileCellEditor(PARAM p) {

		if (fces.get(p) == null) {
			final FileCellEditor temp = new FileCellEditor();
			temp.setServiceInterface(si);
			temp.setHistoryKey("szybki_" + p.toString());

			fces.put(p, temp);
		}
		return fces.get(p);
	}

	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {

		X.p("Value " + value.toString() + " row: " + row + " col: " + column);

		final ParameterValue pv = (ParameterValue) value;

		switch (pv.getType()) {
		case BOOLEAN:
			currentEditor = checkBoxEditor;
			return checkBoxEditor.getTableCellEditorComponent(table, value,
					isSelected, row, column);
		case STRING:
			currentEditor = textFieldEditor;
			return textFieldEditor.getTableCellEditorComponent(table, value,
					isSelected, row, column);

		case FILE:
			final FileCellEditor fce = getFileCellEditor(pv.getParameter());
			currentEditor = fce;
			fce.setConfiguration(pv.getParameter().config);
			return fce.getTableCellEditorComponent(table, value, isSelected,
					row, column);
		default:
			currentEditor = textFieldEditor;
			return textFieldEditor.getTableCellEditorComponent(table, value,
					isSelected, row, column);

		}

	}

}
