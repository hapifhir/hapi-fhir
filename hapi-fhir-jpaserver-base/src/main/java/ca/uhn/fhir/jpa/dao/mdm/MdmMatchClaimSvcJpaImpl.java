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
package ca.uhn.fhir.jpa.dao.mdm;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.dao.data.IMdmMatchClaimJpaRepository;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.entity.MdmMatchClaimEntity;
import ca.uhn.fhir.jpa.model.util.SearchParamHash;
import ca.uhn.fhir.mdm.dao.IMdmMatchClaimSvc;
import ca.uhn.fhir.mdm.dao.MdmMatchClaimKey;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * JPA implementation of {@link IMdmMatchClaimSvc}, storing claims in {@link MdmMatchClaimEntity}. Modelled on
 * {@link ca.uhn.fhir.jpa.search.ResourceSearchUrlSvc}, which uses the same pattern for conditional creates.
 */
// Created by claude-opus-5-5
public class MdmMatchClaimSvcJpaImpl implements IMdmMatchClaimSvc {
	private static final Logger ourLog = LoggerFactory.getLogger(MdmMatchClaimSvcJpaImpl.class);

	/**
	 * Stale-claim deletion is paged so no single statement scans the whole table, and capped under MSSQL's
	 * 2,100-parameter-per-statement limit.
	 */
	static final int DELETE_PAGE_SIZE = 1_800;

	/**
	 * Safety cap on page iterations per sweep. Anything left is removed by the next scheduled run.
	 */
	static final int MAX_DELETE_PAGES = 10_000;

	private final EntityManager myEntityManager;
	private final IMdmMatchClaimJpaRepository myRepository;
	private final TransactionTemplate myPageTxTemplate;
	private final Object myHeldClaimsResourceKey = new Object();

	public MdmMatchClaimSvcJpaImpl(
			EntityManager theEntityManager,
			IMdmMatchClaimJpaRepository theRepository,
			PlatformTransactionManager theTxManager) {
		myEntityManager = theEntityManager;
		myRepository = theRepository;
		myPageTxTemplate = new TransactionTemplate(theTxManager);
	}

	@Nonnull
	@Override
	public Map<MdmMatchClaimKey, Long> findExistingClaims(@Nonnull Collection<MdmMatchClaimKey> theKeys) {
		HapiTransactionService.requireTransaction();
		if (theKeys.isEmpty()) {
			return Map.of();
		}

		// Several keys can share a hash, and they all share the one stored claim
		Map<Long, List<MdmMatchClaimKey>> keysByHash = new HashMap<>();
		for (MdmMatchClaimKey key : theKeys) {
			keysByHash
					.computeIfAbsent(toHash(key), theHash -> new ArrayList<>())
					.add(key);
		}

		Map<MdmMatchClaimKey, Long> retVal = new HashMap<>();
		for (Object[] row : myRepository.findTokens(keysByHash.keySet())) {
			Long token = (Long) row[1];
			keysByHash.getOrDefault((Long) row[0], List.of()).forEach(key -> retVal.put(key, token));
		}
		return retVal;
	}

	@Override
	public void claim(
			@Nonnull Collection<MdmMatchClaimKey> theKeys,
			@Nullable IResourcePersistentId<?> theClaimant,
			@Nonnull Map<MdmMatchClaimKey, Long> theTakeoverTokens) {
		HapiTransactionService.requireTransaction();

		// Sorted, and de-duplicated on the stored key, so that a hash collision can't make us conflict with ourselves
		Map<Long, MdmMatchClaimKey> keysByHash = new LinkedHashMap<>();
		theKeys.stream().sorted().forEach(key -> keysByHash.putIfAbsent(toHash(key), key));

		Long claimant = theClaimant != null && theClaimant.getId() instanceof Long id ? id : null;
		Set<Long> heldByThisTransaction = getClaimsHeldByCurrentTransaction();
		try {
			for (Map.Entry<Long, MdmMatchClaimKey> next : keysByHash.entrySet()) {
				Long hash = next.getKey();
				MdmMatchClaimKey key = next.getValue();
				if (heldByThisTransaction.contains(hash)) {
					// Already claimed earlier in this transaction, e.g. by another MDM update joining it
					continue;
				}

				Long takeoverToken = theTakeoverTokens.get(key);
				if (takeoverToken != null) {
					// A bulk delete, because Hibernate would otherwise run the insert before the delete
					myRepository.deleteByHashAndToken(hash, takeoverToken);
				}

				MdmMatchClaimEntity entity = new MdmMatchClaimEntity()
						.setClaimHash(hash)
						.setClaimType(key.type().name())
						.setClaimKey(key.canonicalKey())
						.setClaimToken(ThreadLocalRandom.current().nextLong())
						.setClaimantResourceId(claimant)
						.setCreatedTime(new Date());
				myEntityManager.persist(entity);
				myEntityManager.flush();
				heldByThisTransaction.add(hash);
			}
		} catch (PersistenceException | DataAccessException e) {
			if (!HapiTransactionService.isRetriable(e)) {
				throw e;
			}
			ourLog.debug("MDM match claim conflict for claimant {}: {}", claimant, e.toString());
			throw new ResourceVersionConflictException(
					Msg.code(3062) + "MDM match claim conflict: another MDM operation is processing a related resource",
					e,
					null);
		}
	}

	/**
	 * Deletes claims created before {@code theCutoffDate}, in fixed-size pages that each commit in their own
	 * transaction. Must not be called inside a transaction.
	 */
	public void deleteEntriesOlderThan(Date theCutoffDate) {
		long totalDeleted = 0;
		boolean moreStaleRemain = true;
		for (int pageIndex = 0; moreStaleRemain && pageIndex < MAX_DELETE_PAGES; pageIndex++) {
			int[] outcome = myPageTxTemplate.execute(theStatus -> {
				Slice<Long> stale = myRepository.findStaleHashes(theCutoffDate, PageRequest.of(0, DELETE_PAGE_SIZE));
				if (stale.isEmpty()) {
					return new int[] {0, 0};
				}
				int deleted = myRepository.deleteStale(stale.getContent(), theCutoffDate);
				return new int[] {deleted, stale.hasNext() ? 1 : 0};
			});
			totalDeleted += outcome[0];
			moreStaleRemain = outcome[1] == 1;
		}
		if (moreStaleRemain) {
			ourLog.warn(
					"Reached the maximum of {} delete pages after removing {} MDM match claims; the remainder will be"
							+ " removed by the next scheduled run",
					MAX_DELETE_PAGES,
					totalDeleted);
		}
		ourLog.debug("Deleted {} stale MDM match claims", totalDeleted);
	}

	/**
	 * The claims taken so far by the current transaction, which are released when it completes.
	 */
	@SuppressWarnings("unchecked")
	private Set<Long> getClaimsHeldByCurrentTransaction() {
		Set<Long> retVal = (Set<Long>) TransactionSynchronizationManager.getResource(myHeldClaimsResourceKey);
		if (retVal == null) {
			Set<Long> heldClaims = new HashSet<>();
			TransactionSynchronizationManager.bindResource(myHeldClaimsResourceKey, heldClaims);
			TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
				@Override
				public void afterCompletion(int theStatus) {
					TransactionSynchronizationManager.unbindResourceIfPossible(myHeldClaimsResourceKey);
				}
			});
			retVal = heldClaims;
		}
		return retVal;
	}

	static long toHash(MdmMatchClaimKey theKey) {
		return SearchParamHash.hashSearchParam(theKey.canonicalKey());
	}
}
