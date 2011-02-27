package org.bestgrid.virtscreen.view.szybki;

import grisu.control.ServiceInterface;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import org.bestgrid.virtscreen.model.szybki.SzybkiException;
import org.bestgrid.virtscreen.model.szybki.SzybkiInputFile;

public class SzybkiInputFileGrid extends JPanel {

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
			// table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		}
		return table;
	}

	public void loadSzybkiInputFile(String inputFile) {

		if (si == null) {
			throw new IllegalStateException(
			"ServiceInterface not initialized yet.");
		}

		try {
			this.inputFile.setInputFile(inputFile);
			showDisabledParameters(false);

		} catch (SzybkiException e) {
			e.printStackTrace();
		}


	}

	public void setServiceInterface(ServiceInterface si) {

		this.si = si;

		this.inputFile = new SzybkiInputFile(si);

		SzybkiInputFileTableModel tm = new SzybkiInputFileTableModel(inputFile);
		getTable().setModel(tm);

		TableColumn tc0 = getTable().getColumnModel().getColumn(0);
		TableColumn tc1 = getTable().getColumnModel().getColumn(1);
		TableColumn tc2 = getTable().getColumnModel().getColumn(2);
		TableColumn tc3 = getTable().getColumnModel().getColumn(3);

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

		SzybkiInputFileTableCellRenderer cr = new SzybkiInputFileTableCellRenderer();
		SzybkiInputFileTableCellEditor ce = new SzybkiInputFileTableCellEditor(
				inputFile.getServiceInterface());
		tc2.setCellRenderer(cr);
		tc2.setCellEditor(ce);

		sorter = new TableRowSorter<SzybkiInputFileTableModel>(
				tm);


		getTable().setRowSorter(sorter);

	}

	public void showDisabledParameters(boolean show) {
		if (show) {
			sorter.setRowFilter(null);
		} else {
			sorter.setRowFilter(ENABLEDFILTER);
		}
	}
}
