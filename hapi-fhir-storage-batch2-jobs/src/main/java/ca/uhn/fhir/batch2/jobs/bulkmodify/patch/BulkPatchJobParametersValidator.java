package ca.uhn.fhir.batch2.jobs.bulkmodify.patch;

import ca.uhn.fhir.batch2.api.IJobParametersValidator;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.patch.FhirPatch;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.List;

public class BulkPatchJobParametersValidator implements IJobParametersValidator<BulkPatchJobParameters> {

	private final FhirContext myFhirContext;

	public BulkPatchJobParametersValidator(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
	}

	@Nullable
	@Override
	public List<String> validate(RequestDetails theRequestDetails, @Nonnull BulkPatchJobParameters theParameters) {

		IBaseResource patch;
		try {
			patch = theParameters.getFhirPatch(myFhirContext);
			if (patch == null) {
				return List.of("No Patch document was provided");
			}
		} catch (DataFormatException e) {
			return List.of("Failed to parse FHIRPatch document: " + e.getMessage());
		}

		if (!"Parameters".equals(myFhirContext.getResourceType(patch))) {
			return List.of("FHIRPatch document must be a Parameters resource, found: " + myFhirContext.getResourceType(patch));
		}

		try {
			new FhirPatch(myFhirContext).validate(patch);
		} catch (InvalidRequestException e) {
			return List.of("Provided FHIRPatch document is invalid: " + e.getMessage());
		}

		return List.of();
	}
}
