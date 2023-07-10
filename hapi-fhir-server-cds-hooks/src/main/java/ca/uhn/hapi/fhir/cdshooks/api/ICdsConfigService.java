package ca.uhn.hapi.fhir.cdshooks.api;

import ca.uhn.fhir.context.FhirContext;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.annotation.Nonnull;

public interface ICdsConfigService {
	@Nonnull
	FhirContext getFhirContext();

	@Nonnull
	ObjectMapper getObjectMapper();
}
