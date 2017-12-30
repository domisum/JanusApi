package de.domisum.janusinfinifrons.api;

import de.domisum.lib.auxilium.util.java.annotations.API;

import java.util.Optional;

public interface JanusInfinifronsAPI
{

	@API Optional<Boolean> isUpdateAvailable();

}
