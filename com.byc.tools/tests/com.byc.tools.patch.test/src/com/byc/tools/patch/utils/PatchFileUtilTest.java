package com.byc.tools.patch.utils;

import static org.junit.Assert.assertEquals;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.junit.Test;

public class PatchFileUtilTest {

	@Test
	public void testGetPatchDefaultVersion() {
		String patchFileName = "com.byc.test.plugin_1.1.0.201607281736_patch.jar";
		String patchDefaultVersion = PatchFileUtil.getPatchDefaultVersion(patchFileName);
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
		Calendar cal = Calendar.getInstance();
		String currentTime = dateFormat.format(cal.getTime());
		String expectVersion = "1.1.0." + currentTime + "_patch";
		assertEquals(expectVersion, patchDefaultVersion);
	}

}
