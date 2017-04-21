package ca.uhn.fhir.jpa.search;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

import javax.persistence.EntityManager;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.IDao;
import ca.uhn.fhir.jpa.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.dao.data.ISearchResultDao;
import ca.uhn.fhir.rest.server.BasePagingProvider;
import ca.uhn.fhir.rest.server.FifoMemoryPagingProvider;
import ca.uhn.fhir.rest.server.IBundleProvider;
import ca.uhn.fhir.rest.server.IPagingProvider;

public class DatabaseBackedPagingProvider extends BasePagingProvider implements IPagingProvider {

	@Autowired
	private FhirContext myContext;
	@Autowired
	private IFhirSystemDao<?, ?> myDao;
	@Autowired
	private EntityManager myEntityManager;
	@Autowired
	private PlatformTransactionManager myPlatformTransactionManager;
	@Autowired
	private ISearchResultDao mySearchResultDao;

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
	public synchronized IBundleProvider retrieveResultList(String theId) {
		PersistedJpaBundleProvider provider = new PersistedJpaBundleProvider(theId, myDao);
		if (!provider.ensureSearchEntityLoaded()) {
			return null;
		}
		return provider;
	}

	@Override
	public synchronized String storeResultList(IBundleProvider theList) {
		String uuid = theList.getUuid();
		Validate.notNull(uuid);
		return uuid;
	}

}
