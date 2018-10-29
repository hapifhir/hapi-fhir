package ca.uhn.fhir.jpa.subscription;

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

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.jpa.config.BaseConfig;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.SearchParameterMap;
import ca.uhn.fhir.jpa.provider.ServletSubRequestDetails;
import ca.uhn.fhir.jpa.util.JpaConstants;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.interceptor.ServerOperationInterceptorAdapter;
import ca.uhn.fhir.util.StopWatch;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.support.ExecutorSubscribableChannel;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.*;
import java.util.concurrent.*;

public abstract class BaseSubscriptionInterceptor<S extends IBaseResource> extends ServerOperationInterceptorAdapter {

	static final String SUBSCRIPTION_STATUS = "Subscription.status";
	static final String SUBSCRIPTION_TYPE = "Subscription.channel.type";
	private static final Integer MAX_SUBSCRIPTION_RESULTS = 1000;
	private final Object myInitSubscriptionsLock = new Object();
	private SubscribableChannel myProcessingChannel;
	private Map<String, SubscribableChannel> myDeliveryChannel;
	private ExecutorService myProcessingExecutor;
	private int myExecutorThreadCount;
	private SubscriptionActivatingSubscriber mySubscriptionActivatingSubscriber;
	private MessageHandler mySubscriptionCheckingSubscriber;
	private ConcurrentHashMap<String, CanonicalSubscription> myIdToSubscription = new ConcurrentHashMap<>();
	private ConcurrentHashMap<String, SubscribableChannel> mySubscribableChannel = new ConcurrentHashMap<>();
	private Multimap<String, MessageHandler> myIdToDeliveryHandler = Multimaps.synchronizedListMultimap(ArrayListMultimap.create());
	private Logger ourLog = LoggerFactory.getLogger(BaseSubscriptionInterceptor.class);
	private ThreadPoolExecutor myDeliveryExecutor;
	private LinkedBlockingQueue<Runnable> myProcessingExecutorQueue;
	private IFhirResourceDao<?> mySubscriptionDao;
	@Autowired
	private List<IFhirResourceDao<?>> myResourceDaos;
	@Autowired
	private FhirContext myCtx;
	@Autowired(required = false)
	@Qualifier("myEventDefinitionDaoR4")
	private IFhirResourceDao<org.hl7.fhir.r4.model.EventDefinition> myEventDefinitionDaoR4;
	@Autowired()
	private PlatformTransactionManager myTxManager;
	@Autowired
	@Qualifier(BaseConfig.TASK_EXECUTOR_NAME)
	private AsyncTaskExecutor myAsyncTaskExecutor;
	private Map<Class<? extends IBaseResource>, IFhirResourceDao<?>> myResourceTypeToDao;
	private Semaphore myInitSubscriptionsSemaphore = new Semaphore(1);

	/**
	 * Constructor
	 */
	public BaseSubscriptionInterceptor() {
		super();
		setExecutorThreadCount(5);
	}

	protected CanonicalSubscription canonicalize(S theSubscription) {
		switch (myCtx.getVersion().getVersion()) {
			case DSTU2:
				return canonicalizeDstu2(theSubscription);
			case DSTU3:
				return canonicalizeDstu3(theSubscription);
			case R4:
				return canonicalizeR4(theSubscription);
			default:
				throw new ConfigurationException("Subscription not supported for version: " + myCtx.getVersion().getVersion());
		}
	}

	protected CanonicalSubscription canonicalizeDstu2(IBaseResource theSubscription) {
		ca.uhn.fhir.model.dstu2.resource.Subscription subscription = (ca.uhn.fhir.model.dstu2.resource.Subscription) theSubscription;

		CanonicalSubscription retVal = new CanonicalSubscription();
		try {
			retVal.setStatus(org.hl7.fhir.r4.model.Subscription.SubscriptionStatus.fromCode(subscription.getStatus()));
			retVal.setChannelType(org.hl7.fhir.r4.model.Subscription.SubscriptionChannelType.fromCode(subscription.getChannel().getType()));
			retVal.setCriteriaString(subscription.getCriteria());
			retVal.setEndpointUrl(subscription.getChannel().getEndpoint());
			retVal.setHeaders(subscription.getChannel().getHeader());
			retVal.setIdElement(subscription.getIdElement());
			retVal.setPayloadString(subscription.getChannel().getPayload());
		} catch (FHIRException theE) {
			throw new InternalErrorException(theE);
		}
		return retVal;
	}

