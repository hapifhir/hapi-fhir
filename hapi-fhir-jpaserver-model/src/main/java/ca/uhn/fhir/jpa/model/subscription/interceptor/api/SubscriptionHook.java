package ca.uhn.fhir.jpa.model.subscription.interceptor.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation should be placed on
 * {@link SubscriptionInterceptor Subscription Interceptor}
 * bean methods.
 * <p>
 *     Methods with this annotation are invoked immediately before a REST HOOK
 *     subscription delivery
 * </p>
 *
 * @see SubscriptionInterceptor
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SubscriptionHook {

	/**
	 * Provides the specific point where this method should be invoked
	 */
	Pointcut[] value();

}
