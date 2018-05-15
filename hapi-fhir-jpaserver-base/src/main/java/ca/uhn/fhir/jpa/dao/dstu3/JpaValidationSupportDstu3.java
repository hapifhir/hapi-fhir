package ca.uhn.fhir.jpa.dao.dstu3;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.UriParam;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.dstu3.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.dstu3.model.ValueSet.ValueSetExpansionComponent;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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
public class JpaValidationSupportDstu3 implements IJpaValidationSupportDstu3, ApplicationContextAware {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(JpaValidationSupportDstu3.class);

	private IFhirResourceDao<StructureDefinition> myStructureDefinitionDao;
	private IFhirResourceDao<ValueSet> myValueSetDao;
	private IFhirResourceDao<Questionnaire> myQuestionnaireDao;
	private IFhirResourceDao<CodeSystem> myCodeSystemDao;
	@Autowired
	private FhirContext myDstu3Ctx;
	private ApplicationContext myApplicationContext;

	/**
	 * Constructor
	 */
	public JpaValidationSupportDstu3() {
		super();
	}

	@Override
	@Transactional(value = TxType.SUPPORTS)
	public ValueSetExpansionComponent expandValueSet(FhirContext theCtx, ConceptSetComponent theInclude) {
		return null;
	}

	@Override
	public List<IBaseResource> fetchAllConformanceResources(FhirContext theContext) {
		return null;
	}

	@Override
	@Transactional(value = TxType.SUPPORTS)
	public List<StructureDefinition> fetchAllStructureDefinitions(FhirContext theContext) {
		return Collections.emptyList();
	}

	@Override
	public CodeSystem fetchCodeSystem(FhirContext theCtx, String theSystem) {
		if (isBlank(theSystem)) {
			return null;
		}
		return fetchResource(theCtx, CodeSystem.class, theSystem);
	}

	@SuppressWarnings("unchecked")
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
	public StructureDefinition fetchStructureDefinition(FhirContext theCtx, String theUrl) {
		return fetchResource(theCtx, StructureDefinition.class, theUrl);
	}

	@Override
	@Transactional(value = TxType.SUPPORTS)
	public boolean isCodeSystemSupported(FhirContext theCtx, String theSystem) {
		return false;
	}

	@Override
	public void setApplicationContext(ApplicationContext theApplicationContext) throws BeansException {
		myApplicationContext = theApplicationContext;
	}

	@PostConstruct
	public void start() {
		myStructureDefinitionDao = myApplicationContext.getBean("myStructureDefinitionDaoDstu3", IFhirResourceDao.class);
		myValueSetDao = myApplicationContext.getBean("myValueSetDaoDstu3", IFhirResourceDao.class);
		myQuestionnaireDao = myApplicationContext.getBean("myQuestionnaireDaoDstu3", IFhirResourceDao.class);
		myCodeSystemDao = myApplicationContext.getBean("myCodeSystemDaoDstu3", IFhirResourceDao.class);
	}

	@Override
	@Transactional(value = TxType.SUPPORTS)
	public CodeValidationResult validateCode(FhirContext theCtx, String theCodeSystem, String theCode, String theDisplay) {
		return null;
	}

}
