package io.domisum.lib.janusinfinifronsapi;

import io.domisum.lib.ezhttp.EzHttpRequestEnvoy;
import io.domisum.lib.ezhttp.request.EzHttpRequest;
import io.domisum.lib.ezhttp.response.EzHttpIoResponse;
import io.domisum.lib.ezhttp.response.EzHttpResponse;
import io.domisum.lib.ezhttp.response.bodyreaders.EzHttpStringBodyReader;
import io.domisum.lib.auxiliumlib.datacontainers.AbstractURL;
import io.domisum.lib.auxiliumlib.exceptions.ShouldNeverHappenError;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.util.Optional;

@RequiredArgsConstructor
public class HttpJanusInfinifronsAPI implements JanusInfinifronsAPI
{

	private final Logger logger = LoggerFactory.getLogger(getClass());


	// SETTINGS
	private final int port;


	// API
	@Override
	public Optional<Boolean> isUpdateAvailable()
	{
		try
		{
			return tryCheckForUpdate();
		}
		catch(IOException e)
		{
			logger.warn("Failed to check if update available", e);
			return Optional.empty();
		}
	}

	private Optional<Boolean> tryCheckForUpdate() throws java.io.IOException
	{
		File runDir = getRunDir();
		String runDirEscaped = AbstractURL.escapeUrlParameterString(runDir.getAbsolutePath());
		AbstractURL url = new AbstractURL(getJanusServerUrl(), "/updateAvailable?directory="+runDirEscaped);

		EzHttpRequest request = EzHttpRequest.get(url);
		EzHttpRequestEnvoy<String> envoy = new EzHttpRequestEnvoy<>(request, new EzHttpStringBodyReader());

		EzHttpIoResponse<String> ioResponse = envoy.send();
		if(!ioResponse.isPresent())
		{
			logger.warn("Failed to connect to janus server, can't check if update available");
			return Optional.empty();
		}
		EzHttpResponse<String> response = ioResponse.get();
		String responseString = response.getSuccessBodyOrThrowHttpIoException("failed to fetch update available status");

		if("true".equals(responseString))
			return Optional.of(true);
		else if("false".equals(responseString))
			return Optional.of(false);
		else
		{
			logger.warn("Received invalid janus server response for update available check: '{}'", responseString);
			return Optional.empty();
		}
	}


	// UTIL
	private AbstractURL getJanusServerUrl()
	{
		return new AbstractURL("http://localhost:"+port);
	}

	private File getRunDir()
	{
		CodeSource codeSource = getClass().getProtectionDomain().getCodeSource();
		try
		{
			return new File(codeSource.getLocation().toURI().getPath()).getAbsoluteFile().getParentFile();
		}
		catch(URISyntaxException e)
		{
			throw new ShouldNeverHappenError(e);
		}
	}

}
