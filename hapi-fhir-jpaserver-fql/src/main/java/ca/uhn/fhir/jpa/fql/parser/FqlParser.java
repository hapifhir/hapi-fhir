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

public class FqlParser {

	private final FqlLexer myLexer;
	private final FhirContext myFhirContext;
	private BaseState myState;

	private FqlStatement myStatement;

	public FqlParser(FhirContext theFhirContext, String theInput) {
		myFhirContext = theFhirContext;
		myLexer = new FqlLexer(theInput);
		myState = new InitialState();
	}

	/**
	 * This method may only be called once for a given instance
	 */
	public FqlStatement parse() {
		Validate.isTrue(myStatement == null, "Already completed parsing");
		myStatement = new FqlStatement();

		while (myLexer.hasNextToken(myState.getLexerOptions())) {
			FqlLexerToken nextToken = myLexer.getNextToken(myState.getLexerOptions());
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
	private FqlLexerToken getNextTokenRequired(@Nonnull FqlLexerOptions theOptions) {
		if (!myLexer.hasNextToken(theOptions)) {
			throw newExceptionUnexpectedToken(null);
		}
		return myLexer.getNextToken();
	}

	@Nonnull
	private static DataFormatException newExceptionUnexpectedToken(@Nullable FqlLexerToken theToken) {
		return newExceptionUnexpectedTokenExpectDescription(theToken, null);
	}

	@Nonnull
	private static DataFormatException newExceptionUnexpectedTokenExpectToken(@Nullable FqlLexerToken theToken, @Nonnull String theExpectedToken) {
		return newExceptionUnexpectedTokenExpectDescription(theToken, "\"" + theExpectedToken + "\"");
	}

	@Nonnull
	private static DataFormatException newExceptionUnexpectedTokenExpectDescription(@Nullable FqlLexerToken theToken, @Nullable String theExpectedDescription) {
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
	private static DataFormatException newExceptionUnknownResourceType(FqlLexerToken theToken, String resourceType) {
		return new DataFormatException("Invalid FROM statement. Unknown resource type '" + resourceType + "' at position: " + theToken.describePosition());
	}

	private static void validateNotPresent(List<?> theClauses, FqlLexerToken theKeyword) {
		if (!theClauses.isEmpty()) {
			throw newExceptionUnexpectedToken(theKeyword);
		}
	}

	private static void validateNotPresent(Object theValue, FqlLexerToken theKeyword) {
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
		void consume(FqlLexerToken theToken) {
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
		public FqlLexerOptions getLexerOptions() {
			return FqlLexerOptions.FHIRPATH_EXPRESSION;
		}

		@Override
		void consume(FqlLexerToken theToken) {
			String string = theToken.asString();
			FqlStatement.SelectClause clause = myStatement.addSelectClause(string);
			myState = new StateInSelectAfterClause(clause);
		}
	}

	private class StateInSelectAfterClause extends BaseRootState {
		private final FqlStatement.SelectClause mySelectClause;

		public StateInSelectAfterClause(FqlStatement.SelectClause theSelectClause) {
			mySelectClause = theSelectClause;
		}

		@Override
		void consume(FqlLexerToken theToken) {
			if (theToken.getToken().equals(":")) {
				FqlLexerToken nextToken = getNextTokenRequired(getLexerOptions());
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
		public FqlLexerOptions getLexerOptions() {
			if (myWhereMode == WhereModeEnum.WHERE) {
				return FqlLexerOptions.FHIRPATH_EXPRESSION;
			} else {
				return FqlLexerOptions.SEARCH_PARAMETER_NAME;
			}
		}

		@Override
		void consume(FqlLexerToken theToken) {
			FqlStatement.WhereClause whereClause;
			String token = theToken.getToken();
			if (token.startsWith("__") && myWhereMode == WhereModeEnum.WHERE) {
				whereClause = myStatement.addSearchClause();
				token = token.substring(2);
			} else if (myWhereMode == WhereModeEnum.SEARCH) {
				whereClause = myStatement.addSearchClause();
			} else {
				whereClause = myStatement.addWhereClause();
			}
			whereClause.setLeft(token);
			myState = new StateInWhereAfterLeft(myWhereMode, whereClause);
		}
	}

	private class StateInWhereAfterLeft extends BaseState {
		private final FqlStatement.WhereClause myWhereClause;
		private final WhereModeEnum myWhereMode;

		public StateInWhereAfterLeft(WhereModeEnum theWhereMode, FqlStatement.WhereClause theWhereClause) {
			myWhereMode = theWhereMode;
			myWhereClause = theWhereClause;
		}

		@Override
		void consume(FqlLexerToken theToken) {
			if ("=".equals(theToken.getToken())) {
				myWhereClause.setOperator(FqlStatement.WhereClauseOperator.EQUALS);
				myState = new StateInWhereAfterOperatorEquals(myWhereMode, myWhereClause);
			} else if ("IN".equals(theToken.asKeyword())) {
				FqlLexerToken nextToken = getNextTokenRequired(getLexerOptions());
				if (!nextToken.getToken().equals("(")) {
					throw newExceptionUnexpectedTokenExpectToken(theToken, "(");
				}
				myWhereClause.setOperator(FqlStatement.WhereClauseOperator.IN);
				myState = new StateInWhereAfterOperatorIn(myWhereMode, myWhereClause);
			} else {
				throw newExceptionUnexpectedTokenExpectToken(theToken, "=");
			}
		}

	}

	private class StateInWhereAfterOperatorEquals extends BaseState {
		private final FqlStatement.WhereClause myWhereClause;
		private final WhereModeEnum myWhereMode;

		public StateInWhereAfterOperatorEquals(WhereModeEnum theWhereMode, FqlStatement.WhereClause theWhereClause) {
			myWhereMode = theWhereMode;
			myWhereClause = theWhereClause;
		}

		@Override
		void consume(FqlLexerToken theToken) {
			if (!theToken.isQuotedString()) {
				throw newExceptionUnexpectedTokenExpectDescription(theToken, "quoted string");
			}
			myWhereClause.addRight(theToken.getToken());
			myState = new StateAfterWhere(myWhereMode);
		}

	}

	private class StateInWhereAfterOperatorIn extends BaseState {
		private final FqlStatement.WhereClause myWhereClause;
		private final WhereModeEnum myWhereMode;

		public StateInWhereAfterOperatorIn(WhereModeEnum theWhereMode, FqlStatement.WhereClause theWhereClause) {
			myWhereMode = theWhereMode;
			myWhereClause = theWhereClause;
		}

		@Override
		void consume(FqlLexerToken theToken) {
			myWhereClause.addRight(theToken.getToken());

			if (myLexer.peekNextToken(getLexerOptions()) != null) {
				if (myLexer.peekNextToken(getLexerOptions()).getToken().equals("|")) {
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
		void consume(FqlLexerToken theToken) {
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
		void consume(FqlLexerToken theToken) {
			try {
				myStatement.setLimit(theToken.asInteger());
			} catch (NumberFormatException e) {
				throw newExceptionUnexpectedTokenExpectDescription(theToken, "integer value");
			}
		}
	}

	private class BaseRootState extends BaseState {

		@Override
		void consume(FqlLexerToken theToken) {
			String keyword = theToken.asKeyword();
			if (keyword.equals("WHERE")) {
				validateNotPresent(myStatement.getWhereClauses(), theToken);
				myState = new StateInWhereInitial(WhereModeEnum.WHERE);
			} else if (keyword.equals("SEARCH")) {
				validateNotPresent(myStatement.getSearchClauses(), theToken);
				myState = new StateInWhereInitial(WhereModeEnum.SEARCH);
			} else if (keyword.equals("SELECT")) {
				validateNotPresent(myStatement.getSelectClauses(), theToken);
				myState = new StateInSelect();
			} else if (keyword.equals("FROM")) {
				validateNotPresent(myStatement.getFromResourceName(), theToken);
				myState = new StateFromStart();
			} else if (theToken.asKeyword().equals("LIMIT")) {
				validateNotPresent(myStatement.getLimit(), theToken);
				myState = new LimitState();
			} else {
				throw newExceptionUnexpectedTokenExpectToken(theToken, "SELECT");
			}
		}

	}

	private abstract static class BaseState {
		abstract void consume(FqlLexerToken theToken);

		@Nonnull
		public FqlLexerOptions getLexerOptions() {
			return FqlLexerOptions.DEFAULT;
		}
	}

}
