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
import ca.uhn.fhir.util.StopWatch;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.interceptor.ServerOperationInterceptorAdapter;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.AsyncTaskExecutor;
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
	private ConcurrentHashMap<String, CanonicalSubscription> myIdToSubscription = new ConcurrentHashMap<>();
	private Logger ourLog = LoggerFactory.getLogger(BaseSubscriptionInterceptor.class);
	private ThreadPoolExecutor myDeliveryExecutor;
	private LinkedBlockingQueue<Runnable> myProcessingExecutorQueue;
	private LinkedBlockingQueue<Runnable> myDeliveryExecutorQueue;
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
			retVal.setBackingSubscription(myCtx, theSubscription);
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
			retVal.setBackingSubscription(myCtx, theSubscription);
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
		retVal.setBackingSubscription(myCtx, theSubscription);
		retVal.setChannelType(subscription.getChannel().getType());
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

	public List<CanonicalSubscription> getSubscriptions() {
		return new ArrayList<>(myIdToSubscription.values());
	}

	public boolean hasSubscription(IIdType theId) {
		Validate.notNull(theId);
		Validate.notBlank(theId.getIdPart());
		return myIdToSubscription.containsKey(theId.getIdPart());
	}

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

	@SuppressWarnings("unused")
	@PreDestroy
	public void preDestroy() {
		getProcessingChannel().unsubscribe(mySubscriptionCheckingSubscriber);

		unregisterDeliverySubscriber();
	}

	protected abstract void registerDeliverySubscriber();

	public void registerSubscription(IIdType theId, S theSubscription) {
		Validate.notNull(theId);
		Validate.notBlank(theId.getIdPart());
		Validate.notNull(theSubscription);

		myIdToSubscription.put(theId.getIdPart(), canonicalize(theSubscription));
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
		msg.setOperationType(RestOperationTypeEnum.CREATE);
		msg.setNewPayload(myCtx, theResource);
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
		msg.setNewPayload(myCtx, theNewResource);
		submitResourceModified(msg);
	}

	protected void sendToProcessingChannel(final ResourceModifiedMessage theMessage) {
		ourLog.trace("Registering synchronization to send resource modified message to processing channel");

		/*
		 * We only actually submit this item work working after the
		 */
		TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
			@Override
			public void afterCommit() {
				ourLog.trace("Sending resource modified message to processing channel");
				getProcessingChannel().send(new ResourceModifiedJsonMessage(theMessage));
			}
		});
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
			if (myCtx.getResourceDefinition(next.getResourceType()).getName().equals("Subscription")) {
				mySubscriptionDao = next;
			}
		}
		Validate.notNull(mySubscriptionDao);

		if (myCtx.getVersion().getVersion() == FhirVersionEnum.R4) {
			Validate.notNull(myEventDefinitionDaoR4);
		}

		if (getProcessingChannel() == null) {
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
			setProcessingChannel(new ExecutorSubscribableChannel(myProcessingExecutor));
		}

		if (getDeliveryChannel() == null) {
			myDeliveryExecutorQueue = new LinkedBlockingQueue<>(1000);
			BasicThreadFactory threadFactory = new BasicThreadFactory.Builder()
				.namingPattern("subscription-delivery-%d")
				.daemon(false)
				.priority(Thread.NORM_PRIORITY)
				.build();
			RejectedExecutionHandler rejectedExecutionHandler = new RejectedExecutionHandler() {
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
				rejectedExecutionHandler);
			setDeliveryChannel(new ExecutorSubscribableChannel(myDeliveryExecutor));
		}

		if (mySubscriptionActivatingSubscriber == null) {
			mySubscriptionActivatingSubscriber = new SubscriptionActivatingSubscriber(getSubscriptionDao(), getChannelType(), this, myTxManager, myAsyncTaskExecutor);
		}

		registerSubscriptionCheckingSubscriber();
		registerDeliverySubscriber();

		TransactionTemplate transactionTemplate = new TransactionTemplate(myTxManager);
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				initSubscriptions();
			}
		});
	}

	protected void submitResourceModified(final ResourceModifiedMessage theMsg) {
		mySubscriptionActivatingSubscriber.handleMessage(theMsg.getOperationType(), theMsg.getId(myCtx), theMsg.getNewPayload(myCtx));
		sendToProcessingChannel(theMsg);
	}

	protected abstract void unregisterDeliverySubscriber();

	public void unregisterSubscription(IIdType theId) {
		Validate.notNull(theId);
		Validate.notBlank(theId.getIdPart());

		myIdToSubscription.remove(theId.getIdPart());
	}


}
