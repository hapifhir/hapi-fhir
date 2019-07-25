package ca.uhn.fhir.jpa.search.warm;

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
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jpa.dao.*;
import ca.uhn.fhir.parser.DataFormatException;
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

		RuntimeResourceDefinition resourceDef = parseUrlResourceType(myCtx, nextUrl);
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

	/**
	 * TODO: this method probably belongs in a utility class, not here
	 *
	 * @throws DataFormatException If the resource type is not known
	 */
	public static RuntimeResourceDefinition parseUrlResourceType(FhirContext theCtx, String theUrl) throws DataFormatException {
		int paramIndex = theUrl.indexOf('?');
		String resourceName = theUrl.substring(0, paramIndex);
		if (resourceName.contains("/")) {
			resourceName = resourceName.substring(resourceName.lastIndexOf('/') + 1);
		}
		return theCtx.getResourceDefinition(resourceName);
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
			parseUrlResourceType(myCtx, next.getUrl());

			myCacheEntryToNextRefresh.put(next, 0L);
		}
		
	}
}
