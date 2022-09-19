package ca.uhn.fhir.jpa.migrate;

public class HapiMigrationException extends RuntimeException {
	private MigrationResult myResult;

	public HapiMigrationException(String theMessage) {
		super(theMessage);
	}
	public HapiMigrationException(String theMessage, Exception theException) {
		super(theMessage, theException);
	}

    public HapiMigrationException(String theMessage, MigrationResult theResult, Exception theException) {
		super(theMessage, theException);
		myResult = theResult;
    }

	public MigrationResult getResult() {
		return myResult;
	}
}
