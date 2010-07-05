package org.bestgrid.virtscreen.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.vpac.grisu.control.ServiceInterface;
import org.vpac.grisu.control.exceptions.JobPropertiesException;
import org.vpac.grisu.control.exceptions.JobSubmissionException;
import org.vpac.grisu.frontend.control.clientexceptions.FileTransactionException;
import org.vpac.grisu.frontend.model.job.JobObject;
import org.vpac.grisu.settings.Environment;

import au.org.arcs.jcommons.constants.Constants;

public class GoldJob {

	public static final File VIRTSCREEN_PLUGIN_DIR = new File(
			Environment.getGrisuClientDirectory(), "virtscreen");
	public static final File VIRTSCREEN_JOB_CONTROL_SCRIPT = new File(
			VIRTSCREEN_PLUGIN_DIR, "script.sh");
	public static final File VIRTSCREEN_HELPER_PY_SCRIPT = new File(
			VIRTSCREEN_PLUGIN_DIR, "gold.py");

	static {
		if (!VIRTSCREEN_PLUGIN_DIR.exists()) {
			VIRTSCREEN_PLUGIN_DIR.mkdirs();
		}

		try {
			InputStream in = GoldJob.class.getResourceAsStream("/script.sh");

			IOUtils.copy(in,
					new FileOutputStream(VIRTSCREEN_JOB_CONTROL_SCRIPT));

			in = GoldJob.class.getResourceAsStream("/gold.py");

			IOUtils.copy(in, new FileOutputStream(VIRTSCREEN_HELPER_PY_SCRIPT));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	private GoldConfFile goldConfFile;
	private final ServiceInterface si;

	private String molFile1;
	private String molFile2;

	public GoldJob(ServiceInterface si) {
		this.si = si;
	}

	public void setConfFileTemplate(String url) throws FileTransactionException {
		this.goldConfFile = new GoldConfFile(this.si, url);
	}

	private void setJobDirectory(String jobDir) {

		this.goldConfFile.setProperty(GoldConfFile.DIRECTORY, "./Results");
		this.goldConfFile.setProperty(GoldConfFile.CONCATENATED_OUTPUT,
				"./Results/test.sdf");

	}

	public void setInputFiles(String proteinPath, String ligandPath,
			String paramsPath) {

		this.goldConfFile.setProperty(GoldConfFile.PROTEIN_DATA_FILE,
				proteinPath);
		this.goldConfFile.setLigand_data_file(ligandPath);
		this.goldConfFile
				.setProperty(GoldConfFile.SCORE_PARAM_FILE, paramsPath);

	}

	public void submit() throws JobSubmissionException, JobPropertiesException {

		JobObject job = new JobObject(si);
		job.setTimestampJobname(goldConfFile.getName());
		job.addInputFileUrl(VIRTSCREEN_JOB_CONTROL_SCRIPT.getPath());
		job.addInputFileUrl(VIRTSCREEN_HELPER_PY_SCRIPT.getPath());
		job.addInputFileUrl(goldConfFile.getConfFile().getPath());
		job.setApplication(Constants.GENERIC_APPLICATION_NAME);
		job.setSubmissionLocation("route@er171.ceres.auckland.ac.nz:ng2.auckland.ac.nz");

		job.setCommandline("sh script.sh");
		job.setCpus(8);
		job.setHostCount(1);
		job.setForce_single(true);
		job.setWalltimeInSeconds(5000 * 60);

		job.createJob("/ARCS/BeSTGRID");

		try {
			job.submitJob();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
}
