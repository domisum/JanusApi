package io.domisum.janus.api;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.util.file.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Objects;

@API
public class JanusApiUsingFiles
	implements JanusApi
{
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	
	// CONSTANTS
	public static final String LATEST_BUILD_FILE_NAME = "latestBuild.txt";
	public static final String BUILD_FINGERPRINT_FILE_NAME = "buildFingerprint.txt";
	
	
	// API
	@Override
	public boolean isUpdateAvailable()
	{
		var currentBuildDirectory = getCurrentBuildDirectory();
		var allBuildsDirectory = currentBuildDirectory.getParentFile();
		var latestBuildFile = new File(allBuildsDirectory, LATEST_BUILD_FILE_NAME);
		
		if(!latestBuildFile.exists())
		{
			logger.error("File containing latest build does not exist: {}", latestBuildFile);
			return false;
		}
		String latestBuild = FileUtil.readString(latestBuildFile);
		
		String currentBuild = currentBuildDirectory.getName();
		boolean runningLatestBuild = Objects.equals(latestBuild, currentBuild);
		return !runningLatestBuild;
	}
	
	@Override
	public String getCurrentBuildFingerprint()
	{
		var buildFingerprintFile = new File(getCurrentBuildDirectory(), BUILD_FINGERPRINT_FILE_NAME);
		if(buildFingerprintFile.exists())
			return FileUtil.readString(buildFingerprintFile);
		else
			return null;
	}
	
	
	// UTIL
	private File getCurrentBuildDirectory()
	{
		return new File("dummyFileName").getAbsoluteFile().getParentFile();
	}
	
}
