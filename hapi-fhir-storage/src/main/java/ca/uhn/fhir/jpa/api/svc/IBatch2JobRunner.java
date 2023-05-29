/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.api.svc;

import ca.uhn.fhir.jpa.api.model.Batch2JobInfo;
import ca.uhn.fhir.jpa.api.model.Batch2JobOperationResult;
import ca.uhn.fhir.jpa.batch.models.Batch2BaseJobParameters;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.rest.api.server.RequestDetails;

public interface IBatch2JobRunner {

	/**
	 * Start the job with the given parameters
	 * @param theParameters
	 * @return returns the job id
	 */
	Batch2JobStartResponse startNewJob(RequestDetails theRequestDetails, Batch2BaseJobParameters theParameters);

	/**
	 * Returns information about a provided job.
	 *
	 * @param theJobId - the job id
	 * @return - the batch2 job info
	 */
	Batch2JobInfo getJobInfo(String theJobId);

	/**
	 * Cancels the job provided
	 */
	Batch2JobOperationResult cancelInstance(String theJobId);
}
