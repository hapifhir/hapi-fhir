package ca.uhn.fhir.jpa.search.builder.sql;

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

import ca.uhn.fhir.jpa.util.ScrollableResultsIterator;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.IoUtil;
import org.apache.commons.lang3.Validate;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import java.io.Closeable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Iterator;
import java.util.List;

public class SearchQueryExecutor implements Iterator<Long>, Closeable {

	private static final Long NO_MORE = -1L;
	private static final SearchQueryExecutor NO_VALUE_EXECUTOR = new SearchQueryExecutor();
	private static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
	private static final Logger ourLog = LoggerFactory.getLogger(SearchQueryExecutor.class);
	private final GeneratedSql myGeneratedSql;
	private final Integer myMaxResultsToFetch;

	// FIXME: remove
	//	@Autowired
//	private HapiFhirLocalContainerEntityManagerFactoryBean myEntityManagerFactory;

	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	private EntityManager myEntityManager;
	private boolean myQueryInitialized;
	private Connection myConnection;
	private PreparedStatement myStatement;
	private ScrollableResultsIterator<Number> myResultSet;
	private Long myNext;

	/**
	 * Constructor
	 */
	public SearchQueryExecutor(GeneratedSql theGeneratedSql, Integer theMaxResultsToFetch) {
		Validate.notNull(theGeneratedSql, "theGeneratedSql must not be null");
		myGeneratedSql = theGeneratedSql;
		myQueryInitialized = false;
		myMaxResultsToFetch = theMaxResultsToFetch;
	}

	/**
	 * Internal constructor for empty executor
	 */
	private SearchQueryExecutor() {
		assert NO_MORE != null;

		myGeneratedSql = null;
		myMaxResultsToFetch = null;
		myNext = NO_MORE;
	}

	@Override
	public void close() {
		IoUtil.closeQuietly(myResultSet);

		// FIXME: remove
//		IoUtil.closeQuietly(myResultSet);
//		JdbcUtils.closeStatement(myStatement);
//		if (myEntityManagerFactory != null) {
//			DataSourceUtils.releaseConnection(myConnection, myEntityManagerFactory.getDataSource());
//		}
//		myResultSet = null;
//		myStatement = null;
//		myConnection = null;
	}

	@Override
	public boolean hasNext() {
		fetchNext();
		return !NO_MORE.equals(myNext);
	}

	@Override
	public Long next() {
		fetchNext();
		Validate.isTrue(hasNext(), "Can not call next() right now, no data remains");
		Long next = myNext;
		myNext = null;
		return next;
	}

	private void fetchNext() {
		if (myNext == null) {
			String sql = myGeneratedSql.getSql();
			Object[] args = myGeneratedSql.getBindVariables().toArray(EMPTY_OBJECT_ARRAY);

			try {
				if (!myQueryInitialized) {

					/*
					 * Note that we use the spring managed connection, and the expectation is that a transaction that
					 * is managed by Spring has been started before this method is called.
					 */
					assert TransactionSynchronizationManager.isSynchronizationActive();

					// Run an explain plan
					{
						ourLog.info("About to execute SQL: explain {}", sql);
						Query nativeQuery = myEntityManager.createNativeQuery("explain " + sql);
						for (int i = 1; i <= args.length; i++) {
							nativeQuery.setParameter(i, args[i - 1]);
						}
						List outcome = nativeQuery.getResultList();
						ourLog.info("Explain plan: {}", outcome);
					}

					Query nativeQuery = myEntityManager.createNativeQuery(sql);
					org.hibernate.query.Query<?> hibernateQuery = (org.hibernate.query.Query<?>) nativeQuery;
					for (int i = 1; i <= args.length; i++) {
						hibernateQuery.setParameter(i, args[i - 1]);
					}

					ourLog.info("About to execute SQL: {}", sql);

					ScrollableResults scrollableResults = hibernateQuery.scroll(ScrollMode.FORWARD_ONLY);
					myResultSet = new ScrollableResultsIterator<>(scrollableResults);
					myQueryInitialized = true;

					// FIXME: remove
//					myConnection = DataSourceUtils.getConnection(myEntityManagerFactory.getDataSource());
//					myStatement = myConnection.prepareStatement(sql);
//
//					if (myMaxResultsToFetch != null) {
//						myStatement.setMaxRows(myMaxResultsToFetch);
//					}
//
//					for (int i = 0; i < args.length; i++) {
//						Object nextObject = args[i];
//						if (nextObject instanceof Date) {
//							Timestamp ts = new Timestamp(((Date) nextObject).getTime());
//							myStatement.setTimestamp(i + 1, ts);
//						} else {
//							myStatement.setObject(i + 1, nextObject);
//						}
//					}
//					myResultSet = myStatement.executeQuery();
//					myQueryInitialized = true;
				}

				if (myResultSet == null || !myResultSet.hasNext()) {
					myNext = NO_MORE;
				} else {
					Number next = myResultSet.next();
					myNext = next.longValue();
				}


				// FIXME: remove
//				if (myResultSet == null || !myResultSet.next()) {
//					myNext = NO_MORE;
//				} else {
//					myNext = myResultSet.getLong(1);
//				}


			} catch (Exception e) {
				ourLog.error("Failed to create or execute SQL query", e);
				close();
				throw new InternalErrorException(e);
			}
		}
	}

	public static SearchQueryExecutor emptyExecutor() {
		return NO_VALUE_EXECUTOR;
	}
}

