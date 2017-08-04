package com.byc.tools.patch.providers;

import org.eclipse.core.runtime.IProgressMonitor;

import com.byc.tools.patch.exceptions.PatchException;
import com.byc.tools.patch.model.PatchInfo;

public interface IMakePatchProvider {

	public boolean makePatch(PatchInfo patchInfo, IProgressMonitor monitor) throws PatchException;

}
