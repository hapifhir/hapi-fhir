package ca.uhn.fhir.jpa.batch2.mockjob;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;

public class MockJobParametersValidator implements ca.uhn.fhir.batch2.api.IJobParametersValidator<MockJobParameters> {

	@Nullable
	@Override
	public List<String> validate(RequestDetails theRequestDetails, @Nonnull MockJobParameters theParameters) {
		return List.of();
	}

}
