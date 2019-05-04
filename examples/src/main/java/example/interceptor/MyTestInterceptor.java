package example.interceptor;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.subscription.module.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.module.subscriber.ResourceDeliveryMessage;

/**
 * Interceptor class
 */
@Interceptor
public class MyTestInterceptor {

	@Hook(Pointcut.SUBSCRIPTION_BEFORE_REST_HOOK_DELIVERY)
	public boolean beforeRestHookDelivery(ResourceDeliveryMessage theDeliveryMessage, CanonicalSubscription theSubscription) {

		String header = "Authorization: Bearer 1234567";

		theSubscription.addHeader(header);

		return true;
	}

}
