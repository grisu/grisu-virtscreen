package org.bestgrid.virtscreen.view;

import org.apache.commons.lang.StringUtils;
import org.bestgrid.virtscreen.model.GoldConfFile;
import org.vpac.grisu.frontend.view.swing.jobcreation.widgets.SingleInputGridFile;

public class LigandFileInputFile extends SingleInputGridFile {

	private GoldConfFile confFile = null;

	public LigandFileInputFile() {
		super();
		setBorder(null);
		setExtensionsToDisplay(new String[] { ".mol2" });
	}

	public String getSelectedLigandFile() {
		return getValue();
	}

	public void setGoldConfFile(GoldConfFile confFile) {
		this.confFile = confFile;
	}

	@Override
	protected void setInputFile(String url) {
		super.setInputFile(url);
		if (StringUtils.isNotBlank(url) && !url.equals(selString)) {
			confFile.setLigandDataFiles(new String[] { url });
		}
	}

}
