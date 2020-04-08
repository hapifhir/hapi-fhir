package ca.uhn.fhir.jpa.term;

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

import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.term.api.ITermVersionAdapterSvc;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.UrlUtil;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.ConceptMap;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.hl7.fhir.convertors.conv30_40.CodeSystem30_40.convertCodeSystem;
import static org.hl7.fhir.convertors.conv30_40.ConceptMap30_40.convertConceptMap;
import static org.hl7.fhir.convertors.conv30_40.ValueSet30_40.convertValueSet;

public class TermVersionAdapterSvcDstu3 extends BaseTermVersionAdapterSvcImpl implements ITermVersionAdapterSvc {

	private IFhirResourceDao<ConceptMap> myConceptMapResourceDao;
	private IFhirResourceDao<CodeSystem> myCodeSystemResourceDao;
	private IFhirResourceDao<ValueSet> myValueSetResourceDao;

	@Autowired
	private ApplicationContext myAppCtx;

	public TermVersionAdapterSvcDstu3() {
		super();
	}


	/**
	 * Initialize the beans that are used by this service.
	 *
	 * Note: There is a circular dependency here where the CodeSystem DAO
	 * needs terminology services, and the term services need the CodeSystem DAO.
	 * So we look these up in a refresh event instead of just autowiring them
	 * in order to avoid weird circular reference errors.
	 */
	@SuppressWarnings({"unchecked", "unused"})
	@EventListener
	public void start(ContextRefreshedEvent theEvent) {
		myCodeSystemResourceDao = (IFhirResourceDao<CodeSystem>) myAppCtx.getBean("myCodeSystemDaoDstu3");
		myValueSetResourceDao = (IFhirResourceDao<ValueSet>) myAppCtx.getBean("myValueSetDaoDstu3");
		myConceptMapResourceDao = (IFhirResourceDao<ConceptMap>) myAppCtx.getBean("myConceptMapDaoDstu3");
	}

		@Override
	public IIdType createOrUpdateCodeSystem(org.hl7.fhir.r4.model.CodeSystem theCodeSystemResource) {
		CodeSystem resourceToStore;
		try {
			resourceToStore = convertCodeSystem(theCodeSystemResource);
		} catch (FHIRException e) {
			throw new InternalErrorException(e);
		}
		validateCodeSystemForStorage(theCodeSystemResource);
		if (isBlank(resourceToStore.getIdElement().getIdPart())) {
			String matchUrl = "CodeSystem?url=" + UrlUtil.escapeUrlParam(theCodeSystemResource.getUrl());
			return myCodeSystemResourceDao.update(resourceToStore, matchUrl).getId();
		} else {
			return myCodeSystemResourceDao.update(resourceToStore).getId();
		}
	}

	@Override
	public void createOrUpdateConceptMap(org.hl7.fhir.r4.model.ConceptMap theConceptMap) {
		ConceptMap resourceToStore;
		try {
			resourceToStore = convertConceptMap(theConceptMap);
		} catch (FHIRException e) {
			throw new InternalErrorException(e);
		}
		if (isBlank(resourceToStore.getIdElement().getIdPart())) {
			String matchUrl = "ConceptMap?url=" + UrlUtil.escapeUrlParam(theConceptMap.getUrl());
			myConceptMapResourceDao.update(resourceToStore, matchUrl);
		} else {
			myConceptMapResourceDao.update(resourceToStore);
		}
	}

	@Override
	public void createOrUpdateValueSet(org.hl7.fhir.r4.model.ValueSet theValueSet) {
		ValueSet valueSetDstu3;
		try {
			valueSetDstu3 = convertValueSet(theValueSet);
		} catch (FHIRException e) {
			throw new InternalErrorException(e);
		}

		if (isBlank(valueSetDstu3.getIdElement().getIdPart())) {
			String matchUrl = "ValueSet?url=" + UrlUtil.escapeUrlParam(theValueSet.getUrl());
			myValueSetResourceDao.update(valueSetDstu3, matchUrl);
		} else {
			myValueSetResourceDao.update(valueSetDstu3);
		}
	}

}
