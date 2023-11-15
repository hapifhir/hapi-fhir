/*-
 * #%L
 * HAPI FHIR JPA Server - Master Data Management
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.mdm.config;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.subscription.channel.api.ChannelProducerSettings;
import ca.uhn.fhir.jpa.subscription.channel.subscription.IChannelNamer;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionLoader;
import ca.uhn.fhir.jpa.topic.SubscriptionTopicLoader;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.api.MdmConstants;
import ca.uhn.fhir.mdm.log.Logs;
import ca.uhn.fhir.model.dstu2.valueset.SubscriptionChannelTypeEnum;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.HapiExtensions;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Subscription;
import org.hl7.fhir.r5.model.CanonicalType;
import org.hl7.fhir.r5.model.Coding;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.SubscriptionTopic;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static ca.uhn.fhir.subscription.SubscriptionConstants.SUBSCRIPTION_CHANNEL_TYPE_SYSTEM_R5;

@Service
public class MdmSubscriptionLoader {

	public static final String MDM_SUBSCIPRION_ID_PREFIX = "mdm-";
	private static final Logger ourLog = Logs.getMdmTroubleshootingLog();

	@Autowired
	public FhirContext myFhirContext;

	@Autowired
	public DaoRegistry myDaoRegistry;

	@Autowired
	IChannelNamer myChannelNamer;

	@Autowired
	private SubscriptionLoader mySubscriptionLoader;

	@Autowired
	private IMdmSettings myMdmSettings;

	@Autowired
	private SubscriptionTopicLoader mySubscriptionTopicLoader;

	private IFhirResourceDao<IBaseResource> mySubscriptionDao;
	private IFhirResourceDao<SubscriptionTopic> mySubscriptionTopicDao;

	public synchronized void daoUpdateMdmSubscriptions() {
		List<IBaseResource> subscriptions;
		List<String> mdmResourceTypes = myMdmSettings.getMdmRules().getMdmTypes();
		switch (myFhirContext.getVersion().getVersion()) {
			case DSTU3:
				subscriptions = mdmResourceTypes.stream()
						.map(resourceType ->
								buildMdmSubscriptionDstu3(MDM_SUBSCIPRION_ID_PREFIX + resourceType, resourceType + "?"))
						.collect(Collectors.toList());
				break;
			case R4:
				subscriptions = mdmResourceTypes.stream()
						.map(resourceType ->
								buildMdmSubscriptionR4(MDM_SUBSCIPRION_ID_PREFIX + resourceType, resourceType + "?"))
						.collect(Collectors.toList());
				break;
			case R5:
				SubscriptionTopic subscriptionTopic = buildMdmSubscriptionTopicR5(mdmResourceTypes);
				mySubscriptionTopicDao = myDaoRegistry.getResourceDao("SubscriptionTopic");
				updateIfNotPresent(subscriptionTopic);
				// After loading subscriptionTopic, sync subscriptionTopic to the registry.
				mySubscriptionTopicLoader.syncDatabaseToCache();

				org.hl7.fhir.r5.model.Subscription subscriptionR5 = buildMdmSubscriptionR5(subscriptionTopic);
				subscriptions = Collections.singletonList(subscriptionR5);
				break;
			default:
				throw new ConfigurationException(Msg.code(736) + "MDM not supported for FHIR version "
						+ myFhirContext.getVersion().getVersion());
		}

		mySubscriptionDao = myDaoRegistry.getResourceDao("Subscription");
		for (IBaseResource subscription : subscriptions) {
			updateIfNotPresent(subscription);
		}
		// After loading all the subscriptions, sync the subscriptions to the registry.
		if (subscriptions != null && subscriptions.size() > 0) {
			mySubscriptionLoader.syncDatabaseToCache();
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

	synchronized void updateIfNotPresent(SubscriptionTopic theSubscriptionTopic) {
		try {
			// TODO: Cross-partition matching is currently only supported for DSTU3 and R4 (i.e. non-topic-based)
			// Subscriptions - how does it affect MDM ?
			// TODO: https://smilecdr.com/docs/subscription/topic.html - should we add (later) Cross-partition matching
			// support to make MDM with R5 work correctly ?
			mySubscriptionTopicDao.read(theSubscriptionTopic.getIdElement(), SystemRequestDetails.forAllPartitions());
		} catch (ResourceNotFoundException | ResourceGoneException e) {
			ourLog.info("Creating subscriptionTopic " + theSubscriptionTopic.getIdElement());
			mySubscriptionTopicDao.update(theSubscriptionTopic, SystemRequestDetails.forAllPartitions());
		}
	}

	private org.hl7.fhir.dstu3.model.Subscription buildMdmSubscriptionDstu3(String theId, String theCriteria) {
		org.hl7.fhir.dstu3.model.Subscription retval = new org.hl7.fhir.dstu3.model.Subscription();
		retval.setId(theId);
		retval.setReason("MDM");
		retval.setStatus(org.hl7.fhir.dstu3.model.Subscription.SubscriptionStatus.REQUESTED);
		retval.setCriteria(theCriteria);
		retval.getMeta()
				.addTag()
				.setSystem(MdmConstants.SYSTEM_MDM_MANAGED)
				.setCode(MdmConstants.CODE_HAPI_MDM_MANAGED);
		retval.addExtension()
				.setUrl(HapiExtensions.EXTENSION_SUBSCRIPTION_CROSS_PARTITION)
				.setValue(new org.hl7.fhir.dstu3.model.BooleanType().setValue(true));
		org.hl7.fhir.dstu3.model.Subscription.SubscriptionChannelComponent channel = retval.getChannel();
		channel.setType(org.hl7.fhir.dstu3.model.Subscription.SubscriptionChannelType.MESSAGE);
		channel.setEndpoint("channel:"
				+ myChannelNamer.getChannelName(IMdmSettings.EMPI_CHANNEL_NAME, new ChannelProducerSettings()));
		channel.setPayload("application/json");
		return retval;
	}

	private Subscription buildMdmSubscriptionR4(String theId, String theCriteria) {
		Subscription retval = new Subscription();
		retval.setId(theId);
		retval.setReason("MDM");
		retval.setStatus(Subscription.SubscriptionStatus.REQUESTED);
		retval.setCriteria(theCriteria);
		retval.getMeta()
				.addTag()
				.setSystem(MdmConstants.SYSTEM_MDM_MANAGED)
				.setCode(MdmConstants.CODE_HAPI_MDM_MANAGED);
		retval.addExtension()
				.setUrl(HapiExtensions.EXTENSION_SUBSCRIPTION_CROSS_PARTITION)
				.setValue(new BooleanType().setValue(true));
		Subscription.SubscriptionChannelComponent channel = retval.getChannel();
		channel.setType(Subscription.SubscriptionChannelType.MESSAGE);
		channel.setEndpoint("channel:"
				+ myChannelNamer.getChannelName(IMdmSettings.EMPI_CHANNEL_NAME, new ChannelProducerSettings()));
		channel.setPayload("application/json");
		return retval;
	}

	private SubscriptionTopic buildMdmSubscriptionTopicR5(List<String> theMdmResourceTypes) {
		SubscriptionTopic subscriptionTopic = new SubscriptionTopic();
		// TODO: what id to set ? mdm-subscription-topic is ok?
		subscriptionTopic.setId(MDM_SUBSCIPRION_ID_PREFIX + "subscription-topic");
		subscriptionTopic.setStatus(Enumerations.PublicationStatus.ACTIVE);
		subscriptionTopic.setUrl("http://example.com/topic/test/mdm");
		theMdmResourceTypes.forEach(
				resourceType -> getSubscriptionTopicResourceTriggerComponent(resourceType, subscriptionTopic));
		return subscriptionTopic;
	}

	private static void getSubscriptionTopicResourceTriggerComponent(
			String theResourceType, SubscriptionTopic theSubscriptionTopic) {
		SubscriptionTopic.SubscriptionTopicResourceTriggerComponent trigger = theSubscriptionTopic.addResourceTrigger();
		trigger.setResource(theResourceType);
		trigger.addSupportedInteraction(SubscriptionTopic.InteractionTrigger.CREATE);
		trigger.addSupportedInteraction(SubscriptionTopic.InteractionTrigger.UPDATE);
		SubscriptionTopic.SubscriptionTopicResourceTriggerQueryCriteriaComponent queryCriteria =
				trigger.getQueryCriteria();
		queryCriteria.setCurrent(theResourceType + "?");
		queryCriteria.setRequireBoth(false);
		// theSubscriptionTopic.addResourceTrigger(trigger);
	}

	private org.hl7.fhir.r5.model.Subscription buildMdmSubscriptionR5(SubscriptionTopic theSubscriptionTopic) {
		org.hl7.fhir.r5.model.Subscription retVal = new org.hl7.fhir.r5.model.Subscription();

		String resourcesString = theSubscriptionTopic.getResourceTrigger().stream()
				.map(SubscriptionTopic.SubscriptionTopicResourceTriggerComponent::getResource)
				.collect(Collectors.joining("-"));
		String subscriptionId = MDM_SUBSCIPRION_ID_PREFIX + "-" + resourcesString;
		retVal.setId(subscriptionId);
		retVal.setReason("MDM");
		retVal.setStatus(Enumerations.SubscriptionStatusCodes.REQUESTED);

		//		fixme jm: this way?
		retVal.setTopic(theSubscriptionTopic.getUrl());
		retVal.setTopicElement(new CanonicalType(theSubscriptionTopic.getUrl()));
		retVal.getMeta()
				.addTag()
				.setSystem(MdmConstants.SYSTEM_MDM_MANAGED)
				.setCode(MdmConstants.CODE_HAPI_MDM_MANAGED);
		retVal.addExtension()
				.setUrl(HapiExtensions.EXTENSION_SUBSCRIPTION_CROSS_PARTITION)
				.setValue(new org.hl7.fhir.r5.model.BooleanType().setValue(true));

		retVal.setChannelType(new Coding()
				.setCode(SubscriptionChannelTypeEnum.MESSAGE.getCode())
				.setSystem(SUBSCRIPTION_CHANNEL_TYPE_SYSTEM_R5));

		retVal.setEndpoint("channel:"
				+ myChannelNamer.getChannelName(IMdmSettings.EMPI_CHANNEL_NAME, new ChannelProducerSettings()));
		retVal.setContentType("application/json");
		//		fixme jm: not required anymore. Right?
		//		org.hl7.fhir.r5.model.Subscription.SubscriptionChannelComponent channel = retval.getChannel();
		//		channel.setPayload("application/json");

		return retVal;
	}
}
