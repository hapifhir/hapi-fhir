package ca.uhn.fhir.batch2.jobs.export;

import ca.uhn.fhir.batch2.api.IFirstJobStepWorker;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportIdList;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportJobParameters;
import ca.uhn.fhir.batch2.jobs.models.Id;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkExportProcessor;
import ca.uhn.fhir.jpa.bulk.export.model.ExportPIDIteratorParameters;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FetchResourceIdsStep implements IFirstJobStepWorker<BulkExportJobParameters, BulkExportIdList> {
	private static final Logger ourLog = LoggerFactory.getLogger(FetchResourceIdsStep.class);

	public static final int MAX_IDS_TO_BATCH = 1000;

	@Autowired
	private IBulkExportProcessor myBulkExportProcessor;

	@Nonnull
	@Override
	public RunOutcome run(@Nonnull StepExecutionDetails<BulkExportJobParameters, VoidModel> theStepExecutionDetails,
								 @Nonnull IJobDataSink<BulkExportIdList> theDataSink) throws JobExecutionFailedException {
		BulkExportJobParameters params = theStepExecutionDetails.getParameters();
		ourLog.info("Starting BatchExport job");

		ExportPIDIteratorParameters providerParams = new ExportPIDIteratorParameters();
		providerParams.setFilters(params.getFilters());
		providerParams.setStartDate(params.getStartDate());
		providerParams.setExportStyle(params.getExportStyle());
		providerParams.setGroupId(params.getGroupId());
		providerParams.setExpandMdm(params.isExpandMdm());

		int submissionCount = 0;
		try {
			for (String resourceType : params.getResourceTypes()) {
				providerParams.setResourceType(resourceType);

				// filters are the filters for searching
				Iterator<ResourcePersistentId> pidIterator = myBulkExportProcessor.getResourcePidIterator(providerParams);
				List<Id> idsToSubmit = new ArrayList<>();
				while (pidIterator.hasNext()) {
					ResourcePersistentId pid = pidIterator.next();

					idsToSubmit.add(Id.getIdFromPID(pid, resourceType));

					// >= so that we know (with confidence)
					// that every batch is <= 1000 items
					if (idsToSubmit.size() >= MAX_IDS_TO_BATCH) {
						submitWorkChunk(idsToSubmit, resourceType, params, theDataSink);
						submissionCount++;
						idsToSubmit = new ArrayList<>();
					}
				}

				// if we have any other Ids left, submit them now
				if (!idsToSubmit.isEmpty()) {
					submitWorkChunk(idsToSubmit, resourceType, params, theDataSink);
					submissionCount++;
					idsToSubmit = new ArrayList<>();
				}
			}
		} catch (Exception ex) {
			ourLog.error(ex.getMessage());

			theDataSink.recoveredError(ex.getMessage());

			throw new JobExecutionFailedException(ex.getMessage());
		}

		ourLog.info("Submitted {} groups of ids for processing", submissionCount);
		return RunOutcome.SUCCESS;
	}

	private void submitWorkChunk(List<Id> theIds,
										  String theResourceType,
										  BulkExportJobParameters theParams,
										  IJobDataSink<BulkExportIdList> theDataSink) {
		BulkExportIdList idList = new BulkExportIdList();

		idList.setIds(theIds);

		idList.setResourceType(theResourceType);

		theDataSink.accept(idList);
	}
}
