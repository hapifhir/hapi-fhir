package ca.uhn.fhir.jpa.dao;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.config.HapiFhirHibernateJpaDialect;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.util.StopWatch;
import org.apache.commons.lang3.Validate;
import org.apache.jena.rdf.model.ModelCon;
import org.hibernate.Session;
import org.hibernate.internal.SessionImpl;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.endsWith;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class TransactionProcessor extends BaseTransactionProcessor {

	private static final Logger ourLog = LoggerFactory.getLogger(TransactionProcessor.class);
	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	private EntityManager myEntityManager;
	@Autowired(required = false)
	private HapiFhirHibernateJpaDialect myHapiFhirHibernateJpaDialect;
	@Autowired
	private IdHelperService myIdHelperService;
	@Autowired
	private PartitionSettings myDaoConfig;

	public void setEntityManagerForUnitTest(EntityManager theEntityManager) {
		myEntityManager = theEntityManager;
	}

	@Override
	protected void validateDependencies() {
		super.validateDependencies();

		Validate.notNull(myEntityManager);
	}

	@Override
	protected Map<IBase, IIdType> doTransactionWriteOperations(final RequestDetails theRequest, String theActionName, TransactionDetails theTransactionDetails, Set<IIdType> theAllIds,
																				  Map<IIdType, IIdType> theIdSubstitutions, Map<IIdType, DaoMethodOutcome> theIdToPersistedOutcome, IBaseBundle theResponse, IdentityHashMap<IBase, Integer> theOriginalRequestOrder, List<IBase> theEntries, StopWatch theTransactionStopWatch) {

		if (!myDaoConfig.isPartitioningEnabled()) {
			ITransactionProcessorVersionAdapter versionAdapter = getVersionAdapter();
			List<IIdType> idsToPreResolve = new ArrayList<>();
			for (IBase nextEntry : theEntries) {
				IBaseResource resource = versionAdapter.getResource(nextEntry);
				if (resource != null) {
					String fullUrl = versionAdapter.getFullUrl(nextEntry);
					boolean isPlaceholder = defaultString(fullUrl).startsWith("urn:");
					if (!isPlaceholder) {
						if (resource.getIdElement().hasIdPart() && resource.getIdElement().hasResourceType()) {
							idsToPreResolve.add(resource.getIdElement());
						}
					}
				}
			}

			Set<String> foundIds = new HashSet<>();
			List<ResourcePersistentId> outcome = myIdHelperService.resolveResourcePersistentIdsWithCache(RequestPartitionId.allPartitions(), idsToPreResolve);
			for (ResourcePersistentId next : outcome) {
				foundIds.add(next.getAssociatedResourceId().toUnqualifiedVersionless().getValue());
				theTransactionDetails.addResolvedResourceId(next.getAssociatedResourceId(), next);
			}
			for (IIdType next : idsToPreResolve) {
				if (!foundIds.contains(next.toUnqualifiedVersionless().getValue())) {
					theTransactionDetails.addResolvedResourceId(next.toUnqualifiedVersionless(), null);
				}
			}

		}

		return super.doTransactionWriteOperations(theRequest, theActionName, theTransactionDetails, theAllIds, theIdSubstitutions, theIdToPersistedOutcome, theResponse, theOriginalRequestOrder, theEntries, theTransactionStopWatch);
	}


		@Override
	protected void flushSession(Map<IIdType, DaoMethodOutcome> theIdToPersistedOutcome) {
		try {
			int insertionCount;
			int updateCount;
			SessionImpl session = myEntityManager.unwrap(SessionImpl.class);
			if (session != null) {
				insertionCount = session.getActionQueue().numberOfInsertions();
				updateCount = session.getActionQueue().numberOfUpdates();
			} else {
				insertionCount = -1;
				updateCount = -1;
			}

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
