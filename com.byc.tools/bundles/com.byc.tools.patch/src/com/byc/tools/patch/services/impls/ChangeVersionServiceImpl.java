package com.byc.tools.patch.services.impls;

import java.io.File;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;

import com.byc.tools.patch.model.PatchInfo;
import com.byc.tools.patch.services.ChangeVersionService;
import com.byc.tools.patch.utils.PatchFileUtil;

/**
 * 
 * @author ycbai
 *
 */
public class ChangeVersionServiceImpl implements ChangeVersionService {

	@Override
	public boolean doChangeVersion(PatchInfo patchInfo, IProgressMonitor monitor) {
		int taskCount = 900;
		SubMonitor subMonitor = SubMonitor.convert(monitor, taskCount);
		
		String path = patchInfo.getPath();
		String version = patchInfo.getVersion();
		File patchDir = new File(path);

		List<File> jarFiles = PatchFileUtil.getJarFiles(patchDir);
		int unitWeight = taskCount/jarFiles.size();
		for (int i = 0; i < jarFiles.size(); i++) {
			File jarFile = jarFiles.get(i);
			subMonitor.setTaskName("Change version of " + jarFile.getName() + " to " + version);
			File newPatchFile = PatchFileUtil.changePatchName(jarFile, version);
			PatchFileUtil.changePatchVersion(newPatchFile, version);
			subMonitor.worked(unitWeight);
		}
		return true;
	}

}
