package ca.uhn.fhir.batch2.jobs.bulkmodify.patch;

import ca.uhn.fhir.batch2.jobs.bulkmodify.api.ResourceModificationRequest;
import ca.uhn.fhir.batch2.jobs.bulkmodify.api.ResourceModificationResponse;
import ca.uhn.fhir.batch2.jobs.bulkmodify.base.BaseBulkModifyResourcesStep;
import jakarta.annotation.Nonnull;

public class BulkPatchModificationService extends BaseBulkModifyResourcesStep<BulkPatchJobParameters> {

	@Nonnull
	@Override
	protected ResourceModificationResponse modifyResource(@Nonnull ResourceModificationRequest theModificationRequest) {
		return null;
	}
}
