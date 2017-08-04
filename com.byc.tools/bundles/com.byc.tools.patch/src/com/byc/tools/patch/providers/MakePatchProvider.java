package com.byc.tools.patch.providers;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.runtime.IProgressMonitor;

import com.byc.tools.patch.exceptions.PatchException;
import com.byc.tools.patch.model.PatchInfo;
import com.byc.tools.patch.services.CopyPatchPluginsService;
import com.byc.tools.patch.services.ExportPatchService;
import com.byc.tools.patch.services.SearchPatchPluginsService;
import com.byc.tools.patch.services.impls.CopyPatchPluginsServiceImpl;
import com.byc.tools.patch.services.impls.ExportPatchServiceImpl;
import com.byc.tools.patch.services.impls.SearchPatchPluginsServiceImpl;
import com.byc.tools.patch.utils.PatchFileUtil;

public class MakePatchProvider implements IMakePatchProvider {

	@Override
	public boolean makePatch(PatchInfo patchInfo, IProgressMonitor monitor) throws PatchException {
		// Search patch plugins.
		SearchPatchPluginsService  searchPatchPluginsService = new SearchPatchPluginsServiceImpl();
		searchPatchPluginsService.setCount(400);
		Set<String> pluginNames = searchPatchPluginsService.searchPatchPlugins(patchInfo.getPatchBranch(), monitor);

		// Create a tmp folder to store patch plugins.
		File pluginsTmpFolder;
		try {
			pluginsTmpFolder = PatchFileUtil.getTmpFolder("pluginsTmpFolder");
			if (pluginsTmpFolder.exists()) {
				FileUtils.forceDelete(pluginsTmpFolder);
			}
			pluginsTmpFolder.mkdirs();
		} catch (IOException e) {
			throw new PatchException(e);
		}

		// Copy patch plugins to the tmp folder.
		CopyPatchPluginsService copyPatchPluginsService = new CopyPatchPluginsServiceImpl();
		copyPatchPluginsService.setCount(400);
		copyPatchPluginsService.copyPatchPlugins(patchInfo.getPluginsFolder(), pluginNames, pluginsTmpFolder, monitor);

		// Export patch
		ExportPatchService exportService = new ExportPatchServiceImpl();
		exportService.setCount(150);
		exportService.doExportPatch(pluginsTmpFolder, patchInfo.getTargetPath(), monitor);

		return true;
	}

}
