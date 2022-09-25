package ca.uhn.fhir.jpa.delete.batch2;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.jpa.api.svc.IDeleteExpungeSvc;
import ca.uhn.fhir.jpa.dao.IFulltextSearchSvc;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

@Transactional(propagation = Propagation.MANDATORY)
public class DeleteExpungeSvcImpl implements IDeleteExpungeSvc {
	private static final Logger ourLog = LoggerFactory.getLogger(DeleteExpungeSvcImpl.class);

	private final EntityManager myEntityManager;
	private final DeleteExpungeSqlBuilder myDeleteExpungeSqlBuilder;
	private final IFulltextSearchSvc myFullTextSearchSvc;

	public DeleteExpungeSvcImpl(EntityManager theEntityManager, DeleteExpungeSqlBuilder theDeleteExpungeSqlBuilder, IFulltextSearchSvc theFullTextSearchSvc) {
		myEntityManager = theEntityManager;
		myDeleteExpungeSqlBuilder = theDeleteExpungeSqlBuilder;
		myFullTextSearchSvc = theFullTextSearchSvc;
	}

	@Override
	public void deleteExpunge(List<ResourcePersistentId> thePersistentIds) {
		List<String> sqlList = myDeleteExpungeSqlBuilder.convertPidsToDeleteExpungeSql(thePersistentIds);

		ourLog.debug("Executing {} delete expunge sql commands", sqlList.size());
		long totalDeleted = 0;
		for (String sql : sqlList) {
			ourLog.trace("Executing sql " + sql);
			totalDeleted += myEntityManager.createNativeQuery(sql).executeUpdate();
		}

		ourLog.info("{} records deleted", totalDeleted);
		clearHibernateSearchIndex(thePersistentIds);
		
		// TODO KHS instead of logging progress, produce result chunks that get aggregated into a delete expunge report
	}

	/**
	 * If we are running with HS enabled, the expunge operation will cause dangling documents because Hibernate Search is not aware of custom SQL queries that delete resources.
	 * This method clears the Hibernate Search index for the given resources.
	 */
	private void clearHibernateSearchIndex(List<ResourcePersistentId> thePersistentIds) {
		if (myFullTextSearchSvc != null) {
			List<Object> objectIds = thePersistentIds.stream().map(ResourcePersistentId::getIdAsLong).collect(Collectors.toList());
			myFullTextSearchSvc.deleteIndexedDocumentsByTypeAndId(ResourceTable.class, objectIds);
			ourLog.info("Cleared Hibernate Search indexes.");
		}
	}


}
