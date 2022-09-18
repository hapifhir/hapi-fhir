package ca.uhn.fhir.jpa.migrate;

import ca.uhn.fhir.jpa.migrate.taskdef.BaseTask;

import java.util.ArrayList;
import java.util.List;

public class MigrationResult {
	public int changes = 0;
	public final List<BaseTask.ExecutedStatement> executedStatements = new ArrayList<>();
	public final List<BaseTask> succeededTasks = new ArrayList<>();
	public final List<BaseTask> failedTasks = new ArrayList<>();

	public String summary() {
		return String.format("Completed executing %s migration tasks: %s succeeded, %s failed.  %s changes were applied to the database.",
			succeededTasks.size() + failedTasks.size(),
			succeededTasks.size(),
			failedTasks.size(),
			changes);
	}
}
