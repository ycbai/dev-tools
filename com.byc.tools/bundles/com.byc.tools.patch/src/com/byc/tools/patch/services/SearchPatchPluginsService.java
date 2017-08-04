package com.byc.tools.patch.services;

import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;

import com.byc.tools.patch.exceptions.PatchException;

public interface SearchPatchPluginsService extends MakePatchService {

	public Set<String> searchPatchPlugins(String patchBranch, IProgressMonitor monitor) throws PatchException;

}
