package ca.uhn.fhir.jpa.batch;

/*-
 * #%L
 * HAPI FHIR JPA Server
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

import ca.uhn.fhir.jpa.bulk.export.job.BulkExportJobConfig;
import ca.uhn.fhir.jpa.bulk.imprt.job.BulkImportJobConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Configuration
//When you define a new batch job, add it here.
@Import({
		  CommonBatchJobConfig.class,
		  BulkExportJobConfig.class,
		  BulkImportJobConfig.class
})
public class BatchJobsConfig {

	/*
	 * Bulk Export
	 */

	public static final String BULK_EXPORT_JOB_NAME = "bulkExportJob";
	public static final String GROUP_BULK_EXPORT_JOB_NAME = "groupBulkExportJob";
	public static final String PATIENT_BULK_EXPORT_JOB_NAME = "patientBulkExportJob";
	public static final String BULK_EXPORT_GENERATE_RESOURCE_FILES_STEP = "bulkExportGenerateResourceFilesStep";

	/*
	 * Bulk Import
	 */

	public static final String BULK_IMPORT_JOB_NAME = "bulkImportJob";
	public static final String BULK_IMPORT_PROCESSING_STEP = "bulkImportProcessingStep";

	/**
	 * This Set contains the step names across all job types that are appropriate for
	 * someone to look at the write count for that given step in order to determine the
	 * number of processed records.
	 *
	 * This is provided since a job might have multiple steps that the same data passes
	 * through, so you can't just sum up the total of all of them.
	 *
	 * For any given batch job type, there should only be one step name in this set
	 */
	public static final Set<String> RECORD_PROCESSING_STEP_NAMES;

	static {
		HashSet<String> recordProcessingStepNames = new HashSet<>();
		recordProcessingStepNames.add(BULK_IMPORT_PROCESSING_STEP);
		recordProcessingStepNames.add(BULK_EXPORT_GENERATE_RESOURCE_FILES_STEP);
		RECORD_PROCESSING_STEP_NAMES = Collections.unmodifiableSet(recordProcessingStepNames);
	}

}
