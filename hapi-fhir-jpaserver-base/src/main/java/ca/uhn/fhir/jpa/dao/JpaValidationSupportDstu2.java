package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.dstu2.resource.Questionnaire;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.UriParam;
import org.hl7.fhir.dstu2.model.IdType;
import org.hl7.fhir.dstu2.model.StructureDefinition;
import org.hl7.fhir.dstu2.model.ValueSet;
import org.hl7.fhir.dstu2.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.dstu2.model.ValueSet.ValueSetExpansionComponent;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import java.util.ArrayList;
import java.util.List;

/*
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

@Transactional(value = TxType.REQUIRED)
public class JpaValidationSupportDstu2 implements IJpaValidationSupportDstu2 {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(JpaValidationSupportDstu2.class);

	@Autowired
	@Qualifier("myFhirContextDstu2Hl7Org")
	private FhirContext myRiCtx;

	@Autowired
	@Qualifier("myStructureDefinitionDaoDstu2")
	private IFhirResourceDao<ca.uhn.fhir.model.dstu2.resource.StructureDefinition> myStructureDefinitionDao;

	@Autowired
	@Qualifier("myQuestionnaireDaoDstu2")
	private IFhirResourceDao<ca.uhn.fhir.model.dstu2.resource.Questionnaire> myQuestionnaireDao;

	@Autowired
	@Qualifier("myValueSetDaoDstu2")
	private IFhirResourceDao<ca.uhn.fhir.model.dstu2.resource.ValueSet> myValueSetDao;

	@Autowired
	@Qualifier("myFhirContextDstu2")
	private FhirContext myDstu2Ctx;

	@Override
	public List<StructureDefinition> allStructures() {
		return new ArrayList<>();
	}

	@Override
	@Transactional(value = TxType.SUPPORTS)
	public ValueSetExpansionComponent expandValueSet(FhirContext theCtx, ConceptSetComponent theInclude) {
		return null;
	}

	@Override
	@Transactional(value = TxType.SUPPORTS)
	public ValueSet fetchCodeSystem(FhirContext theCtx, String theSystem) {
		return null;
	}

	@Override
	public <T extends IBaseResource> T fetchResource(FhirContext theContext, Class<T> theClass, String theUri) {
		String resourceName = myRiCtx.getResourceDefinition(theClass).getName();
		IBundleProvider search;
		IdType uriAsId = new IdType(theUri);
		if ("ValueSet".equals(resourceName)) {
			SearchParameterMap params = new SearchParameterMap();
			params.add(ca.uhn.fhir.model.dstu2.resource.ValueSet.SP_URL, new UriParam(theUri));
			params.setLoadSynchronousUpTo(10);
			search = myValueSetDao.search(params);
		} else if ("StructureDefinition".equals(resourceName)) {
			search = myStructureDefinitionDao.search(new SearchParameterMap().setLoadSynchronous(true).add(ca.uhn.fhir.model.dstu2.resource.StructureDefinition.SP_URL, new UriParam(theUri)));
		} else if ("Questionnaire".equals(resourceName)) {
			search = myQuestionnaireDao.search(new SearchParameterMap().setLoadSynchronous(true).add(Questionnaire.SP_RES_ID, new TokenParam(null, theUri)));
		} else {
			throw new IllegalArgumentException("Can't fetch resource type: " + resourceName);
		}

		if (search.size() == 0) {
			if ("ValueSet".equals(resourceName)) {
				SearchParameterMap params = new SearchParameterMap();
				params.add(ca.uhn.fhir.model.dstu2.resource.ValueSet.SP_RES_ID, new TokenParam(null, uriAsId.toUnqualifiedVersionless().getValue()));
				params.setLoadSynchronousUpTo(10);
				search = myValueSetDao.search(params);
				if (search.size() == 0) {
					return null;
				}
			} else {
				return null;
			}
		}

		if (search.size() > 1) {
			ourLog.warn("Found multiple {} instances with URL search value of: {}", resourceName, theUri);
		}

		IBaseResource res = search.getResources(0, 1).get(0);

		/*
		 * Validator wants RI structures and not HAPI ones, so convert
		 *
		 * TODO: we really need a more efficient way of converting.. Or maybe this will just go away when we move to RI structures
		 */
		String encoded = myDstu2Ctx.newJsonParser().encodeResourceToString(res);
		return myRiCtx.newJsonParser().parseResource(theClass, encoded);
	}

	@Override
	@Transactional(value = TxType.SUPPORTS)
	public boolean isCodeSystemSupported(FhirContext theCtx, String theSystem) {
		return false;
	}

	@Override
	@Transactional(value = TxType.SUPPORTS)
	public CodeValidationResult validateCode(FhirContext theCtx, String theCodeSystem, String theCode, String theDisplay) {
		return null;
	}

}
