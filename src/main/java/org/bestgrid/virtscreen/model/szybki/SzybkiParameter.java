package org.bestgrid.virtscreen.model.szybki;

import grisu.X;
import grisu.frontend.view.swing.jobcreation.widgets.AbstractInputGridFile;
import grisu.model.dto.GridFile;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.Map;

import org.bestgrid.virtscreen.model.szybki.ParameterValue.FILTER;
import org.bestgrid.virtscreen.view.GrisuVirtScreen;

import com.google.common.collect.ImmutableMap;

public class SzybkiParameter implements SzybkiConfigLine {

	public enum PARAM {

		pvmconf(TYPE.FILE, getDefaultMap("conf")),
		complex(TYPE.UNDEF, false),
		fix_file(TYPE.UNDEF, false),
		heavy_rms(TYPE.BOOLEAN),
		ligands(TYPE.FILE, getDefaultMap("mol2")),
		loadPG(TYPE.UNDEF, false),
		log(TYPE.STRING, ParameterValue.FILTER.BASENAME_FILTER),
		out(TYPE.STRING, ParameterValue.FILTER.BASENAME_FILTER),
		out_protein(TYPE.STRING, ParameterValue.FILTER.BASENAME_FILTER),
		prefix(TYPE.FILE),
		protein(TYPE.FILE),
		report(TYPE.BOOLEAN, "true", false, true),
		reportFile(TYPE.UNDEF, false),
		savePG(TYPE.UNDEF, false),
		sdtag(TYPE.UNDEF, false),
		silent(TYPE.BOOLEAN, "false", false, true),
		verbose(TYPE.BOOLEAN, "true", false, true),

		MMFF94S(TYPE.BOOLEAN),
		am1bcc(TYPE.BOOLEAN, "false"),
		exact_vdw(TYPE.BOOLEAN, "true"),
		harm_constr1(TYPE.DOUBLE, false),
		harm_constr2(TYPE.DOUBLE, false),
		harm_smarts(TYPE.UNDEF, false),
		mod_vdw(TYPE.BOOLEAN, false),
		mol2charges(TYPE.BOOLEAN, false),
		neglect_frozen(TYPE.BOOLEAN, "true"),
		noCoulomb(TYPE.BOOLEAN, "false"),
		prot_dielectric(TYPE.DOUBLE),
		protein_elec(TYPE.STRING, "PB"),
		protein_vdw(TYPE.DOUBLE),
		shefA(TYPE.UNDEF, false),
		shefB(TYPE.UNDEF, false),
		sheffield(TYPE.BOOLEAN, false),
		solv_dielectric(TYPE.UNDEF, false),
		solventCA(TYPE.BOOLEAN, false),
		solventPB(TYPE.BOOLEAN, false),
		strict(TYPE.BOOLEAN, false),

		conj(TYPE.BOOLEAN, "true"),
		fix_smarts(TYPE.UNDEF, false),
		grad_conv(TYPE.DOUBLE, "0.05"),
		largest_part(TYPE.BOOLEAN, "false"),
		max_iter(TYPE.INTEGER, "100"),
		no_opt(TYPE.BOOLEAN, "false"),
		opt_cart(TYPE.BOOLEAN, "true"),
		opt_solid(TYPE.BOOLEAN, "false"),
		opt_torsions(TYPE.BOOLEAN, "false"),
		polarH(TYPE.DOUBLE, "8.00"),
		residue(TYPE.UNDEF, false),
		sideC(TYPE.UNDEF, false),
		strip_water(TYPE.BOOLEAN, "false"),

		ent151(TYPE.BOOLEAN, false),
		entropy(TYPE.STRING, "None", true, false),
		rws(TYPE.BOOLEAN, false),
		sfp(TYPE.DOUBLE, false),
		t(TYPE.DOUBLE, false);

		public static PARAM fromString(String paramName) {
			try {
				return PARAM.valueOf(paramName);
			} catch (final IllegalArgumentException e) {
				return null;
			}

		}

		TYPE type;
		boolean userDefined;
		String defaultValue;
		public Map<String, String> config;
		public FILTER[] filters;
		public boolean defaultEnabled;

		PARAM(TYPE type) {
			this.type = type;
			this.defaultValue = null;
			this.userDefined = true;
			this.filters = null;
			this.defaultEnabled = true;
		}

		PARAM(TYPE type, boolean defaultEnabled) {
			this.type = type;
			this.defaultValue = null;
			this.userDefined = true;
			this.filters = null;
			this.defaultEnabled = defaultEnabled;
		}

		PARAM(TYPE type, FILTER filter) {
			this.type = type;
			this.defaultValue = null;
			this.userDefined = true;
			this.filters = new FILTER[] { filter };
			this.defaultEnabled = true;
		}

		PARAM(TYPE type, FILTER[] filters) {
			this.type = type;
			this.defaultValue = null;
			this.userDefined = true;
			this.filters = filters;
			this.defaultEnabled = true;
		}

		PARAM(TYPE type, Map config) {
			this.type = type;
			this.defaultValue = null;
			this.userDefined = true;
			this.config = config;
			this.filters = null;
			this.defaultEnabled = true;
		}

		PARAM(TYPE type, String defaultValue) {
			this.type = type;
			this.defaultValue = defaultValue;
			this.userDefined = true;
			this.filters = null;
			this.defaultEnabled = true;
		}

		PARAM(TYPE type, String defaultValue, boolean userDefined,
				boolean defaultEnbaled) {
			this.type = type;
			this.defaultValue = defaultValue;
			this.userDefined = userDefined;
			this.filters = null;
			this.defaultEnabled = true;
		}

		PARAM(TYPE type, String defaultValue, boolean userDefined, Map config) {
			this.type = type;
			this.defaultValue = defaultValue;
			this.userDefined = userDefined;
			this.config = config;
			this.filters = null;
			this.defaultEnabled = true;
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

		this.isEnabled = parameterName.defaultEnabled;

	}

	public void addPropertyChangeListener(PropertyChangeListener l) {
		pcs.addPropertyChangeListener(l);
	}

	public String getComment() {
		return this.comment;
	}

	public String getLine() {
		final StringBuffer l = new StringBuffer();
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
		final boolean old = this.isEnabled;
		this.isEnabled = e;
		pcs.firePropertyChange("enabled", old, this.isEnabled);
	}

	public void setParameterValue(ParameterValue v) {
		X.p("Sdfsdfdsfsdf");
		final Object oldValue = v;
		this.parameterValue = v;
		pcs.firePropertyChange("parameterValue", oldValue, this.parameterValue);
	}

}
