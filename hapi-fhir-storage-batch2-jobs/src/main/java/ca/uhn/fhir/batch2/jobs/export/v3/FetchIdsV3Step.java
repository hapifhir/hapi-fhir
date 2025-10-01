package ca.uhn.fhir.batch2.jobs.export.v3;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.export.models.MdmExpandedPatientIds;
import ca.uhn.fhir.batch2.jobs.export.models.PatientIdAndPidJson;
import ca.uhn.fhir.batch2.jobs.export.models.ResourceIdList;
import ca.uhn.fhir.batch2.jobs.export.svc.BulkExportIdFetchingSvc;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.bulk.export.model.ExportPIDIteratorParameters;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class FetchIdsV3Step implements IJobStepWorker<BulkExportJobParameters, MdmExpandedPatientIds, ResourceIdList> {
	private static final Logger ourLog = LoggerFactory.getLogger(FetchIdsV3Step.class);

	@Autowired
	private BulkExportIdFetchingSvc myBulkExportIdFetchingSvc;

	@Autowired
	private IIdHelperService<JpaPid> myIdHelperService;

	@Autowired
	private FhirContext myFhirContext;

	@Override
	public RunOutcome run(
			@Nonnull StepExecutionDetails<BulkExportJobParameters, MdmExpandedPatientIds> theStepExecutionDetails,
			@Nonnull IJobDataSink<ResourceIdList> theDataSink)
			throws JobExecutionFailedException {
		BulkExportJobParameters params = theStepExecutionDetails.getParameters();
		MdmExpandedPatientIds expandedPatientIds = theStepExecutionDetails.getData();
		ourLog.info(
				"Fetching resource IDs for bulk export job instance [{}]",
				theStepExecutionDetails.getInstance().getInstanceId());

		ExportPIDIteratorParameters providerParams = new ExportPIDIteratorParameters();
		providerParams.setInstanceId(theStepExecutionDetails.getInstance().getInstanceId());
		providerParams.setChunkId(theStepExecutionDetails.getChunkId());
		providerParams.setFilters(params.getFilters());
		providerParams.setStartDate(params.getSince());
		providerParams.setEndDate(params.getUntil());
		providerParams.setExportStyle(params.getExportStyle());
		providerParams.setGroupId(params.getGroupId());
		providerParams.setPatientIds(params.getPatientIds());
		providerParams.setExpandMdm(params.isExpandMdm());
		providerParams.setPartitionId(params.getPartitionId());
		providerParams.setRequestedResourceTypes(params.getResourceTypes());

		if (params.isExpandMdm()) {
			providerParams.setExpandedPatientIds(convertExpandedPatientIds(expandedPatientIds.getExpandedPatientIds()));
		}

		int submissionCount = 0;
		try {
			submissionCount = myBulkExportIdFetchingSvc.fetchIds(providerParams, theDataSink::accept);
		} catch (JobExecutionFailedException ex) {
			theDataSink.recoveredError(ex.getMessage());
			throw ex;
		}

		ourLog.info("Submitted {} groups of ids for processing", submissionCount);
		return RunOutcome.SUCCESS;
	}

	private List<IResourcePersistentId<?>> convertExpandedPatientIds(List<PatientIdAndPidJson> theExpanded) {
		return theExpanded.stream()
				.map(id -> {
					return id.toPersistentId(myIdHelperService, myFhirContext);
				})
				.collect(Collectors.toList());
	}
}
