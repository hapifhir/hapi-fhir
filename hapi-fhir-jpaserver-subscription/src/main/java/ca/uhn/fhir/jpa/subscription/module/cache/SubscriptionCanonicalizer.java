package ca.uhn.fhir.jpa.subscription.module.cache;

/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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
import ca.uhn.fhir.jpa.subscription.module.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.module.CanonicalSubscriptionChannelType;
import ca.uhn.fhir.jpa.subscription.module.matcher.SubscriptionMatchingStrategy;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseMetaType;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

@Service
public class SubscriptionCanonicalizer<S extends IBaseResource> {
	private static final Logger ourLog = LoggerFactory.getLogger(SubscriptionCanonicalizer.class);

	final FhirContext myFhirContext;

	@Autowired
	public SubscriptionCanonicalizer(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
	}

	public CanonicalSubscription canonicalize(S theSubscription) {
		switch (myFhirContext.getVersion().getVersion()) {
			case DSTU2:
				return canonicalizeDstu2(theSubscription);
			case DSTU3:
				return canonicalizeDstu3(theSubscription);
			case R4:
				return canonicalizeR4(theSubscription);
			case DSTU2_HL7ORG:
			case DSTU2_1:
			default:
				throw new ConfigurationException("Subscription not supported for version: " + myFhirContext.getVersion().getVersion());
		}
	}

	private CanonicalSubscription canonicalizeDstu2(IBaseResource theSubscription) {
		ca.uhn.fhir.model.dstu2.resource.Subscription subscription = (ca.uhn.fhir.model.dstu2.resource.Subscription) theSubscription;

		CanonicalSubscription retVal = new CanonicalSubscription();
		try {
			retVal.setStatus(org.hl7.fhir.r4.model.Subscription.SubscriptionStatus.fromCode(subscription.getStatus()));
			retVal.setChannelType(CanonicalSubscriptionChannelType.fromCode(subscription.getChannel().getType()));
			retVal.setCriteriaString(subscription.getCriteria());
			retVal.setEndpointUrl(subscription.getChannel().getEndpoint());
			retVal.setHeaders(subscription.getChannel().getHeader());
			retVal.setChannelExtensions(extractExtension(subscription));
			retVal.setIdElement(subscription.getIdElement());
			retVal.setPayloadString(subscription.getChannel().getPayload());
		} catch (FHIRException theE) {
			throw new InternalErrorException(theE);
		}
		return retVal;
	}

