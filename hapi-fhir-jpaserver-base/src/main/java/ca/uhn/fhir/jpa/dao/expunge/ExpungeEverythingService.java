package ca.uhn.fhir.jpa.dao.expunge;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.entity.*;
import ca.uhn.fhir.jpa.model.entity.*;
import ca.uhn.fhir.jpa.util.JpaInterceptorBroadcaster;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ExpungeEverythingService {
	private static final Logger ourLog = LoggerFactory.getLogger(ExpungeEverythingService.class);
	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	protected EntityManager myEntityManager;
	@Autowired
	private PlatformTransactionManager myPlatformTransactionManager;
	@Autowired
	protected IInterceptorBroadcaster myInterceptorBroadcaster;

	private TransactionTemplate myTxTemplate;

	@PostConstruct
	public void initTxTemplate() {
		myTxTemplate = new TransactionTemplate(myPlatformTransactionManager);
	}

	void expungeEverything(RequestDetails theRequest) {

		final AtomicInteger counter = new AtomicInteger();

		// Notify Interceptors about pre-action call
		HookParams hooks = new HookParams()
			.add(AtomicInteger.class, counter)
			.add(RequestDetails.class, theRequest)
			.addIfMatchesType(ServletRequestDetails.class, theRequest);
		JpaInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, theRequest, Pointcut.STORAGE_PRESTORAGE_EXPUNGE_EVERYTHING, hooks);

		ourLog.info("BEGINNING GLOBAL $expunge");
		myTxTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		myTxTemplate.execute(t -> {
			counter.addAndGet(doExpungeEverythingQuery("UPDATE " + TermCodeSystem.class.getSimpleName() + " d SET d.myCurrentVersion = null"));
			return null;
		});
		counter.addAndGet(expungeEverythingByType(SearchParamPresent.class));
		counter.addAndGet(expungeEverythingByType(ForcedId.class));
		counter.addAndGet(expungeEverythingByType(ResourceIndexedSearchParamDate.class));
		counter.addAndGet(expungeEverythingByType(ResourceIndexedSearchParamNumber.class));
		counter.addAndGet(expungeEverythingByType(ResourceIndexedSearchParamQuantity.class));
		counter.addAndGet(expungeEverythingByType(ResourceIndexedSearchParamString.class));
		counter.addAndGet(expungeEverythingByType(ResourceIndexedSearchParamToken.class));
		counter.addAndGet(expungeEverythingByType(ResourceIndexedSearchParamUri.class));
		counter.addAndGet(expungeEverythingByType(ResourceIndexedSearchParamCoords.class));
		counter.addAndGet(expungeEverythingByType(ResourceIndexedCompositeStringUnique.class));
		counter.addAndGet(expungeEverythingByType(ResourceLink.class));
		counter.addAndGet(expungeEverythingByType(SearchResult.class));
		counter.addAndGet(expungeEverythingByType(SearchInclude.class));
		counter.addAndGet(expungeEverythingByType(TermValueSetConceptDesignation.class));
		counter.addAndGet(expungeEverythingByType(TermValueSetConcept.class));
		counter.addAndGet(expungeEverythingByType(TermValueSet.class));
		counter.addAndGet(expungeEverythingByType(TermConceptParentChildLink.class));
		counter.addAndGet(expungeEverythingByType(TermConceptMapGroupElementTarget.class));
		counter.addAndGet(expungeEverythingByType(TermConceptMapGroupElement.class));
		counter.addAndGet(expungeEverythingByType(TermConceptMapGroup.class));
		counter.addAndGet(expungeEverythingByType(TermConceptMap.class));
		counter.addAndGet(expungeEverythingByType(TermConceptProperty.class));
		counter.addAndGet(expungeEverythingByType(TermConceptDesignation.class));
		counter.addAndGet(expungeEverythingByType(TermConcept.class));
		myTxTemplate.execute(t -> {
			for (TermCodeSystem next : myEntityManager.createQuery("SELECT c FROM " + TermCodeSystem.class.getName() + " c", TermCodeSystem.class).getResultList()) {
				next.setCurrentVersion(null);
				myEntityManager.merge(next);
			}
			return null;
		});
		counter.addAndGet(expungeEverythingByType(TermCodeSystemVersion.class));
		counter.addAndGet(expungeEverythingByType(TermCodeSystem.class));
		counter.addAndGet(expungeEverythingByType(SubscriptionTable.class));
		counter.addAndGet(expungeEverythingByType(ResourceHistoryTag.class));
		counter.addAndGet(expungeEverythingByType(ResourceTag.class));
		counter.addAndGet(expungeEverythingByType(TagDefinition.class));
		counter.addAndGet(expungeEverythingByType(ResourceHistoryProvenanceEntity.class));
		counter.addAndGet(expungeEverythingByType(ResourceHistoryTable.class));
		counter.addAndGet(expungeEverythingByType(ResourceTable.class));
		myTxTemplate.execute(t -> {
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + org.hibernate.search.jpa.Search.class.getSimpleName() + " d"));
			return null;
		});

		ourLog.info("COMPLETED GLOBAL $expunge - Deleted {} rows", counter.get());
	}

	public int expungeEverythingByType(Class<?> theEntityType) {

		int outcome = 0;
		while (true) {
			StopWatch sw = new StopWatch();

			@SuppressWarnings("ConstantConditions")
			int count = myTxTemplate.execute(t -> {
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

			ourLog.info("Have deleted {} entities of type {} in {}", outcome, theEntityType.getSimpleName(), sw.toString());
		}

		return outcome;
	}

	private int doExpungeEverythingQuery(String theQuery) {
		StopWatch sw = new StopWatch();
		int outcome = myEntityManager.createQuery(theQuery).executeUpdate();
		ourLog.debug("SqlQuery affected {} rows in {}: {}", outcome, sw.toString(), theQuery);
		return outcome;
	}

}