	protected CanonicalSubscription canonicalizeDstu3(IBaseResource theSubscription) {
		org.hl7.fhir.dstu3.model.Subscription subscription = (org.hl7.fhir.dstu3.model.Subscription) theSubscription;

		CanonicalSubscription retVal = new CanonicalSubscription();
		try {
			retVal.setStatus(org.hl7.fhir.r4.model.Subscription.SubscriptionStatus.fromCode(subscription.getStatus().toCode()));
			retVal.setChannelType(org.hl7.fhir.r4.model.Subscription.SubscriptionChannelType.fromCode(subscription.getChannel().getType().toCode()));
			retVal.setCriteriaString(subscription.getCriteria());
			retVal.setEndpointUrl(subscription.getChannel().getEndpoint());
			retVal.setHeaders(subscription.getChannel().getHeader());
			retVal.setIdElement(subscription.getIdElement());
			retVal.setPayloadString(subscription.getChannel().getPayload());

			if (retVal.getChannelType() == Subscription.SubscriptionChannelType.EMAIL) {
				String from;
				String subjectTemplate;
				String bodyTemplate;
				try {
					from = subscription.getChannel().getExtensionString(JpaConstants.EXT_SUBSCRIPTION_EMAIL_FROM);
					subjectTemplate = subscription.getChannel().getExtensionString(JpaConstants.EXT_SUBSCRIPTION_SUBJECT_TEMPLATE);
				} catch (FHIRException theE) {
					throw new ConfigurationException("Failed to extract subscription extension(s): " + theE.getMessage(), theE);
				}
				retVal.getEmailDetails().setFrom(from);
				retVal.getEmailDetails().setSubjectTemplate(subjectTemplate);
			}

			if (retVal.getChannelType() == Subscription.SubscriptionChannelType.RESTHOOK) {
				String stripVersionIds;
				String deliverLatestVersion;
				try {
					stripVersionIds = subscription.getChannel().getExtensionString(JpaConstants.EXT_SUBSCRIPTION_RESTHOOK_STRIP_VERSION_IDS);
					deliverLatestVersion = subscription.getChannel().getExtensionString(JpaConstants.EXT_SUBSCRIPTION_RESTHOOK_DELIVER_LATEST_VERSION);
				} catch (FHIRException theE) {
					throw new ConfigurationException("Failed to extract subscription extension(s): " + theE.getMessage(), theE);
				}
				retVal.getRestHookDetails().setStripVersionId(Boolean.parseBoolean(stripVersionIds));
				retVal.getRestHookDetails().setDeliverLatestVersion(Boolean.parseBoolean(deliverLatestVersion));
			}

		} catch (FHIRException theE) {
			throw new InternalErrorException(theE);
		}
		return retVal;
	}

	protected CanonicalSubscription canonicalizeR4(IBaseResource theSubscription) {
		org.hl7.fhir.r4.model.Subscription subscription = (org.hl7.fhir.r4.model.Subscription) theSubscription;

		CanonicalSubscription retVal = new CanonicalSubscription();
		retVal.setStatus(subscription.getStatus());
		retVal.setChannelType(subscription.getChannel().getType());
		retVal.setCriteriaString(subscription.getCriteria());
		retVal.setEndpointUrl(subscription.getChannel().getEndpoint());
		retVal.setHeaders(subscription.getChannel().getHeader());
		retVal.setIdElement(subscription.getIdElement());
		retVal.setPayloadString(subscription.getChannel().getPayload());

		if (retVal.getChannelType() == Subscription.SubscriptionChannelType.EMAIL) {
			String from;
			String subjectTemplate;
			try {
				from = subscription.getChannel().getExtensionString(JpaConstants.EXT_SUBSCRIPTION_EMAIL_FROM);
				subjectTemplate = subscription.getChannel().getExtensionString(JpaConstants.EXT_SUBSCRIPTION_SUBJECT_TEMPLATE);
			} catch (FHIRException theE) {
				throw new ConfigurationException("Failed to extract subscription extension(s): " + theE.getMessage(), theE);
			}
			retVal.getEmailDetails().setFrom(from);
			retVal.getEmailDetails().setSubjectTemplate(subjectTemplate);
		}

		if (retVal.getChannelType() == Subscription.SubscriptionChannelType.RESTHOOK) {
			String stripVersionIds;
			String deliverLatestVersion;
			try {
				stripVersionIds = subscription.getChannel().getExtensionString(JpaConstants.EXT_SUBSCRIPTION_RESTHOOK_STRIP_VERSION_IDS);
				deliverLatestVersion = subscription.getChannel().getExtensionString(JpaConstants.EXT_SUBSCRIPTION_RESTHOOK_DELIVER_LATEST_VERSION);
			} catch (FHIRException theE) {
				throw new ConfigurationException("Failed to extract subscription extension(s): " + theE.getMessage(), theE);
			}
			retVal.getRestHookDetails().setStripVersionId(Boolean.parseBoolean(stripVersionIds));
			retVal.getRestHookDetails().setDeliverLatestVersion(Boolean.parseBoolean(deliverLatestVersion));
		}

		List<org.hl7.fhir.r4.model.Extension> topicExts = subscription.getExtensionsByUrl("http://hl7.org/fhir/subscription/topics");
		if (topicExts.size() > 0) {
			IBaseReference ref = (IBaseReference) topicExts.get(0).getValueAsPrimitive();
			if (!"EventDefinition".equals(ref.getReferenceElement().getResourceType())) {
				throw new PreconditionFailedException("Topic reference must be an EventDefinition");
			}

			org.hl7.fhir.r4.model.EventDefinition def = myEventDefinitionDaoR4.read(ref.getReferenceElement());
			retVal.addTrigger(new CanonicalSubscription.CanonicalEventDefinition(def));
		}

		return retVal;
	}

