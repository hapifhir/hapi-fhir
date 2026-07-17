/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.dao.index;

import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.searchparam.extractor.ResourceIndexedSearchParams;
import ca.uhn.fhir.jpa.util.AddRemoveCount;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;

/**
 * Extension point for contributing a custom secondary index that is kept in sync alongside the
 * built-in JPA search-parameter indexes.
 *
 * <p>Implementations are discovered as Spring beans and invoked by {@link DaoSearchParamSynchronizer}
 * once per resource create/update, after the built-in indexes have been synchronized. Each
 * implementation is responsible for reconciling its own tables for the given resource: it loads its
 * current rows, diffs them against the desired state derived from {@code theParams}, and persists the
 * difference. HAPI remains agnostic of the custom index's schema.
 *
 * <p>Vanilla HAPI registers no implementations, so this is a no-op unless an extension (e.g. Smile CDR
 * compressed token indexing) provides a bean.
 *
 * <p>Implementations own the full lifecycle of their rows: {@link #synchronize} covers create and
 * update (a FHIR delete is an update with empty extracted params, so it flows through the same
 * diff), and {@link #deleteByResourceId} covers physical row deletion during {@code $expunge},
 * invoked from {@code IResourceExpungeService#deleteAllSearchParams}.
 */
public interface ICustomIndexSynchronizer {

	/**
	 * Reconcile the custom index for a single resource.
	 *
	 * @param theRequestDetails   the request context (may be {@code null} for internal operations)
	 * @param theTransactionDetails the active transaction context
	 * @param theParams           the freshly extracted (desired) search parameters
	 * @param theEntity           the resource entity being indexed
	 * @param theResourceIsBeingCreated {@code true} if this is the first-ever persist of the resource,
	 *     so no rows can pre-exist; implementations may skip loading current rows to reconcile against
	 * @return the number of rows this implementation added and removed, never {@code null}; the caller
	 *     adds it to the overall synchronization total
	 */
	AddRemoveCount synchronize(
			RequestDetails theRequestDetails,
			TransactionDetails theTransactionDetails,
			ResourceIndexedSearchParams theParams,
			ResourceTable theEntity,
			boolean theResourceIsBeingCreated);

	/**
	 * Physically delete every row the custom index holds for the given resource. Invoked during
	 * {@code $expunge}, alongside the built-in index deletions. Unlike the built-in indexes there is
	 * no populated-flag guard, so implementations must tolerate being called when no rows exist.
	 *
	 * @param theResourceId the id of the resource being expunged
	 */
	void deleteByResourceId(JpaPid theResourceId);
}
