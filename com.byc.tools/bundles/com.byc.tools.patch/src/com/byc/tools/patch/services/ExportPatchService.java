package com.byc.tools.patch.services;

import org.eclipse.core.runtime.IProgressMonitor;

import com.byc.tools.patch.exceptions.PatchException;
import com.byc.tools.patch.model.PatchInfo;

public interface ExportPatchService extends MakePatchService {

	boolean doExportPatch(PatchInfo patchInfo, IProgressMonitor monitor) throws PatchException;

}
