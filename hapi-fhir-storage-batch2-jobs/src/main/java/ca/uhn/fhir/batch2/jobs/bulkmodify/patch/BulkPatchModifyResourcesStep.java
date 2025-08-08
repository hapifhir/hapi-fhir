package ca.uhn.fhir.batch2.jobs.bulkmodify.patch;

import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.api.ResourceModificationRequest;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.api.ResourceModificationResponse;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.base.BaseBulkModifyResourcesStep;
import ca.uhn.fhir.batch2.jobs.chunk.TypedPidAndVersionListWorkChunkJson;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.patch.FhirPatch;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;

public class BulkPatchModifyResourcesStep<PT extends BulkPatchJobParameters> extends BaseBulkModifyResourcesStep<PT, BulkPatchModificationContext> {

	@Autowired
	private FhirContext myFhirContext;

	@Nullable
	@Override
	protected BulkPatchModificationContext preModifyResources(StepExecutionDetails<PT, TypedPidAndVersionListWorkChunkJson> theStepExecutionDetails) {
		BulkPatchJobParameters jobParameters = theStepExecutionDetails.getParameters();
		IBaseResource patch = jobParameters.getFhirPatch(myFhirContext);
		return new BulkPatchModificationContext(patch);
	}

	@Nonnull
	@Override
	protected ResourceModificationResponse modifyResource(StepExecutionDetails<PT, TypedPidAndVersionListWorkChunkJson> theStepExecutionDetails, BulkPatchModificationContext theModificationContext, @Nonnull ResourceModificationRequest theModificationRequest) {
		IBaseResource resourceToPatch = theModificationRequest.getResource();
		IBaseResource patchToApply = theModificationContext.getPatch();
		new FhirPatch(myFhirContext).apply(resourceToPatch, patchToApply);
		return ResourceModificationResponse.updateResource(resourceToPatch);
	}

}
