package ca.uhn.fhir.jpa.dao.expunge;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

import ca.uhn.fhir.jpa.entity.*;
import ca.uhn.fhir.jpa.model.entity.*;
import ca.uhn.fhir.util.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

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

	void expungeEverything() {

		final AtomicInteger counter = new AtomicInteger();

		ourLog.info("BEGINNING GLOBAL $expunge");
		TransactionTemplate txTemplate = new TransactionTemplate(myPlatformTransactionManager);
		txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		txTemplate.execute(t -> {
			counter.addAndGet(doExpungeEverythingQuery("UPDATE " + ResourceHistoryTable.class.getSimpleName() + " d SET d.myForcedId = null"));
			counter.addAndGet(doExpungeEverythingQuery("UPDATE " + ResourceTable.class.getSimpleName() + " d SET d.myForcedId = null"));
			counter.addAndGet(doExpungeEverythingQuery("UPDATE " + TermCodeSystem.class.getSimpleName() + " d SET d.myCurrentVersion = null"));
			return null;
		});
		counter.addAndGet(doExpungeEverythingQuery(txTemplate, SearchParamPresent.class));
		counter.addAndGet(doExpungeEverythingQuery(txTemplate, ForcedId.class));
		counter.addAndGet(doExpungeEverythingQuery(txTemplate, ResourceIndexedSearchParamDate.class));
		counter.addAndGet(doExpungeEverythingQuery(txTemplate, ResourceIndexedSearchParamNumber.class));
		counter.addAndGet(doExpungeEverythingQuery(txTemplate, ResourceIndexedSearchParamQuantity.class));
		counter.addAndGet(doExpungeEverythingQuery(txTemplate, ResourceIndexedSearchParamString.class));
		counter.addAndGet(doExpungeEverythingQuery(txTemplate, ResourceIndexedSearchParamToken.class));
		counter.addAndGet(doExpungeEverythingQuery(txTemplate, ResourceIndexedSearchParamUri.class));
		counter.addAndGet(doExpungeEverythingQuery(txTemplate, ResourceIndexedSearchParamCoords.class));
		counter.addAndGet(doExpungeEverythingQuery(txTemplate, ResourceIndexedCompositeStringUnique.class));
		counter.addAndGet(doExpungeEverythingQuery(txTemplate, ResourceLink.class));
		counter.addAndGet(doExpungeEverythingQuery(txTemplate, SearchResult.class));
		counter.addAndGet(doExpungeEverythingQuery(txTemplate, SearchInclude.class));
		counter.addAndGet(doExpungeEverythingQuery(txTemplate, TermConceptParentChildLink.class));
		counter.addAndGet(doExpungeEverythingQuery(txTemplate, TermConceptMapGroupElementTarget.class));
		counter.addAndGet(doExpungeEverythingQuery(txTemplate, TermConceptMapGroupElement.class));
		counter.addAndGet(doExpungeEverythingQuery(txTemplate, TermConceptMapGroup.class));
		counter.addAndGet(doExpungeEverythingQuery(txTemplate, TermConceptMap.class));
		counter.addAndGet(doExpungeEverythingQuery(txTemplate, TermConceptProperty.class));
		counter.addAndGet(doExpungeEverythingQuery(txTemplate, TermConceptDesignation.class));
		counter.addAndGet(doExpungeEverythingQuery(txTemplate, TermConcept.class));
		txTemplate.execute(t -> {
			for (TermCodeSystem next : myEntityManager.createQuery("SELECT c FROM " + TermCodeSystem.class.getName() + " c", TermCodeSystem.class).getResultList()) {
				next.setCurrentVersion(null);
				myEntityManager.merge(next);
			}
			return null;
		});
		counter.addAndGet(doExpungeEverythingQuery(txTemplate, TermCodeSystemVersion.class));
		counter.addAndGet(doExpungeEverythingQuery(txTemplate, TermCodeSystem.class));
		counter.addAndGet(doExpungeEverythingQuery(txTemplate, SubscriptionTable.class));
		counter.addAndGet(doExpungeEverythingQuery(txTemplate, ResourceHistoryTag.class));
		counter.addAndGet(doExpungeEverythingQuery(txTemplate, ResourceTag.class));
		counter.addAndGet(doExpungeEverythingQuery(txTemplate, TagDefinition.class));
		counter.addAndGet(doExpungeEverythingQuery(txTemplate, ResourceHistoryTable.class));
		counter.addAndGet(doExpungeEverythingQuery(txTemplate, ResourceTable.class));
		txTemplate.execute(t -> {
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + org.hibernate.search.jpa.Search.class.getSimpleName() + " d"));
			return null;
		});

		ourLog.info("COMPLETED GLOBAL $expunge - Deleted {} rows", counter.get());
	}

	private int doExpungeEverythingQuery(TransactionTemplate txTemplate, Class<?> theEntityType) {

		int outcome = 0;
		while (true) {
			StopWatch sw = new StopWatch();

			@SuppressWarnings("ConstantConditions")
			int count = txTemplate.execute(t -> {
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
		ourLog.debug("Query affected {} rows in {}: {}", outcome, sw.toString(), theQuery);
		return outcome;
	}

}
