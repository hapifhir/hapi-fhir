package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IContextValidationSupport;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.dstu2.resource.Questionnaire;
import ca.uhn.fhir.model.dstu2.resource.StructureDefinition;
import ca.uhn.fhir.model.dstu2.resource.ValueSet;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.UriParam;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.dstu2.model.IdType;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

/*
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

@Transactional(value = TxType.REQUIRED)
public class JpaValidationSupportDstu2 implements IJpaValidationSupportDstu2 {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(JpaValidationSupportDstu2.class);

	@Autowired
	@Qualifier("myStructureDefinitionDaoDstu2")
	private IFhirResourceDao<StructureDefinition> myStructureDefinitionDao;

	@Autowired
	@Qualifier("myQuestionnaireDaoDstu2")
	private IFhirResourceDao<Questionnaire> myQuestionnaireDao;

	@Autowired
	@Qualifier("myValueSetDaoDstu2")
	private IFhirResourceDao<ValueSet> myValueSetDao;

	private final FhirContext myDstu2Ctx;

	/**
	 * Constructor
	 */
	public JpaValidationSupportDstu2(FhirContext theDstu2Ctx) {
		Validate.notNull(theDstu2Ctx);
		myDstu2Ctx = theDstu2Ctx;
	}

	@Override
	public <T extends IBaseResource> T fetchResource(Class<T> theClass, String theUri) {
		String resourceName = myDstu2Ctx.getResourceDefinition(theClass).getName();
		IBundleProvider search;
		IdType uriAsId = new IdType(theUri);
		if ("ValueSet".equals(resourceName)) {
			SearchParameterMap params = new SearchParameterMap();
			params.add(ValueSet.SP_URL, new UriParam(theUri));
			params.setLoadSynchronousUpTo(10);
			search = myValueSetDao.search(params);
		} else if ("StructureDefinition".equals(resourceName)) {
			search = myStructureDefinitionDao.search(new SearchParameterMap().setLoadSynchronous(true).add(StructureDefinition.SP_URL, new UriParam(theUri)));
		} else if ("Questionnaire".equals(resourceName)) {
			search = myQuestionnaireDao.search(new SearchParameterMap().setLoadSynchronous(true).add(Questionnaire.SP_RES_ID, new TokenParam(null, theUri)));
		} else {
			throw new IllegalArgumentException("Can't fetch resource type: " + resourceName);
		}

		if (search.size() == 0) {
			if ("ValueSet".equals(resourceName)) {
				SearchParameterMap params = new SearchParameterMap();
				params.add(ValueSet.SP_RES_ID, new TokenParam(null, uriAsId.toUnqualifiedVersionless().getValue()));
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

		return (T) search.getResources(0, 1).get(0);
	}

	@Override
	@Transactional(value = TxType.SUPPORTS)
	public boolean isCodeSystemSupported(IContextValidationSupport theRootValidationSupport, String theSystem) {
		return false;
	}

	@Override
	public FhirContext getFhirContext() {
		return myDstu2Ctx;
	}

}
