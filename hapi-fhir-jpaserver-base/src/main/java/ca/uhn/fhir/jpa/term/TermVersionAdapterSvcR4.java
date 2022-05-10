package ca.uhn.fhir.jpa.term;

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
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.term.api.ITermVersionAdapterSvc;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.util.UrlUtil;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.ValueSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import java.security.InvalidParameterException;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.hl7.fhir.common.hapi.validation.support.ValidationConstants.LOINC_LOW;

public class TermVersionAdapterSvcR4 extends BaseTermVersionAdapterSvcImpl implements ITermVersionAdapterSvc {
	private IFhirResourceDao<ConceptMap> myConceptMapResourceDao;
	private IFhirResourceDao<CodeSystem> myCodeSystemResourceDao;
	private IFhirResourceDao<ValueSet> myValueSetResourceDao;

	@Autowired
	private ApplicationContext myAppCtx;

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
		myCodeSystemResourceDao = (IFhirResourceDao<CodeSystem>) myAppCtx.getBean("myCodeSystemDaoR4");
		myValueSetResourceDao = (IFhirResourceDao<ValueSet>) myAppCtx.getBean("myValueSetDaoR4");
		myConceptMapResourceDao = (IFhirResourceDao<ConceptMap>) myAppCtx.getBean("myConceptMapDaoR4");
	}

	@Override
	public IIdType createOrUpdateCodeSystem(org.hl7.fhir.r4.model.CodeSystem theCodeSystemResource, RequestDetails theRequestDetails) {
		validateCodeSystemForStorage(theCodeSystemResource);
		if (isBlank(theCodeSystemResource.getIdElement().getIdPart())) {
			if (theCodeSystemResource.getUrl().contains(LOINC_LOW)) {
				throw new InvalidParameterException(Msg.code(859) + "'loinc' CodeSystem must have an 'ID' element");
			}
			String matchUrl = "CodeSystem?url=" + UrlUtil.escapeUrlParam(theCodeSystemResource.getUrl());
			return myCodeSystemResourceDao.update(theCodeSystemResource, matchUrl, theRequestDetails).getId();
		} else {
			return myCodeSystemResourceDao.update(theCodeSystemResource, theRequestDetails).getId();
		}
	}

	@Override
	public void createOrUpdateConceptMap(org.hl7.fhir.r4.model.ConceptMap theConceptMap) {
		if (isBlank(theConceptMap.getIdElement().getIdPart())) {
			String matchUrl = "ConceptMap?url=" + UrlUtil.escapeUrlParam(theConceptMap.getUrl());
			myConceptMapResourceDao.update(theConceptMap, matchUrl);
		} else {
			myConceptMapResourceDao.update(theConceptMap);
		}
	}

	@Override
	public void createOrUpdateValueSet(org.hl7.fhir.r4.model.ValueSet theValueSet) {
		if (isBlank(theValueSet.getIdElement().getIdPart())) {
			String matchUrl = "ValueSet?url=" + UrlUtil.escapeUrlParam(theValueSet.getUrl());
			myValueSetResourceDao.update(theValueSet, matchUrl);
		} else {
			myValueSetResourceDao.update(theValueSet);
		}
	}

}
