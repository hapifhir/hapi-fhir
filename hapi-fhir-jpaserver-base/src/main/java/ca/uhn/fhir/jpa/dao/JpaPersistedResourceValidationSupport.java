package ca.uhn.fhir.jpa.dao;

/*-
 * #%L
 * HAPI FHIR JPA Server
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

import ca.uhn.fhir.i18n.Msg;
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
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
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

import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
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
@Transactional(value = Transactional.TxType.REQUIRED)
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
	private Class<? extends IBaseResource> myQuestionnaireType;
	private Class<? extends IBaseResource> myImplementationGuideType;
	private Cache<String, IBaseResource> myLoadCache = Caffeine.newBuilder().maximumSize(1000).expireAfterWrite(1, TimeUnit.MINUTES).build();

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
	public IBaseResource fetchCodeSystem(String theSystem) {
		if (TermReadSvcUtil.isLoincUnversionedCodeSystem(theSystem)) {
			Optional<IBaseResource> currentCSOpt = getCodeSystemCurrentVersion(new UriType(theSystem));
			if (! currentCSOpt.isPresent()) {
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
		if (! theUrl.getValueAsString().contains(LOINC_LOW))  return Optional.empty();

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
		if (! vsIdOpt.isPresent())  return Optional.empty();

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
		IBundleProvider search = myDaoRegistry.getResourceDao("StructureDefinition").search(new SearchParameterMap().setLoadSynchronousUpTo(1000));
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
			return null;
		}

		return (T) fetched;
	}

	private <T extends IBaseResource> IBaseResource doFetchResource(@Nullable Class<T> theClass, String theUri) {
		if (theClass == null) {
			Supplier<IBaseResource>[] fetchers = new Supplier[]{
				() -> doFetchResource(ValueSet.class, theUri),
				() -> doFetchResource(CodeSystem.class, theUri),
				() -> doFetchResource(StructureDefinition.class, theUri)
			};
			return Arrays
				.stream(fetchers)
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
				throw new IllegalArgumentException(Msg.code(952) + "Can't fetch resource type: " + resourceName);
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
		myStructureDefinitionType = myFhirContext.getResourceDefinition("StructureDefinition").getImplementingClass();
		myValueSetType = myFhirContext.getResourceDefinition("ValueSet").getImplementingClass();
		myQuestionnaireType = myFhirContext.getResourceDefinition("Questionnaire").getImplementingClass();
		myImplementationGuideType = myFhirContext.getResourceDefinition("ImplementationGuide").getImplementingClass();

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
