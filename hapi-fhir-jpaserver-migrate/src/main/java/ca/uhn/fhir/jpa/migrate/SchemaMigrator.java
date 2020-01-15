package ca.uhn.fhir.jpa.migrate;

/*-
 * #%L
 * HAPI FHIR JPA Server - Migration
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.jpa.migrate.taskdef.BaseTask;
import org.flywaydb.core.api.MigrationInfo;
import org.flywaydb.core.api.MigrationInfoService;
import org.hibernate.cfg.AvailableSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class SchemaMigrator {
	private static final Logger ourLog = LoggerFactory.getLogger(SchemaMigrator.class);
	public static final String HAPI_FHIR_MIGRATION_TABLENAME = "FLY_HFJ_MIGRATION";
	private final DataSource myDataSource;
	private final boolean mySkipValidation;
	private final String myMigrationTableName;
	private final List<BaseTask> myMigrationTasks;
	private boolean myDontUseFlyway;
	private boolean myOutOfOrderPermitted;
	private DriverTypeEnum myDriverType;

	/**
	 * Constructor
	 */
	public SchemaMigrator(String theMigrationTableName, DataSource theDataSource, Properties jpaProperties, List<BaseTask> theMigrationTasks) {
		myDataSource = theDataSource;
		myMigrationTableName = theMigrationTableName;
		myMigrationTasks = theMigrationTasks;

		if (jpaProperties.containsKey(AvailableSettings.HBM2DDL_AUTO) && "update".equals(jpaProperties.getProperty(AvailableSettings.HBM2DDL_AUTO))) {
			mySkipValidation = true;
		} else {
			mySkipValidation = false;
		}
	}

	public void setDontUseFlyway(boolean theDontUseFlyway) {
		myDontUseFlyway = theDontUseFlyway;
	}

	public void setOutOfOrderPermitted(boolean theOutOfOrderPermitted) {
		myOutOfOrderPermitted = theOutOfOrderPermitted;
	}

	public void validate() {
		if (mySkipValidation) {
			ourLog.warn("Database running in hibernate auto-update mode.  Skipping schema validation.");
			return;
		}
		try (Connection connection = myDataSource.getConnection()) {
			Optional<MigrationInfoService> migrationInfo = newMigrator().getMigrationInfo();
			if (migrationInfo.isPresent()) {
				if (migrationInfo.get().pending().length > 0) {

					String url = connection.getMetaData().getURL();
					throw new ConfigurationException("The database schema for " + url + " is out of date.  " +
						"Current database schema version is " + getCurrentVersion(migrationInfo.get()) + ".  Schema version required by application is " +
						getLastVersion(migrationInfo.get()) + ".  Please run the database migrator.");
				}
				ourLog.info("Database schema confirmed at expected version " + getCurrentVersion(migrationInfo.get()));
			}
		} catch (SQLException e) {
			throw new ConfigurationException("Unable to connect to " + myDataSource.toString(), e);
		}
	}

	public void migrate() {
		if (mySkipValidation) {
			ourLog.warn("Database running in hibernate auto-update mode.  Skipping schema migration.");
			return;
		}
		newMigrator().migrate();
	}

	private BaseMigrator newMigrator() {
		BaseMigrator migrator;
		if (myDontUseFlyway) {
			migrator = new TaskOnlyMigrator();
			migrator.setDriverType(myDriverType);
			migrator.setDataSource(myDataSource);
		} else {
			migrator = new FlywayMigrator(myMigrationTableName, myDataSource, myDriverType);
			migrator.setOutOfOrderPermitted(myOutOfOrderPermitted);
		}
		migrator.addTasks(myMigrationTasks);
		return migrator;
	}

	private String getCurrentVersion(MigrationInfoService theMigrationInfo) {
		MigrationInfo migrationInfo = theMigrationInfo.current();
		if (migrationInfo == null) {
			return "unknown";
		}
		return migrationInfo.getVersion().toString();
	}

	private String getLastVersion(MigrationInfoService theMigrationInfo) {
		MigrationInfo[] pending = theMigrationInfo.pending();
		if (pending.length > 0) {
			return pending[pending.length - 1].getVersion().toString();
		}
		return "unknown";
	}

	public void setDriverType(DriverTypeEnum theDriverType) {
		myDriverType = theDriverType;
	}
}
