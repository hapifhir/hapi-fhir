package ca.uhn.fhir.jpa.model.dialect;

import org.hibernate.dialect.DatabaseVersion;
import org.hibernate.dialect.MariaDBDialect;

import javax.xml.crypto.Data;

/**
 * Dialect for MySQL database.
 * Minimum version: 10.11.5
 */
public class HapiFhirMariaDBDialect extends MariaDBDialect {

	public HapiFhirMariaDBDialect() {
		super(DatabaseVersion.make(10, 11, 5));
	}

}
