package org.bestgrid.virtscreen.model;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.vpac.grisu.control.ServiceInterface;
import org.vpac.grisu.frontend.control.clientexceptions.FileTransactionException;
import org.vpac.grisu.model.FileManager;
import org.vpac.grisu.model.GrisuRegistryManager;

public class GoldConfFile {

	public enum PARAMETER {
		protein_datafile, ligand_data_file, directory, concatenated_output, score_param_file
	}

	private final String url;
	private final ServiceInterface si;
	private final FileManager fm;
	private final File templateFile;

	private File newConfFile;

	private final List<String> configLines;

	private String currentJobDirectory = null;

	private final Map<PARAMETER, String> parameters = new HashMap<PARAMETER, String>();

	public GoldConfFile(ServiceInterface si, String url)
			throws FileTransactionException {
		this.si = si;
		this.url = url;
		this.fm = GrisuRegistryManager.getDefault(si).getFileManager();

		this.templateFile = this.fm.downloadFile(this.url);

		try {
			this.configLines = FileUtils.readLines(this.templateFile);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	public void setParameter(PARAMETER key, String value) {
		parameters.put(key, value);
	}

	public void updateConfFile() {

		for (PARAMETER key : parameters.keySet()) {

			switch (key) {

			case ligand_data_file:
				setLigand_data_file(parameters.get(key));
				break;

			default:

				for (int i = 0; i < configLines.size(); i++) {
					if (configLines.get(i).trim().startsWith(key.toString())) {
						String tmp = key + " = " + parameters.get(key);
						configLines.set(i, tmp);
						break;
					}
				}
			}
		}

		try {
			newConfFile.delete();
			newConfFile.deleteOnExit();
			FileUtils.writeLines(newConfFile, configLines);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	private void setLigand_data_file(String path) {

		for (int i = 0; i < configLines.size(); i++) {
			if (configLines.get(i).trim()
					.startsWith(PARAMETER.ligand_data_file.toString())) {
				String[] parts = configLines.get(i).split(" ");
				String tmp = PARAMETER.ligand_data_file.toString() + " " + path
						+ " " + parts[parts.length - 1];
				configLines.set(i, tmp);
				break;
			}
		}

	}

	public File getConfFile() {

		if (newConfFile == null) {
			// temp = File.createTempFile(
			// FilenameUtils.getBaseName(file.getName()), "."
			// + FilenameUtils.getExtension(file.getName()));
			// temp = File.createTempFile("test", "suffix");
			// temp.deleteOnExit();
			File tmpDir = new File(System.getProperty("java.io.tmpdir"));
			newConfFile = new File(tmpDir, templateFile.getName());
			updateConfFile();
		}

		return newConfFile;

	}

	public String getName() {
		return this.templateFile.getName();
	}

	public void setJobDirectory(String jobDirectoryUrl) {
		currentJobDirectory = jobDirectoryUrl;
	}

}
