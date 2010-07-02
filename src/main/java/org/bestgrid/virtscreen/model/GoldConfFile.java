package org.bestgrid.virtscreen.model;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.vpac.grisu.control.ServiceInterface;
import org.vpac.grisu.frontend.control.clientexceptions.FileTransactionException;
import org.vpac.grisu.model.FileManager;
import org.vpac.grisu.model.GrisuRegistryManager;

public class GoldConfFile {

	public static final String PROTEIN_DATA_FILE = "protein_datafile";
	public static final String LIGAND_DATA_FILE = "ligand_data_file";
	public static final String DIRECTORY = "directory";
	public static final String CONCATENATED_OUTPUT = "concatenated_output";
	public static final String SCORE_PARAM_FILE = "score_param_file";

	private final String url;
	private final ServiceInterface si;
	private final FileManager fm;
	private final File file;

	private final List<String> configLines;

	public GoldConfFile(ServiceInterface si, String url)
			throws FileTransactionException {
		this.si = si;
		this.url = url;
		this.fm = GrisuRegistryManager.getDefault(si).getFileManager();

		this.file = this.fm.downloadFile(this.url);

		try {
			this.configLines = FileUtils.readLines(this.file);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	private void setProperty(String key, String value) {

		for (int i = 0; i < configLines.size(); i++) {
			if (configLines.get(i).trim().startsWith(key)) {
				String tmp = key + " = " + value;
				configLines.set(i, tmp);
			}
		}

	}

	public File getConfFile() {

		File temp;
		try {
			temp = File.createTempFile("pattern", ".suffix");
			temp.deleteOnExit();
			FileUtils.writeLines(temp, configLines);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return temp;

	}

	public String getName() {
		return this.file.getName();
	}

}
