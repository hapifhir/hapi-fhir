package ca.uhn.fhir.batch2.jobs.bulkmodify.patchrewrite;

import ca.uhn.fhir.batch2.jobs.bulkmodify.patch.BulkPatchJobParametersValidator;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.IDaoRegistry;

public class BulkPatchRewriteJobParametersValidator
		extends BulkPatchJobParametersValidator<BulkPatchRewriteJobParameters> {

	/**
	 * Constructor
	 */
	public BulkPatchRewriteJobParametersValidator(FhirContext theFhirContext, IDaoRegistry theDaoRegistry) {
		super(theFhirContext, theDaoRegistry);
	}
}
