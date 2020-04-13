package ca.uhn.fhir.jpa.subscription;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.subscription.channel.impl.LinkedBlockingChannel;
import ca.uhn.fhir.jpa.subscription.submit.interceptor.SubscriptionSubmitInterceptorLoader;
import ca.uhn.fhir.jpa.subscription.match.registry.ActiveSubscription;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionRegistry;
import ca.uhn.fhir.jpa.subscription.channel.subscription.SubscriptionChannelRegistry;
import ca.uhn.fhir.jpa.subscription.channel.subscription.SubscriptionChannelWithHandlers;
import ca.uhn.fhir.jpa.subscription.submit.interceptor.SubscriptionMatcherInterceptor;
import ca.uhn.fhir.jpa.subscription.match.deliver.email.JavaMailEmailSender;
import ca.uhn.fhir.jpa.subscription.match.deliver.email.SubscriptionDeliveringEmailSubscriber;
import org.hl7.fhir.dstu2.model.Subscription;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;

public class SubscriptionTestUtil {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SubscriptionTestUtil.class);

	private JavaMailEmailSender myEmailSender;

	@Autowired
	private DaoConfig myDaoConfig;
	@Autowired
	private SubscriptionSubmitInterceptorLoader mySubscriptionSubmitInterceptorLoader;
	@Autowired
	private SubscriptionMatcherInterceptor mySubscriptionMatcherInterceptor;
	@Autowired
	private SubscriptionRegistry mySubscriptionRegistry;
	@Autowired
	private SubscriptionChannelRegistry mySubscriptionChannelRegistry;

	public int getExecutorQueueSize() {
		LinkedBlockingChannel channel = mySubscriptionMatcherInterceptor.getProcessingChannelForUnitTest();
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
		mySubscriptionSubmitInterceptorLoader.start();
	}

	public void registerRestHookInterceptor() {
		myDaoConfig.addSupportedSubscriptionType(Subscription.SubscriptionChannelType.RESTHOOK);
		mySubscriptionSubmitInterceptorLoader.start();
	}

	public void registerWebSocketInterceptor() {
		myDaoConfig.addSupportedSubscriptionType(Subscription.SubscriptionChannelType.WEBSOCKET);
		mySubscriptionSubmitInterceptorLoader.start();
	}

	public void unregisterSubscriptionInterceptor() {
		myDaoConfig.clearSupportedSubscriptionTypesForUnitTest();
		mySubscriptionSubmitInterceptorLoader.unregisterInterceptorsForUnitTest();
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
		SubscriptionChannelWithHandlers subscriptionChannelWithHandlers = mySubscriptionChannelRegistry.getDeliveryReceiverChannel(activeSubscription.getChannelName());
		SubscriptionDeliveringEmailSubscriber subscriber = (SubscriptionDeliveringEmailSubscriber) subscriptionChannelWithHandlers.getDeliveryHandlerForUnitTest();
		subscriber.setEmailSender(myEmailSender);
	}

}
