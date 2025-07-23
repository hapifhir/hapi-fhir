package ca.uhn.fhir.batch2.jobs.bulkmodify.patch;

import org.hl7.fhir.instance.model.api.IBaseResource;

public class BulkPatchModificationContext {

	public IBaseResource getPatch() {
		return myPatch;
	}

	private final IBaseResource myPatch;

	public BulkPatchModificationContext(IBaseResource thePatch) {
		myPatch = thePatch;
	}
}
