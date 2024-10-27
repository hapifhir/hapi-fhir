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
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoSearchParameter;
import ca.uhn.fhir.jpa.dao.validation.SearchParameterDaoValidator;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.hapi.converters.canonical.VersionCanonicalizer;
import com.google.common.annotations.VisibleForTesting;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.Enumeration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class JpaResourceDaoSearchParameter<T extends IBaseResource> extends BaseHapiFhirResourceDao<T>
		implements IFhirResourceDaoSearchParameter<T> {

	private final AtomicBoolean myCacheReloadTriggered = new AtomicBoolean(false);

	@Autowired
	private VersionCanonicalizer myVersionCanonicalizer;

	@Autowired
	private SearchParameterDaoValidator mySearchParameterDaoValidator;

	protected void reindexAffectedResources(T theResource, RequestDetails theRequestDetails) {

		/*
		 * After we commit, flush the search parameter cache. This only helps on the
		 * local server (ie in a cluster the other servers won't be flushed) but
		 * the cache is short anyhow, and  flushing locally is better than nothing.
		 * Many use cases where you would create a search parameter and immediately
		 * try to use it tend to be on single-server setups anyhow, e.g. unit tests
		 */
		if (!shouldSkipReindex(theRequestDetails)) {
			if (!myCacheReloadTriggered.getAndSet(true)) {
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
					@Override
					public void afterCommit() {
						myTransactionService
								.withSystemRequest()
								.withPropagation(Propagation.NOT_SUPPORTED)
								.execute(() -> {
									// do this outside any current tx.
									myCacheReloadTriggered.set(false);
									mySearchParamRegistry.forceRefresh();
								});
					}
				});
			}
		}

		// N.B. Don't do this on the canonicalized version
		Boolean reindex = theResource != null ? CURRENTLY_REINDEXING.get(theResource) : null;

		org.hl7.fhir.r5.model.SearchParameter searchParameter =
				myVersionCanonicalizer.searchParameterToCanonical(theResource);
		List<String> base = theResource != null
				? searchParameter.getBase().stream().map(Enumeration::getCode).collect(Collectors.toList())
				: null;
		requestReindexForRelatedResources(reindex, base, theRequestDetails);
	}

	@Override
	protected void postPersist(ResourceTable theEntity, T theResource, RequestDetails theRequestDetails) {
		super.postPersist(theEntity, theResource, theRequestDetails);
		reindexAffectedResources(theResource, theRequestDetails);
	}

	@Override
	protected void postUpdate(ResourceTable theEntity, T theResource, RequestDetails theRequestDetails) {
		super.postUpdate(theEntity, theResource, theRequestDetails);
		reindexAffectedResources(theResource, theRequestDetails);
	}

	@Override
	protected void preDelete(T theResourceToDelete, ResourceTable theEntityToDelete, RequestDetails theRequestDetails) {
		super.preDelete(theResourceToDelete, theEntityToDelete, theRequestDetails);
		reindexAffectedResources(theResourceToDelete, theRequestDetails);
	}

	@Override
	protected void validateResourceForStorage(T theResource, ResourceTable theEntityToSave) {
		super.validateResourceForStorage(theResource, theEntityToSave);

		validateSearchParam(theResource);
	}

	public void validateSearchParam(IBaseResource theResource) {
		org.hl7.fhir.r5.model.SearchParameter searchParameter =
				myVersionCanonicalizer.searchParameterToCanonical(theResource);
		mySearchParameterDaoValidator.validate(searchParameter);
	}

	@VisibleForTesting
	void setVersionCanonicalizerForUnitTest(VersionCanonicalizer theVersionCanonicalizer) {
		myVersionCanonicalizer = theVersionCanonicalizer;
	}

	@VisibleForTesting
	public void setSearchParameterDaoValidatorForUnitTest(SearchParameterDaoValidator theSearchParameterDaoValidator) {
		mySearchParameterDaoValidator = theSearchParameterDaoValidator;
	}
}
