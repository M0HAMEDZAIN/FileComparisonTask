package com.evision.task.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.files")
public class FileProperties {

	private String referencePath;
	private String poolDirectory;

	public String getReferencePath() {
		return referencePath;
	}

	public void setReferencePath(String referencePath) {
		this.referencePath = referencePath;
	}

	public String getPoolDirectory() {
		return poolDirectory;
	}

	public void setPoolDirectory(String poolDirectory) {
		this.poolDirectory = poolDirectory;
	}
}