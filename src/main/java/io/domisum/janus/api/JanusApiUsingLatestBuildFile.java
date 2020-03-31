package io.domisum.janus.api;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.util.file.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Objects;

@API
public class JanusApiUsingLatestBuildFile
		implements JanusApi
{
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	
	// CONSTANTS
	private static final String LATEST_BUILD_FILE_NAME = "latestBuild.txt";
	
	
	// API
	@Override
	public boolean isUpdateAvailable()
	{
		var buildDirectory = new File("dummyFileName").getAbsoluteFile().getParentFile();
		
		var latestBuildFile = new File(buildDirectory.getParentFile(), LATEST_BUILD_FILE_NAME);
		if(!latestBuildFile.exists())
		{
			logger.error("File containing latest build does not exist: {}", latestBuildFile);
			return false;
		}
		String latestBuild = FileUtil.readString(latestBuildFile);
		
		String currentBuild = buildDirectory.getName();
		boolean runningLatestBuild = Objects.equals(latestBuild, currentBuild);
		boolean updateAvailable = !runningLatestBuild;
		
		return updateAvailable;
	}
	
}
