package de.domisum.janusinfinifrons.api;

import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@API
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JanusInfinifronsAPIFactory
{

	@API public static JanusInfinifronsAPI fromIntercomPort(int port)
	{
		return new HttpJanusInfinifronsAPI(port);
	}

}