	protected SubscribableChannel createDeliveryChannel(CanonicalSubscription theSubscription) {
		String subscriptionId = theSubscription.getIdElement(myCtx).getIdPart();

		LinkedBlockingQueue<Runnable> executorQueue = new LinkedBlockingQueue<>(1000);
		BasicThreadFactory threadFactory = new BasicThreadFactory.Builder()
			.namingPattern("subscription-delivery-" + subscriptionId + "-%d")
			.daemon(false)
			.priority(Thread.NORM_PRIORITY)
			.build();
		RejectedExecutionHandler rejectedExecutionHandler = new RejectedExecutionHandler() {
			@Override
			public void rejectedExecution(Runnable theRunnable, ThreadPoolExecutor theExecutor) {
				ourLog.info("Note: Executor queue is full ({} elements), waiting for a slot to become available!", executorQueue.size());
				StopWatch sw = new StopWatch();
				try {
					executorQueue.put(theRunnable);
				} catch (InterruptedException theE) {
					throw new RejectedExecutionException("Task " + theRunnable.toString() +
						" rejected from " + theE.toString());
				}
				ourLog.info("Slot become available after {}ms", sw.getMillis());
			}
		};
		ThreadPoolExecutor deliveryExecutor = new ThreadPoolExecutor(
			1,
			getExecutorThreadCount(),
			0L,
			TimeUnit.MILLISECONDS,
			executorQueue,
			threadFactory,
			rejectedExecutionHandler);

		return new ExecutorSubscribableChannel(deliveryExecutor);
	}

	/**
	 * Returns an empty handler if the interceptor will manually handle registration and unregistration
	 */
	protected abstract Optional<MessageHandler> createDeliveryHandler(CanonicalSubscription theSubscription);

	public abstract Subscription.SubscriptionChannelType getChannelType();

	@SuppressWarnings("unchecked")
	public <R extends IBaseResource> IFhirResourceDao<R> getDao(Class<R> theType) {
		if (myResourceTypeToDao == null) {
			Map<Class<? extends IBaseResource>, IFhirResourceDao<?>> theResourceTypeToDao = new HashMap<>();
			for (IFhirResourceDao<?> next : myResourceDaos) {
				theResourceTypeToDao.put(next.getResourceType(), next);
			}

			if (this instanceof IFhirResourceDao<?>) {
				IFhirResourceDao<?> thiz = (IFhirResourceDao<?>) this;
				theResourceTypeToDao.put(thiz.getResourceType(), thiz);
			}

			myResourceTypeToDao = theResourceTypeToDao;
		}

		return (IFhirResourceDao<R>) myResourceTypeToDao.get(theType);
	}

	protected MessageChannel getDeliveryChannel(CanonicalSubscription theSubscription) {
		return mySubscribableChannel.get(theSubscription.getIdElement(myCtx).getIdPart());
	}

	public int getExecutorQueueSizeForUnitTests() {
		return myProcessingExecutorQueue.size();
	}

	public int getExecutorThreadCount() {
		return myExecutorThreadCount;
	}

	public void setExecutorThreadCount(int theExecutorThreadCount) {
		Validate.inclusiveBetween(1, Integer.MAX_VALUE, theExecutorThreadCount);
		myExecutorThreadCount = theExecutorThreadCount;
	}

	public Map<String, CanonicalSubscription> getIdToSubscription() {
		return Collections.unmodifiableMap(myIdToSubscription);
	}

	public SubscribableChannel getProcessingChannel() {
		return myProcessingChannel;
	}

