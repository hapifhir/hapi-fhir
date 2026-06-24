package ca.uhn.fhir.jpa.batch2.jobs.term.valueset;

import ca.uhn.fhir.batch2.api.IJobParametersValidator;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;

public class PreExpandValueSetParametersValidator implements IJobParametersValidator<PreExpandValueSetParameters> {

	@Nullable
	@Override
	public List<String> validate(RequestDetails theRequestDetails, @Nonnull PreExpandValueSetParameters theParameters) {
		return List.of();
	}

}
