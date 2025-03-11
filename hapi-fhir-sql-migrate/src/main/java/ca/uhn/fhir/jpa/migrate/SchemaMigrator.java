/*-
 * #%L
 * HAPI FHIR Server - SQL Migration
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.migrate;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.i18n.Msg;
import org.hibernate.cfg.AvailableSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import javax.sql.DataSource;

/**
 * The SchemaMigrator class is responsible for managing and executing database schema migrations during standard bootup.
 * It provides methods to validate the schema version and migrate the schema if necessary.
 *
 * This supports all the configuation needed to run via the CLI or via a bootup migrator. Specifically
 * 1. Dry Run
 * 2. Enable Heavyweight Tasks
 * 3. Task Skipping
 *
 */
public class SchemaMigrator {
	public static final String HAPI_FHIR_MIGRATION_TABLENAME = "FLY_HFJ_MIGRATION";
	private static final Logger ourLog = LoggerFactory.getLogger(SchemaMigrator.class);
	/**
	 * The Schema Name
	 */
	private final String mySchemaName;
	/**
	 * The datasource to connect to the Database with.
	 */
	private final DataSource myDataSource;
	/**
	 * Whether to skip validation of the schema
	 */
	private final boolean mySkipValidation;
	/**
	 * See {@link HapiMigrator#isRunHeavyweightSkippableTasks()}. Enables or disables Optional Heavyweight migrations
	 */
	private final boolean myRunHeavyweightMigrationTasks;
	/**
	 * The name of the table in which migrations are tracked.
	 */
	private final String myMigrationTableName;
	/**
	 * The actual task list, which will be filtered during construction, and having all Tasks enumerated in `theMigrationTasksToSkip` removed before execution.
	 */
	private final MigrationTaskList myMigrationTasks;
	/**
	 * See {@link HapiMigrator#isDryRun()}
	 */
	private final boolean myDryRun;

	private DriverTypeEnum myDriverType;
	/**
	 * Inventory of callbacks to invoke for pre and post execution
	 */
	private List<IHapiMigrationCallback> myCallbacks = Collections.emptyList();

	private final HapiMigrationStorageSvc myHapiMigrationStorageSvc;

	/**
	 * Constructor
	 */
	public SchemaMigrator(
			String theSchemaName,
			String theMigrationTableName,
			DataSource theDataSource,
			boolean theEnableHeavyweighTtasks,
			String theMigrationTasksToSkip,
			boolean theDryRun,
			Properties jpaProperties,
			MigrationTaskList theMigrationTasks,
			HapiMigrationStorageSvc theHapiMigrationStorageSvc) {
		mySchemaName = theSchemaName;
		myDataSource = theDataSource;
		myMigrationTableName = theMigrationTableName;
		myMigrationTasks = theMigrationTasks;
		myRunHeavyweightMigrationTasks = theEnableHeavyweighTtasks;
		mySkipValidation = jpaProperties.containsKey(AvailableSettings.HBM2DDL_AUTO)
				&& "update".equals(jpaProperties.getProperty(AvailableSettings.HBM2DDL_AUTO));
		myHapiMigrationStorageSvc = theHapiMigrationStorageSvc;
		// Skip the skipped versions here.
		myMigrationTasks.setDoNothingOnSkippedTasks(theMigrationTasksToSkip);
		myDryRun = theDryRun;
	}

	/**
	 * Temporary Dummy Constructor to remove once CDR side merges.
	 */
	public SchemaMigrator(
			String theSchemaName,
			String theMigrationTableName,
			DataSource theDataSource,
			Properties jpaProperties,
			MigrationTaskList theMigrationTasks,
			HapiMigrationStorageSvc theHapiMigrationStorageSvc) {
		mySchemaName = theSchemaName;
		myDataSource = theDataSource;
		myMigrationTableName = theMigrationTableName;
		myMigrationTasks = theMigrationTasks;
		myRunHeavyweightMigrationTasks = false;
		mySkipValidation = jpaProperties.containsKey(AvailableSettings.HBM2DDL_AUTO)
				&& "update".equals(jpaProperties.getProperty(AvailableSettings.HBM2DDL_AUTO));
		myHapiMigrationStorageSvc = theHapiMigrationStorageSvc;
		// Skip the skipped versions here.
		myDryRun = false;
	}

	public void validate() {
		if (mySkipValidation) {
			ourLog.warn("Database running in hibernate auto-update mode.  Skipping schema validation.");
			return;
		}
		try (Connection connection = myDataSource.getConnection()) {
			MigrationTaskList unappliedMigrations = myHapiMigrationStorageSvc.diff(myMigrationTasks);

			// remove skippable tasks
			MigrationTaskList unappliedUnskippable = unappliedMigrations.getUnskippableTasks();

			if (unappliedUnskippable.size() > 0) {
				String url = connection.getMetaData().getURL();
				throw new ConfigurationException(Msg.code(27) + "The database schema for " + url + " is out of date.  "
						+ "Current database schema version is "
						+ myHapiMigrationStorageSvc.getLatestAppliedVersion()
						+ ".  Schema version required by application is " + unappliedMigrations.getLastVersion()
						+ ".  Please run the database migrator.");
			}
			ourLog.info("Database schema confirmed at expected version "
					+ myHapiMigrationStorageSvc.getLatestAppliedVersion());
		} catch (SQLException e) {
			throw new ConfigurationException(Msg.code(28) + "Unable to connect to " + myDataSource, e);
		}
	}

	public MigrationResult migrate() {
		if (mySkipValidation) {
			ourLog.warn("Database running in hibernate auto-update mode.  Skipping schema migration.");
			return null;
		}
		try {
			ourLog.info("Migrating " + mySchemaName);
			MigrationResult retval = newMigrator().migrate();
			ourLog.info(mySchemaName + " migrated successfully: {}", retval.summary());
			return retval;
		} catch (Exception e) {
			ourLog.error("Failed to migrate " + mySchemaName, e);
			throw e;
		}
	}

	private HapiMigrator newMigrator() {
		HapiMigrator migrator;
		migrator = new HapiMigrator(myMigrationTableName, myDataSource, myDriverType);
		migrator.addTasks(myMigrationTasks);
		migrator.setCallbacks(myCallbacks);
		migrator.setDryRun(myDryRun);
		migrator.setRunHeavyweightSkippableTasks(myRunHeavyweightMigrationTasks);
		return migrator;
	}

	public void setDriverType(DriverTypeEnum theDriverType) {
		myDriverType = theDriverType;
	}

	public void setCallbacks(List<IHapiMigrationCallback> theCallbacks) {
		myCallbacks = theCallbacks;
	}

	public boolean createMigrationTableIfRequired() {
		return myHapiMigrationStorageSvc.createMigrationTableIfRequired();
	}
}
