package ca.uhn.fhir.jpa.test.util;

/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.jpa.subscription.match.deliver.resthook.SubscriptionDeliveringRestHookSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;

import java.util.concurrent.CountDownLatch;

public class StoppableSubscriptionDeliveringRestHookSubscriber extends SubscriptionDeliveringRestHookSubscriber {
	private static final Logger ourLog = LoggerFactory.getLogger(StoppableSubscriptionDeliveringRestHookSubscriber.class);

	private boolean myPauseEveryMessage = false;
	private CountDownLatch myCountDownLatch;

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
