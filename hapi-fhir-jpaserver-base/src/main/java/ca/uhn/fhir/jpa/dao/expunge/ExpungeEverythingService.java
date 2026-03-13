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
package ca.uhn.fhir.jpa.dao.expunge;

import ca.uhn.fhir.batch2.api.IJobMaintenanceService;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
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
import ca.uhn.fhir.jpa.model.entity.IndexedSearchParamIdentity;
import ca.uhn.fhir.jpa.model.entity.NpmPackageEntity;
import ca.uhn.fhir.jpa.model.entity.NpmPackageVersionEntity;
import ca.uhn.fhir.jpa.model.entity.NpmPackageVersionResourceEntity;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryProvenanceEntity;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTag;
import ca.uhn.fhir.jpa.model.entity.ResourceIdentifierPatientUniqueEntity;
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
import ca.uhn.fhir.jpa.model.entity.ResourceSystemEntity;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.entity.ResourceTag;
import ca.uhn.fhir.jpa.model.entity.SearchParamPresentEntity;
import ca.uhn.fhir.jpa.model.entity.TagDefinition;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.search.builder.SearchBuilder;
import ca.uhn.fhir.jpa.util.MemoryCacheService;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;
import ca.uhn.fhir.util.StopWatch;
import com.google.common.annotations.VisibleForTesting;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.Metamodel;
import jakarta.persistence.metamodel.SingularAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.util.comparator.Comparators;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

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

	@Autowired
	private IJobMaintenanceService myJobMaintenanceService;

	private int deletedResourceEntityCount;

	@Override
	public void expungeEverything(@Nullable RequestDetails theRequest) {

		final AtomicInteger counter = new AtomicInteger();

		// Notify Interceptors about pre-action call
		IInterceptorBroadcaster compositeBroadcaster =
				CompositeInterceptorBroadcaster.newCompositeBroadcaster(myInterceptorBroadcaster, theRequest);
		if (compositeBroadcaster.hasHooks(Pointcut.STORAGE_PRESTORAGE_EXPUNGE_EVERYTHING)) {
			HookParams hooks = new HookParams()
					.add(AtomicInteger.class, counter)
					.add(RequestDetails.class, theRequest)
					.addIfMatchesType(ServletRequestDetails.class, theRequest);
			compositeBroadcaster.callHooks(Pointcut.STORAGE_PRESTORAGE_EXPUNGE_EVERYTHING, hooks);
		}

		ourLog.info("BEGINNING GLOBAL $expunge");
		Propagation propagation = Propagation.REQUIRES_NEW;
		RequestPartitionId requestPartitionId =
				myRequestPartitionHelperSvc.determineReadPartitionForRequestForServerOperation(
						theRequest, ProviderConstants.OPERATION_EXPUNGE);

		deleteAll(theRequest, propagation, requestPartitionId, counter);

		purgeAllCaches();

		ourLog.info("COMPLETED GLOBAL $expunge - Deleted {} rows", counter.get());
	}

	protected void deleteAll(
			@Nullable RequestDetails theRequest,
			Propagation propagation,
			RequestPartitionId requestPartitionId,
			AtomicInteger counter) {
		myTxService
				.withRequest(theRequest)
				.withPropagation(propagation)
				.withRequestPartitionId(requestPartitionId)
				.execute(() -> {
					counter.addAndGet(doExpungeEverythingQuery(
							"UPDATE " + TermCodeSystem.class.getSimpleName() + " d SET d.myCurrentVersion = null"));
				});
		counter.addAndGet(expungeBatch2Entities(theRequest, requestPartitionId));
		counter.addAndGet(expungeEverythingByTypeWithoutPurging(
				theRequest, ResourceIdentifierPatientUniqueEntity.class, requestPartitionId));
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
		counter.addAndGet(expungeEverythingByTypeWithoutPurging(
				theRequest, IndexedSearchParamIdentity.class, requestPartitionId));
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
				expungeEverythingByTypeWithoutPurging(theRequest, ResourceSystemEntity.class, requestPartitionId));
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
	}

	@Override
	public int getExpungeDeletedEntityCount() {
		return deletedResourceEntityCount;
	}

	private void purgeAllCaches() {
		myMemoryCacheService.invalidateAllCaches();
	}

	protected <T> int expungeEverythingByTypeWithoutPurging(
			RequestDetails theRequest, Class<T> theEntityType, RequestPartitionId theRequestPartitionId) {
		HapiTransactionService.noTransactionAllowed();

		int outcome = 0;
		while (true) {
			StopWatch sw = new StopWatch();

			int count = myTxService
					.withRequest(theRequest)
					.withPropagation(Propagation.REQUIRES_NEW)
					.withRequestPartitionId(theRequestPartitionId)
					.execute(() -> {

						/*
						 * This method uses a nice efficient mechanism where we figure out the PID datatype
						 * and load only the PIDs and delete by PID for all resource types except ResourceTable.
						 * We delete ResourceTable using the entitymanager so that Hibernate Search knows to
						 * delete the corresponding records it manages in ElasticSearch. See
						 * FhirResourceDaoR4SearchWithElasticSearchIT for a test that fails without the
						 * block below.
						 */
						if (ResourceTable.class.equals(theEntityType)) {
							CriteriaBuilder cb = myEntityManager.getCriteriaBuilder();
							CriteriaQuery<?> cq = cb.createQuery(theEntityType);
							cq.from(theEntityType);
							TypedQuery<?> query = myEntityManager.createQuery(cq);
							query.setMaxResults(800);
							List<?> results = query.getResultList();
							for (Object result : results) {
								myEntityManager.remove(result);
							}
							return results.size();
						}

						Metamodel metamodel = myEntityManager.getMetamodel();
						EntityType<T> entity = metamodel.entity(theEntityType);
						Set<SingularAttribute<? super T, ?>> singularAttributes = entity.getSingularAttributes();
						List<String> idProperty = new ArrayList<>();
						for (SingularAttribute<? super T, ?> singularAttribute : singularAttributes) {
							if (singularAttribute.isId()) {
								idProperty.add(singularAttribute.getName());
							}
						}
						idProperty.sort(Comparators.comparable());
						String idPropertyNames = String.join(",", idProperty);

						Query nativeQuery = myEntityManager.createQuery(
								"SELECT (" + idPropertyNames + ") FROM " + theEntityType.getSimpleName());

						// Each ID is 2 parameters in DB partition mode, so this
						// is the maximum we should allow
						nativeQuery.setMaxResults(SearchBuilder.getMaximumPageSize() / 2);

						List pids = nativeQuery.getResultList();
						if (pids.isEmpty()) {
							return 0;
						}

						StringBuilder deleteBuilder = new StringBuilder();
						deleteBuilder.append("DELETE FROM ");
						deleteBuilder.append(theEntityType.getSimpleName());
						deleteBuilder.append(" WHERE (");
						deleteBuilder.append(idPropertyNames);
						deleteBuilder.append(") IN ");
						if (idProperty.size() > 1) {
							deleteBuilder.append('(');
							for (Iterator<Object> iter = pids.iterator(); iter.hasNext(); ) {
								Object[] pid = (Object[]) iter.next();
								deleteBuilder.append('(');
								for (int i = 0; i < pid.length; i++) {
									if (i > 0) {
										deleteBuilder.append(',');
									}
									deleteBuilder.append(pid[i]);
								}
								deleteBuilder.append(')');
								if (iter.hasNext()) {
									deleteBuilder.append(',');
								}
							}
							deleteBuilder.append(')');
						} else {
							deleteBuilder.append("(:pids)");
						}
						String deleteSql = deleteBuilder.toString();
						nativeQuery = myEntityManager.createQuery(deleteSql);
						if (idProperty.size() == 1) {
							nativeQuery.setParameter("pids", pids);
						}
						nativeQuery.executeUpdate();
						return pids.size();
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

	private static final int EXPUNGE_BATCH2_MAX_RETRIES = 10;

	/**
	 * Deletes Batch2WorkChunkEntity and Batch2JobInstanceEntity while holding the maintenance
	 * semaphore (via {@link IJobMaintenanceService#holdMaintenanceForExpunge()}) to prevent any
	 * concurrent maintenance pass from inserting or updating batch2 entities during deletion.
	 *
	 * <p>The semaphore hold prevents maintenance on the local JVM. In clustered deployments,
	 * maintenance on another node could still create new work chunks between passes. The retry
	 * loop catches FK constraint violations and retries the deletion cycle.
	 *
	 * <p>Work chunks have a foreign key (FK_BT2WC_INSTANCE) referencing job instances, so we
	 * delete work chunks before job instances.
	 */
	// Created by claude-opus-4-6
	@VisibleForTesting
	int expungeBatch2Entities(RequestDetails theRequest, RequestPartitionId theRequestPartitionId) {
		HapiTransactionService.noTransactionAllowed();

		// Hold the maintenance semaphore while deleting batch2 entities.
		// This prevents the local maintenance job from inserting/updating work chunks and job
		// instances while we are deleting them, eliminating FK violations and deadlocks.
		try (Closeable ignored = myJobMaintenanceService.holdMaintenanceForExpunge()) {
			int outcome = 0;
			int consecutiveFailures = 0;
			while (true) {
				int deleted;
				try {
					// Delete work chunks first (child FK), then job instances (parent)
					deleted = 0;
					deleted += expungeEverythingByTypeWithoutPurging(
							theRequest, Batch2WorkChunkEntity.class, theRequestPartitionId);
					deleted += expungeEverythingByTypeWithoutPurging(
							theRequest, Batch2JobInstanceEntity.class, theRequestPartitionId);
				} catch (ResourceVersionConflictException e) {
					// In clustered deployments, maintenance on another node may create new
					// work chunks, causing FK violations when we delete job instances.
					consecutiveFailures++;
					if (consecutiveFailures > EXPUNGE_BATCH2_MAX_RETRIES) {
						throw e;
					}
					ourLog.info(
							"Retrying batch2 entity deletion due to constraint conflict (attempt {}/{})",
							consecutiveFailures,
							EXPUNGE_BATCH2_MAX_RETRIES);
					continue;
				}
				consecutiveFailures = 0;
				outcome += deleted;
				if (deleted == 0) {
					break;
				}
			}
			return outcome;
		} catch (IOException e) {
			throw new InternalErrorException(Msg.code(2844) + "Error releasing maintenance hold", e);
		}
	}
}
