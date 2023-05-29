package ca.uhn.fhir.jpa.topic.status;

import ca.uhn.fhir.jpa.subscription.match.registry.ActiveSubscription;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.List;

public interface INotificationStatusBuilder<T extends IBaseResource> {
	/**
	 * Build a notification status resource to include as the first element in a topic subscription notification bundle
	 * @param theResources The resources to include in the notification bundle.  It should _NOT_ include the
	 *                       notification status resource.  The first resource will be the "focus" resource.
	 * @param theActiveSubscription	The active subscription that triggered the notification
	 * @param theTopicUrl	The topic URL of the topic subscription
	 * @return the notification status resource.  The resource type varies depending on the FHIR version.
	 */
	T buildNotificationStatus(List<IBaseResource> theResources, ActiveSubscription theActiveSubscription, String theTopicUrl);
}
