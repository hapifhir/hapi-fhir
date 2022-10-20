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

import org.hibernate.cfg.AvailableSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class SchemaMigrator {
	public static final String HAPI_FHIR_MIGRATION_TABLENAME = "FLY_HFJ_MIGRATION";
	private static final Logger ourLog = LoggerFactory.getLogger(SchemaMigrator.class);
	private final String mySchemaName;
	private final DataSource myDataSource;
	private final boolean mySkipValidation;
	private final SimpleFlywayExecutor mySimpleFlywayExecutor;
	private final String myMigrationTableName;
	private final MigrationTaskList myMigrationTasks;
	private DriverTypeEnum myDriverType;
	private List<IHapiMigrationCallback> myCallbacks = Collections.emptyList();

	/**
	 * Constructor
	 */
	public SchemaMigrator(String theSchemaName, String theMigrationTableName, DataSource theDataSource, Properties jpaProperties, MigrationTaskList theMigrationTasks, SimpleFlywayExecutor theSimpleFlywayExecutor) {
		mySchemaName = theSchemaName;
		myDataSource = theDataSource;
		myMigrationTableName = theMigrationTableName;
		myMigrationTasks = theMigrationTasks;

		mySkipValidation = jpaProperties.containsKey(AvailableSettings.HBM2DDL_AUTO) && "update".equals(jpaProperties.getProperty(AvailableSettings.HBM2DDL_AUTO));
		mySimpleFlywayExecutor = theSimpleFlywayExecutor;
	}

	public void validate() {
		if (mySkipValidation) {
			ourLog.warn("Database running in hibernate auto-update mode.  Skipping schema validation.");
			return;
		}
		mySimpleFlywayExecutor.validate();
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
		return migrator;
	}

	public void setDriverType(DriverTypeEnum theDriverType) {
		myDriverType = theDriverType;
	}

	public void setCallbacks(List<IHapiMigrationCallback> theCallbacks) {
		myCallbacks = theCallbacks;
	}

	public void createMigrationTableIfRequired() {
		mySimpleFlywayExecutor.createMigrationTableIfRequired();
	}
}
