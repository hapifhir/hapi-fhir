/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.dao.expunge;

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.ReadPartitionIdRequestDetails;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.entity.Batch2JobInstanceEntity;
import ca.uhn.fhir.jpa.entity.Batch2WorkChunkEntity;
import ca.uhn.fhir.jpa.entity.BulkImportJobEntity;
import ca.uhn.fhir.jpa.entity.BulkImportJobFileEntity;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.entity.SearchInclude;
import ca.uhn.fhir.jpa.entity.SearchResult;
import ca.uhn.fhir.jpa.entity.SubscriptionTable;
import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptDesignation;
import ca.uhn.fhir.jpa.entity.TermConceptMap;
import ca.uhn.fhir.jpa.entity.TermConceptMapGroup;
import ca.uhn.fhir.jpa.entity.TermConceptMapGroupElement;
import ca.uhn.fhir.jpa.entity.TermConceptMapGroupElementTarget;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink;
import ca.uhn.fhir.jpa.entity.TermConceptProperty;
import ca.uhn.fhir.jpa.entity.TermValueSet;
import ca.uhn.fhir.jpa.entity.TermValueSetConcept;
import ca.uhn.fhir.jpa.entity.TermValueSetConceptDesignation;
import ca.uhn.fhir.jpa.model.entity.ForcedId;
import ca.uhn.fhir.jpa.model.entity.NpmPackageEntity;
import ca.uhn.fhir.jpa.model.entity.NpmPackageVersionEntity;
import ca.uhn.fhir.jpa.model.entity.NpmPackageVersionResourceEntity;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryProvenanceEntity;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTag;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedComboStringUnique;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedComboTokenNonUnique;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamCoords;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamDate;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamNumber;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamQuantity;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamQuantityNormalized;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamUri;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.jpa.model.entity.ResourceSearchUrlEntity;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.entity.ResourceTag;
import ca.uhn.fhir.jpa.model.entity.SearchParamPresentEntity;
import ca.uhn.fhir.jpa.model.entity.TagDefinition;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.util.MemoryCacheService;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;
import ca.uhn.fhir.util.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

@Service
public class ExpungeEverythingService implements IExpungeEverythingService {
	private static final Logger ourLog = LoggerFactory.getLogger(ExpungeEverythingService.class);

	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	protected EntityManager myEntityManager;

	@Autowired
	protected IInterceptorBroadcaster myInterceptorBroadcaster;

	@Autowired
	private HapiTransactionService myTxService;

	@Autowired
	private MemoryCacheService myMemoryCacheService;

	@Autowired
	private IRequestPartitionHelperSvc myRequestPartitionHelperSvc;

	private int deletedResourceEntityCount;

