/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.topic;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.subscription.SubscriptionConstants;
import org.hl7.fhir.convertors.factory.VersionConvertorFactory_43_50;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Basic;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r5.model.DateTimeType;
import org.hl7.fhir.r5.model.Enumerations.PublicationStatus;
import org.hl7.fhir.r5.model.SubscriptionTopic;
import org.hl7.fhir.r5.model.SubscriptionTopic.InteractionTrigger;
import org.hl7.fhir.r5.model.SubscriptionTopic.SubscriptionTopicCanFilterByComponent;
import org.hl7.fhir.r5.model.SubscriptionTopic.SubscriptionTopicNotificationShapeComponent;
import org.hl7.fhir.r5.model.SubscriptionTopic.SubscriptionTopicResourceTriggerComponent;

public final class SubscriptionTopicCanonicalizer {
	private static final FhirContext ourFhirContextR5 = FhirContext.forR5();
	
	private SubscriptionTopicCanonicalizer() {}

	public static SubscriptionTopic canonicalizeTopic(FhirContext theFhirContext, IBaseResource theSubscriptionTopic) {
		switch (theFhirContext.getVersion().getVersion()) {
			case R4:
				if (theSubscriptionTopic instanceof Basic) {
					return canonicalizeR4BasicTopic((Basic) theSubscriptionTopic);
				}
				throw new UnsupportedOperationException(
						Msg.code(2337) + "Unsupported R4 resource type for subscription topic: " + 
						theSubscriptionTopic.getClass().getSimpleName());
			case R4B:
				return (SubscriptionTopic) VersionConvertorFactory_43_50.convertResource(
						(org.hl7.fhir.r4b.model.SubscriptionTopic) theSubscriptionTopic);
			case R5:
				return (SubscriptionTopic) theSubscriptionTopic;
			default:
				throw new UnsupportedOperationException(
						Msg.code(2337) + "Subscription topics are not supported in FHIR version "
								+ theFhirContext.getVersion().getVersion());
		}
	}
	
	private static SubscriptionTopic canonicalizeR4BasicTopic(Basic theBasicTopic) {
		SubscriptionTopic retVal = new SubscriptionTopic();
		
		// Basic properties
		retVal.setId(theBasicTopic.getIdElement().getIdPart());
		
		// Extract regular extensions
		for (Extension extension : theBasicTopic.getExtension()) {
			String url = extension.getUrl();
			
			if (SubscriptionConstants.SUBSCRIPTION_TOPIC_R4_EXT_TOPIC_URL.equals(url)) {
				retVal.setUrl(extension.getValue().primitiveValue());
			} else if (SubscriptionConstants.SUBSCRIPTION_TOPIC_R4_EXT_TOPIC_VERSION.equals(url)) {
				retVal.setVersion(extension.getValue().primitiveValue());
			} else if (SubscriptionConstants.SUBSCRIPTION_TOPIC_R4_EXT_TOPIC_NAME.equals(url)) {
				retVal.setName(extension.getValue().primitiveValue());
			} else if (SubscriptionConstants.SUBSCRIPTION_TOPIC_R4_EXT_TOPIC_TITLE.equals(url)) {
				retVal.setTitle(extension.getValue().primitiveValue());
			} else if (SubscriptionConstants.SUBSCRIPTION_TOPIC_R4_EXT_TOPIC_DATE.equals(url)) {
				retVal.setDateElement(new DateTimeType(extension.getValue().primitiveValue()));
			} else if (SubscriptionConstants.SUBSCRIPTION_TOPIC_R4_EXT_TOPIC_DESCRIPTION.equals(url)) {
				retVal.setDescription(extension.getValue().primitiveValue());
			} else if (SubscriptionConstants.SUBSCRIPTION_TOPIC_R4_EXT_RESOURCE_TRIGGER.equals(url)) {
				processResourceTrigger(extension, retVal);
			} else if (SubscriptionConstants.SUBSCRIPTION_TOPIC_R4_EXT_CAN_FILTER_BY.equals(url)) {
				processCanFilterBy(extension, retVal);
			} else if (SubscriptionConstants.SUBSCRIPTION_TOPIC_R4_EXT_NOTIFICATION_SHAPE.equals(url)) {
				processNotificationShape(extension, retVal);
			}
		}
		
		// Extract modifier extensions (e.g., status)
		for (Extension extension : theBasicTopic.getModifierExtension()) {
			String url = extension.getUrl();
			
			if (SubscriptionConstants.SUBSCRIPTION_TOPIC_R4_EXT_TOPIC_STATUS.equals(url)) {
				String statusCode = extension.getValue().primitiveValue();
				retVal.setStatus(PublicationStatus.fromCode(statusCode));
			}
		}
		
		return retVal;
	}
	
