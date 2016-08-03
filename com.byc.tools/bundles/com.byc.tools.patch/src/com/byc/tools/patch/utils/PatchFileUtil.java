package com.byc.tools.patch.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.osgi.framework.Constants;

import com.byc.tools.patch.exceptions.PatchException;

/**
 * 
 * @author ycbai
 *
 */
public class PatchFileUtil {

	public static final String JAR_SUFFIX = ".jar";

	public static final String VERSION_SEP = "_";

	public static final String PATCH_NAME_PATTERN = ".+_((\\d\\.){3}([\\d|_]+)(_patch)?)\\.jar";

	public static String getPatchVersion(String patchName) {
		return patchName.substring(patchName.indexOf(VERSION_SEP) + 1, patchName.lastIndexOf(JAR_SUFFIX));
	}

	public static File changePatchName(File patchFile, String newVersion) throws PatchException {
		File newPatchFile = null;
		try {
			String oldName = patchFile.getName();
			String oldVersion = getPatchVersion(oldName);
			String newName = oldName.replace(oldVersion, newVersion);
			newPatchFile = new File(patchFile.getParentFile(), newName);
			patchFile.renameTo(newPatchFile);
		} catch (Exception e) {
			throw new PatchException(e);
		}
		return newPatchFile;
	}

	public static boolean changePatchVersion(File patchFile, String newVersion) throws PatchException {
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
			FileUtils.forceDeleteOnExit(targetFolder);
		} catch (Exception e) {
			throw new PatchException(e);
		}
		return true;
	}

	public static List<File> getJarFiles(File file) throws PatchException {
		List<File> jarFiles = new ArrayList<>();
		try {
			if (file.isFile()) {
				if (!jarFiles.contains(file) && file.getName().endsWith(JAR_SUFFIX)) {
					jarFiles.add(file);
				}
			} else if (file.isDirectory()) {
				File[] files = file.listFiles();
				for (File file2 : files) {
					jarFiles.addAll(getJarFiles(file2));
				}
			}
		} catch (Exception e) {
			throw new PatchException(e);
		}
		return jarFiles;
	}

	public static File getTmpFolder() {
		File tmpFolder = new File(System.getProperty("java.io.tmpdir"));
		return tmpFolder;
	}

	public static String getPatchDefaultVersion(String patchFileName) {
		String newVersion = null;
		try {
			Pattern pattern = Pattern.compile(PATCH_NAME_PATTERN);
			Matcher matcher = pattern.matcher(patchFileName);
			if (matcher.matches()) {
				String orignalPatchVersion = matcher.group(1);
				String time = matcher.group(3);
				DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmm");
				Calendar cal = Calendar.getInstance();
				String currentTime = dateFormat.format(cal.getTime());
				newVersion = orignalPatchVersion.replace(time, currentTime);
			}
		} catch (Exception e) {
			// Ignore it.
		}
		return newVersion;
	}

}
