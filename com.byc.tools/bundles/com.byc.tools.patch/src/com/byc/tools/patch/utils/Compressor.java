package com.byc.tools.patch.utils;

import java.io.File;

import com.byc.tools.patch.exceptions.PatchException;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

public class Compressor {

	public static void zip(File sourceFolder, File destinationFile, boolean isSkipRootFolder) throws PatchException {
		zip(sourceFolder.getAbsolutePath(), destinationFile.getAbsolutePath(), isSkipRootFolder);
	}

	public static void zip(String sourceFolderPath, String destinationFilePath, boolean isSkipRootFolder) throws PatchException {
		try {
			ZipParameters parameters = new ZipParameters();
			parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
			parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
			ZipFile zipFile = new ZipFile(destinationFilePath);

			File sourceFolder = new File(sourceFolderPath);
			if (isSkipRootFolder) {
				File[] files = sourceFolder.listFiles();
				for (File file : files) {
					addFileToZip(file, parameters, zipFile);
				}
			} else {
				addFileToZip(sourceFolder, parameters, zipFile);
			}
		} catch (ZipException e) {
			throw new PatchException(e);
		}
	}

	private static void addFileToZip(File sourceFile, ZipParameters parameters, ZipFile zipFile) throws ZipException {
		if (sourceFile.isFile()) {
			zipFile.addFile(sourceFile, parameters);
		} else if (sourceFile.isDirectory()) {
			zipFile.addFolder(sourceFile, parameters);
		} else {
			// show error message
		}
	}

	public static void unzip(File zipFile, File destinationFolder) throws PatchException {
		unzip(zipFile.getAbsolutePath(), destinationFolder.getAbsolutePath());
	}

	public static void unzip(String zipFilePath, String destinationFolderPath) throws PatchException {
		try {
			ZipFile zipFile = new ZipFile(zipFilePath);
			zipFile.extractAll(destinationFolderPath);
		} catch (ZipException e) {
			throw new PatchException(e);
		}
	}

}