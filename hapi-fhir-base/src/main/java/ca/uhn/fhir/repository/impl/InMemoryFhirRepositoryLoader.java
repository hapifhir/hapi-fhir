package ca.uhn.fhir.repository.impl;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.repository.IRepository;
import jakarta.annotation.Nonnull;
import org.apache.commons.collections4.map.ReferenceMap;

public class InMemoryFhirRepositoryLoader extends SchemeBasedFhirRepositoryLoader implements IRepositoryLoader {

	public static final String URL_SUB_SCHEME = "memory";
	static final ReferenceMap<String, InMemoryFhirRepository> ourRepositories = new ReferenceMap<>();

	public InMemoryFhirRepositoryLoader() {
		super(URL_SUB_SCHEME);
	}

	@Nonnull
	@Override
	public IRepository loadRepository(@Nonnull IRepositoryRequest theRepositoryRequest) {
		FhirContext context = theRepositoryRequest
				.getFhirContext()
				.orElseThrow(() -> new IllegalArgumentException(
						Msg.code(2736) + "The :memory: FHIR repository requires a FhirContext."));

		String memoryKey = theRepositoryRequest.getDetails();
		return ourRepositories.computeIfAbsent(memoryKey, k -> {
			InMemoryFhirRepository inMemoryFhirRepository = InMemoryFhirRepository.emptyRepository(context);
			inMemoryFhirRepository.setBaseUrl(theRepositoryRequest.getUrl());
			return inMemoryFhirRepository;
		});
	}
}
