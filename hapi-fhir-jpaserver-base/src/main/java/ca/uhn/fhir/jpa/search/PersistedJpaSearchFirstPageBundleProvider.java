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
package ca.uhn.fhir.jpa.search;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.entity.SearchTypeEnum;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.search.SearchStatusEnum;
import ca.uhn.fhir.jpa.search.builder.tasks.SearchTask;
import ca.uhn.fhir.jpa.util.QueryParameterUtils;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.valueset.BundleEntrySearchModeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.method.ResponsePage;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PersistedJpaSearchFirstPageBundleProvider extends PersistedJpaBundleProvider {
	private static final Logger ourLog = LoggerFactory.getLogger(PersistedJpaSearchFirstPageBundleProvider.class);
	private final SearchTask mySearchTask;

	@SuppressWarnings("rawtypes")
	private final ISearchBuilder mySearchBuilder;

	/**
	 * Constructor
	 */
	@SuppressWarnings("rawtypes")
	public PersistedJpaSearchFirstPageBundleProvider(
			SearchTask theSearchTask,
			ISearchBuilder theSearchBuilder,
			RequestDetails theRequest,
			RequestPartitionId theRequestPartitionId) {
		super(theRequest, theSearchTask.getSearch());

		assert getSearchEntity().getSearchType() != SearchTypeEnum.HISTORY;

		mySearchTask = theSearchTask;
		mySearchBuilder = theSearchBuilder;
		super.setRequestPartitionId(theRequestPartitionId);
	}

	@Nonnull
	@Override
	public List<IBaseResource> getResources(
			int theFromIndex, int theToIndex, @Nonnull ResponsePage.ResponsePageBuilder thePageBuilder) {
		ensureSearchEntityLoaded();
		QueryParameterUtils.verifySearchHasntFailedOrThrowInternalErrorException(getSearchEntity());

		mySearchTask.awaitInitialSync();

		// request 1 more than we need to, in order to know if there are extra values
		ourLog.trace("Fetching search resource PIDs from task: {}", mySearchTask.getClass());
		final List<JpaPid> pids = mySearchTask.getResourcePids(theFromIndex, theToIndex + 1);
		ourLog.trace("Done fetching search resource PIDs");

		int countOfPids = pids.size();

		int maxSize = Math.min(theToIndex - theFromIndex, countOfPids);
		thePageBuilder.setTotalRequestedResourcesFetched(countOfPids);

		RequestPartitionId requestPartitionId = getRequestPartitionId();

		List<JpaPid> firstBatch = pids.subList(0, maxSize);
		List<IBaseResource> retVal = myTxService
				.withRequest(myRequest)
				.withRequestPartitionId(requestPartitionId)
				.execute(() -> toResourceList(mySearchBuilder, firstBatch, thePageBuilder));

		long totalCountWanted = theToIndex - theFromIndex;
		long totalCountMatch = (int) retVal.stream().filter(t -> !isInclude(t)).count();

		if (totalCountMatch < totalCountWanted) {
			if (getSearchEntity().getStatus() == SearchStatusEnum.PASSCMPLET
					|| ((getSearchEntity().getStatus() == SearchStatusEnum.FINISHED
							&& getSearchEntity().getNumFound() >= theToIndex))) {

				/*
				 * This is a bit of complexity to account for the possibility that
				 * the consent service has filtered some results.
				 */
				Set<String> existingIds = retVal.stream()
						.map(t -> t.getIdElement().getValue())
						.filter(t -> t != null)
						.collect(Collectors.toSet());

				long remainingWanted = totalCountWanted - totalCountMatch;
				long fromIndex = theToIndex - remainingWanted;
				ResponsePage.ResponsePageBuilder pageBuilder = new ResponsePage.ResponsePageBuilder();
				pageBuilder.setBundleProvider(this);
				List<IBaseResource> remaining = super.getResources((int) fromIndex, theToIndex, pageBuilder);
				remaining.forEach(t -> {
					if (!existingIds.contains(t.getIdElement().getValue())) {
						retVal.add(t);
					}
				});
				thePageBuilder.combineWith(pageBuilder);
			}
		}
		ourLog.trace("Loaded resources to return");

		return retVal;
	}

	private boolean isInclude(IBaseResource theResource) {
		BundleEntrySearchModeEnum searchMode = ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.get(theResource);
		return BundleEntrySearchModeEnum.INCLUDE.equals(searchMode);
	}

	@Override
	public Integer size() {
		ourLog.trace("size() - Waiting for initial sync");
		Integer size = mySearchTask.awaitInitialSync();
		ourLog.trace("size() - Finished waiting for local sync");

		ensureSearchEntityLoaded();
		QueryParameterUtils.verifySearchHasntFailedOrThrowInternalErrorException(getSearchEntity());
		if (size != null) {
			return size;
		}
		return super.size();
	}
}
