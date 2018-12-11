package ca.uhn.fhir.jpa.subscription.dbcache;

import ca.uhn.fhir.jpa.config.BaseConfig;
import ca.uhn.fhir.jpa.dao.DaoRegistry;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.provider.ServletSubRequestDetails;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.subscription.SubscriptionActivatingInterceptor;
import ca.uhn.fhir.jpa.subscription.cache.ISubscriptionLoader;
import ca.uhn.fhir.jpa.subscription.cache.SubscriptionConstants;
import ca.uhn.fhir.jpa.subscription.cache.SubscriptionRegistry;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import com.google.common.annotations.VisibleForTesting;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Semaphore;


@Service
@Lazy
public class SubscriptionLoaderDatabase implements ISubscriptionLoader {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SubscriptionLoaderDatabase.class);

	@Autowired
	private PlatformTransactionManager myTxManager;
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private SubscriptionRegistry mySubscriptionRegistry;
	@Autowired
	private SubscriptionActivatingInterceptor mySubscriptionActivatingInterceptor;

	private final Object myInitSubscriptionsLock = new Object();
	private Semaphore myInitSubscriptionsSemaphore = new Semaphore(1);

	@PostConstruct
	public void start() {

		TransactionTemplate transactionTemplate = new TransactionTemplate(myTxManager);
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				initSubscriptions();
			}
		});
	}

	/**
	 * Read the existing subscriptions from the database
	 */
	@SuppressWarnings("unused")
	@Scheduled(fixedDelay = 60000)
	@Override
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

	@VisibleForTesting
	public int doInitSubscriptions() {
		synchronized (myInitSubscriptionsLock) {
			ourLog.debug("Starting init subscriptions");
			SearchParameterMap map = new SearchParameterMap();
			map.add(Subscription.SP_STATUS, new TokenOrListParam()
				.addOr(new TokenParam(null, Subscription.SubscriptionStatus.REQUESTED.toCode()))
				.addOr(new TokenParam(null, Subscription.SubscriptionStatus.ACTIVE.toCode())));
			map.setLoadSynchronousUpTo(SubscriptionConstants.MAX_SUBSCRIPTION_RESULTS);

			RequestDetails req = new ServletSubRequestDetails();
			req.setSubRequest(true);

			IFhirResourceDao<?> subscriptionDao = myDaoRegistry.getSubscriptionDao();
			IBundleProvider subscriptionBundleList = subscriptionDao.search(map, req);
			if (subscriptionBundleList.size() >= SubscriptionConstants.MAX_SUBSCRIPTION_RESULTS) {
				ourLog.error("Currently over " + SubscriptionConstants.MAX_SUBSCRIPTION_RESULTS + " subscriptions.  Some subscriptions have not been loaded.");
			}

			List<IBaseResource> resourceList = subscriptionBundleList.getResources(0, subscriptionBundleList.size());

			Set<String> allIds = new HashSet<>();
			int changesCount = 0;
			for (IBaseResource resource : resourceList) {
				String nextId = resource.getIdElement().getIdPart();
				allIds.add(nextId);
				boolean changed = mySubscriptionActivatingInterceptor.activateOrRegisterSubscriptionIfRequired(resource);
				if (changed) {
					changesCount++;
				}
			}

			mySubscriptionRegistry.unregisterAllSubscriptionsNotInCollection(allIds);
			ourLog.trace("Finished init subscriptions - found {}", resourceList.size());

			return changesCount;
		}
	}
}

