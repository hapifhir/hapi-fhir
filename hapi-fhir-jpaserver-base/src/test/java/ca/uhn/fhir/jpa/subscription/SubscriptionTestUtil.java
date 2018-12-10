package ca.uhn.fhir.jpa.subscription;

import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.subscription.subscriber.email.SubscriptionDeliveringEmailSubscriber;
import ca.uhn.fhir.rest.server.RestfulServer;
import org.hl7.fhir.instance.model.Subscription;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class SubscriptionTestUtil {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SubscriptionTestUtil.class);

	@Autowired
	private BeanFactory myBeanFactory;
	@Autowired
	private DaoConfig myDaoConfig;
	@Autowired
	private SubscriptionInterceptorLoader mySubscriptionInterceptorLoader;
	@Autowired
	private SubscriptionMatcherInterceptor mySubscriptionMatcherInterceptor;

	public void waitForQueueToDrain() throws InterruptedException {
		Thread.sleep(100);
		ourLog.info("Executor work queue has {} items", mySubscriptionMatcherInterceptor.getExecutorQueueSizeForUnitTests());
		if (mySubscriptionMatcherInterceptor.getExecutorQueueSizeForUnitTests() > 0) {
			while (mySubscriptionMatcherInterceptor.getExecutorQueueSizeForUnitTests() > 0) {
				Thread.sleep(50);
			}
			ourLog.info("Executor work queue has {} items", mySubscriptionMatcherInterceptor.getExecutorQueueSizeForUnitTests());
		}
		Thread.sleep(100);
	}

	public SubscriptionDeliveringEmailSubscriber createSubscriptionDeliveringEmailSubscriber() {
		return myBeanFactory.getBean(SubscriptionDeliveringEmailSubscriber.class);
	}

	public void registerEmailInterceptor(RestfulServer theRestfulServer) {
		myDaoConfig.addSupportedSubscriptionType(Subscription.SubscriptionChannelType.EMAIL);
		mySubscriptionInterceptorLoader.registerInterceptors(theRestfulServer);
	}

	public void registerRestHookInterceptor(RestfulServer theRestfulServer) {
		myDaoConfig.addSupportedSubscriptionType(Subscription.SubscriptionChannelType.RESTHOOK);
		mySubscriptionInterceptorLoader.registerInterceptors(theRestfulServer);
	}

	public void registerWebSocketInterceptor(RestfulServer theRestfulServer) {
		myDaoConfig.addSupportedSubscriptionType(Subscription.SubscriptionChannelType.WEBSOCKET);
		mySubscriptionInterceptorLoader.registerInterceptors(theRestfulServer);
	}

	public void unregisterSubscriptionInterceptor(RestfulServer theRestfulServer) {
		myDaoConfig.clearSupportedSubscriptionTypes();
		mySubscriptionInterceptorLoader.unregisterInterceptors(theRestfulServer);
	}

	public int getExecutorQueueSizeForUnitTests() {
		return mySubscriptionMatcherInterceptor.getExecutorQueueSizeForUnitTests();
	}
}
