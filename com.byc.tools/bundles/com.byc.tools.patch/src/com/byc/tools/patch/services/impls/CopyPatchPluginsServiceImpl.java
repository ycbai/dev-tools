package com.byc.tools.patch.services.impls;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.runtime.IProgressMonitor;

import com.byc.tools.patch.exceptions.PatchException;
import com.byc.tools.patch.services.CopyPatchPluginsService;
import com.byc.tools.patch.utils.PatchFileUtil;

public class CopyPatchPluginsServiceImpl extends MakePatchServiceImpl implements CopyPatchPluginsService {

	private static final String PLUGIN_FILE_NAME_PATTERN = "(org\\.talend(.\\w+)+)_\\d\\.\\d\\.\\d\\.\\d{8}_\\d{4}.*";

	@Override
	public boolean copyPatchPlugins(File pluginsFolder, Set<String> pluginNames, IProgressMonitor monitor)
			throws PatchException {
		try {
			File tmpFolder = PatchFileUtil.getTmpFolder("pluginsTmpFolder");
			if (tmpFolder.exists()) {
				FileUtils.forceDelete(tmpFolder);
			}
			File[] filterFiles = pluginsFolder.listFiles(new FileFilter() {

				@Override
				public boolean accept(File file) {
					return isFileNeeded(file, pluginNames);
				}

			});
		} catch (IOException e) {
			throw new PatchException(e);
		}
		return false;
	}

	private boolean isFileNeeded(File file, Set<String> pluginNames) {
		Matcher matcher = Pattern.compile(PLUGIN_FILE_NAME_PATTERN).matcher(file.getName());
		if (matcher.find()) {
			String pluginFileName = matcher.group(1);
			return pluginNames.contains(pluginFileName);
		}
		return false;
	}

}