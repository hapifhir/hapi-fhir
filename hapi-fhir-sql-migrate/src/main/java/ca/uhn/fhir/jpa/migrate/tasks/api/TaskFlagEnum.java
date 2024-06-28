/*-
 * #%L
 * HAPI FHIR Server - SQL Migration
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.migrate.tasks.api;

import ca.uhn.fhir.jpa.migrate.taskdef.InitializeSchemaTask;

public enum TaskFlagEnum {

	/**
	 * This task should be skipped by default on live system migrations. Use this flag
	 * in cases where the benefit of applying a migration doesn't justify the effort, and
	 * only use this flag in cases where skipping the task will not leave the system in
	 * an unusable state.
	 * <p>
	 * This flag is intended for situations where a change to the schema would be useful
	 * to add to the default initialized schema, but would not be worth the effort of
	 * applying in-place to large databases due to the outsized effort it would take.
	 * </p>
	 * <p>
	 * USE THIS FLAG WITH CAUTION: It means that the migrator unit tests will execute the
	 * task, but live migrations probably won't. So think long and hard before you set it!
	 * </p>
	 */
	HEAVYWEIGHT_SKIP_BY_DEFAULT,

	/**
	 * Should this task run even if we're doing the very first initialization of an empty schema. By
	 * default, we skip most tasks during that pass, since they just take up time and the
	 * schema should be fully initialized by the {@link InitializeSchemaTask}
	 */
	RUN_DURING_SCHEMA_INITIALIZATION,

	/**
	 * This task is allowed to fail, and failure won't abort the migration. Upon any
	 * detected failures, the task will still be recorded as having been successfully
	 * complected. A failure in this context means that the SQL statement(s) being
	 * executed returned a non-successful response, or timed out.
	 */
	FAILURE_ALLOWED,

	/**
	 * This task should not actually do anything, and will always be recorded as
	 * a success. Use this flag for tasks that are no longer needed, e.g. because
	 * they are superseded by later tasks or because they turned out to be a bad
	 * idea.
	 */
	DO_NOTHING
}
