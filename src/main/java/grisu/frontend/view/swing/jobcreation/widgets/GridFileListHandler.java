package grisu.frontend.view.swing.jobcreation.widgets;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import javax.swing.JList;
import javax.swing.TransferHandler;

public class GridFileListHandler extends TransferHandler {
	
	private DragDropList list;
	
	public GridFileListHandler(DragDropList list) {
		this.list = list;
	}

	public boolean canImport(TransferHandler.TransferSupport support) {
		if (!support.isDataFlavorSupported(DataFlavor.stringFlavor)) {
			return false;
		}
		JList.DropLocation dl = (JList.DropLocation) support.getDropLocation();
		if (dl.getIndex() == -1) {
			return false;
		} else {
			return true;
		}
	}

	public boolean importData(TransferHandler.TransferSupport support) {
		if (!canImport(support)) {
			return false;
		}

		Transferable transferable = support.getTransferable();
		String indexString;
		try {
			indexString = (String) transferable
					.getTransferData(DataFlavor.stringFlavor);
		} catch (Exception e) {
			return false;
		}

		int index = Integer.parseInt(indexString);
		JList.DropLocation dl = (JList.DropLocation) support.getDropLocation();

		int dropTargetIndex = dl.getIndex();
		
		if ( dropTargetIndex == 0 && index == 0 ) {
			return false;
		}
		
		Object objToMove = null;
		try {
			objToMove = list.model.get(index);
		} catch (Exception e) {
			e.printStackTrace();
		}
		list.model.remove(index);

		int indexToInsert = dropTargetIndex-1;
		if (indexToInsert < 0 ) {
			indexToInsert = 0;
		}
		try {
			list.model.insertElementAt(objToMove, indexToInsert);
		}catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}
}
