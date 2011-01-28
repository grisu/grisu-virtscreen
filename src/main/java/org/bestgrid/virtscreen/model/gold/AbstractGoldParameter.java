package org.bestgrid.virtscreen.model.gold;

import grisu.control.ServiceInterface;
import grisu.model.FileManager;
import grisu.model.GrisuRegistryManager;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

public abstract class AbstractGoldParameter {

	public static String getKey(String line) {
		line = line.trim();
		if (StringUtils.isBlank(line)) {
			return null;
		}

		int index = line.indexOf("=");
		if (index <= 0) {
			return null;
		} else {
			return line.substring(0, index).trim();
		}

	}

	public static String getValue(String line) {
		line = line.trim();
		if (StringUtils.isBlank(line)) {
			return null;
		}

		int index = line.indexOf("=");
		if (index < 0) {
			return null;
		} else {
			return line.substring(index + 1).trim();
		}

	}

	public static boolean hasKey(String line, String key) {
		if (key.equals(getKey(line))) {
			return true;
		} else {
			return false;
		}

	}

	public static String replaceValue(String line, String newValue) {

		return getKey(line) + " = " + newValue;

	}

	private ServiceInterface si;

	private FileManager fm;
	private String origLine;
	private String newValue;
	private boolean valueChanged = false;

	private String workingDir = null;

	private final Map<String, String> parseMessage = new LinkedHashMap<String, String>();
	private final Map<String, String> fixes = new LinkedHashMap<String, String>();

	private final Set<String> filesToStageIn = new HashSet<String>();

	public void addFileToStageIn(String url) {
		filesToStageIn.add(url);
	}

	protected void addFix(String key, String fix) {
		getFixesMap().put(key, fix);
	}

	protected void addMessage(String key, String message) {
		getParseMessage().put(key, message);
	}

	public void clearStageInFiles() {
		filesToStageIn.clear();
	}

	abstract protected boolean configLineIsValid();

	public String getConfigLine() {
		if (valueChanged) {
			return getNewConfigLine();
		} else {
			return getOriginalLine();
		}
	}

	protected FileManager getFileManager() {
		return this.fm;
	}

	public Set<String> getFilesToStageIn() {
		return filesToStageIn;
	}

	public String getFixes() {
		StringBuffer result = new StringBuffer();

		for (String s : getFixesMap().values()) {
			result.append(s + "\n");
		}
		return result.toString();
	}

	protected Map<String, String> getFixesMap() {
		return fixes;
	}

	public String getMessage() {
		StringBuffer result = new StringBuffer();

		for (String s : getParseMessage().values()) {
			result.append(s + "\n");
		}
		return result.toString();
	}

	abstract protected String getNewConfigLine();

	public String getOriginalLine() {
		return this.origLine;
	}

	abstract public String getParameterName();

	protected Map<String, String> getParseMessage() {
		return parseMessage;
	}

	public String getValue() {
		String temp = getValue(getConfigLine());
		if (StringUtils.isBlank(temp)) {
			return "n/a";
		} else {
			return temp;
		}
	}

	protected String getWorkingDir() {
		return workingDir;
	}

	public void init(String configLine, String workingDir) {
		this.workingDir = workingDir;

		if (StringUtils.isBlank(configLine)) {
			throw new IllegalArgumentException("Config line is blank");
		}

		if (!isResponsibleForLine(configLine)) {
			throw new IllegalArgumentException();
		}

		this.origLine = configLine;
		initParameter();
	}

	abstract void initParameter();

	abstract boolean isOptional();

	abstract boolean isResponsibleForLine(String line);

	public boolean isValid() {

		if (StringUtils.isBlank(this.origLine)) {
			return false;
		}

		return configLineIsValid();
	}

	public void removeFileToStageIn(String url) {
		filesToStageIn.remove(url);
	}

	protected void removeFix(String key) {
		getFixesMap().remove(key);
	}

	protected void removeMessage(String key) {
		getParseMessage().remove(key);
	}

	abstract protected void setNewConfigValue(String value);

	public void setNewValue(String newValue) {
		getFixesMap().clear();
		getParseMessage().clear();
		setNewConfigValue(newValue);
		this.valueChanged = true;
		this.newValue = newValue;
	}

	public void setServiceInterface(ServiceInterface si) {
		this.si = si;
		this.fm = GrisuRegistryManager.getDefault(si).getFileManager();
	}
}
