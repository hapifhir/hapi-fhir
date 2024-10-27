/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.term.TermReadSvcUtil;
import ca.uhn.fhir.jpa.term.api.ITermReadSvc;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.sl.cache.Cache;
import ca.uhn.fhir.sl.cache.CacheFactory;
import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.ImplementationGuide;
import org.hl7.fhir.r4.model.Questionnaire;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.UriType;
import org.hl7.fhir.r4.model.ValueSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.hl7.fhir.common.hapi.validation.support.ValidationConstants.LOINC_LOW;

/**
 * This class is a {@link IValidationSupport Validation support} module that loads
 * validation resources (StructureDefinition, ValueSet, CodeSystem, etc.) from the resources
 * persisted in the JPA server.
 */
@Transactional(propagation = Propagation.REQUIRED)
public class JpaPersistedResourceValidationSupport implements IValidationSupport {

	private static final Logger ourLog = LoggerFactory.getLogger(JpaPersistedResourceValidationSupport.class);

	private final FhirContext myFhirContext;
	private final IBaseResource myNoMatch;

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	private ITermReadSvc myTermReadSvc;

	private Class<? extends IBaseResource> myCodeSystemType;
	private Class<? extends IBaseResource> myStructureDefinitionType;
	private Class<? extends IBaseResource> myValueSetType;

	// TODO: JA2 We shouldn't need to cache here, but we probably still should since the
	// TermReadSvcImpl calls these methods as a part of its "isCodeSystemSupported" calls.
	// We should modify CachingValidationSupport to cache the results of "isXXXSupported"
	// at which point we could do away with this cache
	// TODO:  LD: This cache seems to supersede the cache in CachingValidationSupport, as that cache is set to
	// 10 minutes, but this 1 minute cache now determines the expiry.
	// This new behaviour was introduced between the 7.0.0 release and the current master (7.2.0)
	private Cache<String, IBaseResource> myLoadCache = CacheFactory.build(TimeUnit.MINUTES.toMillis(1), 1000);

	/**
	 * Constructor
	 */
	public JpaPersistedResourceValidationSupport(FhirContext theFhirContext) {
		super();
		Validate.notNull(theFhirContext);
		myFhirContext = theFhirContext;

		myNoMatch = myFhirContext.getResourceDefinition("Basic").newInstance();
	}

	@Override
	public String getName() {
		return myFhirContext.getVersion().getVersion() + " JPA Validation Support";
	}

	@Override
	public IBaseResource fetchCodeSystem(String theSystem) {
		if (TermReadSvcUtil.isLoincUnversionedCodeSystem(theSystem)) {
			Optional<IBaseResource> currentCSOpt = getCodeSystemCurrentVersion(new UriType(theSystem));
			if (!currentCSOpt.isPresent()) {
				ourLog.info("Couldn't find current version of CodeSystem: " + theSystem);
			}
			return currentCSOpt.orElse(null);
		}

		return fetchResource(myCodeSystemType, theSystem);
	}

	/**
	 * Obtains the current version of a CodeSystem using the fact that the current
	 * version is always pointed by the ForcedId for the no-versioned CS
	 */
	private Optional<IBaseResource> getCodeSystemCurrentVersion(UriType theUrl) {
		if (!theUrl.getValueAsString().contains(LOINC_LOW)) {
			return Optional.empty();
		}

		return myTermReadSvc.readCodeSystemByForcedId(LOINC_LOW);
	}

	@Override
	public IBaseResource fetchValueSet(String theSystem) {
		if (TermReadSvcUtil.isLoincUnversionedValueSet(theSystem)) {
			Optional<IBaseResource> currentVSOpt = getValueSetCurrentVersion(new UriType(theSystem));
			return currentVSOpt.orElse(null);
		}

		return fetchResource(myValueSetType, theSystem);
	}

