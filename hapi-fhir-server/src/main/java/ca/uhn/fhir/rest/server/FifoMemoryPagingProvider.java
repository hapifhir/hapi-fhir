package ca.uhn.fhir.rest.server;

/*
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.apache.commons.lang3.Validate;

import java.util.LinkedHashMap;
import java.util.UUID;

public class FifoMemoryPagingProvider extends BasePagingProvider {

	private final LinkedHashMap<String, IBundleProvider> myBundleProviders;
	private final int mySize;

	public FifoMemoryPagingProvider(int theSize) {
		Validate.isTrue(theSize > 0, "theSize must be greater than 0");

		mySize = theSize;
		myBundleProviders = new LinkedHashMap<>(mySize);
	}

	@Override
	public synchronized IBundleProvider retrieveResultList(RequestDetails theRequest, String theId) {
		return myBundleProviders.get(theId);
	}

	@Override
	public synchronized String storeResultList(RequestDetails theRequestDetails, IBundleProvider theList) {
		while (myBundleProviders.size() > mySize) {
			myBundleProviders.remove(myBundleProviders.keySet().iterator().next());
		}

		String key = UUID.randomUUID().toString();
		myBundleProviders.put(key, theList);
		return key;
	}

}
