package ca.uhn.fhir.jpa.model.dialect;

import org.hibernate.dialect.PostgreSQL94Dialect;

import java.sql.Types;

/**
 * This
 */
public class HapiFhirPostgres91Dialect extends PostgreSQL94Dialect {

	public HapiFhirPostgres91Dialect() {
		super();
		registerColumnType( Types.CLOB, "oid" );
	}

}