	/**
	 * Obtains the current version of a ValueSet using the fact that the current
	 * version is always pointed by the ForcedId for the no-versioned VS
	 */
	private Optional<IBaseResource> getValueSetCurrentVersion(UriType theUrl) {
		Optional<String> vsIdOpt = TermReadSvcUtil.getValueSetId(theUrl.getValueAsString());
		if (!vsIdOpt.isPresent()) {
			return Optional.empty();
		}

		IFhirResourceDao<? extends IBaseResource> valueSetResourceDao = myDaoRegistry.getResourceDao(myValueSetType);
		IBaseResource valueSet = valueSetResourceDao.read(new IdDt("ValueSet", vsIdOpt.get()));
		return Optional.ofNullable(valueSet);
	}

	@Override
	public IBaseResource fetchStructureDefinition(String theUrl) {
		return fetchResource(myStructureDefinitionType, theUrl);
	}

	@SuppressWarnings("unchecked")
	@Nullable
	@Override
	public <T extends IBaseResource> List<T> fetchAllStructureDefinitions() {
		if (!myDaoRegistry.isResourceTypeSupported("StructureDefinition")) {
			return null;
		}
		IBundleProvider search = myDaoRegistry
				.getResourceDao("StructureDefinition")
				.search(new SearchParameterMap().setLoadSynchronousUpTo(1000), new SystemRequestDetails());
		return (List<T>) search.getResources(0, 1000);
	}

	@Override
	@SuppressWarnings({"unchecked", "unused"})
	public <T extends IBaseResource> T fetchResource(@Nullable Class<T> theClass, String theUri) {
		if (isBlank(theUri)) {
			return null;
		}

		String key = theClass + " " + theUri;
		IBaseResource fetched = myLoadCache.get(key, t -> doFetchResource(theClass, theUri));

		if (fetched == myNoMatch) {
			ourLog.debug(
					"Invalidating cache entry for URI: {} since the result of the underlying query is empty", theUri);
			myLoadCache.invalidate(key);
			return null;
		}

		return (T) fetched;
	}

