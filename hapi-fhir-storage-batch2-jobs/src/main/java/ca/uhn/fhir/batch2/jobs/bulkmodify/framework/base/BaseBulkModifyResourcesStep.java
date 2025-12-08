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
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.common.BulkModifyResourcesChunkOutcomeJson;
import ca.uhn.fhir.batch2.jobs.chunk.TypedPidAndVersionJson;
import ca.uhn.fhir.batch2.jobs.chunk.TypedPidAndVersionListWorkChunkJson;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.util.StopWatch;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * This is the base class for any <b>bulk modification</b> or <b>bulk rewrite</b> jobs.
 * <p>
 * Bulk modification/rewrite jobs should subclass {@link BaseBulkModifyResourcesIndividuallyStep} if they want to make modifications
 * to individual resources one-at-a-time. If they want to make modifications to multiple resources in a batch, they should subclass
 * this class and implement {@link #processPidsInTransaction(String, String, BaseBulkModifyJobParameters, State, List, TransactionDetails, IJobDataSink)}
 * </p>
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
	protected DaoRegistry myDaoRegistry;

	@Autowired
	protected IFhirSystemDao<?, ?> mySystemDao;

	@Autowired
	protected IIdHelperService<IResourcePersistentId<?>> myIdHelperService;

	@Autowired
	protected FhirContext myFhirContext;

	@Autowired
	private IHapiTransactionService myTransactionService;

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

		String instanceId = theStepExecutionDetails.getInstance().getInstanceId();
		String chunkId = theStepExecutionDetails.getChunkId();
		ourLog.info(
				"Starting {} work chunk with {} resources - Instance[{}] Chunk[{}]",
				getJobNameForLogging(),
				pids.size(),
				instanceId,
				chunkId);
		StopWatch sw = new StopWatch();

		PT jobParameters = theStepExecutionDetails.getParameters();
		RequestPartitionId requestPartitionId =
				theStepExecutionDetails.getData().getRequestPartitionId();

		State state = new State();
		state.addPids(pids);

		// Try to update the whole chunk in a single database transaction for fast performance
		processPids(instanceId, chunkId, jobParameters, state, pids, requestPartitionId, theDataSink);

		// If our attempt to process as a single DB transaction failed, we will try each
		// resource individually in a separate DB transaction so that any failures on one
		// resource don't block another from succeeding
		state.moveFailedResourcesBackToInitialState();
		if (state.hasPidsInInitialState()) {
			while (true) {
				int retryCount = state.incrementAndGetRetryCount();
				state.addRetriedResourceCount(state.countPidsToModify());

				while (state.hasPidsInInitialState()) {
					TypedPidAndVersionJson singlePid = state.getSinglePidInState(StateEnum.INITIAL);
					processPids(
							instanceId,
							chunkId,
							jobParameters,
							state,
							List.of(singlePid),
							requestPartitionId,
							theDataSink);
				}

				if (retryCount == MAX_RETRIES) {
					break;
				} else {
					state.moveFailedResourcesBackToInitialState();
				}
			}
		}

		Validate.isTrue(
				!state.hasPidsInInitialState(),
				"PIDs remain in INITIAL state, this is a bug: %s",
				state.getPidsInState(StateEnum.INITIAL));

		ourLog.info(
				"Finished {} work chunk with {} resources in {} - {}/sec - Instance[{}] Chunk[{}]",
				getJobNameForLogging(),
				pids.size(),
				sw,
				sw.formatThroughput(pids.size(), TimeUnit.SECONDS),
				instanceId,
				chunkId);

		BulkModifyResourcesChunkOutcomeJson outcome = generateOutcome(jobParameters, state);
		theDataSink.accept(outcome);

		return new RunOutcome(pids.size());
	}

	/**
	 * @param thePids If <code>true</code>, attempt to modify all resources in the {@link State}. If <code>false</code>, only attempt to modify a single resource.
	 */
	private void processPids(
			String theInstanceId,
			String theChunkId,
			PT theJobParameters,
			State theState,
			List<TypedPidAndVersionJson> thePids,
			RequestPartitionId theRequestPartitionId,
			IJobDataSink<BulkModifyResourcesChunkOutcomeJson> theDataSink) {
		HapiTransactionService.noTransactionAllowed();

		final TransactionDetails transactionDetails = new TransactionDetails();
		try {

			processPidsOutsideTransaction(
					theInstanceId, theChunkId, theJobParameters, theState, thePids, transactionDetails, theDataSink);

			myTransactionService
					.withSystemRequestOnPartition(theRequestPartitionId)
					.withTransactionDetails(transactionDetails)
					.readOnly(theJobParameters.isDryRun())
					.execute(() -> processPidsInTransaction(
							theInstanceId,
							theChunkId,
							theJobParameters,
							theState,
							thePids,
							transactionDetails,
							theDataSink));

			// Storage transaction succeeded
			theState.movePendingToSaved();

		} catch (JobExecutionFailedException e) {
			throw e;
		} catch (Throwable e) {
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

	/**
	 * For each group of PIDs, this method is called outside of any FHIR transaction, prior to
	 * {@link #processPidsInTransaction(String, String, BaseBulkModifyJobParameters, State, List, TransactionDetails, IJobDataSink)}
	 * being called. It can handle any pre-processing that needs to happen outside of a DB transaction.
	 *
	 * @param theInstanceId         The job instance ID
	 * @param theChunkId            The work chunk ID
	 * @param theJobParameters      The job parameters for this job instance
	 * @param theState              The modification state object, which must be updated for all PIDs
	 * @param thePids               The PIDs to modify.
	 * @param theTransactionDetails A TransactionDetails associated with the current DB transaction.
	 * @param theDataSink           The sink that will ultimately be written to
	 * @see #processPidsInTransaction(String, String, BaseBulkModifyJobParameters, State, List, TransactionDetails, IJobDataSink)
	 */
	protected void processPidsOutsideTransaction(
			String theInstanceId,
			String theChunkId,
			PT theJobParameters,
			State theState,
			List<TypedPidAndVersionJson> thePids,
			TransactionDetails theTransactionDetails,
			IJobDataSink<BulkModifyResourcesChunkOutcomeJson> theDataSink) {
		// nothing
	}

	/**
	 * Subclasses should implement this method to perform the actual bulk modification work.
	 * This method is responsible for handling all PIDs passed in via {@literal thePids}, and
	 * for updating their state {@literal theState} once that has happened. No other resources
	 * in {@literal theState} should be modified by this method.
	 * <p>
	 * If you need to perform modifications on resources one-by-one (as opposed to as a large
	 * group), you should override {@link BaseBulkModifyResourcesIndividuallyStep} instead of
	 * this class.
	 * </p>
	 *
	 * @param theInstanceId         The job instance ID
	 * @param theChunkId            The work chunk ID
	 * @param theJobParameters      The job parameters for this job instance
	 * @param theState              The modification state object, which must be updated for all PIDs
	 * @param thePids               The PIDs to modify.
	 * @param theTransactionDetails A TransactionDetails associated with the current DB transaction.
	 * @param theDataSink           The sink that will ultimately be written to
	 * @see #processPidsOutsideTransaction(String, String, BaseBulkModifyJobParameters, State, List, TransactionDetails, IJobDataSink)
	 */
	protected abstract void processPidsInTransaction(
			String theInstanceId,
			String theChunkId,
			PT theJobParameters,
			State theState,
			List<TypedPidAndVersionJson> thePids,
			TransactionDetails theTransactionDetails,
			IJobDataSink<BulkModifyResourcesChunkOutcomeJson> theDataSink);

	/**
	 * Subclasses may override this method to indicate that this resource should be stored
	 * as a history rewrite
	 */
	@SuppressWarnings("unused")
	protected boolean isRewriteHistory(C theState, IBaseResource theResource) {
		return false;
	}

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
			if (theJobParameters.isDryRun()) {
				if (theJobParameters.getDryRunMode() == BaseBulkModifyJobParameters.DryRunMode.COLLECT_CHANGED) {
					IBaseResource resource = theState.getResourceForPid(next);
					outcome.addChangedResourceBody(myFhirContext.newJsonParser().encodeResourceToString(resource));
				}
			}

			outcome.addChangedId(toId(next, theState));
		}
		for (TypedPidAndVersionJson next : theState.getPidsInState(StateEnum.DELETED_SAVED)) {
			outcome.addDeletedId(toId(next, theState));
		}
		for (TypedPidAndVersionJson next : theState.getPidsInState(StateEnum.UNCHANGED)) {
			outcome.addUnchangedId(toId(next, theState));
		}
		for (Map.Entry<TypedPidAndVersionJson, String> next :
				theState.getFailures().entrySet()) {
			outcome.addFailure(toId(next.getKey(), theState), next.getValue());
		}
		return outcome;
	}

	private IIdType toId(TypedPidAndVersionJson thePid, State theState) {
		IIdType retVal = theState.getResourceIdForPidOrNull(thePid);
		if (retVal == null) {
			IResourcePersistentId<?> persistentId = thePid.toTypedPid().toPersistentId(myIdHelperService);
			retVal = myIdHelperService.translatePidIdToForcedId(myFhirContext, thePid.getResourceType(), persistentId);
			if (thePid.getVersionId() != null) {
				retVal = retVal.withVersion(Long.toString(thePid.getVersionId()));
			}
		}
		return retVal;
	}

	/**
	 * Subclasses should provide a meaningful name for the job that will be used in logging
	 * updates (FHIR operation name should be enough, e.g. "$hapi.fhir.bulk-patch")
	 */
	protected abstract String getJobNameForLogging();

	protected enum StateEnum {

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
	protected static class State {

		private final Map<TypedPidAndVersionJson, StateEnum> myPidToState = new HashMap<>();
		private final Map<TypedPidAndVersionJson, IBaseResource> myPidToResource = new HashMap<>();
		private final Map<TypedPidAndVersionJson, IIdType> myPidToResourceId = new HashMap<>();
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

		/**
		 * @throws java.util.NoSuchElementException if no PIDs are present in the given state
		 */
		@Nonnull
		public TypedPidAndVersionJson getSinglePidInState(StateEnum theState) {
			TypedPidAndVersionJson pid = myStateToPids.get(theState).iterator().next();
			Validate.notNull(pid, "No PIDs in state %s", theState);
			return pid;
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

		public void setResourceForPid(TypedPidAndVersionJson thePid, IBaseResource theResource) {
			Validate.isTrue(myPidToState.containsKey(thePid), "Pid %s is not present", thePid);
			myPidToResource.put(thePid, theResource);
			myPidToResourceId.put(thePid, theResource.getIdElement());
		}

		public void setResourceIdForPid(TypedPidAndVersionJson thePid, IIdType theResourceId) {
			Validate.isTrue(myPidToState.containsKey(thePid), "Pid %s is not present", thePid);
			myPidToResourceId.put(thePid, theResourceId);
		}

		@Nonnull
		public IBaseResource getResourceForPid(TypedPidAndVersionJson thePid) {
			IBaseResource retVal = myPidToResource.get(thePid);
			Validate.notNull(retVal, "Resource for PID %s is null", thePid);
			return retVal;
		}

		@Nullable
		public IIdType getResourceIdForPidOrNull(TypedPidAndVersionJson thePid) {
			return myPidToResourceId.get(thePid);
		}

		public boolean isPidInState(@Nonnull TypedPidAndVersionJson thePid, @Nonnull StateEnum theStateEnum) {
			return theStateEnum.equals(myPidToState.get(thePid));
		}
	}
}
