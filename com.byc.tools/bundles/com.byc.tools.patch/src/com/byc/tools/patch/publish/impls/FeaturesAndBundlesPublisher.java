package com.byc.tools.patch.publish.impls;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;

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
import org.eclipse.equinox.p2.repository.artifact.IArtifactRepository;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepository;

import com.byc.tools.patch.exceptions.PublishException;
import com.byc.tools.patch.publish.P2Publisher;

@SuppressWarnings("restriction")
public class FeaturesAndBundlesPublisher implements P2Publisher {

	@Override
	public void publish() throws PublishException {
		try {
			IPublisherInfo info = createPublisherInfo();
			IPublisherAction[] actions = createActions();
			Publisher publisher = new Publisher(info);
			IStatus result = publisher.publish(actions, new NullProgressMonitor());
			if (result.getSeverity() == IStatus.ERROR) {
				throw new CoreException(result);
			}
		} catch (Exception e) {
			throw new PublishException(e);
		}
	}

	public static IPublisherInfo createPublisherInfo() throws ProvisionException, URISyntaxException {
		PublisherInfo result = new PublisherInfo();

		// Create the metadata repository. This will fail if a repository
		// already exists here
		IMetadataRepository metadataRepository = new SimpleMetadataRepositoryFactory().create(
				new URI("file:/Users/ycbai/Desktop/rep/p2site"), "Sample Metadata Repository",
				MetadataRepositoryManager.TYPE_SIMPLE_REPOSITORY, Collections.emptyMap());

		// Create the artifact repository. This will fail if a repository
		// already exists here
		IArtifactRepository artifactRepository = new SimpleArtifactRepositoryFactory().create(
				new URI("file:/Users/ycbai/Desktop/rep/p2site"), "Sample Artifact Repository",
				ArtifactRepositoryManager.TYPE_SIMPLE_REPOSITORY, Collections.emptyMap());

		result.setMetadataRepository(metadataRepository);
		result.setArtifactRepository(artifactRepository);
		result.setArtifactOptions(IPublisherInfo.A_PUBLISH | IPublisherInfo.A_INDEX);
		return result;
	}

	public static IPublisherAction[] createActions() {
		IPublisherAction[] result = new IPublisherAction[1];
		File[] bundleLocations = new File[1];
		bundleLocations[0] = new File("file:/Users/ycbai/Desktop/rep/bundles");
		BundlesAction bundlesAction = new BundlesAction(bundleLocations);
		result[0] = bundlesAction;
		return result;
	}

}
