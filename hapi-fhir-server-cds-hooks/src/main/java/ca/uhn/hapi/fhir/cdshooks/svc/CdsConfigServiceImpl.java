package ca.uhn.hapi.fhir.cdshooks.svc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.hapi.fhir.cdshooks.api.ICdsConfigService;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.annotation.Nonnull;

public class CdsConfigServiceImpl implements ICdsConfigService {
	private final FhirContext myFhirContext;
	private final ObjectMapper myObjectMapper;

	public CdsConfigServiceImpl(@Nonnull FhirContext theFhirContext, @Nonnull ObjectMapper theObjectMapper) {
		myFhirContext = theFhirContext;
		myObjectMapper = theObjectMapper;
	}

	@Nonnull
	@Override
	public FhirContext getFhirContext() {
		return myFhirContext;
	}

	@Nonnull
	@Override
	public ObjectMapper getObjectMapper() {
		return myObjectMapper;
	}

}
