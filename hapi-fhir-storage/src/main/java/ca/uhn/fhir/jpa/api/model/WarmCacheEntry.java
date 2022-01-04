package ca.uhn.fhir.jpa.api.model;

/*-
 * #%L
 * HAPI FHIR Storage api
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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Denotes a search that should be performed in the background
 * periodically in order to keep a fresh copy in the query cache.
 * This improves performance for searches by keeping a copy
 * loaded in the background.
 */
public class WarmCacheEntry {

	private long myPeriodMillis;
	private String myUrl;

	@Override
	public boolean equals(Object theO) {
		if (this == theO) {
			return true;
		}

		if (theO == null || getClass() != theO.getClass()) {
			return false;
		}

		WarmCacheEntry that = (WarmCacheEntry) theO;

		return new EqualsBuilder()
			.append(myPeriodMillis, that.myPeriodMillis)
			.append(myUrl, that.myUrl)
			.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
			.append(myPeriodMillis)
			.append(myUrl)
			.toHashCode();
	}

	public long getPeriodMillis() {
		return myPeriodMillis;
	}

	public WarmCacheEntry setPeriodMillis(long thePeriodMillis) {
		myPeriodMillis = thePeriodMillis;
		return this;
	}

	public String getUrl() {
		return myUrl;
	}

	public WarmCacheEntry setUrl(String theUrl) {
		myUrl = theUrl;
		return this;
	}

}
