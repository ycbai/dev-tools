package com.byc.tools.patch.ui.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Control;

import com.byc.tools.patch.model.PatchInfo;

public abstract class AbstractMakePatchPage extends WizardPage {

	protected PatchInfo patchInfo;

	protected AbstractMakePatchPage(String pageName, PatchInfo patchInfo) {
		super(pageName);
		this.patchInfo = patchInfo;
	}

	protected void hideControl(Control control, boolean hide) {
        Object layoutData = control.getLayoutData();
        if (layoutData instanceof GridData) {
            GridData data = (GridData) layoutData;
            data.exclude = hide;
            control.setLayoutData(data);
            control.setVisible(!hide);
            if (control.getParent() != null) {
                control.getParent().layout();
            }
        }
    }

}
