package grisu.frontend.view.swing.jobcreation.widgets;

import grisu.model.dto.GridFile;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class OrderableMultiInputGridFile extends AbstractFileWidget {

	private static final long serialVersionUID = 1L;
	
	private final DefaultListModel fileModel = new DefaultListModel();


	private JScrollPane scrollPane;
	private DragDropList list;
	private JButton addButton;
	private JButton removeButton;

	public OrderableMultiInputGridFile() {
		super();
		setBorder(new TitledBorder(null, "Input files", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC, }, new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, }));
		add(getScrollPane(), "2, 2, 3, 1, fill, fill");
		add(getRemoveButton(), "2, 4, right, default");
		add(getAddButton(), "4, 4, right, default");

		setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setValue(String value) {
		// TODO Auto-generated method stub

	}

	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setViewportView(getList());
		}
		return scrollPane;
	}

	private DragDropList getList() {
		if (list == null) {
			list = new DragDropList(fileModel);

		}
		return list;
	}

	private void addFile(GridFile f) {

//		System.out.println(f.getUrl());
		fileModel.addElement(f);

	}
	
	public void clearFiles() {
		fileModel.clear();
	}

	private JButton getAddButton() {
		if (addButton == null) {
			addButton = new JButton("Add");
			addButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					Set<GridFile> files = popupFileDialogAndAskForFiles();
					TreeSet<GridFile> filesSorted = Sets.newTreeSet(files);
					for (GridFile f : filesSorted) {
						if (f != null) {
							addFile(f);
						}
					}
				}
			});
		}
		return addButton;
	}

	private JButton getRemoveButton() {
		if (removeButton == null) {
			removeButton = new JButton("Remove");
			removeButton.addActionListener(new ActionListener() {


				public void actionPerformed(ActionEvent e) {

					final Object[] selFiles = getList().getSelectedValues();

					for (final Object file : selFiles) {
						fileModel.removeElement(file);
					}

				}
			});
		}
		return removeButton;
	}

	public List<GridFile> getInputFiles() {
		List<GridFile> result = Lists.newLinkedList();
		for (int i = 0; i < fileModel.getSize(); i++) {
			final GridFile existingFile = (GridFile) fileModel
					.getElementAt(i);
			result.add(existingFile);
		}
		return result;
	}
}
