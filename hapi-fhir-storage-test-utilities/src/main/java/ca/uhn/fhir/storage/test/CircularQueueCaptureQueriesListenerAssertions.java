/*-
 * #%L
 * HAPI-FHIR Storage Test Utilities
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
package ca.uhn.fhir.storage.test;

import ca.uhn.fhir.jpa.util.CircularQueueCaptureQueriesListener;
import ca.uhn.fhir.jpa.util.SqlQuery;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.Validate;
import org.assertj.core.api.Condition;
import org.assertj.core.description.Description;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Assertj class for asserting a set of {@link CircularQueueCaptureQueriesListener} counts.
 * This generates helpful failure messages. If any counts fail, the associated SQL statements
 * are also included in the failure message.
 *
 * @since 8.10.0
 */
public class CircularQueueCaptureQueriesListenerAssertions {

	private static final int NO_COUNT = -1;

	/**
	 * Constructor
	 */
	private CircularQueueCaptureQueriesListenerAssertions() {
		// non instantiable
	}

	public static QueryCondition onCurrentThread() {
		return new QueryCondition(false);
	}

	public static QueryCondition onAllThreads() {
		return new QueryCondition(true);
	}

	public static class QueryCondition extends Condition<CircularQueueCaptureQueriesListener> {

		private final boolean myAllThreads;
		private final List<BaseTest> myTests = new ArrayList<>();
		private CircularQueueCaptureQueriesListener myListener;
		private boolean myHaveSelectCounts;
		private boolean myHaveUpdateCounts;
		private boolean myHaveInsertCounts;
		private boolean myHaveDeleteCounts;
		private boolean myHaveCommitCounts;
		private boolean myHaveRollbackCounts;


		/**
		 * Constructor
		 */
		public QueryCondition(boolean theAllThreads) {
			myAllThreads = theAllThreads;
		}

		public QueryCondition selectCount(int theCount) {
			myHaveSelectCounts = true;
			myTests.add(new TestSelect(theCount, myAllThreads));
			return this;
		}

		public QueryCondition selectSqlContains(int theIndex, String theExpectedSelectSql) {
			myTests.add(new TestSelect(theIndex, myAllThreads, theExpectedSelectSql));
			return this;
		}

		public QueryCondition updateCount(int theCount) {
			myHaveUpdateCounts = true;
			myTests.add(new TestUpdate(theCount, myAllThreads));
			return this;
		}

		public QueryCondition insertCount(int theCount) {
			myHaveInsertCounts = true;
			myTests.add(new TestInsert(theCount, myAllThreads));
			return this;
		}

		public QueryCondition deleteCount(int theCount) {
			myHaveDeleteCounts = true;
			myTests.add(new TestDelete(theCount, myAllThreads));
			return this;
		}

		public QueryCondition commitCount(int theCount) {
			myHaveCommitCounts = true;
			myTests.add(new TestCommit(theCount, myAllThreads));
			return this;
		}

		public QueryCondition rollbackCount(int theCount) {
			myHaveRollbackCounts = true;
			myTests.add(new TestRollback(theCount, myAllThreads));
			return this;
		}

		public QueryCondition connectionCount(int theConnectionCount) {
			myTests.add(new TestConnections(theConnectionCount, myAllThreads));
			return this;
		}

		public QueryCondition noOtherCounts() {
			if (!myHaveSelectCounts) {
				selectCount(0);
			}
			if (!myHaveUpdateCounts) {
				updateCount(0);
			}
			if (!myHaveInsertCounts) {
				insertCount(0);
			}
			if (!myHaveDeleteCounts) {
				deleteCount(0);
			}
			if (!myHaveCommitCounts) {
				commitCount(0);
			}
			if (!myHaveRollbackCounts) {
				rollbackCount(0);
			}
			return this;
		}

