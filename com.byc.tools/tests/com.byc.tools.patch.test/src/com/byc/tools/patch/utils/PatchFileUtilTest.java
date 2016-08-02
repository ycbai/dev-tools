package com.byc.tools.patch.utils;

import static org.junit.Assert.assertEquals;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.junit.Test;

public class PatchFileUtilTest {

	@Test
	public void testGetPatchDefaultVersion() {
		String patchFileName = "com.byc.test.plugin_1.0.0.20160728_1736_patch.jar";
		String patchDefaultVersion = PatchFileUtil.getPatchDefaultVersion(patchFileName);
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmm");
		Calendar cal = Calendar.getInstance();
		String currentTime = dateFormat.format(cal.getTime());
		String expectVersion = "1.0.0." + currentTime + "_patch";
		assertEquals(expectVersion, patchDefaultVersion);
	}

}
