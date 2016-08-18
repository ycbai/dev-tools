package com.byc.tools.patch.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.osgi.framework.Constants;

import com.byc.tools.patch.PatchPlugin;
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
			File targetFolder = new File(getTmpFolder("changePatchVersion"), patchFile.getName().replace(".", "_"));
			if (targetFolder.exists()) {
				FileUtils.deleteDirectory(targetFolder);
			}
			ZipFileUtil.unZip(patchFile, targetFolder.getAbsolutePath());
			patchFile.delete();

			File targetManifest = new File(targetFolder, JarFile.MANIFEST_NAME);
			if (targetManifest.isFile() && targetManifest.exists()) {
				replaceVersion(targetManifest, newVersion);
			}
			ZipFileUtil.zip(targetFolder.getAbsolutePath(), patchFile.getAbsolutePath());
			FileUtils.forceDeleteOnExit(targetFolder);
		} catch (Exception e) {
			throw new PatchException(e);
		}
		return true;
	}

	private static void replaceVersion(File manifestFile, String version) throws PatchException {
		try {
			List<String> newLines = new ArrayList<>();
			for (String line : Files.readAllLines(Paths.get(manifestFile.toURI()), StandardCharsets.UTF_8)) {
				if (line.contains(Constants.BUNDLE_VERSION)) {
					newLines.add(Constants.BUNDLE_VERSION + ": " + version);
				} else {
					newLines.add(line);
				}
			}
			Files.write(Paths.get(manifestFile.toURI()), newLines, StandardCharsets.UTF_8);
		} catch (Exception e) {
			throw new PatchException(e);
		}
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

	public static File getTmpFolder(String name) throws IOException {
		File sysTmpFolder = new File(System.getProperty("java.io.tmpdir"));
	    File patchTmpFolder = new File(sysTmpFolder, PatchPlugin.PLUGIN_ID.replace(".", "_"));
	    if (StringUtils.isNotEmpty(name)) {
	    	patchTmpFolder = new File(patchTmpFolder, name);
	    }
	    if (!patchTmpFolder.exists()) {
	    	patchTmpFolder.mkdirs();
	    }
	    FileUtils.forceDeleteOnExit(patchTmpFolder);
		return patchTmpFolder;
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
