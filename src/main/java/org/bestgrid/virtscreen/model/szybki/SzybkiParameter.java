package org.bestgrid.virtscreen.model.szybki;

import grisu.X;
import grisu.frontend.view.swing.jobcreation.widgets.AbstractInputGridFile;
import grisu.model.dto.GridFile;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.Map;

import org.bestgrid.virtscreen.view.GrisuVirtScreen;

import com.google.common.collect.ImmutableMap;

public class SzybkiParameter implements SzybkiConfigLine {

	public enum PARAM {

		pvmconf(TYPE.FILE, getDefaultMap("conf")),
		complex(TYPE.UNDEF),
		fix_file(TYPE.UNDEF),
		heavy_rms(TYPE.BOOLEAN),
		ligands(TYPE.FILE, getDefaultMap("mol2")),
		loadPG(TYPE.UNDEF),
		log(TYPE.FILE),
		out(TYPE.FILE),
		out_protein(TYPE.FILE),
		prefix(TYPE.FILE),
		protein(TYPE.FILE),
		report(TYPE.BOOLEAN, "true", false),
		reportFile(TYPE.UNDEF),
		savePG(TYPE.UNDEF),
		sdtag(TYPE.UNDEF),
		silent(TYPE.BOOLEAN, "false", false),
		verbose(TYPE.BOOLEAN, "true", false),

		MMFF94S(TYPE.BOOLEAN),
		am1bcc(TYPE.BOOLEAN, "false"),
		exact_vdw(TYPE.BOOLEAN, "true"),
		harm_constr1(TYPE.UNDEF),
		harm_constr2(TYPE.UNDEF),
		harm_smarts(TYPE.UNDEF),
		mod_vdw(TYPE.UNDEF),
		mol2charges(TYPE.UNDEF),
		neglect_frozen(TYPE.BOOLEAN, "true"),
		noCoulomb(TYPE.BOOLEAN, "false"),
		prot_dielectric(TYPE.DOUBLE),
		protein_elec(TYPE.STRING, "PB"),
		protein_vdw(TYPE.DOUBLE),
		shefA(TYPE.UNDEF),
		shefB(TYPE.UNDEF),
		sheffield(TYPE.UNDEF),
		solv_dielectric(TYPE.UNDEF),
		solventCA(TYPE.UNDEF),
		solventPB(TYPE.UNDEF),
		strict(TYPE.UNDEF),

		conj(TYPE.BOOLEAN, "true"),
		fix_smarts(TYPE.UNDEF),
		grad_conv(TYPE.DOUBLE, "0.05"),
		largest_part(TYPE.BOOLEAN, "false"),
		max_iter(TYPE.INTEGER, "100"),
		no_opt(TYPE.BOOLEAN, "false"),
		opt_cart(TYPE.BOOLEAN, "true"),
		opt_solid(TYPE.BOOLEAN, "false"),
		opt_torsions(TYPE.BOOLEAN, "false"),
		polarH(TYPE.DOUBLE, "8.00"),
		residue(TYPE.UNDEF),
		sideC(TYPE.UNDEF),
		strip_water(TYPE.BOOLEAN, "false"),

		ent151(TYPE.UNDEF),
		entropy(TYPE.UNDEF),
		rws(TYPE.UNDEF),
		sfp(TYPE.UNDEF),
		t(TYPE.UNDEF);

		public static PARAM fromString(String paramName) {
			try {
				return PARAM.valueOf(paramName);
			} catch (IllegalArgumentException e) {
				return null;
			}

		}

		TYPE type;
		boolean userDefined;
		String defaultValue;
		public Map<String, String> config;

		PARAM(TYPE type) {
			this.type = type;
			this.defaultValue = null;
			this.userDefined = true;
		}

		PARAM(TYPE type, Map config) {
			this.type = type;
			this.defaultValue = null;
			this.userDefined = true;
			this.config = config;
		}

		PARAM(TYPE type, String defaultValue) {
			this.type = type;
			this.defaultValue = defaultValue;
			this.userDefined = true;
		}

		PARAM(TYPE type, String defaultValue, boolean userDefined) {
			this.type = type;
			this.defaultValue = defaultValue;
			this.userDefined = userDefined;
		}

		PARAM(TYPE type, String defaultValue, boolean userDefined, Map config) {
			this.type = type;
			this.defaultValue = defaultValue;
			this.userDefined = userDefined;
			this.config = config;
		}

	}

	public enum TYPE {
		FILE(GridFile.class),
		INTEGER(Integer.class),
		DOUBLE(Double.class),
		BOOLEAN(Boolean.class),
		STRING(String.class),
		UNDEF(Object.class);

		Class valueClass;

		TYPE(Class valueClass) {
			this.valueClass = valueClass;
		}
	}

	public static List<String> createSyzbkiParamFileContent(
			List<SzybkiParameter> parameters) {
		return null;
	}

	private static ImmutableMap getDefaultMap(String extension) {
		return ImmutableMap.of(AbstractInputGridFile.EXTENSIONS_TO_DISPLAY,
				extension, AbstractInputGridFile.FOLDER_SELECTABLE, "false",
				AbstractInputGridFile.ROOTS, GrisuVirtScreen.VIRTSCREEN_ROOTS);
	}

	public static void main(String[] args) {

		System.out.println(PARAM.fromString("dconj"));

	}

	private final String line;
	private final PARAM parameterName;
	private ParameterValue parameterValue;

	private boolean isEnabled = false;

	private String comment;

	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	public SzybkiParameter(String line, PARAM parameterName,
			ParameterValue parameterValue) {
		this(line, parameterName, parameterValue, null);
	}

	public SzybkiParameter(String line, PARAM parameterName,
			ParameterValue parameterValue, String optionalComment) {
		this.line = line;
		this.parameterName = parameterName;
		this.parameterValue = parameterValue;
		this.comment = optionalComment;

		if (getType() == TYPE.UNDEF) {
			this.isEnabled = false;
		} else {
			this.isEnabled = true;
		}

	}

	public void addPropertyChangeListener(PropertyChangeListener l) {
		pcs.addPropertyChangeListener(l);
	}

	public String getComment() {
		return this.comment;
	}

	public String getLine() {
		StringBuffer l = new StringBuffer();
		if (!isEnabled) {
			l.append("# ");
		}

		l.append("-" + getParameterName());
		l.append(" " + getParameterValue().getStringValue());

		return l.toString();
	}

	public PARAM getParameterName() {
		return this.parameterName;
	}

	public ParameterValue getParameterValue() {
		return this.parameterValue;
	}

	public TYPE getType() {
		return this.parameterName.type;
	}

	public boolean isEnabled() {

		return isEnabled;

	}

	public void removePropertyChangeListener(PropertyChangeListener l) {
		pcs.removePropertyChangeListener(l);
	}

	public void setComment(String c) {
		this.comment = c;
	}

	public void setEnabled(boolean e) {
		boolean old = this.isEnabled;
		this.isEnabled = e;
		pcs.firePropertyChange("enabled", old, this.isEnabled);
	}

	public void setParameterValue(ParameterValue v) {
		X.p("Sdfsdfdsfsdf");
		Object oldValue = v;
		this.parameterValue = v;
		pcs.firePropertyChange("parameterValue", oldValue, this.parameterValue);
	}

}
