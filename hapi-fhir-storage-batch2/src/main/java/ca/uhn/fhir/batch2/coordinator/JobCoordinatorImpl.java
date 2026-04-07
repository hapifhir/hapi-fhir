/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.batch2.coordinator;

import ca.uhn.fhir.batch2.api.AttachmentDetails;
import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.JobOperationResultJson;
import ca.uhn.fhir.batch2.model.BatchInstanceStatusDTO;
import ca.uhn.fhir.batch2.model.BatchWorkChunkStatusDTO;
import ca.uhn.fhir.batch2.model.FetchJobInstancesRequest;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.models.JobInstanceFetchRequest;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.Logs;
import ca.uhn.fhir.util.ValidateUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Propagation;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class JobCoordinatorImpl implements IJobCoordinator {
	private static final Logger ourLog = Logs.getBatchTroubleshootingLog();

	private final IJobPersistence myJobPersistence;
	private final JobDefinitionRegistry myJobDefinitionRegistry;
	private final JobQuerySvc myJobQuerySvc;
	private final JobParameterJsonValidator myJobParameterJsonValidator;
	private final IHapiTransactionService myTransactionService;
	private final IInterceptorService myInterceptorService;

	/**
	 * Constructor
	 */
	public JobCoordinatorImpl(
			@Nonnull IJobPersistence theJobPersistence,
			@Nonnull JobDefinitionRegistry theJobDefinitionRegistry,
			@Nonnull IHapiTransactionService theTransactionService,
			@Nonnull IInterceptorService theInterceptorService) {
		Validate.notNull(theJobPersistence);

		myJobPersistence = theJobPersistence;
		myJobDefinitionRegistry = theJobDefinitionRegistry;

		myJobQuerySvc = new JobQuerySvc(theJobPersistence, theJobDefinitionRegistry);
		myJobParameterJsonValidator = new JobParameterJsonValidator();
		myTransactionService = theTransactionService;
		myInterceptorService = theInterceptorService;
	}

	@Override
	public Batch2JobStartResponse startInstance(
			RequestDetails theRequestDetails, JobInstanceStartRequest theStartRequest) {
		String paramsString = theStartRequest.getParameters();
		if (isBlank(paramsString)) {
			throw new InvalidRequestException(Msg.code(2065) + "No parameters supplied");
		}
		Validate.notBlank(theStartRequest.getJobDefinitionId(), "No job definition ID supplied in start request");

		// if cache - use that first
		if (theStartRequest.isUseCache()) {
			FetchJobInstancesRequest request = new FetchJobInstancesRequest(
					theStartRequest.getJobDefinitionId(), theStartRequest.getParameters(), getStatesThatTriggerCache());

			List<JobInstance> existing = myJobPersistence.fetchInstances(request, 0, 1000);
			if (!existing.isEmpty()) {
				// we'll look for completed ones first... otherwise, take any of the others
				existing.sort(
						(o1, o2) -> -(o1.getStatus().ordinal() - o2.getStatus().ordinal()));

				JobInstance first = existing.stream().findFirst().orElseThrow();

				Batch2JobStartResponse response = new Batch2JobStartResponse();
				response.setInstanceId(first.getInstanceId());
				response.setUsesCachedResult(true);

				ourLog.info(
						"Reusing cached {} job with status {} and id {}",
						first.getJobDefinitionId(),
						first.getStatus(),
						first.getInstanceId());

				return response;
			}
		}

		JobDefinition<?> jobDefinition = myJobDefinitionRegistry
				.getLatestJobDefinition(theStartRequest.getJobDefinitionId())
				.orElseThrow(() -> new IllegalArgumentException(
						Msg.code(2063) + "Unknown job definition ID: " + theStartRequest.getJobDefinitionId()));

		myJobParameterJsonValidator.validateJobParameters(theRequestDetails, theStartRequest, jobDefinition);

		// we only create the first chunk amd job here
		// JobMaintenanceServiceImpl.doMaintenancePass will handle the rest
		IJobPersistence.CreateResult instanceAndFirstChunk = myTransactionService
				.withSystemRequestOnDefaultPartition()
				.withPropagation(Propagation.REQUIRES_NEW)
				.execute(() -> myJobPersistence.onCreateWithFirstChunk(
						theRequestDetails, jobDefinition, theStartRequest.getParameters()));

		Batch2JobStartResponse response = new Batch2JobStartResponse();
		response.setInstanceId(instanceAndFirstChunk.jobInstanceId);
		return response;
	}

	/**
	 * Cache will be used if an identical job is QUEUED or IN_PROGRESS. Otherwise a new one will kickoff.
	 */
	private StatusEnum[] getStatesThatTriggerCache() {
		return new StatusEnum[] {StatusEnum.QUEUED, StatusEnum.IN_PROGRESS};
	}

	@Override
	@Nonnull
	public JobInstance getInstance(String theInstanceId) {
		return myJobQuerySvc.fetchInstance(theInstanceId);
	}

	@Override
	public List<JobInstance> getInstances(int thePageSize, int thePageIndex) {
		return myJobQuerySvc.fetchInstances(thePageSize, thePageIndex);
	}

	@Override
	public List<JobInstance> getRecentInstances(int theCount, int theStart) {
		return myJobQuerySvc.fetchRecentInstances(theCount, theStart);
	}

	@Override
	public List<JobInstance> getInstancesbyJobDefinitionIdAndEndedStatus(
			String theJobDefinitionId, @Nullable Boolean theEnded, int theCount, int theStart) {
		return myJobQuerySvc.getInstancesByJobDefinitionIdAndEndedStatus(
				theJobDefinitionId, theEnded, theCount, theStart);
	}

	@Override
	public List<JobInstance> getJobInstancesByJobDefinitionIdAndStatuses(
			String theJobDefinitionId, Set<StatusEnum> theStatuses, int theCount, int theStart) {
		return myJobQuerySvc.getInstancesByJobDefinitionAndStatuses(
				theJobDefinitionId, theStatuses, theCount, theStart);
	}

	@Override
	public List<JobInstance> getJobInstancesByJobDefinitionId(String theJobDefinitionId, int theCount, int theStart) {
		return getJobInstancesByJobDefinitionIdAndStatuses(
				theJobDefinitionId, new HashSet<>(Arrays.asList(StatusEnum.values())), theCount, theStart);
	}

	@Override
	public List<BatchWorkChunkStatusDTO> getWorkChunkStatus(String theInstanceId) {
		return myJobPersistence.fetchWorkChunkStatusForInstance(theInstanceId);
	}

	@Override
	public BatchInstanceStatusDTO getBatchInstanceStatus(String theInstanceId) {
		return myJobPersistence.fetchBatchInstanceStatus(theInstanceId);
	}

	@Override
	public void enqueueBuildingJobForExecution(String theInstanceId) {
		myTransactionService.withSystemRequestOnDefaultPartition().execute(() -> {
			JobInstance instance = getInstance(theInstanceId);
			instance.setStatus(StatusEnum.BUILDING);
			boolean changed = myJobPersistence.markInstanceAsStatusWhenStatusIn(
					theInstanceId, StatusEnum.QUEUED, Set.of(StatusEnum.BUILDING));
			if (changed) {
				ourLog.info("Moving job instance[{}] from status BUILDING to QUEUED", theInstanceId);
			} else {
				throw new InvalidRequestException(Msg.code(2901) + "Job instance is in " + instance.getStatus()
						+ " status and cannot be enqueued for execution");
			}
		});
	}

	@Override
	public Page<JobInstance> fetchAllJobInstances(JobInstanceFetchRequest theFetchRequest) {
		return myJobQuerySvc.fetchAllInstances(theFetchRequest);
	}

	// wipmb For 6.8 - Clarify this interface. We currently return a JobOperationResultJson, and don't throw
	// ResourceNotFoundException
	@Override
	public JobOperationResultJson cancelInstance(String theInstanceId) throws ResourceNotFoundException {
		return myJobPersistence.cancelInstance(theInstanceId);
	}

	@Override
	public void addAttachmentToBuildingJob(String theInstanceId, AttachmentDetails theAttachmentDetails) {

		/*
		 * Note that we block any attachments from being added to a job by the outside world
		 * once the job is no longer in BUILDING status. Step processors can still add attachments
		 * though by working directly against the job persistence.
		 */
		myTransactionService.withSystemRequestOnDefaultPartition().execute(() -> {
			Optional<JobInstance> instance = myJobPersistence.fetchInstance(theInstanceId);
			ValidateUtil.isTrueOrThrowInvalidRequest(
					instance.isPresent(), "Job instance does not exist: %s", theAttachmentDetails);
			ValidateUtil.isTrueOrThrowInvalidRequest(
					instance.get().getStatus() == StatusEnum.BUILDING,
					"Job instance %s is not in BUILDING status: %s",
					instance.get().getInstanceId(),
					instance.get().getStatus());

			myJobPersistence.storeNewAttachment(theInstanceId, theAttachmentDetails);
		});
	}
}
