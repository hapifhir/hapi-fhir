package ca.uhn.fhir.jpa.dao.dstu3;

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
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.jpa.dao.BaseHapiFhirSystemDao;
import ca.uhn.fhir.jpa.dao.FhirResourceDaoMessageHeaderDstu2;
import ca.uhn.fhir.jpa.dao.TransactionProcessor;
import ca.uhn.fhir.jpa.model.entity.TagDefinition;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor.ActionRequestDetails;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.Meta;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.TypedQuery;
import java.util.Collection;
import java.util.List;

public class FhirSystemDaoDstu3 extends BaseHapiFhirSystemDao<Bundle, Meta> {

	@Autowired
	private TransactionProcessor<Bundle, BundleEntryComponent> myTransactionProcessor;

	@PostConstruct
	public void start() {
		myTransactionProcessor.setDao(this);
	}

	@Override
	public Meta metaGetOperation(RequestDetails theRequestDetails) {
		// Notify interceptors
		ActionRequestDetails requestDetails = new ActionRequestDetails(theRequestDetails);
		notifyInterceptors(RestOperationTypeEnum.META, requestDetails);

		String sql = "SELECT d FROM TagDefinition d WHERE d.myId IN (SELECT DISTINCT t.myTagId FROM ResourceTag t)";
		TypedQuery<TagDefinition> q = myEntityManager.createQuery(sql, TagDefinition.class);
		List<TagDefinition> tagDefinitions = q.getResultList();

		return toMeta(tagDefinitions);
	}

	private Meta toMeta(Collection<TagDefinition> tagDefinitions) {
		Meta retVal = new Meta();
		for (TagDefinition next : tagDefinitions) {
			switch (next.getTagType()) {
				case PROFILE:
					retVal.addProfile(next.getCode());
					break;
				case SECURITY_LABEL:
					retVal.addSecurity().setSystem(next.getSystem()).setCode(next.getCode()).setDisplay(next.getDisplay());
					break;
				case TAG:
					retVal.addTag().setSystem(next.getSystem()).setCode(next.getCode()).setDisplay(next.getDisplay());
					break;
			}
		}
		return retVal;
	}

	@Override
	public IBaseBundle processMessage(RequestDetails theRequestDetails, IBaseBundle theMessage) {
		return FhirResourceDaoMessageHeaderDstu2.throwProcessMessageNotImplemented();
	}

	@Transactional(propagation = Propagation.NEVER)
	@Override
	public Bundle transaction(RequestDetails theRequestDetails, Bundle theRequest) {
		return myTransactionProcessor.transaction(theRequestDetails, theRequest);
	}



}
