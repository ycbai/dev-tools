package com.byc.tools.patch.publish;

import com.byc.tools.patch.exceptions.PublishException;

public interface P2Publisher {

	public void publish(String metadataRepositoryPath, String artifactRepositoryPath, String sourcePath)
			throws PublishException;

}
