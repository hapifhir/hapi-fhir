package ca.uhn.fhir.jpa.subscription;

import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.subscription.module.LinkedBlockingQueueSubscribableChannel;
import ca.uhn.fhir.jpa.subscription.module.cache.ActiveSubscription;
import ca.uhn.fhir.jpa.subscription.module.cache.SubscriptionRegistry;
import ca.uhn.fhir.jpa.subscription.module.channel.SubscriptionChannelRegistry;
import ca.uhn.fhir.jpa.subscription.module.channel.SubscriptionChannelWithHandlers;
import ca.uhn.fhir.jpa.subscription.module.subscriber.email.JavaMailEmailSender;
import ca.uhn.fhir.jpa.subscription.module.subscriber.email.SubscriptionDeliveringEmailSubscriber;
import org.hl7.fhir.dstu2.model.Subscription;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;

public class SubscriptionTestUtil {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SubscriptionTestUtil.class);

	private JavaMailEmailSender myEmailSender;

	@Autowired
	private DaoConfig myDaoConfig;
	@Autowired
	private SubscriptionInterceptorLoader mySubscriptionInterceptorLoader;
	@Autowired
	private SubscriptionMatcherInterceptor mySubscriptionMatcherInterceptor;
	@Autowired
	private SubscriptionRegistry mySubscriptionRegistry;
	@Autowired
	private SubscriptionChannelRegistry mySubscriptionChannelRegistry;

	public int getExecutorQueueSize() {
		LinkedBlockingQueueSubscribableChannel channel = mySubscriptionMatcherInterceptor.getProcessingChannelForUnitTest();
		return channel.getQueueSizeForUnitTest();
	}

	// TODO KHS replace this and similar functions with CountdownLatch
	public void waitForQueueToDrain() throws InterruptedException {
		Thread.sleep(100);
		ourLog.info("Executor work queue has {} items", getExecutorQueueSize());
		if (getExecutorQueueSize() > 0) {
			while (getExecutorQueueSize() > 0) {
				Thread.sleep(50);
			}
			ourLog.info("Executor work queue has {} items", getExecutorQueueSize());
		}
		Thread.sleep(100);
	}

	public void registerEmailInterceptor() {
		myDaoConfig.addSupportedSubscriptionType(Subscription.SubscriptionChannelType.EMAIL);
		mySubscriptionInterceptorLoader.registerInterceptors();
	}

	public void registerRestHookInterceptor() {
		myDaoConfig.addSupportedSubscriptionType(Subscription.SubscriptionChannelType.RESTHOOK);
		mySubscriptionInterceptorLoader.registerInterceptors();
	}

	public void registerWebSocketInterceptor() {
		myDaoConfig.addSupportedSubscriptionType(Subscription.SubscriptionChannelType.WEBSOCKET);
		mySubscriptionInterceptorLoader.registerInterceptors();
	}

	public void unregisterSubscriptionInterceptor() {
		myDaoConfig.clearSupportedSubscriptionTypesForUnitTest();
		mySubscriptionInterceptorLoader.unregisterInterceptorsForUnitTest();
	}

	public int getExecutorQueueSizeForUnitTests() {
		return getExecutorQueueSize();
	}

	public void initEmailSender(int theListenerPort) {
		myEmailSender = new JavaMailEmailSender();
		myEmailSender.setSmtpServerHostname("localhost");
		myEmailSender.setSmtpServerPort(theListenerPort);
		myEmailSender.start();
	}

	public void setEmailSender(IIdType theIdElement) {
		ActiveSubscription activeSubscription = mySubscriptionRegistry.get(theIdElement.getIdPart());
		SubscriptionChannelWithHandlers subscriptionChannelWithHandlers = mySubscriptionChannelRegistry.get(activeSubscription.getChannelName());
		SubscriptionDeliveringEmailSubscriber subscriber = (SubscriptionDeliveringEmailSubscriber) subscriptionChannelWithHandlers.getDeliveryHandlerForUnitTest();
		subscriber.setEmailSender(myEmailSender);
	}

}
