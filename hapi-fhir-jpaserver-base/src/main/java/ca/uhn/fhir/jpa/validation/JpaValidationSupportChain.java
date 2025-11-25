/*
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.config.JpaConfig;
import ca.uhn.fhir.jpa.packages.NpmJpaValidationSupport;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.term.api.ITermConceptMappingSvc;
import ca.uhn.fhir.jpa.term.api.ITermReadSvc;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService;
import org.hl7.fhir.common.hapi.validation.support.InMemoryTerminologyServerValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.SnapshotGeneratingValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.common.hapi.validation.validator.WorkerContextValidationSupportAdapter;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

public class JpaValidationSupportChain extends ValidationSupportChain {

	private static final Logger ourLog = LoggerFactory.getLogger(JpaValidationSupportChain.class);

	private final FhirContext myFhirContext;
	private final WorkerContextValidationSupportAdapter myWorkerContextValidationSupportAdapter;

	@Autowired
	@Qualifier(JpaConfig.JPA_VALIDATION_SUPPORT)
	public IValidationSupport myJpaValidationSupport;

	@Qualifier("myDefaultProfileValidationSupport")
	@Autowired
	private IValidationSupport myDefaultProfileValidationSupport;

	@Autowired
	private ITermReadSvc myTerminologyService;

	@Autowired
	private NpmJpaValidationSupport myNpmJpaValidationSupport;

	@Autowired
	private ITermConceptMappingSvc myConceptMappingSvc;

	@Autowired
	private InMemoryTerminologyServerValidationSupport myInMemoryTerminologyServerValidationSupport;

	@Autowired
	private DaoRegistry myDaoRegistry;

	/**
	 * Constructor
	 */
	public JpaValidationSupportChain(
			FhirContext theFhirContext,
			CacheConfiguration theCacheConfiguration,
			WorkerContextValidationSupportAdapter theWorkerContextValidationSupportAdapter) {
		super(theCacheConfiguration);

		assert theFhirContext != null;
		assert theCacheConfiguration != null;

		myFhirContext = theFhirContext;
		myWorkerContextValidationSupportAdapter = theWorkerContextValidationSupportAdapter;
	}

	@Override
	public FhirContext getFhirContext() {
		return myFhirContext;
	}

	@PreDestroy
	public void flush() {
		invalidateCaches();
	}

	@PostConstruct
	public void postConstruct() {
		myWorkerContextValidationSupportAdapter.setValidationSupport(this);

		// DefaultProfileValidationSupport comes first for performance (in-memory lookup).
		// We then remove any URLs from its cache that exist in JPA, so that user-persisted
		// resources take precedence over built-in defaults.
		addValidationSupport(myDefaultProfileValidationSupport);
		addValidationSupport(myJpaValidationSupport);
		addValidationSupport(myTerminologyService);
		addValidationSupport(
				new SnapshotGeneratingValidationSupport(myFhirContext, myWorkerContextValidationSupportAdapter));
		addValidationSupport(myInMemoryTerminologyServerValidationSupport);
		addValidationSupport(myNpmJpaValidationSupport);
		addValidationSupport(new CommonCodeSystemsTerminologyService(myFhirContext));
		addValidationSupport(myConceptMappingSvc);

		// Remove any URLs from the default profile cache that exist in JPA,
		// so that JPA-persisted resources take precedence over built-in defaults.
		removeJpaPersistedUrlsFromDefaultProfileCache();
	}

	/**
	 * Removes URLs from the DefaultProfileValidationSupport cache for any CodeSystem or ValueSet
	 * resources that have been persisted in JPA. This allows user-uploaded resources to override
	 * built-in defaults while maintaining the performance benefit of checking the in-memory
	 * default profile cache first.
	 */
	private void removeJpaPersistedUrlsFromDefaultProfileCache() {
		if (!(myDefaultProfileValidationSupport instanceof DefaultProfileValidationSupport)) {
			return;
		}
		DefaultProfileValidationSupport defaultSupport =
				(DefaultProfileValidationSupport) myDefaultProfileValidationSupport;

		// Query for all CodeSystem URLs in JPA
		removeUrlsFromDefaultCache(defaultSupport, "CodeSystem");

		// Query for all ValueSet URLs in JPA
		removeUrlsFromDefaultCache(defaultSupport, "ValueSet");
	}

	@SuppressWarnings("unchecked")
	private void removeUrlsFromDefaultCache(DefaultProfileValidationSupport theDefaultSupport, String theResourceType) {
		if (!myDaoRegistry.isResourceTypeSupported(theResourceType)) {
			return;
		}

		IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(theResourceType);
		SearchParameterMap searchMap = SearchParameterMap.newSynchronous();
		IBundleProvider results = dao.search(searchMap, new SystemRequestDetails());

		List<IBaseResource> resources = results.getAllResources();
		for (IBaseResource resource : resources) {
			String url = extractUrl(resource);
			if (url != null) {
				if ("CodeSystem".equals(theResourceType)) {
					theDefaultSupport.removeCodeSystem(url);
				} else if ("ValueSet".equals(theResourceType)) {
					theDefaultSupport.removeValueSet(url);
				} else {
					ourLog.warn(
							"Unexpected resource type {} when removing URL from default profile cache",
							theResourceType);
					return;
				}
				ourLog.debug(
						"Removed {} URL {} from default profile cache (JPA-persisted resource takes precedence)",
						theResourceType,
						url);
			}
		}

		if (!resources.isEmpty()) {
			ourLog.info(
					"Removed {} {} URLs from default profile cache to allow JPA-persisted resources to take precedence",
					resources.size(),
					theResourceType);
		}
	}

	@SuppressWarnings("unchecked")
	private String extractUrl(IBaseResource theResource) {
		IPrimitiveType<String> urlElement = (IPrimitiveType<String>)
				myFhirContext.newTerser().getSingleValueOrNull(theResource, "url", IPrimitiveType.class);
		return urlElement != null ? urlElement.getValue() : null;
	}

	/**
	 * Removes a CodeSystem URL from the default profile cache.
	 * Called when a CodeSystem is created or updated in JPA.
	 */
	public void removeCodeSystemFromDefaultCache(String theUrl) {
		if (myDefaultProfileValidationSupport instanceof DefaultProfileValidationSupport defaultSupport) {
			defaultSupport.removeCodeSystem(theUrl);
			invalidateCaches();
		}
	}

	/**
	 * Removes a ValueSet URL from the default profile cache.
	 * Called when a ValueSet is created or updated in JPA.
	 */
	public void removeValueSetFromDefaultCache(String theUrl) {
		if (myDefaultProfileValidationSupport instanceof DefaultProfileValidationSupport defaultSupport) {
			defaultSupport.removeValueSet(theUrl);
			invalidateCaches();
		}
	}
}
