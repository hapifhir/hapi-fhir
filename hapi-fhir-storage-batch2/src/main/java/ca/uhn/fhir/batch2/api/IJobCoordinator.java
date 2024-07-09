/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
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
package ca.uhn.fhir.batch2.api;

import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.models.JobInstanceFetchRequest;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

public interface IJobCoordinator {

	/**
	 * Starts a new job instance
	 *
	 * @param theStartRequest The request, containing the job type and parameters
	 * @return Returns a unique ID for this job execution
	 * @throws InvalidRequestException If the request is invalid (incorrect/missing parameters, etc)
	 * @deprecated Use {@link #startInstance(RequestDetails, JobInstanceStartRequest)}
	 */
	@Deprecated(since = "6.8.0", forRemoval = true)
	default Batch2JobStartResponse startInstance(JobInstanceStartRequest theStartRequest)
			throws InvalidRequestException {
		return startInstance(null, theStartRequest);
	}

	/**
	 * Starts a new job instance
	 *
	 * @param theRequestDetails The request details associated with the request. This will get used to validate that the
	 *                          request is appropriate for the given user, so if at all possible it should be the
	 *                          original RequestDetails from the server request.
	 * @param theStartRequest The request, containing the job type and parameters
	 * @return Returns a unique ID for this job execution
	 * @throws InvalidRequestException If the request is invalid (incorrect/missing parameters, etc)
	 */
	Batch2JobStartResponse startInstance(RequestDetails theRequestDetails, JobInstanceStartRequest theStartRequest)
			throws InvalidRequestException;

	/**
	 * Fetch details about a job instance
	 *
	 * @param theInstanceId The instance ID
	 * @return Returns the current instance details
	 * @throws ResourceNotFoundException If the instance ID can not be found
	 */
	@Nonnull
	JobInstance getInstance(String theInstanceId) throws ResourceNotFoundException;

	/**
	 * Fetch all job instances
	 */
	List<JobInstance> getInstances(int thePageSize, int thePageIndex);

	/**
	 * Fetch recent job instances
	 */
	List<JobInstance> getRecentInstances(int theCount, int theStart);

	JobOperationResultJson cancelInstance(String theInstanceId) throws ResourceNotFoundException;

	List<JobInstance> getInstancesbyJobDefinitionIdAndEndedStatus(
			String theJobDefinitionId, @Nullable Boolean theEnded, int theCount, int theStart);

	/**
	 * Fetches all job instances tht meet the FetchRequest criteria
	 * @param theFetchRequest - fetch request
	 * @return - page of job instances
	 */
	Page<JobInstance> fetchAllJobInstances(JobInstanceFetchRequest theFetchRequest);

	/**
	 * Fetches all job instances by job definition id and statuses
	 */
	List<JobInstance> getJobInstancesByJobDefinitionIdAndStatuses(
			String theJobDefinitionId, Set<StatusEnum> theStatuses, int theCount, int theStart);

	/**
	 * Fetches all jobs by job definition id
	 */
	List<JobInstance> getJobInstancesByJobDefinitionId(String theJobDefinitionId, int theCount, int theStart);
}
