package com.byc.tools.patch.services.impls;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.runtime.IProgressMonitor;

import com.byc.tools.patch.exceptions.PatchException;
import com.byc.tools.patch.model.PatchInfo;
import com.byc.tools.patch.publish.P2Publisher;
import com.byc.tools.patch.publish.impls.FeaturesAndBundlesPublisher;
import com.byc.tools.patch.services.ExportPatchService;

public class ExportPatchServiceImpl extends MakePatchServiceImpl implements ExportPatchService {

	@Override
	public boolean doExportPatch(PatchInfo patchInfo, IProgressMonitor monitor) throws PatchException {
		// TODO: copy jar files of patchInfo into a temp bundles folder as
		// bundles folder.
		// Create a temp repository folder.
		// FileUtils.copyFile();

		P2Publisher publisher = new FeaturesAndBundlesPublisher();
		publisher.publish(MetadataRepositoryPath, ArtifactRepositoryPath, BundlesPath);

		// TODO: zip exported p2 repository.
		return true;
	}

}
