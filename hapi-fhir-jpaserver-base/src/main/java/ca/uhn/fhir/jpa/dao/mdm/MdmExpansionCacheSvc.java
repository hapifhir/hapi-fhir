package ca.uhn.fhir.jpa.dao.mdm;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * The purpose of this class is to share context between steps of a given GroupBulkExport job.
 *
 * This cache allows you to port state between reader/processor/writer. In this case, we are maintaining
 * a cache of Source Resource ID -> Golden Resource ID, so that we can annotate outgoing resources with their golden owner
 * if applicable.
 *
 */
public class MdmExpansionCacheSvc {
	private static final Logger ourLog = getLogger(MdmExpansionCacheSvc.class);

	private final ConcurrentHashMap<String, String> mySourceToGoldenIdCache = new ConcurrentHashMap<>();

	/**
	 * Lookup a given resource's golden resource ID in the cache. Note that if you pass this function the resource ID of a
	 * golden resource, it will just return itself.
	 *
	 * @param theSourceId the resource ID of the source resource ,e.g. PAT123
	 * @return the resource ID of the associated golden resource.
	 */
	public String getGoldenResourceId(String theSourceId) {
		ourLog.debug(buildLogMessage("About to lookup cached resource ID " + theSourceId));
		String goldenResourceId = mySourceToGoldenIdCache.get(theSourceId);

		//A golden resources' golden resource ID is itself.
		if (StringUtils.isBlank(goldenResourceId)) {
			if (mySourceToGoldenIdCache.containsValue(theSourceId)) {
				goldenResourceId = theSourceId;
			}
		}
		return goldenResourceId;
	}

	private String buildLogMessage(String theMessage) {
		return buildLogMessage(theMessage, false);
	}

	/**
	 * Builds a log message, potentially enriched with the cache content.
	 *
	 * @param message The log message
	 * @param theAddCacheContentContent If true, will annotate the log message with the current cache contents.
	 * @return a built log message, which may include the cache content.
	 */
	public String buildLogMessage(String message, boolean theAddCacheContentContent) {
		StringBuilder builder = new StringBuilder();
		builder.append(message);
		if (ourLog.isDebugEnabled() || theAddCacheContentContent) {
			builder.append("\n")
				.append("Current cache content is:")
				.append("\n");
			mySourceToGoldenIdCache.entrySet().stream().forEach(entry -> builder.append(entry.getKey()).append(" -> ").append(entry.getValue()).append("\n"));
			return builder.toString();
		}
		return builder.toString();

	}

	/**
	 * Populate the cache
	 *
	 * @param theSourceResourceIdToGoldenResourceIdMap the source ID -> golden ID map to populate the cache with.
	 */
	public void setCacheContents(Map<String, String> theSourceResourceIdToGoldenResourceIdMap) {
		if (mySourceToGoldenIdCache.isEmpty()) {
			this.mySourceToGoldenIdCache.putAll(theSourceResourceIdToGoldenResourceIdMap);
		}
	}

	/**
	 * Since this cache is used at @JobScope, we can skip a whole whack of expansions happening by simply checking
	 * if one of our child steps has populated the cache yet. .
	 */
	public boolean hasBeenPopulated() {
		return !mySourceToGoldenIdCache.isEmpty();
	}
}
