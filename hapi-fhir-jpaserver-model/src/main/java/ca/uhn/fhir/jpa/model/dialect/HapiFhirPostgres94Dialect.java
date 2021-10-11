package ca.uhn.fhir.jpa.model.dialect;

import org.hibernate.dialect.PostgreSQL94Dialect;

import java.sql.Types;

/**
 * This dialect is recommended when using HAPI FHIR JPA on Postgresql database.
 */
public class HapiFhirPostgres94Dialect extends PostgreSQL94Dialect {

	public HapiFhirPostgres94Dialect() {
		super();
		registerColumnType( Types.CLOB, "oid" );
	}

}
