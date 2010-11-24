package org.bestgrid.virtscreen.view;

import org.apache.commons.lang.StringUtils;
import org.vpac.grisu.frontend.view.swing.jobcreation.widgets.SingleInputGridFile;

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

}
