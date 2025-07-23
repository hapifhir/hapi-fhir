package ca.uhn.fhir.batch2.jobs.bulkmodify.patch;

import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.base.BaseBulkModifyJobParameters;
import ca.uhn.fhir.context.FhirContext;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import org.hl7.fhir.instance.model.api.IBaseResource;

public class BulkPatchJobParameters extends BaseBulkModifyJobParameters {

	@JsonRawValue
	@JsonProperty("fhirPatch")
	private String myFhirPatch;

	public void setFhirPatch(FhirContext theContext, IBaseResource theFhirPatch) {
		myFhirPatch = theContext.newJsonParser().encodeResourceToString(theFhirPatch);
	}

	public IBaseResource getFhirPatch(FhirContext theContext) {
		return theContext.newJsonParser().parseResource(myFhirPatch);
	}


}
