package ca.uhn.fhir.jpa.empi.broker;

/*-
 * #%L
 * HAPI FHIR JPA Server - Enterprise Master Patient Index
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

import ca.uhn.fhir.empi.api.Constants;
import ca.uhn.fhir.empi.api.IEmpiProperties;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.subscription.channel.subscription.IChannelNamer;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmpiSubscriptionLoader {
	@Autowired
	public DaoRegistry myDaoRegistry;
	@Autowired
	IChannelNamer myChannelNamer;

	public void daoUpdateEmpiSubscriptions() {
		IBaseResource patientSub = buildEmpiSubscription("empi-patient", "Patient?");
		IBaseResource practitionerSub = buildEmpiSubscription("empi-practitioner", "Practitioner?");
		IFhirResourceDao<IBaseResource> subscriptionDao = myDaoRegistry.getResourceDao("Subscription");
		subscriptionDao.update(patientSub);
		subscriptionDao.update(practitionerSub);
	}

	private Subscription buildEmpiSubscription(String theId, String theCriteria) {
		Subscription retval = new Subscription();
		retval.setId(theId);
		retval.setReason("EMPI");
		retval.setStatus(Subscription.SubscriptionStatus.REQUESTED);
		retval.setCriteria(theCriteria);
		retval.getMeta().addTag().setSystem(ca.uhn.fhir.empi.api.Constants.SYSTEM_EMPI_MANAGED).setCode(Constants.CODE_HAPI_EMPI_MANAGED);
		Subscription.SubscriptionChannelComponent channel = retval.getChannel();
		channel.setType(Subscription.SubscriptionChannelType.MESSAGE);
		channel.setEndpoint("jms:queue:" + myChannelNamer.getChannelName(IEmpiProperties.EMPI_MATCHING_CHANNEL_NAME));
		channel.setPayload("application/json");
		return retval;
	}
}