		@Override
		public Description description() {
			return new Description() {
				@Override
				public String value() {
					Validate.notNull(myListener, "Listener must be set before calling value()");
					List<BaseTest> failingTests = myTests
						.stream()
						.filter(t -> t.test(myListener).isPresent())
						.toList();

					String retVal = failingTests
						.stream()
						.map(t -> t.test(myListener).orElseThrow())
						.collect(Collectors.joining("\n"));

					retVal += "\n\n";

					retVal += failingTests
						.stream()
						.map(t -> {
							String rendered = t.getName() + " Statements:\n";
							rendered += t.renderStatements(myListener);
							return rendered;
						})
						.collect(Collectors.joining("\n\n"));


					return retVal;
				}
			};
		}

		@Override
		public boolean matches(CircularQueueCaptureQueriesListener theListener) {
			myListener = theListener;
			return myTests.stream().allMatch(t -> t.test(theListener).isEmpty());
		}
    }

	private abstract static class BaseTest {
		protected final int myExpectCount;
		protected final boolean myForCurrentThread;

		public BaseTest(int theExpectCount, boolean theForCurrentThread) {
			myExpectCount = theExpectCount;
			myForCurrentThread = theForCurrentThread;
		}

		public Optional<String> test(CircularQueueCaptureQueriesListener theListener) {

			if (myExpectCount != NO_COUNT) {
				int actualCount = getCount(theListener);

				if (actualCount != myExpectCount) {
					String name = getName();
					String msg = String.format("\n  %-10s Expected[%d] Actual[%d]", name, myExpectCount, actualCount);
					return Optional.of(msg);
				}
			}

			return Optional.empty();
		}


		@Nonnull
		protected abstract String getName();

		protected abstract int getCount(CircularQueueCaptureQueriesListener theListener);

		@Nullable
		public String renderStatements(CircularQueueCaptureQueriesListener theListener) {
			return null;
		}
	}


	private static abstract class BaseSqlStatementTest extends BaseTest {

		private final Integer myExpectAtIndex;
		private final String myExpectedSql;

		/**
		 * Constructor for a statement counting assertion
		 */
		private BaseSqlStatementTest(int theExpectCount, boolean theForCurrentThread) {
			super(theExpectCount, theForCurrentThread);
			myExpectAtIndex = null;
			myExpectedSql = null;
		}

		/**
		 * Constructor for a statement matching assertion
		 */
		public BaseSqlStatementTest(int theIndex, boolean theForCurrentThread, String theExpectedSql) {
			super(NO_COUNT, theForCurrentThread);

			myExpectAtIndex = theIndex;
			myExpectedSql = theExpectedSql;
		}

		@Override
		protected int getCount(CircularQueueCaptureQueriesListener theListener) {
			List<SqlQuery> actualQueries = getActualStatements(theListener);
			return CircularQueueCaptureQueriesListener.countQueries(actualQueries);
		}

		@Override
		public Optional<String> test(CircularQueueCaptureQueriesListener theListener) {
			Optional<String> retVal = super.test(theListener);
			if (retVal.isEmpty()) {
				if (myExpectAtIndex != null && myExpectedSql != null) {
					SqlQuery statement = getActualStatements(theListener).get(myExpectAtIndex);
					String renderedSql = statement.getSql(true, false);
					if (!renderedSql.contains(myExpectedSql)) {
						retVal = Optional.of("Expected SQL: " + renderedSql + "\nTo contain: " + myExpectedSql);
					}
				}
			}
			return retVal;
		}

		protected abstract List<SqlQuery> getActualStatements(CircularQueueCaptureQueriesListener theListener);

		@Nullable
		public String renderStatements(CircularQueueCaptureQueriesListener theListener) {
			List<SqlQuery> statements = getActualStatements(theListener);
			return CircularQueueCaptureQueriesListener.formatQueries(statements);
		}

	}

	private static class TestSelect extends BaseSqlStatementTest {

