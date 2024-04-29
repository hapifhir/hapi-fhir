/*-
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
package ca.uhn.fhir.jpa.search.builder.tasks;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.SearchBuilderFactory;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.search.SearchStatusEnum;
import ca.uhn.fhir.jpa.search.ExceptionService;
import ca.uhn.fhir.jpa.search.cache.ISearchCacheSvc;
import ca.uhn.fhir.jpa.search.cache.ISearchResultCacheSvc;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;

import java.util.List;

public class SearchContinuationTask extends SearchTask {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchContinuationTask.class);

	private final ExceptionService myExceptionSvc;
	private final RequestDetails myRequestDetails;

	public SearchContinuationTask(
			SearchTaskParameters theCreationParams,
			HapiTransactionService theTxService,
			FhirContext theContext,
			IInterceptorBroadcaster theInterceptorBroadcaster,
			SearchBuilderFactory theSearchBuilderFactory,
			ISearchResultCacheSvc theSearchResultCacheSvc,
			JpaStorageSettings theStorageSettings,
			ISearchCacheSvc theSearchCacheSvc,
			IPagingProvider thePagingProvider,
			ExceptionService theExceptionSvc) {
		super(
				theCreationParams,
				theTxService,
				theContext,
				theInterceptorBroadcaster,
				theSearchBuilderFactory,
				theSearchResultCacheSvc,
				theStorageSettings,
				theSearchCacheSvc,
				thePagingProvider);

		myRequestDetails = theCreationParams.Request;
		myExceptionSvc = theExceptionSvc;
	}

	@Override
	public Void call() {
		try {
			RequestPartitionId requestPartitionId = getRequestPartitionId();
			myTxService
					.withRequest(myRequestDetails)
					.withRequestPartitionId(requestPartitionId)
					.execute(() -> {
						List<JpaPid> previouslyAddedResourcePids = mySearchResultCacheSvc.fetchAllResultPids(
								getSearch(), myRequestDetails, requestPartitionId);
						if (previouslyAddedResourcePids == null) {
							throw myExceptionSvc.newUnknownSearchException(
									getSearch().getUuid());
						}

						ourLog.trace(
								"Have {} previously added IDs in search: {}",
								previouslyAddedResourcePids.size(),
								getSearch().getUuid());
						setPreviouslyAddedResourcePids(previouslyAddedResourcePids);
					});

		} catch (Throwable e) {
			ourLog.error("Failure processing search", e);
			getSearch().setFailureMessage(e.getMessage());
			getSearch().setStatus(SearchStatusEnum.FAILED);
			if (e instanceof BaseServerResponseException) {
				getSearch().setFailureCode(((BaseServerResponseException) e).getStatusCode());
			}

			saveSearch();
			return null;
		}

		return super.call();
	}
}
