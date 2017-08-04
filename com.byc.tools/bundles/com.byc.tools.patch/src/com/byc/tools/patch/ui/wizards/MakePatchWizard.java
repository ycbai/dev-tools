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
import com.byc.tools.patch.exceptions.PatchException;
import com.byc.tools.patch.log.ExceptionLogger;
import com.byc.tools.patch.model.PatchInfo;
import com.byc.tools.patch.providers.IMakePatchProvider;
import com.byc.tools.patch.providers.MakeOldPatchProvider;
import com.byc.tools.patch.providers.MakePatchProvider;

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
						makePatch(monitor);
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

	private void makePatch(IProgressMonitor monitor) throws PatchException {
		IMakePatchProvider patchProvider = null;
		if (patchMainPage.isNewPatch()) {
			patchProvider = new MakePatchProvider();
		} else {
			patchProvider = new MakeOldPatchProvider();
		}
		patchProvider.makePatch(patchInfo, monitor);
	}

}
