package ca.uhn.fhir.mdm.batch2.submit;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.jobs.export.models.ExpandedResourcesList;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.mdm.api.IMdmChannelSubmitterSvc;
import ca.uhn.fhir.parser.IParser;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class SubmitToBrokerStep implements IJobStepWorker<MdmSubmitJobParameters, ExpandedResourcesList, VoidModel> {

	@Autowired
	private IMdmChannelSubmitterSvc myMdmChannelSubmitterSvc;
	@Autowired
	private FhirContext myFhirContext;

	@Override
	public RunOutcome run(StepExecutionDetails<MdmSubmitJobParameters, ExpandedResourcesList> theStepExecutionDetails, IJobDataSink<VoidModel> theDataSink) throws JobExecutionFailedException {

		ExpandedResourcesList data = theStepExecutionDetails.getData();
		List<String> stringifiedResources = data.getStringifiedResources();

		IParser iParser = myFhirContext.newJsonParser();
		int processedCount = 0;
		for (String stringifiedResource : stringifiedResources) {
			IBaseResource iBaseResource = iParser.parseResource(stringifiedResource);
			myMdmChannelSubmitterSvc.submitResourceToMdmChannel(iBaseResource);
			processedCount += 1 ;
		}

		return new RunOutcome(processedCount);

	}
}


