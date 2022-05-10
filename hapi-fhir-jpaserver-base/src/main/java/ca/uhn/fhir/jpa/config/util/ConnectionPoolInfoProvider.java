package ca.uhn.fhir.jpa.config.util;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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