		private TestSelect(int theExpectCount, boolean theForCurrentThread) {
			super(theExpectCount, theForCurrentThread);
		}

		private TestSelect(int theIndex, boolean theForCurrentThread, String theExpectedSql) {
			super(theIndex, theForCurrentThread, theExpectedSql);
		}

		@Nonnull
		@Override
		protected String getName() {
			return "SELECT";
		}

		@Override
		protected List<SqlQuery> getActualStatements(CircularQueueCaptureQueriesListener theListener) {
			List<SqlQuery> actualCount;
			if (myForCurrentThread) {
				actualCount = theListener.getSelectQueriesForCurrentThread();
			} else {
				actualCount = theListener.getSelectQueries();
			}
			return actualCount;
		}
	}

	private static class TestInsert extends BaseSqlStatementTest {

		private TestInsert(int theExpectCount, boolean theForCurrentThread) {
			super(theExpectCount, theForCurrentThread);
		}

		@Nonnull
		@Override
		protected String getName() {
			return "INSERT";
		}

		@Override
		protected List<SqlQuery> getActualStatements(CircularQueueCaptureQueriesListener theListener) {
			List<SqlQuery> actualCount;
			if (myForCurrentThread) {
				actualCount = theListener.getInsertQueriesForCurrentThread();
			} else {
				actualCount = theListener.getInsertQueries();
			}
			return actualCount;
		}
	}

	private static class TestDelete extends BaseSqlStatementTest {

		private TestDelete(int theExpectCount, boolean theForCurrentThread) {
			super(theExpectCount, theForCurrentThread);
		}

		@Nonnull
		@Override
		protected String getName() {
			return "DELETE";
		}

		@Override
		protected List<SqlQuery> getActualStatements(CircularQueueCaptureQueriesListener theListener) {
			List<SqlQuery> actualCount;
			if (myForCurrentThread) {
				actualCount = theListener.getDeleteQueriesForCurrentThread();
			} else {
				actualCount = theListener.getDeleteQueries();
			}
			return actualCount;
		}
	}

	private static class TestUpdate extends BaseSqlStatementTest {

		private TestUpdate(int theExpectCount, boolean theForCurrentThread) {
			super(theExpectCount, theForCurrentThread);
		}

		@Nonnull
		@Override
		protected String getName() {
			return "UPDATE";
		}

		@Override
		protected List<SqlQuery> getActualStatements(CircularQueueCaptureQueriesListener theListener) {
			List<SqlQuery> actualCount;
			if (myForCurrentThread) {
				actualCount = theListener.getUpdateQueriesForCurrentThread();
			} else {
				actualCount = theListener.getUpdateQueries();
			}
			return actualCount;
		}
	}

	private static class TestCommit extends BaseTest {

		private TestCommit(int theExpectCount, boolean theForCurrentThread) {
			super(theExpectCount, theForCurrentThread);
		}

		@Nonnull
		@Override
		protected String getName() {
			return "COMMIT";
		}

		@Override
		protected int getCount(CircularQueueCaptureQueriesListener theListener) {
			return theListener.countCommits();
		}
	}

	private static class TestRollback extends BaseTest {

		private TestRollback(int theExpectCount, boolean theForCurrentThread) {
			super(theExpectCount, theForCurrentThread);
		}

		@Nonnull
		@Override
		protected String getName() {
			return "ROLLBACK";
		}

		@Override
		protected int getCount(CircularQueueCaptureQueriesListener theListener) {
			return theListener.countRollbacks();
		}
	}

	private static class TestConnections extends BaseTest {

		private TestConnections(int theExpectCount, boolean theForCurrentThread) {
			super(theExpectCount, theForCurrentThread);
		}

		@Nonnull
		@Override
		protected String getName() {
			return "GET_CONNECTION";
		}

		@Override
		protected int getCount(CircularQueueCaptureQueriesListener theListener) {
			return theListener.countGetConnections();
		}
	}

}
