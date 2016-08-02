package com.byc.tools.patch.utils.testutils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class PatchTestUtil {
	
	public File getJarToTest(String newName) {
		String fileName = newName;
		File jarFileTemplate = new File("resources/com.byc.test.plugin_1.0.0.20160728_1736_patch.jar");
		if (fileName == null) {
			fileName = jarFileTemplate.getName();
		}
		File tmpFolder = new File(System.getProperty("java.io.tmpdir"));
		File testJar = new File(tmpFolder, fileName);
		try {
			FileUtils.copyFile(jarFileTemplate, testJar);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return testJar;
	}
	
}
