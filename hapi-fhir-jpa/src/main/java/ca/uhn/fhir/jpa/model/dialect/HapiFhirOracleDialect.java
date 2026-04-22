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
import org.hibernate.boot.model.TypeContributions;
import org.hibernate.dialect.OracleDialect;
import org.hibernate.engine.jdbc.dialect.spi.DialectResolutionInfo;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.SqlTypes;
import org.hibernate.type.descriptor.jdbc.JdbcType;
import org.hibernate.type.descriptor.jdbc.spi.JdbcTypeRegistry;

import java.sql.Types;

/**
 * Dialect for Oracle database.
 * Minimum version: 12.2 (Oracle 12c R2)
 */
public class HapiFhirOracleDialect extends OracleDialect implements IHapiFhirDialect {

	public HapiFhirOracleDialect() {
		super();
	}

	public HapiFhirOracleDialect(DialectResolutionInfo info) {
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
		return DriverTypeEnum.ORACLE_12C;
	}

	@Override
	public int getPreferredSqlTypeCodeForBoolean() {
		// Use Types.BIT instead of native Oracle 23 BOOLEAN type to maintain
		// compatibility with existing NUMERIC(1,0) schema and match behavior
		// of Oracle versions < 23
		return Types.BIT;
	}

	@Override
	public void contributeTypes(TypeContributions typeContributions, ServiceRegistry serviceRegistry) {
		super.contributeTypes(typeContributions, serviceRegistry);

		// What follows is necessary for Oracle 23+ which would otherwise try to use
		// native BOOLEAN type that causes conversion issues with existing
		// NUMERIC(1,0) schemas.
		JdbcTypeRegistry jdbcTypeRegistry =
				typeContributions.getTypeConfiguration().getJdbcTypeRegistry();

		// Use TINYINT type descriptor which handles Boolean <-> Integer conversion
		JdbcType tinyIntType = jdbcTypeRegistry.getDescriptor(SqlTypes.TINYINT);
		jdbcTypeRegistry.addDescriptor(SqlTypes.BOOLEAN, tinyIntType);
	}
}
