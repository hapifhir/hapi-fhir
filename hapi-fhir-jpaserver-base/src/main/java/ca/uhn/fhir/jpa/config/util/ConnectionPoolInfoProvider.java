package ca.uhn.fhir.jpa.config.util;

import net.ttddyy.dsproxy.support.ProxyDataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Utility to hide complexity involved in obtaining connection pool information
 */
public class ConnectionPoolInfoProvider implements IConnectionPoolInfoProvider {
	private static final Logger ourLog = LoggerFactory.getLogger(ConnectionPoolInfoProvider.class);

	private IConnectionPoolInfoProvider myProvider;


	public ConnectionPoolInfoProvider(DataSource theDataSource) {
		if (theDataSource.getClass().isAssignableFrom(BasicDataSource.class)) {
			myProvider =  new BasicDataSourceConnectionPoolInfoProvider((BasicDataSource) theDataSource);
			return;
		}

		if ( theDataSource.getClass().isAssignableFrom(ProxyDataSource.class)) {
			boolean basiDataSourceWrapped;
			try {
				basiDataSourceWrapped = theDataSource.isWrapperFor(BasicDataSource.class);
				if (basiDataSourceWrapped) {
					BasicDataSource basicDataSource = theDataSource.unwrap(BasicDataSource.class);
					myProvider = new BasicDataSourceConnectionPoolInfoProvider(basicDataSource);
				}
			} catch (SQLException ignored) { }
		}
	}


	@Override
	public Optional<Integer> getTotalConnectionSize() {
		return myProvider == null ? Optional.empty() : myProvider.getTotalConnectionSize();
	}

	@Override
	public Optional<Integer> getActiveConnections() {
		return myProvider == null ? Optional.empty() : myProvider.getActiveConnections();
	}

	@Override
	public Optional<Long> getMaxWaitMillis() {
		return myProvider == null ? Optional.empty() : myProvider.getMaxWaitMillis();
	}
}



