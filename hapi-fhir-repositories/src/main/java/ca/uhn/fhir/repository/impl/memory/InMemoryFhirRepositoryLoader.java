/*-
 * #%L
 * HAPI FHIR - Repository implementations and utilities
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.repository.impl.memory;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.repository.IRepository;
import ca.uhn.fhir.repository.IRepositoryLoader;
import ca.uhn.fhir.repository.impl.BaseSchemeBasedFhirRepositoryLoader;
import jakarta.annotation.Nonnull;
import org.apache.commons.collections4.map.ReferenceMap;

/**
 * ServiceLoader provider for {@link IRepositoryLoader} that loads an InMemoryFhirRepository.
 */
public class InMemoryFhirRepositoryLoader extends BaseSchemeBasedFhirRepositoryLoader implements IRepositoryLoader {

	// This Loader is registered under META-INF/services/ca.uhn.fhir.repository.IRepositoryLoader

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
