package ca.uhn.fhir.jpa.searchparam.cache;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.model.sched.HapiJob;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.jpa.searchparam.retry.Retrier;
import ca.uhn.fhir.util.StopWatch;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Repository
public class VersionChangeConsumerRegistry implements IVersionChangeConsumerRegistry {
	private static final Logger ourLog = LoggerFactory.getLogger(VersionChangeConsumerRegistry.class);

	private static long REFRESH_INTERVAL = DateUtils.MILLIS_PER_MINUTE;
	private static final int MAX_RETRIES = 60; // 5 minutes
	private volatile long myLastRefresh;

	@Autowired
	private IInterceptorService myInterceptorBroadcaster;
	@Autowired
	private ISchedulerService mySchedulerService;
	@Autowired
	private ResourceVersionCacheSvc myResourceVersionCacheSvc;

	private final Map<String, Map<IVersionChangeConsumer, SearchParameterMap>> myConsumers = new HashMap<>();
	private final VersionChangeCache myVersionChangeCache = new VersionChangeCache();

	private RefreshVersionCacheAndNotifyConsumersOnUpdate myInterceptor;

	@Override
	public void registerResourceVersionChangeConsumer(String theResourceType, SearchParameterMap map, IVersionChangeConsumer theVersionChangeConsumer) {
		myConsumers.computeIfAbsent(theResourceType, consumer -> new HashMap<>());
		myConsumers.get(theResourceType).put(theVersionChangeConsumer, map);
	}

	@PostConstruct
	public void start() {
		myInterceptor = new RefreshVersionCacheAndNotifyConsumersOnUpdate();
		myInterceptorBroadcaster.registerInterceptor(myInterceptor);

		ScheduledJobDefinition jobDetail = new ScheduledJobDefinition();
		jobDetail.setId(getClass().getName());
		jobDetail.setJobClass(Job.class);
		mySchedulerService.scheduleLocalJob(10 * DateUtils.MILLIS_PER_SECOND, jobDetail);
	}

	public static class Job implements HapiJob {
		@Autowired
		private ISearchParamRegistry myTarget;

		@Override
		public void execute(JobExecutionContext theContext) {
			myTarget.refreshCacheIfNecessary();
		}
	}

	@Override
	public boolean refreshCacheIfNecessary() {
		if (myLastRefresh == 0 || System.currentTimeMillis() - REFRESH_INTERVAL > myLastRefresh) {
			refreshCacheWithRetry();
			return true;
		} else {
			return false;
		}
	}

	@Override
	@VisibleForTesting
	public void clearConsumersForUnitTest() {
		myConsumers.clear();
	}

	@Override
	public void forceRefresh() {
		requestRefresh();
		refreshCacheWithRetry();
	}

	@Override
	public void requestRefresh() {
		synchronized (this) {
			myLastRefresh = 0;
		}
	}

	private int refreshCacheWithRetry() {
		Retrier<Integer> refreshCacheRetrier = new Retrier<>(() -> {
			synchronized (VersionChangeConsumerRegistry.this) {
				return doRefresh(REFRESH_INTERVAL);
			}
		}, MAX_RETRIES);
		return refreshCacheRetrier.runWithRetry();
	}

	public int doRefresh(long theRefreshInterval) {
		if (System.currentTimeMillis() - theRefreshInterval <= myLastRefresh) {
			return 0;
		}
		StopWatch sw = new StopWatch();
		// FIXME KHS call myResourceVersionCacheSvc and store the results in myVersionChangeCache
		myLastRefresh = System.currentTimeMillis();
		ourLog.debug("Refreshed search parameter cache in {}ms", sw.getMillis());
		// FIXME KHS return something
		return 0;
	}

	@Interceptor
	public class RefreshVersionCacheAndNotifyConsumersOnUpdate {

		@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_CREATED)
		public void created(IBaseResource theResource) {
			handle(theResource);
		}

		@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_DELETED)
		public void deleted(IBaseResource theResource) {
			handle(theResource);
		}

		@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_UPDATED)
		public void updated(IBaseResource theResource) {
			handle(theResource);
		}

		private void handle(IBaseResource theResource) {
			// FIXME KHS do something like this
//			if (theResource != null && myFhirContext.getResourceType(theResource).equals("SearchParameter")) {
//				requestRefresh();
//			}
		}

	}
}
