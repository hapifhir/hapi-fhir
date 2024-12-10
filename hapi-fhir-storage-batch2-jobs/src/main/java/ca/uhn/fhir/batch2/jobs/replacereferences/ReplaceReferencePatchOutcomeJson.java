package ca.uhn.fhir.batch2.jobs.replacereferences;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hl7.fhir.r4.model.Bundle;

public class ReplaceReferencePatchOutcomeJson implements IModelJson {
	@JsonProperty("patchResponseBundle")
	String myPatchResponseBundle;

	public ReplaceReferencePatchOutcomeJson() {}

	public ReplaceReferencePatchOutcomeJson(FhirContext theFhirContext, Bundle theResult) {
		myPatchResponseBundle = theFhirContext.newJsonParser().encodeResourceToString(theResult);
	}

	public String getPatchResponseBundle() {
		return myPatchResponseBundle;
	}

	public void setPatchResponseBundle(String thePatchResponseBundle) {
		myPatchResponseBundle = thePatchResponseBundle;
	}
}
