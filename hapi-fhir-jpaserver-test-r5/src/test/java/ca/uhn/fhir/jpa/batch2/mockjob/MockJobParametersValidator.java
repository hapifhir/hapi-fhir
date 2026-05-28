package ca.uhn.fhir.jpa.batch2.mockjob;

import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;

public class MockJobParametersValidator<T extends IModelJson> implements ca.uhn.fhir.batch2.api.IJobParametersValidator<T> {

	@Nullable
	@Override
	public List<String> validate(RequestDetails theRequestDetails, @Nonnull T theParameters) {
		return List.of();
	}

}
