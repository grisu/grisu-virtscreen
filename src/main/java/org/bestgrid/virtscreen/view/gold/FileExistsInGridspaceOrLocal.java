package org.bestgrid.virtscreen.view.gold;

import grisu.control.ServiceInterface;
import grisu.model.FileManager;
import grisu.model.GrisuRegistryManager;

import org.netbeans.validation.api.Problems;
import org.netbeans.validation.api.Validator;

public class FileExistsInGridspaceOrLocal implements Validator<String> {

	private final ServiceInterface si;
	private final FileManager fm;

	public FileExistsInGridspaceOrLocal(ServiceInterface si) {
		this.si = si;
		this.fm = GrisuRegistryManager.getDefault(si).getFileManager();
	}

	public boolean validate(Problems arg0, String arg1, String arg2) {

		if (fm.isFolder(arg2)) {

			final String problem = "Specified url does not exist or is not a file";
			arg0.add(problem);

			return false;
		}
		return true;

	}
}
