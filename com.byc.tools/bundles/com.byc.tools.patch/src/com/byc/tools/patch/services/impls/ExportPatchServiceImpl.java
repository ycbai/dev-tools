package com.byc.tools.patch.services.impls;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;

import com.byc.tools.patch.exceptions.PatchException;
import com.byc.tools.patch.model.PatchInfo;
import com.byc.tools.patch.publish.P2Publisher;
import com.byc.tools.patch.publish.impls.FeaturesAndBundlesPublisher;
import com.byc.tools.patch.services.ExportPatchService;
import com.byc.tools.patch.utils.PatchFileUtil;
import com.byc.tools.patch.utils.ZipFileUtil;

public class ExportPatchServiceImpl extends MakePatchServiceImpl implements ExportPatchService {

	@Override
	public boolean doExportPatch(PatchInfo patchInfo, IProgressMonitor monitor) throws PatchException {
		int totalCount = getCount();
		int unitWeight = totalCount / 100;
		try {
			File tmpFolder = PatchFileUtil.getTmpFolder("exportPatch");
			if (tmpFolder.exists()) {
				FileUtils.forceDelete(tmpFolder);
			}
			List<File> jarFiles = patchInfo.getJarFiles();
			File jarFile = jarFiles.get(0);
			File jarFolder = jarFile.getParentFile();
			File bundlesFolder = new File(tmpFolder, "bundles");
			File pluginsFolder = new File(bundlesFolder, "plugins");
			pluginsFolder.mkdirs();
			FileUtils.copyDirectory(jarFolder, pluginsFolder);
			File siteFolder = new File(tmpFolder, "p2site");
			List<File> otherTypeFiles = new ArrayList<>();
//			File[] files = pluginsFolder.listFiles();
//			for (File file : files) {
//				if (!file.getName().endsWith(PatchFileUtil.JAR_SUFFIX)) {
//					otherTypeFiles.add(file);
//				}
//			}
			monitor.worked(unitWeight * 30);

			monitor.setTaskName("Generating p2 repository");
			P2Publisher publisher = new FeaturesAndBundlesPublisher();
			publisher.publish(siteFolder.getAbsolutePath(), siteFolder.getAbsolutePath(),
					bundlesFolder.getAbsolutePath());
			monitor.worked(unitWeight * 40);
			
			File newPluginsFolder = new File(siteFolder, "plugins");
			for (File file : otherTypeFiles) {
				FileUtils.moveFileToDirectory(file, newPluginsFolder, true);
			}

			monitor.setTaskName("Compressing p2 repository");
			ZipFileUtil.zip(siteFolder.getAbsolutePath(), patchInfo.getTargetPath());
			FileUtils.forceDeleteOnExit(tmpFolder);
			monitor.worked(unitWeight * 30);
		} catch (IOException e) {
			throw new PatchException(e);
		}
		return true;
	}

}
