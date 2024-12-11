package ca.uhn.fhir.batch2.jobs.replacereferences;

import ca.uhn.fhir.batch2.api.ChunkExecutionDetails;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IReductionStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.model.ChunkOutcome;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Task;

import java.util.ArrayList;
import java.util.List;

public class ReplaceReferenceUpdateTaskReducerStep<PT extends ReplaceReferencesJobParameters>
		implements IReductionStepWorker<
	PT, ReplaceReferencePatchOutcomeJson, ReplaceReferenceResultsJson> {
	public static final String RESOURCE_TYPES_SYSTEM = "http://hl7.org/fhir/ValueSet/resource-types";

	private final FhirContext myFhirContext;
	private final DaoRegistry myDaoRegistry;

	private List<Bundle> myPatchOutputBundles = new ArrayList<>();

	public ReplaceReferenceUpdateTaskReducerStep(DaoRegistry theDaoRegistry) {
		myDaoRegistry = theDaoRegistry;
		myFhirContext = theDaoRegistry.getFhirContext();
	}

	@Nonnull
	@Override
	public ChunkOutcome consume(
			ChunkExecutionDetails<PT, ReplaceReferencePatchOutcomeJson> theChunkDetails) {
		ReplaceReferencePatchOutcomeJson result = theChunkDetails.getData();
		Bundle patchOutputBundle =
				myFhirContext.newJsonParser().parseResource(Bundle.class, result.getPatchResponseBundle());
		myPatchOutputBundles.add(patchOutputBundle);
		return ChunkOutcome.SUCCESS();
	}

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull
					StepExecutionDetails<PT, ReplaceReferencePatchOutcomeJson>
							theStepExecutionDetails,
			@Nonnull IJobDataSink<ReplaceReferenceResultsJson> theDataSink)
			throws JobExecutionFailedException {

		ReplaceReferencesJobParameters params = theStepExecutionDetails.getParameters();
		SystemRequestDetails requestDetails = SystemRequestDetails.forRequestPartitionId(params.getPartitionId());
		Task task =
				myDaoRegistry.getResourceDao(Task.class).read(params.getTaskId().asIdDt(), requestDetails);

		task.setStatus(Task.TaskStatus.COMPLETED);
		// TODO KHS this Task will probably be too large for large jobs. Revisit this model once we support Provenance
		// resources.
		myPatchOutputBundles.forEach(outputBundle -> {
			Task.TaskOutputComponent output = task.addOutput();
			Coding coding = output.getType().getCodingFirstRep();
			coding.setSystem(RESOURCE_TYPES_SYSTEM);
			coding.setCode("Bundle");
			Reference outputBundleReference =
					new Reference("#" + outputBundle.getIdElement().getIdPart());
			output.setValue(outputBundleReference);
			task.addContained(outputBundle);
		});

		myDaoRegistry.getResourceDao(Task.class).update(task, requestDetails);

		ReplaceReferenceResultsJson result = new ReplaceReferenceResultsJson();
		result.setTaskId(params.getTaskId());
		theDataSink.accept(result);

		// Reusing the same reducer for all jobs feels confusing and dangerous to me. We need to fix this.
		// See https://github.com/hapifhir/hapi-fhir/pull/6551
		myPatchOutputBundles.clear();

		return new RunOutcome(myPatchOutputBundles.size());
	}
}
