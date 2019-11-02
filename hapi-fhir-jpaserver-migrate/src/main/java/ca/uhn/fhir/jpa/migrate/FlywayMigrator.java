package ca.uhn.fhir.jpa.migrate;

/*-
 * #%L
 * HAPI FHIR JPA Server - Migration
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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
import org.apache.commons.dbcp2.BasicDataSource;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfoService;
import org.flywaydb.core.api.migration.JavaMigration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Driver;
import java.util.ArrayList;
import java.util.List;

public class FlywayMigrator {

	private static final Logger ourLog = LoggerFactory.getLogger(FlywayMigrator.class);

	private DriverTypeEnum myDriverType;
	private String myConnectionUrl;
	private String myUsername;
	private String myPassword;
	// FIXME KHS
//	private String myDefaultSchema;
	private List<FlywayMigration> myTasks = new ArrayList<>();
	private boolean myDryRun;
	private boolean myNoColumnShrink;

	public FlywayMigrator() {}

	// FIXME KHS test with nonexistent database
	public FlywayMigrator(BasicDataSource theDataSource) {
		myConnectionUrl = theDataSource.getUrl();
		myUsername = theDataSource.getUsername();
		myPassword = theDataSource.getPassword();
//		myDefaultSchema = theDataSource.getDefaultSchema();
		// FIXME KHS
//		if (myDefaultSchema == null) {
//			myDefaultSchema = "kentest";
//		}
		String driverClassName = theDataSource.getDriverClassName();
		if (driverClassName == null) {
			ourLog.error(this.getClass().getSimpleName() + " constructed without a database driver");
		} else {
			myDriverType = DriverTypeEnum.fromDriverClassName(driverClassName);
			if (myDriverType == null) {
				ourLog.error("Unknown driver class " + driverClassName);
			}
		}
	}

	public void setDriverType(DriverTypeEnum theDriverType) {
		myDriverType = theDriverType;
	}

	public void setConnectionUrl(String theConnectionUrl) {
		myConnectionUrl = theConnectionUrl;
	}

	public void setUsername(String theUsername) {
		myUsername = theUsername;
	}

	public void setPassword(String thePassword) {
		myPassword = thePassword;
	}

	public void addTask(BaseTask<?> theTask) {
		myTasks.add(new FlywayMigration(theTask, this));
	}

	public void setDryRun(boolean theDryRun) {
		myDryRun = theDryRun;
	}

	public void migrate() {
		try (DriverTypeEnum.ConnectionProperties connectionProperties = myDriverType.newConnectionProperties(myConnectionUrl, myUsername, myPassword)) {
			Flyway flyway = initFlyway(connectionProperties);
			flyway.migrate();
		} catch (Exception e) {
			throw new ConfigurationException("Failed to migrate schema", e);
		}
	}

	private Flyway initFlyway(DriverTypeEnum.ConnectionProperties theConnectionProperties) {
		// FIXME KHS instantiate from database, not this other stuff
		// FIXME KHS ensure we have a default schema
// FIXME KHS succeeds from zero.  But then second time fails with error.  It's as though the flyway db isn't persisting.... or maybe a checksum issue...?
		Flyway flyway = Flyway.configure()
			// FIXME KHS required?
//			.schemas(myDefaultSchema)
			.dataSource(myConnectionUrl, myUsername, myPassword)
			.baselineOnMigrate(true)
			.javaMigrations(myTasks.toArray(new JavaMigration[0]))
			.load();
		for (FlywayMigration task : myTasks) {
			task.setConnectionProperties(theConnectionProperties);
		}
		return flyway;
	}

	public void addTasks(List<BaseTask<?>> theTasks) {
		theTasks.forEach(this::addTask);
	}

	public void setNoColumnShrink(boolean theNoColumnShrink) {
		myNoColumnShrink = theNoColumnShrink;
	}

	public DriverTypeEnum getDriverType() {
		return myDriverType;
	}

	public String getConnectionUrl() {
		return myConnectionUrl;
	}

	public String getUsername() {
		return myUsername;
	}

	public String getPassword() {
		return myPassword;
	}

	public boolean isDryRun() {
		return myDryRun;
	}

	public boolean isNoColumnShrink() {
		return myNoColumnShrink;
	}

	public MigrationInfoService getMigrationInfo() {
		if (myDriverType == null) {
			return null;
		}
		try (DriverTypeEnum.ConnectionProperties connectionProperties = myDriverType.newConnectionProperties(myConnectionUrl, myUsername, myPassword)) {
			Flyway flyway = initFlyway(connectionProperties);
			return flyway.info();
		}
	}
}
