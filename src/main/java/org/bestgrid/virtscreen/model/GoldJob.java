package org.bestgrid.virtscreen.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.bestgrid.virtscreen.model.GoldConfFile.PARAMETER;
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

	private JobObject job;

	private final GoldConfFile goldConfFile;
	private final ServiceInterface si;

	private String molFile1;
	private String molFile2;

	private int cpus = 1;
	private int walltimeInSeconds = 600;

	public GoldJob(ServiceInterface si, String confFileTemplate)
			throws FileTransactionException {
		this.si = si;
		this.goldConfFile = new GoldConfFile(this.si, confFileTemplate);
		createJobObject();
	}

	private void setParameter(GoldConfFile.PARAMETER key, String value) {
		this.goldConfFile.setParameter(key, value);
	}

	public void setOptionalParamsFile(String fileUrl) {
		this.goldConfFile.setParameter(PARAMETER.score_param_file, fileUrl);
		this.job.addInputFileUrl(fileUrl);
		// TODO update conf file
	}

	public void setWalltime(int inSeconds) {
		this.walltimeInSeconds = inSeconds;
	}

	public void setCpus(int cpus) {
		this.cpus = cpus;
	}

	private JobObject createJobObject() {
		job = new JobObject(si);
		return job;
	}

	public void createAndSubmitJob() throws JobSubmissionException,
			JobPropertiesException {

		job.setTimestampJobname(goldConfFile.getName());
		job.addInputFileUrl(VIRTSCREEN_JOB_CONTROL_SCRIPT.getPath());
		job.addInputFileUrl(VIRTSCREEN_HELPER_PY_SCRIPT.getPath());
		job.addInputFileUrl(goldConfFile.getConfFile().getPath());
		job.setApplication(Constants.GENERIC_APPLICATION_NAME);
		job.setSubmissionLocation("route@er171.ceres.auckland.ac.nz:ng2.auckland.ac.nz");

		job.setCommandline("sh script.sh " + this.goldConfFile.getName());
		job.setCpus(this.cpus);
		job.setHostCount(1);
		job.setForce_single(true);
		job.setWalltimeInSeconds(walltimeInSeconds);

		job.createJob("/ARCS/BeSTGRID/VS_Discovery");
		// job.createJob("/ARCS/BeSTGRID");

		this.goldConfFile.updateConfFile();

		try {
			job.submitJob();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		job = null;

	}
}
