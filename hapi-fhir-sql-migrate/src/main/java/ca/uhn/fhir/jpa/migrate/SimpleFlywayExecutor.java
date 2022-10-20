package ca.uhn.fhir.jpa.migrate;

import javax.sql.DataSource;

public class SimpleFlywayExecutor {
	private final DataSource myDataSource;
	private final DriverTypeEnum myH2Embedded;
	private final String myTableName;

	public SimpleFlywayExecutor(DataSource theDataSource, DriverTypeEnum theH2Embedded, String theTableName) {
		myDataSource = theDataSource;
		myH2Embedded = theH2Embedded;
		myTableName = theTableName;
	}

	public void createMigrationTableIfRequired() {
		// FIXME KHS
	}

	public void migrate(MigrationTaskList theTaskList) {
		// FIXME KHS
	}

	public void validate() {
		// FIXME KHS
	}

	public void dropTable() {
		// FIXME KHS
	}
}
