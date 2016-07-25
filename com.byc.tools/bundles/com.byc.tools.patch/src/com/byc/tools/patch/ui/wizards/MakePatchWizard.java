package com.byc.tools.patch.ui.wizards;

import org.eclipse.jface.wizard.Wizard;

import com.byc.tools.patch.model.PatchInfo;
import com.byc.tools.patch.services.ChangeVersionService;
import com.byc.tools.patch.services.impls.ChangeVersionServiceImpl;

/**
 * 
 * @author ycbai
 *
 */
public class MakePatchWizard extends Wizard {

	private PatchInfo patchInfo;

	public MakePatchWizard() {
		super();
		patchInfo = new PatchInfo();
		setNeedsProgressMonitor(true);
	}

	@Override
	public String getWindowTitle() {
		return "Make Patch";
	}

	@Override
	public void addPages() {
		ChangeVersionPage changeVersionPage = new ChangeVersionPage(patchInfo);
		addPage(changeVersionPage);
	}

	@Override
	public boolean performFinish() {
		ChangeVersionService service = new ChangeVersionServiceImpl();
		return service.doChangeVersion(patchInfo);
	}

}
