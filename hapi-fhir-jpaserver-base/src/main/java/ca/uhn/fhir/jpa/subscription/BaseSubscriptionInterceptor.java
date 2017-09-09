package ca.uhn.fhir.jpa.subscription;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.SearchParameterMap;
import ca.uhn.fhir.jpa.provider.ServletSubRequestDetails;
import ca.uhn.fhir.jpa.util.StopWatch;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.interceptor.ServerOperationInterceptorAdapter;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.support.ExecutorSubscribableChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.*;
import java.util.concurrent.*;

public abstract class BaseSubscriptionInterceptor extends ServerOperationInterceptorAdapter {

	static final String SUBSCRIPTION_STATUS = "Subscription.status";
	static final String SUBSCRIPTION_TYPE = "Subscription.channel.type";
	static final String SUBSCRIPTION_CRITERIA = "Subscription.criteria";
	static final String SUBSCRIPTION_ENDPOINT = "Subscription.channel.endpoint";
	static final String SUBSCRIPTION_PAYLOAD = "Subscription.channel.payload";
	static final String SUBSCRIPTION_HEADER = "Subscription.channel.header";
	private static final Integer MAX_SUBSCRIPTION_RESULTS = 1000;
	private SubscribableChannel myProcessingChannel;
	private SubscribableChannel myDeliveryChannel;
	private ExecutorService myProcessingExecutor;
	private int myExecutorThreadCount;
	private SubscriptionActivatingSubscriber mySubscriptionActivatingSubscriber;
	private MessageHandler mySubscriptionCheckingSubscriber;
	private ConcurrentHashMap<String, IBaseResource> myIdToSubscription = new ConcurrentHashMap<>();
	private Logger ourLog = LoggerFactory.getLogger(BaseSubscriptionInterceptor.class);
	private ThreadPoolExecutor myDeliveryExecutor;
	private LinkedBlockingQueue<Runnable> myProcessingExecutorQueue;
	private LinkedBlockingQueue<Runnable> myDeliveryExecutorQueue;

	/**
	 * Constructor
	 */
	public BaseSubscriptionInterceptor() {
		super();
		setExecutorThreadCount(5);
	}

	public abstract Subscription.SubscriptionChannelType getChannelType();

	public SubscribableChannel getDeliveryChannel() {
		return myDeliveryChannel;
	}

	public void setDeliveryChannel(SubscribableChannel theDeliveryChannel) {
		myDeliveryChannel = theDeliveryChannel;
	}

	public int getExecutorQueueSizeForUnitTests() {
		return myProcessingExecutorQueue.size() + myDeliveryExecutorQueue.size();
	}

	public int getExecutorThreadCount() {
		return myExecutorThreadCount;
	}

	public void setExecutorThreadCount(int theExecutorThreadCount) {
		Validate.inclusiveBetween(1, Integer.MAX_VALUE, theExecutorThreadCount);
		myExecutorThreadCount = theExecutorThreadCount;
	}

	public ConcurrentHashMap<String, IBaseResource> getIdToSubscription() {
		return myIdToSubscription;
	}

	public SubscribableChannel getProcessingChannel() {
		return myProcessingChannel;
	}

	public void setProcessingChannel(SubscribableChannel theProcessingChannel) {
		myProcessingChannel = theProcessingChannel;
	}

	protected abstract IFhirResourceDao<?> getSubscriptionDao();

	/**
	 * Read the existing subscriptions from the database
	 */
	@SuppressWarnings("unused")
	@Scheduled(fixedDelay = 10000)
	public void initSubscriptions() {
		SearchParameterMap map = new SearchParameterMap();
		map.add(Subscription.SP_TYPE, new TokenParam(null, getChannelType().toCode()));
		map.add(Subscription.SP_STATUS, new TokenOrListParam()
			.addOr(new TokenParam(null, Subscription.SubscriptionStatus.REQUESTED.toCode()))
			.addOr(new TokenParam(null, Subscription.SubscriptionStatus.ACTIVE.toCode())));
		map.setLoadSynchronousUpTo(MAX_SUBSCRIPTION_RESULTS);

		RequestDetails req = new ServletSubRequestDetails();
		req.setSubRequest(true);

		IBundleProvider subscriptionBundleList = getSubscriptionDao().search(map, req);
		if (subscriptionBundleList.size() >= MAX_SUBSCRIPTION_RESULTS) {
			ourLog.error("Currently over " + MAX_SUBSCRIPTION_RESULTS + " subscriptions.  Some subscriptions have not been loaded.");
		}

		List<IBaseResource> resourceList = subscriptionBundleList.getResources(0, subscriptionBundleList.size());

		Set<String> allIds = new HashSet<>();
		for (IBaseResource resource : resourceList) {
			String nextId = resource.getIdElement().getIdPart();
			allIds.add(nextId);
			mySubscriptionActivatingSubscriber.activateAndRegisterSubscriptionIfRequired(resource);
		}

		for (Enumeration<String> keyEnum = myIdToSubscription.keys(); keyEnum.hasMoreElements(); ) {
			String next = keyEnum.nextElement();
			if (!allIds.contains(next)) {
				ourLog.info("Unregistering Subscription/{} as it no longer exists", next);
				myIdToSubscription.remove(next);
			}
		}
	}

