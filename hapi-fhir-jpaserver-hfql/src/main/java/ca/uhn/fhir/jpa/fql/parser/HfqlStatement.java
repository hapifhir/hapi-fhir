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

import ca.uhn.fhir.jpa.fql.executor.HfqlDataTypeEnum;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.util.ValidateUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.join;

/**
 * This class represents a parsed HFQL expression tree. It is useful for
 * passing over the wire, but it should not be considered a stable model (in
 * other words, don't persist these things long-term).
 */
public class HfqlStatement implements IModelJson {

	@JsonProperty("select")
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private List<SelectClause> mySelectClauses = new ArrayList<>();

	@JsonProperty("where")
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private List<WhereClause> myWhereClauses = new ArrayList<>();

	@JsonProperty("groupBy")
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private List<String> myGroupByClauses = new ArrayList<>();

	@JsonProperty("orderBy")
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private List<OrderByClause> myOrderByClauses = new ArrayList<>();

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

	@Nonnull
	public SelectClause addSelectClause(@Nonnull String theClause) {
		SelectClauseOperator operator = SelectClauseOperator.SELECT;
		return addSelectClause(theClause, operator);
	}

	@Nonnull
	public SelectClause addSelectClause(@Nonnull String theClause, @Nonnull SelectClauseOperator operator) {
		SelectClause clause = new SelectClause();
		clause.setClause(theClause);
		clause.setOperator(operator);
		mySelectClauses.add(clause);
		return clause;
	}

	public WhereClause addWhereClause() {
		WhereClause clause = new WhereClause();
		myWhereClauses.add(clause);
		return clause;
	}

	public void addWhereClause(String theLeft, WhereClauseOperatorEnum theOperator) {
		WhereClause whereClause = addWhereClause();
		whereClause.setLeft(theLeft);
		whereClause.setOperator(theOperator);
	}

	public List<WhereClause> getWhereClauses() {
		return myWhereClauses;
	}

	@Nullable
	public Integer getLimit() {
		return myLimit;
	}

	public void setLimit(Integer theLimit) {
		myLimit = theLimit;
	}

	public void addGroupByClause(String theGroupByClause) {
		ValidateUtil.isNotBlankOrThrowIllegalArgument(theGroupByClause, "theGroupByClause must not be null or blank");
		getGroupByClauses().add(theGroupByClause);
	}

	public List<String> getGroupByClauses() {
		if (myGroupByClauses == null) {
			myGroupByClauses = new ArrayList<>();
		}
		return myGroupByClauses;
	}

	public boolean hasCountClauses() {
		return getSelectClauses().stream().anyMatch(t -> t.getOperator() == SelectClauseOperator.COUNT);
	}

	public OrderByClause addOrderByClause(String theClause, boolean theAscending) {
		ValidateUtil.isNotBlankOrThrowIllegalArgument(theClause, "theClause must not be null or blank");
		OrderByClause clause = new OrderByClause();
		clause.setClause(theClause);
		clause.setAscending(theAscending);
		getOrderByClauses().add(clause);
		return clause;
	}

	public List<OrderByClause> getOrderByClauses() {
		if (myOrderByClauses == null) {
			myGroupByClauses = new ArrayList<>();
		}
		return myOrderByClauses;
	}

	public int findSelectClauseIndex(String theClause) {
		for (int i = 0; i < getSelectClauses().size(); i++) {
			if (theClause.equals(getSelectClauses().get(i).getClause())
					|| theClause.equals(getSelectClauses().get(i).getAlias())) {
				return i;
			}
		}
		return -1;
	}

	public boolean hasOrderClause() {
		return !getOrderByClauses().isEmpty();
	}

	public List<String> toSelectedColumnAliases() {
		return mySelectClauses.stream().map(SelectClause::getAlias).collect(Collectors.toList());
	}

	public List<HfqlDataTypeEnum> toSelectedColumnDataTypes() {
		return mySelectClauses.stream().map(SelectClause::getDataType).collect(Collectors.toList());
	}

	public SelectClause addSelectClauseAndAlias(String theSelectClause) {
		return addSelectClause(theSelectClause).setAlias(theSelectClause);
	}

	public enum WhereClauseOperatorEnum {
		EQUALS,
		IN,
		UNARY_BOOLEAN,
		SEARCH_MATCH
	}

	public enum SelectClauseOperator {
		SELECT,
		COUNT
	}

	public static class OrderByClause implements IModelJson {

		@JsonProperty("clause")
		private String myClause;

		@JsonProperty("ascending")
		private boolean myAscending;

		public String getClause() {
			return myClause;
		}

		public void setClause(String theClause) {
			myClause = theClause;
		}

		public boolean isAscending() {
			return myAscending;
		}

		public void setAscending(boolean theAscending) {
			myAscending = theAscending;
		}
	}

	public static class SelectClause implements IModelJson {
		@JsonProperty("clause")
		private String myClause;

		@JsonProperty("alias")
		private String myAlias;

		@JsonProperty("operator")
		private SelectClauseOperator myOperator;

		@JsonProperty("dataType")
		private HfqlDataTypeEnum myDataType;

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
		}

		public HfqlDataTypeEnum getDataType() {
			return myDataType;
		}

		public SelectClause setDataType(HfqlDataTypeEnum theDataType) {
			myDataType = theDataType;
			return this;
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

		public SelectClause setAlias(String theAlias) {
			myAlias = theAlias;
			return this;
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

		public void setRight(String... theValues) {
			myRight.clear();
			myRight.addAll(Arrays.asList(theValues));
		}

		public void addRight(String theRight) {
			myRight.add(theRight);
		}

		/**
		 * Returns the {@link #getRight() right} values as raw strings. That
		 * means that any surrounding quote marks are stripped.
		 */
		public List<String> getRightAsStrings() {
			List<String> retVal = new ArrayList<>();
			for (String next : getRight()) {
				if (next.startsWith("'")) {
					next = next.substring(1, next.length() - 1);
				}
				retVal.add(next);
			}
			return retVal;
		}

		/**
		 * Returns a concatenation of the {@link #getLeft() left} and all of the {@link #getRight() right} expressions,
		 * each joined by a single string. This is useful for obtaining expressions of
		 * type {@link WhereClauseOperatorEnum#UNARY_BOOLEAN}.
		 */
		public String asUnaryExpression() {
			return getLeft() + " " + join(getRight(), ' ');
		}
	}
}
