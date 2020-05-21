package ca.uhn.fhir.jpa.empi.config;

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

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.api.EmpiConstants;
import ca.uhn.fhir.empi.api.IEmpiSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.searchparam.retry.Retrier;
import ca.uhn.fhir.jpa.subscription.channel.api.ChannelProducerSettings;
import ca.uhn.fhir.jpa.subscription.channel.subscription.IChannelNamer;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Semaphore;

@Service
public class EmpiSubscriptionLoader {
	private static final int MAX_RETRIES = 60; // 60 * 5 seconds = 5 minutes
	public static final String EMPI_PATIENT_SUBSCRIPTION_ID = "empi-patient";
	public static final String EMPI_PRACTITIONER_SUBSCRIPTION_ID = "empi-practitioner";
	@Autowired
	public FhirContext myFhirContext;
	@Autowired
	public DaoRegistry myDaoRegistry;
	@Autowired
	IChannelNamer myChannelNamer;
	private Semaphore myUpdateSubscriptionsSemaphore = new Semaphore(1);
	private final Object myUpdateSubscriptionsLock = new Object();
	private IFhirResourceDao<IBaseResource> mySubscriptionDao;
	private IBaseResource myPatientSub;
	private IBaseResource myPractitionerSub;

	public void daoUpdateEmpiSubscriptions() {
		mySubscriptionDao = myDaoRegistry.getResourceDao("Subscription");

		switch (myFhirContext.getVersion().getVersion()) {
			case DSTU3:
				myPatientSub = buildEmpiSubscriptionDstu3(EMPI_PATIENT_SUBSCRIPTION_ID, "Patient?");
				myPractitionerSub = buildEmpiSubscriptionDstu3(EMPI_PRACTITIONER_SUBSCRIPTION_ID, "Practitioner?");
				break;
			case R4:
				myPatientSub = buildEmpiSubscriptionR4(EMPI_PATIENT_SUBSCRIPTION_ID, "Patient?");
				myPractitionerSub = buildEmpiSubscriptionR4(EMPI_PRACTITIONER_SUBSCRIPTION_ID, "Practitioner?");
				break;
			default:
				throw new ConfigurationException("EMPI not supported for FHIR version " + myFhirContext.getVersion().getVersion());
		}

		updateSubscriptions();
	}

	private void updateSubscriptions() {
		if (!myUpdateSubscriptionsSemaphore.tryAcquire()) {
			return;
		}
		try {
			doUpdateSubscriptionsWithRetry();
		} finally {
			myUpdateSubscriptionsSemaphore.release();
		}
	}

	private Boolean doUpdateSubscriptionsWithRetry() {
		Retrier<Boolean> updateSubscriptionRetrier = new Retrier<>(this::doUpdateSubscriptions, MAX_RETRIES);
		return updateSubscriptionRetrier.runWithRetry();
	}

	private Boolean doUpdateSubscriptions() {
		synchronized (myUpdateSubscriptionsLock) {
			mySubscriptionDao.update(myPatientSub);
			mySubscriptionDao.update(myPractitionerSub);
			return true;
		}
	}

	private org.hl7.fhir.dstu3.model.Subscription buildEmpiSubscriptionDstu3(String theId, String theCriteria) {
		org.hl7.fhir.dstu3.model.Subscription retval = new org.hl7.fhir.dstu3.model.Subscription();
		retval.setId(theId);
		retval.setReason("EMPI");
		retval.setStatus(org.hl7.fhir.dstu3.model.Subscription.SubscriptionStatus.REQUESTED);
		retval.setCriteria(theCriteria);
		retval.getMeta().addTag().setSystem(EmpiConstants.SYSTEM_EMPI_MANAGED).setCode(EmpiConstants.CODE_HAPI_EMPI_MANAGED);
		org.hl7.fhir.dstu3.model.Subscription.SubscriptionChannelComponent channel = retval.getChannel();
		channel.setType(org.hl7.fhir.dstu3.model.Subscription.SubscriptionChannelType.MESSAGE);
		channel.setEndpoint("channel:" + myChannelNamer.getChannelName(IEmpiSettings.EMPI_CHANNEL_NAME, new ChannelProducerSettings()));
		channel.setPayload("application/json");
		return retval;
	}

	private Subscription buildEmpiSubscriptionR4(String theId, String theCriteria) {
		Subscription retval = new Subscription();
		retval.setId(theId);
		retval.setReason("EMPI");
		retval.setStatus(Subscription.SubscriptionStatus.REQUESTED);
		retval.setCriteria(theCriteria);
		retval.getMeta().addTag().setSystem(EmpiConstants.SYSTEM_EMPI_MANAGED).setCode(EmpiConstants.CODE_HAPI_EMPI_MANAGED);
		Subscription.SubscriptionChannelComponent channel = retval.getChannel();
		channel.setType(Subscription.SubscriptionChannelType.MESSAGE);
		channel.setEndpoint("channel:" + myChannelNamer.getChannelName(IEmpiSettings.EMPI_CHANNEL_NAME, new ChannelProducerSettings()));
		channel.setPayload("application/json");
		return retval;
	}
}
