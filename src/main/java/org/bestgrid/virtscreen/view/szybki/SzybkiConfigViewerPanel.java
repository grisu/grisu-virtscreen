package org.bestgrid.virtscreen.view.szybki;

import grisu.X;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.apache.log4j.Logger;
import org.bestgrid.virtscreen.model.szybki.SzybkiException;
import org.bestgrid.virtscreen.model.szybki.SzybkiInputFile;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

public class SzybkiConfigViewerPanel extends JPanel implements
PropertyChangeListener {

	static final Logger myLogger = Logger
			.getLogger(SzybkiConfigViewerPanel.class.getName());

	private JScrollPane scrollPane;
	private JTextArea textArea;

	private SzybkiInputFile inputFile;
	private boolean showDisabledParameters = false;
	private JButton btnApply;

	private boolean keyTyped = false;

	/**
	 * Create the panel.
	 */
	public SzybkiConfigViewerPanel() {
		setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("97px:grow"),
				FormSpecs.RELATED_GAP_COLSPEC, }, new RowSpec[] {
				FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("65px:grow"),
				FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC, }));
		add(getScrollPane(), "2, 2, fill, fill");
		add(getBtnApply(), "2, 4, right, default");
	}

	private JButton getBtnApply() {
		if (btnApply == null) {
			btnApply = new JButton("Apply");
			btnApply.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					updateSzybkiInputFile();

					keyTyped = false;
					getBtnApply().setEnabled(false);

				}
			});
		}
		return btnApply;
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
			textArea.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {

					keyTyped = true;
					getBtnApply().setEnabled(true);

				}
			});
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

		showDisabledParameters(true);
	}

	public void showDisabledParameters(boolean selected) {

		this.showDisabledParameters = selected;
		updateViewer();

	}

	private void updateSzybkiInputFile() {

		final String text = getTextArea().getText();
		final String[] lines = text.split("\\r?\\n");

		for (final String l : lines) {
			X.p("XXX" + l + "XXX");
		}

		try {
			this.inputFile.createTempFileFromStrings(Arrays.asList(lines));
		} catch (final SzybkiException e) {
			myLogger.error(e.getLocalizedMessage(), e);
		}

	}

	private void updateViewer() {

		final int y = getScrollPane().getVerticalScrollBar().getValue();
		final int c = getTextArea().getCaretPosition();

		final String text = this.inputFile
				.getParametersAsString(showDisabledParameters);
		getTextArea().setText(text);

		getTextArea().setCaretPosition(c);
		getScrollPane().getVerticalScrollBar().setValue(y);

	}
}
