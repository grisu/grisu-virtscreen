package org.bestgrid.virtscreen.model.szybki;

import java.util.List;

public class SzybkiParameter {

	public static List<String> createSyzbkiParamFileContent(
			List<SzybkiParameter> parameters) {
		return null;
	}

	private final String parameterName;
	private final String parameterValue;

	public SzybkiParameter(String parameterName, String parameterValue) {
		this.parameterName = parameterName;
		this.parameterValue = parameterValue;
	}

	public String getParameterName() {
		return this.parameterName;
	}

	public String getParameterValue() {
		return this.parameterValue;
	}

}
