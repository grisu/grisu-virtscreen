package org.bestgrid.virtscreen.view.szybki;

import grisu.X;
import grisu.control.ServiceInterface;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import org.bestgrid.virtscreen.model.szybki.SzybkiInputFile;

public class SzybkiInputFileGrid extends JPanel implements
		PropertyChangeListener {

	public static final RowFilter<SzybkiInputFileTableModel, Integer> ENABLEDFILTER = new RowFilter<SzybkiInputFileTableModel, Integer>() {
		@Override
		public boolean include(
				Entry<? extends SzybkiInputFileTableModel, ? extends Integer> entry) {

			// X.p("Entry: " + entry.getStringValue(0));

			return (Boolean) entry.getValue(0);

		}
	};

	private JScrollPane scrollPane;

	private JTable table;

	private SzybkiInputFile inputFile;

	private ServiceInterface si;

	private SzybkiInputFileTableModel tm;

	private TableRowSorter<SzybkiInputFileTableModel> sorter = null;

	/**
	 * Create the panel.
	 */
	public SzybkiInputFileGrid() {
		setLayout(new BorderLayout(0, 0));
		add(getScrollPane(), BorderLayout.CENTER);
	}

	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setViewportView(getTable());
		}
		return scrollPane;
	}

	public SzybkiInputFile getSzybkiInputFile() {
		return this.inputFile;
	}

	private JTable getTable() {
		if (table == null) {
			table = new JTable();
			table.setRowHeight(48);
			table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
			// table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		}
		return table;
	}

	public void propertyChange(PropertyChangeEvent evt) {

		X.p("Changed in Grid: " + evt.getPropertyName());
		if ("inputFile".equals(evt.getPropertyName())) {
			X.p("New: " + evt.getNewValue());
			tm.fireTableDataChanged();
		}

	}

	private void setServiceInterface(ServiceInterface si) {

		this.si = si;

		tm = new SzybkiInputFileTableModel();
		getTable().setModel(tm);

		final TableColumn tc0 = getTable().getColumnModel().getColumn(0);
		final TableColumn tc1 = getTable().getColumnModel().getColumn(1);
		final TableColumn tc2 = getTable().getColumnModel().getColumn(2);
		final TableColumn tc3 = getTable().getColumnModel().getColumn(3);

		tc0.setPreferredWidth(24);
		tc0.setMaxWidth(24);
		tc0.setMinWidth(24);
		tc1.setPreferredWidth(200);
		tc1.setMinWidth(200);
		tc1.setMaxWidth(200);
		tc2.setMinWidth(400);
		tc2.setPreferredWidth(800);
		tc2.setMaxWidth(800);
		tc3.setPreferredWidth(60);
		tc3.setMaxWidth(60);
		tc3.setMinWidth(60);

		final SzybkiInputFileTableCellRenderer cr = new SzybkiInputFileTableCellRenderer(
				this.si);
		final SzybkiInputFileTableCellEditor ce = new SzybkiInputFileTableCellEditor(
				this.si);
		tc2.setCellRenderer(cr);
		tc2.setCellEditor(ce);

		sorter = new TableRowSorter<SzybkiInputFileTableModel>(tm);

		getTable().setRowSorter(sorter);

	}

	public void setSzybkiInputFile(SzybkiInputFile inputFile) {

		setServiceInterface(inputFile.getServiceInterface());
		this.inputFile = inputFile;
		this.inputFile.addPropertyChangeListener(this);
		tm.setInputFile(this.inputFile);
		showDisabledParameters(true);
	}

	public void showDisabledParameters(boolean show) {

		if (show) {
			sorter.setRowFilter(null);
		} else {
			sorter.setRowFilter(ENABLEDFILTER);
		}
	}
}
