package com.byc.tools.patch.providers;

import org.eclipse.core.runtime.IProgressMonitor;

import com.byc.tools.patch.exceptions.PatchException;
import com.byc.tools.patch.model.PatchInfo;
import com.byc.tools.patch.services.ChangeVersionService;
import com.byc.tools.patch.services.ExportPatchService;
import com.byc.tools.patch.services.impls.ChangeVersionServiceImpl;
import com.byc.tools.patch.services.impls.ExportPatchServiceImpl;

public class MakeOldPatchProvider implements IMakePatchProvider {

	@Override
	public boolean makePatch(PatchInfo patchInfo, IProgressMonitor monitor) throws PatchException {
		// Change name and version.
		ChangeVersionService versionService = new ChangeVersionServiceImpl();
		versionService.setCount(800);
		versionService.doChangeVersion(patchInfo, monitor);
		
		// Export patch
		ExportPatchService exportService = new ExportPatchServiceImpl();
		exportService.setCount(150);
		exportService.doExportPatch(patchInfo.getPluginsFolder(), patchInfo.getTargetPath(), monitor);

		return true;
	}

}
