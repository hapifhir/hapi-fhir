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
package ca.uhn.fhir.jpa.model.dialect;

import org.hibernate.dialect.PostgreSQLDialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This dialect is recommended when using HAPI FHIR JPA on Postgresql database.
 *
 * @deprecated Use {@link HapiFhirPostgresDialect} instead
 */
public class HapiFhirPostgres94Dialect extends PostgreSQLDialect {
	private static final Logger ourLog = LoggerFactory.getLogger(HapiFhirPostgres94Dialect.class);

	public HapiFhirPostgres94Dialect() {
		super();
		ourLog.warn("The " + getClass() + " dialect is deprecated and will be removed in a future release. Use "
				+ HapiFhirPostgresDialect.class.getName() + " instead");
	}
}
