package org.bestgrid.virtscreen.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.vpac.grisu.X;
import org.vpac.grisu.control.ServiceInterface;
import org.vpac.grisu.frontend.control.clientexceptions.FileTransactionException;
import org.vpac.grisu.frontend.control.login.LoginManager;
import org.vpac.grisu.model.FileManager;
import org.vpac.grisu.model.GrisuRegistryManager;

public class GoldConfFileNew {

	public static void main(String[] args) throws Exception {

		ServiceInterface si = LoginManager.loginCommandline("Local");

		GoldConfFileNew conf = new GoldConfFileNew(si);

		conf.setConfFile("/home/markus/Desktop/jack/exampleJob2/p110a_lead4x.conf");

		for (String line : conf.getNewConfig()) {
			X.p(line);
		}

		X.p("");
		X.p("");

		X.p("Parse messages: ");
		X.p(conf.getParseMessages());

	}

	// gold specific variables
	private final LigandDataFile ligandFile = new LigandDataFile();
	private final ProteinDataFile proteinDataFile = new ProteinDataFile();

	private final Directory directory = new Directory();

	private final ConcatenatedOutput concatenatedOutput = new ConcatenatedOutput();

	private final ScoreParamFile scoreParamFile = new ScoreParamFile();

	private final Set<AbstractGoldParameter> customparameters = new HashSet<AbstractGoldParameter>();

	// internal variables
	private final ServiceInterface si;

	private final FileManager fm;
	private String url;
	private String parentUrl;

	private File templateFile;

	private List<String> configLines;
	private final List<AbstractGoldParameter> parameters = new LinkedList<AbstractGoldParameter>();

	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	public GoldConfFileNew(ServiceInterface si) {

		customparameters.add(ligandFile);
		customparameters.add(proteinDataFile);
		customparameters.add(directory);
		customparameters.add(concatenatedOutput);
		customparameters.add(scoreParamFile);

		this.si = si;

		this.fm = GrisuRegistryManager.getDefault(si).getFileManager();

	}

	public void addListener(PropertyChangeListener l) {
		pcs.addPropertyChangeListener(l);
	}

	public String getConcatenatedOutput() {
		return getConcatenatedOutputObject().getValue();
	}

	private ConcatenatedOutput getConcatenatedOutputObject() {
		return concatenatedOutput;
	}

	public String getConfFile() {
		return this.url;
	}

	public String getDirectory() {
		return getDirectoryObject().getValue();
	}

	private Directory getDirectoryObject() {
		return directory;
	}

	public Set<String> getFilesToStageIn() {

		Set<String> result = new HashSet<String>();

		for (AbstractGoldParameter p : parameters) {
			result.addAll(p.getFilesToStageIn());

		}

		return result;
	}

	public File getJobConfFile() {

		File tmpDir = new File(System.getProperty("java.io.tmpdir"));
		File newConfFile = new File(tmpDir, templateFile.getName());

		newConfFile.delete();
		newConfFile.deleteOnExit();

		try {
			FileUtils.writeLines(newConfFile, getNewConfig());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return newConfFile;
	}

	public LigandDataFile getLigandFile() {
		return ligandFile;
	}

	public String getName() {
		return this.templateFile.getName();
	}

	public List<String> getNewConfig() {
		List<String> result = new LinkedList<String>();

		for (AbstractGoldParameter p : parameters) {
			result.add(p.getConfigLine());
		}

		return result;
	}

	public String getParseMessages() {

		StringBuffer result = new StringBuffer();

		for (AbstractGoldParameter p : parameters) {
			String msg = p.getMessage();
			if (StringUtils.isNotBlank(msg)) {
				result.append("Parameter: " + p.getParameterName() + "\n");
				result.append(msg);
				result.append("\n");
			}
		}

		return result.toString();
	}

	public String getProteinDataFile() {
		return proteinDataFile.getValue();
	}

	private ProteinDataFile getProteinDataFileObject() {
		return proteinDataFile;
	}

	public String getScoreParamFile() {
		return scoreParamFile.getValue();
	}

	private ScoreParamFile getScoreParamFileObject() {
		return scoreParamFile;
	}

	public String getUrl() {
		return url;
	}

	public boolean isValid() {

		for (AbstractGoldParameter p : parameters) {
			if (!p.isValid()) {
				return false;
			}
		}

		return true;
	}

	public void removeListener(PropertyChangeListener l) {
		pcs.removePropertyChangeListener(l);
	}

	public void setConcatenatedOutput(String conc) {
		getConcatenatedOutputObject().setNewValue(conc);
		pcs.firePropertyChange("concatenatedOutput", null, conc);

	}

	public void setConfFile(String url) throws FileTransactionException {
		this.url = url;
		this.parentUrl = FileManager.calculateParentUrl(this.url);
		this.templateFile = this.fm.downloadFile(this.url);

		try {
			this.configLines = FileUtils.readLines(this.templateFile);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		for (String line : configLines) {

			if (StringUtils.isBlank(line)) {
				continue;
			}

			boolean custom = false;
			Iterator<AbstractGoldParameter> i = customparameters.iterator();

			while (i.hasNext()) {
				AbstractGoldParameter temp = i.next();
				if (temp.isResponsibleForLine(line)) {
					temp.setServiceInterface(si);
					temp.init(line);
					parameters.add(temp);
					custom = true;
					customparameters.remove(temp);
					break;
				}
			}

			if (!custom) {
				parameters.add(new GenericGoldParameter(si, line));
			}

		}

		pcs.firePropertyChange("confFile", null, url);
	}

	public void setDirectory(String dir) {
		getDirectoryObject().setNewConfigValue(dir);
		pcs.firePropertyChange("directory", null, dir);

	}

	public void setProteinDataFile(String file) {
		getProteinDataFileObject().setNewValue(file);
		pcs.firePropertyChange("proteinDataFile", null, file);
	}

	public void setScoreParamFile(String file) {
		getScoreParamFileObject().setNewValue(file);
		pcs.firePropertyChange("scoreParamFile", null, file);
	}

}
