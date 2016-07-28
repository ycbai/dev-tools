package com.byc.tools.tests;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import org.junit.Test;
import org.osgi.framework.Constants;

public class ExampleTest {

	@Test
	public void test() {
		// just an example
		assertTrue(true);

		File file = new File("/Users/ycbai/test/tmp/commons-io-2.5.jar");
		try {
			JarFile jarFile = new JarFile(file);
			Manifest manifest = jarFile.getManifest();
			Attributes mainAttributes = manifest.getMainAttributes();
			mainAttributes.putValue(Constants.BUNDLE_VERSION, "1.2_sdfsdf");
			FileOutputStream fstream = new FileOutputStream(file);
			JarOutputStream stream = new JarOutputStream(fstream, manifest);
			stream.flush();
			stream.close();
			jarFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
