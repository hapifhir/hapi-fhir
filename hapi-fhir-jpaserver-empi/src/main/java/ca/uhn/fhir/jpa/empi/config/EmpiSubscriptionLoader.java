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
import ca.uhn.fhir.empi.log.Logs;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.subscription.channel.api.ChannelProducerSettings;
import ca.uhn.fhir.jpa.subscription.channel.subscription.IChannelNamer;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Subscription;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmpiSubscriptionLoader {
	private static final Logger ourLog = Logs.getEmpiTroubleshootingLog();

	public static final String EMPI_PATIENT_SUBSCRIPTION_ID = "empi-patient";
	public static final String EMPI_PRACTITIONER_SUBSCRIPTION_ID = "empi-practitioner";
	@Autowired
	public FhirContext myFhirContext;
	@Autowired
	public DaoRegistry myDaoRegistry;
	@Autowired
	public IdHelperService myIdHelperService;
	@Autowired
	IChannelNamer myChannelNamer;
	private IFhirResourceDao<IBaseResource> mySubscriptionDao;

	synchronized public void daoUpdateEmpiSubscriptions() {
		IBaseResource patientSub;
		IBaseResource practitionerSub;
		switch (myFhirContext.getVersion().getVersion()) {
			case DSTU3:
				patientSub = buildEmpiSubscriptionDstu3(EMPI_PATIENT_SUBSCRIPTION_ID, "Patient?");
				practitionerSub = buildEmpiSubscriptionDstu3(EMPI_PRACTITIONER_SUBSCRIPTION_ID, "Practitioner?");
				break;
			case R4:
				patientSub = buildEmpiSubscriptionR4(EMPI_PATIENT_SUBSCRIPTION_ID, "Patient?");
				practitionerSub = buildEmpiSubscriptionR4(EMPI_PRACTITIONER_SUBSCRIPTION_ID, "Practitioner?");
				break;
			default:
				throw new ConfigurationException("EMPI not supported for FHIR version " + myFhirContext.getVersion().getVersion());
		}

		mySubscriptionDao = myDaoRegistry.getResourceDao("Subscription");
		updateIfNotPresent(patientSub);
		updateIfNotPresent(practitionerSub);
	}

	private synchronized void updateIfNotPresent(IBaseResource theSubscription) {
		try {
			mySubscriptionDao.read(theSubscription.getIdElement());
		} catch (ResourceNotFoundException e) {
			ourLog.info("Creating subsription " + theSubscription.getIdElement());
			mySubscriptionDao.update(theSubscription);
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
