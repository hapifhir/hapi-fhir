package ca.uhn.fhir.batch2.jobs.bulkmodify.patch;

import ca.uhn.fhir.batch2.api.IJobParametersValidator;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.IDaoRegistry;
import ca.uhn.fhir.jpa.patch.FhirPatch;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ca.uhn.fhir.util.UrlUtil.sanitizeUrlPart;
import static org.apache.commons.lang3.StringUtils.defaultString;

public class BulkPatchJobParametersValidator<PT extends BulkPatchJobParameters> implements IJobParametersValidator<PT> {

	private final FhirContext myFhirContext;
	private static final Pattern URL_PATTERN = Pattern.compile("([A-Z][a-zA-Z0-9]+)\\?.*");
	private final IDaoRegistry myDaoRegistry;

	public BulkPatchJobParametersValidator(FhirContext theFhirContext, IDaoRegistry theDaoRegistry) {
		myFhirContext = theFhirContext;
		myDaoRegistry = theDaoRegistry;
	}

	@Nullable
	@Override
	public List<String> validate(RequestDetails theRequestDetails, @Nonnull PT theParameters) {

		List<String> urls = theParameters.getUrls();
		if (urls.isEmpty()) {
			return List.of("No URLs were provided");
		}

		for (String nextUrl : urls) {
			Matcher matcher = URL_PATTERN.matcher(defaultString(nextUrl));
			if (!matcher.matches()) {
				return List.of("Invalid/unsupported URL (must use syntax '{resourceType}?[optional params]': "
						+ sanitizeUrlPart(nextUrl));
			}
			String resourceType = matcher.group(1);
			if (!myDaoRegistry.isResourceTypeSupported(resourceType)) {
				return List.of("Resource type " + sanitizeUrlPart(resourceType) + " is not supported");
			}
		}

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
			return List.of(
					"FHIRPatch document must be a Parameters resource, found: " + myFhirContext.getResourceType(patch));
		}

		try {
			new FhirPatch(myFhirContext).validate(patch);
		} catch (InvalidRequestException e) {
			return List.of("Provided FHIRPatch document is invalid: " + e.getMessage());
		}

		return List.of();
	}
}
