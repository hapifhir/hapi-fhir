/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.search;

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.dao.SearchBuilderFactory;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.BasePagingProvider;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;

// Note: this class is not annotated with @Service because we want to
// explicitly define it in BaseConfig.java. This is done so that
// implementors can override if they want to.
public class DatabaseBackedPagingProvider extends BasePagingProvider {

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	private SearchBuilderFactory mySearchBuilderFactory;

	@Autowired
	private PersistedJpaBundleProviderFactory myPersistedJpaBundleProviderFactory;

	@Autowired
	private IRequestPartitionHelperSvc myRequestPartitionHelperSvc;

	/**
	 * Constructor
	 */
	public DatabaseBackedPagingProvider() {
		super();
	}

	/**
	 * Constructor
	 *
	 * @deprecated Use {@link DatabaseBackedPagingProvider} as this constructor has no purpose
	 */
	@Deprecated
	public DatabaseBackedPagingProvider(int theSize) {
		this();
	}

	@Override
	public synchronized IBundleProvider retrieveResultList(RequestDetails theRequestDetails, String theId) {
		PersistedJpaBundleProvider provider = myPersistedJpaBundleProviderFactory.newInstance(theRequestDetails, theId);
		return validateAndReturnBundleProvider(provider);
	}

	/**
	 * Subclasses may override in order to modify the bundle provider being returned
	 */
	@Nullable
	protected PersistedJpaBundleProvider validateAndReturnBundleProvider(PersistedJpaBundleProvider theBundleProvider) {
		if (!theBundleProvider.ensureSearchEntityLoaded()) {
			return null;
		}
		return theBundleProvider;
	}

	@Override
	public synchronized String storeResultList(RequestDetails theRequestDetails, IBundleProvider theList) {
		String uuid = theList.getUuid();
		return uuid;
	}
}
