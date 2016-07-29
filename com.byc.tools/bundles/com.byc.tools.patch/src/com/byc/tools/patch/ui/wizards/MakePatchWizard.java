package com.byc.tools.patch.ui.wizards;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
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
		try {
			this.getContainer().run(true, true, new IRunnableWithProgress() {

				@Override
				public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					monitor.beginTask("Making patch...", 1000);
					try {
						ChangeVersionService service = new ChangeVersionServiceImpl();
						service.doChangeVersion(patchInfo, monitor);
					} finally {
						monitor.done();
					}
				}

			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

}
