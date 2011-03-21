package org.bestgrid.virtscreen.model.szybki;

public class EmptyConfigLine implements SzybkiConfigLine {

	public String getLine() {
		return "";
	}

	public boolean isEnabled() {
		return false;
	}

}
