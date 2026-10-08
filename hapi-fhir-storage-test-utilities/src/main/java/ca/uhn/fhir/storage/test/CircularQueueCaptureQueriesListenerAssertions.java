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
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.assertj.core.api.Condition;
import org.assertj.core.description.Description;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * Assertj class for asserting a set of {@link CircularQueueCaptureQueriesListener} counts.
 * This generates helpful failure messages. If any counts fail, the associated SQL statements
 * are also included in the failure message.
 *
 * @since 8.10.0
 */
public class CircularQueueCaptureQueriesListenerAssertions {

	private static final int NO_COUNT = -1;
	public static final String LS = System.lineSeparator();
	public static final String DOUBLE_LS = LS + LS;

	/**
	 * Constructor
	 */
	private CircularQueueCaptureQueriesListenerAssertions() {
		// non instantiable
	}

	public static QueryCondition onCurrentThread() {
		return new QueryCondition(threadName -> Objects.equals(threadName, Thread.currentThread().getName()));
	}

	public static QueryCondition onAllThreads() {
		return new QueryCondition(threadName -> true);
	}

	/**
	 * Matches threads with {@link Thread#getName() names} matched by the given pattern. The regex
	 * is applied using a substring {@link Matcher#matches()}.
	 */
	public static QueryCondition onThreadsWithNameMatching(Pattern thePattern) {
		return new QueryCondition(threadName -> thePattern.matcher(threadName).matches());
	}

	public static class QueryCondition extends Condition<CircularQueueCaptureQueriesListener> {

		private final Predicate<String> myThreadPredicate;
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
		public QueryCondition(@Nonnull Predicate<String> theThreadPredicate) {
			Validate.notNull(theThreadPredicate, "theThreadPredicate must not be null");
			myThreadPredicate = theThreadPredicate;
		}

		public QueryCondition selectCount(int theCount) {
			myHaveSelectCounts = true;
			myTests.add(new TestSelect(theCount, myThreadPredicate));
			return this;
		}

		public StatementAtIndexBuilder selectSqlAtIndex(int theIndex) {
			return new StatementAtIndexBuilder(theIndex);
		}

		/**
		 * How many UPDATE statements were executed
		 * (uses {@link SqlCountTypeEnum#PARAMETER_SETS} counting)
		 */
		public QueryCondition updateCount(int theCount) {
			return updateCount(theCount, BaseSqlStatementTest.DEFAULT_SQL_COUNT_TYPE);
		}

		public QueryCondition updateCount(int theCount, @Nonnull SqlCountTypeEnum theCountType) {
			myHaveUpdateCounts = true;
			myTests.add(new TestUpdate(theCount, myThreadPredicate, theCountType));
			return this;
		}

		/**
		 * How many INSERT statements were executed
		 * (uses {@link SqlCountTypeEnum#PARAMETER_SETS} counting)
		 */
		public QueryCondition insertCount(int theCount) {
			return insertCount(theCount, BaseSqlStatementTest.DEFAULT_SQL_COUNT_TYPE);
		}
		public QueryCondition insertCount(int theCount, @Nonnull SqlCountTypeEnum theCountType) {
			myHaveInsertCounts = true;
			myTests.add(new TestInsert(theCount, myThreadPredicate, theCountType));
			return this;
		}

		/**
		 * How many DELETE statements were executed
		 * (uses {@link SqlCountTypeEnum#PARAMETER_SETS} counting)
		 */
		public QueryCondition deleteCount(int theCount) {
			return deleteCount(theCount, BaseSqlStatementTest.DEFAULT_SQL_COUNT_TYPE);
		}

		public QueryCondition deleteCount(int theCount, @Nonnull SqlCountTypeEnum theCountType) {
			myHaveDeleteCounts = true;
			myTests.add(new TestDelete(theCount, myThreadPredicate, theCountType));
			return this;
		}

		public QueryCondition commitCount(int theCount) {
			myHaveCommitCounts = true;
			myTests.add(new TestCommit(theCount, myThreadPredicate));
			return this;
		}

		public QueryCondition rollbackCount(int theCount) {
			myHaveRollbackCounts = true;
			myTests.add(new TestRollback(theCount, myThreadPredicate));
			return this;
		}

