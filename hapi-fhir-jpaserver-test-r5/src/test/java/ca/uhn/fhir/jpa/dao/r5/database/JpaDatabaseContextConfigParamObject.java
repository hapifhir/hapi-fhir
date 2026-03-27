package ca.uhn.fhir.jpa.dao.r5.database;

import ca.uhn.fhir.jpa.embedded.JpaEmbeddedDatabase;

public class JpaDatabaseContextConfigParamObject {

	private final JpaEmbeddedDatabase myJpaEmbeddedDatabase;
	private final String myDialect;

	public JpaDatabaseContextConfigParamObject(JpaEmbeddedDatabase theJpaEmbeddedDatabase, String theDialect) {
		myJpaEmbeddedDatabase = theJpaEmbeddedDatabase;
		myDialect = theDialect;
	}

	public JpaEmbeddedDatabase getJpaEmbeddedDatabase() {
		return myJpaEmbeddedDatabase;
	}

	public String getDialect() {
		return myDialect;
	}
}
