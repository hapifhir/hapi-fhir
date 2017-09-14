package ca.uhn.fhir.jpa.subscription.r4;

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
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.EventDefinition;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.Subscription;
import org.hl7.fhir.r4.model.TriggerDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

public class RestHookSubscriptionR4Interceptor extends BaseSubscriptionRestHookInterceptor {
	@Autowired
	@Qualifier("mySubscriptionDaoR4")
	private IFhirResourceDao<org.hl7.fhir.r4.model.Subscription> mySubscriptionDao;

	@Autowired
	@Qualifier("myEventDefinitionDaoR4")
	private IFhirResourceDao<org.hl7.fhir.r4.model.EventDefinition> myEventDefinitionDao;

	public org.hl7.fhir.r4.model.Subscription.SubscriptionChannelType getChannelType() {
		return org.hl7.fhir.r4.model.Subscription.SubscriptionChannelType.RESTHOOK;
	}

	@Override
	protected IFhirResourceDao<?> getSubscriptionDao() {
		return mySubscriptionDao;
	}

	public void setSubscriptionDao(IFhirResourceDao<org.hl7.fhir.r4.model.Subscription> theSubscriptionDao) {
		mySubscriptionDao = theSubscriptionDao;
	}

	@Override
	protected CanonicalSubscription canonicalize(IBaseResource theSubscription) {
		return doCanonicalize(theSubscription, myEventDefinitionDao);
	}

	static CanonicalSubscription doCanonicalize(IBaseResource theSubscription, IFhirResourceDao<org.hl7.fhir.r4.model.EventDefinition> theEventDefinitionDao) {
		Subscription subscription = (Subscription) theSubscription;

		CanonicalSubscription retVal = new CanonicalSubscription();
		retVal.setStatus(subscription.getStatus());
		retVal.setBackingSubscription(theSubscription);
		retVal.setChannelType(subscription.getChannel().getType());
		retVal.setCriteriaString(subscription.getCriteria());
		retVal.setEndpointUrl(subscription.getChannel().getEndpoint());
		retVal.setHeaders(subscription.getChannel().getHeader());
		retVal.setIdElement(subscription.getIdElement());
		retVal.setPayloadString(subscription.getChannel().getPayload());

		List<Extension> topicExts = subscription.getExtensionsByUrl("http://hl7.org/fhir/subscription/topics");
		if (topicExts.size() > 0) {
			IBaseReference ref = (IBaseReference) topicExts.get(0).getValueAsPrimitive();
			if (!"EventDefinition".equals(ref.getReferenceElement().getResourceType())) {
				throw new PreconditionFailedException("Topic reference must be an EventDefinition");
			}

			EventDefinition def = theEventDefinitionDao.read(ref.getReferenceElement());
			retVal.addTrigger(def.getTrigger());
		}

		return retVal;
	}



}
