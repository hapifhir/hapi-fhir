/*-
 * #%L
 * HAPI FHIR JPA Model
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.logging;

import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.engine.jdbc.spi.SqlStatementLogger;
import org.hibernate.service.spi.ServiceContributor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FilteringSqlLoggerImplContributor implements ServiceContributor {
	private static final Logger logger = LoggerFactory.getLogger(FilteringSqlLoggerImplContributor.class);

	@Override
	public void contribute(StandardServiceRegistryBuilder serviceRegistryBuilder) {
		logger.info("Adding service: SqlStatementFilteringLogger");

		serviceRegistryBuilder.addService(
				SqlStatementLogger.class, new SqlStatementFilteringLogger(SqlLoggerFilteringUtil.getInstance()));
	}
}
