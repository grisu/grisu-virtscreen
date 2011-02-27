package org.bestgrid.virtscreen.view.gold;

import grisu.control.ServiceInterface;
import grisu.frontend.view.swing.jobcreation.widgets.SingleInputGridFile;
import grisu.model.GrisuRegistryManager;
import grisu.model.dto.GridFile;

import java.util.List;

import org.bestgrid.virtscreen.view.GrisuVirtScreen;

public class ConfFileInputFile extends SingleInputGridFile {

	private final GoldJobInputPanelNew parent;

	public ConfFileInputFile(GoldJobInputPanelNew parent) {
		this.parent = parent;
		setExtensionsToDisplay(new String[] { ".conf" });

	}

	@Override
	public void setInputFile(GridFile f) {
		super.setInputFile(f);
		parent.parseConfig();
	}

	@Override
	public void setServiceInterface(ServiceInterface si) {
		super.setServiceInterface(si);

		List<GridFile> roots = (List<GridFile>) (GrisuRegistryManager
				.getDefault(getServiceInterface())
				.get(GrisuVirtScreen.VIRTSCREEN_ROOTS));

		setRoots(roots);
	}
}
