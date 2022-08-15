package ca.uhn.fhir.jpa.searchparam.config;

/*-
 * #%L
 * HAPI FHIR Search Parameters
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.cache.IResourceChangeListener;
import ca.uhn.fhir.jpa.cache.IResourceChangeListenerCacheRefresher;
import ca.uhn.fhir.jpa.cache.IResourceChangeListenerRegistry;
import ca.uhn.fhir.jpa.cache.ResourceChangeListenerCache;
import ca.uhn.fhir.jpa.cache.ResourceChangeListenerCacheFactory;
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
import ca.uhn.fhir.jpa.searchparam.registry.SearchParamRegistryImpl;
import ca.uhn.fhir.jpa.searchparam.registry.SearchParameterCanonicalizer;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
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
				throw new IllegalStateException(Msg.code(501) + "Can not handle version: " + myFhirContext.getVersion().getVersion());
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
	public SearchParamExtractorService searchParamExtractorService() {
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
	IResourceChangeListenerRegistry resourceChangeListenerRegistry(FhirContext theFhirContext, ResourceChangeListenerCacheFactory theResourceChangeListenerCacheFactory, InMemoryResourceMatcher theInMemoryResourceMatcher) {
		return new ResourceChangeListenerRegistryImpl(theFhirContext, theResourceChangeListenerCacheFactory, theInMemoryResourceMatcher);
	}

	@Bean
	IResourceChangeListenerCacheRefresher resourceChangeListenerCacheRefresher() {
		return new ResourceChangeListenerCacheRefresherImpl();
	}

	@Bean
	ResourceChangeListenerCacheFactory registeredResourceListenerFactory() {
		return new ResourceChangeListenerCacheFactory();
	}

	@Bean
	@Scope("prototype")
	ResourceChangeListenerCache registeredResourceChangeListener(String theResourceName, IResourceChangeListener theResourceChangeListener, SearchParameterMap theSearchParameterMap, long theRemoteRefreshIntervalMs) {
		return new ResourceChangeListenerCache(theResourceName, theResourceChangeListener, theSearchParameterMap, theRemoteRefreshIntervalMs);
	}
}