	private CanonicalSubscription canonicalizeDstu3(IBaseResource theSubscription) {
		org.hl7.fhir.dstu3.model.Subscription subscription = (org.hl7.fhir.dstu3.model.Subscription) theSubscription;

		CanonicalSubscription retVal = new CanonicalSubscription();
		try {
			retVal.setStatus(org.hl7.fhir.r4.model.Subscription.SubscriptionStatus.fromCode(subscription.getStatus().toCode()));
			retVal.setChannelType(CanonicalSubscriptionChannelType.fromCode(subscription.getChannel().getType().toCode()));
			retVal.setCriteriaString(subscription.getCriteria());
			retVal.setEndpointUrl(subscription.getChannel().getEndpoint());
			retVal.setHeaders(subscription.getChannel().getHeader());
			retVal.setChannelExtensions(extractExtension(subscription));
			retVal.setIdElement(subscription.getIdElement());
			retVal.setPayloadString(subscription.getChannel().getPayload());

			if (retVal.getChannelType() == CanonicalSubscriptionChannelType.EMAIL) {
				String from;
				String subjectTemplate;

				try {
					from = subscription.getChannel().getExtensionString(SubscriptionConstants.EXT_SUBSCRIPTION_EMAIL_FROM);
					subjectTemplate = subscription.getChannel().getExtensionString(SubscriptionConstants.EXT_SUBSCRIPTION_SUBJECT_TEMPLATE);
				} catch (FHIRException theE) {
					throw new ConfigurationException("Failed to extract subscription extension(s): " + theE.getMessage(), theE);
				}
				retVal.getEmailDetails().setFrom(from);
				retVal.getEmailDetails().setSubjectTemplate(subjectTemplate);
			}

			if (retVal.getChannelType() == CanonicalSubscriptionChannelType.RESTHOOK) {

				String stripVersionIds;
				String deliverLatestVersion;
				try {
					stripVersionIds = subscription.getChannel().getExtensionString(SubscriptionConstants.EXT_SUBSCRIPTION_RESTHOOK_STRIP_VERSION_IDS);
					deliverLatestVersion = subscription.getChannel().getExtensionString(SubscriptionConstants.EXT_SUBSCRIPTION_RESTHOOK_DELIVER_LATEST_VERSION);
				} catch (FHIRException theE) {
					throw new ConfigurationException("Failed to extract subscription extension(s): " + theE.getMessage(), theE);
				}
				retVal.getRestHookDetails().setStripVersionId(Boolean.parseBoolean(stripVersionIds));
				retVal.getRestHookDetails().setDeliverLatestVersion(Boolean.parseBoolean(deliverLatestVersion));
			}

		} catch (FHIRException theE) {
			throw new InternalErrorException(theE);
		}
		return retVal;
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
		retVal.setChannelType(CanonicalSubscriptionChannelType.fromCode(subscription.getChannel().getType().toCode()));
		retVal.setCriteriaString(subscription.getCriteria());
		retVal.setEndpointUrl(subscription.getChannel().getEndpoint());
		retVal.setHeaders(subscription.getChannel().getHeader());
		retVal.setChannelExtensions(extractExtension(subscription));
		retVal.setIdElement(subscription.getIdElement());
		retVal.setPayloadString(subscription.getChannel().getPayload());

		if (retVal.getChannelType() == CanonicalSubscriptionChannelType.EMAIL) {
			String from;
			String subjectTemplate;
			try {
				from = subscription.getChannel().getExtensionString(SubscriptionConstants.EXT_SUBSCRIPTION_EMAIL_FROM);
				subjectTemplate = subscription.getChannel().getExtensionString(SubscriptionConstants.EXT_SUBSCRIPTION_SUBJECT_TEMPLATE);
			} catch (FHIRException theE) {
				throw new ConfigurationException("Failed to extract subscription extension(s): " + theE.getMessage(), theE);
			}
			retVal.getEmailDetails().setFrom(from);
			retVal.getEmailDetails().setSubjectTemplate(subjectTemplate);
		}

		if (retVal.getChannelType() == CanonicalSubscriptionChannelType.RESTHOOK) {
			String stripVersionIds;
			String deliverLatestVersion;
			try {
				stripVersionIds = subscription.getChannel().getExtensionString(SubscriptionConstants.EXT_SUBSCRIPTION_RESTHOOK_STRIP_VERSION_IDS);
				deliverLatestVersion = subscription.getChannel().getExtensionString(SubscriptionConstants.EXT_SUBSCRIPTION_RESTHOOK_DELIVER_LATEST_VERSION);
			} catch (FHIRException theE) {
				throw new ConfigurationException("Failed to extract subscription extension(s): " + theE.getMessage(), theE);
			}
			retVal.getRestHookDetails().setStripVersionId(Boolean.parseBoolean(stripVersionIds));
			retVal.getRestHookDetails().setDeliverLatestVersion(Boolean.parseBoolean(deliverLatestVersion));
		}

		List<Extension> topicExts = subscription.getExtensionsByUrl("http://hl7.org/fhir/subscription/topics");
		if (topicExts.size() > 0) {
			IBaseReference ref = (IBaseReference) topicExts.get(0).getValueAsPrimitive();
			if (!"EventDefinition".equals(ref.getReferenceElement().getResourceType())) {
				throw new PreconditionFailedException("Topic reference must be an EventDefinition");
			}
		}

		return retVal;
	}

	public String getCriteria(IBaseResource theSubscription) {
		switch (myFhirContext.getVersion().getVersion()) {
			case DSTU2:
				return ((ca.uhn.fhir.model.dstu2.resource.Subscription) theSubscription).getCriteria();
			case DSTU3:
				return ((org.hl7.fhir.dstu3.model.Subscription) theSubscription).getCriteria();
			case R4:
				return ((org.hl7.fhir.r4.model.Subscription) theSubscription).getCriteria();
			case DSTU2_1:
			case DSTU2_HL7ORG:
			default:
				throw new ConfigurationException("Subscription not supported for version: " + myFhirContext.getVersion().getVersion());
		}
	}


	public void setMatchingStrategyTag(IBaseResource theSubscription, SubscriptionMatchingStrategy theStrategy) {
		IBaseMetaType meta = theSubscription.getMeta();
		String value = theStrategy.toString();
		String display;

		if (theStrategy == SubscriptionMatchingStrategy.DATABASE) {
			display = "Database";
		} else if (theStrategy == SubscriptionMatchingStrategy.IN_MEMORY) {
			display = "In-memory";
		} else {
			throw new IllegalStateException("Unknown " + SubscriptionMatchingStrategy.class.getSimpleName() + ": " + theStrategy);
		}
		meta.addTag().setSystem(SubscriptionConstants.EXT_SUBSCRIPTION_MATCHING_STRATEGY).setCode(value).setDisplay(display);
	}

	public String getSubscriptionStatus(IBaseResource theSubscription) {
		final IPrimitiveType<?> status = myFhirContext.newTerser().getSingleValueOrNull(theSubscription, SubscriptionConstants.SUBSCRIPTION_STATUS, IPrimitiveType.class);
		if (status == null) {
			return null;
		}
		return status.getValueAsString();
	}

}
