package com.byc.tools.patch.ui.wizards;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.Wizard;

import com.byc.tools.patch.PatchPlugin;
import com.byc.tools.patch.log.ExceptionLogger;
import com.byc.tools.patch.model.PatchInfo;
import com.byc.tools.patch.services.ChangeVersionService;
import com.byc.tools.patch.services.ExportPatchService;
import com.byc.tools.patch.services.impls.ChangeVersionServiceImpl;
import com.byc.tools.patch.services.impls.ExportPatchServiceImpl;

/**
 * 
 * @author ycbai
 *
 */
public class MakePatchWizard extends Wizard {

	private PatchInfo patchInfo;

	private PatchMainPage patchMainPage;

	public MakePatchWizard() {
		super();
		patchInfo = new PatchInfo();
		setNeedsProgressMonitor(true);
		setHelpAvailable(false);
	}

	@Override
	public String getWindowTitle() {
		return "Make Patch";
	}

	@Override
	public void addPages() {
		patchMainPage = new PatchMainPage(patchInfo);
		addPage(patchMainPage);
	}

	@Override
	public boolean performFinish() {
		try {
			this.getContainer().run(true, true, new IRunnableWithProgress() {

				@Override
				public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					monitor.beginTask("Making patch...", 1000);
					try {
						// Change name and version.
						ChangeVersionService versionService = new ChangeVersionServiceImpl();
						versionService.setCount(800);
						versionService.doChangeVersion(patchInfo, monitor);
						// Export patch
						ExportPatchService exportService = new ExportPatchServiceImpl();
						exportService.setCount(150);
						exportService.doExportPatch(patchInfo, monitor);
					} catch (Exception ex) {
						throw new InvocationTargetException(ex);
					} finally {
						monitor.done();
					}
				}

			});
		} catch (Exception e) {
			ExceptionLogger.log(e);
			Status errorStatus = new Status(IStatus.ERROR, PatchPlugin.PLUGIN_ID, ExceptionUtils.getStackTrace(e));
			MultiStatus multiErrorStatus = new MultiStatus(PatchPlugin.PLUGIN_ID, IStatus.ERROR,
					new Status[] { errorStatus }, "Please check it in details.", e);
			ErrorDialog.openError(getShell(), "Error", "Fail to make the patch.", multiErrorStatus);
			return false;
		}
		return true;
	}

}
