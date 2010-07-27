package org.bestgrid.virtscreen.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.vpac.grisu.control.ServiceInterface;
import org.vpac.grisu.control.exceptions.JobPropertiesException;
import org.vpac.grisu.control.exceptions.JobSubmissionException;
import org.vpac.grisu.frontend.control.clientexceptions.FileTransactionException;
import org.vpac.grisu.frontend.model.job.JobObject;
import org.vpac.grisu.settings.Environment;

public class GoldJob {

	public static final File VIRTSCREEN_PLUGIN_DIR = new File(
			Environment.getGrisuClientDirectory(), "virtscreen");
	public static final File VIRTSCREEN_JOB_CONTROL_SCRIPT = new File(
			VIRTSCREEN_PLUGIN_DIR, "gold.sh");
	public static final File VIRTSCREEN_HELPER_PY_SCRIPT = new File(
			VIRTSCREEN_PLUGIN_DIR, "gold.py");

	static {
		if (!VIRTSCREEN_PLUGIN_DIR.exists()) {
			VIRTSCREEN_PLUGIN_DIR.mkdirs();
		}

		try {
			InputStream in = GoldJob.class.getResourceAsStream("/gold.sh");

			IOUtils.copy(in,
					new FileOutputStream(VIRTSCREEN_JOB_CONTROL_SCRIPT));

			in = GoldJob.class.getResourceAsStream("/gold.py");

			IOUtils.copy(in, new FileOutputStream(VIRTSCREEN_HELPER_PY_SCRIPT));
		} catch (Exception e) {

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
		job = new JobObject(si);

	}
	
	public void setCustomLibraryFiles(String[] files) {
		this.goldConfFile.setCostumLigandDataFiles(files);
	}
	
	public void setCustomDockingAmount(int amount){
		this.goldConfFile.setCostumLigandAmount(amount);
	}

	private void setParameter(GoldConfFile.PARAMETER key, String value) {
		this.goldConfFile.setParameter(key, value);
	}

	public void setWalltime(int inSeconds) {
		this.walltimeInSeconds = inSeconds;
	}

	public void setCpus(int cpus) {
		this.cpus = cpus;
	}

	public JobObject getJobObject() {
		return this.job;
	}

	public void createAndSubmitJob() throws JobSubmissionException,
			JobPropertiesException {

		job.setTimestampJobname(FilenameUtils.getBaseName(goldConfFile
				.getName()));
		job.setApplication("Gold");
		job.setApplicationVersion("1.3.2");
		job.setSubmissionLocation("gold@er171.ceres.auckland.ac.nz:ng2.auckland.ac.nz");

		job.setCommandline("sh gold.sh " + this.goldConfFile.getName());
		job.setCpus(this.cpus);
		job.setHostCount(1);
		job.setForce_single(true);
		job.setWalltimeInSeconds(walltimeInSeconds);

		job.addInputFileUrl(VIRTSCREEN_JOB_CONTROL_SCRIPT.getPath());
		job.addInputFileUrl(VIRTSCREEN_HELPER_PY_SCRIPT.getPath());
		job.addInputFileUrl(goldConfFile.getConfFile().getPath());

		// for (String url : goldConfFile.getLigandUrls()) {
		// System.out.println(url);
		// job.addInputFileUrl(url);
		// }

		for (String url : goldConfFile.getFilesToStageIn()) {
			job.addInputFileUrl(url);
		}

		// job.createJob("/ARCS/BeSTGRID/Drug_discovery");
		job.createJob("/ARCS/BeSTGRID/Drug_discovery/Local");
		// job.createJob("/ARCS/BeSTGRID/UoA/LocalUsers");
		// job.createJob("/ARCS/BeSTGRID");

		// String resultsDir = "./Results";
		// String concOut = "./Results/test.sdf";
		// setParameter(GoldConfFile.PARAMETER.directory, resultsDir);
		// setParameter(GoldConfFile.PARAMETER.concatenated_output, concOut);

		// need to do this after we get the files to stage in, otherwise the new
		// paths would be already there
		this.goldConfFile.updateConfFile();

		try {
			job.submitJob();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		job = null;

	}
}
