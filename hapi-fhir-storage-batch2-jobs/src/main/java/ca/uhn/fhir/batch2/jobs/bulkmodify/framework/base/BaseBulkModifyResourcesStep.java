package ca.uhn.fhir.batch2.jobs.bulkmodify.framework.base;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.api.ResourceModificationRequest;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.api.ResourceModificationResponse;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.common.BulkModifyResourcesChunkOutcomeJson;
import ca.uhn.fhir.batch2.jobs.chunk.ResourceIdListWorkChunkJson;
import ca.uhn.fhir.batch2.jobs.chunk.TypedPidJson;
import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.dao.TransactionUtil;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.util.TransactionSemanticsHeader;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.IModelVisitor2;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

public abstract class BaseBulkModifyResourcesStep<T extends BaseBulkModifyJobParameters, C> implements IJobStepWorker<T, ResourceIdListWorkChunkJson, BulkModifyResourcesChunkOutcomeJson> {

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

	@Nonnull
	@Override
	public RunOutcome run(@Nonnull StepExecutionDetails<T, ResourceIdListWorkChunkJson> theStepExecutionDetails, @Nonnull IJobDataSink<BulkModifyResourcesChunkOutcomeJson> theDataSink) throws JobExecutionFailedException {
		State state = new State();

		myTransactionService
			.withSystemRequestOnPartition(theStepExecutionDetails.getData().getRequestPartitionId())
			.execute(() -> performBatchInTransaction(theStepExecutionDetails, state));

		// FIXME: handle unsaved

		BulkModifyResourcesChunkOutcomeJson outcome = new BulkModifyResourcesChunkOutcomeJson();

		for (IBaseResource next : state.getChangedSavedResources()) {
			outcome.addChangedId(next.getIdElement());
		}
		for (IBaseResource next : state.getUnchangedResources()) {
			outcome.addUnchangedId(next.getIdElement());
		}
		for (Map.Entry<IBaseResource, String> next : state.getFailures().entrySet()) {
			outcome.addFailure(next.getKey().getIdElement(), next.getValue());
		}
		theDataSink.accept(outcome);

		return RunOutcome.SUCCESS;
	}

