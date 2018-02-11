package de.domisum.janusinfinifrons.api;

import de.domisum.lib.auxilium.data.container.AbstractURL;
import de.domisum.lib.auxilium.mattp.util.MattpGetUtil;
import de.domisum.lib.auxilium.util.java.exceptions.ShouldNeverHappenError;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
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
	@Override public Optional<Boolean> isUpdateAvailable()
	{
		File runDir = getRunDir();
		String runDirEscaped = AbstractURL.escapeParameterValue(runDir.getAbsolutePath());
		AbstractURL url = new AbstractURL(getJanusServerUrl(), "/updateAvailable?directory="+runDirEscaped);

		Optional<String> updateAvailableOptional = MattpGetUtil.getString(url);
		if(!updateAvailableOptional.isPresent())
			return Optional.empty();

		if(!"true".equals(updateAvailableOptional.get()) && !"false".equals(updateAvailableOptional.get()))
		{
			logger.info("Received invalid server response: {}", updateAvailableOptional.get());
			return Optional.empty();
		}

		return Optional.of(Boolean.parseBoolean(updateAvailableOptional.get()));
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
