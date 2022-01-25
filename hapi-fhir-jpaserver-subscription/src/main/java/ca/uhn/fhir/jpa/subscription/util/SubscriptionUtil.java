package ca.uhn.fhir.jpa.subscription.util;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.entity.PartitionablePartitionId;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.rest.api.server.RequestDetails;

/**
 * Utilities for working with the subscription resource
 */
public class SubscriptionUtil {

	public static RequestDetails createRequestDetailForPartitionedRequest(CanonicalSubscription theSubscription) {
		RequestPartitionId requestPartitionId = new PartitionablePartitionId(theSubscription.getRequestPartitionId(), null).toPartitionId();

		if (theSubscription.getCrossPartitionEnabled()) {
			requestPartitionId = RequestPartitionId.allPartitions();
		}

		return new SystemRequestDetails().setRequestPartitionId(requestPartitionId);
	}
}
