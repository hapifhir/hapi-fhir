package ca.uhn.fhir.jpa.batch.config;

/*-
 * #%L
 * HAPI FHIR JPA Server - Batch Task Processor
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

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class BatchConstants {
	public static final String JOB_PARAM_REQUEST_LIST = "url-list";
	public static final String JOB_PARAM_BATCH_SIZE = "batch-size";
	public static final String JOB_PARAM_START_TIME = "start-time";
	public static final String CURRENT_URL_INDEX = "current.url-index";
	public static final String CURRENT_THRESHOLD_HIGH = "current.threshold-high";
	public static final String JOB_UUID_PARAMETER = "jobUUID";
	public static final String JOB_LAUNCHING_TASK_EXECUTOR = "jobLaunchingTaskExecutor";
	public static final String BULK_EXPORT_JOB_NAME = "bulkExportJob";
	public static final String GROUP_BULK_EXPORT_JOB_NAME = "groupBulkExportJob";
	public static final String PATIENT_BULK_EXPORT_JOB_NAME = "patientBulkExportJob";
	public static final String BULK_EXPORT_GENERATE_RESOURCE_FILES_STEP = "bulkExportGenerateResourceFilesStep";
	public static final String BULK_IMPORT_JOB_NAME = "bulkImportJob";
	public static final String BULK_IMPORT_PROCESSING_STEP = "bulkImportProcessingStep";
	/**
	 * Delete Expunge
	 */
	public static final String DELETE_EXPUNGE_JOB_NAME = "deleteExpungeJob";
	/**
	 * MDM Clear
	 */
	public static final String MDM_CLEAR_JOB_NAME = "mdmClearJob";

	/**
	 * TermCodeSystem delete
	 */
	public static final String TERM_CODE_SYSTEM_DELETE_JOB_NAME 			= "termCodeSystemDeleteJob";
	public static final String TERM_CONCEPT_RELATIONS_DELETE_STEP_NAME	= "termConceptRelationsDeleteStep";
	public static final String TERM_CONCEPTS_DELETE_STEP_NAME 				= "termConceptsDeleteStep";
	public static final String TERM_CODE_SYSTEM_VERSION_DELETE_STEP_NAME = "termCodeSystemVersionDeleteStep";
	public static final String TERM_CODE_SYSTEM_DELETE_STEP_NAME 			= "termCodeSystemDeleteStep";
	public static final String JOB_PARAM_CODE_SYSTEM_ID 						= "termCodeSystemPid";

	/**
	 * TermCodeSystemVersion delete
	 */
	public static final String TERM_CODE_SYSTEM_VERSION_DELETE_JOB_NAME 						= "termCodeSystemVersionDeleteJob";
	public static final String TERM_CONCEPT_RELATIONS_UNIQUE_VERSION_DELETE_STEP_NAME 	= "termConceptRelationsUniqueVersionDeleteStep";
	public static final String TERM_CONCEPTS_UNIQUE_VERSION_DELETE_STEP_NAME 				= "termConceptsUniqueVersionDeleteStep";
	public static final String TERM_CODE_SYSTEM_UNIQUE_VERSION_DELETE_STEP_NAME 			= "termCodeSystemUniqueVersionDeleteStep";

	/**
	 * Both: TermCodeSystem delete and TermCodeSystemVersion delete
	 */
	public static final String JOB_PARAM_CODE_SYSTEM_VERSION_ID 			= "termCodeSystemVersionPid";


	public static final String BULK_EXPORT_READ_CHUNK_PARAMETER = "readChunkSize";
	public static final String BULK_EXPORT_GROUP_ID_PARAMETER = "groupId";
	/**
	 * Job Parameters
	 */
	public static final String READ_CHUNK_PARAMETER = "readChunkSize";
	public static final String EXPAND_MDM_PARAMETER = "expandMdm";
	public static final String GROUP_ID_PARAMETER = "groupId";
	public static final String JOB_RESOURCE_TYPES_PARAMETER = "resourceTypes";
	public static final String JOB_DESCRIPTION = "jobDescription";
	public static final String JOB_SINCE_PARAMETER = "since";
	public static final String JOB_TYPE_FILTERS = "filters";
	public static final String JOB_COLLECTION_ENTITY_ID = "bulkExportCollectionEntityId";

	/**
	 * Job Execution Context
	 */
	public static final String JOB_EXECUTION_RESOURCE_TYPE = "resourceType";

	public static final List<String> PATIENT_BULK_EXPORT_FORWARD_REFERENCE_RESOURCE_TYPES = List.of("Practitioner", "Organization");

	/**
	 * This Set contains the step names across all job types that are appropriate for
	 * someone to look at the write count for that given step in order to determine the
	 * number of processed records.
	 * <p>
	 * This is provided since a job might have multiple steps that the same data passes
	 * through, so you can't just sum up the total of all of them.
	 * <p>
	 * For any given batch job type, there should only be one step name in this set
	 */
	public static Set<String> RECORD_PROCESSING_STEP_NAMES;

	static {
		HashSet<String> recordProcessingStepNames = new HashSet<>();
		recordProcessingStepNames.add(BatchConstants.BULK_IMPORT_PROCESSING_STEP);
		recordProcessingStepNames.add(BatchConstants.BULK_EXPORT_GENERATE_RESOURCE_FILES_STEP);
		BatchConstants.RECORD_PROCESSING_STEP_NAMES = Collections.unmodifiableSet(recordProcessingStepNames);
	}

	/**
	 * v	 * Non instantiable
	 */
	private BatchConstants() {
	}
}
