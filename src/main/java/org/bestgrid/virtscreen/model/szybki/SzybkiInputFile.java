package org.bestgrid.virtscreen.model.szybki;

import grisu.control.ServiceInterface;
import grisu.frontend.control.clientexceptions.FileTransactionException;
import grisu.model.FileManager;
import grisu.model.GrisuRegistryManager;

import java.io.File;
import java.util.List;


public class SzybkiInputFile {

	private File templateFile;
	private String url;
	private String parentUrl;

	private final ServiceInterface si;
	private final FileManager fm;

	private final List<SzybkiParameter> parameters = null;

	public SzybkiInputFile(ServiceInterface si) {
		this.si = si;
		this.fm = GrisuRegistryManager.getDefault(si).getFileManager();
	}

	public File getJobConfFile() {

		// File tmpDir = new File(System.getProperty("java.io.tmpdir"));
		// File newConfFile = new File(tmpDir, templateFile.getName());
		//
		// newConfFile.delete();
		// newConfFile.deleteOnExit();
		//
		// try {
		// FileUtils.writeLines(newConfFile,
		// SzybkiParameter.createSyzbkiParamFileContent(parameters));
		// } catch (IOException e) {
		// throw new RuntimeException(e);
		// }
		//
		// return newConfFile;

		return templateFile;

	}

	public String getName() {
		return this.templateFile.getName();
	}

	private void parseInputFile() {

	}

	public void setInputFile(String url) throws FileTransactionException {

		this.url = url;
		this.parentUrl = FileManager.calculateParentUrl(this.url);
		this.templateFile = this.fm.downloadFile(this.url);

		parseInputFile();
	}

}
