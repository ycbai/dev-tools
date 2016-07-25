package com.byc.tools.patch.ui.wizards;

import org.eclipse.jface.wizard.Wizard;

import com.byc.tools.patch.services.ChangeVersionService;
import com.byc.tools.patch.services.impls.ChangeVersionServiceImpl;

/**
 * 
 * @author ycbai
 *
 */
public class MakePatchWizard extends Wizard {

	protected ChangeVersionPage changeVersionPage;

	public MakePatchWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	@Override
	public String getWindowTitle() {
		return "Make Patch";
	}

	@Override
	public void addPages() {
		changeVersionPage = new ChangeVersionPage();
		addPage(changeVersionPage);
	}

	@Override
	public boolean performFinish() {
		String newVersion = changeVersionPage.getVersion();
		ChangeVersionService service = new ChangeVersionServiceImpl();
		service.doChangeVersion(newVersion);
		return true;
	}

}
