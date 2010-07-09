package org.bestgrid.virtscreen.view;

import org.apache.commons.lang.StringUtils;
import org.vpac.grisu.frontend.view.swing.jobcreation.widgets.SingleInputFile;

public class ConfFileInputFile extends SingleInputFile {

	private final GoldJobInputPanel parent;

	public ConfFileInputFile(GoldJobInputPanel parent) {
		this.parent = parent;
	}

	@Override
	protected void setInputFile(String url) {
		super.setInputFile(url);

		if (StringUtils.isNotBlank(url)) {
			parent.parseConfig();
		}
	}

}
