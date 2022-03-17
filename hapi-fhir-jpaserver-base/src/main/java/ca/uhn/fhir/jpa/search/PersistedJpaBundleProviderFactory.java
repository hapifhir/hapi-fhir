package ca.uhn.fhir.jpa.search;

/*-
 * #%L
 * HAPI FHIR JPA Server
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

import ca.uhn.fhir.jpa.config.JpaConfig;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class PersistedJpaBundleProviderFactory {

	@Autowired
	private ApplicationContext myApplicationContext;

	public PersistedJpaBundleProvider newInstance(RequestDetails theRequest, String theUuid) {
		Object retVal = myApplicationContext.getBean(JpaConfig.PERSISTED_JPA_BUNDLE_PROVIDER, theRequest, theUuid);
		return (PersistedJpaBundleProvider) retVal;
	}

	public PersistedJpaBundleProvider newInstance(RequestDetails theRequest, Search theSearch) {
		Object retVal = myApplicationContext.getBean(JpaConfig.PERSISTED_JPA_BUNDLE_PROVIDER_BY_SEARCH, theRequest, theSearch);
		return (PersistedJpaBundleProvider) retVal;
	}

	public PersistedJpaSearchFirstPageBundleProvider newInstanceFirstPage(RequestDetails theRequestDetails, Search theSearch, SearchCoordinatorSvcImpl.SearchTask theTask, ISearchBuilder theSearchBuilder) {
		return (PersistedJpaSearchFirstPageBundleProvider) myApplicationContext.getBean(JpaConfig.PERSISTED_JPA_SEARCH_FIRST_PAGE_BUNDLE_PROVIDER, theRequestDetails, theSearch, theTask, theSearchBuilder);
	}
}
