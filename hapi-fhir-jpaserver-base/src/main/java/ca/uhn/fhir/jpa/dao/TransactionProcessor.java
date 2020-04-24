package ca.uhn.fhir.jpa.dao;

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

import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.config.HapiFhirHibernateJpaDialect;
import ca.uhn.fhir.util.StopWatch;
import org.apache.commons.lang3.Validate;
import org.hibernate.Session;
import org.hibernate.internal.SessionImpl;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TransactionProcessor extends BaseTransactionProcessor {

	private static final Logger ourLog = LoggerFactory.getLogger(TransactionProcessor.class);

	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	private EntityManager myEntityManager;
	@Autowired(required = false)
	private HapiFhirHibernateJpaDialect myHapiFhirHibernateJpaDialect;

	@Override
	protected void validateDependencies() {
		super.validateDependencies();

		Validate.notNull(myEntityManager);
	}


	@Override
	protected void flushSession(Map<IIdType, DaoMethodOutcome> theIdToPersistedOutcome) {
		try {
			SessionImpl session = (SessionImpl) myEntityManager.unwrap(Session.class);
			int insertionCount = session.getActionQueue().numberOfInsertions();
			int updateCount = session.getActionQueue().numberOfUpdates();

			StopWatch sw = new StopWatch();
			myEntityManager.flush();
			ourLog.debug("Session flush took {}ms for {} inserts and {} updates", sw.getMillis(), insertionCount, updateCount);
		} catch (PersistenceException e) {
			if (myHapiFhirHibernateJpaDialect != null) {
				List<String> types = theIdToPersistedOutcome.keySet().stream().filter(t -> t != null).map(t -> t.getResourceType()).collect(Collectors.toList());
				String message = "Error flushing transaction with resource types: " + types;
				throw myHapiFhirHibernateJpaDialect.translate(e, message);
			}
			throw e;
		}
	}


}
