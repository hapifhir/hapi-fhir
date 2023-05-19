package ca.uhn.fhir.jpa.topic;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryMatchResult;
import ca.uhn.fhir.jpa.subscription.match.matcher.subscriber.SubscriptionDeliveryRequest;
import ca.uhn.fhir.jpa.subscription.match.matcher.subscriber.SubscriptionMatchDeliverer;
import ca.uhn.fhir.jpa.subscription.match.registry.ActiveSubscription;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionRegistry;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

/**
 * Subscription topic notifications are natively supported in R5, R4B.  They are also partially supported and in R4
 * via the subscription backport spec <a href="http://build.fhir.org/ig/HL7/fhir-subscription-backport-ig/components.html">Subscription Backport</a>.
 * In all versions, it is possible for a FHIR Repository to submit topic subscription notifications triggered by some
 * arbitrary "business event".  In R5 and R4B most subscription topic notifications will be triggered by a SubscriptionTopic
 * match.  However, in the R4 backport, the SubscriptionTopic is not supported and the SubscriptionTopicDispatcher service
 * is provided to generate those notifications instead.  Any custom java extension to the FHIR repository can @Autowire this service to
 * send topic notifications to all Subscription resources subscribed to that topic.
 */
public class SubscriptionTopicDispatcher {
	private final SubscriptionRegistry mySubscriptionRegistry;
	private final SubscriptionMatchDeliverer mySubscriptionMatchDeliverer;
	private final SubscriptionTopicPayloadBuilder mySubscriptionTopicPayloadBuilder;

	public SubscriptionTopicDispatcher(SubscriptionRegistry theSubscriptionRegistry, SubscriptionMatchDeliverer theSubscriptionMatchDeliverer, SubscriptionTopicPayloadBuilder theSubscriptionTopicPayloadBuilder) {
		mySubscriptionRegistry = theSubscriptionRegistry;
		mySubscriptionMatchDeliverer = theSubscriptionMatchDeliverer;
		mySubscriptionTopicPayloadBuilder = theSubscriptionTopicPayloadBuilder;
	}

	/**
	 * Deliver a Subscription topic notification to all subscriptions for the given topic.
	 *
	 * @param theTopicUrl            Deliver to subscriptions for this topic
	 * @param theResources           The list of resources to deliver.  The first resource will be the primary "focus" resource per the Subscription documentation.
	 *                               This list should _not_ include the SubscriptionStatus.  The SubscriptionStatus will be added as the first element to
	 *                               the delivered bundle.  The reason for this is that the SubscriptionStatus needs to reference the subscription ID, which is
	 *                               not known until the bundle is delivered.
	 * @param theInMemoryMatchResult Information about the match event that led to this dispatch that is sent to SUBSCRIPTION_RESOURCE_MATCHED
	 * @param theRequestPartitionId  The request partitions of the request, if any.  This is used by subscriptions that need to perform repository
	 *                               operations as a part of their delivery.  Those repository operations will be performed on the supplied request partitions
	 * @param theTransactionId			The transaction ID of the request, if any.  This is used for logging.
	 * @return 							   The number of subscription notifications that were successfully queued for delivery
	 */
	public int dispatch(@Nonnull String theTopicUrl, @Nonnull List<IBaseResource> theResources, @Nonnull RestOperationTypeEnum theRequestType, @Nullable InMemoryMatchResult theInMemoryMatchResult, @Nullable RequestPartitionId theRequestPartitionId, @Nullable String theTransactionId) {
		int count = 0;

		List<ActiveSubscription> topicSubscriptions = mySubscriptionRegistry.getTopicSubscriptionsByTopic(theTopicUrl);
		if (!topicSubscriptions.isEmpty()) {
			for (ActiveSubscription activeSubscription : topicSubscriptions) {
				// WIP STR5 apply subscription filters
				IBaseBundle bundlePayload = mySubscriptionTopicPayloadBuilder.buildPayload(theResources, activeSubscription, theTopicUrl, theRequestType);
				// WIP STR5 do we need to add a total?  If so can do that with R5BundleFactory
				bundlePayload.setId(UUID.randomUUID().toString());
				SubscriptionDeliveryRequest subscriptionDeliveryRequest = new SubscriptionDeliveryRequest(bundlePayload, activeSubscription, theRequestType, theRequestPartitionId, theTransactionId);
				boolean success = mySubscriptionMatchDeliverer.deliverPayload(subscriptionDeliveryRequest, theInMemoryMatchResult);
				if (success) {
					count++;
				}
			}
		}
		return count;
	}
}
