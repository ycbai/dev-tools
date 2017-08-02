package com.byc.tools.patch.services;

import java.io.File;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;

import com.byc.tools.patch.exceptions.PatchException;

public interface CopyPatchPluginsService extends MakePatchService {

	public boolean copyPatchPlugins(File pluginsFolder, Set<String> pluginNames, IProgressMonitor monitor) throws PatchException;

}
