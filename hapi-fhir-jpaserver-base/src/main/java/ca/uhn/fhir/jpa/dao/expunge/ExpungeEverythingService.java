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
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ExpungeEverythingService {
	private static final Logger ourLog = LoggerFactory.getLogger(ExpungeEverythingService.class);
	@Autowired
	private PlatformTransactionManager myPlatformTransactionManager;
	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	protected EntityManager myEntityManager;

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
		txTemplate.execute(t -> {
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + SearchParamPresent.class.getSimpleName() + " d"));
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + ForcedId.class.getSimpleName() + " d"));
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + ResourceIndexedSearchParamDate.class.getSimpleName() + " d"));
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + ResourceIndexedSearchParamNumber.class.getSimpleName() + " d"));
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + ResourceIndexedSearchParamQuantity.class.getSimpleName() + " d"));
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + ResourceIndexedSearchParamString.class.getSimpleName() + " d"));
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + ResourceIndexedSearchParamToken.class.getSimpleName() + " d"));
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + ResourceIndexedSearchParamUri.class.getSimpleName() + " d"));
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + ResourceIndexedSearchParamCoords.class.getSimpleName() + " d"));
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + ResourceIndexedCompositeStringUnique.class.getSimpleName() + " d"));
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + ResourceLink.class.getSimpleName() + " d"));
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + SearchResult.class.getSimpleName() + " d"));
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + SearchInclude.class.getSimpleName() + " d"));
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + TermConceptParentChildLink.class.getSimpleName() + " d"));
			return null;
		});
		txTemplate.execute(t -> {
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + TermConceptMapGroupElementTarget.class.getSimpleName() + " d"));
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + TermConceptMapGroupElement.class.getSimpleName() + " d"));
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + TermConceptMapGroup.class.getSimpleName() + " d"));
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + TermConceptMap.class.getSimpleName() + " d"));
			return null;
		});
		txTemplate.execute(t -> {
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + TermConceptProperty.class.getSimpleName() + " d"));
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + TermConceptDesignation.class.getSimpleName() + " d"));
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + TermConcept.class.getSimpleName() + " d"));
			for (TermCodeSystem next : myEntityManager.createQuery("SELECT c FROM " + TermCodeSystem.class.getName() + " c", TermCodeSystem.class).getResultList()) {
				next.setCurrentVersion(null);
				myEntityManager.merge(next);
			}
			return null;
		});
		txTemplate.execute(t -> {
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + TermCodeSystemVersion.class.getSimpleName() + " d"));
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + TermCodeSystem.class.getSimpleName() + " d"));
			return null;
		});
		txTemplate.execute(t -> {
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + SubscriptionTable.class.getSimpleName() + " d"));
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + ResourceHistoryTag.class.getSimpleName() + " d"));
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + ResourceTag.class.getSimpleName() + " d"));
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + TagDefinition.class.getSimpleName() + " d"));
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + ResourceHistoryTable.class.getSimpleName() + " d"));
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + ResourceTable.class.getSimpleName() + " d"));
			counter.addAndGet(doExpungeEverythingQuery("DELETE from " + org.hibernate.search.jpa.Search.class.getSimpleName() + " d"));
			return null;
		});

		ourLog.info("COMPLETED GLOBAL $expunge - Deleted {} rows", counter.get());
	}

	private int doExpungeEverythingQuery(String theQuery) {
		StopWatch sw = new StopWatch();
		int outcome = myEntityManager.createQuery(theQuery).executeUpdate();
		ourLog.debug("Query affected {} rows in {}: {}", outcome, sw.toString(), theQuery);
		return outcome;
	}
}
