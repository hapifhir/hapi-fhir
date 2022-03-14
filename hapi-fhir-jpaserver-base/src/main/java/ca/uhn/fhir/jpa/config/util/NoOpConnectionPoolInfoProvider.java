package ca.uhn.fhir.jpa.config.util;

import java.util.Optional;

/**
 * ConnectionPoolInfoProvider instantiated when no other can be configured
 */
public class NoOpConnectionPoolInfoProvider implements IConnectionPoolInfoProvider {

	@Override
	public Optional<Integer> getTotalConnectionSize()  { return Optional.empty(); }

	@Override
	public Optional<Integer> getActiveConnections()  { return Optional.empty(); }

	@Override
	public Optional<Long> getMaxWaitMillis() { return Optional.empty(); }

}

