package com.byc.tools.patch.publish.impls;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.equinox.internal.p2.artifact.repository.ArtifactRepositoryManager;
import org.eclipse.equinox.internal.p2.artifact.repository.simple.SimpleArtifactRepositoryFactory;
import org.eclipse.equinox.internal.p2.metadata.repository.MetadataRepositoryManager;
import org.eclipse.equinox.internal.p2.metadata.repository.SimpleMetadataRepositoryFactory;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.equinox.p2.publisher.IPublisherAction;
import org.eclipse.equinox.p2.publisher.IPublisherInfo;
import org.eclipse.equinox.p2.publisher.Publisher;
import org.eclipse.equinox.p2.publisher.PublisherInfo;
import org.eclipse.equinox.p2.publisher.eclipse.BundlesAction;
import org.eclipse.equinox.p2.publisher.eclipse.FeaturesAction;
import org.eclipse.equinox.p2.repository.IRepository;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRepository;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepository;

import com.byc.tools.patch.exceptions.PublishException;
import com.byc.tools.patch.publish.P2Publisher;

/**
 * Publish features and bundles to p2 repository.
 * 
 * @author ycbai
 *
 */
@SuppressWarnings("restriction")
public class FeaturesAndBundlesPublisher implements P2Publisher {

	@Override
	public void publish(String metadataRepositoryPath, String artifactRepositoryPath, String sourcePath)
			throws PublishException {
		try {
			IPublisherInfo info = createPublisherInfo(getFormalRepositoryPath(metadataRepositoryPath),
					getFormalRepositoryPath(artifactRepositoryPath));
			IPublisherAction[] actions = createActions(getFormalSourcePath(sourcePath));
			Publisher publisher = new Publisher(info);
			IStatus result = publisher.publish(actions, new NullProgressMonitor());
			if (result.getSeverity() == IStatus.ERROR) {
				throw new CoreException(result);
			}
		} catch (Exception e) {
			throw new PublishException(e);
		}
	}

	private String getFormalRepositoryPath(String repositoryPath) {
		String formalPath = repositoryPath;
		formalPath = formalPath.replace("\\", "/");
		formalPath = "file:" + formalPath;
		return formalPath;
	}

	private String getFormalSourcePath(String sourcePath) {
		String formalPath = sourcePath;
		formalPath = formalPath.replace("\\", "/");
		formalPath = "/" + formalPath;
		return formalPath;
	}

	public static IPublisherInfo createPublisherInfo(String metadataRepositoryPath, String artifactRepositoryPath)
			throws ProvisionException, URISyntaxException {
		PublisherInfo result = new PublisherInfo();
		Map<String, String> properties = new HashMap<>();
		properties.put(IRepository.PROP_COMPRESSED, "false");

		// Create the metadata repository. This will fail if a repository
		// already exists here
		IMetadataRepository metadataRepository = new SimpleMetadataRepositoryFactory().create(
				new URI(metadataRepositoryPath), metadataRepositoryPath + " - metadata",
				MetadataRepositoryManager.TYPE_SIMPLE_REPOSITORY, properties);

		// Create the artifact repository. This will fail if a repository
		// already exists here
		IArtifactRepository artifactRepository = new SimpleArtifactRepositoryFactory().create(
				new URI(artifactRepositoryPath), artifactRepositoryPath + " - artifacts",
				ArtifactRepositoryManager.TYPE_SIMPLE_REPOSITORY, properties);

		result.setMetadataRepository(metadataRepository);
		result.setArtifactRepository(artifactRepository);
		result.setArtifactOptions(IPublisherInfo.A_PUBLISH | IPublisherInfo.A_INDEX);
		return result;
	}

	public static IPublisherAction[] createActions(String sourcePath) {
		File[] bundleLocations = new File[] { new File(sourcePath) };
		BundlesAction bundlesAction = new BundlesAction(bundleLocations);
		FeaturesAction featuresAction = new FeaturesAction(bundleLocations);
		return new IPublisherAction[] { bundlesAction, featuresAction };
	}

}
