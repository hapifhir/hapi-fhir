package ca.uhn.fhir.jpa.model.dialect;

import org.hibernate.dialect.DatabaseVersion;

public class HapiFhirPostgresDialect extends HapiFhirPostgres94Dialect {

	public HapiFhirPostgresDialect() {
		super(DatabaseVersion.make(11, 0, 0));
	}

}