	public void setProcessingChannel(SubscribableChannel theProcessingChannel) {
		myProcessingChannel = theProcessingChannel;
	}

	protected IFhirResourceDao<?> getSubscriptionDao() {
		return mySubscriptionDao;
	}

	public List<CanonicalSubscription> getRegisteredSubscriptions() {
		return new ArrayList<>(myIdToSubscription.values());
	}

	public CanonicalSubscription hasSubscription(IIdType theId) {
		Validate.notNull(theId);
		Validate.notBlank(theId.getIdPart());
		return myIdToSubscription.get(theId.getIdPart());
	}

	/**
	 * Read the existing subscriptions from the database
	 */
	@SuppressWarnings("unused")
	@Scheduled(fixedDelay = 60000)
	public void initSubscriptions() {
		if (!myInitSubscriptionsSemaphore.tryAcquire()) {
			return;
		}
		try {
			doInitSubscriptions();
		} finally {
			myInitSubscriptionsSemaphore.release();
		}
	}

	public Integer doInitSubscriptions() {
		synchronized (myInitSubscriptionsLock) {
			ourLog.debug("Starting init subscriptions");
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
			int changesCount = 0;
			for (IBaseResource resource : resourceList) {
				String nextId = resource.getIdElement().getIdPart();
				allIds.add(nextId);
				boolean changed = mySubscriptionActivatingSubscriber.activateOrRegisterSubscriptionIfRequired(resource);
				if (changed) {
					changesCount++;
				}
			}

			unregisterAllSubscriptionsNotInCollection(allIds);
			ourLog.trace("Finished init subscriptions - found {}", resourceList.size());

			return changesCount;
		}
	}

	@SuppressWarnings("unused")
	@PreDestroy
	public void preDestroy() {
		getProcessingChannel().unsubscribe(mySubscriptionCheckingSubscriber);
		unregisterAllSubscriptionsNotInCollection(Collections.emptyList());
	}

	public void registerHandler(String theSubscriptionId, MessageHandler theHandler) {
		mySubscribableChannel.get(theSubscriptionId).subscribe(theHandler);
		myIdToDeliveryHandler.put(theSubscriptionId, theHandler);
	}

	@SuppressWarnings("UnusedReturnValue")
	public CanonicalSubscription registerSubscription(IIdType theId, S theSubscription) {
		Validate.notNull(theId);
		String subscriptionId = theId.getIdPart();
		Validate.notBlank(subscriptionId);
		Validate.notNull(theSubscription);

		CanonicalSubscription canonicalized = canonicalize(theSubscription);
		SubscribableChannel deliveryChannel = createDeliveryChannel(canonicalized);
		Optional<MessageHandler> deliveryHandler = createDeliveryHandler(canonicalized);

		mySubscribableChannel.put(subscriptionId, deliveryChannel);
		myIdToSubscription.put(subscriptionId, canonicalized);

		deliveryHandler.ifPresent(handler -> registerHandler(subscriptionId, handler));

		return canonicalized;
	}

	protected void registerSubscriptionCheckingSubscriber() {
		if (mySubscriptionCheckingSubscriber == null) {
			mySubscriptionCheckingSubscriber = new SubscriptionCheckingSubscriber(getSubscriptionDao(), getChannelType(), this);
		}
		getProcessingChannel().subscribe(mySubscriptionCheckingSubscriber);
	}

	@Override
	public void resourceCreated(RequestDetails theRequest, IBaseResource theResource) {
		ResourceModifiedMessage msg = new ResourceModifiedMessage();
		msg.setId(theResource.getIdElement());
		msg.setOperationType(ResourceModifiedMessage.OperationTypeEnum.CREATE);
		msg.setNewPayload(myCtx, theResource);
		submitResourceModified(msg);
	}

	@Override
	public void resourceDeleted(RequestDetails theRequest, IBaseResource theResource) {
		ResourceModifiedMessage msg = new ResourceModifiedMessage();
		msg.setId(theResource.getIdElement());
		msg.setOperationType(ResourceModifiedMessage.OperationTypeEnum.DELETE);
		submitResourceModified(msg);
	}

	@Override
	public void resourceUpdated(RequestDetails theRequest, IBaseResource theOldResource, IBaseResource theNewResource) {
		submitResourceModifiedForUpdate(theNewResource);
	}

	void submitResourceModifiedForUpdate(IBaseResource theNewResource) {
		ResourceModifiedMessage msg = new ResourceModifiedMessage();
		msg.setId(theNewResource.getIdElement());
		msg.setOperationType(ResourceModifiedMessage.OperationTypeEnum.UPDATE);
		msg.setNewPayload(myCtx, theNewResource);
		submitResourceModified(msg);
	}

