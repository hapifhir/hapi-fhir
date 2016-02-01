package ca.uhn.fhir.jpa.dao.dstu3;

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
 * Copyright (C) 2014 - 2016 University Health Network
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
	@Qualifier("myFhirContextDstu3")
	private FhirContext myDstu3Ctx;

	public JpaValidationSupportDstu3() {
		super();
	}
	
	
	@Override
	public ValueSetExpansionComponent expandValueSet(FhirContext theCtx, ConceptSetComponent theInclude) {
		return null;
	}

	@Override
	public ValueSet fetchCodeSystem(FhirContext theCtx, String theSystem) {
		return null;
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
				search = myValueSetDao.search(IAnyResource.SP_RES_ID, new StringParam(theUri));
			} else {
				search = myValueSetDao.search(ValueSet.SP_URL, new UriParam(theUri));
			}
		} else if ("StructureDefinition".equals(resourceName)) {
			search = myStructureDefinitionDao.search(StructureDefinition.SP_URL, new UriParam(theUri));
		} else if ("Questionnaire".equals(resourceName)) {
			search = myQuestionnaireDao.search(IAnyResource.SP_RES_ID, new StringParam(id.getIdPart()));
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

}
