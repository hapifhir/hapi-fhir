package ca.uhn.fhir.jpa.model.dialect;

import org.hibernate.dialect.DatabaseVersion;
import org.hibernate.dialect.OracleDialect;

/**
 * Dialect for Oracle database.
 * Minimum version: 12.2 (Oracle 12c R2)
 */
public class HapiFhirOracleDialect extends OracleDialect {

	public HapiFhirOracleDialect() {
		super(DatabaseVersion.make(12, 2));
	}
}
