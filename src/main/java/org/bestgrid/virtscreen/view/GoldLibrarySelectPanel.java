package org.bestgrid.virtscreen.view;

import java.util.LinkedList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;

import org.apache.commons.lang.ArrayUtils;
import org.bestgrid.virtscreen.model.GoldConfFile;
import org.bestgrid.virtscreen.model.LigandFiles;
import org.vpac.grisu.control.ServiceInterface;
import org.vpac.grisu.control.exceptions.RemoteFileSystemException;
import org.vpac.grisu.frontend.view.swing.jobcreation.widgets.AbstractWidget;
import org.vpac.grisu.model.FileManager;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class GoldLibrarySelectPanel extends AbstractWidget {

	private final DefaultListModel ligandModel = new DefaultListModel();
	private final JList list = new JList(ligandModel);
		
	private ServiceInterface si = null;
	private GoldConfFile confFile = null;
	private JScrollPane scrollPane;
	
	public GoldLibrarySelectPanel() {
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(36dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(85dlu;default):grow"),
				FormFactory.RELATED_GAP_ROWSPEC,}));
		
		JLabel lblLibraries = new JLabel("Libraries");
		add(lblLibraries, "2, 2, right, top");
		add(getScrollPane(), "4, 2, fill, fill");
	}

	@Override
	public void setValue(String value) {
		// TODO Auto-generated method stub

	}
	
	private void loadLibraries() {
		if ( confFile == null || getServiceInterface() == null ) {
			return;
		}
		
		String[] ligandFiles = confFile.getLigandUrls();
		int[] temp = {};
		list.setSelectedIndices(temp);
		List<Integer> selected = new LinkedList<Integer>();
		for ( String ligand : ligandFiles ) {
			System.out.println(ligand);
			int i = ligandModel.indexOf(FileManager.getFilename(ligand));
			if ( i>= 0 ) {
				selected.add(i);
				System.out.println(i);
			}
		}
		
		list.setSelectedIndices(ArrayUtils.toPrimitive(selected.toArray(new Integer[]{})));
	}
	
	public String[] getSelectedLibraryFiles() {
		
		Object[] o = list.getSelectedValues();
		String[] result = new String[o.length];
		for ( int i=0; i<o.length; i++ ) {
			result[i] = (String)o[i];
		}
		return result;
	}
	
	@Override
	public void setServiceInterface(ServiceInterface si) {
		super.setServiceInterface(si);
		List<String> allLigands = null;
		
		try {
			ligandModel.removeAllElements();
			allLigands = getFileManager().listAllChildrenFilesOfRemoteFolder(LigandFiles.VS_LIBRARY_FILES_URL);
			for ( String ligand : allLigands ) {
				ligandModel.addElement(FileManager.getFilename(ligand));
			}
		} catch (RemoteFileSystemException e) {
			e.printStackTrace();
		}
		loadLibraries();
	}
	
	public void setGoldConfFile(GoldConfFile confFile) {
		this.confFile = confFile;
		loadLibraries();
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setViewportView(list);
		}
		return scrollPane;
	}
}
