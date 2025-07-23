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
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.IModelVisitor2;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

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
		// Fetch all the resources in the chunk
		List<IBaseResource> resources = fetchResources(theStepExecutionDetails);

		// Perform the modification (handled by subclasses)
		ModificationOutcome modificationOutcome = modifyResources(theStepExecutionDetails, resources);

		// Store the modified resources to the DB
		storeResources(modificationOutcome.modifiedResources());

		return RunOutcome.SUCCESS;
	}

	private @Nonnull List<IBaseResource> fetchResources(@Nonnull StepExecutionDetails<T, ResourceIdListWorkChunkJson> theStepExecutionDetails) {
		return myTransactionService
				.withSystemRequestOnPartition(theStepExecutionDetails.getData().getRequestPartitionId())
				.readOnly()
				.execute(() -> fetchResourcesInTransaction(theStepExecutionDetails));
	}

	private @Nonnull List<IBaseResource> fetchResourcesInTransaction(@Nonnull StepExecutionDetails<T, ResourceIdListWorkChunkJson> theStepExecutionDetails) {
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

	/**
	 * Subclasses may override this method, which will be called imeediately before beginning processing
	 * of a batch of resources. It can be used to perform any shared processing which would otherwise need
	 * to be repeated, such as looking up context resources, parsing a patch object in the job parameters
	 * or other expensive operations.
	 *
	 * @param theStepExecutionDetails Contains the job parameters and work chunk details
	 * @return A context object which will be passed to {@link #modifyResource(StepExecutionDetails, Object, ResourceModificationRequest)}
	 *         during each invocation. The format of the context object is up to the subclass, the framework
	 *         won't look at it and doesn't care if it is {@literal null}.
	 */
	@Nullable
	protected C preModifyResources(StepExecutionDetails<T, ResourceIdListWorkChunkJson> theStepExecutionDetails) {
		return null;
	}

	@Nonnull
	private ModificationOutcome modifyResources(StepExecutionDetails<T, ResourceIdListWorkChunkJson> theStepExecutionDetails, List<IBaseResource> resources) {
		FhirTerser terser = myFhirContext.newTerser();

		C modificationContext = preModifyResources(theStepExecutionDetails);

		List<IBaseResource> modifiedResources = new ArrayList<>(resources.size());
		List<IBaseResource> unchangedResources = new ArrayList<>(resources.size());

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
				unchangedResources.add(updatedResource);
			} else {
				modifiedResources.add(updatedResource);
			}
		}

		return new ModificationOutcome(modifiedResources, unchangedResources);
	}

	private void storeResources(List<IBaseResource> modifiedResources) {
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

	}

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

	private record ModificationOutcome(List<IBaseResource> modifiedResources, List<IBaseResource> unchangedResources) {}
}
