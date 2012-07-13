package org.bestgrid.virtscreen.view.gold;

import grisu.control.ServiceInterface;
import grisu.frontend.view.swing.files.virtual.GridFileTreePanel;
import grisu.frontend.view.swing.jobcreation.widgets.SingleInputGridFile;
import grisu.model.dto.GridFile;

import java.util.List;

import org.bestgrid.virtscreen.model.gold.GoldConfFile;

public class LigandFileInputFile extends SingleInputGridFile {

	private GoldConfFile confFile = null;

	public LigandFileInputFile() {
		super();
		setBorder(null);
		setExtensionsToDisplay(new String[] { ".mol2" });
		setFoldersSelectable(false);
		setDisplayHiddenFiles(false);
	}

	public String getSelectedLigandFile() {
		return getValue();
	}

	public void setGoldConfFile(GoldConfFile confFile) {
		this.confFile = confFile;
	}

	@Override
	public void setInputFile(GridFile f) {
		if (f == null) {
			return;
		}
		super.setInputFile(f);
		confFile.setLigandDataFiles(new String[] { f.getUrl() });
	}

	@Override
	public void setServiceInterface(ServiceInterface si) {
		super.setServiceInterface(si);

		final List<GridFile> roots = GridFileTreePanel.getDefaultRoots(si);

		setRoots(roots);
	}

}
