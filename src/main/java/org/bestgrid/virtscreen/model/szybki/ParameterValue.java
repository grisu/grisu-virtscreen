package org.bestgrid.virtscreen.model.szybki;

import grisu.control.ServiceInterface;
import grisu.control.exceptions.RemoteFileSystemException;
import grisu.model.FileManager;
import grisu.model.GrisuRegistryManager;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.bestgrid.virtscreen.model.szybki.SzybkiParameter.PARAM;
import org.bestgrid.virtscreen.model.szybki.SzybkiParameter.TYPE;

public class ParameterValue {

	public enum FILTER {
		BASENAME_FILTER;
	}

	private String stringValue;

	private final PARAM param;

	private Object value;
	private Object old;

	private final ServiceInterface si;

	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	public ParameterValue(ServiceInterface si, PARAM p,
			String value) {
		this.si = si;
		this.param = p;

		String newValue = filter(value);
		setStringValue(newValue);

	}

	public void addPropertyChangeListener(PropertyChangeListener l) {
		pcs.addPropertyChangeListener(l);
	}

	private String filter(String value) {

		FILTER[] filters = this.param.filters;

		if (filters == null) {
			return value;
		}

		for (FILTER filter : filters) {
			switch (filter) {
			case BASENAME_FILTER:
				value = FileManager.getFilename(value);
				break;
			default:
				break;
			}
		}

		return value;
	}

	public PARAM getParameter() {
		return this.param;
	}

	public String getStringValue() {
		return stringValue;
	}

	public TYPE getType() {
		return param.type;
	}

	public Object getValue() {
		return value;
	}

	public void removePropertyChangeListener(PropertyChangeListener l) {
		pcs.removePropertyChangeListener(l);
	}

	public void setStringValue(String value) {

		switch (param.type) {
		case BOOLEAN:
			try {
				this.value = Boolean.parseBoolean(value);
			} catch (Exception e) {
				throw new IllegalArgumentException(
				"Value needs to be of type Boolean.");
			}
			this.stringValue = value;
			return;
		case STRING:
			this.value = value;
			this.stringValue = value;
			return;
		case INTEGER:
			try {
				this.value = Integer.parseInt(value);
			} catch (Exception e) {
				throw new IllegalArgumentException(
				"Value needs to be of type Integer.");
			}
			this.stringValue = value;
			return;
		case DOUBLE:
			try {
				this.value = Double.parseDouble(value);
			} catch (Exception e) {
				throw new IllegalArgumentException(
				"Value needs to be of type Double.");
			}
			this.stringValue = value;
			return;
		case FILE:
			try {
				this.value = GrisuRegistryManager.getDefault(si)
				.getFileManager().createGridFile(value);
				this.stringValue = value;
			} catch (RemoteFileSystemException e) {
				throw new IllegalArgumentException(e);
			}
			this.stringValue = value;
			return;
		case UNDEF:
			this.value = value;
			this.stringValue = value;
			return;
		default:
			this.value = value;
			this.stringValue = value;
		}

	}

	public void setValue(Object value) {

		old = this.value;

		if (value instanceof String) {
			setStringValue((String) value);
			pcs.firePropertyChange("value", old, this.value);
			return;
		}

		if (!getType().valueClass.isInstance(value)) {
			throw new IllegalArgumentException("Value needs to be of type "
					+ getType().valueClass.getName());
		}

		this.value = value;
		this.stringValue = value.toString();

		pcs.firePropertyChange("value", old, this.value);

	}

	@Override
	public String toString() {
		return getStringValue();
	}
}
