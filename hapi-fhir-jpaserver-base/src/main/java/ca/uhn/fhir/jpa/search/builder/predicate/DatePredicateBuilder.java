/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.search.builder.predicate;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.predicate.SearchFilterParser;
import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryBuilder;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.DateUtils;
import com.google.common.annotations.VisibleForTesting;
import com.healthmarketscience.sqlbuilder.ComboCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class DatePredicateBuilder extends BaseSearchParamPredicateBuilder {

	private static final Logger ourLog = LoggerFactory.getLogger(DatePredicateBuilder.class);
	private final DbColumn myColumnValueHigh;
	private final DbColumn myColumnValueLow;
	private final DbColumn myColumnValueLowDateOrdinal;
	private final DbColumn myColumnValueHighDateOrdinal;

	@Autowired
	private JpaStorageSettings myStorageSettings;

	/**
	 * Constructor
	 */
	public DatePredicateBuilder(SearchQueryBuilder theSearchSqlBuilder) {
		super(theSearchSqlBuilder, theSearchSqlBuilder.addTable("HFJ_SPIDX_DATE"));

		myColumnValueLow = getTable().addColumn("SP_VALUE_LOW");
		myColumnValueHigh = getTable().addColumn("SP_VALUE_HIGH");
		myColumnValueLowDateOrdinal = getTable().addColumn("SP_VALUE_LOW_DATE_ORDINAL");
		myColumnValueHighDateOrdinal = getTable().addColumn("SP_VALUE_HIGH_DATE_ORDINAL");
	}

	@VisibleForTesting
	public void setStorageSettingsForUnitTest(JpaStorageSettings theStorageSettings) {
		myStorageSettings = theStorageSettings;
	}

	public Condition createPredicateDateWithoutIdentityPredicate(
			IQueryParameterType theParam, SearchFilterParser.CompareOperation theOperation) {

		Condition p;
		if (theParam instanceof DateParam) {
			DateParam date = (DateParam) theParam;
			if (!date.isEmpty()) {
				if (theOperation == SearchFilterParser.CompareOperation.ne) {
					date = new DateParam(ParamPrefixEnum.EQUAL, date.getValueAsString());
				}
				DateRangeParam range = new DateRangeParam(date);
				p = createPredicateDateFromRange(range, theOperation);
			} else {
				// TODO: handle missing date param?
				p = null;
			}
		} else if (theParam instanceof DateRangeParam) {
			DateRangeParam range = (DateRangeParam) theParam;
			p = createPredicateDateFromRange(range, theOperation);
		} else {
			throw new IllegalArgumentException(Msg.code(1251) + "Invalid token type: " + theParam.getClass());
		}

		return p;
	}

	private Condition createPredicateDateFromRange(
			DateRangeParam theRange, SearchFilterParser.CompareOperation theOperation) {

		DatePredicateBounds datePredicateBounds = new DatePredicateBounds(theRange);

		return datePredicateBounds.calculate(theOperation);
	}

	public DbColumn getColumnValueLow() {
		return myColumnValueLow;
	}

	private boolean isNullOrDatePrecision(DateParam theDateParam) {
		return theDateParam == null || theDateParam.getPrecision().ordinal() <= TemporalPrecisionEnum.DAY.ordinal();
	}

	private Condition createPredicate(ColumnEnum theColumn, ParamPrefixEnum theComparator, Object theValue) {

		DbColumn column;
		switch (theColumn) {
			case LOW:
				column = myColumnValueLow;
				break;
			case LOW_DATE_ORDINAL:
				column = myColumnValueLowDateOrdinal;
				break;
			case HIGH:
				column = myColumnValueHigh;
				break;
			case HIGH_DATE_ORDINAL:
				column = myColumnValueHighDateOrdinal;
				break;
			default:
				throw new IllegalArgumentException(Msg.code(1256));
		}

		return createConditionForValueWithComparator(theComparator, column, theValue);
	}

	public enum ColumnEnum {
		LOW,
		LOW_DATE_ORDINAL,
		HIGH,
		HIGH_DATE_ORDINAL
	}

	public class DatePredicateBounds {
		private DatePredicateBuilder.ColumnEnum myLowValueField;
		private DatePredicateBuilder.ColumnEnum myHighValueField;

		private Condition myLowerBoundCondition = null;
		private Condition myUpperBoundCondition = null;

		private final Date myLowerBoundInstant;
		private final Date myUpperBoundInstant;

		private final DateParam myLowerBound;
		private final DateParam myUpperBound;

		private final Integer myLowerBoundAsOrdinal;
		private final Integer myUpperBoundAsOrdinal;
		private Comparable<?> myGenericLowerBound;
		private Comparable<?> myGenericUpperBound;

		public DatePredicateBounds(DateRangeParam theRange) {
			myLowerBoundInstant = theRange.getLowerBoundAsInstant();
			myUpperBoundInstant = theRange.getUpperBoundAsInstant();

			myLowerBound = theRange.getLowerBound();
			myUpperBound = theRange.getUpperBound();
			myLowerBoundAsOrdinal = theRange.getLowerBoundAsDateInteger();
			myUpperBoundAsOrdinal = theRange.getUpperBoundAsDateInteger();

			init();
		}

		public Condition calculate(SearchFilterParser.CompareOperation theOperation) {
			if (theOperation == SearchFilterParser.CompareOperation.lt
					|| theOperation == SearchFilterParser.CompareOperation.le) {
				// use lower bound first
				handleLessThanAndLessThanOrEqualTo();
			} else if (theOperation == SearchFilterParser.CompareOperation.gt
					|| theOperation == SearchFilterParser.CompareOperation.ge) {
				// use upper bound first, e.g value between 6 and 10
				handleGreaterThanAndGreaterThanOrEqualTo();
			} else if (theOperation == SearchFilterParser.CompareOperation.ne) {
				if ((myLowerBoundInstant == null) || (myUpperBoundInstant == null)) {
					throw new InvalidRequestException(Msg.code(1254)
							+ "lowerBound and/or upperBound value not correctly specified for compare theOperation");
				}
				Condition lessThan = DatePredicateBuilder.this.createPredicate(
						myLowValueField, ParamPrefixEnum.LESSTHAN, myGenericLowerBound);
				Condition greaterThan = DatePredicateBuilder.this.createPredicate(
						myHighValueField, ParamPrefixEnum.GREATERTHAN, myGenericUpperBound);
				myLowerBoundCondition = ComboCondition.or(lessThan, greaterThan);
			} else if ((theOperation == SearchFilterParser.CompareOperation.eq)
					|| (theOperation == SearchFilterParser.CompareOperation.sa)
					|| (theOperation == SearchFilterParser.CompareOperation.eb)
					|| (theOperation == null)) {

				handleEqualToCompareOperator();
			} else {
				throw new InvalidRequestException(Msg.code(1255)
						+ String.format("Unsupported operator specified, operator=%s", theOperation.name()));
			}

			if (isOrdinalComparison()) {
				ourLog.trace("Ordinal date range is {} - {} ", myLowerBoundAsOrdinal, myUpperBoundAsOrdinal);
			} else {
				ourLog.trace("Date range is {} - {}", myLowerBoundInstant, myUpperBoundInstant);
			}

			if (myLowerBoundCondition != null && myUpperBoundCondition != null) {
				return (ComboCondition.and(myLowerBoundCondition, myUpperBoundCondition));
			} else if (myLowerBoundCondition != null) {
				return (myLowerBoundCondition);
			} else {
				return (myUpperBoundCondition);
			}
		}

		private void handleEqualToCompareOperator() {
			Condition lessThan;
			Condition greaterThan;
			if (myLowerBoundInstant != null && myUpperBoundInstant != null) {
				// both upper and lower bound
				// lowerbound; :lowerbound <= low_field <= :upperbound
				greaterThan = ComboCondition.and(
						DatePredicateBuilder.this.createPredicate(
								myLowValueField, ParamPrefixEnum.GREATERTHAN_OR_EQUALS, myGenericLowerBound),
						DatePredicateBuilder.this.createPredicate(
								myLowValueField, ParamPrefixEnum.LESSTHAN_OR_EQUALS, myGenericUpperBound));
				// upperbound; :lowerbound <= high_field <= :upperbound
				lessThan = ComboCondition.and(
						DatePredicateBuilder.this.createPredicate(
								myHighValueField, ParamPrefixEnum.LESSTHAN_OR_EQUALS, myGenericUpperBound),
						DatePredicateBuilder.this.createPredicate(
								myHighValueField, ParamPrefixEnum.GREATERTHAN_OR_EQUALS, myGenericLowerBound));

				myLowerBoundCondition = greaterThan;
				myUpperBoundCondition = lessThan;
			} else if (myLowerBoundInstant != null) {
				// lower bound only
				greaterThan = DatePredicateBuilder.this.createPredicate(
						myLowValueField, ParamPrefixEnum.GREATERTHAN_OR_EQUALS, myGenericLowerBound);
				lessThan = DatePredicateBuilder.this.createPredicate(
						myHighValueField, ParamPrefixEnum.GREATERTHAN_OR_EQUALS, myGenericLowerBound);

				if (myLowerBound.getPrefix() == ParamPrefixEnum.STARTS_AFTER
						|| myLowerBound.getPrefix() == ParamPrefixEnum.EQUAL) {
					myLowerBoundCondition = greaterThan;
				} else {
					myLowerBoundCondition = ComboCondition.or(greaterThan, lessThan);
				}
			} else {
				// only upper bound provided
				greaterThan = DatePredicateBuilder.this.createPredicate(
						myLowValueField, ParamPrefixEnum.LESSTHAN_OR_EQUALS, myGenericUpperBound);
				lessThan = DatePredicateBuilder.this.createPredicate(
						myHighValueField, ParamPrefixEnum.LESSTHAN_OR_EQUALS, myGenericUpperBound);

				if (myUpperBound.getPrefix() == ParamPrefixEnum.ENDS_BEFORE
						|| myUpperBound.getPrefix() == ParamPrefixEnum.EQUAL) {
					myUpperBoundCondition = lessThan;
				} else {
					myUpperBoundCondition = ComboCondition.or(greaterThan, lessThan);
				}
			}
		}

		private void handleGreaterThanAndGreaterThanOrEqualTo() {
			if (myUpperBoundInstant != null) {
				// upper bound only
				myUpperBoundCondition = DatePredicateBuilder.this.createPredicate(
						myHighValueField, ParamPrefixEnum.GREATERTHAN_OR_EQUALS, myGenericUpperBound);
				if (myStorageSettings.isAccountForDateIndexNulls()) {
					myUpperBoundCondition = ComboCondition.or(
							myUpperBoundCondition,
							DatePredicateBuilder.this.createPredicate(
									myLowValueField, ParamPrefixEnum.GREATERTHAN_OR_EQUALS, myGenericUpperBound));
				}
			} else if (myLowerBoundInstant != null) {
				// lower bound only
				myLowerBoundCondition = DatePredicateBuilder.this.createPredicate(
						myHighValueField, ParamPrefixEnum.GREATERTHAN_OR_EQUALS, myGenericLowerBound);
				if (myStorageSettings.isAccountForDateIndexNulls()) {
					myLowerBoundCondition = ComboCondition.or(
							myLowerBoundCondition,
							DatePredicateBuilder.this.createPredicate(
									myLowValueField, ParamPrefixEnum.GREATERTHAN_OR_EQUALS, myGenericLowerBound));
				}
			} else {
				throw new InvalidRequestException(
						Msg.code(1253)
								+ "upperBound and lowerBound value not correctly specified for greater than (or equal to) compare operator");
			}
		}

		/**
		 * Handle (LOW|HIGH)_FIELD <(=) value
		 */
		private void handleLessThanAndLessThanOrEqualTo() {
			if (myLowerBoundInstant != null) {
				// lower bound only provided
				myLowerBoundCondition = DatePredicateBuilder.this.createPredicate(
						myLowValueField, ParamPrefixEnum.LESSTHAN_OR_EQUALS, myGenericLowerBound);

				if (myStorageSettings.isAccountForDateIndexNulls()) {
					myLowerBoundCondition = ComboCondition.or(
							myLowerBoundCondition,
							DatePredicateBuilder.this.createPredicate(
									myHighValueField, ParamPrefixEnum.LESSTHAN_OR_EQUALS, myGenericLowerBound));
				}
			} else if (myUpperBoundInstant != null) {
				// upper bound only provided
				myUpperBoundCondition = DatePredicateBuilder.this.createPredicate(
						myLowValueField, ParamPrefixEnum.LESSTHAN_OR_EQUALS, myGenericUpperBound);
				if (myStorageSettings.isAccountForDateIndexNulls()) {
					myUpperBoundCondition = ComboCondition.or(
							myUpperBoundCondition,
							DatePredicateBuilder.this.createPredicate(
									myHighValueField, ParamPrefixEnum.LESSTHAN_OR_EQUALS, myGenericUpperBound));
				}
			} else {
				throw new InvalidRequestException(
						Msg.code(1252)
								+ "lowerBound and upperBound value not correctly specified for comparing using lower than (or equal to) compare operator");
			}
		}

		private void init() {
			if (isOrdinalComparison()) {
				myLowValueField = DatePredicateBuilder.ColumnEnum.LOW_DATE_ORDINAL;
				myHighValueField = DatePredicateBuilder.ColumnEnum.HIGH_DATE_ORDINAL;
				myGenericLowerBound = myLowerBoundAsOrdinal;
				myGenericUpperBound = myUpperBoundAsOrdinal;
				if (myUpperBound != null
						&& myUpperBound.getPrecision().ordinal() <= TemporalPrecisionEnum.MONTH.ordinal()) {
					myGenericUpperBound = Integer.parseInt(DateUtils.getCompletedDate(myUpperBound.getValueAsString())
							.getRight()
							.replace("-", ""));
				}
			} else {
				myLowValueField = DatePredicateBuilder.ColumnEnum.LOW;
				myHighValueField = DatePredicateBuilder.ColumnEnum.HIGH;
				myGenericLowerBound = myLowerBoundInstant;
				myGenericUpperBound = myUpperBoundInstant;
				if (myUpperBound != null
						&& myUpperBound.getPrecision().ordinal() <= TemporalPrecisionEnum.MONTH.ordinal()) {
					String theCompleteDateStr = DateUtils.getCompletedDate(myUpperBound.getValueAsString())
							.getRight()
							.replace("-", "");
					myGenericUpperBound = DateUtils.parseDate(theCompleteDateStr);
				}
			}
		}

		/**
		 * If all present search parameters are of DAY precision, and {@link ca.uhn.fhir.jpa.model.entity.StorageSettings#getUseOrdinalDatesForDayPrecisionSearches()} is true,
		 * then we attempt to use the ordinal field for date comparisons instead of the date field.
		 */
		private boolean isOrdinalComparison() {
			return isNullOrDatePrecision(myLowerBound)
					&& isNullOrDatePrecision(myUpperBound)
					&& myStorageSettings.getUseOrdinalDatesForDayPrecisionSearches();
		}
	}
}