	private <T extends IBaseResource> IBaseResource doFetchResource(@Nullable Class<T> theClass, String theUri) {
		if (theClass == null) {
			Supplier<IBaseResource>[] fetchers = new Supplier[] {
				() -> doFetchResource(ValueSet.class, theUri),
				() -> doFetchResource(CodeSystem.class, theUri),
				() -> doFetchResource(StructureDefinition.class, theUri)
			};
			return Arrays.stream(fetchers)
					.map(t -> t.get())
					.filter(t -> t != myNoMatch)
					.findFirst()
					.orElse(myNoMatch);
		}

		IdType id = new IdType(theUri);
		boolean localReference = false;
		if (id.hasBaseUrl() == false && id.hasIdPart() == true) {
			localReference = true;
		}

		String resourceName = myFhirContext.getResourceType(theClass);
		IBundleProvider search;
		switch (resourceName) {
			case "ValueSet":
				if (localReference) {
					SearchParameterMap params = new SearchParameterMap();
					params.setLoadSynchronousUpTo(1);
					params.add(IAnyResource.SP_RES_ID, new StringParam(theUri));
					search = myDaoRegistry.getResourceDao(resourceName).search(params);
					if (search.size() == 0) {
						params = new SearchParameterMap();
						params.setLoadSynchronousUpTo(1);
						params.add(ValueSet.SP_URL, new UriParam(theUri));
						search = myDaoRegistry.getResourceDao(resourceName).search(params);
					}
				} else {
					int versionSeparator = theUri.lastIndexOf('|');
					SearchParameterMap params = new SearchParameterMap();
					params.setLoadSynchronousUpTo(1);
					if (versionSeparator != -1) {
						params.add(ValueSet.SP_VERSION, new TokenParam(theUri.substring(versionSeparator + 1)));
						params.add(ValueSet.SP_URL, new UriParam(theUri.substring(0, versionSeparator)));
					} else {
						params.add(ValueSet.SP_URL, new UriParam(theUri));
					}
					params.setSort(new SortSpec("_lastUpdated").setOrder(SortOrderEnum.DESC));
					search = myDaoRegistry.getResourceDao(resourceName).search(params);

					if (search.isEmpty()
							&& myFhirContext.getVersion().getVersion().isOlderThan(FhirVersionEnum.DSTU3)) {
						params = new SearchParameterMap();
						params.setLoadSynchronousUpTo(1);
						if (versionSeparator != -1) {
							params.add(ValueSet.SP_VERSION, new TokenParam(theUri.substring(versionSeparator + 1)));
							params.add("system", new UriParam(theUri.substring(0, versionSeparator)));
						} else {
							params.add("system", new UriParam(theUri));
						}
						params.setSort(new SortSpec("_lastUpdated").setOrder(SortOrderEnum.DESC));
						search = myDaoRegistry.getResourceDao(resourceName).search(params);
					}
				}
				break;
			case "StructureDefinition": {
				// Don't allow the core FHIR definitions to be overwritten
				if (theUri.startsWith("http://hl7.org/fhir/StructureDefinition/")) {
					String typeName = theUri.substring("http://hl7.org/fhir/StructureDefinition/".length());
					if (myFhirContext.getElementDefinition(typeName) != null) {
						return myNoMatch;
					}
				}
				SearchParameterMap params = new SearchParameterMap();
				params.setLoadSynchronousUpTo(1);
				params.add(StructureDefinition.SP_URL, new UriParam(theUri));
				search = myDaoRegistry.getResourceDao("StructureDefinition").search(params);
				break;
			}
			case "Questionnaire": {
				SearchParameterMap params = new SearchParameterMap();
				params.setLoadSynchronousUpTo(1);
				if (localReference || myFhirContext.getVersion().getVersion().isEquivalentTo(FhirVersionEnum.DSTU2)) {
					params.add(IAnyResource.SP_RES_ID, new StringParam(id.getIdPart()));
				} else {
					params.add(Questionnaire.SP_URL, new UriParam(id.getValue()));
				}
				search = myDaoRegistry.getResourceDao("Questionnaire").search(params);
				break;
			}
			case "CodeSystem": {
				int versionSeparator = theUri.lastIndexOf('|');
				SearchParameterMap params = new SearchParameterMap();
				params.setLoadSynchronousUpTo(1);
				if (versionSeparator != -1) {
					params.add(CodeSystem.SP_VERSION, new TokenParam(theUri.substring(versionSeparator + 1)));
					params.add(CodeSystem.SP_URL, new UriParam(theUri.substring(0, versionSeparator)));
				} else {
					params.add(CodeSystem.SP_URL, new UriParam(theUri));
				}
				params.setSort(new SortSpec("_lastUpdated").setOrder(SortOrderEnum.DESC));
				search = myDaoRegistry.getResourceDao(resourceName).search(params);
				break;
			}
			case "ImplementationGuide":
			case "SearchParameter": {
				SearchParameterMap params = new SearchParameterMap();
				params.setLoadSynchronousUpTo(1);
				params.add(ImplementationGuide.SP_URL, new UriParam(theUri));
				search = myDaoRegistry.getResourceDao(resourceName).search(params);
				break;
			}
			default:
				// N.B.: this code assumes that we are searching by canonical URL and that the CanonicalType in question
				// has a URL
				SearchParameterMap params = new SearchParameterMap();
				params.setLoadSynchronousUpTo(1);
				params.add("url", new UriParam(theUri));
				search = myDaoRegistry.getResourceDao(resourceName).search(params);
		}

		Integer size = search.size();
		if (size == null || size == 0) {
			return myNoMatch;
		}

		if (size > 1) {
			ourLog.warn("Found multiple {} instances with URL search value of: {}", resourceName, theUri);
		}

		return search.getResources(0, 1).get(0);
	}

	@Override
	public FhirContext getFhirContext() {
		return myFhirContext;
	}

	@PostConstruct
	public void start() {
		myStructureDefinitionType =
				myFhirContext.getResourceDefinition("StructureDefinition").getImplementingClass();
		myValueSetType = myFhirContext.getResourceDefinition("ValueSet").getImplementingClass();

		if (myFhirContext.getVersion().getVersion().isNewerThan(FhirVersionEnum.DSTU2)) {
			myCodeSystemType = myFhirContext.getResourceDefinition("CodeSystem").getImplementingClass();
		} else {
			myCodeSystemType = myFhirContext.getResourceDefinition("ValueSet").getImplementingClass();
		}
	}

	public void clearCaches() {
		myLoadCache.invalidateAll();
	}
}
