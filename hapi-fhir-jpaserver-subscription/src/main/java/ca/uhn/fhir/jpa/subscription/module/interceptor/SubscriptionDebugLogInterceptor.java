package ca.uhn.fhir.jpa.subscription.module.interceptor;

import ca.uhn.fhir.jpa.model.interceptor.api.Hook;
import ca.uhn.fhir.jpa.model.interceptor.api.Interceptor;
import ca.uhn.fhir.jpa.model.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.subscription.module.ResourceModifiedMessage;
import ca.uhn.fhir.jpa.subscription.module.subscriber.ResourceDeliveryMessage;
import ca.uhn.fhir.util.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.util.Date;

/**
 * This interceptor can be used for troubleshooting subscription processing. It provides very
 * detailed logging about the subscription processing pipeline.
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

	@Hook(Pointcut.SUBSCRIPTION_BEFORE_PERSISTED_RESOURCE_CHECKED)
	public void checkMessage(ResourceModifiedMessage theMessage) {
		String value = Long.toString(System.currentTimeMillis());
		theMessage.setAdditionalProperty(SUBSCRIPTION_DEBUG_LOG_INTERCEPTOR_PRECHECK, value);

		log("About to check {} for subscription matches", theMessage.getPayloadId());
	}

	@Hook(Pointcut.SUBSCRIPTION_BEFORE_DELIVERY)
	public void beforeDelivery(ResourceDeliveryMessage theMessage) {
		log("About to deliver resource {} for subscription {} to channel of type {}", theMessage.getPayloadId(), theMessage.getSubscription().getIdElementString(), theMessage.getSubscription().getChannelType());
	}

	@Hook(Pointcut.SUBSCRIPTION_AFTER_DELIVERY)
	public void afterDelivery(ResourceDeliveryMessage theMessage) {
		Date precheckTime = theMessage
			.getAdditionalProperty(SUBSCRIPTION_DEBUG_LOG_INTERCEPTOR_PRECHECK)
			.map(Long::parseLong)
			.map(t -> new Date(t))
			.orElse(null);

		String processingTime = "(unknown)";
		if (precheckTime != null) {
			processingTime = new StopWatch(precheckTime).toString();
		}

		log("Finished delivery of resource {} for subscription {} to channel of type {} - Total processing time: {}", theMessage.getPayloadId(), theMessage.getSubscription().getIdElementString(), theMessage.getSubscription().getChannelType(), processingTime);
	}

	private void log(String theMessage, Object... theArguments) {
		switch (myLevel) {
			case ERROR:
				myLogger.error(theMessage, theArguments);
				break;
			case WARN:
				myLogger.warn(theMessage, theArguments);
				break;
			case INFO:
				myLogger.info(theMessage, theArguments);
				break;
			case DEBUG:
				myLogger.debug(theMessage, theArguments);
				break;
			case TRACE:
				myLogger.trace(theMessage, theArguments);
				break;
		}
	}

}
