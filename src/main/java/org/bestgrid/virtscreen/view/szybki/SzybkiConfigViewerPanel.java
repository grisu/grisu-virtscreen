package org.bestgrid.virtscreen.view.szybki;

import grisu.X;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.bestgrid.virtscreen.model.szybki.SzybkiInputFile;

public class SzybkiConfigViewerPanel extends JPanel implements
PropertyChangeListener {
	private JScrollPane scrollPane;
	private JTextArea textArea;

	private SzybkiInputFile inputFile;

	/**
	 * Create the panel.
	 */
	public SzybkiConfigViewerPanel() {
		setLayout(new BorderLayout(0, 0));
		add(getScrollPane(), BorderLayout.CENTER);
	}

	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setViewportView(getTextArea());
		}
		return scrollPane;
	}

	private JTextArea getTextArea() {
		if (textArea == null) {
			textArea = new JTextArea();
		}
		return textArea;
	}

	public void propertyChange(PropertyChangeEvent evt) {

		updateViewer();

	}

	public void removeFile() {
		this.inputFile.removePropertyChangeListener(this);
		this.inputFile = null;

	}

	public void setSzybkiInputFile(SzybkiInputFile file) {
		this.inputFile = file;
		this.inputFile.addPropertyChangeListener(this);

		updateViewer();

	}

	private void updateViewer() {
		X.p("UPdateddd....");
		getTextArea().setText("");
		String text = this.inputFile.getParametersAsString();
	}
}
