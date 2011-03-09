package org.bestgrid.virtscreen.model.szybki;

public class SzybkiCommentLine implements SzybkiConfigLine {

	private final String line;

	public SzybkiCommentLine(String line) {
		this.line = line;
	}

	public String getLine() {
		return line;
	}

	public boolean isEnabled() {
		return false;
	}

}
