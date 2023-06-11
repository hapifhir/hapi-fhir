package ca.uhn.fhir.jpa.fql.parser;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class FqlStatement {

	private final List<SelectClause> mySelectClauses = new ArrayList<>();
	private final List<WhereClause> myWhereClauses = new ArrayList<>();
	private final List<WhereClause> mySearchClauses = new ArrayList<>();
	private String myFromResourceName;
	private Integer myLimit;

	public List<SelectClause> getSelectClauses() {
		return mySelectClauses;
	}

	public String getFromResourceName() {
		return myFromResourceName;
	}

	public void setFromResourceName(String theFromResourceName) {
		myFromResourceName = theFromResourceName;
	}

	public SelectClause addSelectClause(String theClause) {
		SelectClause clause = new SelectClause();
		clause.setClause(theClause);
		clause.setAlias(theClause);
		mySelectClauses.add(clause);
		return clause;
	}

	public WhereClause addWhereClause() {
		WhereClause clause = new WhereClause();
		myWhereClauses.add(clause);
		return clause;
	}

	public List<WhereClause> getWhereClauses() {
		return myWhereClauses;
	}

	public WhereClause addSearchClause() {
		WhereClause clause = new WhereClause();
		mySearchClauses.add(clause);
		return clause;
	}

	public List<WhereClause> getSearchClauses() {
		return mySearchClauses;
	}

	@Nullable
	public Integer getLimit() {
		return myLimit;
	}

	public void setLimit(Integer theLimit) {
		myLimit = theLimit;
	}

	public enum WhereClauseOperator {
		EQUALS,
		IN
	}

	public static class SelectClause {
		private String myClause;
		private String myAlias;

		public String getAlias() {
			return myAlias;
		}

		public void setAlias(String theAlias) {
			myAlias = theAlias;
		}

		public String getClause() {
			return myClause;
		}

		public void setClause(String theClause) {
			myClause = theClause;
		}
	}

	public static class WhereClause {

		private final List<String> myRight = new ArrayList<>();
		private String myLeft;
		private WhereClauseOperator myOperator;

		public WhereClauseOperator getOperator() {
			return myOperator;
		}

		public void setOperator(WhereClauseOperator theOperator) {
			myOperator = theOperator;
		}

		public String getLeft() {
			return myLeft;
		}

		public void setLeft(String theLeft) {
			myLeft = theLeft;
		}

		public List<String> getRight() {
			return myRight;
		}

		public void addRight(String theRight) {
			myRight.add(theRight);
		}

	}

}
