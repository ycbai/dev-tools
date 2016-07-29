package com.byc.tools.tests;

import java.io.File;
import java.io.FileOutputStream;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.osgi.framework.Constants;

import com.byc.tools.patch.utils.Compressor;

public class ExampleTest {

	@Test
	public void test() {
		try {
			File jarFileTemplate = new File("resources/com.byc.test.plugin_1.0.0.201607281736.jar");
			File tmpFolder = new File(System.getProperty("java.io.tmpdir"));
			File testJar = new File(tmpFolder, jarFileTemplate.getName());
			FileUtils.copyFile(jarFileTemplate, testJar);
			String testJarPath = testJar.getAbsolutePath();

			JarFile jarFile = new JarFile(testJar);
			Manifest manifest = jarFile.getManifest();
			Attributes mainAttributes = manifest.getMainAttributes();
			mainAttributes.putValue(Constants.BUNDLE_VERSION, "1.2_sdfsdf");

			File targetFolder = new File(tmpFolder, testJar.getName().replace(".", "_"));
			if (targetFolder.exists()) {
				FileUtils.deleteDirectory(targetFolder);
			}
			Compressor.unzip(testJar, targetFolder);
			testJar.delete();

			File targetManifest = new File(targetFolder + "/META-INF/MANIFEST.MF");
			if (targetManifest.isFile() && targetManifest.exists()) {
				FileOutputStream fstream = new FileOutputStream(targetManifest);
				manifest.write(fstream);
			}

			Compressor.zip(targetFolder.getAbsolutePath(), testJarPath, true);
			FileUtils.deleteDirectory(targetFolder);

			jarFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
