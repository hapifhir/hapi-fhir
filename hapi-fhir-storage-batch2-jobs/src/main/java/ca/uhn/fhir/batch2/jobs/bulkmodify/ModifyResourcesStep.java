package ca.uhn.fhir.batch2.jobs.bulkmodify;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.chunk.ResourceIdListWorkChunkJson;
import ca.uhn.fhir.batch2.jobs.chunk.TypedPidJson;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class ModifyResourcesStep implements IJobStepWorker<BulkModifyJobParameters, ResourceIdListWorkChunkJson, ModifyResourcesChunkOutcomeJson> {

	@Autowired
	private IHapiTransactionService myTransactionService;
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private IFhirSystemDao<?, ?> mySystemDao;
	@Autowired
	private IIdHelperService<IResourcePersistentId<?>> myIdHelperService;

	@Nonnull
	@Override
	public RunOutcome run(@Nonnull StepExecutionDetails<BulkModifyJobParameters, ResourceIdListWorkChunkJson> theStepExecutionDetails, @Nonnull IJobDataSink<ModifyResourcesChunkOutcomeJson> theDataSink) throws JobExecutionFailedException {
		List<IBaseResource> resources = fetchResources(theStepExecutionDetails);

		for (IBaseResource resource : resources) {

		}
		
		return null;
	}

	private @Nonnull List<IBaseResource> fetchResources(@Nonnull StepExecutionDetails<BulkModifyJobParameters, ResourceIdListWorkChunkJson> theStepExecutionDetails) {
		return myTransactionService
			.withSystemRequestOnPartition(theStepExecutionDetails.getData().getRequestPartitionId())
			.readOnly()
			.execute(() -> fetchResourcesInTransaction(theStepExecutionDetails));
	}

	private @Nonnull List<IBaseResource> fetchResourcesInTransaction(@Nonnull StepExecutionDetails<BulkModifyJobParameters, ResourceIdListWorkChunkJson> theStepExecutionDetails) {
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

}
