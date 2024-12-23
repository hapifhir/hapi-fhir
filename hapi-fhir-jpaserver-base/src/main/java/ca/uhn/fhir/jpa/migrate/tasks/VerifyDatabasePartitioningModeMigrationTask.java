package ca.uhn.fhir.jpa.migrate.tasks;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.taskdef.BaseTask;
import ca.uhn.fhir.jpa.util.PartitionedIdModeVerificationSvc;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.sql.SQLException;

/**
 * This task verifies that the in-place schema is appropriate for
 * Database Partition Mode (if that mode is enabled), or is appropriate
 * for legacy mode otherwise.
 */
public class VerifyDatabasePartitioningModeMigrationTask extends BaseTask {
	private final boolean myExpectDatabasePartitionMode;

	public VerifyDatabasePartitioningModeMigrationTask(
			String theProductVersion, String theSchemaVersion, boolean theExpectDatabasePartitionMode) {
		super(theProductVersion, theSchemaVersion);
		myExpectDatabasePartitionMode = theExpectDatabasePartitionMode;
	}

	@Override
	public void validate() {
		// nothing
	}

	@Override
	protected void doExecute() throws SQLException {
		DriverTypeEnum.ConnectionProperties cp = getConnectionProperties();
		PartitionedIdModeVerificationSvc.verifySchemaIsAppropriateForDatabasePartitionMode(
				cp, myExpectDatabasePartitionMode);
	}

	/**
	 * Nothing added other than the class name, just to give some kind
	 * of meaningful value. There is one bit of interesting state in this class,
	 * in the {@link #myExpectDatabasePartitionMode} field. But we actually want
	 * this task to keep working even if the wrong flag is passed in, so we
	 * don't include it here.
	 */
	@Override
	protected void generateHashCode(HashCodeBuilder theBuilder) {
		theBuilder.append(getClass().getSimpleName());
	}

	@Override
	protected void generateEquals(EqualsBuilder theBuilder, BaseTask theOtherObject) {
		theBuilder.append(getClass().getSimpleName(), theOtherObject.getClass().getSimpleName());
	}
}
