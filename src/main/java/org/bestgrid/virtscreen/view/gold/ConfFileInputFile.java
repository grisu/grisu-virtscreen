package org.bestgrid.virtscreen.view.gold;

import grisu.control.ServiceInterface;
import grisu.frontend.view.swing.files.virtual.GridFileTreePanel;
import grisu.frontend.view.swing.jobcreation.widgets.SingleInputGridFile;
import grisu.model.dto.GridFile;

import java.util.List;

public class ConfFileInputFile extends SingleInputGridFile {

	private final GoldJobInputPanelNew parent;

	public ConfFileInputFile(GoldJobInputPanelNew parent) {
		this.parent = parent;
		setExtensionsToDisplay(new String[] { ".conf" });
		setFoldersSelectable(false);
		setDisplayHiddenFiles(false);
	}

	@Override
	public void setInputFile(GridFile f) {
		super.setInputFile(f);
		parent.parseConfig();
	}

	@Override
	public void setServiceInterface(ServiceInterface si) {
		super.setServiceInterface(si);

		final List<GridFile> roots = GridFileTreePanel.getDefaultRoots(si);

		setRoots(roots);
	}
}
