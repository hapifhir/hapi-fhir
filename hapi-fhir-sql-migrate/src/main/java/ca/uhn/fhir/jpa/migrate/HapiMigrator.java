package ca.uhn.fhir.jpa.migrate;

/*-
 * #%L
 * HAPI FHIR Server - SQL Migration
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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
import ca.uhn.fhir.util.StopWatch;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.Validate;
import org.flywaydb.core.api.callback.Callback;
import org.flywaydb.core.api.callback.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * This class is an alternative to {@link HapiMigrator ). It doesn't use Flyway, but instead just
 * executes all tasks.
 */
public class HapiMigrator {

	private static final Logger ourLog = LoggerFactory.getLogger(HapiMigrator.class);
	private List<BaseTask> myTasks = new ArrayList<>();
	private final List<BaseTask.ExecutedStatement> myExecutedStatements = new ArrayList<>();
	private boolean myDryRun;
	private boolean myNoColumnShrink;
	private final DriverTypeEnum myDriverType;
	private final DataSource myDataSource;
	private final HapiMigrationStorageSvc myHapiMigrationStorageSvc;
	private List<Callback> myCallbacks = Collections.emptyList();
	private int myChangesCount;

	public HapiMigrator(DriverTypeEnum theDriverType, DataSource theDataSource, String theMigrationTableName) {
		myDriverType = theDriverType;
		myDataSource = theDataSource;
		myHapiMigrationStorageSvc = new HapiMigrationStorageSvc(new HapiMigrationDao(theDataSource, theMigrationTableName));
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


	protected StringBuilder buildExecutedStatementsString() {
		StringBuilder statementBuilder = new StringBuilder();
		String lastTable = null;
		for (BaseTask.ExecutedStatement next : myExecutedStatements) {
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

	public void migrate() {
		ourLog.info("Loaded {} migration tasks", myTasks.size());
		List<BaseTask> newTasks = myHapiMigrationStorageSvc.diff(myTasks);
		ourLog.info("{} of these {} migration tasks are new.  Executing them now.", newTasks.size(), myTasks.size());

		try (DriverTypeEnum.ConnectionProperties connectionProperties = getDriverType().newConnectionProperties(getDataSource())) {

			for (BaseTask next : newTasks) {
				next.setDriverType(getDriverType());
				next.setDryRun(isDryRun());
				next.setNoColumnShrink(isNoColumnShrink());
				next.setConnectionProperties(connectionProperties);

				StopWatch sw = new StopWatch();
				try {
					if (isDryRun()) {
						ourLog.info("Dry run {} {}", next.getMigrationVersion(), next.getDescription());
					} else {
						ourLog.info("Executing {} {}", next.getMigrationVersion(), next.getDescription());
					}
					// WIP KHS replace with different callback probably a BaseTask consumer
					myCallbacks.forEach(action -> action.handle(Event.BEFORE_EACH_MIGRATE, null));
					// WIP KHS break up
					next.execute();
					postExecute(next, sw, true);
					myChangesCount += next.getChangesCount();
					myExecutedStatements.addAll(next.getExecutedStatements());

				} catch (SQLException e) {
					postExecute(next, sw, false);
					String description = next.getDescription();
					if (isBlank(description)) {
						description = next.getClass().getSimpleName();
					}
					String prefix = "Failure executing task \"" + description + "\", aborting! Cause: ";
					throw new HapiMigrationException(Msg.code(47) + prefix + e, e);
				}
			}
		}

		ourLog.info("Completed executing {} migration tasks resulting in {} changes", myTasks.size(), myChangesCount);

		if (isDryRun()) {
			StringBuilder statementBuilder = buildExecutedStatementsString();
			ourLog.info("SQL that would be executed:\n\n***********************************\n{}***********************************", statementBuilder);
		}
	}

	private void postExecute(BaseTask theNext, StopWatch theStopWatch, boolean theSuccess) {
		myHapiMigrationStorageSvc.saveTask(theNext, Math.toIntExact(theStopWatch.getMillis()), theSuccess);
	}

	public void addTasks(List<BaseTask> theMigrationTasks) {
		myTasks.addAll(theMigrationTasks);
	}

	public void addTask(BaseTask theTask) {
		myTasks.add(theTask);
	}

	@Nonnull
	public List<Callback> getCallbacks() {
		return myCallbacks;
	}

	public void setCallbacks(@Nonnull List<Callback> theCallbacks) {
		Validate.notNull(theCallbacks);
		myCallbacks = theCallbacks;
	}

	@VisibleForTesting
	public void removeAllTasksForUnitTest() {
		myTasks.clear();
	}

	public void createMigrationTableIfRequired() {
		myHapiMigrationStorageSvc.createMigrationTableIfRequired();
	}
}
