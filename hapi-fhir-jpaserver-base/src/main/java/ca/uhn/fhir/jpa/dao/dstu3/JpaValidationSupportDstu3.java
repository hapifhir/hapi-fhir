package ca.uhn.fhir.jpa.dao.dstu3;

import java.util.Collections;
import java.util.List;

import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Questionnaire;
import org.hl7.fhir.dstu3.model.StructureDefinition;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.dstu3.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.dstu3.model.ValueSet.ValueSetExpansionComponent;
import org.hl7.fhir.instance.model.api.IAnyResource;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.SearchParameterMap;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.server.IBundleProvider;

public class JpaValidationSupportDstu3 implements IJpaValidationSupportDstu3 {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(JpaValidationSupportDstu3.class);

	@Autowired
	@Qualifier("myStructureDefinitionDaoDstu3")
	private IFhirResourceDao<StructureDefinition> myStructureDefinitionDao;

	@Autowired
	@Qualifier("myValueSetDaoDstu3")
	private IFhirResourceDao<ValueSet> myValueSetDao;

	@Autowired
	@Qualifier("myQuestionnaireDaoDstu3")
	private IFhirResourceDao<Questionnaire> myQuestionnaireDao;

	@Autowired
	@Qualifier("myCodeSystemDaoDstu3")
	private IFhirResourceDao<CodeSystem> myCodeSystemDao;

	@Autowired
	private FhirContext myDstu3Ctx;

	public JpaValidationSupportDstu3() {
		super();
	}
	
	
	@Override
	public ValueSetExpansionComponent expandValueSet(FhirContext theCtx, ConceptSetComponent theInclude) {
		return null;
	}

	@Override
	public CodeSystem fetchCodeSystem(FhirContext theCtx, String theSystem) {
		return fetchResource(theCtx, CodeSystem.class, theSystem);
	}

	@Override
	public <T extends IBaseResource> T fetchResource(FhirContext theContext, Class<T> theClass, String theUri) {
		IdType id = new IdType(theUri);
		boolean localReference = false;
		if (id.hasBaseUrl() == false && id.hasIdPart() == true) {
			localReference = true;
		}

		String resourceName = myDstu3Ctx.getResourceDefinition(theClass).getName();
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
			if (theUri.startsWith("http://hl7.org/fhir/StructureDefinition/")) {
				return null;
			}
			SearchParameterMap params = new SearchParameterMap();
			params.setLoadSynchronousUpTo(1);
			params.add(StructureDefinition.SP_URL, new UriParam(theUri));
			search = myStructureDefinitionDao.search(params);
		} else if ("Questionnaire".equals(resourceName)) {
			SearchParameterMap params = new SearchParameterMap();
			params.setLoadSynchronousUpTo(1);
			params.add(IAnyResource.SP_RES_ID, new StringParam(id.getIdPart()));
			search = myQuestionnaireDao.search(params);
		} else if ("CodeSystem".equals(resourceName)) {
			SearchParameterMap params = new SearchParameterMap();
			params.setLoadSynchronousUpTo(1);
			params.add(CodeSystem.SP_URL, new UriParam(theUri));
			search = myCodeSystemDao.search(params);
		} else {
			throw new IllegalArgumentException("Can't fetch resource type: " + resourceName);
		}

		if (search.size() == 0) {
			return null;
		}

		if (search.size() > 1) {
			ourLog.warn("Found multiple {} instances with URL search value of: {}", resourceName, theUri);
		}

		return (T) search.getResources(0, 1).get(0);
	}

	@Override
	public boolean isCodeSystemSupported(FhirContext theCtx, String theSystem) {
		return false;
	}

	@Override
	public CodeValidationResult validateCode(FhirContext theCtx, String theCodeSystem, String theCode, String theDisplay) {
		return null;
	}


	@Override
	public StructureDefinition fetchStructureDefinition(FhirContext theCtx, String theUrl) {
		return fetchResource(theCtx, StructureDefinition.class, theUrl);
	}


	@Override
	public List<StructureDefinition> fetchAllStructureDefinitions(FhirContext theContext) {
		return Collections.emptyList();
	}

}
