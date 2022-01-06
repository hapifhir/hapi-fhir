package ca.uhn.fhir.rest.api;

/*-
 * #%L
 * HAPI FHIR - Core Library
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.StringTokenizer;

import static org.apache.commons.lang3.StringUtils.trim;

/**
 * Parses and stores the value(s) within HTTP Cache-Control headers
 */
public class CacheControlDirective {

	private static final String MAX_RESULTS_EQUALS = Constants.CACHE_CONTROL_MAX_RESULTS + "=";
	private static final Logger ourLog = LoggerFactory.getLogger(CacheControlDirective.class);
	private boolean myNoCache;
	private boolean myNoStore;
	private Integer myMaxResults;

	/**
	 * Constructor
	 */
	public CacheControlDirective() {
		super();
	}

	/**
	 * If the {@link #isNoStore() no-store} directive is set, this HAPI FHIR extention
	 * to the <code>Cache-Control</code> header called <code>max-results=123</code>
	 * specified the maximum number of results which will be fetched from the
	 * database before returning.
	 */
	public Integer getMaxResults() {
		return myMaxResults;
	}

	/**
	 * If the {@link #isNoStore() no-store} directive is set, this HAPI FHIR extention
	 * to the <code>Cache-Control</code> header called <code>max-results=123</code>
	 * specified the maximum number of results which will be fetched from the
	 * database before returning.
	 */
	public CacheControlDirective setMaxResults(Integer theMaxResults) {
		myMaxResults = theMaxResults;
		return this;
	}

	/**
	 * If <code>true<</code>, adds the <code>no-cache</code> directive to the
	 * request. This directive indicates that the cache should not be used to
	 * serve this request.
	 */
	public boolean isNoCache() {
		return myNoCache;
	}

	/**
	 * If <code>true<</code>, adds the <code>no-cache</code> directive to the
	 * request. This directive indicates that the cache should not be used to
	 * serve this request.
	 */
	public CacheControlDirective setNoCache(boolean theNoCache) {
		myNoCache = theNoCache;
		return this;
	}

	public boolean isNoStore() {
		return myNoStore;
	}

	public CacheControlDirective setNoStore(boolean theNoStore) {
		myNoStore = theNoStore;
		return this;
	}

	/**
	 * Parses a list of <code>Cache-Control</code> header values
	 *
	 * @param theValues The <code>Cache-Control</code> header values
	 */
	public CacheControlDirective parse(List<String> theValues) {
		if (theValues != null) {
			for (String nextValue : theValues) {
				StringTokenizer tok = new StringTokenizer(nextValue, ",");
				while (tok.hasMoreTokens()) {
					String next = trim(tok.nextToken());
					if (Constants.CACHE_CONTROL_NO_CACHE.equals(next)) {
						myNoCache = true;
					} else if (Constants.CACHE_CONTROL_NO_STORE.equals(next)) {
						myNoStore = true;
					} else if (next.startsWith(MAX_RESULTS_EQUALS)) {
						String valueString = trim(next.substring(MAX_RESULTS_EQUALS.length()));
						try {
							myMaxResults = Integer.parseInt(valueString);
						} catch (NumberFormatException e) {
							ourLog.warn("Invalid {} value: {}", Constants.CACHE_CONTROL_MAX_RESULTS, valueString);
						}

					}
				}
			}
		}

		return this;
	}

	/**
	 * Convenience factory method for a no-cache directivel
	 */
	public static CacheControlDirective noCache() {
		return new CacheControlDirective().setNoCache(true);
	}
}
