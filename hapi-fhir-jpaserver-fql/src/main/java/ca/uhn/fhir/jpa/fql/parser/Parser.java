package ca.uhn.fhir.jpa.fql.parser;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.DataFormatException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

class Parser {

	private final Lexer myLexer;
	private final FhirContext myFhirContext;
	private BaseState myState;

	private FqlStatement myStatement;

	public Parser(FhirContext theFhirContext, String theInput) {
		myFhirContext = theFhirContext;
		myLexer = new Lexer(theInput);
		myState = new InitialState();
	}

	/**
	 * This method may only be called once for a given instance
	 */
	public FqlStatement parse() {
		Validate.isTrue(myStatement == null, "Already completed parsing");
		myStatement = new FqlStatement();

		while (myLexer.hasNextToken()) {
			Token nextToken = myLexer.getNextToken();
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
	private Token getNextTokenRequired() {
		if (!myLexer.hasNextToken()) {
			throw newExceptionUnexpectedToken(null);
		}
		return myLexer.getNextToken();
	}

	@Nonnull
	private static DataFormatException newExceptionUnexpectedToken(@Nullable Token theToken) {
		return newExceptionUnexpectedTokenExpectDescription(theToken, null);
	}

	@Nonnull
	private static DataFormatException newExceptionUnexpectedTokenExpectToken(@Nullable Token theToken, @Nonnull String theExpectedToken) {
		return newExceptionUnexpectedTokenExpectDescription(theToken, "\"" + theExpectedToken + "\"");
	}

	@Nonnull
	private static DataFormatException newExceptionUnexpectedTokenExpectDescription(@Nullable Token theToken, @Nullable String theExpectedDescription) {
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
	private static DataFormatException newExceptionUnknownResourceType(Token theToken, String resourceType) {
		return new DataFormatException("Invalid FROM statement. Unknown resource type '" + resourceType + "' at position: " + theToken.describePosition());
	}

	/**
	 * No tokens consumed yet
	 */
	public class InitialState extends BaseState {

		@Override
		void consume(Token theToken) {
			if (theToken.asKeyword().equals("FROM")) {
				myState = new StateFromStart();
			} else {
				throw newExceptionUnexpectedToken(theToken);
			}
		}
	}

	/**
	 * Have consumed a 'from' token but not a resource type yet
	 */
	public class StateFromStart extends BaseState {
		@Override
		void consume(Token theToken) {
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
	public class StateFromAfter extends BaseState {
		@Override
		void consume(Token theToken) {
			String keyword = theToken.asKeyword();
			if (keyword.equals("WHERE")) {
				myState = new StateInWhereInitial(WhereModeEnum.WHERE);
			} else if (keyword.equals("SEARCH")) {
				myState = new StateInWhereInitial(WhereModeEnum.SEARCH);
			} else if (keyword.equals("SELECT")) {
				myState = new StateInSelect();
			} else {
				throw newExceptionUnexpectedTokenExpectToken(theToken, "SELECT");
			}
		}
	}

	/**
	 * We're in the select statement
	 */
	public class StateInSelect extends BaseState {

		@Override
		void consume(Token theToken) {
			String string = theToken.asString();
			FqlStatement.SelectClause clause = myStatement.addSelectClause(string);
			myState = new StateInSelectAfterClause(clause);
		}
	}

	private class StateInSelectAfterClause extends BaseState {
		private final FqlStatement.SelectClause mySelectClause;

		public StateInSelectAfterClause(FqlStatement.SelectClause theSelectClause) {
			mySelectClause = theSelectClause;
		}

		@Override
		void consume(Token theToken) {
			if (theToken.getToken().equals(":")) {
				Token nextToken = getNextTokenRequired();
				String clause = nextToken.asString();
				String alias = mySelectClause.getClause();
				mySelectClause.setAlias(alias);
				mySelectClause.setClause(clause);
			} else if (theToken.getToken().equals(",")) {
				myState = new StateInSelect();
			} else {
				throw newExceptionUnexpectedToken(theToken);
			}
		}
	}

	private class StateInWhereInitial extends BaseState {

		private final WhereModeEnum myWhereMode;

		public StateInWhereInitial(WhereModeEnum theWhereMode) {
			myWhereMode = theWhereMode;
		}

		@Override
		void consume(Token theToken) {
			FqlStatement.WhereClause whereClause;
			if (myWhereMode == WhereModeEnum.WHERE) {
				whereClause = myStatement.addWhereClause();
			} else {
				whereClause = myStatement.addSearchClause();
			}
			whereClause.setLeft(theToken.getToken());
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
		void consume(Token theToken) {
			if ("=".equals(theToken.getToken())) {
				myWhereClause.setOperator(FqlStatement.WhereClauseOperator.EQUALS);
				myState = new StateInWhereAfterOperatorEquals(myWhereMode, myWhereClause);
			} else if ("IN".equals(theToken.asKeyword())) {
				Token nextToken = getNextTokenRequired();
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
		void consume(Token theToken) {
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
		void consume(Token theToken) {
			myWhereClause.addRight(theToken.getToken());

			if (myLexer.peekNextToken() != null) {
				if (myLexer.peekNextToken().getToken().equals("|")) {
					myLexer.consumeNextToken();
					return;
				} else if (myLexer.peekNextToken().getToken().equals(")")) {
					myLexer.consumeNextToken();
					myState = new StateAfterWhere(myWhereMode);
					return;
				}
			}

			throw newExceptionUnexpectedToken(myLexer.peekNextToken());
		}
	}

	private class StateAfterWhere extends BaseState {
		private final WhereModeEnum myWhereMode;

		private StateAfterWhere(WhereModeEnum theWhereMode) {
			myWhereMode = theWhereMode;
		}

		@Override
		void consume(Token theToken) {
			String keyword = theToken.asKeyword();
			if (keyword.equals("AND")) {
				myState = new StateInWhereInitial(myWhereMode);
			} else if (keyword.equals("SELECT")) {
				myState = new StateInSelect();
			}
		}
	}

	private abstract static class BaseState {
		abstract void consume(Token theToken);
	}


	private enum WhereModeEnum {
		WHERE,
		SEARCH
	}

}
