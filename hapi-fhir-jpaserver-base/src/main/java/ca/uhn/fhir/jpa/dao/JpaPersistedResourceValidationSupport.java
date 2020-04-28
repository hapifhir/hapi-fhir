package ca.uhn.fhir.jpa.dao;

/*-
 * #%L
 * HAPI FHIR JPA Server
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
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.UriParam;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.ImplementationGuide;
import org.hl7.fhir.r4.model.Questionnaire;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.ValueSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * This class is a {@link IValidationSupport Validation support} module that loads
 * validation resources (StructureDefinition, ValueSet, CodeSystem, etc.) from the resources
 * persisted in the JPA server.
 */
@Transactional(value = Transactional.TxType.REQUIRED)
public class JpaPersistedResourceValidationSupport implements IValidationSupport {

	private static final Logger ourLog = LoggerFactory.getLogger(JpaPersistedResourceValidationSupport.class);

	private final FhirContext myFhirContext;

	@Autowired
	private DaoRegistry myDaoRegistry;
	private Class<? extends IBaseResource> myCodeSystemType;
	private Class<? extends IBaseResource> myStructureDefinitionType;
	private Class<? extends IBaseResource> myValueSetType;
	private Class<? extends IBaseResource> myQuestionnaireType;
	private Class<? extends IBaseResource> myImplementationGuideType;

	/**
	 * Constructor
	 */
	public JpaPersistedResourceValidationSupport(FhirContext theFhirContext) {
		super();
		Validate.notNull(theFhirContext);
		myFhirContext = theFhirContext;
	}


	@Override
	public IBaseResource fetchCodeSystem(String theSystem) {
		return fetchResource(myCodeSystemType, theSystem);
	}

	@Override
	public IBaseResource fetchValueSet(String theSystem) {
		return fetchResource(myValueSetType, theSystem);
	}

	@Override
	public IBaseResource fetchStructureDefinition(String theUrl) {
		return fetchResource(myStructureDefinitionType, theUrl);
	}


	@Override
	@SuppressWarnings({"unchecked", "unused"})
	public <T extends IBaseResource> T fetchResource(Class<T> theClass, String theUri) {
		if (isBlank(theUri)) {
			return null;
		}

		IdType id = new IdType(theUri);
		boolean localReference = false;
		if (id.hasBaseUrl() == false && id.hasIdPart() == true) {
			localReference = true;
		}

		String resourceName = myFhirContext.getResourceDefinition(theClass).getName();
		IBundleProvider search;
		if ("ValueSet".equals(resourceName)) {
			if (localReference) {
				SearchParameterMap params = new SearchParameterMap();
				params.setLoadSynchronousUpTo(1);
				params.add(IAnyResource.SP_RES_ID, new StringParam(theUri));
				search = myDaoRegistry.getResourceDao("ValueSet").search(params);
				if (search.size() == 0) {
					params = new SearchParameterMap();
					params.setLoadSynchronousUpTo(1);
					params.add(ValueSet.SP_URL, new UriParam(theUri));
					search = myDaoRegistry.getResourceDao("ValueSet").search(params);
				}
			} else {
				SearchParameterMap params = new SearchParameterMap();
				params.setLoadSynchronousUpTo(1);
				params.add(ValueSet.SP_URL, new UriParam(theUri));
				search = myDaoRegistry.getResourceDao("ValueSet").search(params);
			}
		} else if ("StructureDefinition".equals(resourceName)) {
			// Don't allow the core FHIR definitions to be overwritten
			if (theUri.startsWith("http://hl7.org/fhir/StructureDefinition/")) {
				String typeName = theUri.substring("http://hl7.org/fhir/StructureDefinition/".length());
				if (myFhirContext.getElementDefinition(typeName) != null) {
					return null;
				}
			}
			SearchParameterMap params = new SearchParameterMap();
			params.setLoadSynchronousUpTo(1);
			params.add(StructureDefinition.SP_URL, new UriParam(theUri));
			search = myDaoRegistry.getResourceDao("StructureDefinition").search(params);
		} else if ("Questionnaire".equals(resourceName)) {
			SearchParameterMap params = new SearchParameterMap();
			params.setLoadSynchronousUpTo(1);
			if (localReference || myFhirContext.getVersion().getVersion().isEquivalentTo(FhirVersionEnum.DSTU2)) {
				params.add(IAnyResource.SP_RES_ID, new StringParam(id.getIdPart()));
			} else {
				params.add(Questionnaire.SP_URL, new UriParam(id.getValue()));
			}
			search = myDaoRegistry.getResourceDao("Questionnaire").search(params);
		} else if ("CodeSystem".equals(resourceName)) {
			SearchParameterMap params = new SearchParameterMap();
			params.setLoadSynchronousUpTo(1);
			params.add(CodeSystem.SP_URL, new UriParam(theUri));
			search = myDaoRegistry.getResourceDao(resourceName).search(params);
		} else if ("ImplementationGuide".equals(resourceName)) {
			SearchParameterMap params = new SearchParameterMap();
			params.setLoadSynchronousUpTo(1);
			params.add(ImplementationGuide.SP_URL, new UriParam(theUri));
			search = myDaoRegistry.getResourceDao("ImplementationGuide").search(params);
		} else {
			throw new IllegalArgumentException("Can't fetch resource type: " + resourceName);
		}

		Integer size = search.size();
		if (size == null || size == 0) {
			return null;
		}

		if (size > 1) {
			ourLog.warn("Found multiple {} instances with URL search value of: {}", resourceName, theUri);
		}

		return (T) search.getResources(0, 1).get(0);
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


}
