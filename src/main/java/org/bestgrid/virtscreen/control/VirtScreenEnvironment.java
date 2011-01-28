package org.bestgrid.virtscreen.control;

import grisu.settings.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.bestgrid.virtscreen.model.gold.GoldJob;
import org.bestgrid.virtscreen.model.szybki.SzybkiJob;

public class VirtScreenEnvironment {

	public static final File VIRTSCREEN_PLUGIN_DIR = new File(
			Environment.getGrisuClientDirectory(), "virtscreen");

	public static void init() {
		if (!VIRTSCREEN_PLUGIN_DIR.exists()) {
			VIRTSCREEN_PLUGIN_DIR.mkdirs();
		}

		try {
			InputStream in = GoldJob.class.getResourceAsStream("/gold.sh");
			IOUtils.copy(in, new FileOutputStream(
					GoldJob.GOLD_JOB_CONTROL_SCRIPT));
			in.close();

			in = GoldJob.class.getResourceAsStream("/gold.py");
			IOUtils.copy(in,
					new FileOutputStream(GoldJob.GOLD_HELPER_PY_SCRIPT));
			in.close();

			in = GoldJob.class.getResourceAsStream("/szybki.sh");
			IOUtils.copy(in, new FileOutputStream(
					SzybkiJob.SZYBKI_JOB_CONTROL_SCRIPT));
			in.close();

			in = GoldJob.class.getResourceAsStream("/szybki.py");
			IOUtils.copy(in, new FileOutputStream(
					SzybkiJob.SZYBKI_HELPER_PY_SCRIPT));
			in.close();
		} catch (Exception e) {

		}
	}

}
