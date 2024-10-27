/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 specification tests
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
package ca.uhn.hapi.fhir.batch2.test;

public interface WorkChunkTestConstants {
	public static final String JOB_DEFINITION_ID = "definition-id";
	// we use a separate id for gated jobs because these job definitions might not
	// be cleaned up after any given test run
	String GATED_JOB_DEFINITION_ID = "gated_job_def_id";
	public static final String FIRST_STEP_ID = "step-id";
	public static final String SECOND_STEP_ID = "2nd-step-id";
	public static final String LAST_STEP_ID = "last-step-id";
	public static final String DEF_CHUNK_ID = "definition-chunkId";
	public static final int JOB_DEF_VER = 1;
	public static final int SEQUENCE_NUMBER = 1;
	public static final String CHUNK_DATA = "{\"key\":\"value\"}";
	public static final String ERROR_MESSAGE_A = "This is an error message: A";
	public static final String ERROR_MESSAGE_B = "This is a different error message: B";
	public static final String ERROR_MESSAGE_C = "This is a different error message: C";

	String FIRST_ERROR_MESSAGE = ERROR_MESSAGE_A;
}
