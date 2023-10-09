package ca.uhn.fhir.jpa.model.dialect;

import org.hibernate.dialect.DatabaseVersion;
import org.hibernate.dialect.SQLServerDialect;

/**
 * Dialect for MS SQL Server database.
 * Minimum version: 12.0 (SQL Server 2014 and Azure SQL Database)
 */
public class HapiFhirSQLServerDialect extends SQLServerDialect {

	public HapiFhirSQLServerDialect() {
		super(DatabaseVersion.make(12));
	}

}
