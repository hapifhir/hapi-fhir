package ca.uhn.fhir.jpa.migrate.taskdef.containertests;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.taskdef.BaseTask;
import ca.uhn.fhir.jpa.migrate.tasks.api.BaseMigrationTasks;
import ca.uhn.fhir.jpa.migrate.tasks.api.Builder;

import java.sql.SQLException;
import java.util.LinkedList;

/**
 * Mixin for a migration task test suite
 */
public interface BaseMigrationTaskTestSuite {
	Support getSupport();

	class Support {
		final TaskExecutor myTaskExecutor;
		final Builder myBuilder;
		final DriverTypeEnum.ConnectionProperties myConnectionProperties;

		public static Support supportFrom(DriverTypeEnum.ConnectionProperties theConnectionProperties) {
			return new Support(theConnectionProperties);
		}

		Support(DriverTypeEnum.ConnectionProperties theConnectionProperties) {
			myConnectionProperties = theConnectionProperties;
			myTaskExecutor = new TaskExecutor(theConnectionProperties);
			myBuilder = new Builder("1.0", myTaskExecutor);
		}

		public Builder getBuilder() {
			return myBuilder;
		}

		public void executeAndClearPendingTasks() throws SQLException {
			myTaskExecutor.flushPendingTasks();
		}

		public DriverTypeEnum.ConnectionProperties getConnectionProperties() {
			return myConnectionProperties;
		}
	}


	/**
	 * Collect and execute the tasks from the Builder
	 */
	class TaskExecutor implements BaseMigrationTasks.IAcceptsTasks {
		final DriverTypeEnum.ConnectionProperties myConnectionProperties;
		final LinkedList<BaseTask> myTasks = new LinkedList<>();

		TaskExecutor(DriverTypeEnum.ConnectionProperties theConnectionProperties) {
			myConnectionProperties = theConnectionProperties;
		}

		/**
		 * Receive a task from the Builder
		 */
		@Override
		public void addTask(BaseTask theTask) {
			myTasks.add(theTask);
		}

		/**
		 * Remove and execute each task in the list.
		 */
		public void flushPendingTasks() throws SQLException {
			while (!myTasks.isEmpty()) {
				executeTask(myTasks.removeFirst());
			}
		}

		void executeTask(BaseTask theTask) throws SQLException {
			theTask.setDriverType(myConnectionProperties.getDriverType());
			theTask.setConnectionProperties(myConnectionProperties);
			theTask.execute();
		}

	}

}
