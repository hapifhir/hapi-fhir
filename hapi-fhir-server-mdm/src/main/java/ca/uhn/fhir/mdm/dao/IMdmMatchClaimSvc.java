/*-
 * #%L
 * HAPI FHIR - Master Data Management
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
package ca.uhn.fhir.mdm.dao;

import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Collection;
import java.util.Map;

/**
 * Stores "match claims", the rows that let concurrent MDM units of work run safely in parallel. This follows
 * the same pattern conditional creates use with the search URL table: a row with a deterministic key is
 * inserted, and the database rejects a concurrent transaction inserting the same key.
 * <p>
 * An MDM unit of work claims its own source resource, the external EIDs it carries, and any matching source
 * resource it found that has no MATCH link yet. It takes these claims as the first statements of its
 * transaction, before it searches for candidates. A competing transaction that holds an overlapping claim
 * makes the insert wait until it commits, after which the insert fails with a
 * {@link ResourceVersionConflictException}. The caller then retries, and its fresh search sees the winner's
 * committed links.
 * <p>
 * Claims stay in place after the transaction commits and are purged once they are older than the
 * configured retention. A stale claim is taken over by passing the token returned by
 * {@link #findExistingClaims(Collection)}, which is safe because that claim's transaction committed before
 * the caller's search.
 */
// Created by claude-opus-5-5
public interface IMdmMatchClaimSvc {

	/**
	 * Reads the claims that already exist for the given keys. Must be called inside the caller's transaction,
	 * before it searches for candidates, so that every claim it returns was committed before that search.
	 *
	 * @param theKeys the keys about to be claimed
	 * @return the token of each existing claim, by key; keys with no claim are absent
	 */
	@Nonnull
	Map<MdmMatchClaimKey, Long> findExistingClaims(@Nonnull Collection<MdmMatchClaimKey> theKeys);

	/**
	 * Claims the given keys in {@link MdmMatchClaimKey#compareTo(MdmMatchClaimKey) sorted order} and flushes
	 * immediately, so that a conflict surfaces here rather than at commit. An existing claim is replaced only
	 * if its token is the one in {@code theTakeoverTokens}.
	 *
	 * @param theKeys           the keys to claim
	 * @param theClaimant       the source resource whose processing makes the claims, for diagnostics
	 * @param theTakeoverTokens tokens previously returned by {@link #findExistingClaims(Collection)} in the same transaction
	 * @throws ResourceVersionConflictException if another transaction holds, or has newly taken, one of the claims
	 */
	void claim(
			@Nonnull Collection<MdmMatchClaimKey> theKeys,
			@Nullable IResourcePersistentId<?> theClaimant,
			@Nonnull Map<MdmMatchClaimKey, Long> theTakeoverTokens);
}
