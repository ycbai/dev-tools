package com.byc.tools.patch.services.impls;

import java.io.File;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;

import com.byc.tools.patch.exceptions.PatchException;
import com.byc.tools.patch.model.PatchInfo;
import com.byc.tools.patch.services.ChangeVersionService;
import com.byc.tools.patch.utils.PatchFileUtil;

/**
 * 
 * @author ycbai
 *
 */
public class ChangeVersionServiceImpl extends MakePatchServiceImpl implements ChangeVersionService {

	@Override
	public boolean doChangeVersion(PatchInfo patchInfo, IProgressMonitor monitor) throws PatchException {
		int totalCount = getCount();
		SubMonitor subMonitor = SubMonitor.convert(monitor, getCount());

		String version = patchInfo.getVersion();
		List<File> jarFiles = patchInfo.getJarFiles();
		int unitWeight = totalCount / jarFiles.size();
		for (int i = 0; i < jarFiles.size(); i++) {
			File jarFile = jarFiles.get(i);
			subMonitor.setTaskName(jarFile.getName() + ": Change version to " + version);
			File newPatchFile = PatchFileUtil.changePatchName(jarFile, version);
			PatchFileUtil.changePatchVersion(newPatchFile, version);
			subMonitor.worked(unitWeight);
		}
		return true;
	}

}