	@Override
	public void expungeEverything(@Nullable RequestDetails theRequest) {

		final AtomicInteger counter = new AtomicInteger();

		// Notify Interceptors about pre-action call
		HookParams hooks = new HookParams()
				.add(AtomicInteger.class, counter)
				.add(RequestDetails.class, theRequest)
				.addIfMatchesType(ServletRequestDetails.class, theRequest);
		CompositeInterceptorBroadcaster.doCallHooks(
				myInterceptorBroadcaster, theRequest, Pointcut.STORAGE_PRESTORAGE_EXPUNGE_EVERYTHING, hooks);

		ourLog.info("BEGINNING GLOBAL $expunge");
		Propagation propagation = Propagation.REQUIRES_NEW;
		ReadPartitionIdRequestDetails details =
				ReadPartitionIdRequestDetails.forOperation(null, null, ProviderConstants.OPERATION_EXPUNGE);
		RequestPartitionId requestPartitionId =
				myRequestPartitionHelperSvc.determineReadPartitionForRequest(theRequest, details);

		myTxService
				.withRequest(theRequest)
				.withPropagation(propagation)
				.withRequestPartitionId(requestPartitionId)
				.execute(() -> {
					counter.addAndGet(doExpungeEverythingQuery(
							"UPDATE " + TermCodeSystem.class.getSimpleName() + " d SET d.myCurrentVersion = null"));
				});
		counter.addAndGet(
				expungeEverythingByTypeWithoutPurging(theRequest, Batch2WorkChunkEntity.class, requestPartitionId));
		counter.addAndGet(
				expungeEverythingByTypeWithoutPurging(theRequest, Batch2JobInstanceEntity.class, requestPartitionId));
		counter.addAndGet(expungeEverythingByTypeWithoutPurging(
				theRequest, NpmPackageVersionResourceEntity.class, requestPartitionId));
		counter.addAndGet(
				expungeEverythingByTypeWithoutPurging(theRequest, NpmPackageVersionEntity.class, requestPartitionId));
		counter.addAndGet(
				expungeEverythingByTypeWithoutPurging(theRequest, NpmPackageEntity.class, requestPartitionId));
		counter.addAndGet(
				expungeEverythingByTypeWithoutPurging(theRequest, SearchParamPresentEntity.class, requestPartitionId));
		counter.addAndGet(
				expungeEverythingByTypeWithoutPurging(theRequest, BulkImportJobFileEntity.class, requestPartitionId));
		counter.addAndGet(
				expungeEverythingByTypeWithoutPurging(theRequest, BulkImportJobEntity.class, requestPartitionId));
		counter.addAndGet(expungeEverythingByTypeWithoutPurging(theRequest, ForcedId.class, requestPartitionId));
		counter.addAndGet(expungeEverythingByTypeWithoutPurging(
				theRequest, ResourceIndexedSearchParamDate.class, requestPartitionId));
		counter.addAndGet(expungeEverythingByTypeWithoutPurging(
				theRequest, ResourceIndexedSearchParamNumber.class, requestPartitionId));
		counter.addAndGet(expungeEverythingByTypeWithoutPurging(
				theRequest, ResourceIndexedSearchParamQuantity.class, requestPartitionId));
		counter.addAndGet(expungeEverythingByTypeWithoutPurging(
				theRequest, ResourceIndexedSearchParamQuantityNormalized.class, requestPartitionId));
		counter.addAndGet(expungeEverythingByTypeWithoutPurging(
				theRequest, ResourceIndexedSearchParamString.class, requestPartitionId));
		counter.addAndGet(expungeEverythingByTypeWithoutPurging(
				theRequest, ResourceIndexedSearchParamToken.class, requestPartitionId));
		counter.addAndGet(expungeEverythingByTypeWithoutPurging(
				theRequest, ResourceIndexedSearchParamUri.class, requestPartitionId));
		counter.addAndGet(expungeEverythingByTypeWithoutPurging(
				theRequest, ResourceIndexedSearchParamCoords.class, requestPartitionId));
		counter.addAndGet(expungeEverythingByTypeWithoutPurging(
				theRequest, ResourceIndexedComboStringUnique.class, requestPartitionId));
		counter.addAndGet(expungeEverythingByTypeWithoutPurging(
				theRequest, ResourceIndexedComboTokenNonUnique.class, requestPartitionId));
		counter.addAndGet(expungeEverythingByTypeWithoutPurging(theRequest, ResourceLink.class, requestPartitionId));
		counter.addAndGet(expungeEverythingByTypeWithoutPurging(theRequest, SearchResult.class, requestPartitionId));
		counter.addAndGet(expungeEverythingByTypeWithoutPurging(theRequest, SearchInclude.class, requestPartitionId));
		counter.addAndGet(expungeEverythingByTypeWithoutPurging(
				theRequest, TermValueSetConceptDesignation.class, requestPartitionId));
		counter.addAndGet(
				expungeEverythingByTypeWithoutPurging(theRequest, TermValueSetConcept.class, requestPartitionId));
		counter.addAndGet(expungeEverythingByTypeWithoutPurging(theRequest, TermValueSet.class, requestPartitionId));
		counter.addAndGet(expungeEverythingByTypeWithoutPurging(
				theRequest, TermConceptParentChildLink.class, requestPartitionId));
		counter.addAndGet(expungeEverythingByTypeWithoutPurging(
				theRequest, TermConceptMapGroupElementTarget.class, requestPartitionId));
		counter.addAndGet(expungeEverythingByTypeWithoutPurging(
				theRequest, TermConceptMapGroupElement.class, requestPartitionId));
		counter.addAndGet(
				expungeEverythingByTypeWithoutPurging(theRequest, TermConceptMapGroup.class, requestPartitionId));
		counter.addAndGet(expungeEverythingByTypeWithoutPurging(theRequest, TermConceptMap.class, requestPartitionId));
		counter.addAndGet(
				expungeEverythingByTypeWithoutPurging(theRequest, TermConceptProperty.class, requestPartitionId));
		counter.addAndGet(
				expungeEverythingByTypeWithoutPurging(theRequest, TermConceptDesignation.class, requestPartitionId));
		counter.addAndGet(expungeEverythingByTypeWithoutPurging(theRequest, TermConcept.class, requestPartitionId));
		myTxService
				.withRequest(theRequest)
				.withPropagation(propagation)
				.withRequestPartitionId(requestPartitionId)
				.execute(() -> {
					for (TermCodeSystem next : myEntityManager
							.createQuery("SELECT c FROM " + TermCodeSystem.class.getName() + " c", TermCodeSystem.class)
							.getResultList()) {
						next.setCurrentVersion(null);
						myEntityManager.merge(next);
					}
				});
		counter.addAndGet(
				expungeEverythingByTypeWithoutPurging(theRequest, TermCodeSystemVersion.class, requestPartitionId));
		counter.addAndGet(expungeEverythingByTypeWithoutPurging(theRequest, TermCodeSystem.class, requestPartitionId));
		counter.addAndGet(
				expungeEverythingByTypeWithoutPurging(theRequest, SubscriptionTable.class, requestPartitionId));
		counter.addAndGet(
				expungeEverythingByTypeWithoutPurging(theRequest, ResourceHistoryTag.class, requestPartitionId));
		counter.addAndGet(expungeEverythingByTypeWithoutPurging(theRequest, ResourceTag.class, requestPartitionId));
		counter.addAndGet(expungeEverythingByTypeWithoutPurging(theRequest, TagDefinition.class, requestPartitionId));
		counter.addAndGet(expungeEverythingByTypeWithoutPurging(
				theRequest, ResourceHistoryProvenanceEntity.class, requestPartitionId));
		counter.addAndGet(
				expungeEverythingByTypeWithoutPurging(theRequest, ResourceHistoryTable.class, requestPartitionId));
		counter.addAndGet(
				expungeEverythingByTypeWithoutPurging(theRequest, ResourceSearchUrlEntity.class, requestPartitionId));
		int counterBefore = counter.get();
		counter.addAndGet(expungeEverythingByTypeWithoutPurging(theRequest, ResourceTable.class, requestPartitionId));
		counter.addAndGet(expungeEverythingByTypeWithoutPurging(theRequest, PartitionEntity.class, requestPartitionId));

		deletedResourceEntityCount = counter.get() - counterBefore;

		myTxService
				.withRequest(theRequest)
				.withPropagation(propagation)
				.withRequestPartitionId(requestPartitionId)
				.execute(() -> {
					counter.addAndGet(doExpungeEverythingQuery("DELETE from " + Search.class.getSimpleName() + " d"));
				});

		purgeAllCaches();

		ourLog.info("COMPLETED GLOBAL $expunge - Deleted {} rows", counter.get());
	}

