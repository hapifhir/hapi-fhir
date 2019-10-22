package ca.uhn.fhir.jpa.dao.r4;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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
import ca.uhn.fhir.jpa.dao.DaoRegistry;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.UriParam;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

public abstract class BaseJpaValidationSupport {

	private static final Logger ourLog = LoggerFactory.getLogger(BaseJpaValidationSupport.class);

	@Autowired
	private FhirContext myR4Ctx;
	@Autowired
	private DaoRegistry myDaoRegistry;
	private IFhirResourceDao<?> myStructureDefinitionDao;
	private IFhirResourceDao<?> myValueSetDao;
	private IFhirResourceDao<?> myQuestionnaireDao;
	private IFhirResourceDao<?> myCodeSystemDao;
	private IFhirResourceDao<?> myImplementationGuideDao;

	@SuppressWarnings({"unchecked", "unused"})
	public <T extends IBaseResource> T fetchResource(FhirContext theContext, Class<T> theClass, String theUri) {
		IdType id = new IdType(theUri);
		boolean localReference = false;
		if (id.hasBaseUrl() == false && id.hasIdPart() == true) {
			localReference = true;
		}

		String resourceName = myR4Ctx.getResourceDefinition(theClass).getName();
		IBundleProvider search;
		if ("ValueSet".equals(resourceName)) {
			if (localReference) {
				SearchParameterMap params = new SearchParameterMap();
				params.setLoadSynchronousUpTo(1);
				params.add(IAnyResource.SP_RES_ID, new StringParam(theUri));
				search = myValueSetDao.search(params);
				if (search.size() == 0) {
					params = new SearchParameterMap();
					params.setLoadSynchronousUpTo(1);
					params.add(ValueSet.SP_URL, new UriParam(theUri));
					search = myValueSetDao.search(params);
				}
			} else {
				SearchParameterMap params = new SearchParameterMap();
				params.setLoadSynchronousUpTo(1);
				params.add(ValueSet.SP_URL, new UriParam(theUri));
				search = myValueSetDao.search(params);
			}
		} else if ("StructureDefinition".equals(resourceName)) {
			// Don't allow the core FHIR definitions to be overwritten
			if (theUri.startsWith("http://hl7.org/fhir/StructureDefinition/")) {
				String typeName = theUri.substring("http://hl7.org/fhir/StructureDefinition/".length());
				if (myR4Ctx.getElementDefinition(typeName) != null) {
					return null;
				}
			}
			SearchParameterMap params = new SearchParameterMap();
			params.setLoadSynchronousUpTo(1);
			params.add(StructureDefinition.SP_URL, new UriParam(theUri));
			search = myStructureDefinitionDao.search(params);
		} else if ("Questionnaire".equals(resourceName)) {
			SearchParameterMap params = new SearchParameterMap();
			params.setLoadSynchronousUpTo(1);
			if (localReference) {
				params.add(IAnyResource.SP_RES_ID, new StringParam(id.getIdPart()));
			} else {
				params.add(Questionnaire.SP_URL, new UriParam(id.getValue()));
			}
			search = myQuestionnaireDao.search(params);
		} else if ("CodeSystem".equals(resourceName)) {
			SearchParameterMap params = new SearchParameterMap();
			params.setLoadSynchronousUpTo(1);
			params.add(CodeSystem.SP_URL, new UriParam(theUri));
			search = myCodeSystemDao.search(params);
		} else if ("ImplementationGuide".equals(resourceName)) {
			SearchParameterMap params = new SearchParameterMap();
			params.setLoadSynchronousUpTo(1);
			params.add(ImplementationGuide.SP_URL, new UriParam(theUri));
			search = myImplementationGuideDao.search(params);
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

	@PostConstruct
	public void start() {
		myStructureDefinitionDao = myDaoRegistry.getResourceDao("StructureDefinition");
		myValueSetDao = myDaoRegistry.getResourceDao("ValueSet");
		myQuestionnaireDao = myDaoRegistry.getResourceDao("Questionnaire");
		myCodeSystemDao = myDaoRegistry.getResourceDao("CodeSystem");
		myImplementationGuideDao = myDaoRegistry.getResourceDao("ImplementationGuide");
	}


}
