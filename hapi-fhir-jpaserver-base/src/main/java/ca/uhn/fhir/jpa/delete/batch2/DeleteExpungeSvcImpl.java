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
package ca.uhn.fhir.jpa.delete.batch2;

import ca.uhn.fhir.jpa.api.svc.IDeleteExpungeSvc;
import ca.uhn.fhir.jpa.dao.IFulltextSearchSvc;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.util.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionOperations;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;

public class DeleteExpungeSvcImpl implements IDeleteExpungeSvc<JpaPid> {
	private static final Logger ourLog = LoggerFactory.getLogger(DeleteExpungeSvcImpl.class);
	private final IHapiTransactionService myHapiTransactionService;
	private final EntityManager myEntityManager;
	private final DeleteExpungeSqlBuilder myDeleteExpungeSqlBuilder;
	private final IFulltextSearchSvc myFullTextSearchSvc;

	public DeleteExpungeSvcImpl(
		EntityManager theEntityManager,
		DeleteExpungeSqlBuilder theDeleteExpungeSqlBuilder,
		@Autowired(required = false) IFulltextSearchSvc theFullTextSearchSvc,
		IHapiTransactionService theHapiTransactionService) {
		myEntityManager = theEntityManager;
		myDeleteExpungeSqlBuilder = theDeleteExpungeSqlBuilder;
		myFullTextSearchSvc = theFullTextSearchSvc;
		myHapiTransactionService = theHapiTransactionService;
	}

	public int deleteExpungeSingleResource(JpaPid theJpaPid,
										   boolean theCascade,
										   Integer theCascadeMaxRounds) {
		assert TransactionSynchronizationManager.isActualTransactionActive();
		Long pid = theJpaPid.getId();
		DeleteExpungeSqlBuilder.DeleteExpungeSqlResult sqlResult =
			myDeleteExpungeSqlBuilder.convertPidsToDeleteExpungeSql(Collections.singleton(pid), theCascade, theCascadeMaxRounds);

		executeSqlList(sqlResult.getSqlStatementsToDeleteReferences());
		executeSqlList(sqlResult.getSqlStatementsToDeleteResources());

		return sqlResult.getRecordCount();
	}

	@Override
	public int deleteExpungeBatch(List<JpaPid> theJpaPids,
								  boolean theCascade,
								  Integer theCascadeMaxRounds,
								  RequestDetails theRequestDetails) {

		//assert there is no active transaction
		assert !TransactionSynchronizationManager.isActualTransactionActive();
		Set<Long> pids = JpaPid.toLongSet(theJpaPids);
		DeleteExpungeSqlBuilder.DeleteExpungeSqlResult sqlResult =
			myDeleteExpungeSqlBuilder.convertPidsToDeleteExpungeSql(pids, theCascade, theCascadeMaxRounds);

		IHapiTransactionService.IExecutionBuilder executionBuilder = myHapiTransactionService
			.withRequest(theRequestDetails)
			.withTransactionDetails(new TransactionDetails());

		executionBuilder.execute(() -> {
			executeSqlList(sqlResult.getSqlStatementsToDeleteReferences())
		});

		executionBuilder.execute(() -> {
			executeSqlList(sqlResult.getSqlStatementsToDeleteResources());
		});


		return sqlResult.getRecordCount();
	}


	private int executeSqlList(List<String> sqlList) {
		long totalDeleted = 0;

		StopWatch sw = new StopWatch();
		for (String sql : sqlList) {
			sw.restart();
			ourLog.info("Executing sql " + sql);
			int deleted = myEntityManager.createNativeQuery(sql).executeUpdate();
			ourLog.info("sql took " + StopWatch.formatMillis(sw.getMillis()));
			ourLog.info("#deleted records:" + deleted);
			totalDeleted += deleted;
		}

		ourLog.info("{} records deleted", totalDeleted);
	}

	private int deleteExpungeReferencesToResources(
		List<JpaPid> theJpaPids) {


		ourLog.info("Executing {} delete expunge sql commands to delete references ", sqlList.size());


		// TODO KHS instead of logging progress, produce result chunks that get aggregated into a delete expunge report

	}

	@Override
	public int deleteExpungeResources(List<JpaPid> theJpaPids, boolean theCascade, Integer theCascadeMaxRounds) {
		Set<Long> pids = JpaPid.toLongSet(theJpaPids);
		DeleteExpungeSqlBuilder.DeleteExpungeSqlResult sqlResult =
			myDeleteExpungeSqlBuilder.convertPidsToDeleteExpungeResourcesSql(pids);
		List<String> sqlList = sqlResult.getSqlStatementsToDeleteReferences();

		ourLog.info("Executing {} delete expunge sql commands", sqlList.size());
		long totalDeleted = 0;

		StopWatch sw = new StopWatch();
		for (String sql : sqlList) {
			sw.restart();
			ourLog.info("Executing sql " + sql);
			int deleted = myEntityManager.createNativeQuery(sql).executeUpdate();
			ourLog.info("sql took " + StopWatch.formatMillis(sw.getMillis()));
			ourLog.info("#deleted records:" + deleted);
			totalDeleted += deleted;
		}

		ourLog.info("{} records deleted", totalDeleted);
		clearHibernateSearchIndex(theJpaPids);

		// TODO KHS instead of logging progress, produce result chunks that get aggregated into a delete expunge report
		return sqlResult.getRecordCount();
	}

	@Override
	public boolean isCascadeSupported() {
		return true;
	}

	/**
	 * If we are running with HS enabled, the expunge operation will cause dangling documents because Hibernate Search is not aware of custom SQL queries that delete resources.
	 * This method clears the Hibernate Search index for the given resources.
	 */
	private void clearHibernateSearchIndex(List<JpaPid> thePersistentIds) {
		if (myFullTextSearchSvc != null && !myFullTextSearchSvc.isDisabled()) {
			List<Object> objectIds =
				thePersistentIds.stream().map(JpaPid::getId).collect(Collectors.toList());
			myFullTextSearchSvc.deleteIndexedDocumentsByTypeAndId(ResourceTable.class, objectIds);
			ourLog.info("Cleared Hibernate Search indexes.");
		}
	}
}
