package ca.uhn.fhir.jpa.subscription;

import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.SearchParameterMap;
import ca.uhn.fhir.jpa.provider.ServletSubRequestDetails;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.interceptor.ServerOperationInterceptorAdapter;
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

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

public abstract class BaseSubscriptionInterceptor extends ServerOperationInterceptorAdapter {

	private static final Integer MAX_SUBSCRIPTION_RESULTS = 1000;
	private SubscribableChannel myProcessingChannel;
	private ExecutorService myExecutor;
	private boolean myAutoActivateSubscriptions = true;
	private int myExecutorThreadCount = 1;
	private MessageHandler mySubscriptionActivatingSubscriber;
	private MessageHandler mySubscriptionCheckingSubscriber;
	private ConcurrentHashMap<String, IBaseResource> myIdToSubscription = new ConcurrentHashMap<>();
	private Subscription.SubscriptionChannelType myChannelType = Subscription.SubscriptionChannelType.RESTHOOK;
	private Logger ourLog = LoggerFactory.getLogger(BaseSubscriptionInterceptor.class);

	protected abstract IFhirResourceDao<?> getSubscriptionDao();

	/**
	 * Read the existing subscriptions from the database
	 */
	@SuppressWarnings("unused")
	@Scheduled(fixedDelay = 10000)
	public void initSubscriptions() {
		SearchParameterMap map = new SearchParameterMap();
		map.add(Subscription.SP_TYPE, new TokenParam(null, myChannelType.toCode()));
		map.add(Subscription.SP_STATUS, new TokenParam(null, Subscription.SubscriptionStatus.ACTIVE.toCode()));
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
			myIdToSubscription.put(nextId, resource);
		}

		for (String next : myIdToSubscription.keySet()) {
			if (!allIds.contains(next)) {
				myIdToSubscription.remove(next);
			}
		}
	}

	@PostConstruct
	public void postConstruct() {
		RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.AbortPolicy();
		ThreadFactory threadFactory = new BasicThreadFactory.Builder()
			.namingPattern("subscription-%d")
			.daemon(false)
			.priority(Thread.NORM_PRIORITY)
			.build();
		myExecutor = new ThreadPoolExecutor(
			myExecutorThreadCount,
			myExecutorThreadCount,
			0L,
			TimeUnit.MILLISECONDS,
			new LinkedBlockingQueue<Runnable>(1000),
			threadFactory,
			rejectedExecutionHandler);


		if (myProcessingChannel == null) {
			myProcessingChannel = new ExecutorSubscribableChannel(myExecutor);
		}

		if (myAutoActivateSubscriptions) {
			if (mySubscriptionActivatingSubscriber == null) {
				mySubscriptionActivatingSubscriber = new SubscriptionActivatingSubscriber(getSubscriptionDao(), myIdToSubscription, myChannelType, myProcessingChannel);
			}
			myProcessingChannel.subscribe(mySubscriptionActivatingSubscriber);
		}

		if (mySubscriptionCheckingSubscriber == null) {
			mySubscriptionCheckingSubscriber = new SubscriptionCheckingSubscriber(getSubscriptionDao(), myIdToSubscription, myChannelType, myProcessingChannel);
		}
		myProcessingChannel.subscribe(mySubscriptionCheckingSubscriber);
	}

	@SuppressWarnings("unused")
	@PreDestroy
	public void preDestroy() {
		if (myAutoActivateSubscriptions) {
			myProcessingChannel.unsubscribe(mySubscriptionActivatingSubscriber);
		}
		myProcessingChannel.unsubscribe(mySubscriptionCheckingSubscriber);
	}

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

	private void submitResourceModified(ResourceModifiedMessage theMsg) {
		myProcessingChannel.send(new GenericMessage<>(theMsg));
	}
}
