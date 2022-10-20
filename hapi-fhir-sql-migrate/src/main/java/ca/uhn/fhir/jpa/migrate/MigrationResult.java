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
import org.flywaydb.core.api.output.MigrateResult;

import java.util.ArrayList;
import java.util.List;

public class MigrationResult {
	public int changes = 0;
	public final List<BaseTask.ExecutedStatement> executedStatements = new ArrayList<>();
	public final List<BaseTask> succeededTasks = new ArrayList<>();
	public final List<BaseTask> failedTasks = new ArrayList<>();

	public static MigrationResult fromFlywayResult(MigrateResult theResult) {
		// FIXME KHS
		MigrationResult retVal = new MigrationResult();
		retVal.changes = theResult.migrationsExecuted;
		return retVal;
	}

	public String summary() {
		return String.format("Completed executing %s migration tasks: %s succeeded, %s failed.  %s SQL statements were executed.",
			succeededTasks.size() + failedTasks.size(),
			succeededTasks.size(),
			failedTasks.size(),
			executedStatements.size());
	}
}
