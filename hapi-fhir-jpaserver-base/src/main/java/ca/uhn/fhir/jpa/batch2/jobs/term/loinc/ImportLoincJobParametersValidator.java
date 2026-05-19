package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class ImportLoincJobParametersValidator implements ca.uhn.fhir.batch2.api.IJobParametersValidator<ImportLoincJobParameters> {
	private static final Pattern VERSION_PATTERN = Pattern.compile("(\\d+\\.)*\\d+");

	@Nullable
	@Override
	public List<String> validate(RequestDetails theRequestDetails, @Nonnull ImportLoincJobParameters theParameters) {
		List<String> retVal = new ArrayList<>();

		if (isBlank(theParameters.getVersionId())) {
			retVal.add("Version ID is required");
		} else if (!VERSION_PATTERN.matcher(theParameters.getVersionId()).matches()) {
			retVal.add("Version ID is invalid: " + theParameters.getVersionId());
		}

		return retVal;
	}
}
