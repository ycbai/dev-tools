package com.byc.tools.patch.services;

import org.eclipse.core.runtime.IProgressMonitor;

import com.byc.tools.patch.exceptions.PatchException;
import com.byc.tools.patch.model.PatchInfo;

/**
 * 
 * @author ycbai
 *
 */
public interface ChangeVersionService {

	boolean doChangeVersion(PatchInfo patchInfo, IProgressMonitor monitor) throws PatchException;

}
