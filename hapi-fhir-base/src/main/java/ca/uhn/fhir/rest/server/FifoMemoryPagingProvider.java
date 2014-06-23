package ca.uhn.fhir.rest.server;

import java.util.LinkedHashMap;
import java.util.UUID;

import org.apache.commons.lang3.Validate;

public class FifoMemoryPagingProvider implements IPagingProvider {

	private LinkedHashMap<String, IBundleProvider> myBundleProviders;
	private int myDefaultPageSize=10;
	private int myMaximumPageSize=50;
	private int mySize;

	public FifoMemoryPagingProvider(int theSize) {
		Validate.isTrue(theSize > 0, "theSize must be greater than 0");

		mySize = theSize;
		myBundleProviders = new LinkedHashMap<String, IBundleProvider>(mySize);
	}

	@Override
	public int getDefaultPageSize() {
		return myDefaultPageSize;
	}

	@Override
	public int getMaximumPageSize() {
		return myMaximumPageSize;
	}

	@Override
	public synchronized IBundleProvider retrieveResultList(String theId) {
		return myBundleProviders.get(theId);
	}

	public synchronized void setDefaultPageSize(int theDefaultPageSize) {
		Validate.isTrue(theDefaultPageSize > 0, "size must be greater than 0");
		myDefaultPageSize = theDefaultPageSize;
	}

	public synchronized void setMaximumPageSize(int theMaximumPageSize) {
		Validate.isTrue(theMaximumPageSize > 0, "size must be greater than 0");
		myMaximumPageSize = theMaximumPageSize;
	}

	@Override
	public synchronized String storeResultList(IBundleProvider theList) {
		while (myBundleProviders.size() > mySize) {
			myBundleProviders.remove(myBundleProviders.keySet().iterator().next());
		}

		String key = UUID.randomUUID().toString();
		myBundleProviders.put(key, theList);
		return key;
	}

}
