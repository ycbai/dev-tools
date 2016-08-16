package com.byc.tools.patch.model;

import java.io.File;
import java.util.List;

/**
 * 
 * @author ycbai
 *
 */
public class PatchInfo {

	private String targetPath;

	private String version;
	
	private List<File> jarFiles;

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

	public List<File> getJarFiles() {
		return jarFiles;
	}

	public void setJarFiles(List<File> jarFiles) {
		this.jarFiles = jarFiles;
	}

	@Override
	public String toString() {
		return "PatchInfo [targetPath=" + targetPath + ", version=" + version + ", jarFiles=" + jarFiles + "]";
	}

}
