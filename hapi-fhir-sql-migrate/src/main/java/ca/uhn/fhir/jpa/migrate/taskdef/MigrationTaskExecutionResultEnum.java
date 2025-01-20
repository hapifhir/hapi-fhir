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
package ca.uhn.fhir.jpa.migrate.taskdef;

public enum MigrationTaskExecutionResultEnum {
	/**
	 * Was either skipped via the `skip-versions` flag or the migration task was stubbed
	 */
	NOT_APPLIED_SKIPPED,

	/**
	 * This migration task does not apply to this database
	 */
	NOT_APPLIED_NOT_FOR_THIS_DATABASE,

	/**
	 * This migration task had precondition criteria (expressed as SQL) that was not met
	 */
	NOT_APPLIED_PRECONDITION_NOT_MET,

	/**
	 * The migration failed, but the task has the FAILURE_ALLOWED flag set.
	 */
	NOT_APPLIED_ALLOWED_FAILURE,

	/**
	 * The migration was applied
	 */
	APPLIED,
}
