package com.byc.tools.patch.services;

import java.util.Set;

import com.byc.tools.patch.exceptions.PatchException;

public interface SearchPatchPluginsService extends MakePatchService {

	public Set<String> searchPatchPlugins(String patchBranch) throws PatchException;

}
