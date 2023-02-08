package ca.uhn.fhir.jpa.migrate;

/*-
 * #%L
 * HAPI FHIR Server - SQL Migration
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.migrate.dao.HapiMigrationDao;
import ca.uhn.fhir.jpa.migrate.taskdef.BaseTask;
import ca.uhn.fhir.jpa.migrate.taskdef.InitializeSchemaTask;
import ca.uhn.fhir.system.HapiSystemProperties;
import ca.uhn.fhir.util.StopWatch;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class HapiMigrator {

	private static final Logger ourLog = LoggerFactory.getLogger(HapiMigrator.class);
	private final MigrationTaskList myTaskList = new MigrationTaskList();
	private boolean myDryRun;
	private boolean myNoColumnShrink;
	private final DriverTypeEnum myDriverType;
	private final DataSource myDataSource;
	private final HapiMigrationStorageSvc myHapiMigrationStorageSvc;
	private List<IHapiMigrationCallback> myCallbacks = Collections.emptyList();

	public HapiMigrator(String theMigrationTableName, DataSource theDataSource, DriverTypeEnum theDriverType) {
		myDriverType = theDriverType;
		myDataSource = theDataSource;
		myHapiMigrationStorageSvc = new HapiMigrationStorageSvc(new HapiMigrationDao(theDataSource, theDriverType, theMigrationTableName));
	}

	public DataSource getDataSource() {
		return myDataSource;
	}

	public boolean isDryRun() {
		return myDryRun;
	}

	public void setDryRun(boolean theDryRun) {
		myDryRun = theDryRun;
	}

	public boolean isNoColumnShrink() {
		return myNoColumnShrink;
	}

	public void setNoColumnShrink(boolean theNoColumnShrink) {
		myNoColumnShrink = theNoColumnShrink;
	}

	public DriverTypeEnum getDriverType() {
		return myDriverType;
	}


	protected StringBuilder buildExecutedStatementsString(MigrationResult theMigrationResult) {
		StringBuilder statementBuilder = new StringBuilder();
		String lastTable = null;
		for (BaseTask.ExecutedStatement next : theMigrationResult.executedStatements) {
			if (!Objects.equals(lastTable, next.getTableName())) {
				statementBuilder.append("\n\n-- Table: ").append(next.getTableName()).append("\n");
				lastTable = next.getTableName();
			}

			statementBuilder.append(next.getSql()).append(";\n");

			for (Object nextArg : next.getArguments()) {
				statementBuilder.append("  -- Arg: ").append(nextArg).append("\n");
			}
		}
		return statementBuilder;
	}

	/**
	 * Helper method to clear a lock with a given UUID.
	 * @param theUUID the
	 */
	public void clearMigrationLockWithUUID(String theUUID) {
		ourLog.info("Attempting to remove lock entry. [uuid={}]", theUUID);
		boolean success = myHapiMigrationStorageSvc.deleteLockRecord(theUUID);
		if (success) {
			ourLog.info("Successfully removed lock entry. [uuid={}]", theUUID);
		} else {
			ourLog.error("Did not successfully remove lock entry. [uuid={}]", theUUID);
		}
	}

	public MigrationResult migrate() {
		ourLog.info("Loaded {} migration tasks", myTaskList.size());
		MigrationResult retval = new MigrationResult();

		// Lock the migration table so only one server migrates the database at once
		try (HapiMigrationLock ignored = new HapiMigrationLock(myHapiMigrationStorageSvc)) {
			MigrationTaskList newTaskList = myHapiMigrationStorageSvc.diff(myTaskList);
			ourLog.info("{} of these {} migration tasks are new.  Executing them now.", newTaskList.size(), myTaskList.size());

			try (DriverTypeEnum.ConnectionProperties connectionProperties = getDriverType().newConnectionProperties(getDataSource())) {

				newTaskList.forEach(next -> {

					next.setDriverType(getDriverType());
					next.setDryRun(isDryRun());
					next.setNoColumnShrink(isNoColumnShrink());
					next.setConnectionProperties(connectionProperties);

					executeTask(next, retval);
				});
			}
		} catch (Exception e) {
			ourLog.error("Migration failed", e);
			throw e;
		}

		ourLog.info(retval.summary());

		if (isDryRun()) {
			StringBuilder statementBuilder = buildExecutedStatementsString(retval);
			ourLog.info("SQL that would be executed:\n\n***********************************\n{}***********************************", statementBuilder);
		}

		return retval;
	}

	private void executeTask(BaseTask theTask, MigrationResult theMigrationResult) {
		StopWatch sw = new StopWatch();
		try {
			if (isDryRun()) {
				ourLog.info("Dry run {} {}", theTask.getMigrationVersion(), theTask.getDescription());
			} else {
				ourLog.info("Executing {} {}", theTask.getMigrationVersion(), theTask.getDescription());
			}
			preExecute(theTask);
			theTask.execute();
			postExecute(theTask, sw, true);
			theMigrationResult.changes += theTask.getChangesCount();
			theMigrationResult.executedStatements.addAll(theTask.getExecutedStatements());
			theMigrationResult.succeededTasks.add(theTask);
		} catch (SQLException | HapiMigrationException e) {
			theMigrationResult.failedTasks.add(theTask);
			postExecute(theTask, sw, false);
			String description = theTask.getDescription();
			if (isBlank(description)) {
				description = theTask.getClass().getSimpleName();
			}
			String prefix = "Failure executing task \"" + description + "\", aborting! Cause: ";
			throw new HapiMigrationException(Msg.code(47) + prefix + e, theMigrationResult, e);
		}
	}

	private void preExecute(BaseTask theTask) {
		myCallbacks.forEach(action -> action.preExecution(theTask));

	}

	private void postExecute(BaseTask theNext, StopWatch theStopWatch, boolean theSuccess) {
		myHapiMigrationStorageSvc.saveTask(theNext, Math.toIntExact(theStopWatch.getMillis()), theSuccess);
	}

	public void addTasks(Iterable<BaseTask> theMigrationTasks) {
		if (HapiSystemProperties.isUnitTestModeEnabled()) {
			// Tests only need to initialize the schemas. No need to run all the migrations for every test.
			for (BaseTask task : theMigrationTasks) {
				if (task instanceof InitializeSchemaTask) {
					addTask(task);
				}
			}
		} else {
			myTaskList.append(theMigrationTasks);
		}
	}

	public void addTask(BaseTask theTask) {
		myTaskList.add(theTask);
	}

	public void setCallbacks(@Nonnull List<IHapiMigrationCallback> theCallbacks) {
		Validate.notNull(theCallbacks);
		myCallbacks = theCallbacks;
	}

	@VisibleForTesting
	public void removeAllTasksForUnitTest() {
		myTaskList.clear();
	}

	public void createMigrationTableIfRequired() {
		myHapiMigrationStorageSvc.createMigrationTableIfRequired();
	}
}
