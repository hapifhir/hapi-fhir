package ca.uhn.fhir.jpa.config;

import ca.uhn.fhir.jpa.subscription.match.deliver.resthook.SubscriptionDeliveringRestHookSubscriber;
import ca.uhn.fhir.jpa.subscription.match.registry.ActiveSubscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;

import java.util.concurrent.CountDownLatch;

public class StoppableSubscriptionDeliveringRestHookTestSubscriber extends SubscriptionDeliveringRestHookSubscriber {
	private static final Logger ourLog = LoggerFactory.getLogger(StoppableSubscriptionDeliveringRestHookTestSubscriber.class);

	private boolean myPauseEveryMessage = false;
	private CountDownLatch myCountDownLatch;

	public StoppableSubscriptionDeliveringRestHookTestSubscriber() {
		super(null);
	}

	@Override
	public void handleMessage(Message theMessage) throws MessagingException {
		if (myCountDownLatch != null) {
			myCountDownLatch.countDown();
		}
		if (myPauseEveryMessage) {
			waitIfPaused();
		}
		super.handleMessage(theMessage);
	}

	private synchronized void waitIfPaused() {
		try {
			if (myPauseEveryMessage) {
				wait();
			}
		} catch (InterruptedException theE) {
			ourLog.error("interrupted", theE);
		}
	}

	public void pause() {
		myPauseEveryMessage = true;
	}

	public synchronized void unPause() {
		myPauseEveryMessage = false;
		notifyAll();
	}

	public void setCountDownLatch(CountDownLatch theCountDownLatch) {
		myCountDownLatch = theCountDownLatch;
	}
}
