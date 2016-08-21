package ca.uhn.fhir.jpa.search;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.IDao;
import ca.uhn.fhir.jpa.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.dao.data.ISearchResultDao;
import ca.uhn.fhir.rest.server.FifoMemoryPagingProvider;
import ca.uhn.fhir.rest.server.IBundleProvider;

public class DatabaseBackedPagingProvider extends FifoMemoryPagingProvider {

	@Autowired
	private PlatformTransactionManager thePlatformTransactionManager;
	@Autowired
	private ISearchResultDao theSearchResultDao;
	@Autowired
	private EntityManager theEntityManager;
	@Autowired
	private FhirContext theContext;
	@Autowired
	private IFhirSystemDao<?, ?> theDao;

	public DatabaseBackedPagingProvider(int theSize) {
		super(theSize);
	}

	@Override
	public synchronized IBundleProvider retrieveResultList(String theId) {
		IBundleProvider retVal = super.retrieveResultList(theId);
		if (retVal == null) {
			PersistedJpaBundleProvider provider = new PersistedJpaBundleProvider(theId, theDao);
			if (!provider.ensureSearchEntityLoaded()) {
				return null;
			}
			retVal = provider;
		}
		return retVal;
	}

	@Override
	public synchronized String storeResultList(IBundleProvider theList) {
		if (theList instanceof PersistedJpaBundleProvider) {
			return ((PersistedJpaBundleProvider)theList).getSearchUuid();
		}
		return super.storeResultList(theList);
	}

}
