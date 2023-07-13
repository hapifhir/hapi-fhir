/*-
 * #%L
 * HAPI FHIR JPA Server - HFQL Driver
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
package ca.uhn.fhir.jpa.fql.parser;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.DataFormatException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class HfqlStatementParser {

	public static final String KEYWORD_AND = "AND";
	public static final String KEYWORD_HAVING = "HAVING";
	public static final String KEYWORD_WHERE = "WHERE";
	public static final String KEYWORD_SELECT = "SELECT";
	public static final String KEYWORD_FROM = "FROM";
	public static final String KEYWORD_LIMIT = "LIMIT";
	public static final String KEYWORD_GROUP = "GROUP";
	public static final String KEYWORD_ORDER = "ORDER";
	public static final String KEYWORD_TRUE = "TRUE";
	public static final String KEYWORD_FALSE = "FALSE";
	private static final Set<String> DIRECTIVE_KEYWORDS = Set.of(
			KEYWORD_FROM, KEYWORD_GROUP, KEYWORD_LIMIT, KEYWORD_ORDER, KEYWORD_WHERE, KEYWORD_SELECT, KEYWORD_HAVING);
	private final HfqlLexer myLexer;
	private final FhirContext myFhirContext;
	private BaseState myState;

	private HfqlStatement myStatement;

	public HfqlStatementParser(FhirContext theFhirContext, String theInput) {
		myFhirContext = theFhirContext;
		myLexer = new HfqlLexer(theInput);
		myState = new InitialState();
	}

	/**
	 * This method may only be called once for a given instance
	 */
	public HfqlStatement parse() {
		Validate.isTrue(myStatement == null, "Already completed parsing");
		myStatement = new HfqlStatement();

		while (myLexer.hasNextToken(myState.getLexerOptions())) {
			HfqlLexerToken nextToken = myLexer.getNextToken(myState.getLexerOptions());
			myState.consume(nextToken);
		}

		if (StringUtils.isBlank(myStatement.getFromResourceName())) {
			throw newExceptionUnexpectedTokenExpectToken(null, KEYWORD_FROM);
		}

		if (myStatement.getSelectClauses().isEmpty()) {
			throw newExceptionUnexpectedTokenExpectToken(null, KEYWORD_SELECT);
		}

		return myStatement;
	}

	@Nonnull
	private HfqlLexerToken getNextTokenRequired(@Nonnull HfqlLexerOptions theOptions) {
		if (!myLexer.hasNextToken(theOptions)) {
			throw newExceptionUnexpectedToken(null);
		}
		return myLexer.getNextToken(theOptions);
	}

	@Nonnull
	private static DataFormatException newExceptionUnexpectedToken(@Nullable HfqlLexerToken theToken) {
		return newExceptionUnexpectedTokenExpectDescription(theToken, null);
	}

	@Nonnull
	private static DataFormatException newExceptionUnexpectedTokenExpectToken(
			@Nullable HfqlLexerToken theToken, @Nonnull String theExpectedToken) {
		return newExceptionUnexpectedTokenExpectDescription(theToken, "\"" + theExpectedToken + "\"");
	}

	@Nonnull
	private static DataFormatException newExceptionUnexpectedTokenExpectDescription(
			@Nullable HfqlLexerToken theToken, @Nullable String theExpectedDescription) {
		StringBuilder b = new StringBuilder();
		b.append("Unexpected ");
		if (theToken != null) {
			b.append("token");
		} else {
			b.append("end of stream");
		}
		if (theExpectedDescription != null) {
			b.append(" (expected ");
			b.append(theExpectedDescription);
			b.append(")");
		}
		if (theToken != null) {
			b.append(" at position ");
			b.append(theToken.describePosition());
			b.append(": ");
			b.append(theToken.getToken());
		}
		String message = b.toString();
		return new DataFormatException(message);
	}

	@Nonnull
	private static DataFormatException newExceptionUnknownResourceType(HfqlLexerToken theToken, String resourceType) {
		return new DataFormatException("Invalid FROM statement. Unknown resource type '" + resourceType
				+ "' at position: " + theToken.describePosition());
	}

	private static void validateNotPresent(List<?> theClauses, HfqlLexerToken theKeyword) {
		if (!theClauses.isEmpty()) {
			throw newExceptionUnexpectedToken(theKeyword);
		}
	}

	private static void validateNotPresent(Object theValue, HfqlLexerToken theKeyword) {
		if (theValue != null) {
			throw newExceptionUnexpectedToken(theKeyword);
		}
	}

	private enum WhereModeEnum {
		HAVING,
		WHERE
	}

	/**
	 * No tokens consumed yet
	 */
	public class InitialState extends BaseRootState {
		// nothing
	}

	/**
	 * Have consumed a 'from' token but not a resource type yet
	 */
	public class StateFromStart extends BaseState {
		@Override
		void consume(HfqlLexerToken theToken) {
			String resourceType = theToken.asString();
			if (!myFhirContext.getResourceTypes().contains(resourceType)) {
				throw newExceptionUnknownResourceType(theToken, resourceType);
			}
			myStatement.setFromResourceName(resourceType);
			myState = new StateFromAfter();
		}
	}

	/**
	 * Have consumed a 'from' token and a resource type
	 */
	public class StateFromAfter extends BaseRootState {
		// nothing
	}

	/**
	 * We're in the select statement
	 */
	public class StateInSelect extends BaseState {
		@Nonnull
		@Override
		public HfqlLexerOptions getLexerOptions() {
			return HfqlLexerOptions.FHIRPATH_EXPRESSION;
		}

		@Override
		void consume(HfqlLexerToken theToken) {
			String asKeyword = theToken.asKeyword();
			HfqlStatement.SelectClause clause;
			if (asKeyword.startsWith("COUNT(") && asKeyword.endsWith(")")) {
				String countClause = theToken.asString().substring("COUNT(".length(), asKeyword.length() - 1);
				clause = myStatement.addSelectClause(countClause, HfqlStatement.SelectClauseOperator.COUNT);
				clause.setAlias(theToken.getToken());
			} else {
				String string = theToken.asString();
				clause = myStatement.addSelectClause(string);
			}
			myState = new StateInSelectAfterClause(clause);
		}
	}

	private class StateInSelectAfterClause extends StateSelectAfterClauseFinal {
		public StateInSelectAfterClause(HfqlStatement.SelectClause theSelectClause) {
			super(theSelectClause);
		}

		@Override
		void consume(HfqlLexerToken theToken) {
			if (theToken.getToken().equals(":")) {
				HfqlLexerToken nextToken = getNextTokenRequired(HfqlLexerOptions.FHIRPATH_EXPRESSION);
				String clause = nextToken.asString();
				String alias = mySelectClause.getClause();
				mySelectClause.setAlias(alias);
				mySelectClause.setClause(clause);
				myState = new StateSelectAfterClauseFinal(mySelectClause);
			} else if (theToken.asKeyword().equals("AS")) {
				HfqlLexerToken nextToken = getNextTokenRequired(HfqlLexerOptions.HFQL_TOKEN);
				String alias = nextToken.asString();
				mySelectClause.setAlias(alias);
				myState = new StateSelectAfterClauseFinal(mySelectClause);
			} else {
				super.consume(theToken);
			}
		}
	}

	private class StateSelectAfterClauseFinal extends BaseRootState {
		protected final HfqlStatement.SelectClause mySelectClause;

		private StateSelectAfterClauseFinal(HfqlStatement.SelectClause theSelectClause) {
			mySelectClause = theSelectClause;
		}

		@Nonnull
		@Override
		public HfqlLexerOptions getLexerOptions() {
			return HfqlLexerOptions.FHIRPATH_EXPRESSION;
		}

		@Override
		void consume(HfqlLexerToken theToken) {
			if (theToken.getToken().equals(",")) {
				myState = new StateInSelect();
			} else if (!DIRECTIVE_KEYWORDS.contains(theToken.asKeyword())) {
				String newClause = mySelectClause.getClause() + " " + theToken.getToken();
				mySelectClause.setClause(newClause);
			} else {
				super.consume(theToken);
			}
		}
	}

	private class StateInWhereInitial extends BaseState {
		private final WhereModeEnum myWhereMode;

		public StateInWhereInitial(WhereModeEnum theWhereMode) {
			myWhereMode = theWhereMode;
		}

		@Nonnull
		@Override
		public HfqlLexerOptions getLexerOptions() {
			if (myWhereMode == WhereModeEnum.HAVING) {
				return HfqlLexerOptions.FHIRPATH_EXPRESSION;
			} else {
				return HfqlLexerOptions.SEARCH_PARAMETER_NAME;
			}
		}

		@Override
		void consume(HfqlLexerToken theToken) {
			if (myWhereMode == WhereModeEnum.WHERE) {
				HfqlStatement.HavingClause havingClause = myStatement.addWhereClause();
				String token = theToken.getToken();
				havingClause.setLeft(token);
				myState = new StateInWhereAfterLeft(myWhereMode, havingClause);
			} else {
				assert myWhereMode == WhereModeEnum.HAVING;

				StringBuilder token = new StringBuilder(theToken.getToken());
				while (myLexer.hasNextToken(HfqlLexerOptions.FHIRPATH_EXPRESSION)) {
					HfqlLexerToken nextToken = myLexer.peekNextToken(HfqlLexerOptions.FHIRPATH_EXPRESSION);
					String nextTokenAsKeyword = nextToken.asKeyword();
					if (KEYWORD_AND.equals(nextTokenAsKeyword) || DIRECTIVE_KEYWORDS.contains(nextTokenAsKeyword)) {
						break;
					}
					if ("=".equals(nextTokenAsKeyword) || "IN".equals(nextTokenAsKeyword)) {
						HfqlStatement.HavingClause havingClause = myStatement.addHavingClause();
						havingClause.setLeft(token.toString());
						myState = new StateInWhereAfterLeft(myWhereMode, havingClause);
						return;
					}

					myLexer.consumeNextToken();
					token.append(' ').append(nextToken.getToken());
				}

				HfqlStatement.HavingClause havingClause = myStatement.addHavingClause();
				havingClause.setOperator(HfqlStatement.WhereClauseOperatorEnum.UNARY_BOOLEAN);
				havingClause.setLeft(token.toString());
				myState = new StateAfterWhere(myWhereMode);
			}
		}
	}

	private class StateInWhereAfterLeft extends BaseState {
		private final HfqlStatement.HavingClause myHavingClause;
		private final WhereModeEnum myWhereMode;

		public StateInWhereAfterLeft(WhereModeEnum theWhereMode, HfqlStatement.HavingClause theHavingClause) {
			myWhereMode = theWhereMode;
			myHavingClause = theHavingClause;
		}

		@Override
		void consume(HfqlLexerToken theToken) {
			if ("=".equals(theToken.getToken())) {
				myHavingClause.setOperator(HfqlStatement.WhereClauseOperatorEnum.EQUALS);
				myState = new StateInWhereAfterOperatorEquals(myWhereMode, myHavingClause);
			} else if ("IN".equals(theToken.asKeyword())) {
				HfqlLexerToken nextToken = getNextTokenRequired(getLexerOptions());
				if (!nextToken.getToken().equals("(")) {
					throw newExceptionUnexpectedTokenExpectToken(theToken, "(");
				}
				myHavingClause.setOperator(HfqlStatement.WhereClauseOperatorEnum.IN);
				myState = new StateInWhereAfterOperatorIn(myWhereMode, myHavingClause);
			} else {
				throw newExceptionUnexpectedTokenExpectToken(theToken, "=");
			}
		}
	}

	private class StateInWhereAfterOperatorEquals extends BaseState {
		private final HfqlStatement.HavingClause myHavingClause;
		private final WhereModeEnum myWhereMode;

		public StateInWhereAfterOperatorEquals(WhereModeEnum theWhereMode, HfqlStatement.HavingClause theHavingClause) {
			myWhereMode = theWhereMode;
			myHavingClause = theHavingClause;
		}

		@Override
		void consume(HfqlLexerToken theToken) {
			String token = theToken.getToken();
			String keyword = theToken.asKeyword();
			if (KEYWORD_TRUE.equals(keyword) || KEYWORD_FALSE.equals(keyword)) {
				token = keyword.toLowerCase(Locale.US);
			} else if (!theToken.isQuotedString()) {
				throw newExceptionUnexpectedTokenExpectDescription(theToken, "quoted string");
			}
			myHavingClause.addRight(token);
			myState = new StateAfterWhere(myWhereMode);
		}
	}

	private class StateInWhereAfterOperatorIn extends BaseState {
		private final HfqlStatement.HavingClause myHavingClause;
		private final WhereModeEnum myWhereMode;

		public StateInWhereAfterOperatorIn(WhereModeEnum theWhereMode, HfqlStatement.HavingClause theHavingClause) {
			myWhereMode = theWhereMode;
			myHavingClause = theHavingClause;
		}

		@Override
		void consume(HfqlLexerToken theToken) {
			myHavingClause.addRight(theToken.getToken());

			if (myLexer.peekNextToken(getLexerOptions()) != null) {
				if (myLexer.peekNextToken(getLexerOptions()).getToken().equals("|")) {
					myLexer.consumeNextToken();
					return;
				} else if (myLexer.peekNextToken(getLexerOptions()).getToken().equals(",")) {
					myLexer.consumeNextToken();
					return;
				} else if (myLexer.peekNextToken(getLexerOptions()).getToken().equals(")")) {
					myLexer.consumeNextToken();
					myState = new StateAfterWhere(myWhereMode);
					return;
				}
			}

			throw newExceptionUnexpectedToken(myLexer.peekNextToken(getLexerOptions()));
		}
	}

	private class StateAfterWhere extends BaseRootState {
		private final WhereModeEnum myWhereMode;

		private StateAfterWhere(WhereModeEnum theWhereMode) {
			myWhereMode = theWhereMode;
		}

		@Override
		void consume(HfqlLexerToken theToken) {
			String keyword = theToken.asKeyword();
			if (keyword.equals(KEYWORD_AND)) {
				myState = new StateInWhereInitial(myWhereMode);
			} else {
				super.consume(theToken);
			}
		}
	}

	private class LimitState extends BaseState {
		@Override
		void consume(HfqlLexerToken theToken) {
			try {
				myStatement.setLimit(theToken.asInteger());
			} catch (NumberFormatException e) {
				throw newExceptionUnexpectedTokenExpectDescription(theToken, "integer value");
			}
		}
	}

	private abstract class BaseRootState extends BaseState {

		@Override
		void consume(HfqlLexerToken theToken) {
			String keyword = theToken.asKeyword();
			switch (keyword) {
					/*
					 * Update DIRECTIVE_KEYWORDS if you add new
					 * keywords here!
					 */
				case KEYWORD_HAVING:
					validateNotPresent(myStatement.getHavingClauses(), theToken);
					myState = new StateInWhereInitial(WhereModeEnum.HAVING);
					break;
				case KEYWORD_WHERE:
					validateNotPresent(myStatement.getWhereClauses(), theToken);
					myState = new StateInWhereInitial(WhereModeEnum.WHERE);
					break;
				case KEYWORD_SELECT:
					validateNotPresent(myStatement.getSelectClauses(), theToken);
					myState = new StateInSelect();
					break;
				case KEYWORD_FROM:
					validateNotPresent(myStatement.getFromResourceName(), theToken);
					myState = new StateFromStart();
					break;
				case KEYWORD_LIMIT:
					validateNotPresent(myStatement.getLimit(), theToken);
					myState = new LimitState();
					break;
				case KEYWORD_GROUP:
					validateNotPresent(myStatement.getGroupByClauses(), theToken);
					myState = new StateGroup();
					break;
				case KEYWORD_ORDER:
					validateNotPresent(myStatement.getOrderByClauses(), theToken);
					myState = new OrderState();
					break;
				default:
					if (myStatement.getWhereClauses().isEmpty()) {
						throw newExceptionUnexpectedTokenExpectToken(theToken, KEYWORD_SELECT);
					} else {
						throw newExceptionUnexpectedToken(theToken);
					}
			}
		}
	}

	private class StateGroup extends BaseState {
		@Override
		void consume(HfqlLexerToken theToken) {
			if (!"BY".equals(theToken.asKeyword())) {
				throw newExceptionUnexpectedTokenExpectToken(theToken, "BY");
			}
			myState = new StateGroupBy();
		}
	}

	private class StateGroupBy extends BaseState {
		@Override
		void consume(HfqlLexerToken theToken) {
			myStatement.addGroupByClause(theToken.asString());

			if (myLexer.hasNextToken(HfqlLexerOptions.HFQL_TOKEN)
					&& ","
							.equals(myLexer.peekNextToken(HfqlLexerOptions.HFQL_TOKEN)
									.getToken())) {
				myLexer.consumeNextToken();
			} else {
				myState = new StateAfterGroupBy();
			}
		}
	}

	private class StateAfterGroupBy extends BaseRootState {
		// nothing
	}

	private class OrderState extends BaseState {
		@Override
		void consume(HfqlLexerToken theToken) {
			if (!"BY".equals(theToken.asKeyword())) {
				throw newExceptionUnexpectedTokenExpectToken(theToken, "BY");
			}

			myState = new OrderByState();
		}
	}

	private class OrderByState extends BaseState {

		@Nonnull
		@Override
		public HfqlLexerOptions getLexerOptions() {
			return HfqlLexerOptions.FHIRPATH_EXPRESSION;
		}

		@Override
		void consume(HfqlLexerToken theToken) {
			HfqlStatement.OrderByClause clause = myStatement.addOrderByClause(theToken.getToken(), true);
			myState = new OrderByAfterState(clause);
		}
	}

	private class OrderByAfterState extends BaseRootState {
		private final HfqlStatement.OrderByClause myClause;

		public OrderByAfterState(HfqlStatement.OrderByClause theClause) {
			myClause = theClause;
		}

		@Override
		void consume(HfqlLexerToken theToken) {
			if ("ASC".equals(theToken.asKeyword())) {
				myClause.setAscending(true);
			} else if ("DESC".equals(theToken.asKeyword())) {
				myClause.setAscending(false);
			} else if (",".equals(theToken.getToken())) {
				myState = new OrderByState();
			} else {
				super.consume(theToken);
			}
		}
	}

	private abstract static class BaseState {
		abstract void consume(HfqlLexerToken theToken);

		@Nonnull
		public HfqlLexerOptions getLexerOptions() {
			return HfqlLexerOptions.HFQL_TOKEN;
		}
	}
}
