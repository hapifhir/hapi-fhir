package ca.uhn.fhir.rest.server;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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

	public FifoMemoryPagingProvider setDefaultPageSize(int theDefaultPageSize) {
		Validate.isTrue(theDefaultPageSize > 0, "size must be greater than 0");
		myDefaultPageSize = theDefaultPageSize;
		return this;
	}

	public FifoMemoryPagingProvider setMaximumPageSize(int theMaximumPageSize) {
		Validate.isTrue(theMaximumPageSize > 0, "size must be greater than 0");
		myMaximumPageSize = theMaximumPageSize;
		return this;
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
