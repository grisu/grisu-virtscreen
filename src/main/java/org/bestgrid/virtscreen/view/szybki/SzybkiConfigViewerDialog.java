package org.bestgrid.virtscreen.view.szybki;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import org.bestgrid.virtscreen.model.szybki.SzybkiInputFile;

public class SzybkiConfigViewerDialog extends JDialog {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			final SzybkiConfigViewerDialog dialog = new SzybkiConfigViewerDialog(
					null);
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private final JPanel contentPanel = new JPanel();
	private SzybkiConfigViewerPanel szybkiConfigViewerPanel = null;

	/**
	 * Create the dialog.
	 */
	public SzybkiConfigViewerDialog(final SzybkiInputFile file) {
		super();
		setModal(false);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			szybkiConfigViewerPanel = new SzybkiConfigViewerPanel();
			szybkiConfigViewerPanel.setSzybkiInputFile(file);
			contentPanel.add(szybkiConfigViewerPanel, BorderLayout.CENTER);
		}
		{
			final JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				final JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						szybkiConfigViewerPanel.removeFile();
						SzybkiConfigViewerDialog.this.dispose();
					}
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}

		setVisible(true);
	}

}
