/*-
 * #%L
 * HAPI FHIR JPA Server - Firely Query Language
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

public class HfqlStatementParser {

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
			throw newExceptionUnexpectedTokenExpectToken(null, "FROM");
		}

		if (myStatement.getSelectClauses().isEmpty()) {
			throw newExceptionUnexpectedTokenExpectToken(null, "SELECT");
		}

		return myStatement;

	}

	@Nonnull
	private HfqlLexerToken getNextTokenRequired(@Nonnull HfqlLexerOptions theOptions) {
		if (!myLexer.hasNextToken(theOptions)) {
			throw newExceptionUnexpectedToken(null);
		}
		return myLexer.getNextToken();
	}

	@Nonnull
	private static DataFormatException newExceptionUnexpectedToken(@Nullable HfqlLexerToken theToken) {
		return newExceptionUnexpectedTokenExpectDescription(theToken, null);
	}

	@Nonnull
	private static DataFormatException newExceptionUnexpectedTokenExpectToken(@Nullable HfqlLexerToken theToken, @Nonnull String theExpectedToken) {
		return newExceptionUnexpectedTokenExpectDescription(theToken, "\"" + theExpectedToken + "\"");
	}

	@Nonnull
	private static DataFormatException newExceptionUnexpectedTokenExpectDescription(@Nullable HfqlLexerToken theToken, @Nullable String theExpectedDescription) {
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
		return new DataFormatException("Invalid FROM statement. Unknown resource type '" + resourceType + "' at position: " + theToken.describePosition());
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
		WHERE,
		SEARCH
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

	private class StateInSelectAfterClause extends BaseRootState {
		private final HfqlStatement.SelectClause mySelectClause;

		public StateInSelectAfterClause(HfqlStatement.SelectClause theSelectClause) {
			mySelectClause = theSelectClause;
		}

		@Override
		void consume(HfqlLexerToken theToken) {
			if (theToken.getToken().equals(":")) {
				HfqlLexerToken nextToken = getNextTokenRequired(getLexerOptions());
				String clause = nextToken.asString();
				String alias = mySelectClause.getClause();
				mySelectClause.setAlias(alias);
				mySelectClause.setClause(clause);
			} else if (theToken.getToken().equals(",")) {
				myState = new StateInSelect();
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
			if (myWhereMode == WhereModeEnum.WHERE) {
				return HfqlLexerOptions.FHIRPATH_EXPRESSION;
			} else {
				return HfqlLexerOptions.SEARCH_PARAMETER_NAME;
			}
		}

		@Override
		void consume(HfqlLexerToken theToken) {
			HfqlStatement.WhereClause whereClause;
			if (myWhereMode == WhereModeEnum.SEARCH) {
				whereClause = myStatement.addSearchClause();
			} else {
				whereClause = myStatement.addWhereClause();
			}
			String token = theToken.getToken();
			whereClause.setLeft(token);
			myState = new StateInWhereAfterLeft(myWhereMode, whereClause);
		}
	}

	private class StateInWhereAfterLeft extends BaseState {
		private final HfqlStatement.WhereClause myWhereClause;
		private final WhereModeEnum myWhereMode;

		public StateInWhereAfterLeft(WhereModeEnum theWhereMode, HfqlStatement.WhereClause theWhereClause) {
			myWhereMode = theWhereMode;
			myWhereClause = theWhereClause;
		}

		@Override
		void consume(HfqlLexerToken theToken) {
			if ("=".equals(theToken.getToken())) {
				myWhereClause.setOperator(HfqlStatement.WhereClauseOperatorEnum.EQUALS);
				myState = new StateInWhereAfterOperatorEquals(myWhereMode, myWhereClause);
			} else if ("IN".equals(theToken.asKeyword())) {
				HfqlLexerToken nextToken = getNextTokenRequired(getLexerOptions());
				if (!nextToken.getToken().equals("(")) {
					throw newExceptionUnexpectedTokenExpectToken(theToken, "(");
				}
				myWhereClause.setOperator(HfqlStatement.WhereClauseOperatorEnum.IN);
				myState = new StateInWhereAfterOperatorIn(myWhereMode, myWhereClause);
			} else {
				throw newExceptionUnexpectedTokenExpectToken(theToken, "=");
			}
		}

	}

	private class StateInWhereAfterOperatorEquals extends BaseState {
		private final HfqlStatement.WhereClause myWhereClause;
		private final WhereModeEnum myWhereMode;

		public StateInWhereAfterOperatorEquals(WhereModeEnum theWhereMode, HfqlStatement.WhereClause theWhereClause) {
			myWhereMode = theWhereMode;
			myWhereClause = theWhereClause;
		}

		@Override
		void consume(HfqlLexerToken theToken) {
			String token = theToken.getToken();
			String keyword = theToken.asKeyword();
			if ("TRUE".equals(keyword) || "FALSE".equals(keyword)) {
				token = keyword.toLowerCase(Locale.US);
			} else if (!theToken.isQuotedString()) {
				throw newExceptionUnexpectedTokenExpectDescription(theToken, "quoted string");
			}
			myWhereClause.addRight(token);
			myState = new StateAfterWhere(myWhereMode);
		}

	}

	private class StateInWhereAfterOperatorIn extends BaseState {
		private final HfqlStatement.WhereClause myWhereClause;
		private final WhereModeEnum myWhereMode;

		public StateInWhereAfterOperatorIn(WhereModeEnum theWhereMode, HfqlStatement.WhereClause theWhereClause) {
			myWhereMode = theWhereMode;
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
			if (keyword.equals("AND")) {
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

	private class BaseRootState extends BaseState {

		@Override
		void consume(HfqlLexerToken theToken) {
			String keyword = theToken.asKeyword();
			switch (keyword) {
				case "WHERE":
					validateNotPresent(myStatement.getWhereClauses(), theToken);
					myState = new StateInWhereInitial(WhereModeEnum.WHERE);
					break;
				case "SEARCH":
					validateNotPresent(myStatement.getSearchClauses(), theToken);
					myState = new StateInWhereInitial(WhereModeEnum.SEARCH);
					break;
				case "SELECT":
					validateNotPresent(myStatement.getSelectClauses(), theToken);
					myState = new StateInSelect();
					break;
				case "FROM":
					validateNotPresent(myStatement.getFromResourceName(), theToken);
					myState = new StateFromStart();
					break;
				case "LIMIT":
					validateNotPresent(myStatement.getLimit(), theToken);
					myState = new LimitState();
					break;
				case "GROUP":
					validateNotPresent(myStatement.getGroupByClauses(), theToken);
					myState = new StateGroup();
					break;
				default:
					throw newExceptionUnexpectedTokenExpectToken(theToken, "SELECT");
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

			if (myLexer.hasNextToken(HfqlLexerOptions.DEFAULT) &&
				",".equals(myLexer.peekNextToken(HfqlLexerOptions.DEFAULT).getToken())) {
				myLexer.consumeNextToken();
			} else {
				myState = new StateAfterGroupBy();
			}
		}
	}

	private class StateAfterGroupBy extends BaseRootState {
		// nothing
	}

	private abstract static class BaseState {
		abstract void consume(HfqlLexerToken theToken);

		@Nonnull
		public HfqlLexerOptions getLexerOptions() {
			return HfqlLexerOptions.DEFAULT;
		}
	}

}
