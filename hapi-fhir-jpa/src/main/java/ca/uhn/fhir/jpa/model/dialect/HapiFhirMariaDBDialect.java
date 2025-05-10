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
import org.hibernate.dialect.MariaDBDialect;
import org.hibernate.engine.jdbc.dialect.spi.DialectResolutionInfo;

/**
 * Dialect for MySQL database.
 * Minimum version: 10.11.5
 */
public class HapiFhirMariaDBDialect extends MariaDBDialect implements IHapiFhirDialect {

	public HapiFhirMariaDBDialect() {
		super();
	}

	public HapiFhirMariaDBDialect(DialectResolutionInfo info) {
		super(info);
	}

	/**
	 * @see HapiFhirH2Dialect#supportsColumnCheck() for an explanation of why we disable this
	 */
	@Override
	public boolean supportsColumnCheck() {
		return false;
	}

	@Override
	public DriverTypeEnum getDriverType() {
		return DriverTypeEnum.MARIADB_10_1;
	}

	/**
	 * @see HapiFhirH2Dialect#supportsColumnCheck() for an explanation of why we disable this
	 */
	@Override
	public String getEnumTypeDeclaration(Class<? extends Enum<?>> enumType) {
		return null;
	}

	/**
	 * @see HapiFhirH2Dialect#supportsColumnCheck() for an explanation of why we disable this
	 */
	@Override
	public String getEnumTypeDeclaration(String name, String[] values) {
		return null;
	}
}
