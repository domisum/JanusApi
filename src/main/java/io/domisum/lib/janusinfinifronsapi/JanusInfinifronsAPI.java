package io.domisum.lib.janusinfinifronsapi;

import io.domisum.lib.auxiliumlib.annotations.API;

import java.util.Optional;

public interface JanusInfinifronsAPI
{

	@API
	Optional<Boolean> isUpdateAvailable();

}
