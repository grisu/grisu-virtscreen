package org.bestgrid.virtscreen.view.gold;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bestgrid.virtscreen.view.GrisuVirtScreen;
import org.vpac.grisu.control.ServiceInterface;
import org.vpac.grisu.frontend.view.swing.jobcreation.widgets.SingleInputGridFile;
import org.vpac.grisu.model.GrisuRegistryManager;
import org.vpac.grisu.model.dto.GridFile;

public class ConfFileInputFile extends SingleInputGridFile {

	private final GoldJobInputPanelNew parent;

	public ConfFileInputFile(GoldJobInputPanelNew parent) {
		this.parent = parent;
		setExtensionsToDisplay(new String[] { ".conf" });

	}

	@Override
	protected void setInputFile(String url) {
		super.setInputFile(url);
		if (StringUtils.isNotBlank(url) && !url.equals(selString)) {
			parent.parseConfig();
		}
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
