package ca.uhn.fhir.batch2.jobs.mdm;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportBinaryFileId;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportJobParameters;
import ca.uhn.fhir.batch2.jobs.export.models.ExpandedResourcesList;
import ca.uhn.fhir.batch2.jobs.mdm.models.MdmSubmitJobParameters;
import ca.uhn.fhir.mdm.api.IMdmChannelSubmitterSvc;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

public class SubmitToBrokerStep implements IJobStepWorker<MdmSubmitJobParameters, ExpandedResourcesList, VoidModel> {


	@Autowired
	private IMdmChannelSubmitterSvc myMdmChannelSubmitterSvc;

	@NotNull
	@Override
	public RunOutcome run(@NotNull StepExecutionDetails<MdmSubmitJobParameters, ExpandedResourcesList> theStepExecutionDetails, @NotNull IJobDataSink<VoidModel> theDataSink) throws JobExecutionFailedException {

	}
}


