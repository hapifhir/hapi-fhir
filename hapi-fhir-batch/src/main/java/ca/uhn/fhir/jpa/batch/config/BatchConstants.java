package ca.uhn.fhir.jpa.batch.config;

/*-
 * #%L
 * HAPI FHIR JPA Server - Batch Task Processor
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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
	 * Reindex
	 */
	public static final String REINDEX_JOB_NAME = "reindexJob";
	/**
	 * Reindex Everything
	 */
	public static final String REINDEX_EVERYTHING_JOB_NAME = "reindexEverythingJob";
	/**
	 * MDM Clear
	 */
	public static final String MDM_CLEAR_JOB_NAME = "mdmClearJob";
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
