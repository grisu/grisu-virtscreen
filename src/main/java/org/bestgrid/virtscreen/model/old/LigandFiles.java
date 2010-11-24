package org.bestgrid.virtscreen.model.old;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.vpac.grisu.X;
import org.vpac.grisu.control.ServiceInterface;
import org.vpac.grisu.control.exceptions.RemoteFileSystemException;
import org.vpac.grisu.model.FileManager;
import org.vpac.grisu.model.GrisuRegistryManager;

public class LigandFiles {

	public static final String VS_LIBRARY_FILES_URL = "gsiftp://ng2.auckland.ac.nz/home/grid-vs/libraries/";
	public static final String VS_LIBRARY_LOCAL_PATH = "/home/grid-vs/libraries/";

	private final ServiceInterface si;
	private final FileManager fm;
	private String[] origFiles;
	private String[] remoteFiles;
	private String[] remoteUrls;
	private boolean valid = false;

	public LigandFiles(ServiceInterface si) {
		this.si = si;
		this.fm = GrisuRegistryManager.getDefault(si).getFileManager();
	}

	public String[] getRemotePaths() {
		X.p("REMOTE PATH: " + StringUtils.join(this.remoteFiles));
		return this.remoteFiles;
	}

	public String[] getRemoteUrls() {

		X.p("FILES OUTPUT: " + StringUtils.join(this.remoteUrls, ","));
		return this.remoteUrls;
	}

	/**
	 * 
	 * @return
	 */
	public String[] getStageInLigands() {
		return null;
	}

	public boolean isValid() {
		return this.valid;
	}

	public void setFiles(String[] files) {

		X.p("FILES INPUT: " + StringUtils.join(files, ","));

		this.origFiles = files;
		this.remoteFiles = new String[files.length];
		this.remoteUrls = new String[files.length];

		for (int i = 0; i < files.length; i++) {

			this.remoteUrls[i] = VS_LIBRARY_FILES_URL
					+ FilenameUtils.getName(files[i]);
			this.remoteFiles[i] = VS_LIBRARY_LOCAL_PATH
					+ FilenameUtils.getName(files[i]);
		}
		validateFiles();
	}

	private void validateFiles() {

		valid = true;
		for (int i = 0; i < origFiles.length; i++) {
			try {
				if (!fm.fileExists(this.remoteUrls[i])) {
					this.valid = false;
					break;
				}
			} catch (RemoteFileSystemException e) {
				this.valid = false;
				break;
			}
		}
	}

}
