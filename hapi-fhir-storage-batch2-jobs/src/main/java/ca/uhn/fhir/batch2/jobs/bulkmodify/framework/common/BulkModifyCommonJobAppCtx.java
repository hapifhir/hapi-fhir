/*-
 * #%L
 * HAPI-FHIR Storage Batch2 Jobs
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
package ca.uhn.fhir.batch2.jobs.bulkmodify.framework.common;

import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.base.BaseBulkModifyJobParameters;
import ca.uhn.fhir.batch2.jobs.step.GenerateRangeChunksStep;
import ca.uhn.fhir.batch2.jobs.step.LoadIdsStep;
import ca.uhn.fhir.jpa.api.svc.IBatch2DaoSvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BulkModifyCommonJobAppCtx {

	private final IBatch2DaoSvc myBatch2DaoSvc;

	/**
	 * Constructor
	 */
	public BulkModifyCommonJobAppCtx(IBatch2DaoSvc theBatch2DaoSvc) {
		myBatch2DaoSvc = theBatch2DaoSvc;
	}

	/**
	 * Step 1
	 */
	@Bean("bulkModifyGenerateRangesStep")
	public GenerateRangeChunksStep<BaseBulkModifyJobParameters> generateRangesStep() {
		return new GenerateRangeChunksStep<>();
	}

	/**
	 * Step 2
	 */
	@Bean("bulkModifyLoadIdsStep")
	public LoadIdsStep<BaseBulkModifyJobParameters> loadIdsStep() {
		return new LoadIdsStep<>(myBatch2DaoSvc);
	}

	/**
	 * Step 3
	 */
	@Bean("bulkModifyExpandIdsStep")
	public TypedPidToTypedPidAndVersionStep<BaseBulkModifyJobParameters> expandIdVersionsStep() {
		return new TypedPidToTypedPidAndVersionStep<>();
	}

	/*
	 * Step 4 is the actual modification step and is provided by the specific job definition
	 */

	/*
	 * Step 5 is the report generation step
	 */

}
