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
import org.hibernate.dialect.SQLServerDialect;
import org.hibernate.engine.jdbc.dialect.spi.DialectResolutionInfo;

/**
 * Dialect for MS SQL Server database.
 * Minimum version: 12.0 (SQL Server 2014 and Azure SQL Database)
 * <p>
 * The HAPI FHIR schema uses plain {@code VARCHAR} columns, but the Microsoft SQL Server JDBC
 * driver sends string parameters as Unicode ({@code NVARCHAR}) by default. The resulting implicit
 * {@code NVARCHAR}-to-{@code VARCHAR} conversions prevent SQL Server from using indexes on those
 * columns and can severely degrade query performance. Always add
 * {@code sendStringParametersAsUnicode=false} to the JDBC connection URL, e.g.:
 * <pre>
 * jdbc:sqlserver://localhost:1433;databaseName=hapi;sendStringParametersAsUnicode=false
 * </pre>
 */
public class HapiFhirSQLServerDialect extends SQLServerDialect implements IHapiFhirDialect {

	public HapiFhirSQLServerDialect() {
		super();
	}

	public HapiFhirSQLServerDialect(DialectResolutionInfo info) {
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
		return DriverTypeEnum.MSSQL_2012;
	}
}
