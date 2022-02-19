package ca.uhn.fhir.jpa.search.builder.predicate;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
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
	private DaoConfig myDaoConfig;

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
	public void setDaoConfigForUnitTest(DaoConfig theDaoConfig) {
		myDaoConfig = theDaoConfig;
	}

	public Condition createPredicateDateWithoutIdentityPredicate(IQueryParameterType theParam,
																					 SearchFilterParser.CompareOperation theOperation) {

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

	private Condition createPredicateDateFromRange(DateRangeParam theRange,
																  SearchFilterParser.CompareOperation theOperation) {


		Date lowerBoundInstant = theRange.getLowerBoundAsInstant();
		Date upperBoundInstant = theRange.getUpperBoundAsInstant();

		DateParam lowerBound = theRange.getLowerBound();
		DateParam upperBound = theRange.getUpperBound();
		Integer lowerBoundAsOrdinal = theRange.getLowerBoundAsDateInteger();
		Integer upperBoundAsOrdinal = theRange.getUpperBoundAsDateInteger();
		Comparable<?> genericLowerBound;
		Comparable<?> genericUpperBound;

		/*
		 * If all present search parameters are of DAY precision, and {@link ca.uhn.fhir.jpa.model.entity.ModelConfig#getUseOrdinalDatesForDayPrecisionSearches()} is true,
		 * then we attempt to use the ordinal field for date comparisons instead of the date field.
		 */
		boolean isOrdinalComparison = isNullOrDatePrecision(lowerBound) && isNullOrDatePrecision(upperBound) && myDaoConfig.getModelConfig().getUseOrdinalDatesForDayPrecisionSearches();

		Condition lt;
		Condition gt;
		Condition lb = null;
		Condition ub = null;
		DatePredicateBuilder.ColumnEnum lowValueField;
		DatePredicateBuilder.ColumnEnum highValueField;

		if (isOrdinalComparison) {
			lowValueField = DatePredicateBuilder.ColumnEnum.LOW_DATE_ORDINAL;
			highValueField = DatePredicateBuilder.ColumnEnum.HIGH_DATE_ORDINAL;
			genericLowerBound = lowerBoundAsOrdinal;
			genericUpperBound = upperBoundAsOrdinal;
			if (upperBound != null && upperBound.getPrecision().ordinal() <= TemporalPrecisionEnum.MONTH.ordinal()) {
				genericUpperBound = Integer.parseInt(DateUtils.getCompletedDate(upperBound.getValueAsString()).getRight().replace("-", ""));
			}
		} else {
			lowValueField = DatePredicateBuilder.ColumnEnum.LOW;
			highValueField = DatePredicateBuilder.ColumnEnum.HIGH;
			genericLowerBound = lowerBoundInstant;
			genericUpperBound = upperBoundInstant;
			if (upperBound != null && upperBound.getPrecision().ordinal() <= TemporalPrecisionEnum.MONTH.ordinal()) {
				String theCompleteDateStr = DateUtils.getCompletedDate(upperBound.getValueAsString()).getRight().replace("-", "");
				genericUpperBound = DateUtils.parseDate(theCompleteDateStr);
			}
		}

		if (theOperation == SearchFilterParser.CompareOperation.lt || theOperation == SearchFilterParser.CompareOperation.le) {
			// use lower bound first
			if (lowerBoundInstant != null) {
				lb = this.createPredicate(lowValueField, ParamPrefixEnum.LESSTHAN_OR_EQUALS, genericLowerBound);
				if (myDaoConfig.isAccountForDateIndexNulls()) {
					lb = ComboCondition.or(lb, this.createPredicate(highValueField, ParamPrefixEnum.LESSTHAN_OR_EQUALS, genericLowerBound));
				}
			} else if (upperBoundInstant != null) {
				ub = this.createPredicate(lowValueField, ParamPrefixEnum.LESSTHAN_OR_EQUALS, genericUpperBound);
				if (myDaoConfig.isAccountForDateIndexNulls()) {
					ub = ComboCondition.or(ub, this.createPredicate(highValueField, ParamPrefixEnum.LESSTHAN_OR_EQUALS, genericUpperBound));
				}
			} else {
				throw new InvalidRequestException(Msg.code(1252) + "lowerBound and upperBound value not correctly specified for comparing " + theOperation);
			}
		} else if (theOperation == SearchFilterParser.CompareOperation.gt || theOperation == SearchFilterParser.CompareOperation.ge) {
			// use upper bound first, e.g value between 6 and 10
			if (upperBoundInstant != null) {
				ub = this.createPredicate(highValueField, ParamPrefixEnum.GREATERTHAN_OR_EQUALS, genericUpperBound);
				if (myDaoConfig.isAccountForDateIndexNulls()) {
					ub = ComboCondition.or(ub, this.createPredicate(lowValueField, ParamPrefixEnum.GREATERTHAN_OR_EQUALS, genericUpperBound));
				}
			} else if (lowerBoundInstant != null) {
				lb = this.createPredicate(highValueField, ParamPrefixEnum.GREATERTHAN_OR_EQUALS, genericLowerBound);
				if (myDaoConfig.isAccountForDateIndexNulls()) {
					lb = ComboCondition.or(lb, this.createPredicate(lowValueField, ParamPrefixEnum.GREATERTHAN_OR_EQUALS, genericLowerBound));
				}
			} else {
				throw new InvalidRequestException(Msg.code(1253) + "upperBound and lowerBound value not correctly specified for compare theOperation");
			}
		} else if (theOperation == SearchFilterParser.CompareOperation.ne) {
			if ((lowerBoundInstant == null) ||
				(upperBoundInstant == null)) {
				throw new InvalidRequestException(Msg.code(1254) + "lowerBound and/or upperBound value not correctly specified for compare theOperation");
			}
			lt = this.createPredicate(lowValueField, ParamPrefixEnum.LESSTHAN, genericLowerBound);
			gt = this.createPredicate(highValueField, ParamPrefixEnum.GREATERTHAN, genericUpperBound);
			lb = ComboCondition.or(lt, gt);
		} else if ((theOperation == SearchFilterParser.CompareOperation.eq)
			|| (theOperation == SearchFilterParser.CompareOperation.sa)
			|| (theOperation == SearchFilterParser.CompareOperation.eb)
			|| (theOperation == null)) {
			if (lowerBoundInstant != null) {
				gt = this.createPredicate(lowValueField, ParamPrefixEnum.GREATERTHAN_OR_EQUALS, genericLowerBound);
				lt = this.createPredicate(highValueField, ParamPrefixEnum.GREATERTHAN_OR_EQUALS, genericLowerBound);

				if (lowerBound.getPrefix() == ParamPrefixEnum.STARTS_AFTER || lowerBound.getPrefix() == ParamPrefixEnum.EQUAL) {
					lb = gt;
				} else {
					lb = ComboCondition.or(gt, lt);
				}
			}

			if (upperBoundInstant != null) {
				gt = this.createPredicate(lowValueField, ParamPrefixEnum.LESSTHAN_OR_EQUALS, genericUpperBound);
				lt = this.createPredicate(highValueField, ParamPrefixEnum.LESSTHAN_OR_EQUALS, genericUpperBound);


				if (theRange.getUpperBound().getPrefix() == ParamPrefixEnum.ENDS_BEFORE || theRange.getUpperBound().getPrefix() == ParamPrefixEnum.EQUAL) {
					ub = lt;
				} else {
					ub = ComboCondition.or(gt, lt);
				}
			}
		} else {
			throw new InvalidRequestException(Msg.code(1255) + String.format("Unsupported operator specified, operator=%s",
				theOperation.name()));
		}
		if (isOrdinalComparison) {
			ourLog.trace("Ordinal date range is {} - {} ", lowerBoundAsOrdinal, upperBoundAsOrdinal);
		} else {
			ourLog.trace("Date range is {} - {}", lowerBoundInstant, upperBoundInstant);
		}

		if (lb != null && ub != null) {
			return (ComboCondition.and(lb, ub));
		} else if (lb != null) {
			return (lb);
		} else {
			return (ub);
		}
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

}
