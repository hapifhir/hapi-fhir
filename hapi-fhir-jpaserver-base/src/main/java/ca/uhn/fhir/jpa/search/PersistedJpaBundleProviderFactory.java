/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.config.JpaConfig;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.entity.SearchTypeEnum;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.search.SearchStatusEnum;
import ca.uhn.fhir.jpa.search.builder.tasks.SearchTask;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.HistorySearchStyleEnum;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

import static ca.uhn.fhir.jpa.config.JpaConfig.PREDICATED_PERSISTED_JPA_BUNDLE_PROVIDER_BY_SEARCH;
import static ca.uhn.fhir.model.dstu2.resource.BaseResource.SP_RES_ID;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;

public class PersistedJpaBundleProviderFactory {

	@Autowired
	private ApplicationContext myApplicationContext;

	public PersistedJpaBundleProvider newInstance(RequestDetails theRequest, String theUuid) {
		Object retVal = myApplicationContext.getBean(JpaConfig.PERSISTED_JPA_BUNDLE_PROVIDER, theRequest, theUuid);
		return (PersistedJpaBundleProvider) retVal;
	}

	public PersistedJpaBundleProvider newInstance(RequestDetails theRequest, Search theSearch) {
		Predicate<? super IBaseResource> predicate = buildIdPredicateFromParameters(theRequest);
		if (predicate != null) {
			Object retVal = myApplicationContext.getBean(
					PREDICATED_PERSISTED_JPA_BUNDLE_PROVIDER_BY_SEARCH, theRequest, theSearch, predicate);
			return (PersistedJpaBundleProvider) retVal;
		}

		Object retVal =
				myApplicationContext.getBean(JpaConfig.PERSISTED_JPA_BUNDLE_PROVIDER_BY_SEARCH, theRequest, theSearch);
		return (PersistedJpaBundleProvider) retVal;
	}

	private Predicate<? super IBaseResource> buildIdPredicateFromParameters(RequestDetails theRequest) {
		String[] idParam = theRequest.getParameters().get(SP_RES_ID);
		if (idParam == null || idParam.length == 0) {
			return null;
		}
		return res -> List.of(idParam)
				.contains(res.getIdElement().toUnqualifiedVersionless().getValue());
	}

	public PersistedJpaSearchFirstPageBundleProvider newInstanceFirstPage(
			RequestDetails theRequestDetails,
			SearchTask theTask,
			ISearchBuilder<JpaPid> theSearchBuilder,
			RequestPartitionId theRequestPartitionId) {
		return (PersistedJpaSearchFirstPageBundleProvider) myApplicationContext.getBean(
				JpaConfig.PERSISTED_JPA_SEARCH_FIRST_PAGE_BUNDLE_PROVIDER,
				theRequestDetails,
				theTask,
				theSearchBuilder,
				theRequestPartitionId);
	}

	public IBundleProvider history(
			RequestDetails theRequest,
			String theResourceType,
			@Nullable JpaPid theResourcePid,
			Date theRangeStartInclusive,
			Date theRangeEndInclusive,
			Integer theOffset,
			RequestPartitionId theRequestPartitionId) {
		return history(
				theRequest,
				theResourceType,
				theResourcePid,
				theRangeStartInclusive,
				theRangeEndInclusive,
				theOffset,
				null,
				theRequestPartitionId);
	}

	public IBundleProvider history(
			RequestDetails theRequest,
			String theResourceType,
			@Nullable JpaPid theResourcePid,
			Date theRangeStartInclusive,
			Date theRangeEndInclusive,
			Integer theOffset,
			HistorySearchStyleEnum searchParameterType,
			RequestPartitionId theRequestPartitionId) {
		String resourceName = defaultIfBlank(theResourceType, null);

		Search search = new Search();
		search.setOffset(theOffset);
		search.setDeleted(false);
		search.setCreated(new Date());
		search.setLastUpdated(theRangeStartInclusive, theRangeEndInclusive);
		search.setUuid(UUID.randomUUID().toString());
		search.setResourceType(resourceName);
		search.setResourceId(theResourcePid);
		search.setSearchType(SearchTypeEnum.HISTORY);
		search.setStatus(SearchStatusEnum.FINISHED);
		search.setHistorySearchStyle(searchParameterType);

		PersistedJpaBundleProvider provider = newInstance(theRequest, search);
		provider.setRequestPartitionId(theRequestPartitionId);

		return provider;
	}
}
