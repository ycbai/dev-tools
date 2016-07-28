package com.byc.tools.tests;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.AccessController;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.Constants;

import sun.security.action.GetPropertyAction;

public class ExampleTest {

	@Test
	public void test() {
		try {
			File jarFileTemplate = new File("resources/com.byc.test.plugin_1.0.0.201607281736.jar");
			File tmpFolder = new File(AccessController.doPrivileged(new GetPropertyAction("java.io.tmpdir")));
			File testJar = new File(tmpFolder, jarFileTemplate.getName());
			FileUtils.copyFile(jarFileTemplate, testJar);

			JarFile jarFile = new JarFile(testJar);
			Manifest manifest = jarFile.getManifest();
			Attributes mainAttributes = manifest.getMainAttributes();
			mainAttributes.putValue(Constants.BUNDLE_VERSION, "1.2_sdfsdf");
			FileOutputStream fstream = new FileOutputStream(testJar);
			JarOutputStream stream = new JarOutputStream(fstream, manifest);
			stream.flush();
			stream.close();
			 jarFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
