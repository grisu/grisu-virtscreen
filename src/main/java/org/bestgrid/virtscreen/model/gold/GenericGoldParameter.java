package org.bestgrid.virtscreen.model.gold;

import grisu.control.ServiceInterface;

import org.apache.commons.lang.StringUtils;

public class GenericGoldParameter extends AbstractGoldParameter {

	public GenericGoldParameter(ServiceInterface si, String configLine,
			String parentUrl) {
		super();
		init(configLine, parentUrl);
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
		final String temp = getKey(getConfigLine());
		if (StringUtils.isNotBlank(temp)) {
			return temp;
		} else {
			return "n/a";
		}
	}

	@Override
	void initParameter() {
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
	}

}
