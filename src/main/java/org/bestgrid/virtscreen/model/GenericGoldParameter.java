package org.bestgrid.virtscreen.model;

import org.apache.commons.lang.StringUtils;
import org.vpac.grisu.control.ServiceInterface;

public class GenericGoldParameter extends AbstractGoldParameter {

	public GenericGoldParameter(ServiceInterface si, String configLine) {
		super();
		init(configLine);
		setServiceInterface(si);
	}

	@Override
	protected boolean configLineIsValid() {
		return true;
	}

	@Override
	protected String getNewConfigLine() {
		return null;
	}

	@Override
	public String getParameterName() {
		String temp = getKey(getConfigLine());
		if (StringUtils.isNotBlank(temp)) {
			return temp;
		} else {
			return "n/a";
		}
	}

	@Override
	boolean isOptional() {
		return false;
	}

	@Override
	boolean isResponsibleForLine(String line) {
		return true;
	}

	@Override
	protected void setNewConfigValue(String value) {
		// do nothing, not supported nor necessary
	}

}
