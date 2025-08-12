package ca.uhn.fhir.batch2.jobs.bulkmodify.framework.common;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.base.BaseBulkModifyJobParameters;
import ca.uhn.fhir.batch2.jobs.chunk.ResourceIdListWorkChunkJson;
import ca.uhn.fhir.batch2.jobs.chunk.TypedPidAndVersionJson;
import ca.uhn.fhir.batch2.jobs.chunk.TypedPidAndVersionListWorkChunkJson;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import static ca.uhn.fhir.batch2.jobs.step.ResourceIdListStep.MAX_BATCH_OF_IDS;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

public class TypedPidToTypedPidAndVersionStep<PT extends BaseBulkModifyJobParameters>
		implements IJobStepWorker<PT, ResourceIdListWorkChunkJson, TypedPidAndVersionListWorkChunkJson> {

	@Autowired
	private IIdHelperService<? extends IResourcePersistentId<?>> myIdHelperService;

	@Autowired
	private IHapiTransactionService myTransactionService;

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull StepExecutionDetails<PT, ResourceIdListWorkChunkJson> theStepExecutionDetails,
			@Nonnull IJobDataSink<TypedPidAndVersionListWorkChunkJson> theDataSink)
			throws JobExecutionFailedException {
		return myTransactionService
				.withSystemRequestOnPartition(theStepExecutionDetails.getData().getRequestPartitionId())
				.execute(() -> {
					Integer batchSize = theStepExecutionDetails.getParameters().getBatchSize();
					int chunkSize = Math.min(defaultIfNull(batchSize, MAX_BATCH_OF_IDS), MAX_BATCH_OF_IDS);

					ResourceIdListWorkChunkJson data = theStepExecutionDetails.getData();
					List<TypedPidAndVersionJson> versionedPids = new ArrayList<>();

					Multimap<String, IResourcePersistentId<?>> resourceTypeToPersistentIds = ArrayListMultimap.create();
					List<? extends IResourcePersistentId<?>> persistentIds =
							data.getResourcePersistentIds(myIdHelperService);
					for (IResourcePersistentId<?> next : persistentIds) {
						resourceTypeToPersistentIds.put(next.getResourceType(), next);
					}

					for (String nextResourceType : resourceTypeToPersistentIds.keySet()) {
						Collection<IResourcePersistentId<?>> typePersistentIds =
								resourceTypeToPersistentIds.get(nextResourceType);
						IFhirResourceDao dao = myDaoRegistry.getResourceDao(nextResourceType);
						Stream<IResourcePersistentId> versionStream =
								dao.fetchAllVersionsOfResources(new SystemRequestDetails(), typePersistentIds);
						Iterator<IResourcePersistentId> iter = versionStream.iterator();
						while (iter.hasNext()) {

							IResourcePersistentId next = iter.next();
							versionedPids.add(new TypedPidAndVersionJson(
									nextResourceType,
									next.getPartitionId(),
									next.getId().toString(),
									next.getVersion()));

							if (versionedPids.size() >= chunkSize || !iter.hasNext()) {
								theDataSink.accept(new TypedPidAndVersionListWorkChunkJson(
										data.getRequestPartitionId(), versionedPids));
							}
						}
					}

					return RunOutcome.SUCCESS;
				});
	}
}
