package com.byc.tools.patch.ui.wizards;

import org.eclipse.jface.wizard.WizardPage;

import com.byc.tools.patch.model.PatchInfo;

public abstract class AbstractMakePatchPage extends WizardPage {

	protected PatchInfo patchInfo;

	protected AbstractMakePatchPage(String pageName, PatchInfo patchInfo) {
		super(pageName);
		this.patchInfo = patchInfo;
	}

}
