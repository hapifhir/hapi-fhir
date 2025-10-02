package ca.uhn.fhir.batch2.jobs.export.svc;

import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.jobs.chunk.TypedPidJson;
import ca.uhn.fhir.batch2.jobs.export.models.ResourceIdList;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkExportProcessor;
import ca.uhn.fhir.jpa.bulk.export.model.ExportPIDIteratorParameters;
import ca.uhn.fhir.rest.api.IResourceSupportedSvc;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.util.SearchParameterUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class BulkExportIdFetchingSvc {

	private static final Logger ourLog = LoggerFactory.getLogger(BulkExportIdFetchingSvc.class);

	@SuppressWarnings("rawtypes")
	@Autowired
	private IBulkExportProcessor myBulkExportProcessor;

	@Autowired
	private JpaStorageSettings myStorageSettings;

	@Autowired
	private IResourceSupportedSvc myResourceSupportedSvc;

	public int fetchIds(ExportPIDIteratorParameters theProviderParameters, Consumer<ResourceIdList> theConsumer) {
		BulkExportJobParameters.ExportStyle exportStyle = theProviderParameters.getExportStyle();
		List<String> resourceTypes = theProviderParameters.getRequestedResourceTypes();

		int submissionCount = 0;

		try {
			Set<TypedPidJson> submittedBatchResourceIds = new HashSet<>();

			/*
			 * NB: patient-compartment limitation
			 * We know that Group and List are part of patient compartment.
			 * But allowing export of them seems like a security flaw.
			 * So we'll exclude them.
			 */
			Set<String> resourceTypesToOmit = exportStyle == BulkExportJobParameters.ExportStyle.PATIENT
					? new HashSet<>(SearchParameterUtil.RESOURCE_TYPES_TO_SP_TO_OMIT_FROM_PATIENT_COMPARTMENT.keySet())
					: Set.of();

			/*
			 * We will fetch ids for each resource type in the ResourceTypes (_type filter).
			 */
			for (String resourceType : resourceTypes) {
				if (resourceTypesToOmit.contains(resourceType) || !myResourceSupportedSvc.isSupported(resourceType)) {
					continue;
				}
				// clone them because we'll change them before use
				ExportPIDIteratorParameters providerParams = new ExportPIDIteratorParameters(theProviderParameters);
				providerParams.setResourceType(resourceType);

				// filters are the filters for searching
				ourLog.info(
						"Running FetchIds for resource type: {} with params: {}",
						resourceType,
						providerParams);

				@SuppressWarnings({"rawtypes", "unchecked"})
				Iterator<IResourcePersistentId> pidIterator =
						myBulkExportProcessor.getResourcePidIterator(providerParams);
				List<TypedPidJson> idsToSubmit = new ArrayList<>();

				int estimatedChunkSize = 0;

				if (!pidIterator.hasNext()) {
					ourLog.debug("Bulk Export generated an iterator with no results!");
				}

				while (pidIterator.hasNext()) {
					IResourcePersistentId<?> pid = pidIterator.next();

					TypedPidJson batchResourceId;
					if (pid.getResourceType() != null) {
						batchResourceId = new TypedPidJson(pid.getResourceType(), pid);
					} else {
						batchResourceId = new TypedPidJson(resourceType, pid);
					}

					if (!submittedBatchResourceIds.add(batchResourceId)) {
						continue;
					}

					idsToSubmit.add(batchResourceId);

					if (estimatedChunkSize > 0) {
						// Account for comma between array entries
						estimatedChunkSize++;
					}
					estimatedChunkSize += batchResourceId.estimateSerializedSize();

					// Make sure resources stored in each batch does not go over the max capacity
					if (idsToSubmit.size() >= myStorageSettings.getBulkExportFileMaximumCapacity()
							|| estimatedChunkSize >= myStorageSettings.getBulkExportFileMaximumSize()) {
						ResourceIdList list = new ResourceIdList();
						list.setIds(idsToSubmit);
						list.setResourceType(resourceType);
						theConsumer.accept(list);
						submissionCount++;
						idsToSubmit = new ArrayList<>();
						estimatedChunkSize = 0;
					}
				}

				// if we have any other Ids left, submit them now
				if (!idsToSubmit.isEmpty()) {
					ResourceIdList list = new ResourceIdList();
					list.setIds(idsToSubmit);
					list.setResourceType(resourceType);
					theConsumer.accept(list);
					submissionCount++;
				}
			}
		} catch (Exception ex) {
			// any recoverable error presumably
			ourLog.error(ex.getMessage(), ex);

			throw new JobExecutionFailedException(Msg.code(2239) + " : " + ex.getMessage(), ex);
		}

		return submissionCount;
	}
}
