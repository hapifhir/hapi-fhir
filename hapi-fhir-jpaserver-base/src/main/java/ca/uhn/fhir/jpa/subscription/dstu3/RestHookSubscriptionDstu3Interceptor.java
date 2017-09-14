package ca.uhn.fhir.jpa.subscription.dstu3;

/*-
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

import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.subscription.BaseSubscriptionRestHookInterceptor;
import ca.uhn.fhir.jpa.subscription.CanonicalSubscription;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hl7.fhir.dstu3.model.Subscription;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Arrays;

public class RestHookSubscriptionDstu3Interceptor extends BaseSubscriptionRestHookInterceptor {
	@Autowired
	@Qualifier("mySubscriptionDaoDstu3")
	private IFhirResourceDao<Subscription> mySubscriptionDao;

	@Override
	protected IFhirResourceDao<?> getSubscriptionDao() {
		return mySubscriptionDao;
	}

	public void setSubscriptionDao(IFhirResourceDao<Subscription> theSubscriptionDao) {
		mySubscriptionDao = theSubscriptionDao;
	}

	public org.hl7.fhir.r4.model.Subscription.SubscriptionChannelType getChannelType() {
		return org.hl7.fhir.r4.model.Subscription.SubscriptionChannelType.RESTHOOK;
	}

	@Override
	protected CanonicalSubscription canonicalize(IBaseResource theSubscription) {
		return doCanonicalize(theSubscription);
	}

	static CanonicalSubscription doCanonicalize(IBaseResource theSubscription) {
		Subscription subscription = (Subscription) theSubscription;

		CanonicalSubscription retVal = new CanonicalSubscription();
		try {
			retVal.setStatus(org.hl7.fhir.r4.model.Subscription.SubscriptionStatus.fromCode(subscription.getStatus().toCode()));
			retVal.setBackingSubscription(theSubscription);
			retVal.setChannelType(org.hl7.fhir.r4.model.Subscription.SubscriptionChannelType.fromCode(subscription.getChannel().getType().toCode()));
			retVal.setCriteriaString(subscription.getCriteria());
			retVal.setEndpointUrl(subscription.getChannel().getEndpoint());
			retVal.setHeaders(subscription.getChannel().getHeader());
			retVal.setIdElement(subscription.getIdElement());
			retVal.setPayloadString(subscription.getChannel().getPayload());
		} catch (FHIRException theE) {
			throw new InternalErrorException(theE);
		}
		return retVal;
	}



}
