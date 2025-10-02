package ca.uhn.fhir.batch2.jobs.export.v3;

import ca.uhn.fhir.batch2.api.IFirstJobStepWorker;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.jobs.export.models.MdmExpandedPatientIds;
import ca.uhn.fhir.batch2.jobs.export.models.PatientIdAndPidJson;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkExportProcessor;
import ca.uhn.fhir.jpa.bulk.export.model.ExpandPatientIdsParams;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MdmExpansionStep implements IFirstJobStepWorker<BulkExportJobParameters, MdmExpandedPatientIds> {
	private static final Logger ourLog = LoggerFactory.getLogger(MdmExpansionStep.class);

	@SuppressWarnings("rawtypes")
	@Autowired
	private IBulkExportProcessor myBulkExportProcessor;

	@Autowired
	private IRequestPartitionHelperSvc myRequestPartitionHelperSvc;

	@Override
	public RunOutcome run(
			StepExecutionDetails<BulkExportJobParameters, VoidModel> theStepExecutionDetails,
			@Nonnull IJobDataSink<MdmExpandedPatientIds> theDataSink)
			throws JobExecutionFailedException {
		BulkExportJobParameters jobParameters = theStepExecutionDetails.getParameters();

		ourLog.info(
			"Doing MDM expansion for bulk export job instance[{}]",
			theStepExecutionDetails.getInstance().getInstanceId());

		ExpandPatientIdsParams params = new ExpandPatientIdsParams(jobParameters.getExportStyle());
		params.setToDoMdmExpansion(jobParameters.isExpandMdm());
		params.setGroupId(jobParameters.getGroupId());
		params.setRequestPartitionId(jobParameters.getPartitionId());
		params.setFilters(jobParameters.getFilters());
		List<String> patientIds = jobParameters.getPatientIds();

		params.setPatientIds(patientIds);

		@SuppressWarnings("unchecked")
		Set<IResourcePersistentId<?>> resourcePersistentIdSet = myBulkExportProcessor.expandPatientIdList(params);

		MdmExpandedPatientIds expandedPatientIds = new MdmExpandedPatientIds();
		expandedPatientIds.setExpandedPatientIds(resourcePersistentIdSet.stream()
			.map(PatientIdAndPidJson::new)
			.collect(Collectors.toList()));
		theDataSink.accept(expandedPatientIds);

		ourLog.info(
			"MDM expansion performed generating {} ids",
			expandedPatientIds.getExpandedPatientIds().size());

		return RunOutcome.SUCCESS;
	}
}
