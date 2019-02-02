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

/**
 * This interceptor can be used for troubleshooting subscription processing. It provides very
 * detailed logging about the subscription processing pipeline.
 *
 * @since 3.7.0
 */
@Interceptor
public class SubscriptionDebugLogInterceptor {

	private static final Logger DEFAULT_LOGGER = LoggerFactory.getLogger(SubscriptionDebugLogInterceptor.class);
	private static final String SUBSCRIPTION_DEBUG_LOG_INTERCEPTOR_PRECHECK = "SubscriptionDebugLogInterceptor_precheck";
	private final Logger myLogger;
	private final Level myLevel;

	/**
	 * Constructor that logs at INFO level to the logger <code>ca.uhn.fhir.jpa.subscription.module.interceptor.SubscriptionDebugLogInterceptor</code>
	 */
	public SubscriptionDebugLogInterceptor() {
		this(DEFAULT_LOGGER, Level.INFO);
	}

	/**
	 * Constructor using a specific logger
	 */
	public SubscriptionDebugLogInterceptor(Logger theLog, Level theLevel) {
		myLogger = theLog;
		myLevel = theLevel;
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

	@Hook(Pointcut.SUBSCRIPTION_RESOURCE_MODIFIED)
	public void step10_resourceModified(ResourceModifiedMessage theMessage) {
		String value = Long.toString(System.currentTimeMillis());
		theMessage.setAdditionalProperty(SUBSCRIPTION_DEBUG_LOG_INTERCEPTOR_PRECHECK, value);
		log("SUBS10","Resource {} was submitted to the processing pipeline", theMessage.getPayloadId(), theMessage.getOperationType());
	}

	@Hook(Pointcut.SUBSCRIPTION_BEFORE_PERSISTED_RESOURCE_CHECKED)
	public void step20_beforeChecked(ResourceModifiedMessage theMessage) {
		log("SUBS20","Checking resource {} (op={}) for matching subscriptions", theMessage.getPayloadId(), theMessage.getOperationType());
	}

	@Hook(Pointcut.SUBSCRIPTION_RESOURCE_MATCHED)
	public void step30_subscriptionMatched(ResourceDeliveryMessage theMessage, SubscriptionMatchResult theResult) {
		log("SUBS30","Resource {} matched by subscription {} (memory match={})", theMessage.getPayloadId(), theMessage.getSubscription().getIdElementString(), theResult.isInMemory());
	}

	@Hook(Pointcut.SUBSCRIPTION_RESOURCE_DID_NOT_MATCH_ANY_SUBSCRIPTIONS)
	public void step35_subscriptionMatched(ResourceModifiedMessage theMessage) {
		log("SUBS35","Resource {} did not match any subscriptions", theMessage.getPayloadId());
	}

	@Hook(Pointcut.SUBSCRIPTION_BEFORE_DELIVERY)
	public void step40_beforeDelivery(ResourceDeliveryMessage theMessage) {
		log("SUBS40","Delivering resource {} for subscription {} to channel of type {} to endpoint {}", theMessage.getPayloadId(), theMessage.getSubscription().getIdElementString(), theMessage.getSubscription().getChannelType(), theMessage.getSubscription().getEndpointUrl());
	}

	@Hook(Pointcut.SUBSCRIPTION_AFTER_DELIVERY)
	public void step50_afterDelivery(ResourceDeliveryMessage theMessage) {
		String processingTime = theMessage
			.getAdditionalProperty(SUBSCRIPTION_DEBUG_LOG_INTERCEPTOR_PRECHECK)
			.map(Long::parseLong)
			.map(Date::new)
			.map(start -> new StopWatch(start).toString())
			.orElse("(unknown)");

		log("SUBS50","Finished delivery of resource {} for subscription {} to channel of type {} - Total processing time: {}", theMessage.getPayloadId(), theMessage.getSubscription().getIdElementString(), theMessage.getSubscription().getChannelType(), processingTime);
	}

	private void log(String theCode, String theMessage, Object... theArguments) {
		String msg = "[" + theCode + "] " + theMessage;
		switch (myLevel) {
			case ERROR:
				myLogger.error(msg, theArguments);
				break;
			case WARN:
				myLogger.warn(msg, theArguments);
				break;
			case INFO:
				myLogger.info(msg, theArguments);
				break;
			case DEBUG:
				myLogger.debug(msg, theArguments);
				break;
			case TRACE:
				myLogger.trace(msg, theArguments);
				break;
		}
	}

}
