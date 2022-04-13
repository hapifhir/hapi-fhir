package ca.uhn.fhir.jpa.mdm.config;

/*-
 * #%L
 * HAPI FHIR JPA Server - Master Data Management
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

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.jpa.dao.index.IJpaIdHelperService;
import ca.uhn.fhir.jpa.subscription.channel.api.ChannelProducerSettings;
import ca.uhn.fhir.jpa.subscription.channel.subscription.IChannelNamer;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.api.MdmConstants;
import ca.uhn.fhir.mdm.log.Logs;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.HapiExtensions;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Subscription;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MdmSubscriptionLoader {

	public static final String MDM_SUBSCIPRION_ID_PREFIX = "mdm-";
	private static final Logger ourLog = Logs.getMdmTroubleshootingLog();
	@Autowired
	public FhirContext myFhirContext;
	@Autowired
	public DaoRegistry myDaoRegistry;
	@Autowired
	public IJpaIdHelperService myIdHelperService;
	@Autowired
	IChannelNamer myChannelNamer;
	@Autowired
	private IMdmSettings myMdmSettings;

	private IFhirResourceDao<IBaseResource> mySubscriptionDao;

	synchronized public void daoUpdateMdmSubscriptions() {
		List<IBaseResource> subscriptions;
		List<String> mdmResourceTypes = myMdmSettings.getMdmRules().getMdmTypes();
		switch (myFhirContext.getVersion().getVersion()) {
			case DSTU3:
				subscriptions = mdmResourceTypes
					.stream()
					.map(resourceType -> buildMdmSubscriptionDstu3(MDM_SUBSCIPRION_ID_PREFIX + resourceType, resourceType + "?"))
					.collect(Collectors.toList());
				break;
			case R4:
				subscriptions = mdmResourceTypes
					.stream()
					.map(resourceType -> buildMdmSubscriptionR4(MDM_SUBSCIPRION_ID_PREFIX + resourceType, resourceType + "?"))
					.collect(Collectors.toList());
				break;
			default:
				throw new ConfigurationException(Msg.code(736) + "MDM not supported for FHIR version " + myFhirContext.getVersion().getVersion());
		}

		mySubscriptionDao = myDaoRegistry.getResourceDao("Subscription");
		for (IBaseResource subscription : subscriptions) {
			updateIfNotPresent(subscription);
		}
	}

	synchronized void updateIfNotPresent(IBaseResource theSubscription) {
		try {
			mySubscriptionDao.read(theSubscription.getIdElement(), SystemRequestDetails.forAllPartitions());
		} catch (ResourceNotFoundException | ResourceGoneException e) {
			ourLog.info("Creating subscription " + theSubscription.getIdElement());
			mySubscriptionDao.update(theSubscription, SystemRequestDetails.forAllPartitions());
		}
	}

	private org.hl7.fhir.dstu3.model.Subscription buildMdmSubscriptionDstu3(String theId, String theCriteria) {
		org.hl7.fhir.dstu3.model.Subscription retval = new org.hl7.fhir.dstu3.model.Subscription();
		retval.setId(theId);
		retval.setReason("MDM");
		retval.setStatus(org.hl7.fhir.dstu3.model.Subscription.SubscriptionStatus.REQUESTED);
		retval.setCriteria(theCriteria);
		retval.getMeta().addTag().setSystem(MdmConstants.SYSTEM_MDM_MANAGED).setCode(MdmConstants.CODE_HAPI_MDM_MANAGED);
		retval.addExtension().setUrl(HapiExtensions.EXTENSION_SUBSCRIPTION_CROSS_PARTITION).setValue(new org.hl7.fhir.dstu3.model.BooleanType().setValue(true));
		org.hl7.fhir.dstu3.model.Subscription.SubscriptionChannelComponent channel = retval.getChannel();
		channel.setType(org.hl7.fhir.dstu3.model.Subscription.SubscriptionChannelType.MESSAGE);
		channel.setEndpoint("channel:" + myChannelNamer.getChannelName(IMdmSettings.EMPI_CHANNEL_NAME, new ChannelProducerSettings()));
		channel.setPayload("application/json");
		return retval;
	}

	private Subscription buildMdmSubscriptionR4(String theId, String theCriteria) {
		Subscription retval = new Subscription();
		retval.setId(theId);
		retval.setReason("MDM");
		retval.setStatus(Subscription.SubscriptionStatus.REQUESTED);
		retval.setCriteria(theCriteria);
		retval.getMeta().addTag().setSystem(MdmConstants.SYSTEM_MDM_MANAGED).setCode(MdmConstants.CODE_HAPI_MDM_MANAGED);
		retval.addExtension().setUrl(HapiExtensions.EXTENSION_SUBSCRIPTION_CROSS_PARTITION).setValue(new BooleanType().setValue(true));
		Subscription.SubscriptionChannelComponent channel = retval.getChannel();
		channel.setType(Subscription.SubscriptionChannelType.MESSAGE);
		channel.setEndpoint("channel:" + myChannelNamer.getChannelName(IMdmSettings.EMPI_CHANNEL_NAME, new ChannelProducerSettings()));
		channel.setPayload("application/json");
		return retval;
	}
}
