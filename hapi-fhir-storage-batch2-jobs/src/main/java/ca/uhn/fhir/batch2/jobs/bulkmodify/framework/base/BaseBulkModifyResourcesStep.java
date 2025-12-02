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
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;
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
	private IFhirSystemDao<?, ?> mySystemDao;

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

		List<TypedPidAndVersionJson> pids = theStepExecutionDetails.getData().getTypedPidAndVersions();
		PT jobParameters = theStepExecutionDetails.getParameters();
		RequestPartitionId requestPartitionId =
				theStepExecutionDetails.getData().getRequestPartitionId();

		State state = new State();
		state.addPids(pids);

		// Try to update the whole chunk in a single database transaction for fast performance
		processPids(jobParameters, state, pids, requestPartitionId);

		// If our attempt to process as a single DB transaction failed, we will try each
		// resource individually in a separate DB transaction so that any failures on one
		// resource don't block another from succeeding
		state.moveFailedResourcesBackToInitialState();
		if (state.hasPidsInInitialState()) {
			for (int retryCount = state.incrementAndGetRetryCount();
					retryCount <= MAX_RETRIES;
					retryCount = state.incrementAndGetRetryCount()) {
				state.addRetriedResourceCount(state.countPidsToModify());

				while (state.hasPidsInInitialState()) {
					List<TypedPidAndVersionJson> singlePidList = state.getSinglePidInState(StateEnum.INITIAL);
					processPids(jobParameters, state, singlePidList, requestPartitionId);
				}

				boolean finalRetry = retryCount == MAX_RETRIES;
				if (!finalRetry) {
					state.moveFailedResourcesBackToInitialState();
				}
			}
		}

		Validate.isTrue(
				!state.hasPidsInInitialState(),
				"PIDs remain in INITIAL state, this is a bug: %s",
				state.getPidsInState(StateEnum.INITIAL));

		BulkModifyResourcesChunkOutcomeJson outcome = generateOutcome(jobParameters, state);
		theDataSink.accept(outcome);

		return RunOutcome.SUCCESS;
	}

	/**
	 * @param thePids If <code>true</code>, attempt to modify all resources in the {@link State}. If <code>false</code>, only attempt to modify a single resource.
	 */
	private void processPids(
			PT theJobParameters,
			State theState,
			List<TypedPidAndVersionJson> thePids,
			RequestPartitionId theRequestPartitionId) {
		HapiTransactionService.noTransactionAllowed();

		final TransactionDetails transactionDetails = new TransactionDetails();
		try {

			myTransactionService
					.withSystemRequestOnPartition(theRequestPartitionId)
					.withTransactionDetails(transactionDetails)
					.readOnly(theJobParameters.isDryRun())
					.execute(() -> processPidsInTransaction(theJobParameters, theState, thePids, transactionDetails));

			// Storage transaction succeeded
			theState.movePendingToSaved();

		} catch (JobExecutionFailedException e) {
			throw e;
		} catch (Exception e) {
			String failureMessage = e.toString();
			ourLog.warn("Failure occurred during bulk modification. Failure: {}", failureMessage);
			for (TypedPidAndVersionJson pid : thePids) {
				theState.moveToFailure(pid, failureMessage);
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
		fetchResourcesInTransaction(theState, thePids);

		// Perform the modification (handled by subclasses)
		C modificationContext;
		try {
			modificationContext = modifyResourcesInTransaction(theState, theJobParameters, thePids);
		} catch (JobExecutionFailedException e) {
			theState.setJobFailure(e);
			throw e;
		}

		// Store the modified resources to the DB
		if (!theJobParameters.isDryRun()) {
			storeResourcesInTransaction(modificationContext, theState, thePids, theTransactionDetails);
		} else {
			theState.moveUnsavedToSaved();
		}
	}

	/**
	 * Fetches the given resources by PID from the database, and stores the fetched
	 * resources in the {@link State}.
	 */
	private void fetchResourcesInTransaction(State theState, List<TypedPidAndVersionJson> thePids) {
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

					// Current version
					theState.setResourceForPid(typedVersionedPid, resource);

				} else {

					// If we're fetching specific versions of resources, we fetch them
					// individually here. This could be made more efficient with some kind
					// of bulk versioned fetch in the future.
					IIdType nextVersionedId = resource.getIdElement().withVersion(Long.toString(nextVersion));
					IBaseResource resourceVersion = dao.read(nextVersionedId, new SystemRequestDetails(), true);

					// Ignore deleted resources
					if (ResourceMetadataKeyEnum.DELETED_AT.get(resourceVersion) != null) {
						continue;
					}

					theState.setResourceForPid(typedVersionedPid, resourceVersion);
				}
			}
		}
	}

	private C modifyResourcesInTransaction(State theState, PT theJobParameters, List<TypedPidAndVersionJson> thePids) {
		assert TransactionSynchronizationManager.isActualTransactionActive();

		C modificationContext = preModifyResources(theJobParameters, thePids);

		for (TypedPidAndVersionJson pid : thePids) {
			IBaseResource resource = theState.getResourceForPid(pid);
			if (resource == null) {
				continue;
			}
			String resourceType = myFhirContext.getResourceType(resource);
			String resourceId = resource.getIdElement().getIdPart();
			String resourceVersion = resource.getIdElement().getVersionIdPart();

			try (HashingWriter preModificationHash = hashResource(resource)) {

				ResourceModificationRequest modificationRequest = new ResourceModificationRequest(resource);
				ResourceModificationResponse modificationResponse =
						modifyResource(theJobParameters, modificationContext, modificationRequest);
				if (modificationResponse == null) {
					throw new JobExecutionFailedException(Msg.code(2789)
							+ "Null response from Modification for Resource[" + resource.getIdElement() + "]");
				}

				if (modificationResponse.isDeleted()) {
					theState.moveToState(pid, StateEnum.DELETED_UNSAVED);
					continue;
				}

				IBaseResource updatedResource = modificationResponse.getResource();
				if (updatedResource == null) {
					theState.moveToState(pid, StateEnum.UNCHANGED);
					continue;
				}

				if (!resourceType.equals(myFhirContext.getResourceType(updatedResource))) {
					throw new JobExecutionFailedException(Msg.code(2782) + "Modification for Resource["
							+ resource.getIdElement() + "] returned wrong resource type, expected " + resourceType
							+ " but was " + myFhirContext.getResourceType(updatedResource));
				}
				if (!resourceId.equals(updatedResource.getIdElement().getIdPart())) {
					throw new JobExecutionFailedException(Msg.code(2783) + "Modification for Resource[" + resourceType
							+ "/" + resourceId + "] attempted to change the resource ID");
				}
				if (!resourceVersion.equals(updatedResource.getIdElement().getVersionIdPart())) {
					throw new JobExecutionFailedException(Msg.code(2784) + "Modification for Resource[" + resourceType
							+ "/" + resourceId + "] attempted to change the resource version");
				}

				HashingWriter postModificationHash = hashResource(resource);

				if (preModificationHash.matches(postModificationHash)) {
					theState.moveToState(pid, StateEnum.UNCHANGED);
				} else {
					theState.moveToState(pid, StateEnum.CHANGED_UNSAVED);
					theState.setResourceForPid(pid, updatedResource);
				}
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
			C theModificationContext,
			State theState,
			List<TypedPidAndVersionJson> thePids,
			TransactionDetails theTransactionDetails) {
		assert TransactionSynchronizationManager.isActualTransactionActive();

		for (TypedPidAndVersionJson pid : thePids) {

			// Changed resources
			if (theState.isPidInState(pid, StateEnum.CHANGED_UNSAVED)) {
				IBaseResource resource = theState.getResourceForPid(pid);
				Validate.isTrue(resource != null, "Resource for PID[%s] is null", pid);
				IFhirResourceDao<IBaseResource> dao = myDaoRegistry.getResourceDao(resource);

				SystemRequestDetails requestDetails = createRequestDetails(theModificationContext, resource);

				DaoMethodOutcome outcome =
						dao.update(resource, null, true, false, requestDetails, theTransactionDetails);
				ourLog.debug(
						"Storage for PID[{}] resulted in version: {}",
						pid,
						outcome.getId().getVersionIdPart());
				theState.moveToState(pid, StateEnum.CHANGED_PENDING);
			}

			// Deleted resources
			if (theState.isPidInState(pid, StateEnum.DELETED_UNSAVED)) {
				IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(pid.getResourceType());
				IBaseResource resource = theState.getResourceForPid(pid);
				Validate.isTrue(resource != null, "Resource for PID[%s] is null", pid);

				SystemRequestDetails requestDetails = createRequestDetails(theModificationContext, resource);
				if (requestDetails.isRewriteHistory()) {
					throw new JobExecutionFailedException(
							Msg.code(2806) + "Can't store deleted resources as history rewrites");
				}

				IIdType resourceId = resource.getIdElement();
				DaoMethodOutcome outcome = dao.delete(resourceId, requestDetails);

				ourLog.debug(
						"Deletion for PID[{}] resulted in version: {}",
						pid,
						outcome.getId().getVersionIdPart());
				theState.moveToState(pid, StateEnum.DELETED_PENDING);
			}
		}
	}

	@Nonnull
	private SystemRequestDetails createRequestDetails(C theModificationContext, IBaseResource theResource) {
		SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setRewriteHistory(isRewriteHistory(theModificationContext, theResource));
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
	 * of a resource batch. It can be used to perform any shared processing that would otherwise need
	 * to be repeated, such as looking up context resources, parsing a patch object in the job parameters
	 * or other expensive operations.
	 *
	 * @param theJobParameters The parameters for the current instance of the job
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
	public void setSystemDaoForUnitTest(IFhirSystemDao<?, ?> theSystemDao) {
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
	private BulkModifyResourcesChunkOutcomeJson generateOutcome(
			BaseBulkModifyJobParameters theJobParameters, State theState) {
		BulkModifyResourcesChunkOutcomeJson outcome = new BulkModifyResourcesChunkOutcomeJson();

		outcome.setChunkRetryCount(theState.getRetryCount());
		outcome.setResourceRetryCount(theState.getRetriedResourceCount());

		for (TypedPidAndVersionJson next : theState.getPidsInState(StateEnum.CHANGED_SAVED)) {
			IBaseResource resource = theState.getResourceForPidNotNull(next);
			if (theJobParameters.isDryRun()) {
				if (theJobParameters.getDryRunMode() == BaseBulkModifyJobParameters.DryRunMode.COLLECT_CHANGED) {
					outcome.addChangedResourceBody(myFhirContext.newJsonParser().encodeResourceToString(resource));
				}
			}

			outcome.addChangedId(resource.getIdElement());
		}
		for (TypedPidAndVersionJson next : theState.getPidsInState(StateEnum.DELETED_SAVED)) {
			IBaseResource resource = theState.getResourceForPidNotNull(next);
			outcome.addDeletedId(resource.getIdElement());
		}
		for (TypedPidAndVersionJson next : theState.getPidsInState(StateEnum.UNCHANGED)) {
			IBaseResource resource = theState.getResourceForPidNotNull(next);
			outcome.addUnchangedId(resource.getIdElement());
		}
		for (Map.Entry<TypedPidAndVersionJson, String> next :
				theState.getFailures().entrySet()) {
			IBaseResource resource = theState.getResourceForPidNotNull(next.getKey());
			outcome.addFailure(resource.getIdElement(), next.getValue());
		}
		return outcome;
	}

	private enum StateEnum {

		/**
		 * Resource has not yet been fetched or modified, only the PID is present
		 */
		INITIAL,
		/**
		 * Resource was not modified and doesn't need to be written to the DB
		 */
		UNCHANGED,
		/**
		 * Resource was modified and needs to be written to the DB
		 */
		CHANGED_UNSAVED,
		/**
		 * Resource was modified and written to the DB, transaction is not yet committed
		 */
		CHANGED_PENDING,
		/**
		 * Resource was modified and successfully written to the DB
		 */
		CHANGED_SAVED,
		/**
		 * Resource was deleted and needs to be written to the DB
		 */
		DELETED_UNSAVED,
		/**
		 * Resource was deleted and written to the DB, transaction is not yet committed
		 */
		DELETED_PENDING,
		/**
		 * Resource was deleted and successfully written to the DB
		 */
		DELETED_SAVED,
		/**
		 * Resource modification was not successful
		 */
		FAILED;

		public StateEnum toSaved() {
			return switch (this) {
				case CHANGED_UNSAVED, CHANGED_PENDING -> CHANGED_SAVED;
				case DELETED_UNSAVED, DELETED_PENDING -> DELETED_SAVED;
				default -> throw new IllegalStateException(Msg.code(2808) + "Can't convert " + this + " to saved");
			};
		}

		public static Set<StateEnum> unsavedStates() {
			return EnumSet.of(CHANGED_UNSAVED, DELETED_UNSAVED);
		}

		public static Set<StateEnum> pendingStates() {
			return EnumSet.of(CHANGED_PENDING, DELETED_PENDING);
		}
	}

	/**
	 * This object contains the current state for a single batch/execution of this step
	 */
	private static class State {

		private final Map<TypedPidAndVersionJson, StateEnum> myPidToState = new HashMap<>();
		private final Map<TypedPidAndVersionJson, IBaseResource> myPidToResource = new HashMap<>();
		private final SetMultimap<StateEnum, TypedPidAndVersionJson> myStateToPids =
				MultimapBuilder.enumKeys(StateEnum.class).hashSetValues().build();
		private final Map<TypedPidAndVersionJson, String> myPidToFailure = new HashMap<>();
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

		public void addPids(List<TypedPidAndVersionJson> thePids) {
			for (TypedPidAndVersionJson next : thePids) {
				addPid(next);
			}
		}

		/**
		 * Adds a resource in {@link StateEnum#INITIAL} state.
		 */
		public void addPid(TypedPidAndVersionJson thePid) {
			Validate.isTrue(
					!myPidToState.containsKey(thePid),
					() -> "Resource " + thePid + " already present in state " + myPidToState.get(thePid));
			myStateToPids.put(StateEnum.INITIAL, thePid);
			myPidToState.put(thePid, StateEnum.INITIAL);
		}

		public void moveToFailure(TypedPidAndVersionJson thePid, String theMessage) {
			String previousValue = myPidToFailure.put(thePid, theMessage);
			Validate.isTrue(previousValue == null, "%s is already present", thePid);

			moveToState(thePid, StateEnum.FAILED);
		}

		public void moveToState(TypedPidAndVersionJson thePid, StateEnum theNewState) {
			StateEnum previousState = myPidToState.get(thePid);
			Validate.notNull(previousState, "Resource %s is not present", thePid);

			myPidToState.put(thePid, theNewState);
			myStateToPids.remove(previousState, thePid);
			myStateToPids.put(theNewState, thePid);
		}

		public Map<TypedPidAndVersionJson, String> getFailures() {
			return myPidToFailure;
		}

		public void movePendingToSaved() {
			for (StateEnum state : StateEnum.pendingStates()) {
				StateEnum saved = state.toSaved();
				assert saved != null;
				List<TypedPidAndVersionJson> pids = getPidsInState(state);
				for (TypedPidAndVersionJson pid : pids) {
					moveToState(pid, saved);
				}
			}
		}

		public void moveFailedResourcesBackToInitialState() {
			List<TypedPidAndVersionJson> failed = getPidsInState(StateEnum.FAILED);
			for (TypedPidAndVersionJson pid : failed) {
				moveToState(pid, StateEnum.INITIAL);
				myPidToResource.remove(pid);
				myPidToFailure.remove(pid);
			}
		}

		public List<TypedPidAndVersionJson> getPidsInState(StateEnum theState) {
			return List.copyOf(myStateToPids.get(theState));
		}

		public List<TypedPidAndVersionJson> getSinglePidInState(StateEnum theState) {
			TypedPidAndVersionJson pid = myStateToPids.get(theState).iterator().next();
			Validate.notNull(pid, "No PIDs in state %s", theState);
			return List.of(pid);
		}

		public boolean hasPidsInInitialState() {
			return !myStateToPids.get(StateEnum.INITIAL).isEmpty();
		}

		public int countPidsToModify() {
			return myStateToPids.get(StateEnum.INITIAL).size();
		}

		public JobExecutionFailedException getJobFailure() {
			return myJobFailure;
		}

		public void setJobFailure(JobExecutionFailedException theJobFailure) {
			myJobFailure = theJobFailure;
		}

		public void moveUnsavedToSaved() {
			for (StateEnum state : StateEnum.unsavedStates()) {
				StateEnum newState = state.toSaved();
				assert newState != null;
				for (TypedPidAndVersionJson pid : getPidsInState(state)) {
					moveToState(pid, newState);
				}
			}
		}

		public void setResourceForPid(TypedPidAndVersionJson thePid, IBaseResource theResourceVersion) {
			Validate.isTrue(myPidToState.containsKey(thePid), "Pid %s is not present", thePid);
			myPidToResource.put(thePid, theResourceVersion);
		}

		/**
		 * This can be <code>null</code> if the resource associated with the given PID was found
		 * to be deleted.
		 */
		@Nullable
		public IBaseResource getResourceForPid(TypedPidAndVersionJson thePid) {
			return myPidToResource.get(thePid);
		}

		/**
		 * This can be <code>null</code>n if the resource associated with the given PID was found
		 * to be deleted.
		 */
		@Nonnull
		public IBaseResource getResourceForPidNotNull(TypedPidAndVersionJson thePid) {
			IBaseResource retVal = myPidToResource.get(thePid);
			Validate.notNull(retVal, "Resource for PID %s is null", thePid);
			return retVal;
		}

		public boolean isPidInState(@Nonnull TypedPidAndVersionJson thePid, @Nonnull StateEnum theStateEnum) {
			return theStateEnum.equals(myPidToState.get(thePid));
		}
	}

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
