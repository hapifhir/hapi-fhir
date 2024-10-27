/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.term.api.ITermVersionAdapterSvc;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.util.UrlUtil;
import org.hl7.fhir.convertors.advisors.impl.BaseAdvisor_40_50;
import org.hl7.fhir.convertors.advisors.impl.BaseAdvisor_43_50;
import org.hl7.fhir.convertors.factory.VersionConvertorFactory_40_50;
import org.hl7.fhir.convertors.factory.VersionConvertorFactory_43_50;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4b.model.CodeSystem;
import org.hl7.fhir.r4b.model.ConceptMap;
import org.hl7.fhir.r4b.model.ValueSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class TermVersionAdapterSvcR4B extends BaseTermVersionAdapterSvcImpl implements ITermVersionAdapterSvc {
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
		myCodeSystemResourceDao = (IFhirResourceDao<CodeSystem>) myAppCtx.getBean("myCodeSystemDaoR4B");
		myValueSetResourceDao = (IFhirResourceDao<ValueSet>) myAppCtx.getBean("myValueSetDaoR4B");
		myConceptMapResourceDao = (IFhirResourceDao<ConceptMap>) myAppCtx.getBean("myConceptMapDaoR4B");
	}

	@Override
	public IIdType createOrUpdateCodeSystem(
			org.hl7.fhir.r4.model.CodeSystem theCodeSystemResource, RequestDetails theRequestDetails) {
		validateCodeSystemForStorage(theCodeSystemResource);

		org.hl7.fhir.r5.model.CodeSystem codeSystemR5 = (org.hl7.fhir.r5.model.CodeSystem)
				VersionConvertorFactory_40_50.convertResource(theCodeSystemResource, new BaseAdvisor_40_50(false));
		CodeSystem codeSystemR4 =
				(CodeSystem) VersionConvertorFactory_43_50.convertResource(codeSystemR5, new BaseAdvisor_43_50(false));
		if (isBlank(theCodeSystemResource.getIdElement().getIdPart())) {
			String matchUrl = "CodeSystem?url=" + UrlUtil.escapeUrlParam(theCodeSystemResource.getUrl());
			return myCodeSystemResourceDao
					.update(codeSystemR4, matchUrl, theRequestDetails)
					.getId();
		} else {
			return myCodeSystemResourceDao
					.update(codeSystemR4, theRequestDetails)
					.getId();
		}
	}

	@Override
	public void createOrUpdateConceptMap(org.hl7.fhir.r4.model.ConceptMap theConceptMap) {

		org.hl7.fhir.r5.model.ConceptMap conceptMapR5 = (org.hl7.fhir.r5.model.ConceptMap)
				VersionConvertorFactory_40_50.convertResource(theConceptMap, new BaseAdvisor_40_50(false));
		ConceptMap conceptMapR4 =
				(ConceptMap) VersionConvertorFactory_43_50.convertResource(conceptMapR5, new BaseAdvisor_43_50(false));

		if (isBlank(theConceptMap.getIdElement().getIdPart())) {
			String matchUrl = "ConceptMap?url=" + UrlUtil.escapeUrlParam(theConceptMap.getUrl());
			myConceptMapResourceDao.update(conceptMapR4, matchUrl);
		} else {
			myConceptMapResourceDao.update(conceptMapR4);
		}
	}

	@Override
	public void createOrUpdateValueSet(org.hl7.fhir.r4.model.ValueSet theValueSet) {

		org.hl7.fhir.r5.model.ValueSet valueSetR5 = (org.hl7.fhir.r5.model.ValueSet)
				VersionConvertorFactory_40_50.convertResource(theValueSet, new BaseAdvisor_40_50(false));
		ValueSet valueSetR4 =
				(ValueSet) VersionConvertorFactory_43_50.convertResource(valueSetR5, new BaseAdvisor_43_50(false));

		if (isBlank(theValueSet.getIdElement().getIdPart())) {
			String matchUrl = "ValueSet?url=" + UrlUtil.escapeUrlParam(theValueSet.getUrl());
			myValueSetResourceDao.update(valueSetR4, matchUrl);
		} else {
			myValueSetResourceDao.update(valueSetR4);
		}
	}
}
