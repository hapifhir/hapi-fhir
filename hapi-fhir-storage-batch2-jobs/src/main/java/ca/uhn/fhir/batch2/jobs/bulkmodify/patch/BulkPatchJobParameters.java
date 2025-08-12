package ca.uhn.fhir.batch2.jobs.bulkmodify.patch;

import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.base.BaseBulkModifyJobParameters;
import ca.uhn.fhir.context.FhirContext;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseResource;

import static org.apache.commons.lang3.StringUtils.isBlank;

@SuppressWarnings("UnusedReturnValue")
public class BulkPatchJobParameters extends BaseBulkModifyJobParameters {

	@JsonProperty("fhirPatch")
	private String myFhirPatch;

	public BulkPatchJobParameters setFhirPatch(@Nonnull FhirContext theContext, @Nullable IBaseResource theFhirPatch) {
		myFhirPatch = theFhirPatch != null ? theContext.newJsonParser().encodeResourceToString(theFhirPatch) : null;
		return this;
	}

	public BulkPatchJobParameters setFhirPatch(@Nullable String theFhirPatch) {
		myFhirPatch = theFhirPatch;
		return this;
	}

	@Nullable
	public IBaseResource getFhirPatch(@Nonnull FhirContext theContext) {
		if (isBlank(myFhirPatch)) {
			return null;
		}
		return theContext.newJsonParser().parseResource(myFhirPatch);
	}
}
