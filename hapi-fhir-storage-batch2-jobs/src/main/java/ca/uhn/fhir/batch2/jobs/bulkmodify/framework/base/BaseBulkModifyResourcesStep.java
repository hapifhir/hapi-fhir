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
package ca.uhn.fhir.batch2.jobs.bulkmodify.framework.base;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.api.ResourceModificationRequest;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.api.ResourceModificationResponse;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.common.BulkModifyResourcesChunkOutcomeJson;
import ca.uhn.fhir.batch2.jobs.chunk.TypedPidAndVersionJson;
import ca.uhn.fhir.batch2.jobs.chunk.TypedPidAndVersionListWorkChunkJson;
import ca.uhn.fhir.batch2.jobs.chunk.TypedPidJson;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This is the base class for any <b>bulk modification</b> or <b>bulk rewrite</b> jobs. Any new bulk modification
 * job type should subclass this class, and implement {@link #modifyResource(BaseBulkModifyJobParameters, Object, ResourceModificationRequest)}.
 * Several other methods are provided which can also be overridden.
 *
 * @param <PT> The job parameters type
 * @param <C> A context object which will be passed between methods for a single chunk of resources. The format of the
 *           context object is up to the subclass, the framework doesn't look at it.
 * @since 8.6.0
 */
public abstract class BaseBulkModifyResourcesStep<PT extends BaseBulkModifyJobParameters, C>
		implements IJobStepWorker<PT, TypedPidAndVersionListWorkChunkJson, BulkModifyResourcesChunkOutcomeJson> {
	public static final int MAX_RETRIES = 2;
	private static final Logger ourLog = LoggerFactory.getLogger(BaseBulkModifyResourcesStep.class);

	@Autowired
	private IHapiTransactionService myTransactionService;

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	private IFhirSystemDao mySystemDao;

	@Autowired
	private IIdHelperService<IResourcePersistentId<?>> myIdHelperService;

	@Autowired
	private FhirContext myFhirContext;

	/**
	 * Constructor
	 */
	public BaseBulkModifyResourcesStep() {
		super();
	}

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull StepExecutionDetails<PT, TypedPidAndVersionListWorkChunkJson> theStepExecutionDetails,
			@Nonnull IJobDataSink<BulkModifyResourcesChunkOutcomeJson> theDataSink)
			throws JobExecutionFailedException {
		State state = new State();

		List<TypedPidAndVersionJson> pids = theStepExecutionDetails.getData().getTypedPidAndVersions();
		PT jobParameters = theStepExecutionDetails.getParameters();
		RequestPartitionId requestPartitionId =
				theStepExecutionDetails.getData().getRequestPartitionId();

		// Try to update the whole chunk in a single database transaction for fast performance
		processPids(jobParameters, state, pids, requestPartitionId, false);

		// If our attempt to process as a single DB transaction failed, we will try each
		// resource individually in a separate DB transaction so that any failures on one
		// resource don't block another from succeeding
		while (state.hasPidsToModify()) {
			int retryCount = state.incrementAndGetRetryCount();
			state.addRetriedResourceCount(state.countPidsToModify());

			boolean finalRetry = retryCount >= MAX_RETRIES;
			processPidsInIndividualTransactions(jobParameters, state, requestPartitionId, finalRetry);
			if (!state.hasPidsToModify()) {
				break;
			}
			if (finalRetry) {
				break;
			}
		}

		Validate.isTrue(
				!state.hasPidsToModify(), "PIDs remain in state, this is a bug: %s", state.getPidsToModifyAndClear());

		BulkModifyResourcesChunkOutcomeJson outcome = generateOutcome(state);
		theDataSink.accept(outcome);

		return RunOutcome.SUCCESS;
	}

	private void processPids(
			PT jobParameters,
			State theState,
			List<TypedPidAndVersionJson> thePids,
			RequestPartitionId theRequestPartitionId,
			boolean theFinalRetry) {
		HapiTransactionService.noTransactionAllowed();

		final TransactionDetails transactionDetails = new TransactionDetails();
		try {

			myTransactionService
					.withSystemRequestOnPartition(theRequestPartitionId)
					.withTransactionDetails(transactionDetails)
					.execute(() -> processPidsInTransaction(jobParameters, theState, thePids, transactionDetails));

			// Storage transaction succeeded
			theState.movePendingToSaved();

		} catch (Exception e) {

			ourLog.warn("Failure occurred during bulk modification, will retry. Failure: {}", e.toString());

			// Storage transaction failed
			if (theFinalRetry) {
				theState.movePendingToFailed(e.getMessage());
			} else {
				theState.movePendingBackToModificationList();
			}
		}

		if (theState.getJobFailure() != null) {
			throw theState.getJobFailure();
		}
	}

	private void processPidsInTransaction(
			PT theJobParameters,
			State theState,
			List<TypedPidAndVersionJson> thePids,
			TransactionDetails theTransactionDetails) {
		HapiTransactionService.requireTransaction();

		// Fetch all the resources in the chunk
		List<PidAndResource> resources = fetchResourcesInTransaction(thePids);

		// Perform the modification (handled by subclasses)
		C modificationContext;
		try {
			modificationContext = modifyResourcesInTransaction(theState, theJobParameters, resources);
		} catch (JobExecutionFailedException e) {
			theState.setJobFailure(e);
			throw e;
		}

		// Store the modified resources to the DB
		storeResourcesInTransaction(modificationContext, theState, theTransactionDetails);
	}

	private void processPidsInIndividualTransactions(
			PT theJobParameters, State theState, RequestPartitionId theRequestPartitionId, boolean theFinalRetry) {
		List<TypedPidAndVersionJson> pids = theState.getPidsToModifyAndClear();

		for (TypedPidAndVersionJson nextPid : pids) {
			processPids(theJobParameters, theState, List.of(nextPid), theRequestPartitionId, theFinalRetry);
		}
	}

	@Nonnull
	private List<PidAndResource> fetchResourcesInTransaction(List<TypedPidAndVersionJson> thePids) {
		assert TransactionSynchronizationManager.isActualTransactionActive();

		Multimap<TypedPidJson, TypedPidAndVersionJson> pidsToVersions =
				MultimapBuilder.hashKeys().arrayListValues().build();
		for (TypedPidAndVersionJson pidAndVersion : thePids) {
			pidsToVersions.put(pidAndVersion.toTypedPid(), pidAndVersion);
		}
		List<TypedPidJson> typedPids = List.copyOf(pidsToVersions.keySet());
		List<? extends IResourcePersistentId<?>> typedPidsAsPersistentIds =
				typedPids.stream().map(t -> t.toPersistentId(myIdHelperService)).toList();

		mySystemDao.preFetchResources(typedPidsAsPersistentIds, true);

		List<PidAndResource> resources = new ArrayList<>(thePids.size());
		for (int i = 0; i < typedPids.size(); i++) {
			TypedPidJson typedPid = typedPids.get(i);
			IResourcePersistentId<?> persistentId = typedPidsAsPersistentIds.get(i);

			IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(typedPid.getResourceType());
			IBaseResource resource = dao.readByPid(persistentId);

			Collection<TypedPidAndVersionJson> typedVersionedPids = pidsToVersions.get(typedPid);
			for (TypedPidAndVersionJson typedVersionedPid : typedVersionedPids) {
				Long nextVersion = typedVersionedPid.getVersionId();
				if (nextVersion == null
						|| nextVersion.equals(resource.getIdElement().getVersionIdPartAsLong())) {
					resources.add(new PidAndResource(typedVersionedPid, resource));
				} else {
					IIdType nextVersionedId = resource.getIdElement().withVersion(Long.toString(nextVersion));
					IBaseResource resourceVersion = dao.read(nextVersionedId, new SystemRequestDetails(), true);

					// Ignore deleted resources
					if (ResourceMetadataKeyEnum.DELETED_AT.get(resourceVersion) != null) {
						continue;
					}

					resources.add(new PidAndResource(typedVersionedPid, resourceVersion));
				}
			}
		}

		return resources;
	}

	private C modifyResourcesInTransaction(State theState, PT theJobParameters, List<PidAndResource> resources) {
		assert TransactionSynchronizationManager.isActualTransactionActive();

		List<TypedPidAndVersionJson> pids =
				resources.stream().map(PidAndResource::pid).toList();
		C modificationContext = preModifyResources(theJobParameters, pids);

		for (PidAndResource pidAndResource : resources) {

			TypedPidAndVersionJson pid = pidAndResource.pid();
			IBaseResource resource = pidAndResource.resource();
			String resourceType = myFhirContext.getResourceType(resource);
			String resourceId = resource.getIdElement().getIdPart();
			String resourceVersion = resource.getIdElement().getVersionIdPart();

			HashingWriter preModificationHash = hashResource(resource);

			ResourceModificationRequest modificationRequest = new ResourceModificationRequest(resource);
			ResourceModificationResponse modificationResponse =
					modifyResource(theJobParameters, modificationContext, modificationRequest);
			if (modificationResponse == null) {
				throw new JobExecutionFailedException(Msg.code(2789) + "Null response from Modification for Resource["
						+ resource.getIdElement() + "]");
			}

			IBaseResource updatedResource = modificationResponse.getResource();
			if (updatedResource == null) {
				theState.addUnchangedResource(new PidAndResource(pid, resource));
				continue;
			}

			if (!resourceType.equals(myFhirContext.getResourceType(updatedResource))) {
				throw new JobExecutionFailedException(Msg.code(2782) + "Modification for Resource["
						+ resource.getIdElement() + "] returned wrong resource type, expected " + resourceType
						+ " but was " + myFhirContext.getResourceType(updatedResource));
			}
			if (!resourceId.equals(updatedResource.getIdElement().getIdPart())) {
				throw new JobExecutionFailedException(Msg.code(2783) + "Modification for Resource[" + resourceType + "/"
						+ resourceId + "] attempted to change the resource ID");
			}
			if (!resourceVersion.equals(updatedResource.getIdElement().getVersionIdPart())) {
				throw new JobExecutionFailedException(Msg.code(2784) + "Modification for Resource[" + resourceType + "/"
						+ resourceId + "] attempted to change the resource version");
			}

			HashingWriter postModificationHash = hashResource(resource);

			if (preModificationHash.matches(postModificationHash)) {
				theState.addUnchangedResource(new PidAndResource(pid, updatedResource));
			} else {
				theState.addChangedUnsavedResource(new PidAndResource(pid, updatedResource));
			}
		}

		return modificationContext;
	}

	@Nonnull
	private HashingWriter hashResource(IBaseResource resource) {
		try (HashingWriter preModificationHash = new HashingWriter()) {
			preModificationHash.append(resource);
			return preModificationHash;
		}
	}

	private void storeResourcesInTransaction(
			C theModificationContext, State theState, TransactionDetails theTransactionDetails) {
		assert TransactionSynchronizationManager.isActualTransactionActive();

		for (PidAndResource pidAndResource : theState.getChangedUnsavedResourcesAndMoveToPending()) {
			IFhirResourceDao<IBaseResource> dao = myDaoRegistry.getResourceDao(pidAndResource.resource());

			SystemRequestDetails requestDetails = new SystemRequestDetails();
			requestDetails.setRewriteHistory(isRewriteHistory(theModificationContext, pidAndResource.resource()));

			DaoMethodOutcome outcome =
					dao.update(pidAndResource.resource(), null, true, false, requestDetails, theTransactionDetails);
			ourLog.debug(
					"Storage for PID[{}] resulted in version: {}",
					pidAndResource.pid,
					outcome.getId().getVersionIdPart());
		}
	}

	/**
	 * Subclasses may override this method to indicate that this resource should be stored
	 * as a history rewrite
	 */
	@SuppressWarnings("unused")
	protected boolean isRewriteHistory(C theState, IBaseResource theResource) {
		return false;
	}

	/**
	 * Subclasses may override this method, which will be called immediately before beginning processing
	 * of a batch of resources. It can be used to perform any shared processing which would otherwise need
	 * to be repeated, such as looking up context resources, parsing a patch object in the job parameters
	 * or other expensive operations.
	 *
	 * @param theJobParameters The parametrs for the current instance of the job
	 * @param thePids          The PIDs of the resources to be modified. The PIDs will be in the same order as the resources in the chunk.
	 * @return A context object which will be passed to {@link #modifyResource(PT, Object, ResourceModificationRequest)}
	 * 	during each invocation. The format of the context object is up to the subclass, the framework
	 * 	won't look at it and doesn't care if it is {@literal null}.
	 */
	@Nullable
	protected C preModifyResources(PT theJobParameters, List<TypedPidAndVersionJson> thePids) {
		return null;
	}

	/**
	 * This method is called once for each resource needing modification
	 */
	protected abstract ResourceModificationResponse modifyResource(
			PT theJobParameters, C theModificationContext, @Nonnull ResourceModificationRequest theModificationRequest);

	@Nonnull
	private static BulkModifyResourcesChunkOutcomeJson generateOutcome(State theState) {
		BulkModifyResourcesChunkOutcomeJson outcome = new BulkModifyResourcesChunkOutcomeJson();

		outcome.setChunkRetryCount(theState.getRetryCount());
		outcome.setResourceRetryCount(theState.getRetriedResourceCount());

		for (PidAndResource next : theState.getChangedSavedResources()) {
			outcome.addChangedId(next.resource().getIdElement());
		}
		for (PidAndResource next : theState.getUnchangedResources()) {
			outcome.addUnchangedId(next.resource().getIdElement());
		}
		for (Map.Entry<PidAndResource, String> next : theState.getFailures().entrySet()) {
			outcome.addFailure(next.getKey().resource().getIdElement(), next.getValue());
		}
		return outcome;
	}

	private static class State {

		private final List<TypedPidAndVersionJson> myPidsToModify = new ArrayList<>();
		private final Set<PidAndResource> myUnchangedResources = new HashSet<>();
		private final Set<PidAndResource> myChangedUnsavedResources = new HashSet<>();
		private final Set<PidAndResource> myChangedSavedResources = new HashSet<>();
		private final List<PidAndResource> myPendingSavedResources = new ArrayList<>();
		private final Map<PidAndResource, String> myFailures = new HashMap<>();
		private int myRetryCount;
		private int myRetriedResourceCount;
		private JobExecutionFailedException myJobFailure;

		public int getRetryCount() {
			return myRetryCount;
		}

		public int incrementAndGetRetryCount() {
			myRetryCount++;
			return myRetryCount;
		}

		public int getRetriedResourceCount() {
			return myRetriedResourceCount;
		}

		public void addRetriedResourceCount(int theIncrement) {
			myRetriedResourceCount += theIncrement;
		}

		public Collection<PidAndResource> getUnchangedResources() {
			return myUnchangedResources;
		}

		public Collection<PidAndResource> getChangedUnsavedResourcesAndMoveToPending() {
			List<PidAndResource> retVal = List.copyOf(myChangedUnsavedResources);
			myChangedUnsavedResources.clear();
			myPendingSavedResources.addAll(retVal);
			return retVal;
		}

		public Collection<PidAndResource> getChangedSavedResources() {
			return myChangedSavedResources;
		}

		public void addChangedSavedResources(Collection<PidAndResource> theResources) {
			int previousSize = myChangedSavedResources.size();
			myChangedSavedResources.addAll(theResources);
			Validate.isTrue(
					myChangedSavedResources.size() == previousSize + theResources.size(), "Added duplicate resources");
		}

		public void addFailure(PidAndResource theResource, String theMessage) {
			String previousValue = myFailures.put(theResource, theMessage);
			Validate.isTrue(previousValue == null, "%s is already present", theResource);
		}

		public Map<PidAndResource, String> getFailures() {
			return myFailures;
		}

		public void addUnchangedResource(PidAndResource theResource) {
			boolean added = myUnchangedResources.add(theResource);
			Validate.isTrue(added, "%s is already present", theResource);
		}

		public void addChangedUnsavedResource(PidAndResource theResource) {
			boolean added = myChangedUnsavedResources.add(theResource);
			Validate.isTrue(added, "%s is already present", theResource);
		}

		public void movePendingToSaved() {
			addChangedSavedResources(myPendingSavedResources);
			myPendingSavedResources.clear();
		}

		public void movePendingBackToModificationList() {
			List<TypedPidAndVersionJson> pids =
					myPendingSavedResources.stream().map(PidAndResource::pid).toList();
			myPendingSavedResources.clear();

			myPidsToModify.addAll(pids);
		}

		public List<TypedPidAndVersionJson> getPidsToModifyAndClear() {
			List<TypedPidAndVersionJson> retVal = List.copyOf(myPidsToModify);
			myPidsToModify.clear();
			return retVal;
		}

		public boolean hasPidsToModify() {
			return !myPidsToModify.isEmpty();
		}

		public void movePendingToFailed(String theMessage) {
			myPendingSavedResources.forEach(r -> addFailure(r, theMessage));
			myPendingSavedResources.clear();
		}

		public int countPidsToModify() {
			return myPidsToModify.size();
		}

		public void setJobFailure(JobExecutionFailedException theJobFailure) {
			myJobFailure = theJobFailure;
		}

		public JobExecutionFailedException getJobFailure() {
			return myJobFailure;
		}
	}

	private record PidAndResource(TypedPidAndVersionJson pid, IBaseResource resource) {}

	@SuppressWarnings("UnstableApiUsage")
	private class HashingWriter extends Writer {

		private final Hasher myHasher = Hashing.goodFastHash(128).newHasher();

		@Override
		public void write(@Nonnull char[] cbuf, final int off, final int len) {
			for (int i = off; i < off + len; i++) {
				myHasher.putChar(cbuf[i]);
			}
		}

		@Override
		public void flush() {
			// nothing
		}

		@Override
		public void close() {
			// nothing
		}

		public void append(IBaseResource theResource) {
			try {
				myFhirContext.newJsonParser().setPrettyPrint(false).encodeResourceToWriter(theResource, this);
			} catch (IOException e) {
				// This shouldn't happen since we don't do any IO in this writer
				throw new JobExecutionFailedException(Msg.code(2785) + "Failed to calculate resource hash", e);
			}
		}

		public boolean matches(HashingWriter thePostModificationHash) {
			return myHasher.hash().equals(thePostModificationHash.myHasher.hash());
		}
	}
}
