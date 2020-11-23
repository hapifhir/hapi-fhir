package ca.uhn.fhir.jpa.searchparam.config;

/*-
 * #%L
 * HAPI FHIR Search Parameters
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.cache.IResourceChangeListener;
import ca.uhn.fhir.jpa.cache.IResourceChangeListenerCacheRefresher;
import ca.uhn.fhir.jpa.cache.IResourceChangeListenerRegistry;
import ca.uhn.fhir.jpa.cache.RegisteredResourceChangeListener;
import ca.uhn.fhir.jpa.cache.RegisteredResourceListenerFactory;
import ca.uhn.fhir.jpa.cache.ResourceChangeListenerCacheRefresherImpl;
import ca.uhn.fhir.jpa.cache.ResourceChangeListenerRegistryImpl;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamExtractor;
import ca.uhn.fhir.jpa.searchparam.extractor.SearchParamExtractorDstu2;
import ca.uhn.fhir.jpa.searchparam.extractor.SearchParamExtractorDstu3;
import ca.uhn.fhir.jpa.searchparam.extractor.SearchParamExtractorR4;
import ca.uhn.fhir.jpa.searchparam.extractor.SearchParamExtractorR5;
import ca.uhn.fhir.jpa.searchparam.extractor.SearchParamExtractorService;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryResourceMatcher;
import ca.uhn.fhir.jpa.searchparam.matcher.IndexedSearchParamExtractor;
import ca.uhn.fhir.jpa.searchparam.matcher.SearchParamMatcher;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.jpa.searchparam.registry.SearchParamRegistryImpl;
import ca.uhn.fhir.jpa.searchparam.registry.SearchParameterCanonicalizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

@Configuration
public class SearchParamConfig {

	@Autowired
	private FhirContext myFhirContext;

	@Bean
	public ISearchParamExtractor searchParamExtractor() {
		switch (myFhirContext.getVersion().getVersion()) {

			case DSTU2:
				return new SearchParamExtractorDstu2();
			case DSTU3:
				return new SearchParamExtractorDstu3();
			case R4:
				return new SearchParamExtractorR4();
			case R5:
				return new SearchParamExtractorR5();
			case DSTU2_HL7ORG:
			case DSTU2_1:
			default:
				throw new IllegalStateException("Can not handle version: " + myFhirContext.getVersion().getVersion());
		}
	}

	@Bean
	public ISearchParamRegistry searchParamRegistry() {
		return new SearchParamRegistryImpl();
	}

	@Bean
	public MatchUrlService matchUrlService() {
		return new MatchUrlService();
	}

	@Bean
	@Lazy
	public SearchParamExtractorService searchParamExtractorService(){
		return new SearchParamExtractorService();
	}

	@Bean
	@Lazy
	public SearchParameterCanonicalizer searchParameterCanonicalizer(FhirContext theFhirContext) {
		return new SearchParameterCanonicalizer(theFhirContext);
	}

	@Bean
	public IndexedSearchParamExtractor indexedSearchParamExtractor() {
		return new IndexedSearchParamExtractor();
	}

	@Bean
	public InMemoryResourceMatcher inMemoryResourceMatcher() {
		return new InMemoryResourceMatcher();
	}

	@Bean
	public SearchParamMatcher searchParamMatcher() {
		return new SearchParamMatcher();
	}

	@Bean
	IResourceChangeListenerRegistry resourceChangeListenerRegistry() {
		return new ResourceChangeListenerRegistryImpl();
	}

	@Bean
	IResourceChangeListenerCacheRefresher resourceChangeListenerCacheRefresher() {
		return new ResourceChangeListenerCacheRefresherImpl();
	}

	@Bean
	RegisteredResourceListenerFactory registeredResourceListenerFactory() {
		return new RegisteredResourceListenerFactory();
	}
	@Bean
	@Scope("prototype")
	RegisteredResourceChangeListener registeredResourceChangeListener(String theResourceName, IResourceChangeListener theResourceChangeListener, SearchParameterMap theSearchParameterMap, long theRemoteRefreshIntervalMs) {
		return new RegisteredResourceChangeListener(theResourceName, theResourceChangeListener, theSearchParameterMap, theRemoteRefreshIntervalMs);
	}
}
