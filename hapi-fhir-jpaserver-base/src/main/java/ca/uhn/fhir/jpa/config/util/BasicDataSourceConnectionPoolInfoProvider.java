package ca.uhn.fhir.jpa.config.util;

import org.apache.commons.dbcp2.BasicDataSource;

import java.util.Optional;

/**
 * Utility class to hide the complexities of obtaining ConnectionPool information
 */
public class BasicDataSourceConnectionPoolInfoProvider implements IConnectionPoolInfoProvider {

	public final BasicDataSource myDataSource;

	public BasicDataSourceConnectionPoolInfoProvider(BasicDataSource theDataSource) {
		myDataSource = theDataSource;
	}

	@Override
	public Optional<Integer> getTotalConnectionSize() {
		return Optional.of( myDataSource.getMaxTotal() );
	}

	@Override
	public Optional<Integer> getActiveConnections() {
		return Optional.of( myDataSource.getNumActive() );
	}

	@Override
	public Optional<Long> getMaxWaitMillis() {
		return Optional.of( myDataSource.getMaxWaitMillis() );
	}
}

