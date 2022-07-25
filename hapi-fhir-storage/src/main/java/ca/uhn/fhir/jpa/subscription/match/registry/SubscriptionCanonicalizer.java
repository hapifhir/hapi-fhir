package ca.uhn.fhir.jpa.subscription.match.registry;

/*-
 * #%L
 * HAPI FHIR Storage api
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
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.subscription.match.matcher.matching.SubscriptionMatchingStrategy;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscriptionChannelType;
import ca.uhn.fhir.model.api.BasePrimitive;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.dstu2.resource.Subscription;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.util.HapiExtensions;
import ca.uhn.fhir.util.SubscriptionUtil;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseHasExtensions;
import org.hl7.fhir.instance.model.api.IBaseMetaType;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static ca.uhn.fhir.util.HapiExtensions.EX_SEND_DELETE_MESSAGES;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

public class SubscriptionCanonicalizer {
	private static final Logger ourLog = LoggerFactory.getLogger(SubscriptionCanonicalizer.class);

	final FhirContext myFhirContext;

	@Autowired
	public SubscriptionCanonicalizer(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
	}

	public CanonicalSubscription canonicalize(IBaseResource theSubscription) {
		switch (myFhirContext.getVersion().getVersion()) {
			case DSTU2:
				return canonicalizeDstu2(theSubscription);
			case DSTU3:
				return canonicalizeDstu3(theSubscription);
			case R4:
				return canonicalizeR4(theSubscription);
			case R5:
				return canonicalizeR5(theSubscription);
			case DSTU2_HL7ORG:
			case DSTU2_1:
			default:
				throw new ConfigurationException(Msg.code(556) + "Subscription not supported for version: " + myFhirContext.getVersion().getVersion());
		}
	}

	private CanonicalSubscription canonicalizeDstu2(IBaseResource theSubscription) {
		ca.uhn.fhir.model.dstu2.resource.Subscription subscription = (ca.uhn.fhir.model.dstu2.resource.Subscription) theSubscription;
		CanonicalSubscription retVal = new CanonicalSubscription();
		try {
			retVal.setStatus(org.hl7.fhir.r4.model.Subscription.SubscriptionStatus.fromCode(subscription.getStatus()));
			retVal.setChannelType(getChannelType(theSubscription));
			retVal.setCriteriaString(subscription.getCriteria());
			retVal.setEndpointUrl(subscription.getChannel().getEndpoint());
			retVal.setHeaders(subscription.getChannel().getHeader());
			retVal.setChannelExtensions(extractExtension(subscription));
			retVal.setIdElement(subscription.getIdElement());
			retVal.setPayloadString(subscription.getChannel().getPayload());
			retVal.setTags(extractTags(subscription));
			retVal.setCrossPartitionEnabled(SubscriptionUtil.isCrossPartition(theSubscription));
			retVal.setSendDeleteMessages(extractDeleteExtensionDstu2(subscription));
		} catch (FHIRException theE) {
			throw new InternalErrorException(Msg.code(557) + theE);
		}
		return retVal;
	}

	private boolean extractDeleteExtensionDstu2(ca.uhn.fhir.model.dstu2.resource.Subscription theSubscription) {
		return theSubscription.getChannel().getUndeclaredExtensionsByUrl(EX_SEND_DELETE_MESSAGES)
			.stream()
			.map(ExtensionDt::getValue)
			.map(BooleanDt.class::cast)
			.map(BasePrimitive::getValue)
			.findFirst()
			.orElse(false);
	}

	/**
	 * Extract the meta tags from the subscription and convert them to a simple string map.
	 * @param theSubscription The subscription to extract the tags from
	 * @return A map of tags System:Code
	 */
	private Map<String, String> extractTags(IBaseResource theSubscription) {
		return theSubscription.getMeta().getTag()
			.stream()
			.filter(t -> t.getSystem() != null && t.getCode() != null)
			.collect(Collectors.toMap(
				IBaseCoding::getSystem,
				IBaseCoding::getCode
			));
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
			retVal.setEndpointUrl(subscription.getChannel().getEndpoint());
			retVal.setHeaders(subscription.getChannel().getHeader());
			retVal.setChannelExtensions(extractExtension(subscription));
			retVal.setIdElement(subscription.getIdElement());
			retVal.setPayloadString(subscription.getChannel().getPayload());
			retVal.setPayloadSearchCriteria(getExtensionString(subscription, HapiExtensions.EXT_SUBSCRIPTION_PAYLOAD_SEARCH_CRITERIA));
			retVal.setTags(extractTags(subscription));
			retVal.setCrossPartitionEnabled(SubscriptionUtil.isCrossPartition(theSubscription));

			if (retVal.getChannelType() == CanonicalSubscriptionChannelType.EMAIL) {
				String from;
				String subjectTemplate;

				try {
					from = subscription.getChannel().getExtensionString(HapiExtensions.EXT_SUBSCRIPTION_EMAIL_FROM);
					subjectTemplate = subscription.getChannel().getExtensionString(HapiExtensions.EXT_SUBSCRIPTION_SUBJECT_TEMPLATE);
				} catch (FHIRException theE) {
					throw new ConfigurationException(Msg.code(558) + "Failed to extract subscription extension(s): " + theE.getMessage(), theE);
				}
				retVal.getEmailDetails().setFrom(from);
				retVal.getEmailDetails().setSubjectTemplate(subjectTemplate);
			}

			if (retVal.getChannelType() == CanonicalSubscriptionChannelType.RESTHOOK) {

				String stripVersionIds;
				String deliverLatestVersion;
				try {
					stripVersionIds = subscription.getChannel().getExtensionString(HapiExtensions.EXT_SUBSCRIPTION_RESTHOOK_STRIP_VERSION_IDS);
					deliverLatestVersion = subscription.getChannel().getExtensionString(HapiExtensions.EXT_SUBSCRIPTION_RESTHOOK_DELIVER_LATEST_VERSION);
				} catch (FHIRException theE) {
					throw new ConfigurationException(Msg.code(559) + "Failed to extract subscription extension(s): " + theE.getMessage(), theE);
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

	private @Nonnull
	Map<String, List<String>> extractExtension(IBaseResource theSubscription) {
		try {
			switch (theSubscription.getStructureFhirVersionEnum()) {
				case DSTU2: {
					ca.uhn.fhir.model.dstu2.resource.Subscription subscription = (ca.uhn.fhir.model.dstu2.resource.Subscription) theSubscription;
					return subscription
						.getChannel()
						.getUndeclaredExtensions()
						.stream()
						.collect(Collectors.groupingBy(t -> t.getUrl(), mapping(t -> t.getValueAsPrimitive().getValueAsString(), toList())));
				}
				case DSTU3: {
					org.hl7.fhir.dstu3.model.Subscription subscription = (org.hl7.fhir.dstu3.model.Subscription) theSubscription;
					return subscription
						.getChannel()
						.getExtension()
						.stream()
						.collect(Collectors.groupingBy(t -> t.getUrl(), mapping(t -> t.getValueAsPrimitive().getValueAsString(), toList())));
				}
				case R4: {
					org.hl7.fhir.r4.model.Subscription subscription = (org.hl7.fhir.r4.model.Subscription) theSubscription;
					return subscription
						.getChannel()
						.getExtension()
						.stream()
						.collect(Collectors.groupingBy(t -> t.getUrl(),
							mapping(t -> {
								return t.getValueAsPrimitive().getValueAsString();
							}, toList())));
				}
				case R5: {
					org.hl7.fhir.r5.model.Subscription subscription = (org.hl7.fhir.r5.model.Subscription) theSubscription;
					return subscription
						.getExtension()
						.stream()
						.collect(Collectors.groupingBy(t -> t.getUrl(), mapping(t -> t.getValueAsPrimitive().getValueAsString(), toList())));
				}
				case DSTU2_HL7ORG:
				case DSTU2_1:
				default: {
					ourLog.error("Failed to extract extension from subscription {}", theSubscription.getIdElement().toUnqualified().getValue());
					break;
				}
			}
		} catch (FHIRException theE) {
			ourLog.error("Failed to extract extension from subscription {}", theSubscription.getIdElement().toUnqualified().getValue(), theE);
		}
		return Collections.emptyMap();
	}

	private CanonicalSubscription canonicalizeR4(IBaseResource theSubscription) {
		org.hl7.fhir.r4.model.Subscription subscription = (org.hl7.fhir.r4.model.Subscription) theSubscription;
		CanonicalSubscription retVal = new CanonicalSubscription();
		retVal.setStatus(subscription.getStatus());
		retVal.setChannelType(getChannelType(theSubscription));
		retVal.setCriteriaString(subscription.getCriteria());
		retVal.setEndpointUrl(subscription.getChannel().getEndpoint());
		retVal.setHeaders(subscription.getChannel().getHeader());
		retVal.setChannelExtensions(extractExtension(subscription));
		retVal.setIdElement(subscription.getIdElement());
		retVal.setPayloadString(subscription.getChannel().getPayload());
		retVal.setPayloadSearchCriteria(getExtensionString(subscription, HapiExtensions.EXT_SUBSCRIPTION_PAYLOAD_SEARCH_CRITERIA));
		retVal.setTags(extractTags(subscription));
		setPartitionIdOnReturnValue(theSubscription, retVal);
		retVal.setCrossPartitionEnabled(SubscriptionUtil.isCrossPartition(theSubscription));

		if (retVal.getChannelType() == CanonicalSubscriptionChannelType.EMAIL) {
			String from;
			String subjectTemplate;
			try {
				from = subscription.getChannel().getExtensionString(HapiExtensions.EXT_SUBSCRIPTION_EMAIL_FROM);
				subjectTemplate = subscription.getChannel().getExtensionString(HapiExtensions.EXT_SUBSCRIPTION_SUBJECT_TEMPLATE);
			} catch (FHIRException theE) {
				throw new ConfigurationException(Msg.code(561) + "Failed to extract subscription extension(s): " + theE.getMessage(), theE);
			}
			retVal.getEmailDetails().setFrom(from);
			retVal.getEmailDetails().setSubjectTemplate(subjectTemplate);
		}

		if (retVal.getChannelType() == CanonicalSubscriptionChannelType.RESTHOOK) {
			String stripVersionIds;
			String deliverLatestVersion;
			try {
				stripVersionIds = subscription.getChannel().getExtensionString(HapiExtensions.EXT_SUBSCRIPTION_RESTHOOK_STRIP_VERSION_IDS);
				deliverLatestVersion = subscription.getChannel().getExtensionString(HapiExtensions.EXT_SUBSCRIPTION_RESTHOOK_DELIVER_LATEST_VERSION);
			} catch (FHIRException theE) {
				throw new ConfigurationException(Msg.code(562) + "Failed to extract subscription extension(s): " + theE.getMessage(), theE);
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

		Extension extension = subscription.getChannel().getExtensionByUrl(EX_SEND_DELETE_MESSAGES);
		if (extension != null && extension.hasValue() && extension.getValue() instanceof BooleanType) {
			retVal.setSendDeleteMessages(((BooleanType) extension.getValue()).booleanValue());
		}
		return retVal;
	}

	private CanonicalSubscription canonicalizeR5(IBaseResource theSubscription) {
		org.hl7.fhir.r5.model.Subscription subscription = (org.hl7.fhir.r5.model.Subscription) theSubscription;

		CanonicalSubscription retVal = new CanonicalSubscription();
		org.hl7.fhir.r5.model.Enumerations.SubscriptionState status = subscription.getStatus();
		if (status != null) {
			retVal.setStatus(org.hl7.fhir.r4.model.Subscription.SubscriptionStatus.fromCode(status.toCode()));
		}
		setPartitionIdOnReturnValue(theSubscription, retVal);
		retVal.setChannelType(getChannelType(subscription));
		retVal.setCriteriaString(getCriteria(theSubscription));
		retVal.setEndpointUrl(subscription.getEndpoint());
		retVal.setHeaders(subscription.getHeader());
		retVal.setChannelExtensions(extractExtension(subscription));
		retVal.setIdElement(subscription.getIdElement());
		retVal.setPayloadString(subscription.getContentType());
		retVal.setPayloadSearchCriteria(getExtensionString(subscription, HapiExtensions.EXT_SUBSCRIPTION_PAYLOAD_SEARCH_CRITERIA));
		retVal.setTags(extractTags(subscription));

		if (retVal.getChannelType() == CanonicalSubscriptionChannelType.EMAIL) {
			String from;
			String subjectTemplate;
			try {
				from = getExtensionString(subscription, HapiExtensions.EXT_SUBSCRIPTION_EMAIL_FROM);
				subjectTemplate = getExtensionString(subscription, HapiExtensions.EXT_SUBSCRIPTION_SUBJECT_TEMPLATE);
			} catch (FHIRException theE) {
				throw new ConfigurationException(Msg.code(564) + "Failed to extract subscription extension(s): " + theE.getMessage(), theE);
			}
			retVal.getEmailDetails().setFrom(from);
			retVal.getEmailDetails().setSubjectTemplate(subjectTemplate);
		}

		if (retVal.getChannelType() == CanonicalSubscriptionChannelType.RESTHOOK) {
			String stripVersionIds;
			String deliverLatestVersion;
			try {
				stripVersionIds = getExtensionString(subscription, HapiExtensions.EXT_SUBSCRIPTION_RESTHOOK_STRIP_VERSION_IDS);
				deliverLatestVersion = getExtensionString(subscription, HapiExtensions.EXT_SUBSCRIPTION_RESTHOOK_DELIVER_LATEST_VERSION);
			} catch (FHIRException theE) {
				throw new ConfigurationException(Msg.code(565) + "Failed to extract subscription extension(s): " + theE.getMessage(), theE);
			}
			retVal.getRestHookDetails().setStripVersionId(Boolean.parseBoolean(stripVersionIds));
			retVal.getRestHookDetails().setDeliverLatestVersion(Boolean.parseBoolean(deliverLatestVersion));
		}

		List<org.hl7.fhir.r5.model.Extension> topicExts = subscription.getExtensionsByUrl("http://hl7.org/fhir/subscription/topics");
		if (topicExts.size() > 0) {
			IBaseReference ref = (IBaseReference) topicExts.get(0).getValueAsPrimitive();
			if (!"EventDefinition".equals(ref.getReferenceElement().getResourceType())) {
				throw new PreconditionFailedException(Msg.code(566) + "Topic reference must be an EventDefinition");
			}
		}

		return retVal;
	}

	private void setPartitionIdOnReturnValue(IBaseResource theSubscription, CanonicalSubscription retVal) {
		RequestPartitionId requestPartitionId = (RequestPartitionId) theSubscription.getUserData(Constants.RESOURCE_PARTITION_ID);
		if (requestPartitionId != null) {
			retVal.setPartitionId(requestPartitionId.getFirstPartitionIdOrNull());
		}
	}

	private String getExtensionString(IBaseHasExtensions theBase, String theUrl) {
		return theBase
			.getExtension()
			.stream()
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
				String channelTypeCode = ((ca.uhn.fhir.model.dstu2.resource.Subscription) theSubscription).getChannel().getType();
				retVal = CanonicalSubscriptionChannelType.fromCode(null, channelTypeCode);
				break;
			}
			case DSTU3: {
				org.hl7.fhir.dstu3.model.Subscription.SubscriptionChannelType type = ((org.hl7.fhir.dstu3.model.Subscription) theSubscription).getChannel().getType();
				if (type != null) {
					String channelTypeCode = type.toCode();
					retVal = CanonicalSubscriptionChannelType.fromCode(null, channelTypeCode);
				}
				break;
			}
			case R4: {
				org.hl7.fhir.r4.model.Subscription.SubscriptionChannelType type = ((org.hl7.fhir.r4.model.Subscription) theSubscription).getChannel().getType();
				if (type != null) {
					String channelTypeCode = type.toCode();
					retVal = CanonicalSubscriptionChannelType.fromCode(null, channelTypeCode);
				}
				break;
			}
			case R5: {
				org.hl7.fhir.r5.model.Coding nextTypeCode = ((org.hl7.fhir.r5.model.Subscription) theSubscription).getChannelType();
				CanonicalSubscriptionChannelType code = CanonicalSubscriptionChannelType.fromCode(nextTypeCode.getSystem(), nextTypeCode.getCode());
				if (code != null) {
					retVal = code;
				}
				break;
			}
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
			case R5:
				org.hl7.fhir.r5.model.Subscription subscription = (org.hl7.fhir.r5.model.Subscription) theSubscription;
				String topicElement = subscription.getTopicElement().getValue();
				org.hl7.fhir.r5.model.SubscriptionTopic topic = (org.hl7.fhir.r5.model.SubscriptionTopic) subscription.getContained().stream().filter(t -> ("#" + t.getId()).equals(topicElement) || (t.getId()).equals(topicElement)).findFirst().orElse(null);
				if (topic == null) {
					ourLog.warn("Missing contained subscription topic in R5 subscription");
					return null;
				}
				retVal = topic.getResourceTriggerFirstRep().getQueryCriteria().getCurrent();
				break;
		}

		return retVal;
	}


	public void setMatchingStrategyTag(@Nonnull IBaseResource theSubscription, @Nullable SubscriptionMatchingStrategy theStrategy) {
		IBaseMetaType meta = theSubscription.getMeta();

		// Remove any existing strategy tag
		meta
			.getTag()
			.stream()
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
		} else {
			throw new IllegalStateException(Msg.code(567) + "Unknown " + SubscriptionMatchingStrategy.class.getSimpleName() + ": " + theStrategy);
		}
		meta.addTag().setSystem(HapiExtensions.EXT_SUBSCRIPTION_MATCHING_STRATEGY).setCode(value).setDisplay(display);
	}

	public String getSubscriptionStatus(IBaseResource theSubscription) {
		final IPrimitiveType<?> status = myFhirContext.newTerser().getSingleValueOrNull(theSubscription, SubscriptionConstants.SUBSCRIPTION_STATUS, IPrimitiveType.class);
		if (status == null) {
			return null;
		}
		return status.getValueAsString();
	}

}
