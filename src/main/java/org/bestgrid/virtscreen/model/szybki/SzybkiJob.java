package org.bestgrid.virtscreen.model.szybki;

import grisu.X;
import grisu.control.ServiceInterface;
import grisu.control.exceptions.JobPropertiesException;
import grisu.control.exceptions.JobSubmissionException;
import grisu.frontend.control.jobMonitoring.RunningJobManager;
import grisu.frontend.control.login.LoginManager;
import grisu.frontend.model.job.JobObject;
import grisu.model.dto.GridFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.bestgrid.virtscreen.control.VirtScreenEnvironment;
import org.bestgrid.virtscreen.model.szybki.SzybkiParameter.TYPE;

public class SzybkiJob {

	static final Logger myLogger = Logger.getLogger(SzybkiJob.class
			.getName());

	public static final File SZYBKI_JOB_CONTROL_SCRIPT = new File(
			VirtScreenEnvironment.VIRTSCREEN_PLUGIN_DIR, "szybki.sh");

	public static final File SZYBKI_HELPER_PY_SCRIPT = new File(
			VirtScreenEnvironment.VIRTSCREEN_PLUGIN_DIR, "szybki.py");

	public static void main(String[] args) throws Exception {

		final ServiceInterface si = LoginManager.loginCommandline("Local");

		final SzybkiInputFile input = new SzybkiInputFile(si);

		input.setInputFile("/home/markus/Desktop/jack/szybki/example.param");

		final SzybkiJob job = new SzybkiJob(si, input);

		job.createAndSubmitJob();

	}

	private final SzybkiInputFile szybkiInputFile;

	private JobObject job;

	private final ServiceInterface si;

	private final int cpus = 1;
	private final int walltimeInSeconds = 600;

	public SzybkiJob(ServiceInterface si, SzybkiInputFile szybkiInputFile) {
		this.si = si;
		this.szybkiInputFile = szybkiInputFile;

		job = new JobObject(si);
	}

	public void createAndSubmitJob() throws JobSubmissionException,
	JobPropertiesException {

		job.setTimestampJobname(FilenameUtils.getBaseName(szybkiInputFile
				.getName()));
		job.setApplication("szybki");
		job.setApplicationVersion("1.3.4");
		// job.setSubmissionLocation("er171.ceres.auckland.ac.nz:ng2.auckland.ac.nz");

		final String commandline = "sh szybki.sh "
				+ this.szybkiInputFile.getName();

		X.p("Commandline: " + commandline);

		job.setCommandline(commandline);
		job.setCpus(this.cpus);
		job.setMemory(2L * 2147483648L * new Long(this.cpus));
		// job.setHostCount(1);
		job.setForce_single(true);
		job.setWalltimeInSeconds(walltimeInSeconds);

		job.addInputFileUrl(SZYBKI_JOB_CONTROL_SCRIPT.getPath());
		job.addInputFileUrl(SZYBKI_HELPER_PY_SCRIPT.getPath());
		job.addInputFileUrl(szybkiInputFile.getJobConfFile().getPath());

		for (final SzybkiParameter p : szybkiInputFile.getParameters(TYPE.FILE)) {

			if (p.isEnabled()) {
				final GridFile f = (GridFile) p.getParameterValue().getValue();
				X.p("Adding file: " + f.getUrl());
				job.addInputFileUrl(f.getUrl());
			}

		}

		RunningJobManager.getDefault(si).createJob(job,
				// "/ARCS/BeSTGRID/Drug_discovery/Local");
				"/ARCS/BeSTGRID");

		final Map<String, String> additionalJobProperties = new HashMap<String, String>();

		try {
			job.submitJob(additionalJobProperties);
		} catch (final InterruptedException e) {
			myLogger.error(e.getLocalizedMessage(), e);
		}

		job = null;

	}

	public JobObject getJob() {
		return job;
	}
}
