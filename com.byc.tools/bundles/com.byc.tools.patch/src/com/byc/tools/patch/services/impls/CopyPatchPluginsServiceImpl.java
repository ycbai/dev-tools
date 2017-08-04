package com.byc.tools.patch.services.impls;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.runtime.IProgressMonitor;

import com.byc.tools.patch.constants.IGenericConstants;
import com.byc.tools.patch.exceptions.PatchException;
import com.byc.tools.patch.services.CopyPatchPluginsService;
import com.byc.tools.patch.utils.PatchFileUtil;
import com.byc.tools.patch.utils.ZipFileUtil;

public class CopyPatchPluginsServiceImpl extends MakePatchServiceImpl implements CopyPatchPluginsService {

	private static final String PLUGIN_FILE_NAME_PATTERN = "(org\\.talend(.\\w+)+)_\\d\\.\\d\\.\\d\\.\\d{8}_\\d{4}.*";

	@Override
	public boolean copyPatchPlugins(File pluginsFolder, Set<String> pluginNames, File targetFolder,
			IProgressMonitor monitor) throws PatchException {
		int totalCount = getCount();
		int unitWeight = totalCount / 100;
		try {
			File tmpFolder = PatchFileUtil.getTmpFolder("copiedPluginsTmpFolder");
			if (tmpFolder.exists()) {
				FileUtils.forceDelete(tmpFolder);
			}
			tmpFolder.mkdirs();
			monitor.worked(unitWeight * 10);
			monitor.setTaskName("Filter plugins...");
			File[] filterFiles = pluginsFolder.listFiles(new FileFilter() {

				@Override
				public boolean accept(File file) {
					return isFileNeeded(file, pluginNames);
				}

			});
			monitor.worked(unitWeight * 30);
			monitor.setTaskName("Copy plugins...");
			for (File file : filterFiles) {
				if (file.isDirectory()) {
					File newFile = new File(tmpFolder, file.getName());
					newFile = new File(newFile.getAbsolutePath() + IGenericConstants.JAR_SUFFIX);
					ZipFileUtil.zip(file.getAbsolutePath(), newFile.getAbsolutePath());
				} else {
					FileUtils.copyFileToDirectory(file, tmpFolder, true);
				}
			}
			FileUtils.copyDirectory(tmpFolder, targetFolder);
			monitor.worked(unitWeight * 60);
		} catch (IOException e) {
			throw new PatchException(e);
		}
		return true;
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