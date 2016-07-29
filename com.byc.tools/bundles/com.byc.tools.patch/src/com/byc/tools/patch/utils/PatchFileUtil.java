package com.byc.tools.patch.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.apache.commons.io.FileUtils;
import org.osgi.framework.Constants;

/**
 * 
 * @author ycbai
 *
 */
public class PatchFileUtil {

	public static final String JAR_SUFFIX = ".jar";

	public static final String VERSION_SEP = "_";

	public static String getPatchVersion(String patchName) {
		return patchName.substring(patchName.indexOf(VERSION_SEP) + 1, patchName.lastIndexOf(JAR_SUFFIX));
	}

	public static File changePatchName(File patchFile, String newVersion) {
		String oldName = patchFile.getName();
		String oldVersion = getPatchVersion(oldName);
		String newName = oldName.replace(oldVersion, newVersion);
		File newPatchFile = new File(patchFile.getParentFile(), newName);
		patchFile.renameTo(newPatchFile);
		return newPatchFile;
	}

	public static boolean changePatchVersion(File patchFile, String newVersion) {
		try {
			File targetFolder = new File(getTmpFolder(), patchFile.getName().replace(".", "_"));
			if (targetFolder.exists()) {
				FileUtils.deleteDirectory(targetFolder);
			}
			Compressor.unzip(patchFile, targetFolder);
			patchFile.delete();

			File targetManifest = new File(targetFolder + "/META-INF/MANIFEST.MF");
			if (targetManifest.isFile() && targetManifest.exists()) {
				Manifest manifest;
				FileInputStream manifestInputStream = null;
				try {
					manifestInputStream = new FileInputStream(targetManifest);
					manifest = new Manifest(manifestInputStream);
				} catch (Exception e) {
					// Should has BOM, need to remove it.
					String content = FileUtils.readFileToString(targetManifest, "ISO8859_1");
					content = content.substring(3);
					FileUtils.write(targetManifest, content, "UTF-8");
					manifestInputStream = new FileInputStream(targetManifest);
					manifest = new Manifest(manifestInputStream);
				} finally {
					if (manifestInputStream != null) {
						manifestInputStream.close();
					}
				}
				FileOutputStream manifestOutputStream = null;
				try {
					Attributes mainAttributes = manifest.getMainAttributes();
					mainAttributes.putValue(Constants.BUNDLE_VERSION, newVersion);
					manifestOutputStream = new FileOutputStream(targetManifest);
					manifest.write(manifestOutputStream);
				} finally {
					if (manifestOutputStream != null) {
						manifestOutputStream.close();
					}
				}
			}
			Compressor.zip(targetFolder.getAbsolutePath(), patchFile.getAbsolutePath(), true);
			FileUtils.forceDelete(targetFolder);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public static List<File> getJarFiles(File file) {
		List<File> jarFiles = new ArrayList<>();
		if(file.isFile()) {
			if (!jarFiles.contains(file) && file.getName().endsWith(JAR_SUFFIX)) {
				jarFiles.add(file);
			}
		} else if (file.isDirectory()) {
			File[] files = file.listFiles();
            for (File file2 : files) {
            	jarFiles.addAll(getJarFiles(file2));
            }
		}
		return jarFiles;
	}

	public static File getTmpFolder() {
		File tmpFolder = new File(System.getProperty("java.io.tmpdir"));
		return tmpFolder;
	}

}
