package org.bestgrid.virtscreen.model;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
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

//	private String currentJobDirectory = null;

	private final Map<PARAMETER, String> parameters = new HashMap<PARAMETER, String>();
	
	private LigandFiles libFile = null;


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
		libFile = new LigandFiles(si);
		libFile.setFiles(getLigandPaths());
	}

	public void setParameter(PARAMETER key, String value) {
		parameters.put(key, value);
	}

	public void updateConfFile() {
		
		String[] tempArray = Arrays.copyOf(libFile.getRemotePaths(), libFile.getRemotePaths().length+1);
		tempArray[tempArray.length-1] = new Integer(getLigandDockingAmount()).toString();
		String newLigandValue = StringUtils.join(tempArray, "");
		
		parameters.put(PARAMETER.ligand_data_file, newLigandValue);
		

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

//	public void setJobDirectory(String jobDirectoryUrl) {
//		currentJobDirectory = jobDirectoryUrl;
//	}
	
	private String getParameter(PARAMETER key) {
		
		if ( PARAMETER.ligand_data_file.equals(key) ) {
			
			for (int i = 0; i < configLines.size(); i++) {
				if (configLines.get(i).trim()
						.startsWith(PARAMETER.ligand_data_file.toString())) {
					String temp = configLines.get(i).substring(key.toString().length()).trim();
					return temp;
				}
			}
			
		} else {
		for ( String line : this.configLines ) {
			if ( line.trim().startsWith(key.toString()) ) {
				return line.substring(line.indexOf("=")+1).trim();
			}
		}
		}
		
		return null;
	}

	public String getLigandValue() {
		return getParameter(PARAMETER.ligand_data_file);
	}
	
	public String[] getLigandPaths() {
		
		String[] temp = getLigandValue().split("\\s");
		
		return Arrays.copyOf(temp, temp.length-1);
		
	}
	
	public int getLigandDockingAmount() {
		
		String[] temp = getLigandValue().split("\\s");
		String temp2 = temp[temp.length-1];
		
		try {
			return Integer.parseInt(temp2);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public String getProteinFilePath() {
		return getParameter(PARAMETER.protein_datafile);
	}

}
