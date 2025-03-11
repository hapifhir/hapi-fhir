/*-
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
package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.term.TermReadSvcUtil;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.UriParam;
import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.ImplementationGuide;
import org.hl7.fhir.r4.model.Questionnaire;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.ValueSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.hl7.fhir.common.hapi.validation.support.ValidationConstants.LOINC_LOW;
import static org.hl7.fhir.instance.model.api.IAnyResource.SP_RES_LAST_UPDATED;

/**
 * This class is a {@link IValidationSupport Validation support} module that loads
 * validation resources (StructureDefinition, ValueSet, CodeSystem, etc.) from the resources
 * persisted in the JPA server.
 */
public class JpaPersistedResourceValidationSupport implements IValidationSupport {

	private static final Logger ourLog = LoggerFactory.getLogger(JpaPersistedResourceValidationSupport.class);

	private final FhirContext myFhirContext;

	@Autowired
	private DaoRegistry myDaoRegistry;

	private Class<? extends IBaseResource> myCodeSystemType;
	private Class<? extends IBaseResource> myStructureDefinitionType;
	private Class<? extends IBaseResource> myValueSetType;

	/**
	 * Constructor
	 */
	public JpaPersistedResourceValidationSupport(FhirContext theFhirContext) {
		super();
		Validate.notNull(theFhirContext, "theFhirContext must not be null");
		myFhirContext = theFhirContext;
	}

	@Override
	public String getName() {
		return myFhirContext.getVersion().getVersion() + " JPA Validation Support";
	}

	@Override
	public IBaseResource fetchCodeSystem(String theSystem) {
		if (TermReadSvcUtil.isLoincUnversionedCodeSystem(theSystem)) {
			IIdType id = myFhirContext.getVersion().newIdType("CodeSystem", LOINC_LOW);
			return findResourceByIdWithNoException(id, myCodeSystemType);
		}

		return fetchResource(myCodeSystemType, theSystem);
	}

	@Override
	public IBaseResource fetchValueSet(String theSystem) {
		if (TermReadSvcUtil.isLoincUnversionedValueSet(theSystem)) {
			Optional<String> vsIdOpt = TermReadSvcUtil.getValueSetId(theSystem);
			if (vsIdOpt.isEmpty()) {
				return null;
			}
			IIdType id = myFhirContext.getVersion().newIdType("ValueSet", vsIdOpt.get());
			return findResourceByIdWithNoException(id, myValueSetType);
		}

		return fetchResource(myValueSetType, theSystem);
	}

	/**
	 * Performs a lookup by ID, with no exception thrown (since that can mark the active
	 * transaction as rollback).
	 */
	@Nullable
	private IBaseResource findResourceByIdWithNoException(IIdType id, Class<? extends IBaseResource> type) {
		SearchParameterMap map = SearchParameterMap.newSynchronous()
				.setLoadSynchronousUpTo(1)
				.add(IAnyResource.SP_RES_ID, new TokenParam(id.getValue()));
		IFhirResourceDao<? extends IBaseResource> dao = myDaoRegistry.getResourceDao(type);
		IBundleProvider outcome = dao.search(map, new SystemRequestDetails());
		List<IBaseResource> resources = outcome.getResources(0, 1);
		if (resources.isEmpty()) {
			return null;
		} else {
			return resources.get(0);
		}
	}

	@Override
	public IBaseResource fetchStructureDefinition(String theUrl) {
		assert myStructureDefinitionType != null;
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

		return (T) doFetchResource(theClass, theUri);
	}

	private <T extends IBaseResource> IBaseResource doFetchResource(@Nullable Class<T> theClass, String theUri) {
		if (theClass == null) {
			Supplier<IBaseResource>[] fetchers = new Supplier[] {
				() -> doFetchResource(ValueSet.class, theUri),
				() -> doFetchResource(CodeSystem.class, theUri),
				() -> doFetchResource(StructureDefinition.class, theUri)
			};
			return Arrays.stream(fetchers)
					.map(Supplier::get)
					.filter(Objects::nonNull)
					.findFirst()
					.orElse(null);
		}

		IdType id = new IdType(theUri);
		boolean localReference = id.hasBaseUrl() == false && id.hasIdPart() == true;

		String resourceName = myFhirContext.getResourceType(theClass);
		IBundleProvider search;
		switch (resourceName) {
			case "ValueSet":
				if (localReference) {
					SearchParameterMap params = new SearchParameterMap();
					params.setLoadSynchronousUpTo(1);
					params.add(IAnyResource.SP_RES_ID, new StringParam(theUri));
					search = myDaoRegistry.getResourceDao(resourceName).search(params);
					if (search.isEmpty()) {
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
					params.setSort(new SortSpec(SP_RES_LAST_UPDATED).setOrder(SortOrderEnum.DESC));
					search = myDaoRegistry.getResourceDao(resourceName).search(params);

					if (search.isEmpty()
							&& myFhirContext.getVersion().getVersion().isOlderThan(FhirVersionEnum.DSTU3)) {
						params = new SearchParameterMap();
						params.setLoadSynchronousUpTo(1);
						if (versionSeparator != -1) {
							params.add(ValueSet.SP_VERSION, new TokenParam(theUri.substring(versionSeparator + 1)));
							params.add(
									ca.uhn.fhir.model.dstu2.resource.ValueSet.SP_SYSTEM,
									new UriParam(theUri.substring(0, versionSeparator)));
						} else {
							params.add(ca.uhn.fhir.model.dstu2.resource.ValueSet.SP_SYSTEM, new UriParam(theUri));
						}
						params.setSort(new SortSpec(SP_RES_LAST_UPDATED).setOrder(SortOrderEnum.DESC));
						search = myDaoRegistry.getResourceDao(resourceName).search(params);
					}
				}
				break;
			case "StructureDefinition": {
				// Don't allow the core FHIR definitions to be overwritten
				if (theUri.startsWith("http://hl7.org/fhir/StructureDefinition/")) {
					String typeName = theUri.substring("http://hl7.org/fhir/StructureDefinition/".length());
					if (myFhirContext.getElementDefinition(typeName) != null) {
						return null;
					}
				}
				SearchParameterMap params = new SearchParameterMap();
				params.setLoadSynchronousUpTo(1);
				int versionSeparator = theUri.lastIndexOf('|');
				if (versionSeparator != -1) {
					params.add(StructureDefinition.SP_VERSION, new TokenParam(theUri.substring(versionSeparator + 1)));
					params.add(StructureDefinition.SP_URL, new UriParam(theUri.substring(0, versionSeparator)));
				} else {
					params.add(StructureDefinition.SP_URL, new UriParam(theUri));
				}
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
				params.setSort(new SortSpec(SP_RES_LAST_UPDATED).setOrder(SortOrderEnum.DESC));
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
			return null;
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
}
