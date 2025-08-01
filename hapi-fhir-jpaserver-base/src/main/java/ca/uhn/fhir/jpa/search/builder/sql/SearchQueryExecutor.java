/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.search.builder.sql;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.search.builder.ISearchQueryExecutor;
import ca.uhn.fhir.jpa.util.ScrollableResultsIterator;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.IoUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;
import jakarta.persistence.Query;
import org.apache.commons.lang3.Validate;
import org.hibernate.CacheMode;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Objects;

public class SearchQueryExecutor implements ISearchQueryExecutor {

	private static final JpaPid NO_MORE = JpaPid.fromId(-1L);
	private static final SearchQueryExecutor NO_VALUE_EXECUTOR = new SearchQueryExecutor();
	private static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
	private static final Logger ourLog = LoggerFactory.getLogger(SearchQueryExecutor.class);
	private final GeneratedSql myGeneratedSql;

	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	private EntityManager myEntityManager;

	private boolean myQueryInitialized;
	private ScrollableResultsIterator<Object> myResultSet;
	private JpaPid myNext;

	/**
	 * Constructor
	 */
	public SearchQueryExecutor(GeneratedSql theGeneratedSql, Integer theMaxResultsToFetch) {
		Validate.notNull(theGeneratedSql, "theGeneratedSql must not be null");
		myGeneratedSql = theGeneratedSql;
		myQueryInitialized = false;
	}

	/**
	 * Internal constructor for empty executor
	 */
	private SearchQueryExecutor() {
		assert NO_MORE != null;

		myGeneratedSql = null;
		myNext = NO_MORE;
	}

	@Override
	public void close() {
		IoUtil.closeQuietly(myResultSet);
	}

	@Override
	public boolean hasNext() {
		fetchNext();
		return !NO_MORE.equals(myNext);
	}

	@Override
	public JpaPid next() {
		fetchNext();
		Validate.isTrue(hasNext(), "Can not call next() right now, no data remains");
		JpaPid next = myNext;
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
					HapiTransactionService.requireTransaction();
					ourLog.trace("About to execute SQL: {}. Parameters: {}", sql, Arrays.toString(args));

					Query nativeQuery = myEntityManager.createNativeQuery(sql);
					org.hibernate.query.Query<?> hibernateQuery = (org.hibernate.query.Query<?>) nativeQuery;
					for (int i = 1; i <= args.length; i++) {
						hibernateQuery.setParameter(i, args[i - 1]);
					}

					/*
					 * These settings help to ensure that we use a search cursor
					 * as opposed to loading all search results into memory
					 */
					hibernateQuery.setFetchSize(500000);
					hibernateQuery.setCacheable(false);
					hibernateQuery.setCacheMode(CacheMode.IGNORE);
					hibernateQuery.setReadOnly(true);

					// This tells hibernate not to flush when we call scroll(), but rather to wait until the transaction
					// commits and
					// only flush then.  We need to do this so that any exceptions that happen in the transaction happen
					// when
					// we try to commit the transaction, and not here.
					// See the test called testTransaction_multiThreaded (in FhirResourceDaoR4ConcurrentWriteTest) which
					// triggers
					// the following exception if we don't set this flush mode:
					// java.util.concurrent.ExecutionException:
					// org.springframework.transaction.UnexpectedRollbackException: Transaction silently rolled back
					// because it has been marked as rollback-only
					hibernateQuery.setFlushMode(FlushModeType.COMMIT);
					ScrollableResults scrollableResults = hibernateQuery.scroll(ScrollMode.FORWARD_ONLY);
					myResultSet = new ScrollableResultsIterator<>(scrollableResults);
					myQueryInitialized = true;
				}

				if (myResultSet == null || !myResultSet.hasNext()) {
					myNext = NO_MORE;
				} else {
					myNext = getNextPid(myResultSet);
				}

			} catch (Exception e) {
				ourLog.error("Failed to create or execute SQL query", e);
				close();
				throw new InternalErrorException(Msg.code(1262) + e, e);
			}
		}
	}

	private JpaPid getNextPid(ScrollableResultsIterator<Object> theResultSet) {
		Object nextRow = Objects.requireNonNull(theResultSet.next());
		// We should typically get two columns back, the first is the partition ID and the second
		// is the resource ID. But if we're doing a count query, we'll get a single column in an array
		// or maybe even just a single non array value depending on how the platform handles it.
		if (nextRow instanceof Number) {
			return JpaPid.fromId(((Number) nextRow).longValue());
		} else {
			Object[] nextRowAsArray = (Object[]) nextRow;
			if (nextRowAsArray.length == 1) {
				return JpaPid.fromId((Long) nextRowAsArray[0]);
			} else {
				int i;
				// TODO MB add a strategy object to GeneratedSql to describe the result set.
				// or make SQE generic
				// Comment to reviewer: this will be cleaner with the next
				// merge from ja_20240718_pk_schema_selector

				// We have some cases to distinguish:
				// - res_id
				// - count
				// - partition_id, res_id
				// - res_id, coord-dist
				// - partition_id, res_id, coord-dist
				// Assume res_id is first Long in row, and is in first two columns
				if (nextRowAsArray[0] instanceof Long) {
					if (nextRowAsArray[1] instanceof String) {
						return JpaPid.fromIdAndResourceType((Long) nextRowAsArray[0], (String) nextRowAsArray[1]);
					}
					return JpaPid.fromId((Long) nextRowAsArray[0]);
				} else {
					Integer partitionId = (Integer) nextRowAsArray[0];
					Long pid = (Long) nextRowAsArray[1];
					return JpaPid.fromId(pid, partitionId);
				}
			}
		}
	}

	public static SearchQueryExecutor emptyExecutor() {
		return NO_VALUE_EXECUTOR;
	}
}
