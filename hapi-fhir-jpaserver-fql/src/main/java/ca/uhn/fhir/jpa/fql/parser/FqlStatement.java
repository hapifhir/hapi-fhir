package ca.uhn.fhir.jpa.fql.parser;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class FqlStatement implements IModelJson {

	@JsonProperty("selectClauses")
	private List<SelectClause> mySelectClauses = new ArrayList<>();
	@JsonProperty("whereClauses")
	private List<WhereClause> myWhereClauses = new ArrayList<>();
	@JsonProperty("searchClauses")
	private List<WhereClause> mySearchClauses = new ArrayList<>();
	@JsonProperty("fromResourceName")
	private String myFromResourceName;
	@JsonProperty("limit")
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

	public static class SelectClause implements IModelJson {
		@JsonProperty("clause")
		private String myClause;
		@JsonProperty("alias")
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

	public static class WhereClause implements IModelJson{

		@JsonProperty("left")
		private String myLeft;
		@JsonProperty("operator")
		private WhereClauseOperator myOperator;
		@JsonProperty("right")
		private List<String> myRight = new ArrayList<>();

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
