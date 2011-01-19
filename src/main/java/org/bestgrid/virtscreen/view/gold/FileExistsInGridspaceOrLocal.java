package org.bestgrid.virtscreen.view.gold;

import org.netbeans.validation.api.Problems;
import org.netbeans.validation.api.Validator;
import org.vpac.grisu.control.ServiceInterface;
import org.vpac.grisu.model.FileManager;
import org.vpac.grisu.model.GrisuRegistryManager;

public class FileExistsInGridspaceOrLocal implements Validator<String> {

	private final ServiceInterface si;
	private final FileManager fm;

	public FileExistsInGridspaceOrLocal(ServiceInterface si) {
		this.si = si;
		this.fm = GrisuRegistryManager.getDefault(si).getFileManager();
	}

	public boolean validate(Problems arg0, String arg1, String arg2) {

		if (fm.isFolder(arg2)) {

			String problem = "Specified url does not exist or is not a file";
			arg0.add(problem);

			return false;
		}
		return true;

	}
}
