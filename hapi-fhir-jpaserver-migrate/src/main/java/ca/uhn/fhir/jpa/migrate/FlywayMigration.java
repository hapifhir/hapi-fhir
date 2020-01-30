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

import ca.uhn.fhir.jpa.migrate.taskdef.BaseTask;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.flywaydb.core.api.MigrationVersion;
import org.flywaydb.core.api.migration.Context;
import org.flywaydb.core.api.migration.JavaMigration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class FlywayMigration implements JavaMigration {
	private static final Logger ourLog = LoggerFactory.getLogger(FlywayMigration.class);

	private final BaseTask myTask;
	private final FlywayMigrator myFlywayMigrator;
	private DriverTypeEnum.ConnectionProperties myConnectionProperties;

	public FlywayMigration(BaseTask theTask, FlywayMigrator theFlywayMigrator) {
		myTask = theTask;
		myFlywayMigrator = theFlywayMigrator;
	}

	@Override
	public MigrationVersion getVersion() {
		return MigrationVersion.fromVersion(myTask.getFlywayVersion());
	}

	@Override
	public String getDescription() {
		return myTask.getDescription();
	}

	@Override
	public Integer getChecksum() {
		return myTask.hashCode();
	}

	@Override
	public boolean isUndo() {
		return false;
	}

	@Override
	public boolean canExecuteInTransaction() {
		return false;
	}

	@Override
	public void migrate(Context theContext) {
		myTask.setDriverType(myFlywayMigrator.getDriverType());
		myTask.setDryRun(myFlywayMigrator.isDryRun());
		myTask.setNoColumnShrink(myFlywayMigrator.isNoColumnShrink());
		myTask.setConnectionProperties(myConnectionProperties);
		try {
			myTask.execute();
			myFlywayMigrator.addExecutedStatements(myTask.getExecutedStatements());
		} catch (SQLException e) {
			String description = myTask.getDescription();
			if (isBlank(description)) {
				description = myTask.getClass().getSimpleName();
			}
			String prefix = "Failure executing task \"" + description + "\", aborting! Cause: ";
			throw new InternalErrorException(prefix + e.toString(), e);
		}
	}

	public void setConnectionProperties(DriverTypeEnum.ConnectionProperties theConnectionProperties) {
		myConnectionProperties = theConnectionProperties;
	}
}
