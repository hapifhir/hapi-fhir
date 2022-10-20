package ca.uhn.fhir.jpa.migrate;

import ca.uhn.fhir.jpa.migrate.taskdef.BaseTask;
import org.flywaydb.core.api.executor.Context;
import org.flywaydb.core.api.executor.MigrationExecutor;

import java.sql.SQLException;

public class HapiFhirMigrationExecutor implements MigrationExecutor {
	private final BaseTask myTask;

	public HapiFhirMigrationExecutor(BaseTask theTask) {
		myTask = theTask;
	}

	@Override
	public void execute(Context context) throws SQLException {
		myTask.execute();
	}

	@Override
	public boolean canExecuteInTransaction() {
		return true;
	}

	@Override
	public boolean shouldExecute() {
		return true;
	}
}
