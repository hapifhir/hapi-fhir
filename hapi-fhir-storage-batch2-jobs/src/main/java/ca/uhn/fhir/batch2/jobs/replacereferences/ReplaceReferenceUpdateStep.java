package ca.uhn.fhir.batch2.jobs.replacereferences;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.chunk.FhirIdJson;
import ca.uhn.fhir.batch2.jobs.chunk.FhirIdListWorkChunkJson;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.replacereferences.ReplaceReferenceRequest;
import ca.uhn.fhir.replacereferences.ReplaceReferencesPatchBundleSvc;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.r4.model.Bundle;

import java.util.List;
import java.util.stream.Collectors;

public class ReplaceReferenceUpdateStep
		implements IJobStepWorker<
				ReplaceReferencesJobParameters, FhirIdListWorkChunkJson, ReplaceReferencePatchOutcomeJson> {

	private final FhirContext myFhirContext;
	private final ReplaceReferencesPatchBundleSvc myReplaceReferencesPatchBundleSvc;

	public ReplaceReferenceUpdateStep(FhirContext theFhirContext, ReplaceReferencesPatchBundleSvc theReplaceReferencesPatchBundleSvc) {
		myFhirContext = theFhirContext;
		myReplaceReferencesPatchBundleSvc = theReplaceReferencesPatchBundleSvc;
	}

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull
					StepExecutionDetails<ReplaceReferencesJobParameters, FhirIdListWorkChunkJson>
							theStepExecutionDetails,
			@Nonnull IJobDataSink<ReplaceReferencePatchOutcomeJson> theDataSink)
			throws JobExecutionFailedException {

		ReplaceReferencesJobParameters params = theStepExecutionDetails.getParameters();
		ReplaceReferenceRequest replaceReferencesRequest = params.asReplaceReferencesRequest();
		List<IdDt> fhirIds = theStepExecutionDetails.getData().getFhirIds().stream().map(FhirIdJson::asIdDt).collect(Collectors.toList());

		SystemRequestDetails requestDetails = SystemRequestDetails.forRequestPartitionId(params.getPartitionId());

		Bundle result = myReplaceReferencesPatchBundleSvc.patchReferencingResources(replaceReferencesRequest, fhirIds, requestDetails);

		ReplaceReferencePatchOutcomeJson data = new ReplaceReferencePatchOutcomeJson(myFhirContext, result);
		theDataSink.accept(data);

		return new RunOutcome(result.getEntry().size());
	}

}
