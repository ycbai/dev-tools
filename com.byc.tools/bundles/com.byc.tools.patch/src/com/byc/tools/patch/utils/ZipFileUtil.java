package com.byc.tools.patch.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class ZipFileUtil {

	private static int BUF_SIZE = 2048;

	public static void unZip(File unZipFile, String destFilePath) throws ZipException, IOException {
		if (destFilePath == null || destFilePath.trim().length() == 0) {
			throw new IOException("Destination path cannot be null!");
		}
		File destFile;
		ZipFile zipFile = null;
		try {
			zipFile = new ZipFile(unZipFile);
			for (Enumeration<? extends ZipEntry> entries = zipFile.entries(); entries.hasMoreElements();) {
				ZipEntry entry = entries.nextElement();
				destFile = new File(destFilePath, entry.getName());
				unZipFile(destFile, zipFile, entry);
			}
		} finally {
			if (zipFile != null) {
				zipFile.close();
			}
		}
	}

	private static void unZipFile(File destFile, ZipFile zipFile, ZipEntry entry) throws IOException {
		InputStream inputStream = null;
		FileOutputStream fileOut = null;
		try {
			if (entry.isDirectory()) {
				destFile.mkdirs();
			} else {
				File parent = destFile.getParentFile();
				if (parent != null && !parent.exists()) {
					parent.mkdirs();
				}
				inputStream = zipFile.getInputStream(entry);
				fileOut = new FileOutputStream(destFile);
				byte[] buf = new byte[BUF_SIZE];
				int readedBytes;
				while ((readedBytes = inputStream.read(buf)) > 0) {
					fileOut.write(buf, 0, readedBytes);
				}
			}
		} finally {
			if (fileOut != null) {
				fileOut.close();
			}
			if (inputStream != null) {
				inputStream.close();
			}
		}
	}

	public static String zip(String zipDirectory, String zipFilePath) throws IOException {
		return zip(zipDirectory, zipFilePath, false);
	}

	public static String zip(String zipDirectory, String zipFilePath, boolean includeSelfDir) throws IOException {
		File zipDir = new File(zipDirectory);
		File[] zipFiles;
		if (includeSelfDir || zipDir.isFile()) {
			zipFiles = new File[] { zipDir };
		} else {
			zipFiles = zipDir.listFiles();
		}
		return zip(zipFiles, zipFilePath);
	}

	private static String zip(File[] files, String zipFilePath) throws IOException {
		JarOutputStream jarOutput = null;
		try {
			jarOutput = new JarOutputStream(new FileOutputStream(zipFilePath));
			for (File file : files) {
				zipFiles(file, jarOutput, "");
			}
		} finally {
			if (jarOutput != null) {
				jarOutput.close();
			}
		}
		return null;
	}

	private static void zipFiles(File file, JarOutputStream jos, String pathName) throws IOException {
		String fileName = pathName + file.getName();
		BufferedInputStream inStream = null;
		try {
			if (file.isDirectory()) {
				fileName = fileName + "/";
				jos.putNextEntry(new JarEntry(fileName));
				String fileNames[] = file.list();
				if (fileNames != null) {
					for (int i = 0; i < fileNames.length; i++) {
						zipFiles(new File(file, fileNames[i]), jos, fileName);
					}
				}
			} else {
				JarEntry jarEntry = new JarEntry(fileName);
				inStream = new BufferedInputStream(new FileInputStream(file));
				jos.putNextEntry(jarEntry);
				byte[] buf = new byte[BUF_SIZE];
				int len;
				while ((len = inStream.read(buf)) >= 0) {
					jos.write(buf, 0, len);
				}
			}
		} finally {
			if (jos != null) {
				jos.closeEntry();
			}
			if (inStream != null) {
				inStream.close();
			}
		}
	}

}
