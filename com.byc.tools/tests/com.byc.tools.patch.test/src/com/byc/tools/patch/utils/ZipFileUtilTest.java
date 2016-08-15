package com.byc.tools.patch.utils;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.byc.tools.patch.utils.testutils.PatchTestUtil;

public class ZipFileUtilTest {

	@Test
	public void testUnZipAndZip() throws IOException {
		File unZipFile = PatchTestUtil.getJarToTest(null);
		File testTempDir = PatchTestUtil.getTestTempDir();
		ZipFileUtil.unZip(unZipFile, testTempDir.getAbsolutePath());
		File zipFile = new File(testTempDir, unZipFile.getName());
		ZipFileUtil.zip(testTempDir.getAbsolutePath(), zipFile.getAbsolutePath());
	}

}
