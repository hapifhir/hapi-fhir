package ca.uhn.fhir.cli;

/*-
 * #%L
 * HAPI FHIR - Command Line Client - API
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

import ca.uhn.fhir.jpa.migrate.BaseMigrator;
import ca.uhn.fhir.jpa.migrate.SchemaMigrator;
import ca.uhn.fhir.jpa.migrate.taskdef.BaseTask;
import ca.uhn.fhir.jpa.migrate.tasks.HapiFhirJpaMigrationTasks;
import ca.uhn.fhir.util.VersionEnum;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;

import java.util.Arrays;
import java.util.List;

public class HapiFlywayMigrateDatabaseCommand extends BaseFlywayMigrateDatabaseCommand<VersionEnum> {

	@Override
	protected List<VersionEnum> provideAllowedVersions() {
		return Arrays.asList(VersionEnum.values());
	}

	@Override
	protected Class<VersionEnum> provideVersionEnumType() {
		return VersionEnum.class;
	}

	@Override
	protected void addTasks(BaseMigrator theMigrator, String theSkipVersions) {
		List<BaseTask> tasks = new HapiFhirJpaMigrationTasks(getFlags()).getAllTasks(VersionEnum.values());
		super.setDoNothingOnSkippedTasks(tasks, theSkipVersions);
		theMigrator.addTasks(tasks);
	}

	@Override
	public void run(CommandLine theCommandLine) throws ParseException {
		setMigrationTableName(SchemaMigrator.HAPI_FHIR_MIGRATION_TABLENAME);
		super.run(theCommandLine);
	}
}
