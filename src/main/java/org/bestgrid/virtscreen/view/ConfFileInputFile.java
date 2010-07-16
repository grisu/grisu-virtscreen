package org.bestgrid.virtscreen.view;

import org.apache.commons.lang.StringUtils;
import org.vpac.grisu.frontend.view.swing.jobcreation.widgets.SingleInputFile;

public class ConfFileInputFile extends SingleInputFile {

	private final GoldJobInputPanel parent;

	public ConfFileInputFile(GoldJobInputPanel parent) {
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