	@PostConstruct
	public void postConstruct() {
		{
			myProcessingExecutorQueue = new LinkedBlockingQueue<>(1000);

			RejectedExecutionHandler rejectedExecutionHandler = new RejectedExecutionHandler() {
				@Override
				public void rejectedExecution(Runnable theRunnable, ThreadPoolExecutor theExecutor) {
					ourLog.info("Note: Executor queue is full ({} elements), waiting for a slot to become available!", myProcessingExecutorQueue.size());
					StopWatch sw = new StopWatch();
					try {
						myProcessingExecutorQueue.put(theRunnable);
					} catch (InterruptedException theE) {
						throw new RejectedExecutionException("Task " + theRunnable.toString() +
							" rejected from " + theE.toString());
					}
					ourLog.info("Slot become available after {}ms", sw.getMillis());
				}
			};
			ThreadFactory threadFactory = new BasicThreadFactory.Builder()
				.namingPattern("subscription-proc-%d")
				.daemon(false)
				.priority(Thread.NORM_PRIORITY)
				.build();
			myProcessingExecutor = new ThreadPoolExecutor(
				1,
				getExecutorThreadCount(),
				0L,
				TimeUnit.MILLISECONDS,
				myProcessingExecutorQueue,
				threadFactory,
				rejectedExecutionHandler);
		}
		{
			myDeliveryExecutorQueue = new LinkedBlockingQueue<>(1000);
			BasicThreadFactory threadFactory = new BasicThreadFactory.Builder()
				.namingPattern("subscription-delivery-%d")
				.daemon(false)
				.priority(Thread.NORM_PRIORITY)
				.build();
			RejectedExecutionHandler rejectedExecutionHandler2 = new RejectedExecutionHandler() {
				@Override
				public void rejectedExecution(Runnable theRunnable, ThreadPoolExecutor theExecutor) {
					ourLog.info("Note: Executor queue is full ({} elements), waiting for a slot to become available!", myDeliveryExecutorQueue.size());
					StopWatch sw = new StopWatch();
					try {
						myDeliveryExecutorQueue.put(theRunnable);
					} catch (InterruptedException theE) {
						throw new RejectedExecutionException("Task " + theRunnable.toString() +
							" rejected from " + theE.toString());
					}
					ourLog.info("Slot become available after {}ms", sw.getMillis());
				}
			};
			myDeliveryExecutor = new ThreadPoolExecutor(
				1,
				getExecutorThreadCount(),
				0L,
				TimeUnit.MILLISECONDS,
				myDeliveryExecutorQueue,
				threadFactory,
				rejectedExecutionHandler2);
		}

		if (getProcessingChannel() == null) {
			setProcessingChannel(new ExecutorSubscribableChannel(myProcessingExecutor));
		}
		if (getDeliveryChannel() == null) {
			setDeliveryChannel(new ExecutorSubscribableChannel(myDeliveryExecutor));
		}

		if (mySubscriptionActivatingSubscriber == null) {
			mySubscriptionActivatingSubscriber = new SubscriptionActivatingSubscriber(getSubscriptionDao(), myIdToSubscription, getChannelType(), this);
		}
		getProcessingChannel().subscribe(mySubscriptionActivatingSubscriber);

		if (mySubscriptionCheckingSubscriber == null) {
			mySubscriptionCheckingSubscriber = new SubscriptionCheckingSubscriber(getSubscriptionDao(), myIdToSubscription, getChannelType(), this);
		}
		getProcessingChannel().subscribe(mySubscriptionCheckingSubscriber);

		registerDeliverySubscriber();

		initSubscriptions();
	}

	@SuppressWarnings("unused")
	@PreDestroy
	public void preDestroy() {
		getProcessingChannel().unsubscribe(mySubscriptionActivatingSubscriber);
		getProcessingChannel().unsubscribe(mySubscriptionCheckingSubscriber);

		unregisterDeliverySubscriber();
	}

	protected abstract void registerDeliverySubscriber();

	@Override
	public void resourceCreated(RequestDetails theRequest, IBaseResource theResource) {
		ResourceModifiedMessage msg = new ResourceModifiedMessage();
		msg.setId(theResource.getIdElement());
		msg.setOperationType(RestOperationTypeEnum.CREATE);
		msg.setNewPayload(theResource);
		submitResourceModified(msg);
	}

	@Override
	public void resourceDeleted(RequestDetails theRequest, IBaseResource theResource) {
		ResourceModifiedMessage msg = new ResourceModifiedMessage();
		msg.setId(theResource.getIdElement());
		msg.setOperationType(RestOperationTypeEnum.DELETE);
		submitResourceModified(msg);
	}

	@Override
	public void resourceUpdated(RequestDetails theRequest, IBaseResource theOldResource, IBaseResource theNewResource) {
		ResourceModifiedMessage msg = new ResourceModifiedMessage();
		msg.setId(theNewResource.getIdElement());
		msg.setOperationType(RestOperationTypeEnum.UPDATE);
		msg.setNewPayload(theNewResource);
		submitResourceModified(msg);
	}

	private void submitResourceModified(final ResourceModifiedMessage theMsg) {
		/*
		 * We only actually submit this item work working after the
		 */
		TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
			@Override
			public void afterCommit() {
				getProcessingChannel().send(new GenericMessage<>(theMsg));
			}
		});
	}

	protected abstract void unregisterDeliverySubscriber();
}