		public QueryCondition connectionCount(int theConnectionCount) {
			myTests.add(new TestConnections(theConnectionCount, myThreadPredicate));
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
						.collect(Collectors.joining(LS));

					retVal += DOUBLE_LS;

					retVal += failingTests
						.stream()
						.map(t -> {
							String rendered = t.getName() + " Statements:\n";
							rendered += t.renderStatements(myListener);
							return rendered;
						})
						.collect(Collectors.joining(DOUBLE_LS));


					return retVal;
				}
			};
		}

		@Override
		public boolean matches(CircularQueueCaptureQueriesListener theListener) {
			myListener = theListener;
			return myTests.stream().allMatch(t -> t.test(theListener).isEmpty());
		}


		public class StatementAtIndexBuilder {

			private final int myIndex;
			private boolean myInlineParams = true;

			public StatementAtIndexBuilder(int theIndex) {
				myIndex = theIndex;
			}

			public QueryCondition contains(String theExpectedSql) {
				myTests.add(new TestSelect(myIndex, myThreadPredicate, myInlineParams, theExpectedSql, SqlMatchModeEnum.CONTAINS));
				return QueryCondition.this;
			}

			public QueryCondition doesNotContain(String theNotExpectedSql) {
				myTests.add(new TestSelect(myIndex, myThreadPredicate, myInlineParams, theNotExpectedSql, SqlMatchModeEnum.DOES_NOT_CONTAIN));
				return QueryCondition.this;
			}

			public QueryCondition startsWith(String theExpectedSql) {
				myTests.add(new TestSelect(myIndex, myThreadPredicate, myInlineParams, theExpectedSql, SqlMatchModeEnum.STARTS_WITH));
				return QueryCondition.this;
			}

			public QueryCondition endsWith(String theExpectedSql) {
				myTests.add(new TestSelect(myIndex, myThreadPredicate, myInlineParams, theExpectedSql, SqlMatchModeEnum.ENDS_WITH));
				return QueryCondition.this;
			}

			/**
			 * @param theExpectContain If {@literal true} then the SQL statement must contain the given SQL, otherwise it must not contain it.
			 */
			public QueryCondition mightContain(boolean theExpectContain, String theExpectedOrNotExpectedSql) {
				if (theExpectContain) {
					return contains(theExpectedOrNotExpectedSql);
				} else {
					return doesNotContain(theExpectedOrNotExpectedSql);
				}
			}

			public QueryCondition countInstances(int theExpectedCount, String theExpectedSql) {
				myTests.add(new TestSelect(myIndex, myThreadPredicate, myInlineParams, theExpectedSql, SqlMatchModeEnum.COUNT_INSTANCES, theExpectedCount));
				return QueryCondition.this;
			}

			public QueryCondition countInstancesIgnoreCase(int theExpectedCount, String theExpectedSql) {
				myTests.add(new TestSelect(myIndex, myThreadPredicate, myInlineParams, theExpectedSql, SqlMatchModeEnum.COUNT_INSTANCES_IGNORE_CASE, theExpectedCount));
				return QueryCondition.this;
			}

			/**
			 * Does the SQL match exactly?
			 */
			public QueryCondition matches(String theExpectedSql) {
				myTests.add(new TestSelect(myIndex, myThreadPredicate, myInlineParams, theExpectedSql, SqlMatchModeEnum.MATCHES));
				return QueryCondition.this;
			}

			public StatementAtIndexBuilder withoutInlinedParams() {
				myInlineParams = false;
				return this;
			}
		}

	}

	private abstract static class BaseTest {
		protected final int myExpectCount;
		protected final Predicate<String> myThreadPredicate;

		public BaseTest(int theExpectCount, Predicate<String> theThreadPredicate) {
			myExpectCount = theExpectCount;
			myThreadPredicate = theThreadPredicate;
		}

		public Optional<String> test(CircularQueueCaptureQueriesListener theListener) {

			if (myExpectCount != NO_COUNT) {
				int actualCount = getCount(theListener);

				if (actualCount != myExpectCount) {
					String name = getName();
					String msg = String.format("%s  %-20s Expected[%d] Actual[%d]", LS, name, myExpectCount, actualCount);
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

		private static final SqlCountTypeEnum DEFAULT_SQL_COUNT_TYPE = SqlCountTypeEnum.PARAMETER_SETS;

		private final Integer myExpectAtIndex;
		private final String myExpectedSql;
		private final SqlMatchModeEnum mySqlMatchMode;
		private final int myExpectedCount;
		private final boolean myInlineParams;
		protected final SqlCountTypeEnum myCountType;

		/**
		 * Constructor for a statement counting assertion
		 */
		private BaseSqlStatementTest(int theExpectCount, Predicate<String> theThreadPredicate, SqlCountTypeEnum theCountType) {
			super(theExpectCount, theThreadPredicate);
			myInlineParams = false;
			myExpectAtIndex = null;
			myExpectedSql = null;
			mySqlMatchMode = null;
			myExpectedCount = 0;
			myCountType = theCountType;
		}

		/**
		 * Constructor for a statement matching assertion
		 */
		public BaseSqlStatementTest(int theIndex, Predicate<String> theThreadPredicate, boolean theInlineParams, String theExpectedSql, SqlMatchModeEnum theSqlMatchMode) {
			this(theIndex, theThreadPredicate, theInlineParams, theExpectedSql, theSqlMatchMode, 0);
		}

		/**
		 * Constructor for a statement matching assertion
		 */
		public BaseSqlStatementTest(int theIndex, Predicate<String> theThreadPredicate, boolean theInlineParams, String theExpectedSql, SqlMatchModeEnum theSqlMatchMode, int theExpectedCount) {
			super(NO_COUNT, theThreadPredicate);

			myInlineParams = theInlineParams;
			myExpectAtIndex = theIndex;
			myExpectedSql = theExpectedSql;
			mySqlMatchMode = theSqlMatchMode;
			myExpectedCount = theExpectedCount;
			myCountType = DEFAULT_SQL_COUNT_TYPE;
		}

		@Override
		protected int getCount(CircularQueueCaptureQueriesListener theListener) {
			List<SqlQuery> actualQueries = getActualStatements(theListener);
			Validate.notNull(myCountType, "No count type configured");
			return switch (myCountType) {
				case PARAMETER_SETS -> CircularQueueCaptureQueriesListener.countQueries(actualQueries);
				case STATEMENTS -> actualQueries.size();
			};
		}

		@Override
		public Optional<String> test(CircularQueueCaptureQueriesListener theListener) {
			Optional<String> retVal = super.test(theListener);
			if (retVal.isEmpty()) {
				if (myExpectAtIndex != null && myExpectedSql != null) {
					SqlQuery statement = getActualStatements(theListener).get(myExpectAtIndex);
					String renderedSql = statement.getSql(myInlineParams, false);
					retVal = switch (requireNonNull(mySqlMatchMode)) {
						case CONTAINS -> {
							if (!renderedSql.contains(myExpectedSql)) {
								yield Optional.of(LS + "Expected SQL: " + renderedSql + LS +
														   "  to contain: " + myExpectedSql);
							} else {
								yield Optional.empty();
							}
						}
						case DOES_NOT_CONTAIN -> {
							if (renderedSql.contains(myExpectedSql)) {
								yield Optional.of(LS + "Expected SQL  : " + renderedSql + LS +
									"not to contain: " + myExpectedSql);
							} else {
								yield Optional.empty();
							}
						}
						case COUNT_INSTANCES, COUNT_INSTANCES_IGNORE_CASE -> {
							String expectedSqlToMatch = myExpectedSql;
							String renderedSqlToMatch = renderedSql;
							if (mySqlMatchMode == SqlMatchModeEnum.COUNT_INSTANCES_IGNORE_CASE) {
								expectedSqlToMatch = expectedSqlToMatch.toLowerCase(Locale.US);
								 renderedSqlToMatch = renderedSqlToMatch.toLowerCase(Locale.US);
							}
							int matchCount = StringUtils.countMatches(renderedSqlToMatch, expectedSqlToMatch);
							if (matchCount != myExpectedCount) {
								yield Optional.of(LS + "Expected SQL: " + renderedSqlToMatch + LS +
									" to contain " + myExpectedCount + " but found " + matchCount + " instances of : " + expectedSqlToMatch);
							} else {
								yield Optional.empty();
							}
						}
						case MATCHES -> {
							Pattern pattern = Pattern.compile(myExpectedSql);
							if (!pattern.matcher(renderedSql).matches()) {
								yield Optional.of(LS + "Expected SQL  : " + renderedSql + LS +
									"to match      : " + myExpectedSql);
							} else {
								yield Optional.empty();
							}
						}
						case ENDS_WITH -> {
							if (!renderedSql.endsWith(myExpectedSql)) {
								yield Optional.of(LS + "Expected SQL: " + renderedSql + LS +
														   " to end with: " + myExpectedSql);
							} else {
								yield Optional.empty();
							}
						}
						case STARTS_WITH -> {
							if (!renderedSql.startsWith(myExpectedSql)) {
								yield Optional.of(LS + "Expected SQL: " + renderedSql + LS +
														   " to start with: " + myExpectedSql);
							} else {
								yield Optional.empty();
							}
						}
					};


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

		private TestSelect(int theExpectCount, Predicate<String> theThreadPredicate) {
			super(theExpectCount, theThreadPredicate, SqlCountTypeEnum.PARAMETER_SETS);
		}

		private TestSelect(int theIndex, Predicate<String> theThreadPredicate,boolean theInlineParams,  String theExpectedSql, SqlMatchModeEnum theSqlMatchMode) {
			super(theIndex, theThreadPredicate, theInlineParams, theExpectedSql, theSqlMatchMode);
		}

		public TestSelect(int theIndex, Predicate<String> theThreadPredicate, boolean theInlineParams, String theExpectedSql, SqlMatchModeEnum theSqlMatchMode, int theExpectedCount) {
			super(theIndex, theThreadPredicate, theInlineParams, theExpectedSql, theSqlMatchMode, theExpectedCount);
		}

		@Nonnull
		@Override
		protected String getName() {
			return "SELECT";
		}

		@Override
		protected List<SqlQuery> getActualStatements(CircularQueueCaptureQueriesListener theListener) {
			return theListener.getSelectQueries().stream().filter(q -> myThreadPredicate.test(q.getThreadName())).toList();
		}
	}

	private static class TestInsert extends BaseSqlStatementTest {

		private TestInsert(int theExpectCount, Predicate<String> theThreadPredicate, SqlCountTypeEnum theCountType) {
			super(theExpectCount, theThreadPredicate, theCountType);
		}

		@Nonnull
		@Override
		protected String getName() {
			return "INSERT";
		}

		@Override
		protected List<SqlQuery> getActualStatements(CircularQueueCaptureQueriesListener theListener) {
			return theListener.getInsertQueries().stream().filter(q -> myThreadPredicate.test(q.getThreadName())).toList();
		}
	}

	private static class TestDelete extends BaseSqlStatementTest {

		private TestDelete(int theExpectCount, Predicate<String> theThreadPredicate, SqlCountTypeEnum theCountType) {
			super(theExpectCount, theThreadPredicate, theCountType);
		}

		@Nonnull
		@Override
		protected String getName() {
			return "DELETE";
		}

		@Override
		protected List<SqlQuery> getActualStatements(CircularQueueCaptureQueriesListener theListener) {
			return theListener.getDeleteQueries().stream().filter(q -> myThreadPredicate.test(q.getThreadName())).toList();
		}
	}

	private static class TestUpdate extends BaseSqlStatementTest {

		private TestUpdate(int theExpectCount, Predicate<String> theThreadPredicate, SqlCountTypeEnum theCountType) {
			super(theExpectCount, theThreadPredicate, theCountType);
		}

		@Nonnull
		@Override
		protected String getName() {
			return "UPDATE";
		}

		@Override
		protected List<SqlQuery> getActualStatements(CircularQueueCaptureQueriesListener theListener) {
			return theListener.getUpdateQueries().stream().filter(q -> myThreadPredicate.test(q.getThreadName())).toList();
		}
	}

	private static class TestCommit extends BaseTest {

		private TestCommit(int theExpectCount, Predicate<String> theThreadPredicate) {
			super(theExpectCount, theThreadPredicate);
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

		private TestRollback(int theExpectCount, Predicate<String> theThreadPredicate) {
			super(theExpectCount, theThreadPredicate);
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

		private TestConnections(int theExpectCount, Predicate<String> theThreadPredicate) {
			super(theExpectCount, theThreadPredicate);
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

	private enum SqlMatchModeEnum {

		CONTAINS,
		DOES_NOT_CONTAIN,
		COUNT_INSTANCES,
		COUNT_INSTANCES_IGNORE_CASE,
		MATCHES,
		STARTS_WITH, ENDS_WITH

	}

}
