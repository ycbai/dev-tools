package com.byc.tools.patch.utils.testutils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;

import com.byc.tools.patch.PatchPlugin;

public class PatchTestUtil {

	public static File getJarToTest(String newName) throws IOException {
		String fileName = newName;
		File jarFileTemplate = new File("resources/com.byc.test.plugin_1.0.0.20160728_1736_patch.jar");
		if (fileName == null) {
			fileName = jarFileTemplate.getName();
		}
		File tmpFolder = new File(System.getProperty("java.io.tmpdir"));
		File testJar = new File(tmpFolder, fileName);
		FileUtils.copyFile(jarFileTemplate, testJar);
		return testJar;
	}

	public static File getTestTempDir() throws IOException {
		File tmpFolder = new File(System.getProperty("java.io.tmpdir"));
		File testFolder = new File(tmpFolder, PatchPlugin.PLUGIN_ID.replace(".", "_") + "_test");
		if (testFolder.exists()) {
			FileUtils.forceDelete(testFolder);
		}
		testFolder.mkdirs();
		FileUtils.forceDeleteOnExit(testFolder);
		return testFolder;
	}

	public static String getSha1(File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		String sha1 = DigestUtils.shaHex(fis);
		fis.close();
		return sha1;
	}

	public static Map<String, File> getFileMaps(File file) {
		Map<String, File> fileMaps = new HashMap<>();
		if (file.isDirectory()) {
			fileMaps.putAll(getFileMaps(file));
		}
		fileMaps.put(file.getName(), file);
		return fileMaps;
	}

}
