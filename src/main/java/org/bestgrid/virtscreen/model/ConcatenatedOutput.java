package org.bestgrid.virtscreen.model;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

public class ConcatenatedOutput extends AbstractGoldParameter {

	private String newLine = null;

	@Override
	protected boolean configLineIsValid() {
		return true;
	}

	@Override
	protected String getNewConfigLine() {
		return newLine;
	}

	@Override
	public String getParameterName() {
		return "concatenated_output";
	}

	@Override
	void initParameter() {
		setNewValue("./" + FilenameUtils.getName(getValue(getConfigLine())));

	}

	@Override
	boolean isOptional() {
		return false;
	}

	@Override
	boolean isResponsibleForLine(String line) {
		return hasKey(line, getParameterName());
	}

	@Override
	protected void setNewConfigValue(String value) {

		removeMessage("static");
		if (StringUtils.isBlank(value)) {
			throw new IllegalArgumentException(
					"New value for concatenated_output is blank");
		}

		newLine = replaceValue(getOriginalLine(), value);
		addMessage("static", "  -> Setting concatenated_output to be: " + value
				+ "\n");
	}

}
