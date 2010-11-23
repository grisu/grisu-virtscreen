package org.bestgrid.virtscreen.model;

import org.apache.commons.lang.StringUtils;

public class ConcatenatedOutput extends AbstractGoldParameter {

	private String newLine = null;

	@Override
	protected boolean configLineIsValid() {
		return StringUtils.isNotBlank(getValue(getConfigLine()));
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
	boolean isOptional() {
		return false;
	}

	@Override
	boolean isResponsibleForLine(String line) {
		return hasKey(line, getParameterName());
	}

	@Override
	protected void setNewConfigValue(String value) {

		if (StringUtils.isBlank(value)) {
			throw new IllegalArgumentException(
					"New value for concatenated_output is blank");
		}

		newLine = replaceValue(getOriginalLine(), value);
	}

}
