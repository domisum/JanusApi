package io.domisum.janus.api;

import io.domisum.lib.auxiliumlib.annotations.API;

import java.io.IOException;

public interface JanusApi
{
	
	@API
	boolean isUpdateAvailable()
			throws IOException;
	
}
