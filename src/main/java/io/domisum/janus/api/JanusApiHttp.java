package io.domisum.janus.api;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.util.java.ExceptionUtil;
import io.domisum.lib.ezhttp.EzHttpRequestEnvoy;
import io.domisum.lib.ezhttp.request.EzHttpRequest;
import io.domisum.lib.ezhttp.request.url.EzUrl;
import io.domisum.lib.ezhttp.response.bodyreaders.EzHttpStringBodyReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

@API
public class JanusApiHttp
		implements JanusApi
{
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	
	// CONSTANTS
	private static final int PORT = 8381;
	
	
	// API
	@Override
	public boolean isUpdateAvailable()
	{
		try
		{
			return isUpdateAvailableUncaught();
		}
		catch(IOException e)
		{
			logger.warn("Failed to check if update is available: {}", ExceptionUtil.getShortSynopsis(e));
			return false;
		}
	}
	
	@Override
	public boolean isUpdateAvailableUncaught()
			throws IOException
	{
		var buildDirectory = new File("dummyFileName").getAbsoluteFile().getParentFile();
		String projectName = buildDirectory.getParentFile().getName();
		String buildName = buildDirectory.getName();
		
		var endpoint = new EzUrl("http", "localhost", PORT, "updateAvailable");
		var url = endpoint.withParameter("project", projectName).withParameter("build", buildName);
		var request = EzHttpRequest.get(url);
		
		var envoy = new EzHttpRequestEnvoy<>(request, new EzHttpStringBodyReader());
		var ioResponse = envoy.send();
		var response = ioResponse.getOrThrowWrapped("Connection to Janus failed");
		response.getSuccessBodyOrThrowHttpIoException("Unexpected Janus HTTP response");
		String responseString = response.getSuccessBody();
		boolean updateAvailable = Boolean.parseBoolean(responseString);
		
		return updateAvailable;
	}
	
}
