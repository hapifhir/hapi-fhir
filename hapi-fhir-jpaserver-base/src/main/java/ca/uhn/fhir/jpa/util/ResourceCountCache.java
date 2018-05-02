package ca.uhn.fhir.jpa.util;

import java.util.Map;
import java.util.concurrent.Callable;

public class ResourceCountCache extends SingleItemLoadingCache<Map<String, Long>> {
	/**
	 * Constructor
	 */
	public ResourceCountCache(Callable<Map<String, Long>> theFetcher) {
		super(theFetcher);
	}
}
