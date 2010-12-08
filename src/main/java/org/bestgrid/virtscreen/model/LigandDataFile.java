package org.bestgrid.virtscreen.model;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.vpac.grisu.control.exceptions.RemoteFileSystemException;
import org.vpac.grisu.model.FileManager;
import org.vpac.grisu.model.dto.GridFile;

public class LigandDataFile extends AbstractGoldParameter {

	static final Logger myLogger = Logger.getLogger(LigandDataFile.class
			.getName());

	public static final String VS_LIBRARY_FILES_URL = "gsiftp://ng2.auckland.ac.nz/home/grid-vs/libraries/";
	public static final String VS_LIBRARY_LOCAL_PATH = "/home/grid-vs/libraries/";

	public static final String LIGAND_PARAMETER_NAME = "ligand_data_file";

	public static int getLigandDockingAmount(String configLine) {
		String[] temp = configLine.split("\\s");
		String temp2 = temp[temp.length - 1];
		try {
			return Integer.parseInt(temp2);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public static String[] getLigandFiles(String configLine) {
		String[] temp = configLine.substring(LIGAND_PARAMETER_NAME.length())
				.trim().split("\\s");

		return Arrays.copyOf(temp, temp.length - 1);
	}

	private String newLine = null;

	private List<String> remoteLigandFiles = null;

	private boolean valid = false;

	@Override
	protected boolean configLineIsValid() {

		return valid;
	}

	public int getLigandDockingAmount() {
		return getLigandDockingAmount(getConfigLine());
	}

	public String[] getLigandFiles() {
		return getLigandFiles(getConfigLine());
	}

	@Override
	protected String getNewConfigLine() {
		return newLine;
	}

	@Override
	public String getParameterName() {
		return LIGAND_PARAMETER_NAME;
	}

	public synchronized List<String> getRemoteLigandFiles()
			throws RemoteFileSystemException {

		if (remoteLigandFiles == null) {
			GridFile folder = getFileManager().ls(VS_LIBRARY_FILES_URL);
			if (folder == null) {
				throw new RemoteFileSystemException(
						"Could not find children for common library location.");
			}
			remoteLigandFiles = GridFile.getChildrenNames(folder);
		}
		return remoteLigandFiles;

	}

	@Override
	public void initParameter() {
		setNewValue(getOriginalLine());
	}

	@Override
	boolean isOptional() {
		return false;
	}

	@Override
	boolean isResponsibleForLine(String line) {
		if (StringUtils.isBlank(line)) {
			return false;
		}
		return line.trim().startsWith(getParameterName());
	}

	public void setLigandDockingAmount(int amount) {
		removeMessage("amount");
		newLine = LIGAND_PARAMETER_NAME + " "
				+ StringUtils.join(getLigandFiles(), " ") + " " + amount;
		addMessage("amount", "  Set ligand docking amount to " + amount + "\n");
	}

	public void setLigandFiles(String[] files) {

		valid = false;
		removeMessage("files");
		removeFix("files");

		StringBuffer msg = new StringBuffer();
		StringBuffer fix = new StringBuffer();

		clearStageInFiles();

		if ((files == null) || (files.length == 0)
				|| ((files.length == 1) && StringUtils.isBlank(files[0]))) {

			addMessage("files", "   No ligand data file selected.\n");
			addFix("files", "   Please specify ligand data file(s) to use.");

			valid = false;
			return;

		}

		List<String> newFiles = new LinkedList<String>();

		boolean parsingSuccessful = true;

		// just connect to the ligand library. if that's not possible, throw
		// error
		try {
			getRemoteLigandFiles();
		} catch (RemoteFileSystemException e) {
			myLogger.error(e);
			msg.append("   Could not connect to common library location: "
					+ e.getLocalizedMessage() + "\n");
			fix.append("   Please contact a BeSTGRID support.");
			parsingSuccessful = false;
			valid = false;
			return;
		}

		for (String file : files) {

			msg.append("  Parsing library: " + FilenameUtils.getName(file)
					+ "\n");

			try {
				// checking common library for library file
				if (getRemoteLigandFiles()
						.contains(FilenameUtils.getName(file))) {
					newFiles.add(VS_LIBRARY_LOCAL_PATH
							+ FilenameUtils.getName(file));
					msg.append("     -> Found in common library location, using remote library file.\n");
				} else {
					msg.append("     -> Not found in common librarying. Checking alternative paths...\n");
					boolean isLocalFile = false;

					if (FileManager.isLocal(file)) {
						try {
							final File localFile = new File(new URI(
									FileManager.ensureUriFormat(file)));
							if (localFile.exists() && localFile.isFile()) {
								msg.append("     -> File "
										+ file
										+ " exists on local filesystem. File will be uploaded when job is created.\n");
								addFileToStageIn(file);
								newFiles.add(FilenameUtils.getName(file));
								isLocalFile = true;

							} else {

								if (localFile.getName().equals(file)) {
									// means it can also be remote file in
									// remote working
									// dir...
									if (StringUtils.isNotBlank(getWorkingDir())) {
										file = getWorkingDir() + "/" + file;
										isLocalFile = false;
									} else {
										isLocalFile = true;
									}
								} else {
									isLocalFile = true;
								}
								if (isLocalFile) {
									msg.append("     -> File "
											+ file
											+ " does not exist on local filesystem.\n");
									fix.append("Please check ligand data file path: "
											+ file);
									parsingSuccessful = false;
								}
							}

						} catch (URISyntaxException e) {
							msg.append("     -> Could not parse url for file: "
									+ file + "\n");
							parsingSuccessful = false;
							fix.append("   Please check url: " + file);
							isLocalFile = true;
						}
					}

					if (!isLocalFile) {
						if (getFileManager().isFile(file)) {
							msg.append("     -> File "
									+ file
									+ " exists on remote filesystem. File will be copied to job directory when job is created.\n");
							addFileToStageIn(file);
							newFiles.add(FilenameUtils.getName(file));
						} else {
							msg.append("     -> File "
									+ file
									+ " either does not exists or could not be accessed.\n");
							parsingSuccessful = false;
							fix.append("Please change path of make sure file exists: "
									+ file);
							parsingSuccessful = false;
						}

					}
				}

			} catch (RemoteFileSystemException e) {
				// should not happen
			}

		}

		if (!parsingSuccessful) {
			valid = false;
			msg.append("\nAt least one of the specified ligand data files is not valid or inaccessable.\n");
			getFilesToStageIn().clear();
		} else {
			// msg.append("\nAll specified ligand data files are valid and accessable.\n");
			valid = true;
			newLine = LIGAND_PARAMETER_NAME + " "
					+ StringUtils.join(newFiles, " ") + " "
					+ getLigandDockingAmount();
			// getFilesToStageIn().addAll(newFiles);
		}

		addMessage("files", msg.toString());
		addFix("files", fix.toString());

	}

	@Override
	protected void setNewConfigValue(String value) {

		int amount = getLigandDockingAmount(value);
		String[] files = getLigandFiles(value);
		setLigandDockingAmount(amount);
		setLigandFiles(files);
	}

}
