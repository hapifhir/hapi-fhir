/*-
 * #%L
 * HAPI FHIR JPA Server - HFQL Driver
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.util.UrlUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.Validate;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class HfqlStatementParser {

	public static final String KEYWORD_AND = "AND";
	public static final String KEYWORD_WHERE = "WHERE";
	public static final String KEYWORD_SELECT = "SELECT";
	public static final String KEYWORD_FROM = "FROM";
	public static final String KEYWORD_LIMIT = "LIMIT";
	public static final String KEYWORD_GROUP = "GROUP";
	public static final String KEYWORD_ORDER = "ORDER";
	public static final String KEYWORD_TRUE = "TRUE";
	public static final String KEYWORD_FALSE = "FALSE";
	private static final Set<String> DIRECTIVE_KEYWORDS =
			Set.of(KEYWORD_FROM, KEYWORD_GROUP, KEYWORD_LIMIT, KEYWORD_ORDER, KEYWORD_WHERE, KEYWORD_SELECT);
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

		if (isBlank(myStatement.getFromResourceName())) {
			throw newExceptionUnexpectedTokenExpectToken(null, KEYWORD_FROM);
		}

		if (myStatement.getSelectClauses().isEmpty()) {
			throw newExceptionUnexpectedTokenExpectToken(null, KEYWORD_SELECT);
		}

		Set<String> existingAliases = new HashSet<>();
		for (HfqlStatement.SelectClause next : myStatement.getSelectClauses()) {
			if (isNotBlank(next.getAlias())) {
				if (!existingAliases.add(next.getAlias())) {
					throw new DataFormatException(Msg.code(2414) + "Duplicate SELECT column alias: "
							+ UrlUtil.sanitizeUrlPart(next.getAlias()));
				}
			}
		}
		for (HfqlStatement.SelectClause next : myStatement.getSelectClauses()) {
			if (isBlank(next.getAlias())) {
				String candidateAlias = next.getClause();
				int nextSuffix = 2;
				while (existingAliases.contains(candidateAlias)) {
					candidateAlias = next.getClause() + nextSuffix;
					nextSuffix++;
				}
				existingAliases.add(candidateAlias);
				next.setAlias(candidateAlias);
			}
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
		@Nonnull
		@Override
		public HfqlLexerOptions getLexerOptions() {
			return HfqlLexerOptions.FHIRPATH_EXPRESSION;
		}

		@Override
		void consume(HfqlLexerToken theToken) {
			HfqlStatement.WhereClause whereClause = myStatement.addWhereClause();
			String token = theToken.getToken();
			whereClause.setLeft(token);
			whereClause.setOperator(HfqlStatement.WhereClauseOperatorEnum.UNARY_BOOLEAN);
			myState = new StateInWhereAfterLeft(whereClause);
		}
	}

	private class StateInWhereAfterLeft extends BaseRootState {
		private final HfqlStatement.WhereClause myWhereClause;

		public StateInWhereAfterLeft(HfqlStatement.WhereClause theWhereClause) {
			myWhereClause = theWhereClause;
		}

		@Nonnull
		@Override
		public HfqlLexerOptions getLexerOptions() {
			return HfqlLexerOptions.FHIRPATH_EXPRESSION;
		}

		@Override
		void consume(HfqlLexerToken theToken) {
			if ("=".equals(theToken.getToken())) {
				myWhereClause.setOperator(HfqlStatement.WhereClauseOperatorEnum.EQUALS);
				myState = new StateInWhereAfterOperatorEquals(myWhereClause);
			} else if ("IN".equals(theToken.asKeyword())) {
				HfqlLexerToken nextToken = getNextTokenRequired(HfqlLexerOptions.HFQL_TOKEN);
				switch (nextToken.asKeyword()) {
					case "(":
						myWhereClause.setOperator(HfqlStatement.WhereClauseOperatorEnum.IN);
						myState = new StateInWhereAfterOperatorIn(myWhereClause);
						return;
					case "SEARCH_MATCH":
						myWhereClause.setOperator(HfqlStatement.WhereClauseOperatorEnum.SEARCH_MATCH);
						HfqlLexerToken argumentsToken = getNextTokenRequired(HfqlLexerOptions.HFQL_TOKEN);
						String token = argumentsToken.getToken();
						if (!token.equals("(")) {
							throw newExceptionUnexpectedTokenExpectToken(theToken, "(");
						}
						myState = new StateInWhereSearchMatch(myWhereClause);
						return;
				}
				throw newExceptionUnexpectedTokenExpectToken(theToken, "(");
			} else {
				myWhereClause.setOperator(HfqlStatement.WhereClauseOperatorEnum.UNARY_BOOLEAN);

				HfqlLexerToken nextToken = theToken;
				if (!KEYWORD_AND.equals(nextToken.asKeyword()) && !DIRECTIVE_KEYWORDS.contains(nextToken.asKeyword())) {
					myWhereClause.addRight(nextToken.getToken());

					while (true) {
						if (myLexer.hasNextToken(HfqlLexerOptions.FHIRPATH_EXPRESSION)) {
							nextToken = myLexer.getNextToken(HfqlLexerOptions.FHIRPATH_EXPRESSION);
							String nextTokenAsKeyword = nextToken.asKeyword();
							if (KEYWORD_AND.equals(nextTokenAsKeyword)
									|| DIRECTIVE_KEYWORDS.contains(nextTokenAsKeyword)) {
								break;
							}
							myWhereClause.addRight(nextToken.getToken());
						} else {
							nextToken = null;
							break;
						}
					}
				}

				if (nextToken != null) {
					super.consume(nextToken);
				}
			}
		}
	}

	private class StateInWhereAfterOperatorEquals extends BaseState {
		private final HfqlStatement.WhereClause myWhereClause;

		public StateInWhereAfterOperatorEquals(HfqlStatement.WhereClause theWhereClause) {
			myWhereClause = theWhereClause;
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
			myWhereClause.addRight(token);
			myState = new StateAfterWhere();
		}
	}

	private class StateInWhereAfterOperatorIn extends BaseState {
		private final HfqlStatement.WhereClause myWhereClause;

		public StateInWhereAfterOperatorIn(HfqlStatement.WhereClause theWhereClause) {
			myWhereClause = theWhereClause;
		}

		@Override
		void consume(HfqlLexerToken theToken) {
			myWhereClause.addRight(theToken.getToken());

			if (myLexer.peekNextToken(getLexerOptions()) != null) {
				if (myLexer.peekNextToken(getLexerOptions()).getToken().equals("|")) {
					myLexer.consumeNextToken();
					return;
				} else if (myLexer.peekNextToken(getLexerOptions()).getToken().equals(",")) {
					myLexer.consumeNextToken();
					return;
				} else if (myLexer.peekNextToken(getLexerOptions()).getToken().equals(")")) {
					myLexer.consumeNextToken();
					myState = new StateAfterWhere();
					return;
				}
			}

			throw newExceptionUnexpectedToken(myLexer.peekNextToken(getLexerOptions()));
		}
	}

	private class StateInWhereSearchMatch extends BaseState {

		private final HfqlStatement.WhereClause myWhereClause;

		public StateInWhereSearchMatch(HfqlStatement.WhereClause theWhereClause) {
			myWhereClause = theWhereClause;
		}

		@Override
		void consume(HfqlLexerToken theToken) {
			if (")".equals(theToken.getToken())) {
				myState = new StateAfterWhere();
			} else {
				myWhereClause.addRight(theToken.getToken());
				HfqlLexerToken nextToken = getNextTokenRequired(getLexerOptions());
				if (")".equals(nextToken.getToken())) {
					myState = new StateAfterWhere();
				} else if (!",".equals(nextToken.getToken())) {
					throw newExceptionUnexpectedTokenExpectToken(nextToken, ",");
				}
			}
		}
	}

	private class StateAfterWhere extends BaseRootState {

		@Override
		void consume(HfqlLexerToken theToken) {
			String keyword = theToken.asKeyword();
			if (keyword.equals(KEYWORD_AND)) {
				myState = new StateInWhereInitial();
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
				case KEYWORD_WHERE:
					validateNotPresent(myStatement.getWhereClauses(), theToken);
					myState = new StateInWhereInitial();
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
