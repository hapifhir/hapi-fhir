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
import ca.uhn.fhir.jpa.migrate.taskdef.BaseTask;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.flywaydb.core.api.MigrationInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This class is an alternative to {@link FlywayMigrator). It doesn't use Flyway, but instead just
 * executes all tasks.
 */
public class TaskOnlyMigrator extends BaseMigrator {

	private static final Logger ourLog = LoggerFactory.getLogger(TaskOnlyMigrator.class);
	private List<BaseTask> myTasks = new ArrayList<>();

	@Override
	public void migrate() {
		DriverTypeEnum.ConnectionProperties connectionProperties = getDriverType().newConnectionProperties(getDataSource());

		for (BaseTask next : myTasks) {
			next.setDriverType(getDriverType());
			next.setDryRun(isDryRun());
			next.setNoColumnShrink(isNoColumnShrink());
			next.setConnectionProperties(connectionProperties);

			try {
				if (isDryRun()) {
					ourLog.info("Dry run {} {}", next.getFlywayVersion(), next.getDescription());
				} else {
					ourLog.info("Executing {} {}", next.getFlywayVersion(), next.getDescription());
				}
				next.execute();
				addExecutedStatements(next.getExecutedStatements());
			} catch (SQLException e) {
				throw new InternalErrorException(Msg.code(48) + e);
			}
		}
		if (isDryRun()) {
			StringBuilder statementBuilder = buildExecutedStatementsString();
			ourLog.info("SQL that would be executed:\n\n***********************************\n{}***********************************", statementBuilder);
		}
	}

	@Override
	public Optional<MigrationInfoService> getMigrationInfo() {
		return Optional.empty();
	}

	@Override
	public void addTasks(List<BaseTask> theMigrationTasks) {
		myTasks.addAll(theMigrationTasks);
	}
}
