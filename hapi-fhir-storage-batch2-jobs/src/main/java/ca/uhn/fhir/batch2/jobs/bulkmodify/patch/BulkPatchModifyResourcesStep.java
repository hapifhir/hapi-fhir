package ca.uhn.fhir.batch2.jobs.bulkmodify.patch;

import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.api.ResourceModificationRequest;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.api.ResourceModificationResponse;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.base.BaseBulkModifyResourcesStep;
import ca.uhn.fhir.batch2.jobs.chunk.TypedPidAndVersionJson;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.patch.FhirPatch;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class BulkPatchModifyResourcesStep<PT extends BulkPatchJobParameters> extends BaseBulkModifyResourcesStep<PT, BulkPatchModificationContext> {

	@Autowired
	private FhirContext myFhirContext;

	private final boolean myRewriteHistory;

	public BulkPatchModifyResourcesStep(boolean theRewriteHistory) {
		myRewriteHistory = theRewriteHistory;
	}


	@Nullable
	@Override
	protected BulkPatchModificationContext preModifyResources(PT theJobParameters, List<TypedPidAndVersionJson> thePids) {
		IBaseResource patch = theJobParameters.getFhirPatch(myFhirContext);
		return new BulkPatchModificationContext(patch);
	}

	@Nonnull
	@Override
	protected ResourceModificationResponse modifyResource(PT theJobParameters, BulkPatchModificationContext theModificationContext, @Nonnull ResourceModificationRequest theModificationRequest) {
		IBaseResource resourceToPatch = theModificationRequest.getResource();
		IBaseResource patchToApply = theModificationContext.getPatch();
		new FhirPatch(myFhirContext).apply(resourceToPatch, patchToApply);
		return ResourceModificationResponse.updateResource(resourceToPatch);
	}

	@Override
	protected boolean isRewriteHistory(BulkPatchModificationContext theState, IBaseResource theResource) {
		return myRewriteHistory;
	}

}
