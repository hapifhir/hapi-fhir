package ca.uhn.fhir.jpa.migrate;

import org.springframework.dao.DataAccessException;

public class HapiMigrationException extends RuntimeException {
	public HapiMigrationException(String theMessage, DataAccessException theException) {
		super(theMessage, theException);
	}
}
