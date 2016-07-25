package com.byc.tools.patch.utils;

import java.io.File;

import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.util.ManifestElement;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;

import com.byc.tools.patch.PatchPlugin;

/**
 * 
 * @author ycbai
 *
 */
public class PatchFileUtil {

	public static final String VERSION_SEP = "_";

	public static String getPatchVersion(String patchName) {
		return patchName.substring(patchName.indexOf(VERSION_SEP) + 1);
	}

	public static boolean changePatchName(File patchFile, String newVersion) {
		String oldName = patchFile.getName();
		String oldVersion = getPatchVersion(oldName);
		String newName = oldName.replace(oldVersion, newVersion);
		File newPatchFile = new File(patchFile.getParentFile(), newName);
		return patchFile.renameTo(newPatchFile);
	}

	public static boolean changePatchVersion(File patchFile, String newVersion) {
		String requireBundle = (String) Platform.getBundle(PatchPlugin.PLUGIN_ID).getHeaders()
				.get(Constants.REQUIRE_BUNDLE);
		try {
			ManifestElement[] elements = ManifestElement.parseHeader(Constants.BUNDLE_CLASSPATH, requireBundle);
			for (ManifestElement manifestElement : elements) {
				System.out.println(manifestElement.getValue());
				manifestElement.getAttribute(Constants.BUNDLE_VERSION);
			}
		} catch (BundleException e) {
			e.printStackTrace();
		}
		return true;
	}

}
