package com.byc.tools.patch.services;

import java.io.File;
import java.util.List;

public interface SearchPatchPluginsService extends MakePatchService {

	public List<File> searchPatchPlugins(String[] gitReps, String patchBranch);

}