	private void performBatchInTransaction(StepExecutionDetails<T, ResourceIdListWorkChunkJson> theStepExecutionDetails, State theState) {
		assert TransactionSynchronizationManager.isActualTransactionActive();

		// Fetch all the resources in the chunk
		List<IBaseResource> resources = fetchResourcesInTransaction(theStepExecutionDetails);

		// Perform the modification (handled by subclasses)
		modifyResourcesInTransaction(theState, theStepExecutionDetails, resources);

		// Store the modified resources to the DB
		storeResourcesInTransaction(theState);

		// We're about to commit
		TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
			@Override
			public void afterCompletion(int status) {
				if (status == TransactionSynchronization.STATUS_COMMITTED) {
					theState.movePendingToSaved();
				} else {
					theState.movePendingToUnsaved();
				}
			}
		});
	}

	private @Nonnull List<IBaseResource> fetchResourcesInTransaction(@Nonnull StepExecutionDetails<T, ResourceIdListWorkChunkJson> theStepExecutionDetails) {
		assert TransactionSynchronizationManager.isActualTransactionActive();

		ResourceIdListWorkChunkJson data = theStepExecutionDetails.getData();

		List<IResourcePersistentId<?>> persistentIds = data.getResourcePersistentIds(myIdHelperService);
		mySystemDao.preFetchResources(persistentIds, true);

		List<TypedPidJson> typedPids = data.getTypedPids();
		List<IBaseResource> resources = new ArrayList<>(typedPids.size());
		for (int i = 0; i < typedPids.size(); i++) {
			TypedPidJson typedPid = typedPids.get(i);
			IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(typedPid.getResourceType());
			IBaseResource resource = dao.readByPid(persistentIds.get(i));
			resources.add(resource);
		}
		return resources;
	}

	private void modifyResourcesInTransaction(State theState, StepExecutionDetails<T, ResourceIdListWorkChunkJson> theStepExecutionDetails, List<IBaseResource> resources) {
		assert TransactionSynchronizationManager.isActualTransactionActive();

		FhirTerser terser = myFhirContext.newTerser();

		C modificationContext = preModifyResources(theStepExecutionDetails);

		for (IBaseResource resource : resources) {
			HashingModelVisitor preModificationHash = new HashingModelVisitor();
			terser.visit(resource, preModificationHash);

			ResourceModificationRequest modificationRequest = new ResourceModificationRequest(resource);
			ResourceModificationResponse modificationResponse = modifyResource(theStepExecutionDetails, modificationContext, modificationRequest);
			IBaseResource updatedResource = modificationResponse.getResource();

			// FIXME: test that updatedResource is not null and is the right resource type and has the right ID

			HashingModelVisitor postModificationHash = new HashingModelVisitor();
			terser.visit(updatedResource, postModificationHash);

			if (preModificationHash.matches(postModificationHash)) {
				theState.addUnchangedResource(updatedResource);
			} else {
				theState.addChangedUnsavedResource(updatedResource);
			}
		}

	}

	private void storeResourcesInTransaction(State theState) {
		assert TransactionSynchronizationManager.isActualTransactionActive();

		SystemRequestDetails requestDetails = new SystemRequestDetails();
		TransactionDetails transactionDetails = new TransactionDetails();

		for (IBaseResource resource : theState.getChangedUnsavedResourcesAndClear()) {
			IFhirResourceDao<IBaseResource> dao = myDaoRegistry.getResourceDao(resource);

			try {
				dao.update(resource, null, true, false, requestDetails, transactionDetails);
				theState.addPendingSavedResource(resource);
			} catch (BaseServerResponseException e) {
				theState.addFailure(resource, "HTTP " + e.getStatusCode() + ": " + e.getMessage());
				// FIXME: add test
			}
		}
	}

	private void storeResourcesUsingTransaction(List<IBaseResource> modifiedResources) {
		BundleBuilder builder = new BundleBuilder(myFhirContext);
		builder.setType(RestOperationTypeEnum.BATCH.getCode());
		for (IBaseResource resource : modifiedResources) {
			builder.addTransactionUpdateEntry(resource);
		}
		IBaseBundle modificationBundle = builder.getBundleTyped();

		SystemRequestDetails requestDetails = new SystemRequestDetails();
		TransactionSemanticsHeader header = TransactionSemanticsHeader.newBuilder().withRetryCount(3).withTryBatchAsTransactionFirst(true).build();
		header.applyTo(requestDetails);

		IBaseBundle transactionResponse = (IBaseBundle) mySystemDao.transaction(requestDetails, modificationBundle);
		TransactionUtil.TransactionResponse parsedTransactionResponse = TransactionUtil.parseTransactionResponse(myFhirContext, modificationBundle, transactionResponse);

		List<IIdType> changedIds = new ArrayList<>();
		List<IIdType> unchangedIds = new ArrayList<>();
		Map<IIdType, String> failures = new HashMap<>();

		for (TransactionUtil.StorageOutcome next : parsedTransactionResponse.getStorageOutcomes()) {
			IIdType id = next.getTargetId();
			boolean success = next.getStorageResponseCode().isSuccessful();
			if (!success) {
				failures.put(id, next.getErrorMessage());
			} else if (next.getStorageResponseCode().isNoOp()) {
				unchangedIds.add(id);
			} else {
				changedIds.add(id);
			}
		}

//		return new StorageOutcome(changedIds, unchangedIds, failures);
	}

	/**
	 * Subclasses may override this method, which will be called immediately before beginning processing
	 * of a batch of resources. It can be used to perform any shared processing which would otherwise need
	 * to be repeated, such as looking up context resources, parsing a patch object in the job parameters
	 * or other expensive operations.
	 *
	 * @param theStepExecutionDetails Contains the job parameters and work chunk details
	 * @return A context object which will be passed to {@link #modifyResource(StepExecutionDetails, Object, ResourceModificationRequest)}
	 * 	during each invocation. The format of the context object is up to the subclass, the framework
	 * 	won't look at it and doesn't care if it is {@literal null}.
	 */
	@Nullable
	protected C preModifyResources(StepExecutionDetails<T, ResourceIdListWorkChunkJson> theStepExecutionDetails) {
		return null;
	}

	/**
	 * This method is called once for each resource needing modification
	 */
	@Nonnull
	protected abstract ResourceModificationResponse modifyResource(StepExecutionDetails<T, ResourceIdListWorkChunkJson> theStepExecutionDetails, C theModificationContext, @Nonnull ResourceModificationRequest theModificationRequest);


	/**
	 * Visitor for a resource which generates a hash of all the contents of the resource for
	 * comparison purposes.
	 */
	@SuppressWarnings("UnstableApiUsage")
	private static class HashingModelVisitor implements IModelVisitor2 {

		private final Hasher myHasher;

		public HashingModelVisitor() {
			myHasher = Hashing.goodFastHash(128).newHasher();
		}

		@Override
		public boolean acceptElement(IBase theElement, List<IBase> theContainingElementPath, List<BaseRuntimeChildDefinition> theChildDefinitionPath, List<BaseRuntimeElementDefinition<?>> theElementDefinitionPath) {
			myHasher.putInt(theChildDefinitionPath.size());
			for (var child : theChildDefinitionPath) {
				myHasher.putInt(child.getElementName().hashCode());
			}
			myHasher.putInt(theElement.hashCode());
			return true;
		}

		@Override
		public boolean acceptUndeclaredExtension(IBaseExtension<?, ?> theNextExt, List<IBase> theContainingElementPath, List<BaseRuntimeChildDefinition> theChildDefinitionPath, List<BaseRuntimeElementDefinition<?>> theElementDefinitionPath) {
			myHasher.putInt(theNextExt.getUrl().hashCode());
			myHasher.putInt(defaultIfNull(theNextExt.getValue(), "").hashCode());
			return true;
		}

		public boolean matches(HashingModelVisitor thePostModificationHash) {
			return myHasher.hash().equals(thePostModificationHash.myHasher);
		}
	}


	private static class State {

		private final Set<IBaseResource> myUnchangedResources = new HashSet<>();
		private final Set<IBaseResource> myChangedUnsavedResources = new HashSet<>();
		private final Set<IBaseResource> myChangedSavedResources = new HashSet<>();
		private final List<IBaseResource> myPendingSavedResources = new ArrayList<>();
		private final Map<IBaseResource, String> myFailures = new HashMap<>();

		public Collection<IBaseResource> getUnchangedResources() {
			return myUnchangedResources;
		}

		public Collection<IBaseResource> getChangedUnsavedResources() {
			return myChangedUnsavedResources;
		}

		public Collection<IBaseResource> getChangedUnsavedResourcesAndClear() {
			List<IBaseResource> retVal = List.copyOf(myChangedUnsavedResources);
			myChangedUnsavedResources.clear();
			return retVal;
		}

		public Collection<IBaseResource> getChangedSavedResources() {
			return myChangedSavedResources;
		}

		public void addUnchangedResources(Collection<IBaseResource> theResources) {
			theResources.forEach(this::addUnchangedResource);
		}

		public void addChangedSavedResources(Collection<IBaseResource> theResources) {
			int previousSize = myChangedSavedResources.size();
			myChangedSavedResources.addAll(theResources);
			Validate.isTrue(myChangedSavedResources.size() == previousSize + theResources.size(), "Added duplicate resources");
		}

		public void addChangedUnsavedResources(Collection<IBaseResource> theResources) {
			int previousSize = myChangedUnsavedResources.size();
			myChangedUnsavedResources.addAll(theResources);
			Validate.isTrue(myChangedUnsavedResources.size() == previousSize + theResources.size(), "Added duplicate resources");
		}

		public void addFailure(IBaseResource theResource, String theMessage) {
			String previousValue = myFailures.put(theResource, theMessage);
			Validate.isTrue(previousValue == null, "%s is already present", theResource);
		}

		public Map<IBaseResource, String> getFailures() {
			return myFailures;
		}

		public void addUnchangedResource(IBaseResource theResource) {
			boolean added = myUnchangedResources.add(theResource);
			Validate.isTrue(added, "%s is already present", theResource);
		}

		public void addChangedUnsavedResource(IBaseResource theResource) {
			boolean added = myChangedUnsavedResources.add(theResource);
			Validate.isTrue(added, "%s is already present", theResource);
		}

		public void addPendingSavedResource(IBaseResource theResource) {
			myPendingSavedResources.add(theResource);
		}

		public void movePendingToSaved() {
			addChangedSavedResources(myPendingSavedResources);
			myPendingSavedResources.clear();
		}

		public void movePendingToUnsaved() {
			addChangedUnsavedResources(myPendingSavedResources);
			myPendingSavedResources.clear();
		}
	}

}
