package ca.uhn.fhir.jpa.search.warm;

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
