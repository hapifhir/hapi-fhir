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

import ca.uhn.fhir.jpa.migrate.taskdef.BaseTask;
import ca.uhn.fhir.jpa.migrate.taskdef.InitializeSchemaTask;
import com.google.common.annotations.VisibleForTesting;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfoService;
import org.flywaydb.core.api.callback.Callback;
import org.flywaydb.core.api.migration.JavaMigration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FlywayMigrator extends BaseMigrator {

	private static final Logger ourLog = LoggerFactory.getLogger(FlywayMigrator.class);

	private final String myMigrationTableName;
	private final List<FlywayMigrationTask> myTasks = new ArrayList<>();

	public FlywayMigrator(String theMigrationTableName, DataSource theDataSource, DriverTypeEnum theDriverType) {
		this(theMigrationTableName);
		setDataSource(theDataSource);
		setDriverType(theDriverType);
	}

	public FlywayMigrator(String theMigrationTableName) {
		myMigrationTableName = theMigrationTableName;
	}

	public void addTask(BaseTask theTask) {
		myTasks.add(new FlywayMigrationTask(theTask, this));
	}

	@Override
	public void migrate() {
		try (DriverTypeEnum.ConnectionProperties connectionProperties = getDriverType().newConnectionProperties(getDataSource())) {
			Flyway flyway = initFlyway(connectionProperties);
			flyway.repair();
			flyway.migrate();
			if (isDryRun()) {
				StringBuilder statementBuilder = buildExecutedStatementsString();
				ourLog.info("SQL that would be executed:\n\n***********************************\n{}***********************************", statementBuilder);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	private Flyway initFlyway(DriverTypeEnum.ConnectionProperties theConnectionProperties) {
		Flyway flyway = Flyway.configure()
			.table(myMigrationTableName)
			.dataSource(theConnectionProperties.getDataSource())
			.baselineOnMigrate(true)
			// By default, migrations are allowed to be run out of order.  You can enforce strict order by setting strictOrder=true.
			.outOfOrder(!isStrictOrder())
			.javaMigrations(myTasks.toArray(new JavaMigration[0]))
			.callbacks(getCallbacks().toArray(new Callback[0]))
			.load();
		for (FlywayMigrationTask task : myTasks) {
			task.setConnectionProperties(theConnectionProperties);
		}
		return flyway;
	}

	@Override
	public void addTasks(List<BaseTask> theTasks) {
		if ("true".equals(System.getProperty("unit_test_mode"))) {
			theTasks.stream().filter(task -> task instanceof InitializeSchemaTask).forEach(this::addTask);
		} else {
			theTasks.forEach(this::addTask);
		}
	}

	@Override
	public Optional<MigrationInfoService> getMigrationInfo() {
		if (getDriverType() == null) {
			return Optional.empty();
		}
		try (DriverTypeEnum.ConnectionProperties connectionProperties = getDriverType().newConnectionProperties(getDataSource())) {
			Flyway flyway = initFlyway(connectionProperties);
			return Optional.of(flyway.info());
		}
	}

	@VisibleForTesting
	public void removeAllTasksForUnitTest() {
		myTasks.clear();
	}
}