	@Override
	public int getExpungeDeletedEntityCount() {
		return deletedResourceEntityCount;
	}

	private void purgeAllCaches() {
		myMemoryCacheService.invalidateAllCaches();
	}

	private int expungeEverythingByTypeWithoutPurging(
			RequestDetails theRequest, Class<?> theEntityType, RequestPartitionId theRequestPartitionId) {
		int outcome = 0;
		while (true) {
			StopWatch sw = new StopWatch();

			int count = myTxService
					.withRequest(theRequest)
					.withPropagation(Propagation.REQUIRES_NEW)
					.withRequestPartitionId(theRequestPartitionId)
					.execute(() -> {
						CriteriaBuilder cb = myEntityManager.getCriteriaBuilder();
						CriteriaQuery<?> cq = cb.createQuery(theEntityType);
						cq.from(theEntityType);
						TypedQuery<?> query = myEntityManager.createQuery(cq);
						query.setMaxResults(1000);
						List<?> results = query.getResultList();
						for (Object result : results) {
							myEntityManager.remove(result);
						}
						return results.size();
					});

			outcome += count;
			if (count == 0) {
				break;
			}

			ourLog.info("Have deleted {} entities of type {} in {}", outcome, theEntityType.getSimpleName(), sw);
		}
		return outcome;
	}

	@Override
	public int expungeEverythingByType(Class<?> theEntityType) {
		int result = expungeEverythingByTypeWithoutPurging(null, theEntityType, RequestPartitionId.allPartitions());
		purgeAllCaches();
		return result;
	}

	@Override
	public int expungeEverythingMdmLinks() {
		return expungeEverythingByType(MdmLink.class);
	}

	private int doExpungeEverythingQuery(String theQuery) {
		StopWatch sw = new StopWatch();
		int outcome = myEntityManager.createQuery(theQuery).executeUpdate();
		ourLog.debug("SqlQuery affected {} rows in {}: {}", outcome, sw, theQuery);
		return outcome;
	}
}
