package ca.uhn.fhir.jpa.subscription.module.interceptor;

import ca.uhn.fhir.jpa.model.interceptor.api.Hook;
import ca.uhn.fhir.jpa.model.interceptor.api.Interceptor;
import ca.uhn.fhir.jpa.model.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.subscription.module.ResourceModifiedMessage;
import ca.uhn.fhir.jpa.subscription.module.matcher.SubscriptionMatchResult;
import ca.uhn.fhir.jpa.subscription.module.subscriber.ResourceDeliveryMessage;
import ca.uhn.fhir.util.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.util.Date;
import java.util.EnumMap;
import java.util.function.Function;

/**
 * This interceptor can be used for troubleshooting subscription processing. It provides very
 * detailed logging about the subscription processing pipeline.
 * <p>
 * This interceptor loges each step in the processing pipeline with a
 * different event code, using the event codes itemized in
 * {@link EventCodeEnum}. By default these are each placed in a logger with
 * a different name (e.g. <code>ca.uhn.fhir.jpa.subscription.module.interceptor.SubscriptionDebugLogInterceptor.SUBS20</code>
 * in order to facilitate fine-grained logging controls where some codes are omitted and
 * some are not.
 * </p>
 * <p>
 * A custom log factory can also be passed in, in which case the logging
 * creation may use another strategy.
 * </p>
 *
 * @since 3.7.0
 * @see EventCodeEnum
 */
@Interceptor
public class SubscriptionDebugLogInterceptor {

	private static final String SUBSCRIPTION_DEBUG_LOG_INTERCEPTOR_PRECHECK = "SubscriptionDebugLogInterceptor_precheck";
	private final Level myLevel;
	private final EnumMap<EventCodeEnum, Logger> myLoggers;

	/**
	 * Constructor that logs at INFO level to the logger <code>ca.uhn.fhir.jpa.subscription.module.interceptor.SubscriptionDebugLogInterceptor</code>
	 */
	public SubscriptionDebugLogInterceptor() {
		this(defaultLogFactory(), Level.INFO);
	}

	/**
	 * Constructor using a specific logger
	 */
	public SubscriptionDebugLogInterceptor(Function<EventCodeEnum, Logger> theLogFactory, Level theLevel) {
		myLevel = theLevel;
		myLoggers = new EnumMap<>(EventCodeEnum.class);
		for (EventCodeEnum next : EventCodeEnum.values()) {
			myLoggers.put(next, theLogFactory.apply(next));
		}
	}

	@Hook(Pointcut.SUBSCRIPTION_RESOURCE_MODIFIED)
	public void step10_resourceModified(ResourceModifiedMessage theMessage) {
		String value = Long.toString(System.currentTimeMillis());
		theMessage.setAttribute(SUBSCRIPTION_DEBUG_LOG_INTERCEPTOR_PRECHECK, value);
		log(EventCodeEnum.SUBS10, "Resource {} was submitted to the processing pipeline (op={})", theMessage.getPayloadId(), theMessage.getOperationType());
	}

	/*
	 * These methods are numbered in the order that an individual
	 * resource would go through them, for clarity and ease of
	 * tracing when debugging and poring over logs.
	 *
	 * I don't know if this numbering scheme makes sense.. I'm incrementing
	 * by 10 for each step in the normal delivery pipeline, leaving lots of
	 * gaps to add things if we ever need them.
	 */

	@Hook(Pointcut.SUBSCRIPTION_BEFORE_PERSISTED_RESOURCE_CHECKED)
	public void step20_beforeChecked(ResourceModifiedMessage theMessage) {
		log(EventCodeEnum.SUBS20, "Checking resource {} (op={}) for matching subscriptions", theMessage.getPayloadId(), theMessage.getOperationType());
	}

