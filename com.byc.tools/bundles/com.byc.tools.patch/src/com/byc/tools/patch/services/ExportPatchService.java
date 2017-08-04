package com.byc.tools.patch.services;

import java.io.File;

import org.eclipse.core.runtime.IProgressMonitor;

import com.byc.tools.patch.exceptions.PatchException;

public interface ExportPatchService extends MakePatchService {

	boolean doExportPatch(File originalPluginsFolder, String targetPath, IProgressMonitor monitor) throws PatchException;

}
