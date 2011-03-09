package org.bestgrid.virtscreen.model.szybki;

import grisu.control.ServiceInterface;
import grisu.frontend.control.clientexceptions.FileTransactionException;
import grisu.frontend.control.login.LoginManager;
import grisu.model.FileManager;
import grisu.model.GrisuRegistryManager;
import grisu.model.UserEnvironmentManager;
import grisu.model.dto.GridFile;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.bestgrid.virtscreen.model.szybki.SzybkiParameter.PARAM;


public class SzybkiInputFile implements PropertyChangeListener {

	public static void main(String[] args) throws Exception {

		ServiceInterface si = LoginManager.loginCommandline("BeSTGRID");

		FileManager fm = GrisuRegistryManager.getDefault(si).getFileManager();
		UserEnvironmentManager uem = GrisuRegistryManager.getDefault(si)
		.getUserEnvironmentManager();

		String input = "grid://";
		while (!"exit".equals(input)) {

			// System.out.println("Available groups:");
			// for (String g : uem.getAllAvailableUniqueGroupnames(true)) {
			// System.out.println(g);
			// }
			// System.out.println();

			System.out.println("Ready...");

			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));

			try {
				String lastInput = input;
				input = br.readLine();
				if (StringUtils.isBlank(input)) {
					input = lastInput;
				} else if ("exit".equals(input)) {
					System.exit(0);
				}
			} catch (IOException ioe) {
				System.out.println("IO error trying to read user input!");
				System.exit(1);
			}

