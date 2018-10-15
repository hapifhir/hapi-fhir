package ca.uhn.fhir.jpa.search.warm;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jpa.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CacheWarmingSvcImpl implements ICacheWarmingSvc {

	@Autowired
	private DaoConfig myDaoConfig;
	private Map<WarmCacheEntry, Long> myCacheEntryToNextRefresh = new LinkedHashMap<>();
	@Autowired
	private FhirContext myCtx;
	@Autowired
	private DaoRegistry myDaoRegistry;

	@Override
	@Scheduled(fixedDelay = 1000)
	public synchronized void performWarmingPass() {

		for (WarmCacheEntry nextCacheEntry : new ArrayList<>(myCacheEntryToNextRefresh.keySet())) {

			long nextRefresh = myCacheEntryToNextRefresh.get(nextCacheEntry);
			if (nextRefresh < System.currentTimeMillis()) {

				// Perform the search
				refreshNow(nextCacheEntry);

				// Set the next time to warm this search
				nextRefresh = nextCacheEntry.getPeriodMillis() + System.currentTimeMillis();
				myCacheEntryToNextRefresh.put(nextCacheEntry, nextRefresh);

			}

		}

	}

	private void refreshNow(WarmCacheEntry theCacheEntry) {
		String nextUrl = theCacheEntry.getUrl();

		RuntimeResourceDefinition resourceDef = parseWarmUrlResourceType(nextUrl);
		IFhirResourceDao<?> callingDao = myDaoRegistry.getResourceDao(resourceDef.getName());
		String queryPart = parseWarmUrlParamPart(nextUrl);
		SearchParameterMap responseCriteriaUrl = BaseHapiFhirDao.translateMatchUrl(callingDao, myCtx, queryPart, resourceDef);

		callingDao.search(responseCriteriaUrl);
	}

	private String parseWarmUrlParamPart(String theNextUrl) {
		int paramIndex = theNextUrl.indexOf('?');
		if (paramIndex == -1) {
			throw new ConfigurationException("Invalid warm cache URL (must have ? character)");
		}
		return theNextUrl.substring(paramIndex);
	}

	private RuntimeResourceDefinition parseWarmUrlResourceType(String theNextUrl) {
		int paramIndex = theNextUrl.indexOf('?');
		String resourceName = theNextUrl.substring(0, paramIndex);
		if (resourceName.contains("/")) {
			resourceName = resourceName.substring(resourceName.lastIndexOf('/') + 1);
		}
		RuntimeResourceDefinition resourceDef = myCtx.getResourceDefinition(resourceName);
		return resourceDef;
	}

	@PostConstruct
	public void start() {
		initCacheMap();
	}

	public synchronized void initCacheMap() {

		myCacheEntryToNextRefresh.clear();
		List<WarmCacheEntry> warmCacheEntries = myDaoConfig.getWarmCacheEntries();
		for (WarmCacheEntry next : warmCacheEntries) {

			// Validate
			parseWarmUrlParamPart(next.getUrl());
			parseWarmUrlResourceType(next.getUrl());

			myCacheEntryToNextRefresh.put(next, 0L);
		}
		
	}
}
