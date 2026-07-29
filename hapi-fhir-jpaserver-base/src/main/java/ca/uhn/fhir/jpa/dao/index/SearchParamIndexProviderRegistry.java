/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.dao.index;

import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Ordered chain-of-responsibility over the registered {@link ISearchParamIndexProvider} beans. This is
 * the single dispatch point shared by the read path ({@code SqlObjectFactory}), the write path
 * ({@link DaoSearchParamSynchronizer}) and expunge ({@code JpaResourceExpungeService}).
 *
 * <p>Providers are ordered by Spring's {@code @Order} / {@link org.springframework.core.Ordered}
 * contract. {@link #resolveProvider(SearchParamIndexRouting)} returns the first provider that
 * {@link ISearchParamIndexProvider#supports supports} the routing; an empty result signals that the
 * caller should fall back to the built-in index.
 */
public class SearchParamIndexProviderRegistry {

	private final List<ISearchParamIndexProvider> myOrderedProviders;

	public SearchParamIndexProviderRegistry(List<ISearchParamIndexProvider> theProviders) {
		List<ISearchParamIndexProvider> providers = new ArrayList<>(theProviders == null ? List.of() : theProviders);
		AnnotationAwareOrderComparator.sort(providers);
		myOrderedProviders = List.copyOf(providers);
	}

	/**
	 * @return the highest-precedence provider (by {@code @Order}) that supports the routing, or empty if none does.
	 */
	public Optional<ISearchParamIndexProvider> resolveProvider(SearchParamIndexRouting theRouting) {
		return myOrderedProviders.stream()
				.filter(provider -> provider.supports(theRouting))
				.findFirst();
	}

	/**
	 * @return {@code true} if any registered provider suppresses built-in index write.
	 */
	public boolean isBuiltInIndexWriteSuppressed(SearchParamIndexRouting theRouting) {
		return myOrderedProviders.stream().anyMatch(provider -> provider.suppressesBuiltInIndex(theRouting));
	}

	/**
	 * @return all registered providers, in {@code @Order}
	 */
	public List<ISearchParamIndexProvider> getProviders() {
		return myOrderedProviders;
	}
}
