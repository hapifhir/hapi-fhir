package ca.uhn.fhir.jpa.migrate;

import ca.uhn.fhir.jpa.migrate.taskdef.BaseTask;

public interface IHapiMigrationCallback {
	default void preExecution(BaseTask theTask) {}
	default void postExecution(BaseTask theTask) {}
}
