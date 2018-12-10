package ca.uhn.fhir.jpa.subscription;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.subscription.cache.SubscriptionConstants;
import ca.uhn.fhir.jpa.subscription.subscriber.ResourceModifiedJsonMessage;
import ca.uhn.fhir.jpa.subscription.subscriber.SubscriptionCheckingSubscriber;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.interceptor.ServerOperationInterceptorAdapter;
import ca.uhn.fhir.util.StopWatch;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.support.ExecutorSubscribableChannel;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.*;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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

@Component
public class SubscriptionMatcherInterceptor extends ServerOperationInterceptorAdapter {

	static final String SUBSCRIPTION_STATUS = "Subscription.status";
	static final String SUBSCRIPTION_TYPE = "Subscription.channel.type";
	private static boolean ourForcePayloadEncodeAndDecodeForUnitTests;
	private SubscribableChannel myProcessingChannel;
	private ExecutorService myProcessingExecutor;
	private Logger ourLog = LoggerFactory.getLogger(SubscriptionMatcherInterceptor.class);
	private LinkedBlockingQueue<Runnable> myProcessingExecutorQueue;

	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private SubscriptionCheckingSubscriber mySubscriptionCheckingSubscriber;

	/**
	 * Constructor
	 */
	public SubscriptionMatcherInterceptor() {
		super();
	}

	@PostConstruct
	public void start() {
		// FIXME KHS
// 		if (myFhirContext.getVersion().getVersion() == FhirVersionEnum.R4) {
//			Validate.notNull(myEventDefinitionDaoR4);
//		}

		if (myProcessingChannel == null) {
			myProcessingExecutorQueue = new LinkedBlockingQueue<>(SubscriptionConstants.PROCESSING_EXECUTOR_QUEUE_SIZE);
			RejectedExecutionHandler rejectedExecutionHandler = (theRunnable, theExecutor) -> {
				ourLog.info("Note: Executor queue is full ({} elements), waiting for a slot to become available!", myProcessingExecutorQueue.size());
				StopWatch sw = new StopWatch();
				try {
					myProcessingExecutorQueue.put(theRunnable);
				} catch (InterruptedException theE) {
					throw new RejectedExecutionException("Task " + theRunnable.toString() +
						" rejected from " + theE.toString());
				}
				ourLog.info("Slot become available after {}ms", sw.getMillis());
			};
			ThreadFactory threadFactory = new BasicThreadFactory.Builder()
				.namingPattern("subscription-proc-%d")
				.daemon(false)
				.priority(Thread.NORM_PRIORITY)
				.build();
			myProcessingExecutor = new ThreadPoolExecutor(
				1,
				SubscriptionConstants.EXECUTOR_THREAD_COUNT,
				0L,
				TimeUnit.MILLISECONDS,
				myProcessingExecutorQueue,
				threadFactory,
				rejectedExecutionHandler);
			myProcessingChannel = new ExecutorSubscribableChannel(myProcessingExecutor);
		}
		myProcessingChannel.subscribe(mySubscriptionCheckingSubscriber);
	}

	@SuppressWarnings("unused")
	@PreDestroy
	public void preDestroy() {
		myProcessingChannel.unsubscribe(mySubscriptionCheckingSubscriber);
	}

	@Override
	public void resourceCreated(RequestDetails theRequest, IBaseResource theResource) {
		submitResourceModified(theResource, ResourceModifiedMessage.OperationTypeEnum.CREATE);
	}

	@Override
	public void resourceDeleted(RequestDetails theRequest, IBaseResource theResource) {
		submitResourceModified(theResource, ResourceModifiedMessage.OperationTypeEnum.DELETE);
	}

	@Override
	public void resourceUpdated(RequestDetails theRequest, IBaseResource theOldResource, IBaseResource theNewResource) {
		submitResourceModified(theNewResource, ResourceModifiedMessage.OperationTypeEnum.UPDATE);
	}

	private void submitResourceModified(IBaseResource theNewResource, ResourceModifiedMessage.OperationTypeEnum theOperationType) {
		ResourceModifiedMessage msg = new ResourceModifiedMessage(myFhirContext, theNewResource, theOperationType);
		if (ourForcePayloadEncodeAndDecodeForUnitTests) {
			msg.clearPayloadDecoded();
		}
		submitResourceModified(msg);
	}

	protected void sendToProcessingChannel(final ResourceModifiedMessage theMessage) {
		ourLog.trace("Sending resource modified message to processing channel");
		myProcessingChannel.send(new ResourceModifiedJsonMessage(theMessage));
	}

	public void setFhirContext(FhirContext theCtx) {
		myFhirContext = theCtx;
	}

	/**
	 * This is an internal API - Use with caution!
	 */
	public void submitResourceModified(final ResourceModifiedMessage theMsg) {
		sendToProcessingChannel(theMsg);
	}

	@VisibleForTesting
	public static void setForcePayloadEncodeAndDecodeForUnitTests(boolean theForcePayloadEncodeAndDecodeForUnitTests) {
		ourForcePayloadEncodeAndDecodeForUnitTests = theForcePayloadEncodeAndDecodeForUnitTests;
	}

	@VisibleForTesting
	public int getExecutorQueueSizeForUnitTests() {
		return myProcessingExecutorQueue.size();
	}

	@VisibleForTesting
	public ExecutorSubscribableChannel getProcessingChannel() {
		return (ExecutorSubscribableChannel)myProcessingChannel;
	}
}