	@Hook(Pointcut.SUBSCRIPTION_RESOURCE_MATCHED)
	public void step30_subscriptionMatched(ResourceDeliveryMessage theMessage, SubscriptionMatchResult theResult) {
		log(EventCodeEnum.SUBS30, "Resource {} matched by subscription {} (memory match={})", theMessage.getPayloadId(), theMessage.getSubscription().getIdElementString(), theResult.isInMemory());
	}

	@Hook(Pointcut.SUBSCRIPTION_RESOURCE_DID_NOT_MATCH_ANY_SUBSCRIPTIONS)
	public void step35_subscriptionNotMatched(ResourceModifiedMessage theMessage) {
		log(EventCodeEnum.SUBS35, "Resource {} did not match any subscriptions", theMessage.getPayloadId());
	}

	@Hook(Pointcut.SUBSCRIPTION_BEFORE_DELIVERY)
	public void step40_beforeDelivery(ResourceDeliveryMessage theMessage) {
		log(EventCodeEnum.SUBS40, "Delivering resource {} for subscription {} to channel of type {} to endpoint {}", theMessage.getPayloadId(), theMessage.getSubscription().getIdElementString(), theMessage.getSubscription().getChannelType(), theMessage.getSubscription().getEndpointUrl());
	}

	@Hook(Pointcut.SUBSCRIPTION_AFTER_DELIVERY_FAILED)
	public void step45_deliveryFailed(ResourceDeliveryMessage theMessage, Exception theFailure) {
		log(EventCodeEnum.SUBS45, "Delivery of resource {} for subscription {} to channel of type {} - Failure: {}", theMessage.getPayloadId(), theMessage.getSubscription().getIdElementString(), theMessage.getSubscription().getChannelType(), theFailure.toString());
	}

	@Hook(Pointcut.SUBSCRIPTION_AFTER_DELIVERY)
	public void step50_afterDelivery(ResourceDeliveryMessage theMessage) {
		String processingTime = theMessage
			.getAttribute(SUBSCRIPTION_DEBUG_LOG_INTERCEPTOR_PRECHECK)
			.map(Long::parseLong)
			.map(Date::new)
			.map(start -> new StopWatch(start).toString())
			.orElse("(unknown)");

		log(EventCodeEnum.SUBS50, "Finished delivery of resource {} for subscription {} to channel of type {} - Total processing time: {}", theMessage.getPayloadId(), theMessage.getSubscription().getIdElementString(), theMessage.getSubscription().getChannelType(), processingTime);
	}

	protected void log(EventCodeEnum theEventCode, String theMessage, Object... theArguments) {
		Logger logger = myLoggers.get(theEventCode);
		if (logger != null) {
			switch (myLevel) {
				case ERROR:
					logger.error(theMessage, theArguments);
					break;
				case WARN:
					logger.warn(theMessage, theArguments);
					break;
				case INFO:
					logger.info(theMessage, theArguments);
					break;
				case DEBUG:
					logger.debug(theMessage, theArguments);
					break;
				case TRACE:
					logger.trace(theMessage, theArguments);
					break;
			}
		}
	}

	public enum EventCodeEnum {
		/**
		 * A new/updated resource has been submitted to the processing pipeline and is about
		 * to be placed on the matchign queue.
		 */
		SUBS10,
		/**
		 * A resources has been dequeued from the matching queue and is about to be checked
		 * for any matching subscriptions.
		 */
		SUBS20,
		/**
		 * The resource has matched a subscription (logged once for each matching subscription)
		 * and is about to be queued for delivery.
		 */
		SUBS30,
		/**
		 * The resource did not match any subscriptions and processing is complete.
		 */
		SUBS35,
		/**
		 * The resource has been dequeued from the delivery queue and is about to be
		 * delivered.
		 */
		SUBS40,
		/**
		 * Delivery failed
		 */
		SUBS45,
		/**
		 * Delivery is now complete and processing is finished.
		 */
		SUBS50
	}


	private static Function<EventCodeEnum, Logger> defaultLogFactory() {
		return code -> LoggerFactory.getLogger(SubscriptionDebugLogInterceptor.class.getName() + "." + code.name());
	}

}
