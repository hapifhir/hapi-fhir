package ca.uhn.fhir.jpa.subscription;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jpa.config.BaseConfig;
import ca.uhn.fhir.jpa.dao.DaoRegistry;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.provider.ServletSubRequestDetails;
import ca.uhn.fhir.jpa.search.warm.CacheWarmingSvcImpl;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.subscription.cache.IDeliveryHandlerCreator;
import ca.uhn.fhir.jpa.subscription.cache.SubscriptionCannonicalizer;
import ca.uhn.fhir.jpa.subscription.cache.SubscriptionConstants;
import ca.uhn.fhir.jpa.subscription.cache.SubscriptionRegistry;
import ca.uhn.fhir.jpa.subscription.dbmatcher.SubscriptionMatcherCompositeInMemoryDatabase;
import ca.uhn.fhir.jpa.subscription.dbmatcher.SubscriptionMatcherDatabase;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.rest.server.interceptor.ServerOperationInterceptorAdapter;
import ca.uhn.fhir.util.StopWatch;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
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

public abstract class BaseSubscriptionInterceptor<S extends IBaseResource> extends ServerOperationInterceptorAdapter implements IDeliveryHandlerCreator {

	static final String SUBSCRIPTION_STATUS = "Subscription.status";
	static final String SUBSCRIPTION_TYPE = "Subscription.channel.type";
	private static boolean ourForcePayloadEncodeAndDecodeForUnitTests;
	private SubscribableChannel myProcessingChannel;
	private ExecutorService myProcessingExecutor;
	private MessageHandler mySubscriptionCheckingSubscriber;
	private Logger ourLog = LoggerFactory.getLogger(BaseSubscriptionInterceptor.class);
	private ThreadPoolExecutor myDeliveryExecutor;
	private LinkedBlockingQueue<Runnable> myProcessingExecutorQueue;
	@Autowired
	private FhirContext myFhirContext;
	@Autowired()
	private PlatformTransactionManager myTxManager;
	@Autowired
	private SubscriptionMatcherCompositeInMemoryDatabase mySubscriptionMatcherCompositeInMemoryDatabase;
	@Autowired
	private SubscriptionMatcherDatabase mySubscriptionMatcherDatabase;
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private BeanFactory beanFactory;
	@Autowired
	private MatchUrlService myMatchUrlService;
	@Autowired
	private SubscriptionRegistry mySubscriptionRegistry;
	@Autowired
	SubscriptionCannonicalizer mySubscriptionCannonicalizer;
	@Autowired
	@Qualifier(BaseConfig.TASK_EXECUTOR_NAME)
	private AsyncTaskExecutor myAsyncTaskExecutor;

	// KHS FIXME state
	private SubscriptionActivatingSubscriber mySubscriptionActivatingSubscriber;

	/**
	 * Constructor
	 */
	public BaseSubscriptionInterceptor() {
		super();
	}

	@PostConstruct
	public void start() {
		// FIXME KHS
// 		if (myFhirContext.getVersion().getVersion() == FhirVersionEnum.R4) {
//			Validate.notNull(myEventDefinitionDaoR4);
//		}

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
				SubscriptionConstants.EXECUTOR_THREAD_COUNT,
				0L,
				TimeUnit.MILLISECONDS,
				myProcessingExecutorQueue,
				threadFactory,
				rejectedExecutionHandler);
			setProcessingChannel(new ExecutorSubscribableChannel(myProcessingExecutor));

			registerSubscriptionCheckingSubscriber();

		}
		if (mySubscriptionActivatingSubscriber == null) {
			IFhirResourceDao<?> subscriptionDao = myDaoRegistry.getSubscriptionDao();
			mySubscriptionActivatingSubscriber = new SubscriptionActivatingSubscriber(subscriptionDao,  this, myTxManager, myAsyncTaskExecutor, mySubscriptionRegistry, mySubscriptionCannonicalizer);
		}






	}

	@SuppressWarnings("unused")
	@PreDestroy
	public void preDestroy() {
		getProcessingChannel().unsubscribe(mySubscriptionCheckingSubscriber);
	}

	protected void registerSubscriptionCheckingSubscriber() {
		if (mySubscriptionCheckingSubscriber == null) {
			mySubscriptionCheckingSubscriber = beanFactory.getBean(SubscriptionCheckingSubscriber.class, getChannelType(), this, mySubscriptionMatcherCompositeInMemoryDatabase);
		}
		getProcessingChannel().subscribe(mySubscriptionCheckingSubscriber);
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

	void submitResourceModifiedForUpdate(IBaseResource theNewResource) {
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

	public void setFhirContext(FhirContext theCtx) {
		myFhirContext = theCtx;
	}

	@VisibleForTesting
	public void setTxManager(PlatformTransactionManager theTxManager) {
		myTxManager = theTxManager;
	}


	/**
	 * This is an internal API - Use with caution!
	 */
	public void submitResourceModified(final ResourceModifiedMessage theMsg) {
		mySubscriptionActivatingSubscriber.handleMessage(theMsg.getOperationType(), theMsg.getId(myFhirContext), theMsg.getNewPayload(myFhirContext));
		sendToProcessingChannel(theMsg);
	}
	public IFhirResourceDao<?> getSubscriptionDao() {
		return myDaoRegistry.getSubscriptionDao();
	}

	public IFhirResourceDao getDao(Class type) {
		return myDaoRegistry.getResourceDao(type);
	}

	public void setResourceDaos(List<IFhirResourceDao> theResourceDaos) {
		myDaoRegistry.setResourceDaos(theResourceDaos);
	}

	public void validateCriteria(final S theResource) {
		CanonicalSubscription subscription = mySubscriptionCannonicalizer.canonicalize(theResource);
		String criteria = subscription.getCriteriaString();
		try {
			RuntimeResourceDefinition resourceDef = CacheWarmingSvcImpl.parseUrlResourceType(myFhirContext, criteria);
			myMatchUrlService.translateMatchUrl(criteria, resourceDef);
		} catch (InvalidRequestException e) {
			throw new UnprocessableEntityException("Invalid subscription criteria submitted: " + criteria + " " + e.getMessage());
		}
	}

	@VisibleForTesting
	public static void setForcePayloadEncodeAndDecodeForUnitTests(boolean theForcePayloadEncodeAndDecodeForUnitTests) {
		ourForcePayloadEncodeAndDecodeForUnitTests = theForcePayloadEncodeAndDecodeForUnitTests;
	}

	public abstract Subscription.SubscriptionChannelType getChannelType();

	public int getExecutorQueueSizeForUnitTests() {
		return myProcessingExecutorQueue.size();
	}

	public SubscribableChannel getProcessingChannel() {
		return myProcessingChannel;
	}

	public void setProcessingChannel(SubscribableChannel theProcessingChannel) {
		myProcessingChannel = theProcessingChannel;
	}

}
