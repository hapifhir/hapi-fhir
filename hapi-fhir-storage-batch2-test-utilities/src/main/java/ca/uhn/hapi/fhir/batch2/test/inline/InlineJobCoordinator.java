/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 specification tests
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
package ca.uhn.hapi.fhir.batch2.test.inline;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.api.JobOperationResultJson;
import ca.uhn.fhir.batch2.coordinator.JobDefinitionRegistry;
import ca.uhn.fhir.batch2.model.BatchInstanceStatusDTO;
import ca.uhn.fhir.batch2.model.BatchWorkChunkStatusDTO;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.models.JobInstanceFetchRequest;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import com.google.common.collect.ListMultimap;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

/**
 * {@link IJobCoordinator} used for testing that tiggers a batch job without the heavy infrastructure of the
 * batch 2 framework.
 * <p/>
 * Note that this class currently runs jobs sequentially and as part of a single thread.
 * @param <T> the type of the job parameters
 */
public class InlineJobCoordinator<T extends IModelJson> implements IJobCoordinator {

	// Test code must pass this header with the job instance ID per RequestDetails
	public static final String JOB_INSTANCE_ID_FOR_TESTING = "jobInstanceIdForTesting";

	private final InlineJobRunner<T> myInlineJobRunner;
	private final JobDefinition<T> myJobDefinition;
	private final JobDefinitionRegistry myJobDefinitionRegistry;
	private final Map<String, JobInstance> myJobInstanceMap = new HashMap<>();
	private final Map<String, Supplier<ListMultimap<String, IModelJson>>> myJobRunExecutions = new HashMap<>();

    public InlineJobCoordinator(JobDefinition<T> theJobDefinition, JobDefinitionRegistry theJobDefinitionRegistry) {
        myInlineJobRunner = new InlineJobRunner<>(theJobDefinition);
		myJobDefinition = theJobDefinition;
		myJobDefinitionRegistry = theJobDefinitionRegistry;
		myJobDefinitionRegistry.addJobDefinition(theJobDefinition);
	}

    @Override
    public Batch2JobStartResponse startInstance(
            RequestDetails requestDetails, JobInstanceStartRequest theJobInstanceStartRequest)
            throws InvalidRequestException {

		final Optional<JobDefinition<?>> optJobDefinition = myJobDefinitionRegistry.getLatestJobDefinition(theJobInstanceStartRequest.getJobDefinitionId());

		// Don't bother with Msg codes and Exceptions since this is just test code.
		if (optJobDefinition.isEmpty()) {
			return null;
		}

		// Assume this is the correct type
		final JobDefinition<T> jobDefinition = unsafeCast(optJobDefinition.get());
		final T jobParameters = theJobInstanceStartRequest.getParameters(jobDefinition.getParametersType());

		final JobInstance jobInstance = JobInstance.fromJobDefinition(myJobDefinition);
		jobInstance.setParameters(jobParameters);
		jobInstance.setStatus(StatusEnum.QUEUED);
		// Allow the test code to pass its own job instance ID so that it may use it to retrieve the job execution results
		jobInstance.setInstanceId(requestDetails.getHeader(JOB_INSTANCE_ID_FOR_TESTING));

		myJobInstanceMap.put(jobInstance.getInstanceId(), jobInstance);

		// In at least one real batch testing use case, we'll get unexpected results if we invoke the job runner immediately,
		// so delay it:
		myJobRunExecutions.put(jobInstance.getInstanceId(), () -> myInlineJobRunner.run(jobParameters));

		final Batch2JobStartResponse batch2JobStartResponse = new Batch2JobStartResponse();
        batch2JobStartResponse.setInstanceId(jobInstance.getInstanceId());
        return batch2JobStartResponse;
    }

	public ListMultimap<String, IModelJson> runJobAndRetrieveJobRunResults(String theJobInstanceId) {
		return myJobRunExecutions.get(theJobInstanceId).get();
	}

    @Nonnull
    @Override
    public JobInstance getInstance(String theJobInstanceId) throws ResourceNotFoundException {
		return myJobInstanceMap.get(theJobInstanceId);
    }

    @Override
    public List<JobInstance> getInstances(int thePageSize, int thePageIndex) {
        return myJobInstanceMap.values().stream().toList();
    }

    @Override
    public List<JobInstance> getRecentInstances(int theCount, int theStart) {
		return myJobInstanceMap.values().stream().toList();
    }

    @Override
    public JobOperationResultJson cancelInstance(String theJobInstanceId) throws ResourceNotFoundException {
        return null;
    }

    @Override
    public List<JobInstance> getInstancesbyJobDefinitionIdAndEndedStatus(
		String theJobDefinitionId, @Nullable Boolean aBoolean, int theCount, int theStart) {

        return myJobInstanceMap.values()
			.stream()
			.filter(jobInstance -> theJobDefinitionId.equals(jobInstance.getInstanceId()))
			.filter(jobInstance -> StatusEnum.getEndedStatuses().contains(jobInstance.getStatus()))
			.toList();
    }

    @Override
	public Page<JobInstance> fetchAllJobInstances(JobInstanceFetchRequest theRequest) {
		PageRequest pageRequest =
			PageRequest.of(theRequest.getPageStart(), theRequest.getBatchSize(), theRequest.getSort());

		List<JobInstance> list = myJobInstanceMap.values()
			.stream()
			.filter(job -> StringUtils.equals(theRequest.getJobStatus(), job.getStatus().name()))
			.filter(job -> StringUtils.equals(theRequest.getJobDefinitionId(), job.getJobDefinitionId()))
			.filter(job -> StringUtils.equals(theRequest.getJobId(), job.getInstanceId()))
			.filter(job -> theRequest.getJobCreateTimeFrom() != null && theRequest.getJobCreateTimeFrom().before(job.getCreateTime()))
			.filter(job -> theRequest.getJobCreateTimeTo() != null && theRequest.getJobCreateTimeTo().after(job.getCreateTime()))
			.toList();
		
		return new PageImpl<>(list, pageRequest, list.size());
	}

    @Override
    public List<JobInstance> getJobInstancesByJobDefinitionIdAndStatuses(String theJobDefinitionId, Set<StatusEnum> theStatuses, int theCount, int theStart) {
		return myJobInstanceMap.values()
			.stream()
			.filter(jobInstance -> theJobDefinitionId.equals(jobInstance.getInstanceId()))
			.filter(jobInstance -> theStatuses.contains(jobInstance.getStatus()))
			.toList();
    }

    @Override
    public List<JobInstance> getJobInstancesByJobDefinitionId(String theJobDefinitionId, int theCount, int i1) {
		return myJobInstanceMap.values()
			.stream()
			.filter(jobInstance -> theJobDefinitionId.equals(jobInstance.getInstanceId()))
			.toList();
    }

    @Override
    public List<BatchWorkChunkStatusDTO> getWorkChunkStatus(String theJobInstanceId) {
        return List.of();
    }

    @Override
    public BatchInstanceStatusDTO getBatchInstanceStatus(String theJobInstanceId) {
        return null;
    }

	@SuppressWarnings("unchecked")
	private static <T> T unsafeCast(Object theObject) {
		return (T) theObject;
	}
}
