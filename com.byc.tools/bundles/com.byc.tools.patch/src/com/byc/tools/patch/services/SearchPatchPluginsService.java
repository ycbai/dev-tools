package com.byc.tools.patch.services;

import java.util.List;

import com.byc.tools.patch.exceptions.PatchException;

public interface SearchPatchPluginsService extends MakePatchService {

	public List<String> searchPatchPlugins(String patchBranch) throws PatchException;

}
