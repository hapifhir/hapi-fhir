package ca.uhn.fhir.jpa.topic.status;

import ca.uhn.fhir.jpa.subscription.match.registry.ActiveSubscription;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.List;

public interface INotificationStatusBuilder<T extends IBaseResource> {
	T buildNotificationStatus(List<IBaseResource> theResources, ActiveSubscription theActiveSubscription, String theTopicUrl);
}
