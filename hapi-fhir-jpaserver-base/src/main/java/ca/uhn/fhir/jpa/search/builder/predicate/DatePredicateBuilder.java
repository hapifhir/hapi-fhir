/*-
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.search.builder.predicate;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.predicate.SearchFilterParser;
import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryBuilder;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.rest.param.Constraint;
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
import java.util.List;

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
					List<Constraint<Date>> constraints = date.getConstraints();
					date = new DateParam(ParamPrefixEnum.EQUAL, date.getValueAsString());
					for (Constraint<Date> constraint : constraints) {
						date.addConstraint(constraint);
					}
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
		DatePredicateBuilder.ColumnEnum lowValueField;
		DatePredicateBuilder.ColumnEnum highValueField;

		Condition lessThan;
		Condition greaterThan;
		Condition lowerBoundCondition = null;
		Condition upperBoundCondition = null;

		Date lowerBoundInstant;
		Date upperBoundInstant;

		DateParam lowerBound;
		DateParam upperBound;

		Integer lowerBoundAsOrdinal;
		Integer upperBoundAsOrdinal;
		Comparable<?> genericLowerBound;
		Comparable<?> genericUpperBound;

		public DatePredicateBounds(DateRangeParam theRange) {
			lowerBoundInstant = theRange.getLowerBoundAsInstant();
			upperBoundInstant = theRange.getUpperBoundAsInstant();

			lowerBound = theRange.getLowerBound();
			upperBound = theRange.getUpperBound();
			lowerBoundAsOrdinal = theRange.getLowerBoundAsDateInteger();
			upperBoundAsOrdinal = theRange.getUpperBoundAsDateInteger();

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
				if ((lowerBoundInstant == null) || (upperBoundInstant == null)) {
					throw new InvalidRequestException(Msg.code(1254)
							+ "lowerBound and/or upperBound value not correctly specified for compare theOperation");
				}
				lessThan = DatePredicateBuilder.this.createPredicate(
						lowValueField, ParamPrefixEnum.LESSTHAN, genericLowerBound);
				greaterThan = DatePredicateBuilder.this.createPredicate(
						highValueField, ParamPrefixEnum.GREATERTHAN, genericUpperBound);
				lowerBoundCondition = ComboCondition.or(lessThan, greaterThan);
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
				ourLog.trace("Ordinal date range is {} - {} ", lowerBoundAsOrdinal, upperBoundAsOrdinal);
			} else {
				ourLog.trace("Date range is {} - {}", lowerBoundInstant, upperBoundInstant);
			}

			if (lowerBoundCondition != null && upperBoundCondition != null) {
				return (ComboCondition.and(lowerBoundCondition, upperBoundCondition));
			} else if (lowerBoundCondition != null) {
				return (lowerBoundCondition);
			} else {
				return (upperBoundCondition);
			}
		}

		private void handleEqualToCompareOperator() {
			//			if (lowerBoundInstant != null && upperBoundInstant != null) {
			//				/*
			//				 * Upper and Lower bounds provided.
			//				 * We do some additional redundancies to help the query analyzer.
			//				 *
			//				 * We know our tables low_value fields are always <= high_value fields.
			//				 * But the Query Planner doesn't know that. So we'll force it to only
			//				 * look for lowerBounds <= high_value and upperBounds >= low_value
			//				 * (which is always true).
			//				 */
			//				// lowerBound <= lowValueField <= upperBound
			//				Condition lowerBoundGE = DatePredicateBuilder.this.createPredicate(lowValueField,
			// ParamPrefixEnum.GREATERTHAN_OR_EQUALS, genericLowerBound);
			//				Condition lowerBoundLE = DatePredicateBuilder.this.createPredicate(lowValueField,
			// ParamPrefixEnum.LESSTHAN_OR_EQUALS, genericUpperBound);
			//
			//				lowerBoundCondition = ComboCondition.and(lowerBoundGE, lowerBoundLE);
			//
			//				// lowerBound <= highValueField <= upperBound
			//				Condition upperBoundLE = DatePredicateBuilder.this.createPredicate(highValueField,
			// ParamPrefixEnum.LESSTHAN_OR_EQUALS, genericUpperBound);
			//				Condition upperBoundGE = DatePredicateBuilder.this.createPredicate(highValueField,
			// ParamPrefixEnum.GREATERTHAN_OR_EQUALS, genericLowerBound);
			//
			//				upperBoundCondition = ComboCondition.and(upperBoundGE, upperBoundLE);
			//			} else if (lowerBoundInstant != null) {
			//				// only lower bound provided
			//				greaterThan = DatePredicateBuilder.this.createPredicate(lowValueField,
			// ParamPrefixEnum.GREATERTHAN_OR_EQUALS, genericLowerBound);
			//				lessThan = DatePredicateBuilder.this.createPredicate(highValueField,
			// ParamPrefixEnum.GREATERTHAN_OR_EQUALS, genericLowerBound);
			//
			//				if (lowerBound.getPrefix() == ParamPrefixEnum.STARTS_AFTER
			//					|| lowerBound.getPrefix() == ParamPrefixEnum.EQUAL) {
			//					lowerBoundCondition = greaterThan;
			//				} else {
			//					lowerBoundCondition = ComboCondition.or(greaterThan, lessThan);
			//				}
			//			} else if (upperBoundInstant != null) {
			//				// only upper bound provided
			//				greaterThan = DatePredicateBuilder.this.createPredicate(lowValueField,
			// ParamPrefixEnum.LESSTHAN_OR_EQUALS, genericUpperBound);
			//				lessThan = DatePredicateBuilder.this.createPredicate(highValueField,
			// ParamPrefixEnum.LESSTHAN_OR_EQUALS, genericUpperBound);
			//
			//				if (upperBound.getPrefix() == ParamPrefixEnum.ENDS_BEFORE
			//					|| upperBound.getPrefix() == ParamPrefixEnum.EQUAL) {
			//					upperBoundCondition = lessThan;
			//				} else {
			//					upperBoundCondition = ComboCondition.or(greaterThan, lessThan);
			//				}
			//			} else {
			//				// TODO - throw
			//			}

			if (lowerBoundInstant != null) {
				lowerBoundCondition = ComboCondition.or(
						DatePredicateBuilder.this.createPredicate(
								highValueField, ParamPrefixEnum.EQUAL, genericLowerBound),
						DatePredicateBuilder.this.createPredicate(
								lowValueField, ParamPrefixEnum.EQUAL, genericLowerBound));
			} else {
				upperBoundCondition = ComboCondition.or(
						DatePredicateBuilder.this.createPredicate(
								highValueField, ParamPrefixEnum.EQUAL, genericUpperBound),
						DatePredicateBuilder.this.createPredicate(
								lowValueField, ParamPrefixEnum.EQUAL, genericUpperBound));
			}
		}

		private void handleGreaterThanAndGreaterThanOrEqualTo() {
			if (upperBoundInstant != null && lowerBoundInstant != null) {
				// both - we have a range
				// t0.SP_VALUE_LOW >= :rangeLow AND t0.SP_VALUE_LOW <= : rangeHigh
				lowerBoundCondition = DatePredicateBuilder.this.createPredicate(
						lowValueField, ParamPrefixEnum.GREATERTHAN_OR_EQUALS, genericLowerBound);
				upperBoundCondition = DatePredicateBuilder.this.createPredicate(
						lowValueField, ParamPrefixEnum.LESSTHAN_OR_EQUALS, genericUpperBound);
			} else if (upperBoundInstant != null) {
				// upper bound only
				upperBoundCondition = DatePredicateBuilder.this.createPredicate(
						highValueField, ParamPrefixEnum.GREATERTHAN_OR_EQUALS, genericUpperBound);
				if (myStorageSettings.isAccountForDateIndexNulls()) {
					upperBoundCondition = ComboCondition.or(
							upperBoundCondition,
							DatePredicateBuilder.this.createPredicate(
									lowValueField, ParamPrefixEnum.GREATERTHAN_OR_EQUALS, genericUpperBound));
				}
			} else if (lowerBoundInstant != null) {
				// lower bound only
				lowerBoundCondition = DatePredicateBuilder.this.createPredicate(
						highValueField, ParamPrefixEnum.GREATERTHAN_OR_EQUALS, genericLowerBound);
				if (myStorageSettings.isAccountForDateIndexNulls()) {
					lowerBoundCondition = ComboCondition.or(
							lowerBoundCondition,
							DatePredicateBuilder.this.createPredicate(
									lowValueField, ParamPrefixEnum.GREATERTHAN_OR_EQUALS, genericLowerBound));
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
			if (lowerBoundInstant != null && upperBoundInstant != null) {
				// we have a range of values
				// t0.SP_VALUE_HIGH >= :rangeLow AND t0.SP_VALUE_HIGH <= :rangeHigh
				lowerBoundCondition = DatePredicateBuilder.this.createPredicate(
						highValueField, ParamPrefixEnum.GREATERTHAN_OR_EQUALS, genericLowerBound);
				upperBoundCondition = DatePredicateBuilder.this.createPredicate(
						highValueField, ParamPrefixEnum.LESSTHAN_OR_EQUALS, genericUpperBound);
			} else if (lowerBoundInstant != null) {
				// lower bound only provided
				lowerBoundCondition = DatePredicateBuilder.this.createPredicate(
						lowValueField, ParamPrefixEnum.LESSTHAN_OR_EQUALS, genericLowerBound);

				if (myStorageSettings.isAccountForDateIndexNulls()) {
					lowerBoundCondition = ComboCondition.or(
							lowerBoundCondition,
							DatePredicateBuilder.this.createPredicate(
									highValueField, ParamPrefixEnum.LESSTHAN_OR_EQUALS, genericLowerBound));
				}
			} else if (upperBoundInstant != null) {
				// upper bound only provided
				upperBoundCondition = DatePredicateBuilder.this.createPredicate(
						lowValueField, ParamPrefixEnum.LESSTHAN_OR_EQUALS, genericUpperBound);
				if (myStorageSettings.isAccountForDateIndexNulls()) {
					upperBoundCondition = ComboCondition.or(
							upperBoundCondition,
							DatePredicateBuilder.this.createPredicate(
									highValueField, ParamPrefixEnum.LESSTHAN_OR_EQUALS, genericUpperBound));
				}
			} else {
				throw new InvalidRequestException(
						Msg.code(1252)
								+ "lowerBound and upperBound value not correctly specified for comparing using lower than (or equal to) compare operator");
			}
		}

		private void init() {
			if (isOrdinalComparison()) {
				lowValueField = DatePredicateBuilder.ColumnEnum.LOW_DATE_ORDINAL;
				highValueField = DatePredicateBuilder.ColumnEnum.HIGH_DATE_ORDINAL;
				genericLowerBound = lowerBoundAsOrdinal;
				genericUpperBound = upperBoundAsOrdinal;
				if (upperBound != null
						&& upperBound.getPrecision().ordinal() <= TemporalPrecisionEnum.MONTH.ordinal()) {
					genericUpperBound = Integer.parseInt(DateUtils.getCompletedDate(upperBound.getValueAsString())
							.getRight()
							.replace("-", ""));
				}
			} else {
				lowValueField = DatePredicateBuilder.ColumnEnum.LOW;
				highValueField = DatePredicateBuilder.ColumnEnum.HIGH;
				genericLowerBound = lowerBoundInstant;
				genericUpperBound = upperBoundInstant;
				if (upperBound != null
						&& upperBound.getPrecision().ordinal() <= TemporalPrecisionEnum.MONTH.ordinal()) {
					String theCompleteDateStr = DateUtils.getCompletedDate(upperBound.getValueAsString())
							.getRight()
							.replace("-", "");
					genericUpperBound = DateUtils.parseDate(theCompleteDateStr);
				}
			}
		}

		/**
		 * If all present search parameters are of DAY precision, and {@link ca.uhn.fhir.jpa.model.entity.StorageSettings#getUseOrdinalDatesForDayPrecisionSearches()} is true,
		 * then we attempt to use the ordinal field for date comparisons instead of the date field.
		 */
		private boolean isOrdinalComparison() {
			return isNullOrDatePrecision(lowerBound)
					&& isNullOrDatePrecision(upperBound)
					&& myStorageSettings.getUseOrdinalDatesForDayPrecisionSearches();
		}
	}
}
