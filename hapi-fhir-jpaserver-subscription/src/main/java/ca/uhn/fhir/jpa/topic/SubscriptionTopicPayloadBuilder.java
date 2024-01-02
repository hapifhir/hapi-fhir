/*-
 * #%L
 * HAPI FHIR Subscription Server
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
package ca.uhn.fhir.jpa.topic;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.subscription.match.registry.ActiveSubscription;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.jpa.topic.status.INotificationStatusBuilder;
import ca.uhn.fhir.jpa.topic.status.R4BNotificationStatusBuilder;
import ca.uhn.fhir.jpa.topic.status.R4NotificationStatusBuilder;
import ca.uhn.fhir.jpa.topic.status.R5NotificationStatusBuilder;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.util.BundleBuilder;
import org.apache.commons.lang3.ObjectUtils;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.hl7.fhir.r5.model.Subscription.SubscriptionPayloadContent.FULLRESOURCE;

public class SubscriptionTopicPayloadBuilder {
	private static final Logger ourLog = LoggerFactory.getLogger(SubscriptionTopicPayloadBuilder.class);
	private final FhirContext myFhirContext;
	private final FhirVersionEnum myFhirVersion;
	private final INotificationStatusBuilder<? extends IBaseResource> myNotificationStatusBuilder;

	public SubscriptionTopicPayloadBuilder(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
		myFhirVersion = myFhirContext.getVersion().getVersion();

		switch (myFhirVersion) {
			case R4:
				myNotificationStatusBuilder = new R4NotificationStatusBuilder(myFhirContext);
				break;
			case R4B:
				myNotificationStatusBuilder = new R4BNotificationStatusBuilder(myFhirContext);
				break;
			case R5:
				myNotificationStatusBuilder = new R5NotificationStatusBuilder(myFhirContext);
				break;
			default:
				throw unsupportedFhirVersionException();
		}
	}

	public IBaseBundle buildPayload(
			List<IBaseResource> theResources,
			ActiveSubscription theActiveSubscription,
			String theTopicUrl,
			RestOperationTypeEnum theRestOperationType) {
		BundleBuilder bundleBuilder = new BundleBuilder(myFhirContext);

		IBaseResource notificationStatus =
				myNotificationStatusBuilder.buildNotificationStatus(theResources, theActiveSubscription, theTopicUrl);
		bundleBuilder.addCollectionEntry(notificationStatus);

		addResources(theResources, theActiveSubscription.getSubscription(), theRestOperationType, bundleBuilder);
		// WIP STR5 add support for notificationShape include, revinclude

		// Note we need to set the bundle type after we add the resources since adding the resources automatically sets
		// the bundle type
		setBundleType(bundleBuilder);
		IBaseBundle retval = bundleBuilder.getBundle();
		if (ourLog.isDebugEnabled()) {
			String bundle = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(retval);
			ourLog.debug("Bundle: {}", bundle);
		}
		return retval;
	}

	private void addResources(
			List<IBaseResource> theResources,
			CanonicalSubscription theCanonicalSubscription,
			RestOperationTypeEnum theRestOperationType,
			BundleBuilder theBundleBuilder) {

		org.hl7.fhir.r5.model.Subscription.SubscriptionPayloadContent content =
				ObjectUtils.defaultIfNull(theCanonicalSubscription.getContent(), FULLRESOURCE);

		switch (content) {
			case EMPTY:
				// skip adding resource to the Bundle
				break;
			case IDONLY:
				addIdOnly(theBundleBuilder, theResources, theRestOperationType);
				break;
			case FULLRESOURCE:
				addFullResources(theBundleBuilder, theResources, theRestOperationType);
				break;
		}
	}

	private void addIdOnly(
			BundleBuilder bundleBuilder, List<IBaseResource> theResources, RestOperationTypeEnum theRestOperationType) {
		for (IBaseResource resource : theResources) {
			switch (theRestOperationType) {
				case CREATE:
					bundleBuilder.addTransactionCreateEntryIdOnly(resource);
					break;
				case UPDATE:
					bundleBuilder.addTransactionUpdateIdOnlyEntry(resource);
					break;
				case DELETE:
					bundleBuilder.addTransactionDeleteEntry(resource);
					break;
			}
		}
	}

	private void addFullResources(
			BundleBuilder bundleBuilder, List<IBaseResource> theResources, RestOperationTypeEnum theRestOperationType) {
		for (IBaseResource resource : theResources) {
			switch (theRestOperationType) {
				case CREATE:
					bundleBuilder.addTransactionCreateEntry(resource);
					break;
				case UPDATE:
					bundleBuilder.addTransactionUpdateEntry(resource);
					break;
				case DELETE:
					bundleBuilder.addTransactionDeleteEntry(resource);
					break;
			}
		}
	}

	private void setBundleType(BundleBuilder bundleBuilder) {
		switch (myFhirVersion) {
			case R4:
			case R4B:
				bundleBuilder.setType(Bundle.BundleType.HISTORY.toCode());
				break;
			case R5:
				bundleBuilder.setType(Bundle.BundleType.SUBSCRIPTIONNOTIFICATION.toCode());
				break;
			default:
				throw unsupportedFhirVersionException();
		}
	}

	private IllegalStateException unsupportedFhirVersionException() {
		return new IllegalStateException(
				Msg.code(2331) + "SubscriptionTopic subscriptions are not supported on FHIR version: " + myFhirVersion);
	}
}
