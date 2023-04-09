package ca.uhn.fhir.cache;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.cache.IResourceChangeEvent;
import ca.uhn.fhir.jpa.cache.IResourceChangeListener;
import ca.uhn.fhir.jpa.cache.IResourceChangeListenerCache;
import ca.uhn.fhir.jpa.cache.IResourceChangeListenerRegistry;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.retry.Retrier;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionConstants;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

public abstract class BaseResourceCacheSynchronizer implements IResourceChangeListener {
	private static final Logger ourLog = LoggerFactory.getLogger(BaseResourceCacheSynchronizer.class);
	public static final int MAX_RETRIES = 60; // 60 * 5 seconds = 5 minutes
	public static final long REFRESH_INTERVAL = DateUtils.MILLIS_PER_MINUTE;
	private final String myResourceName;

	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	protected ISearchParamRegistry mySearchParamRegistry;
	@Autowired
	private IResourceChangeListenerRegistry myResourceChangeListenerRegistry;
	@Autowired
	DaoRegistry myDaoRegistry;

	private SearchParameterMap mySearchParameterMap;
	private SystemRequestDetails mySystemRequestDetails;
	private boolean myStopping;
	private boolean myEnabled;
	private final Semaphore mySyncResourcesSemaphore = new Semaphore(1);
	private final Object mySyncResourcesLock = new Object();

	public BaseResourceCacheSynchronizer(String theResourceName) {
		myResourceName = theResourceName;
	}

	@PostConstruct
	public void registerListener() {
		if (!myFhirContext.getVersion().getVersion().isEqualOrNewerThan(FhirVersionEnum.R4B)) {
			return;
		}
		if (myDaoRegistry.getResourceDaoOrNull(myResourceName) == null) {
			ourLog.info("No resource DAO found for resource type {}, not registering listener", myResourceName);
			return;
		}
		mySearchParameterMap = getSearchParameterMap();
		mySystemRequestDetails = SystemRequestDetails.forAllPartitions();

		IResourceChangeListenerCache resourceCache = myResourceChangeListenerRegistry.registerResourceResourceChangeListener(myResourceName, mySearchParameterMap, this, REFRESH_INTERVAL);
		resourceCache.forceRefresh();
		myEnabled = true;
	}

	@PreDestroy
	public void unregisterListener() {
		myResourceChangeListenerRegistry.unregisterResourceResourceChangeListener(this);
	}

	private boolean resourceDaoExists() {
		return myDaoRegistry != null && myDaoRegistry.isResourceTypeSupported(myResourceName);
	}

	/**
	 * Read the existing resources from the database
	 */
	public void syncDatabaseToCache() {
		if (!myEnabled) {
			return;
		}
		if (!resourceDaoExists()) {
			return;
		}
		if (!mySyncResourcesSemaphore.tryAcquire()) {
			return;
		}
		try {
			doSyncResourcesWithRetry();
		} finally {
			mySyncResourcesSemaphore.release();
		}
	}

	@VisibleForTesting
	public void acquireSemaphoreForUnitTest() throws InterruptedException {
		if (!myEnabled) {
			return;
		}
		mySyncResourcesSemaphore.acquire();
	}

	@VisibleForTesting
	public int doSyncResourcessForUnitTest() {
		if (!myEnabled) {
			return 0;
		}
		// Two passes for delete flag to take effect
		int first = doSyncResourcesWithRetry();
		int second = doSyncResourcesWithRetry();
		return first + second;
	}

	synchronized int doSyncResourcesWithRetry() {
		// retry runs MAX_RETRIES times
		// and if errors result every time, it will fail
		Retrier<Integer> syncResourceRetrier = new Retrier<>(this::doSyncResources, MAX_RETRIES);
		return syncResourceRetrier.runWithRetry();
	}

	private int doSyncResources() {
		if (isStopping()) {
			return 0;
		}

		synchronized (mySyncResourcesLock) {
			ourLog.debug("Starting sync {}s", myResourceName);

			IBundleProvider resourceBundleList = getResourceDao().search(mySearchParameterMap, mySystemRequestDetails);

			Integer resourceCount = resourceBundleList.size();
			assert resourceCount != null;
			if (resourceCount >= SubscriptionConstants.MAX_SUBSCRIPTION_RESULTS) {
				ourLog.error("Currently over {} {}s.  Some {}s have not been loaded.", SubscriptionConstants.MAX_SUBSCRIPTION_RESULTS, myResourceName, myResourceName);
			}

			List<IBaseResource> resourceList = resourceBundleList.getResources(0, resourceCount);

			return syncResourcesIntoCache(resourceList);
		}
	}

	protected abstract int syncResourcesIntoCache(List<IBaseResource> resourceList);

	@EventListener(ContextRefreshedEvent.class)
	public void start() {
		myStopping = false;
	}

	@EventListener(ContextClosedEvent.class)
	public void shutdown() {
		myStopping = true;
	}

	private boolean isStopping() {
		return myStopping;
	}

	private IFhirResourceDao<?> getResourceDao() {
		return myDaoRegistry.getResourceDao(myResourceName);
	}





	@Override
	public void handleInit(Collection<IIdType> theResourceIds) {
		if (!myEnabled) {
			return;
		}

		if (!resourceDaoExists()) {
			ourLog.warn("The resource type {} is enabled on this server, but there is no {} DAO configured.", myResourceName, myResourceName);
			return;
		}
		IFhirResourceDao<?> resourceDao = getResourceDao();
		SystemRequestDetails systemRequestDetails = SystemRequestDetails.forAllPartitions();
		List<IBaseResource> resourceList = theResourceIds.stream().map(n -> resourceDao.read(n, systemRequestDetails)).collect(Collectors.toList());
		handleInit(resourceList);
	}

	protected abstract void handleInit(List<IBaseResource> resourceList);

	@Override
	public void handleChange(IResourceChangeEvent theResourceChangeEvent) {
		if (!myEnabled) {
			return;
		}

		// For now ignore the contents of theResourceChangeEvent.  In the future, consider updating the registry based on
		// known resources that have been created, updated & deleted
		syncDatabaseToCache();
	}

	@Nonnull
	protected abstract SearchParameterMap getSearchParameterMap();
}
