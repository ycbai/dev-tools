package com.byc.tools.patch.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.apache.commons.io.FileUtils;
import org.osgi.framework.Constants;

/**
 * 
 * @author ycbai
 *
 */
public class PatchFileUtil {

	public static final String VERSION_SEP = "_";

	public static String getPatchVersion(String patchName) {
		return patchName.substring(patchName.indexOf(VERSION_SEP) + 1, patchName.lastIndexOf("."));
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
		String patchPath = patchFile.getAbsolutePath();
		try {
			JarFile jarFile = new JarFile(patchFile);
			Manifest manifest = jarFile.getManifest();
			Attributes mainAttributes = manifest.getMainAttributes();
			mainAttributes.putValue(Constants.BUNDLE_VERSION, newVersion);

			File targetFolder = new File(getTmpFolder(), patchFile.getName().replace(".", "_"));
			if (targetFolder.exists()) {
				FileUtils.deleteDirectory(targetFolder);
			}
			Compressor.unzip(patchFile, targetFolder);
			patchFile.delete();

			File targetManifest = new File(targetFolder + "/META-INF/MANIFEST.MF");
			if (targetManifest.isFile() && targetManifest.exists()) {
				FileOutputStream fstream = new FileOutputStream(targetManifest);
				manifest.write(fstream);
			}

			Compressor.zip(targetFolder.getAbsolutePath(), patchPath, true);
			FileUtils.deleteDirectory(targetFolder);

			jarFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	private static File getTmpFolder() {
		File tmpFolder = new File(System.getProperty("java.io.tmpdir"));
		return tmpFolder;
	}

}
