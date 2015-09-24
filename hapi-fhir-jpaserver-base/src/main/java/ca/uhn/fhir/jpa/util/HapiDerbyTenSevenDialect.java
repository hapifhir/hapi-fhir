package ca.uhn.fhir.jpa.util;

import org.hibernate.dialect.DerbyTenSevenDialect;

/**
 * As of Hibernate 5.0.1, DerbyTenSevenDialect doesn't seem to work when updating
 * the schema, as it tries to create a duplicate schema
 */
public class HapiDerbyTenSevenDialect extends DerbyTenSevenDialect {

	@Override
	public String getQuerySequencesString() {
		return "select SEQUENCENAME from sys.syssequences";
	}

}