	private static void processResourceTrigger(Extension theExtension, SubscriptionTopic theTopic) {
		SubscriptionTopicResourceTriggerComponent trigger = new SubscriptionTopicResourceTriggerComponent();
		
		for (Extension ext : theExtension.getExtension()) {
			String url = ext.getUrl();
			
			if (SubscriptionConstants.SUBSCRIPTION_TOPIC_R4_EXT_DESCRIPTION.equals(url)) {
				trigger.setDescription(ext.getValue().primitiveValue());
			} else if (SubscriptionConstants.SUBSCRIPTION_TOPIC_R4_EXT_RESOURCE.equals(url)) {
				trigger.setResource(ext.getValue().primitiveValue());
			} else if (SubscriptionConstants.SUBSCRIPTION_TOPIC_R4_EXT_SUPPORTED_INTERACTION.equals(url)) {
				trigger.addSupportedInteraction(
						InteractionTrigger.fromCode(ext.getValue().primitiveValue()));
			} else if (SubscriptionConstants.SUBSCRIPTION_TOPIC_R4_EXT_FHIRPATH_CRITERIA.equals(url)) {
				trigger.setFhirPathCriteria(ext.getValue().primitiveValue());
			}
		}
		
		theTopic.addResourceTrigger(trigger);
	}
	
	private static void processCanFilterBy(Extension theExtension, SubscriptionTopic theTopic) {
		SubscriptionTopicCanFilterByComponent filterBy = new SubscriptionTopicCanFilterByComponent();
		
		for (Extension ext : theExtension.getExtension()) {
			String url = ext.getUrl();
			
			if (SubscriptionConstants.SUBSCRIPTION_TOPIC_R4_EXT_DESCRIPTION.equals(url)) {
				filterBy.setDescription(ext.getValue().primitiveValue());
			} else if (SubscriptionConstants.SUBSCRIPTION_TOPIC_R4_EXT_RESOURCE.equals(url)) {
				filterBy.setResource(ext.getValue().primitiveValue());
			} else if (SubscriptionConstants.SUBSCRIPTION_TOPIC_R4_EXT_FILTER_PARAMETER.equals(url)) {
				filterBy.setFilterParameter(ext.getValue().primitiveValue());
			}
		}
		
		theTopic.addCanFilterBy(filterBy);
	}
	
	private static void processNotificationShape(Extension theExtension, SubscriptionTopic theTopic) {
		SubscriptionTopicNotificationShapeComponent shape = new SubscriptionTopicNotificationShapeComponent();
		
		for (Extension ext : theExtension.getExtension()) {
			String url = ext.getUrl();
			
			if (SubscriptionConstants.SUBSCRIPTION_TOPIC_R4_EXT_RESOURCE.equals(url)) {
				shape.setResource(ext.getValue().primitiveValue());
			} else if (SubscriptionConstants.SUBSCRIPTION_TOPIC_R4_EXT_INCLUDE.equals(url)) {
				shape.addInclude(ext.getValue().primitiveValue());
			} else if (SubscriptionConstants.SUBSCRIPTION_TOPIC_R4_EXT_REVINCLUDE.equals(url)) {
				shape.addRevInclude(ext.getValue().primitiveValue());
			}
		}
		
		theTopic.addNotificationShape(shape);
	}
}
