/*-
 * #%L
 * HAPI FHIR - Core Library
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
package ca.uhn.fhir.repository;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.repository.impl.UrlRepositoryFactory;
import jakarta.annotation.Nonnull;

/**
 * Static factory methods for creating instances of {@link IRepository}.
 */
public class Repositories {
	/**
	 * Private constructor to prevent instantiation.
	 */
	Repositories() {}

	public static boolean isRepositoryUrl(String theBaseUrl) {
		return UrlRepositoryFactory.isRepositoryUrl(theBaseUrl);
	}

	/**
	 * Constructs a version of {@link IRepository} based on the given URL.
	 * These URLs are expected to be in the form of fhir-repository:subscheme:details.
	 * Currently supported subschemes include:
	 * <ul>
	 *     <li>memory - e.g. fhir-repository:memory:my-repo - the last piece (my-repo) identifies the repository</li>
	 * </ul>
	 * <p>
	 * The subscheme is used to find a matching {@link IRepositoryLoader} implementation.
	 *
	 * @param theFhirContext   the FHIR context to use for the repository.
	 * @param theRepositoryUrl a url of the form fhir-repository:subscheme:details
	 * @return a repository instance
	 * @throws IllegalArgumentException if the URL is not a valid repository URL, or no loader can be found for the URL.
	 */
	@Nonnull
	public static IRepository repositoryForUrl(@Nonnull FhirContext theFhirContext, @Nonnull String theRepositoryUrl) {
		return UrlRepositoryFactory.buildRepository(theFhirContext, theRepositoryUrl);
	}
}