	protected void sendToProcessingChannel(final ResourceModifiedMessage theMessage) {
		ourLog.trace("Registering synchronization to send resource modified message to processing channel");

		/*
		 * We only actually submit this item work working after the
		 */
		if (TransactionSynchronizationManager.isSynchronizationActive()) {
			TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
				@Override
				public void afterCommit() {
					ourLog.trace("Sending resource modified message to processing channel");
					getProcessingChannel().send(new ResourceModifiedJsonMessage(theMessage));
				}
			});
		} else {
			ourLog.trace("Sending resource modified message to processing channel");
			getProcessingChannel().send(new ResourceModifiedJsonMessage(theMessage));
		}
	}

	@VisibleForTesting
	public void setAsyncTaskExecutorForUnitTest(AsyncTaskExecutor theAsyncTaskExecutor) {
		myAsyncTaskExecutor = theAsyncTaskExecutor;
	}

	public void setFhirContext(FhirContext theCtx) {
		myCtx = theCtx;
	}

	public void setResourceDaos(List<IFhirResourceDao<?>> theResourceDaos) {
		myResourceDaos = theResourceDaos;
	}

	@VisibleForTesting
	public void setTxManager(PlatformTransactionManager theTxManager) {
		myTxManager = theTxManager;
	}

	@PostConstruct
	public void start() {
		for (IFhirResourceDao<?> next : myResourceDaos) {
			if (next.getResourceType() != null) {
				if (myCtx.getResourceDefinition(next.getResourceType()).getName().equals("Subscription")) {
					mySubscriptionDao = next;
				}
			}
		}
		Validate.notNull(mySubscriptionDao);

		if (myCtx.getVersion().getVersion() == FhirVersionEnum.R4) {
			Validate.notNull(myEventDefinitionDaoR4);
		}

		if (getProcessingChannel() == null) {
			myProcessingExecutorQueue = new LinkedBlockingQueue<>(1000);
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
				getExecutorThreadCount(),
				0L,
				TimeUnit.MILLISECONDS,
				myProcessingExecutorQueue,
				threadFactory,
				rejectedExecutionHandler);
			setProcessingChannel(new ExecutorSubscribableChannel(myProcessingExecutor));
		}

		if (mySubscriptionActivatingSubscriber == null) {
			mySubscriptionActivatingSubscriber = new SubscriptionActivatingSubscriber(getSubscriptionDao(), getChannelType(), this, myTxManager, myAsyncTaskExecutor);
		}

		registerSubscriptionCheckingSubscriber();

		TransactionTemplate transactionTemplate = new TransactionTemplate(myTxManager);
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				initSubscriptions();
			}
		});
	}

	/**
	 * This is an internal API - Use with caution!
	 */
	public void submitResourceModified(final ResourceModifiedMessage theMsg) {
		mySubscriptionActivatingSubscriber.handleMessage(theMsg.getOperationType(), theMsg.getId(myCtx), theMsg.getNewPayload(myCtx));
		sendToProcessingChannel(theMsg);
	}

	private void unregisterAllSubscriptionsNotInCollection(Collection<String> theAllIds) {
		for (String next : new ArrayList<>(myIdToSubscription.keySet())) {
			if (!theAllIds.contains(next)) {
				ourLog.info("Unregistering Subscription/{}", next);
				CanonicalSubscription subscription = myIdToSubscription.get(next);
				unregisterSubscription(subscription.getIdElement(myCtx));
			}
		}
	}

	public void unregisterHandler(String theSubscriptionId, MessageHandler theMessageHandler) {
		SubscribableChannel channel = mySubscribableChannel.get(theSubscriptionId);
		if (channel != null) {
			channel.unsubscribe(theMessageHandler);
			if (channel instanceof DisposableBean) {
				try {
					((DisposableBean) channel).destroy();
				} catch (Exception e) {
					ourLog.error("Failed to destroy channel bean", e);
				}
			}
		}

		mySubscribableChannel.remove(theSubscriptionId);
	}

	@SuppressWarnings("UnusedReturnValue")
	public CanonicalSubscription unregisterSubscription(IIdType theId) {
		Validate.notNull(theId);

		String subscriptionId = theId.getIdPart();
		Validate.notBlank(subscriptionId);

		for (MessageHandler next : new ArrayList<>(myIdToDeliveryHandler.get(subscriptionId))) {
			unregisterHandler(subscriptionId, next);
		}

		mySubscribableChannel.remove(subscriptionId);

		return myIdToSubscription.remove(subscriptionId);
	}


}
