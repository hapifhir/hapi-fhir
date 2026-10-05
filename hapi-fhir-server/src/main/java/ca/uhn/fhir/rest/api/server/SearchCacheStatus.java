/*-
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.rest.api.server;

import java.util.Date;

/**
 * Cache response statuses as defined in <a href="RFC 9211">https://datatracker.ietf.org/doc/rfc9211/</a>
 */
public class SearchCacheStatus {

	private final String myCacheName;
	private final SearchCacheStatusEnum myStatus;
	private final Date myCacheEntryTimestamp;

	private SearchCacheStatus(String theCacheName, SearchCacheStatusEnum theStatus, Date theCacheEntryTimestamp) {
		myCacheName = theCacheName;
		myStatus = theStatus;
		myCacheEntryTimestamp = theCacheEntryTimestamp;
	}

	public String getCacheName() {
		return myCacheName;
	}

	public SearchCacheStatusEnum getStatus() {
		return myStatus;
	}

	public Date getCacheEntryTimestamp() {
		return myCacheEntryTimestamp;
	}

	/**
	 * Statuses as defined in <a href="RFC 9211">https://datatracker.ietf.org/doc/rfc9211/</a>
	 */
	public enum SearchCacheStatusEnum {
		/**
		 * We did not attempt to check whether the search was a cache hit against a query cache
		 */
		FWD_BYPASS,
		/**
		 * A search cache was checked, and no cache hit was found
		 */
		FWD_MISS,
		/**
		 * A search cache hit was detected
		 */
		HIT
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		private String myCacheName;
		private SearchCacheStatusEnum myStatus;
		private Date myCacheEntryTimestamp;

		public Builder withCacheName(String theCacheName) {
			myCacheName = theCacheName;
			return this;
		}

		public Builder setStatus(SearchCacheStatusEnum theStatus) {
			myStatus = theStatus;
			return this;
		}

		public Builder setCacheEntryTimestamp(Date theCacheEntryTimestamp) {
			myCacheEntryTimestamp = theCacheEntryTimestamp;
			return this;
		}

		public SearchCacheStatus build() {
			return new SearchCacheStatus(myCacheName, myStatus, myCacheEntryTimestamp);
		}
	}
}
