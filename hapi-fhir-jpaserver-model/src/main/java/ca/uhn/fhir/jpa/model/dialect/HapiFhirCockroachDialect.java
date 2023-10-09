package ca.uhn.fhir.jpa.model.dialect;

import org.hibernate.dialect.CockroachDialect;
import org.hibernate.dialect.DatabaseVersion;

/**
 * Dialect for CockroachDB database.
 * Minimum version: 21.1
 */
public class HapiFhirCockroachDialect extends CockroachDialect {

	public HapiFhirCockroachDialect() {
		super(DatabaseVersion.make(21, 1 ));
	}


}
