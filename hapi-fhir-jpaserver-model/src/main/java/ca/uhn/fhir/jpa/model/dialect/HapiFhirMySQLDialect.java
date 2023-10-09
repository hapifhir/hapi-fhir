package ca.uhn.fhir.jpa.model.dialect;

import org.hibernate.dialect.DatabaseVersion;
import org.hibernate.dialect.MySQLDialect;

/**
 * Dialect for MySQL database.
 * Minimum version: 5.7
 */
public class HapiFhirMySQLDialect extends MySQLDialect {

	public HapiFhirMySQLDialect() {
		super(DatabaseVersion.make(5,7));
	}

}
