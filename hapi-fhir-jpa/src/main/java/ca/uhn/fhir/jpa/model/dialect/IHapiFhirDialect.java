/*-
 * #%L
 * HAPI FHIR JPA Model
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
import jakarta.annotation.Nullable;

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

	/**
	 * Returns a String template for a subselect which unpacks a JSON array of resource IDs.
	 * The placeholder {@code %s} marks where the list of IDs goes.
	 * Returns null when the database has no usable JSON function and the IN list should be used.
	 */
	@Nullable
	default String getIdListJsonSubselectTemplate() {
		return null;
	}

	/**
	 * Returns <code>true</code> if the JSON array bound for {@link #getIdListJsonSubselectTemplate()}
	 * must be wrapped as a CLOB bind value rather than passed as a plain <code>String</code>. Oracle
	 * overrides this to <code>true</code> because it binds a plain <code>String</code> as a
	 * <code>VARCHAR2</code>, which is limited to 4,000 bytes by default and raises
	 * <code>ORA-01461</code> beyond that.
	 *
	 * @since 8.14.0
	 */
	default boolean bindsIdListJsonAsClob() {
		return false;
	}
}
