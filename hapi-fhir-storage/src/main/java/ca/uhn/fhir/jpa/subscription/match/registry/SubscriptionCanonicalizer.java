/*-
 * #%L
 * HAPI FHIR Storage api
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
package ca.uhn.fhir.jpa.subscription.match.registry;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.config.SubscriptionSettings;
import ca.uhn.fhir.jpa.subscription.match.matcher.matching.SubscriptionMatchingStrategy;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscriptionChannelType;
import ca.uhn.fhir.jpa.subscription.model.CanonicalTopicSubscription;
import ca.uhn.fhir.jpa.subscription.model.CanonicalTopicSubscriptionFilter;
import ca.uhn.fhir.model.api.BasePrimitive;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.dstu2.resource.Subscription;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.subscription.SubscriptionConstants;
import ca.uhn.fhir.util.HapiExtensions;
import ca.uhn.fhir.util.SubscriptionUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseHasExtensions;
import org.hl7.fhir.instance.model.api.IBaseMetaType;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r5.model.Enumerations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ca.uhn.fhir.util.HapiExtensions.EX_SEND_DELETE_MESSAGES;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

public class SubscriptionCanonicalizer {
	private static final Logger ourLog = LoggerFactory.getLogger(SubscriptionCanonicalizer.class);

	final FhirContext myFhirContext;
	private final SubscriptionSettings mySubscriptionSettings;

	@Autowired
	public SubscriptionCanonicalizer(FhirContext theFhirContext, SubscriptionSettings theSubscriptionSettings) {
		myFhirContext = theFhirContext;
		mySubscriptionSettings = theSubscriptionSettings;
	}

	// TODO:  LD:  remove this constructor once all callers call the 2 arg constructor above

	/**
	 * @deprecated All callers should invoke {@link SubscriptionCanonicalizer()} instead.
	 */
	@Deprecated
	public SubscriptionCanonicalizer(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
		mySubscriptionSettings = new SubscriptionSettings();
	}

	public CanonicalSubscription canonicalize(IBaseResource theSubscription) {
		switch (myFhirContext.getVersion().getVersion()) {
			case DSTU2:
				return canonicalizeDstu2(theSubscription);
			case DSTU3:
				return canonicalizeDstu3(theSubscription);
			case R4:
				return canonicalizeR4(theSubscription);
			case R4B:
				return canonicalizeR4B(theSubscription);
			case R5:
				return canonicalizeR5(theSubscription);
			case DSTU2_HL7ORG:
			case DSTU2_1:
			default:
				throw new ConfigurationException(Msg.code(556) + "Subscription not supported for version: "
						+ myFhirContext.getVersion().getVersion());
		}
	}

	private CanonicalSubscription canonicalizeDstu2(IBaseResource theSubscription) {
		ca.uhn.fhir.model.dstu2.resource.Subscription subscription =
				(ca.uhn.fhir.model.dstu2.resource.Subscription) theSubscription;
		CanonicalSubscription retVal = new CanonicalSubscription();
		try {
			retVal.setStatus(org.hl7.fhir.r4.model.Subscription.SubscriptionStatus.fromCode(subscription.getStatus()));
			retVal.setChannelType(getChannelType(theSubscription));
			retVal.setCriteriaString(subscription.getCriteria());
			Subscription.Channel channel = subscription.getChannel();
			retVal.setEndpointUrl(channel.getEndpoint());
			retVal.setHeaders(channel.getHeader());
			retVal.setChannelExtensions(extractExtension(subscription));
			retVal.setIdElement(subscription.getIdElement());
			retVal.setPayloadString(channel.getPayload());
			retVal.setTags(extractTags(subscription));
			handleCrossPartition(theSubscription, retVal);
			retVal.setSendDeleteMessages(extractDeleteExtensionDstu2(subscription));
		} catch (FHIRException theE) {
			throw new InternalErrorException(Msg.code(557) + theE);
		}
		return retVal;
	}

	private boolean extractDeleteExtensionDstu2(ca.uhn.fhir.model.dstu2.resource.Subscription theSubscription) {
		return theSubscription.getChannel().getUndeclaredExtensionsByUrl(EX_SEND_DELETE_MESSAGES).stream()
				.map(ExtensionDt::getValue)
				.map(value -> (BooleanDt) value)
				.map(BasePrimitive::getValue)
				.findFirst()
				.orElse(false);
	}

	/**
	 * Extract the meta tags from the subscription and convert them to a simple string map.
	 *
	 * @param theSubscription The subscription to extract the tags from
	 * @return A map of tags System:Code
	 */
	private Map<String, String> extractTags(IBaseResource theSubscription) {
		Map<String, String> retVal = new HashMap<>();
		theSubscription.getMeta().getTag().stream()
				.filter(t -> t.getSystem() != null && t.getCode() != null)
				.forEach(t -> retVal.put(t.getSystem(), t.getCode()));
		return retVal;
	}

	private CanonicalSubscription canonicalizeDstu3(IBaseResource theSubscription) {
		org.hl7.fhir.dstu3.model.Subscription subscription = (org.hl7.fhir.dstu3.model.Subscription) theSubscription;

		CanonicalSubscription retVal = new CanonicalSubscription();
		try {
			org.hl7.fhir.dstu3.model.Subscription.SubscriptionStatus status = subscription.getStatus();
			if (status != null) {
				retVal.setStatus(org.hl7.fhir.r4.model.Subscription.SubscriptionStatus.fromCode(status.toCode()));
			}
			setPartitionIdOnReturnValue(theSubscription, retVal);
			retVal.setChannelType(getChannelType(theSubscription));
			retVal.setCriteriaString(subscription.getCriteria());
			org.hl7.fhir.dstu3.model.Subscription.SubscriptionChannelComponent channel = subscription.getChannel();
			retVal.setEndpointUrl(channel.getEndpoint());
			retVal.setHeaders(channel.getHeader());
			retVal.setChannelExtensions(extractExtension(subscription));
			retVal.setIdElement(subscription.getIdElement());
			retVal.setPayloadString(channel.getPayload());
			retVal.setPayloadSearchCriteria(
					getExtensionString(subscription, HapiExtensions.EXT_SUBSCRIPTION_PAYLOAD_SEARCH_CRITERIA));
			retVal.setTags(extractTags(subscription));
			handleCrossPartition(theSubscription, retVal);

			if (retVal.getChannelType() == CanonicalSubscriptionChannelType.EMAIL) {
				String from;
				String subjectTemplate;

				try {
					from = channel.getExtensionString(HapiExtensions.EXT_SUBSCRIPTION_EMAIL_FROM);
					subjectTemplate = channel.getExtensionString(HapiExtensions.EXT_SUBSCRIPTION_SUBJECT_TEMPLATE);
				} catch (FHIRException theE) {
					throw new ConfigurationException(
							Msg.code(558) + "Failed to extract subscription extension(s): " + theE.getMessage(), theE);
				}
				retVal.getEmailDetails().setFrom(from);
				retVal.getEmailDetails().setSubjectTemplate(subjectTemplate);
			}

			if (retVal.getChannelType() == CanonicalSubscriptionChannelType.RESTHOOK) {

				String stripVersionIds;
				String deliverLatestVersion;
				try {
					stripVersionIds =
							channel.getExtensionString(HapiExtensions.EXT_SUBSCRIPTION_RESTHOOK_STRIP_VERSION_IDS);
					deliverLatestVersion =
							channel.getExtensionString(HapiExtensions.EXT_SUBSCRIPTION_RESTHOOK_DELIVER_LATEST_VERSION);
				} catch (FHIRException theE) {
					throw new ConfigurationException(
							Msg.code(559) + "Failed to extract subscription extension(s): " + theE.getMessage(), theE);
				}
				retVal.getRestHookDetails().setStripVersionId(Boolean.parseBoolean(stripVersionIds));
				retVal.getRestHookDetails().setDeliverLatestVersion(Boolean.parseBoolean(deliverLatestVersion));
			}
			retVal.setSendDeleteMessages(extractSendDeletesDstu3(subscription));

		} catch (FHIRException theE) {
			throw new InternalErrorException(Msg.code(560) + theE);
		}
		return retVal;
	}

	private Boolean extractSendDeletesDstu3(org.hl7.fhir.dstu3.model.Subscription subscription) {
		return subscription.getChannel().getExtensionsByUrl(EX_SEND_DELETE_MESSAGES).stream()
				.map(org.hl7.fhir.dstu3.model.Extension::getValue)
				.filter(val -> val instanceof org.hl7.fhir.dstu3.model.BooleanType)
				.map(val -> (org.hl7.fhir.dstu3.model.BooleanType) val)
				.map(org.hl7.fhir.dstu3.model.BooleanType::booleanValue)
				.findFirst()
				.orElse(false);
	}

	private @Nonnull Map<String, List<String>> extractExtension(IBaseResource theSubscription) {
		try {
			switch (theSubscription.getStructureFhirVersionEnum()) {
				case DSTU2: {
					ca.uhn.fhir.model.dstu2.resource.Subscription subscription =
							(ca.uhn.fhir.model.dstu2.resource.Subscription) theSubscription;
					return subscription.getChannel().getUndeclaredExtensions().stream()
							.collect(Collectors.groupingBy(
									t -> t.getUrl(),
									mapping(t -> t.getValueAsPrimitive().getValueAsString(), toList())));
				}
				case DSTU3: {
					org.hl7.fhir.dstu3.model.Subscription subscription =
							(org.hl7.fhir.dstu3.model.Subscription) theSubscription;
					return subscription.getChannel().getExtension().stream()
							.collect(Collectors.groupingBy(
									t -> t.getUrl(),
									mapping(t -> t.getValueAsPrimitive().getValueAsString(), toList())));
				}
				case R4: {
					org.hl7.fhir.r4.model.Subscription subscription =
							(org.hl7.fhir.r4.model.Subscription) theSubscription;
					return subscription.getChannel().getExtension().stream()
							.collect(Collectors.groupingBy(
									t -> t.getUrl(),
									mapping(
											t -> {
												return t.getValueAsPrimitive().getValueAsString();
											},
											toList())));
				}
				case R5: {
					// TODO KHS fix org.hl7.fhir.r4b.model.BaseResource.getStructureFhirVersionEnum() for R4B
					if (theSubscription instanceof org.hl7.fhir.r4b.model.Subscription) {
						org.hl7.fhir.r4b.model.Subscription subscription =
								(org.hl7.fhir.r4b.model.Subscription) theSubscription;
						return subscription.getExtension().stream()
								.collect(Collectors.groupingBy(
										t -> t.getUrl(),
										mapping(t -> t.getValueAsPrimitive().getValueAsString(), toList())));
					} else if (theSubscription instanceof org.hl7.fhir.r5.model.Subscription) {
						org.hl7.fhir.r5.model.Subscription subscription =
								(org.hl7.fhir.r5.model.Subscription) theSubscription;
						return subscription.getExtension().stream()
								.collect(Collectors.groupingBy(
										t -> t.getUrl(),
										mapping(t -> t.getValueAsPrimitive().getValueAsString(), toList())));
					}
				}
				case DSTU2_HL7ORG:
				case DSTU2_1:
				default: {
					ourLog.error(
							"Failed to extract extension from subscription {}",
							theSubscription.getIdElement().toUnqualified().getValue());
					break;
				}
			}
		} catch (FHIRException theE) {
			ourLog.error(
					"Failed to extract extension from subscription {}",
					theSubscription.getIdElement().toUnqualified().getValue(),
					theE);
		}
		return Collections.emptyMap();
	}

	private CanonicalSubscription canonicalizeR4(IBaseResource theSubscription) {
		org.hl7.fhir.r4.model.Subscription subscription = (org.hl7.fhir.r4.model.Subscription) theSubscription;
		CanonicalSubscription retVal = new CanonicalSubscription();
		retVal.setStatus(subscription.getStatus());
		org.hl7.fhir.r4.model.Subscription.SubscriptionChannelComponent channel = subscription.getChannel();
		retVal.setHeaders(channel.getHeader());
		retVal.setChannelExtensions(extractExtension(subscription));
		retVal.setIdElement(subscription.getIdElement());
		retVal.setPayloadString(channel.getPayload());
		retVal.setPayloadSearchCriteria(
				getExtensionString(subscription, HapiExtensions.EXT_SUBSCRIPTION_PAYLOAD_SEARCH_CRITERIA));
		retVal.setTags(extractTags(subscription));
		setPartitionIdOnReturnValue(theSubscription, retVal);
		handleCrossPartition(theSubscription, retVal);

		List<org.hl7.fhir.r4.model.CanonicalType> profiles =
				subscription.getMeta().getProfile();
		for (org.hl7.fhir.r4.model.CanonicalType next : profiles) {
			if (SubscriptionConstants.SUBSCRIPTION_TOPIC_PROFILE_URL.equals(next.getValueAsString())) {
				retVal.setTopicSubscription(true);
			}
		}

		if (retVal.isTopicSubscription()) {
			CanonicalTopicSubscription topicSubscription = retVal.getTopicSubscription();
			topicSubscription.setTopic(getCriteria(theSubscription));

			retVal.setEndpointUrl(channel.getEndpoint());
			retVal.setChannelType(getChannelType(subscription));

			for (org.hl7.fhir.r4.model.Extension next :
					subscription.getCriteriaElement().getExtension()) {
				if (SubscriptionConstants.SUBSCRIPTION_TOPIC_FILTER_URL.equals(next.getUrl())) {
					List<CanonicalTopicSubscriptionFilter> filters = CanonicalTopicSubscriptionFilter.fromQueryUrl(
							next.getValue().primitiveValue());
					filters.forEach(topicSubscription::addFilter);
				}
			}

			if (channel.hasExtension(SubscriptionConstants.SUBSCRIPTION_TOPIC_CHANNEL_HEARTBEAT_PERIOD_URL)) {
				org.hl7.fhir.r4.model.Extension channelHeartbeatPeriotUrlExtension = channel.getExtensionByUrl(
						SubscriptionConstants.SUBSCRIPTION_TOPIC_CHANNEL_HEARTBEAT_PERIOD_URL);
				topicSubscription.setHeartbeatPeriod(Integer.valueOf(
						channelHeartbeatPeriotUrlExtension.getValue().primitiveValue()));
			}
			if (channel.hasExtension(SubscriptionConstants.SUBSCRIPTION_TOPIC_CHANNEL_TIMEOUT_URL)) {
				org.hl7.fhir.r4.model.Extension channelTimeoutUrlExtension =
						channel.getExtensionByUrl(SubscriptionConstants.SUBSCRIPTION_TOPIC_CHANNEL_TIMEOUT_URL);
				topicSubscription.setTimeout(
						Integer.valueOf(channelTimeoutUrlExtension.getValue().primitiveValue()));
			}
			if (channel.hasExtension(SubscriptionConstants.SUBSCRIPTION_TOPIC_CHANNEL_MAX_COUNT)) {
				org.hl7.fhir.r4.model.Extension channelMaxCountExtension =
						channel.getExtensionByUrl(SubscriptionConstants.SUBSCRIPTION_TOPIC_CHANNEL_MAX_COUNT);
				topicSubscription.setMaxCount(
						Integer.valueOf(channelMaxCountExtension.getValue().primitiveValue()));
			}

			// setting full-resource PayloadContent if backport-payload-content is not provided
			org.hl7.fhir.r5.model.Subscription.SubscriptionPayloadContent payloadContent =
					org.hl7.fhir.r5.model.Subscription.SubscriptionPayloadContent.FULLRESOURCE;

			org.hl7.fhir.r4.model.Extension channelPayloadContentExtension = channel.getPayloadElement()
					.getExtensionByUrl(SubscriptionConstants.SUBSCRIPTION_TOPIC_CHANNEL_PAYLOAD_CONTENT);

			if (nonNull(channelPayloadContentExtension)) {
				payloadContent = org.hl7.fhir.r5.model.Subscription.SubscriptionPayloadContent.fromCode(
						channelPayloadContentExtension.getValue().primitiveValue());
			}

			topicSubscription.setContent(payloadContent);
		} else {
			retVal.setCriteriaString(getCriteria(theSubscription));
			retVal.setEndpointUrl(channel.getEndpoint());
			retVal.setChannelType(getChannelType(subscription));
		}

		if (retVal.getChannelType() == CanonicalSubscriptionChannelType.EMAIL) {
			String from;
			String subjectTemplate;
			try {
				from = channel.getExtensionString(HapiExtensions.EXT_SUBSCRIPTION_EMAIL_FROM);
				subjectTemplate = channel.getExtensionString(HapiExtensions.EXT_SUBSCRIPTION_SUBJECT_TEMPLATE);
			} catch (FHIRException theE) {
				throw new ConfigurationException(
						Msg.code(561) + "Failed to extract subscription extension(s): " + theE.getMessage(), theE);
			}
			retVal.getEmailDetails().setFrom(from);
			retVal.getEmailDetails().setSubjectTemplate(subjectTemplate);
		}

		if (retVal.getChannelType() == CanonicalSubscriptionChannelType.RESTHOOK) {
			String stripVersionIds;
			String deliverLatestVersion;
			try {
				stripVersionIds =
						channel.getExtensionString(HapiExtensions.EXT_SUBSCRIPTION_RESTHOOK_STRIP_VERSION_IDS);
				deliverLatestVersion =
						channel.getExtensionString(HapiExtensions.EXT_SUBSCRIPTION_RESTHOOK_DELIVER_LATEST_VERSION);
			} catch (FHIRException theE) {
				throw new ConfigurationException(
						Msg.code(562) + "Failed to extract subscription extension(s): " + theE.getMessage(), theE);
			}
			retVal.getRestHookDetails().setStripVersionId(Boolean.parseBoolean(stripVersionIds));
			retVal.getRestHookDetails().setDeliverLatestVersion(Boolean.parseBoolean(deliverLatestVersion));
		}

		List<Extension> topicExts = subscription.getExtensionsByUrl("http://hl7.org/fhir/subscription/topics");
		if (topicExts.size() > 0) {
			IBaseReference ref = (IBaseReference) topicExts.get(0).getValueAsPrimitive();
			if (!"EventDefinition".equals(ref.getReferenceElement().getResourceType())) {
				throw new PreconditionFailedException(Msg.code(563) + "Topic reference must be an EventDefinition");
			}
		}

		Extension extension = channel.getExtensionByUrl(EX_SEND_DELETE_MESSAGES);
		if (extension != null && extension.hasValue() && extension.getValue() instanceof BooleanType) {
			retVal.setSendDeleteMessages(((BooleanType) extension.getValue()).booleanValue());
		}
		return retVal;
	}

	private CanonicalSubscription canonicalizeR4B(IBaseResource theSubscription) {
		org.hl7.fhir.r4b.model.Subscription subscription = (org.hl7.fhir.r4b.model.Subscription) theSubscription;

		CanonicalSubscription retVal = new CanonicalSubscription();
		org.hl7.fhir.r4b.model.Enumerations.SubscriptionStatus status = subscription.getStatus();
		if (status != null) {
			retVal.setStatus(org.hl7.fhir.r4.model.Subscription.SubscriptionStatus.fromCode(status.toCode()));
		}
		setPartitionIdOnReturnValue(theSubscription, retVal);
		org.hl7.fhir.r4b.model.Subscription.SubscriptionChannelComponent channel = subscription.getChannel();
		retVal.setHeaders(channel.getHeader());
		retVal.setChannelExtensions(extractExtension(subscription));
		retVal.setIdElement(subscription.getIdElement());
		retVal.setPayloadString(channel.getPayload());
		retVal.setPayloadSearchCriteria(
				getExtensionString(subscription, HapiExtensions.EXT_SUBSCRIPTION_PAYLOAD_SEARCH_CRITERIA));
		retVal.setTags(extractTags(subscription));

		List<org.hl7.fhir.r4b.model.CanonicalType> profiles =
				subscription.getMeta().getProfile();
		for (org.hl7.fhir.r4b.model.CanonicalType next : profiles) {
			if (SubscriptionConstants.SUBSCRIPTION_TOPIC_PROFILE_URL.equals(next.getValueAsString())) {
				retVal.setTopicSubscription(true);
			}
		}

		if (retVal.isTopicSubscription()) {
			CanonicalTopicSubscription topicSubscription = retVal.getTopicSubscription();
			topicSubscription.setTopic(getCriteria(theSubscription));

			retVal.setEndpointUrl(channel.getEndpoint());
			retVal.setChannelType(getChannelType(subscription));

			// setting full-resource PayloadContent if backport-payload-content is not provided
			org.hl7.fhir.r5.model.Subscription.SubscriptionPayloadContent payloadContent =
					org.hl7.fhir.r5.model.Subscription.SubscriptionPayloadContent.FULLRESOURCE;

			org.hl7.fhir.r4b.model.Extension channelPayloadContentExtension = channel.getPayloadElement()
					.getExtensionByUrl(SubscriptionConstants.SUBSCRIPTION_TOPIC_CHANNEL_PAYLOAD_CONTENT);

			if (nonNull(channelPayloadContentExtension)) {
				payloadContent = org.hl7.fhir.r5.model.Subscription.SubscriptionPayloadContent.fromCode(
						channelPayloadContentExtension.getValue().primitiveValue());
			}

			topicSubscription.setContent(payloadContent);
		} else {
			retVal.setCriteriaString(getCriteria(theSubscription));
			retVal.setEndpointUrl(channel.getEndpoint());
			retVal.setChannelType(getChannelType(subscription));
		}

		if (retVal.getChannelType() == CanonicalSubscriptionChannelType.EMAIL) {
			String from;
			String subjectTemplate;
			try {
				from = getExtensionString(subscription, HapiExtensions.EXT_SUBSCRIPTION_EMAIL_FROM);
				subjectTemplate = getExtensionString(subscription, HapiExtensions.EXT_SUBSCRIPTION_SUBJECT_TEMPLATE);
			} catch (FHIRException theE) {
				throw new ConfigurationException(
						Msg.code(564) + "Failed to extract subscription extension(s): " + theE.getMessage(), theE);
			}
			retVal.getEmailDetails().setFrom(from);
			retVal.getEmailDetails().setSubjectTemplate(subjectTemplate);
		}

		if (retVal.getChannelType() == CanonicalSubscriptionChannelType.RESTHOOK) {
			String stripVersionIds;
			String deliverLatestVersion;
			try {
				stripVersionIds =
						getExtensionString(channel, HapiExtensions.EXT_SUBSCRIPTION_RESTHOOK_STRIP_VERSION_IDS);
				deliverLatestVersion =
						getExtensionString(channel, HapiExtensions.EXT_SUBSCRIPTION_RESTHOOK_DELIVER_LATEST_VERSION);
			} catch (FHIRException theE) {
				throw new ConfigurationException(
						Msg.code(565) + "Failed to extract subscription extension(s): " + theE.getMessage(), theE);
			}
			retVal.getRestHookDetails().setStripVersionId(Boolean.parseBoolean(stripVersionIds));
			retVal.getRestHookDetails().setDeliverLatestVersion(Boolean.parseBoolean(deliverLatestVersion));
		}

		List<org.hl7.fhir.r4b.model.Extension> topicExts =
				subscription.getExtensionsByUrl("http://hl7.org/fhir/subscription/topics");
		if (topicExts.size() > 0) {
			IBaseReference ref = (IBaseReference) topicExts.get(0).getValueAsPrimitive();
			if (!"EventDefinition".equals(ref.getReferenceElement().getResourceType())) {
				throw new PreconditionFailedException(Msg.code(566) + "Topic reference must be an EventDefinition");
			}
		}

		org.hl7.fhir.r4b.model.Extension extension = channel.getExtensionByUrl(EX_SEND_DELETE_MESSAGES);
		if (extension != null && extension.hasValue() && extension.hasValueBooleanType()) {
			retVal.setSendDeleteMessages(extension.getValueBooleanType().booleanValue());
		}

		handleCrossPartition(theSubscription, retVal);

		return retVal;
	}

	private CanonicalSubscription canonicalizeR5(IBaseResource theSubscription) {
		org.hl7.fhir.r5.model.Subscription subscription = (org.hl7.fhir.r5.model.Subscription) theSubscription;

		CanonicalSubscription retVal = new CanonicalSubscription();

		setPartitionIdOnReturnValue(theSubscription, retVal);
		retVal.setChannelExtensions(extractExtension(subscription));
		retVal.setIdElement(subscription.getIdElement());
		retVal.setPayloadString(subscription.getContentType());
		retVal.setPayloadSearchCriteria(
				getExtensionString(subscription, HapiExtensions.EXT_SUBSCRIPTION_PAYLOAD_SEARCH_CRITERIA));
		retVal.setTags(extractTags(subscription));

		List<org.hl7.fhir.r5.model.Extension> topicExts =
				subscription.getExtensionsByUrl("http://hl7.org/fhir/subscription/topics");
		if (topicExts.size() > 0) {
			IBaseReference ref = (IBaseReference) topicExts.get(0).getValueAsPrimitive();
			if (!"EventDefinition".equals(ref.getReferenceElement().getResourceType())) {
				throw new PreconditionFailedException(Msg.code(2325) + "Topic reference must be an EventDefinition");
			}
		}

		// All R5 subscriptions are topic subscriptions
		retVal.setTopicSubscription(true);

		Enumerations.SubscriptionStatusCodes status = subscription.getStatus();
		if (status != null) {
			switch (status) {
				case REQUESTED:
					retVal.setStatus(org.hl7.fhir.r4.model.Subscription.SubscriptionStatus.REQUESTED);
					break;
				case ACTIVE:
					retVal.setStatus(org.hl7.fhir.r4.model.Subscription.SubscriptionStatus.ACTIVE);
					break;
				case ERROR:
					retVal.setStatus(org.hl7.fhir.r4.model.Subscription.SubscriptionStatus.ERROR);
					break;
				case OFF:
					retVal.setStatus(org.hl7.fhir.r4.model.Subscription.SubscriptionStatus.OFF);
					break;
				case NULL:
				case ENTEREDINERROR:
				default:
					ourLog.warn("Converting R5 Subscription status from {} to ERROR", status);
					retVal.setStatus(org.hl7.fhir.r4.model.Subscription.SubscriptionStatus.ERROR);
			}
		}
		retVal.getTopicSubscription().setContent(subscription.getContent());
		retVal.setEndpointUrl(subscription.getEndpoint());
		retVal.getTopicSubscription().setTopic(subscription.getTopic());
		retVal.setChannelType(getChannelType(subscription));

		subscription.getFilterBy().forEach(filter -> {
			retVal.getTopicSubscription().addFilter(convertFilter(filter));
		});

		retVal.getTopicSubscription().setHeartbeatPeriod(subscription.getHeartbeatPeriod());
		retVal.getTopicSubscription().setMaxCount(subscription.getMaxCount());

		setR5FlagsBasedOnChannelType(subscription, retVal);

		handleCrossPartition(theSubscription, retVal);

		return retVal;
	}

	private void setR5FlagsBasedOnChannelType(
			org.hl7.fhir.r5.model.Subscription subscription, CanonicalSubscription retVal) {
		if (retVal.getChannelType() == CanonicalSubscriptionChannelType.EMAIL) {
			String from;
			String subjectTemplate;
			try {
				from = getExtensionString(subscription, HapiExtensions.EXT_SUBSCRIPTION_EMAIL_FROM);
				subjectTemplate = getExtensionString(subscription, HapiExtensions.EXT_SUBSCRIPTION_SUBJECT_TEMPLATE);
			} catch (FHIRException theE) {
				throw new ConfigurationException(
						Msg.code(2323) + "Failed to extract subscription extension(s): " + theE.getMessage(), theE);
			}
			retVal.getEmailDetails().setFrom(from);
			retVal.getEmailDetails().setSubjectTemplate(subjectTemplate);
		}

		if (retVal.getChannelType() == CanonicalSubscriptionChannelType.RESTHOOK) {
			String stripVersionIds;
			String deliverLatestVersion;
			try {
				stripVersionIds =
						getExtensionString(subscription, HapiExtensions.EXT_SUBSCRIPTION_RESTHOOK_STRIP_VERSION_IDS);
				deliverLatestVersion = getExtensionString(
						subscription, HapiExtensions.EXT_SUBSCRIPTION_RESTHOOK_DELIVER_LATEST_VERSION);
			} catch (FHIRException theE) {
				throw new ConfigurationException(
						Msg.code(2324) + "Failed to extract subscription extension(s): " + theE.getMessage(), theE);
			}
			retVal.getRestHookDetails().setStripVersionId(Boolean.parseBoolean(stripVersionIds));
			retVal.getRestHookDetails().setDeliverLatestVersion(Boolean.parseBoolean(deliverLatestVersion));
		}
	}

	private CanonicalTopicSubscriptionFilter convertFilter(
			org.hl7.fhir.r5.model.Subscription.SubscriptionFilterByComponent theFilter) {
		CanonicalTopicSubscriptionFilter retVal = new CanonicalTopicSubscriptionFilter();
		retVal.setResourceType(theFilter.getResourceType());
		retVal.setFilterParameter(theFilter.getFilterParameter());
		retVal.setModifier(theFilter.getModifier());
		retVal.setComparator(theFilter.getComparator());
		retVal.setValue(theFilter.getValue());
		return retVal;
	}

	private void setPartitionIdOnReturnValue(IBaseResource theSubscription, CanonicalSubscription retVal) {
		RequestPartitionId requestPartitionId =
				(RequestPartitionId) theSubscription.getUserData(Constants.RESOURCE_PARTITION_ID);
		if (requestPartitionId != null) {
			retVal.setPartitionId(requestPartitionId.getFirstPartitionIdOrNull());
		}
	}

	private String getExtensionString(IBaseHasExtensions theBase, String theUrl) {
		return theBase.getExtension().stream()
				.filter(t -> theUrl.equals(t.getUrl()))
				.filter(t -> t.getValue() instanceof IPrimitiveType)
				.map(t -> (IPrimitiveType<?>) t.getValue())
				.map(t -> t.getValueAsString())
				.findFirst()
				.orElse(null);
	}

	@SuppressWarnings("EnumSwitchStatementWhichMissesCases")
	public CanonicalSubscriptionChannelType getChannelType(IBaseResource theSubscription) {
		CanonicalSubscriptionChannelType retVal = null;

		switch (myFhirContext.getVersion().getVersion()) {
			case DSTU2: {
				String channelTypeCode = ((ca.uhn.fhir.model.dstu2.resource.Subscription) theSubscription)
						.getChannel()
						.getType();
				retVal = CanonicalSubscriptionChannelType.fromCode(null, channelTypeCode);
				break;
			}
			case DSTU3: {
				org.hl7.fhir.dstu3.model.Subscription.SubscriptionChannelType type =
						((org.hl7.fhir.dstu3.model.Subscription) theSubscription)
								.getChannel()
								.getType();
				if (type != null) {
					String channelTypeCode = type.toCode();
					retVal = CanonicalSubscriptionChannelType.fromCode(null, channelTypeCode);
				}
				break;
			}
			case R4: {
				org.hl7.fhir.r4.model.Subscription.SubscriptionChannelType type = ((org.hl7.fhir.r4.model.Subscription)
								theSubscription)
						.getChannel()
						.getType();
				if (type != null) {
					String channelTypeCode = type.toCode();
					retVal = CanonicalSubscriptionChannelType.fromCode(null, channelTypeCode);
				}
				break;
			}
			case R4B: {
				org.hl7.fhir.r4b.model.Subscription.SubscriptionChannelType type =
						((org.hl7.fhir.r4b.model.Subscription) theSubscription)
								.getChannel()
								.getType();
				if (type != null) {
					String channelTypeCode = type.toCode();
					retVal = CanonicalSubscriptionChannelType.fromCode(null, channelTypeCode);
				}
				break;
			}
			case R5: {
				org.hl7.fhir.r5.model.Coding nextTypeCode =
						((org.hl7.fhir.r5.model.Subscription) theSubscription).getChannelType();
				CanonicalSubscriptionChannelType code =
						CanonicalSubscriptionChannelType.fromCode(nextTypeCode.getSystem(), nextTypeCode.getCode());
				if (code != null) {
					retVal = code;
				}
				break;
			}
			default:
				throw new IllegalStateException(Msg.code(2326) + "Unsupported Subscription FHIR version: "
						+ myFhirContext.getVersion().getVersion());
		}

		return retVal;
	}

	@SuppressWarnings("EnumSwitchStatementWhichMissesCases")
	@Nullable
	public String getCriteria(IBaseResource theSubscription) {
		String retVal = null;

		switch (myFhirContext.getVersion().getVersion()) {
			case DSTU2:
				retVal = ((Subscription) theSubscription).getCriteria();
				break;
			case DSTU3:
				retVal = ((org.hl7.fhir.dstu3.model.Subscription) theSubscription).getCriteria();
				break;
			case R4:
				retVal = ((org.hl7.fhir.r4.model.Subscription) theSubscription).getCriteria();
				break;
			case R4B:
				retVal = ((org.hl7.fhir.r4b.model.Subscription) theSubscription).getCriteria();
				break;
			case R5:
			default:
				throw new IllegalStateException(
						Msg.code(2327) + "Subscription criteria is not supported for FHIR version: "
								+ myFhirContext.getVersion().getVersion());
		}

		return retVal;
	}

	public void setMatchingStrategyTag(
			@Nonnull IBaseResource theSubscription, @Nullable SubscriptionMatchingStrategy theStrategy) {
		IBaseMetaType meta = theSubscription.getMeta();

		// Remove any existing strategy tag
		meta.getTag().stream()
				.filter(t -> HapiExtensions.EXT_SUBSCRIPTION_MATCHING_STRATEGY.equals(t.getSystem()))
				.forEach(t -> {
					t.setCode(null);
					t.setSystem(null);
					t.setDisplay(null);
				});

		if (theStrategy == null) {
			return;
		}

		String value = theStrategy.toString();
		String display;

		if (theStrategy == SubscriptionMatchingStrategy.DATABASE) {
			display = "Database";
		} else if (theStrategy == SubscriptionMatchingStrategy.IN_MEMORY) {
			display = "In-memory";
		} else if (theStrategy == SubscriptionMatchingStrategy.TOPIC) {
			display = "SubscriptionTopic";
		} else {
			throw new IllegalStateException(Msg.code(567) + "Unknown "
					+ SubscriptionMatchingStrategy.class.getSimpleName() + ": " + theStrategy);
		}
		meta.addTag()
				.setSystem(HapiExtensions.EXT_SUBSCRIPTION_MATCHING_STRATEGY)
				.setCode(value)
				.setDisplay(display);
	}

	public String getSubscriptionStatus(IBaseResource theSubscription) {
		final IPrimitiveType<?> status = myFhirContext
				.newTerser()
				.getSingleValueOrNull(theSubscription, SubscriptionConstants.SUBSCRIPTION_STATUS, IPrimitiveType.class);
		if (status == null) {
			return null;
		}
		return status.getValueAsString();
	}

	private void handleCrossPartition(IBaseResource theSubscription, CanonicalSubscription retVal) {
		if (mySubscriptionSettings.isCrossPartitionSubscriptionEnabled()) {
			retVal.setCrossPartitionEnabled(true);
		} else {
			retVal.setCrossPartitionEnabled(SubscriptionUtil.isCrossPartition(theSubscription));
		}
	}
}
