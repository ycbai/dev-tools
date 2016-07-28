package com.byc.tools.patch.services.impls;

import java.io.File;
import java.io.FileFilter;

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
	public boolean doChangeVersion(PatchInfo patchInfo) {
		String path = patchInfo.getPath();
		String version = patchInfo.getVersion();
		File patchDir = new File(path);
		File[] jarFiles = patchDir.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File file) {
				return file.getName().endsWith(".jar");
			}
		});
		for (int i = 0; i < jarFiles.length; i++) {
			File jarFile = jarFiles[i];
			File newPatchFile = PatchFileUtil.changePatchName(jarFile, version);
			PatchFileUtil.changePatchVersion(newPatchFile, version);
		}
		return true;
	}
	
}