			try {

				SzybkiInputFile sif = new SzybkiInputFile(si);

				sif.setInputFile("/home/markus/Desktop/jack/szybki/example2.param");

				for (SzybkiParameter p : sif.getParameters()) {
					if (p.getType() == SzybkiParameter.TYPE.UNDEF) {
						continue;
					}
					if (p.getComment() == null) {
						System.out.println(p.getParameterName() + "\t\t"
								+ p.getParameterValue());
					} else {
						System.out.println(p.getParameterName() + "\t\t"
								+ p.getParameterValue() + "\t\t"
								+ p.getComment());
					}
				}

			} catch (Exception e) {
				System.out.println(e.getLocalizedMessage());
			}

		}

	}

	/**
	 * Parses one line from the szybki input file and converts it into a
	 * SzybkiParameter.
	 * 
	 * @param line
	 *            the line
	 * @return the parameter
	 * @throws SzybkiException
	 */
	public static SzybkiConfigLine parseLine(ServiceInterface si,
			String origline) throws SzybkiException {

		if (StringUtils.isBlank(origline)) {
			return new EmptyConfigLine();
		}
		String line = origline.trim();

		if (line.startsWith("#")) {
			return new SzybkiCommentLine(line);
		}

		int index = line.indexOf(" ");

		if (index < 1) {
			throw new SzybkiException(
					"No whitespace in line, can't separate key and value: "
					+ line);
		}

		int commentIndex = line.indexOf("#");
		if (commentIndex < 0) {
			String key = line.substring(1, index).trim();
			String value = line.substring(index + 1).trim();
			PARAM p = PARAM.fromString(key);
			if (p == null) {
				throw new SzybkiException("String " + key
						+ " not a valid configuration parameter.");
			}
			return new SzybkiParameter(origline, PARAM.fromString(key),
					new ParameterValue(si, p, value), null);
		} else {
			String key = line.substring(1, index).trim();
			String value = line.substring(index + 1, commentIndex).trim();
			String comment = line.substring(commentIndex + 1).trim();
			PARAM p = PARAM.fromString(key);
			if (p == null) {
				throw new SzybkiException("String " + key
						+ " not a valid configuration parameter.");
			}
			return new SzybkiParameter(line, p,
					new ParameterValue(si, p, value), comment);
		}

	}

	private File templateFile;
	private String url;

	private String parentUrl;
	private final ServiceInterface si;

	private final FileManager fm;
	private List<SzybkiConfigLine> allLines = new LinkedList<SzybkiConfigLine>();

	private List<SzybkiParameter> parameters = new LinkedList<SzybkiParameter>();

	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	public SzybkiInputFile(ServiceInterface si) {
		this.si = si;
		this.fm = GrisuRegistryManager.getDefault(si).getFileManager();
	}

	public void addPropertyChangeListener(PropertyChangeListener l) {
		pcs.addPropertyChangeListener(l);
	}

	public List<String> createConfig(boolean allParameters) {

		List<String> result = new LinkedList<String>();
		for (SzybkiConfigLine line : allLines) {
			if (allParameters || line.isEnabled()) {
				result.add(line.getLine());
			}
		}
		return result;
	}

	public void createTempFileFromStrings(List<String> lines)
	throws SzybkiException {

		File tmpDir = new File(System.getProperty("java.io.tmpdir"));
		File newConfFile = new File(tmpDir, "szybki_tmp_conf_file");

		newConfFile.delete();

		try {
			FileUtils.writeLines(newConfFile, lines);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		setInputFile(newConfFile.toURI().toString());

	}

	public String getInputFile() {
		return url;
	}

	public File getJobConfFile() {

		File tmpDir = new File(System.getProperty("java.io.tmpdir"));
		File newConfFile = new File(tmpDir, templateFile.getName());

		newConfFile.delete();
		newConfFile.deleteOnExit();

		try {
			FileUtils.writeLines(newConfFile, createConfig(true));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return newConfFile;

	}

	public String getName() {
		return this.templateFile.getName();
	}

	public SzybkiParameter getParameter(int index) {
		return this.parameters.get(index);
	}

	public SzybkiParameter getParameter(PARAM param) {

		for ( SzybkiParameter p : parameters ) {
			if ( p.getParameterName().equals(param) ) {
				return p;
			}
		}
		return null;

	}

	public List<SzybkiParameter> getParameters() {
		return this.parameters;
	}

	public String getParametersAsString(boolean allParameters) {

		StringBuffer r = new StringBuffer();
		for (String l : createConfig(allParameters)) {
			r.append(l+"\n");
		}

		return r.toString();
	}

	public ServiceInterface getServiceInterface() {
		return this.si;
	}

	private void parseInputFile() throws SzybkiException {

		if (parameters != null) {
			for (SzybkiParameter p : parameters) {
				p.getParameterValue().removePropertyChangeListener(this);
				p.removePropertyChangeListener(this);
			}
		}

		allLines = new LinkedList<SzybkiConfigLine>();
		parameters = new LinkedList<SzybkiParameter>();

		if (this.templateFile == null) {
			throw new SzybkiException("Not template file set.");
		}

		if (!this.templateFile.canRead()) {
			throw new SzybkiException("Can't read file: "
					+ this.templateFile.getPath());
		}

		List<String> lines = null;
		try {
			lines = FileUtils.readLines(this.templateFile);
		} catch (IOException e) {
			throw new SzybkiException(e);
		}

		for (String line : lines) {
			SzybkiConfigLine scl = parseLine(si, line);
			allLines.add(scl);
			if (scl instanceof SzybkiParameter) {
				SzybkiParameter sp = (SzybkiParameter)scl;
				parameters.add(sp);
				sp.addPropertyChangeListener(this);
				sp.getParameterValue().addPropertyChangeListener(this);
			}
		}

	}

	public void propertyChange(PropertyChangeEvent evt) {

		pcs.firePropertyChange(evt);

	}

	public void removePropertyChangeListener(PropertyChangeListener l) {
		pcs.removePropertyChangeListener(l);
	}

	public void setInputFile(GridFile file) throws SzybkiException {

		setInputFile(file.getUrl());
	}

	public void setInputFile(String url) throws SzybkiException {

		this.url = url;
		this.parentUrl = FileManager.calculateParentUrl(this.url);
		try {
			this.templateFile = this.fm.downloadFile(this.url);
		} catch (FileTransactionException e) {
			throw new SzybkiException(e);
		}

		parseInputFile();

		pcs.firePropertyChange("inputFile", null, this.url);
	}
}
