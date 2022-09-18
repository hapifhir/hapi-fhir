package ca.uhn.fhir.jpa.migrate;

public class HapiMigrationException extends RuntimeException {
	public HapiMigrationException(String theMessage) {
		super(theMessage);
	}
	public HapiMigrationException(String theMessage, Exception theException) {
		super(theMessage, theException);
	}
}
