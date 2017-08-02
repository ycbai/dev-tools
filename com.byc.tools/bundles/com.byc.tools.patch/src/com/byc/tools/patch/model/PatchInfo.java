package com.byc.tools.patch.model;

import java.io.File;
import java.util.List;

import com.byc.tools.patch.exceptions.PatchException;
import com.byc.tools.patch.log.ExceptionLogger;
import com.byc.tools.patch.utils.PatchFileUtil;

/**
 *
 * @author ycbai
 *
 */
public class PatchInfo {

	private String patchBranch;

	private String targetPath;

	private String version;

	private File pluginsFolder;

	private List<File> jarFiles;

	public String getPatchBranch() {
		return patchBranch;
	}

	public void setPatchBranch(String patchBranch) {
		this.patchBranch = patchBranch;
	}

	public String getTargetPath() {
		return targetPath;
	}

	public void setTargetPath(String targetPath) {
		this.targetPath = targetPath;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public File getPluginsFolder() {
		return pluginsFolder;
	}

	public void setPluginsFolder(File pluginsFolder) {
		if (pluginsFolder != null && !pluginsFolder.equals(this.pluginsFolder)) {
			this.pluginsFolder = pluginsFolder;
			jarFiles = null;
		}
	}

	public List<File> getJarFiles() {
		if (jarFiles == null) {
			try {
				jarFiles = PatchFileUtil.getJarFiles(getPluginsFolder());
			} catch (PatchException ex) {
				ExceptionLogger.log(ex);
			}
		}
		return jarFiles;
	}

	@Override
	public String toString() {
		return "PatchInfo [patchBranch=" + patchBranch + ", targetPath=" + targetPath + ", version=" + version
				+ ", pluginsFolder=" + pluginsFolder + "]";
	}

}
