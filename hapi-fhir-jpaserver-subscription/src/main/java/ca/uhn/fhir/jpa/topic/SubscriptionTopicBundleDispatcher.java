package ca.uhn.fhir.jpa.topic;

import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryMatchResult;
import ca.uhn.fhir.jpa.subscription.match.matcher.subscriber.SubscriptionMatchDeliverer;
import ca.uhn.fhir.jpa.subscription.match.registry.ActiveSubscription;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionRegistry;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

/**
 * Deliver subscription bundles to topic subscriptions
 */
public class SubscriptionTopicBundleDispatcher {
	private final SubscriptionRegistry mySubscriptionRegistry;
	private final SubscriptionMatchDeliverer mySubscriptionMatchDeliverer;
	private final SubscriptionTopicPayloadBuilder mySubscriptionTopicPayloadBuilder;

	public SubscriptionTopicBundleDispatcher(SubscriptionRegistry theSubscriptionRegistry, SubscriptionMatchDeliverer theSubscriptionMatchDeliverer, SubscriptionTopicPayloadBuilder theSubscriptionTopicPayloadBuilder) {
		mySubscriptionRegistry = theSubscriptionRegistry;
		mySubscriptionMatchDeliverer = theSubscriptionMatchDeliverer;
		mySubscriptionTopicPayloadBuilder = theSubscriptionTopicPayloadBuilder;
	}

	// WIP STR5 R4 backport http://build.fhir.org/ig/HL7/fhir-subscription-backport-ig/components.html

	/**
	 * @param theTopicUrl           Deliver to subscriptions for this topic
	 * @param theResources          The list of resources to deliver.  The first resource will be the primary "focus" resource per the Subscription documentation.
	 *                              This list should _not_ include the SubscriptionStatus.  The SubscriptionStatus will be added as the first element to
	 *                              the delivered bundle.  The reason for this is that the SubscriptionStatus needs to reference the subscription ID, which is
	 *                              not known until the bundle is delivered.
	 * @param theInMemoryMatchResult
	 */
	public void dispatch(@Nonnull String theTopicUrl, @Nonnull List<IBaseResource> theResources, RestOperationTypeEnum theRequestType, @Nullable InMemoryMatchResult theInMemoryMatchResult) {
		List<ActiveSubscription> topicSubscriptions = mySubscriptionRegistry.getTopicSubscriptionsByTopic(theTopicUrl);
		if (!topicSubscriptions.isEmpty()) {
			for (ActiveSubscription activeSubscription : topicSubscriptions) {
				// WIP STR5 apply subscription filters
				IBaseBundle bundlePayload = mySubscriptionTopicPayloadBuilder.buildPayload(theResources, activeSubscription, theTopicUrl, theRequestType);
				// WIP STR5 do we need to add a total?  If so can do that with R5BundleFactory
				bundlePayload.setId(UUID.randomUUID().toString());
				ResourceModifiedMessage resourceModifiedMessage = new ResourceModifiedMessage();
				mySubscriptionMatchDeliverer.deliverPayload(bundlePayload, resourceModifiedMessage, activeSubscription, theInMemoryMatchResult);
			}
		}
	}
}
