package ca.uhn.fhir.jpa.search;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

import ca.uhn.fhir.jpa.dao.DaoRegistry;
import ca.uhn.fhir.jpa.dao.IFhirSystemDao;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.BasePagingProvider;
import ca.uhn.fhir.rest.server.IPagingProvider;
import org.springframework.beans.factory.annotation.Autowired;

// Note: this class is not annotated with @Service because we want to
// explicitly define it in BaseConfig.java. This is done so that
// implementors can override if they want to.
public class DatabaseBackedPagingProvider extends BasePagingProvider implements IPagingProvider {

	@Autowired
	private DaoRegistry myDaoRegistry;

	/**
	 * Constructor
	 */
	public DatabaseBackedPagingProvider() {
		super();
	}

	/**
	 * Constructor
	 * @deprecated Use {@link DatabaseBackedPagingProvider} as this constructor has no purpose
	 */
	@Deprecated
	public DatabaseBackedPagingProvider(int theSize) {
		this();
	}

	@Override
	public synchronized IBundleProvider retrieveResultList(RequestDetails theRequestDetails, String theId) {
		IFhirSystemDao<?, ?> systemDao = myDaoRegistry.getSystemDao();
		PersistedJpaBundleProvider provider = new PersistedJpaBundleProvider(theRequestDetails, theId, systemDao);
		if (!provider.ensureSearchEntityLoaded()) {
			return null;
		}
		return provider;
	}

	@Override
	public synchronized String storeResultList(RequestDetails theRequestDetails, IBundleProvider theList) {
		String uuid = theList.getUuid();
		return uuid;
	}

}
