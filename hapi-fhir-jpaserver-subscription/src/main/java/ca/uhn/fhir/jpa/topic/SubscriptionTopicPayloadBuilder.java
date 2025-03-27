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
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.ResourceSearch;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.subscription.match.registry.ActiveSubscription;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.jpa.topic.status.INotificationStatusBuilder;
import ca.uhn.fhir.jpa.topic.status.R4BNotificationStatusBuilder;
import ca.uhn.fhir.jpa.topic.status.R4NotificationStatusBuilder;
import ca.uhn.fhir.jpa.topic.status.R5NotificationStatusBuilder;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.util.BundleBuilder;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.ObjectUtils;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.StringType;
import org.hl7.fhir.r5.model.SubscriptionTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static ca.uhn.fhir.rest.api.Constants.PARAM_ID;
import static ca.uhn.fhir.rest.api.Constants.PARAM_INCLUDE;
import static ca.uhn.fhir.rest.api.Constants.PARAM_REVINCLUDE;
import static org.hl7.fhir.r5.model.Subscription.SubscriptionPayloadContent.FULLRESOURCE;

public class SubscriptionTopicPayloadBuilder {
	private static final Logger ourLog = LoggerFactory.getLogger(SubscriptionTopicPayloadBuilder.class);
	private final FhirContext myFhirContext;
	private final FhirVersionEnum myFhirVersion;
	private final INotificationStatusBuilder<? extends IBaseResource> myNotificationStatusBuilder;

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	private SubscriptionTopicRegistry mySubscriptionTopicRegistry;

	@Autowired
	private MatchUrlService myMatchUrlService;

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

		// Handle notification shape includes and revIncludes
		Set<IBaseResource> notificationShapeResources = getNotificationShapeResources(theResources, theTopicUrl);
		for (IBaseResource resource : notificationShapeResources) {
			bundleBuilder.addCollectionEntry(resource);
		}

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

	private Set<IBaseResource> getNotificationShapeResources(List<IBaseResource> theResources, String theTopicUrl) {

		Set<IBaseResource> resultResources = new HashSet<>();

		if (theResources.isEmpty() || mySubscriptionTopicRegistry == null) {
			return Set.of();
		}

		// Get the topic from the registry
		Optional<SubscriptionTopic> oTopic = mySubscriptionTopicRegistry.findSubscriptionTopicByUrl(theTopicUrl);
		if (oTopic.isEmpty()) {
			ourLog.debug("No subscription topic found for URL: {}", theTopicUrl);
			return Set.of();
		}
		SubscriptionTopic topic = oTopic.get();

		// Process each notification shape
		for (SubscriptionTopic.SubscriptionTopicNotificationShapeComponent shape : topic.getNotificationShape()) {
			String resourceType = shape.getResource();

			// If the resourceType doesn't match any of our resources, skip it
			List<IBaseResource> resourcesOfThisType = theResources.stream()
					.filter(r -> myFhirContext.getResourceType(r).equals(resourceType))
					.collect(Collectors.toList());

			if (resourcesOfThisType.isEmpty()) {
				continue;
			}

			List<StringType> include = shape.getInclude();
			List<StringType> revInclude = shape.getRevInclude();

			Set<IBaseResource> includedResources =
					getIncludedResources(resourceType, resourcesOfThisType, include, revInclude);
			resultResources.addAll(includedResources);
		}

		return resultResources;
	}

	private Set<IBaseResource> getIncludedResources(
			String theResourceType,
			List<IBaseResource> theResourcesOfThisType,
			List<StringType> theIncludes,
			List<StringType> theRevIncludes) {
		Set<IBaseResource> resultResources = new HashSet<>();
		IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(theResourceType);

		for (IBaseResource resource : theResourcesOfThisType) {
			StringBuilder query = new StringBuilder(theResourceType + "?" + PARAM_ID + "="
				+ resource.getIdElement().getIdPart());
			for (StringType include : theIncludes) {
				query.append("&" + PARAM_INCLUDE + "=").append(include.getValue());
			}
			for (StringType revInclude : theRevIncludes) {
				query.append("&" + PARAM_REVINCLUDE + "=").append(revInclude.getValue());
			}
			ResourceSearch resourceSearch =
					myMatchUrlService.getResourceSearchWithIncludesAndRevIncludes(query.toString());
			SearchParameterMap map = resourceSearch.getSearchParameterMap();
			map.setLoadSynchronous(true);
			SystemRequestDetails systemRequestDetails = buildSystemRequestDetails(resource);
			IBundleProvider result = dao.search(map, systemRequestDetails);
			result.getAllResources().stream()
					.filter(r -> !r.getIdElement()
							.toUnqualifiedVersionless()
							.equals(resource.getIdElement().toUnqualifiedVersionless()))
					.forEach(resultResources::add);
		}

		return resultResources;
	}

	@Nonnull
	private static SystemRequestDetails buildSystemRequestDetails(IBaseResource resource) {
		SystemRequestDetails systemRequestDetails = new SystemRequestDetails();
		// TODO KHS might need to check cross-partition subscription settings here
		RequestPartitionId requestPartitionId =
				(RequestPartitionId) resource.getUserData(Constants.RESOURCE_PARTITION_ID);
		if (requestPartitionId == null) {
			systemRequestDetails.setRequestPartitionId(RequestPartitionId.allPartitions());
		} else {
			systemRequestDetails.setRequestPartitionId(requestPartitionId);
		}
		return systemRequestDetails;
	}

	private void addResources(
			List<IBaseResource> theResources,
			CanonicalSubscription theCanonicalSubscription,
			RestOperationTypeEnum theRestOperationType,
			BundleBuilder theBundleBuilder) {

		org.hl7.fhir.r5.model.Subscription.SubscriptionPayloadContent content =
				ObjectUtils.defaultIfNull(theCanonicalSubscription.getContent(), FULLRESOURCE);

		switch (content) {
			case IDONLY:
				addIdOnly(theBundleBuilder, theResources, theRestOperationType);
				break;
			case FULLRESOURCE:
				addFullResources(theBundleBuilder, theResources, theRestOperationType);
				break;
			case EMPTY:
			default:
				// skip adding resource to the Bundle
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
				default:
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
				default:
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
