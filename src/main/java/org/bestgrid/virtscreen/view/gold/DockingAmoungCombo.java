package org.bestgrid.virtscreen.view.gold;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.bestgrid.virtscreen.model.gold.GoldConfFile;
import org.vpac.grisu.frontend.view.swing.jobcreation.widgets.AbstractWidget;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class DockingAmoungCombo extends AbstractWidget {
	private JSpinner spinner;
	private final SpinnerNumberModel dockModel = new SpinnerNumberModel(0, 0,
			1000, 1);
	private GoldConfFile confFile = null;

	public DockingAmoungCombo() {
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(36dlu;default):grow"),
				FormFactory.RELATED_GAP_COLSPEC, }, new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC, }));
		add(getSpinner(), "2, 2, fill, default");
		setTitle("# of dockings");
	}

	public int getDockingAmount() {
		return dockModel.getNumber().intValue();
	}

	private JSpinner getSpinner() {
		if (spinner == null) {
			spinner = new JSpinner(dockModel);
			spinner.addChangeListener(new ChangeListener() {

				public void stateChanged(ChangeEvent e) {

					if (confFile == null) {
						return;
					}

					confFile.setLigandDockingAmount((Integer) spinner
							.getValue());

				}
			});
		}
		return spinner;
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setGoldConfFile(GoldConfFile confFile) {
		dockModel.setValue(confFile.getLigandDockingAmount());
		this.confFile = confFile;
	}

	@Override
	public void setValue(String value) {
		// TODO Auto-generated method stub

	}
}
