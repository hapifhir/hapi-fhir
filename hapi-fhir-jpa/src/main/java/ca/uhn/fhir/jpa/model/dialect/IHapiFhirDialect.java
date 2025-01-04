/*-
 * #%L
 * HAPI FHIR JPA Model
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;

/**
 * HAPI FHIR requires the use of customized Hibernate
 * {@link org.hibernate.dialect.Dialect} classes. We do this because our schema
 * migrator isn't compatible with some features that the built-in dialects
 * use, so we disable those features in the HAPI FHIR dialects. For example,
 * Postgres users shouldn't use the Hibernate
 * {@link org.hibernate.dialect.PostgreSQLDialect} and should instead use the
 * HAPI FHIR {@link HapiFhirPostgresDialect} which extends the former class.
 */
public interface IHapiFhirDialect {

	/**
	 * Provides the HAPI FHIR driver enum associated with this dialect
	 */
	DriverTypeEnum getDriverType();
}
