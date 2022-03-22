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

