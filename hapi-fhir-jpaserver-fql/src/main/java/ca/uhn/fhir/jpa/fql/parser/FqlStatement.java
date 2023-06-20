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

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FqlStatement implements IModelJson {

	@JsonProperty("selectClauses")
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private List<SelectClause> mySelectClauses = new ArrayList<>();
	@JsonProperty("whereClauses")
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private List<WhereClause> myWhereClauses = new ArrayList<>();
	@JsonProperty("searchClauses")
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
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
		clause.setOperator(SelectClauseOperator.SELECT);
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

	public void addWhereClause(String theLeft, WhereClauseOperatorEnum theOperator, String theRight) {
		WhereClause clause = addWhereClause();
		clause.setLeft(theLeft);
		clause.setOperator(theOperator);
		clause.addRight(theRight);
	}

	public enum WhereClauseOperatorEnum {
		EQUALS,
		IN
	}

	public enum SelectClauseOperator {

		SELECT,
		COUNT

	}

	public static class SelectClause implements IModelJson {
		@JsonProperty("clause")
		private String myClause;
		@JsonProperty("alias")
		private String myAlias;
		@JsonProperty("operator")
		private SelectClauseOperator myOperator;

		/**
		 * Constructor
		 */
		public SelectClause() {
			// nothing
		}

		/**
		 * Constructor
		 *
		 * @param theClause The clause (will be used as both the clause and the alias)
		 */
		public SelectClause(String theClause) {
			setOperator(SelectClauseOperator.SELECT);
			setClause(theClause);
			setAlias(theClause);
		}

		public SelectClauseOperator getOperator() {
			return myOperator;
		}

		public void setOperator(SelectClauseOperator theOperator) {
			myOperator = theOperator;
		}

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

	public static class WhereClause implements IModelJson {

		@JsonProperty("left")
		private String myLeft;
		@JsonProperty("operator")
		private WhereClauseOperatorEnum myOperator;
		@JsonProperty("right")
		private List<String> myRight = new ArrayList<>();

		public WhereClauseOperatorEnum getOperator() {
			return myOperator;
		}

		public void setOperator(WhereClauseOperatorEnum theOperator) {
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

		/**
		 * Returns the {@link #getRight() right} values as raw strings. That
		 * means that any surrounding quote marks are stripped.
		 */
		public Collection<String> getRightAsStrings() {
			ArrayList<String> retVal = new ArrayList<>();
			for (String next : getRight()) {
				if (next.startsWith("'")) {
					next = next.substring(1, next.length() - 1);
				}
				retVal.add(next);
			}
			return retVal;
		}

	}


}
