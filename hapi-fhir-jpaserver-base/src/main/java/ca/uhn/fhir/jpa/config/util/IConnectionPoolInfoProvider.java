package ca.uhn.fhir.jpa.config.util;


import java.util.Optional;

public interface IConnectionPoolInfoProvider {
	Optional<Integer> getTotalConnectionSize();

	Optional<Integer> getActiveConnections();

	Optional<Long> getMaxWaitMillis();
}
