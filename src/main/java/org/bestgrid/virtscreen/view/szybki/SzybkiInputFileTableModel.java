package org.bestgrid.virtscreen.view.szybki;

import javax.swing.table.AbstractTableModel;

import org.bestgrid.virtscreen.model.szybki.ParameterValue;
import org.bestgrid.virtscreen.model.szybki.SzybkiInputFile;
import org.bestgrid.virtscreen.model.szybki.SzybkiParameter;
import org.bestgrid.virtscreen.model.szybki.SzybkiParameter.TYPE;

public class SzybkiInputFileTableModel extends AbstractTableModel {

	private SzybkiInputFile inputFile;

	public SzybkiInputFileTableModel() {
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return Boolean.class;
		case 1:
			return String.class;
		case 2:
			return Object.class;
		case 3:
			return String.class;
		default:
			return Object.class;
		}
	}


	public int getColumnCount() {
		return 4;
	}

	@Override
	public String getColumnName(int col) {
		switch (col) {
		case 0:
			return "Set";
		case 1:
			return "Parameter";
		case 2:
			return "Value";
		case 3:
			return "Comment";
		default:
			return null;
		}
	}

	private boolean getParameterEnabled(int rowIndex) {

		if (inputFile == null) {
			return false;
		}

		return inputFile.getParameter(rowIndex).isEnabled();
	}

	private String getParameterName(int rowIndex) {
		if (inputFile == null) {
			return "";
		}

		return inputFile.getParameter(rowIndex).getParameterName().toString();
	}

	private TYPE getParameterType(int rowIndex) {

		if (inputFile == null) {
			return TYPE.UNDEF;
		}

		return inputFile.getParameter(rowIndex).getType();
	}

	private ParameterValue getParameterValue(int rowIndex) {

		if (inputFile == null) {
			return null;
		}

		SzybkiParameter p = inputFile.getParameter(rowIndex);
		return p.getParameterValue();
	}

	public int getRowCount() {

		if (inputFile == null) {
			return 0;
		}

		return inputFile.getParameters().size();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {

		if (inputFile == null) {
			return null;
		}

		switch (columnIndex) {
		case 0:
			return getParameterEnabled(rowIndex);
		case 1:
			return getParameterName(rowIndex);
		case 2:
			return getParameterValue(rowIndex);
		case 3:
			return getParameterType(rowIndex).toString();
		default:
			return null;
		}
	}

	@Override
	public boolean isCellEditable(int row, int col) {


		if (col == 0) {
			return true;
		}

		SzybkiParameter p = inputFile.getParameter(row);
		if (!p.isEnabled()) {
			return false;
		} else {
			if (col == 2) {
				return true;
			} else {
				return false;
			}
		}
	}

	public void setInputFile(SzybkiInputFile inputFile) {
		this.inputFile = inputFile;

	}

	private void setParameterValue(SzybkiParameter param, Object value) {

		try {
			param.getParameterValue().setValue(value);
		} catch (Exception e) {
			System.err.println(e.getLocalizedMessage());
		}

	}

	@Override
	public void setValueAt(Object value, int row, int col) {

		SzybkiParameter p = inputFile.getParameter(row);

		switch (col) {
		case 0:
			p.setEnabled((Boolean) value);
			return;
		case 2:
			setParameterValue(p, value);
			return;
		default:
			return;

		}

	}

}
