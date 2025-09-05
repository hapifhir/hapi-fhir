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
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ListMultimap;
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
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This is the base class for any <b>bulk modification</b> or <b>bulk rewrite</b> jobs. Any new bulk modification
 * job type should subclass this class, and implement {@link #modifyResource(BaseBulkModifyJobParameters, Object, ResourceModificationRequest)}.
 * Several other methods are provided which can also be overridden.
 *
 * @param <PT> The job parameters type
 * @param <C>  A context object which will be passed between methods for a single chunk of resources. The format of the
 *             context object is up to the subclass, the framework doesn't look at it.
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

		} catch (JobExecutionFailedException e) {
			throw e;
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
		List<PidAndResource> pids = theState.getPidsToModifyAndClear();

		for (PidAndResource nextPid : pids) {
			processPids(theJobParameters, theState, List.of(nextPid.pid()), theRequestPartitionId, theFinalRetry);
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

			if (modificationResponse.isDeleted()) {
				theState.addResource(StateEnum.DELETED_UNSAVED, new PidAndResource(pid, resource));
				continue;
			}

			IBaseResource updatedResource = modificationResponse.getResource();
			if (updatedResource == null) {
				theState.addResource(StateEnum.UNCHANGED, new PidAndResource(pid, resource));
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
				theState.addResource(StateEnum.UNCHANGED, new PidAndResource(pid, updatedResource));
			} else {
				theState.addResource(StateEnum.CHANGED_UNSAVED, new PidAndResource(pid, updatedResource));
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

		// Changed resources
		for (PidAndResource pidAndResource : theState.getResourcesInStateAndMoveToPending(StateEnum.CHANGED_UNSAVED)) {
			IFhirResourceDao<IBaseResource> dao = myDaoRegistry.getResourceDao(pidAndResource.resource());

			SystemRequestDetails requestDetails = createRequestDetails(theModificationContext, pidAndResource);

			DaoMethodOutcome outcome =
					dao.update(pidAndResource.resource(), null, true, false, requestDetails, theTransactionDetails);
			ourLog.debug(
					"Storage for PID[{}] resulted in version: {}",
					pidAndResource.pid,
					outcome.getId().getVersionIdPart());
		}

		// Deleted resources
		for (PidAndResource pidAndResource : theState.getResourcesInStateAndMoveToPending(StateEnum.DELETED_UNSAVED)) {
			IFhirResourceDao<IBaseResource> dao = myDaoRegistry.getResourceDao(pidAndResource.resource());

			SystemRequestDetails requestDetails = createRequestDetails(theModificationContext, pidAndResource);
			if (requestDetails.isRewriteHistory()) {
				throw new JobExecutionFailedException(
						Msg.code(2806) + "Can't store deleted resources as history rewrites");
			}

			IIdType resourceId = pidAndResource.resource().getIdElement();
			DaoMethodOutcome outcome = dao.delete(resourceId, requestDetails);

			ourLog.debug(
					"Deletion for PID[{}] resulted in version: {}",
					pidAndResource.pid,
					outcome.getId().getVersionIdPart());
		}
	}

	@Nonnull
	private SystemRequestDetails createRequestDetails(C theModificationContext, PidAndResource pidAndResource) {
		SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setRewriteHistory(isRewriteHistory(theModificationContext, pidAndResource.resource()));
		return requestDetails;
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

	@VisibleForTesting
	public void setTransactionServiceForUnitTest(IHapiTransactionService theTransactionService) {
		assert theTransactionService != null;
		myTransactionService = theTransactionService;
	}

	@VisibleForTesting
	public void setDaoRegistryForUnitTest(DaoRegistry theDaoRegistry) {
		assert theDaoRegistry != null;
		myDaoRegistry = theDaoRegistry;
	}

	@VisibleForTesting
	public void setSystemDaoForUnitTest(IFhirSystemDao theSystemDao) {
		assert theSystemDao != null;
		mySystemDao = theSystemDao;
	}

	@VisibleForTesting
	public void setIdHelperServiceForUnitTest(IIdHelperService<IResourcePersistentId<?>> theIdHelperService) {
		assert theIdHelperService != null;
		myIdHelperService = theIdHelperService;
	}

	@VisibleForTesting
	public void setFhirContextForUnitTest(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
	}

	@Nonnull
	private static BulkModifyResourcesChunkOutcomeJson generateOutcome(State theState) {
		BulkModifyResourcesChunkOutcomeJson outcome = new BulkModifyResourcesChunkOutcomeJson();

		outcome.setChunkRetryCount(theState.getRetryCount());
		outcome.setResourceRetryCount(theState.getRetriedResourceCount());

		for (PidAndResource next : theState.getResourcesInState(StateEnum.CHANGED_SAVED)) {
			outcome.addChangedId(next.resource().getIdElement());
		}
		for (PidAndResource next : theState.getResourcesInState(StateEnum.DELETED_SAVED)) {
			outcome.addDeletedId(next.resource().getIdElement());
		}
		for (PidAndResource next : theState.getResourcesInState(StateEnum.UNCHANGED)) {
			outcome.addUnchangedId(next.resource().getIdElement());
		}
		for (Map.Entry<PidAndResource, String> next : theState.getFailures().entrySet()) {
			outcome.addFailure(next.getKey().resource().getIdElement(), next.getValue());
		}
		return outcome;
	}

	private enum StateEnum {

		/** Resource has not yet been fetched or modified, only the PID is present */
		INITIAL,
		/** Resource was not modified and doesn't need to be written to the DB */
		UNCHANGED,
		/** Resource was modified and needs to be written to the DB */
		CHANGED_UNSAVED,
		/** Resource was modified and written to the DB, transaction is not yet committed */
		CHANGED_PENDING,
		/** Resource was modified and successfully written to the DB */
		CHANGED_SAVED,
		/** Resource was deleted and needs to be written to the DB */
		DELETED_UNSAVED,
		/** Resource was deleted and written to the DB, transaction is not yet committed */
		DELETED_PENDING,
		/** Resource was deleted and successfully written to the DB */
		DELETED_SAVED;

		public StateEnum toPending() {
			return switch (this) {
				case CHANGED_UNSAVED -> CHANGED_PENDING;
				case DELETED_UNSAVED -> DELETED_PENDING;
				default -> throw new IllegalStateException(Msg.code(2807) + "Can't convert " + this + " to pending");
			};
		}

		public StateEnum toSaved() {
			return switch (this) {
				case CHANGED_PENDING -> CHANGED_SAVED;
				case DELETED_PENDING -> DELETED_SAVED;
				default -> throw new IllegalStateException(Msg.code(2808) + "Can't convert " + this + " to saved");
			};
		}

		public static Set<StateEnum> pendingStates() {
			return EnumSet.of(CHANGED_PENDING, DELETED_PENDING);
		}
	}

	private static class State {

		//		private final List<TypedPidAndVersionJson> myPidsToModify = new ArrayList<>();
		private ListMultimap<StateEnum, PidAndResource> myStateMap =
				MultimapBuilder.enumKeys(StateEnum.class).arrayListValues().build();
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

		public List<PidAndResource> getResourcesInState(StateEnum theState) {
			return List.copyOf(myStateMap.get(theState));
		}

		public List<PidAndResource> getResourcesInStateAndClear(StateEnum theState) {
			return List.copyOf(myStateMap.removeAll(theState));
		}

		public List<PidAndResource> getResourcesInStateAndMoveToPending(StateEnum theState) {
			List<PidAndResource> retVal = myStateMap.removeAll(theState);
			StateEnum nextState = theState.toPending();
			myStateMap.putAll(nextState, retVal);
			return retVal;
		}

		public void addResource(StateEnum theState, PidAndResource theResource) {
			assert !myStateMap.containsEntry(theState, theResource);
			myStateMap.put(theState, theResource);
		}

		public void addFailure(PidAndResource theResource, String theMessage) {
			String previousValue = myFailures.put(theResource, theMessage);
			Validate.isTrue(previousValue == null, "%s is already present", theResource);
		}

		public Map<PidAndResource, String> getFailures() {
			return myFailures;
		}

		public void movePendingToSaved() {
			for (StateEnum state : StateEnum.pendingStates()) {
				List<PidAndResource> pidsAndResources = getResourcesInStateAndClear(state);
				myStateMap.putAll(state.toSaved(), pidsAndResources);
			}
		}

		public void movePendingBackToModificationList() {
			for (StateEnum state : StateEnum.pendingStates()) {
				List<PidAndResource> pidsAndResources = getResourcesInStateAndClear(state);
				pidsAndResources.forEach(r -> addResource(StateEnum.INITIAL, r));
			}
		}

		public void movePendingToFailed(String theMessage) {
			for (StateEnum state : StateEnum.pendingStates()) {
				List<PidAndResource> pidsAndResources = getResourcesInStateAndClear(state);
				pidsAndResources.forEach(r -> addFailure(r, theMessage));
			}
		}

		public List<PidAndResource> getPidsToModifyAndClear() {
			return getResourcesInStateAndClear(StateEnum.INITIAL);
		}

		public boolean hasPidsToModify() {
			return !myStateMap.get(StateEnum.INITIAL).isEmpty();
		}

		public int countPidsToModify() {
			return myStateMap.get(StateEnum.INITIAL).size();
		}

		public JobExecutionFailedException getJobFailure() {
			return myJobFailure;
		}

		public void setJobFailure(JobExecutionFailedException theJobFailure) {
			myJobFailure = theJobFailure;
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
