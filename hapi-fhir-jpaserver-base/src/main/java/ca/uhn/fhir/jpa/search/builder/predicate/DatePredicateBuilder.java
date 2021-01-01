package ca.uhn.fhir.jpa.search.builder.predicate;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.predicate.SearchFilterParser;
import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryBuilder;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
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


	public Condition createPredicateDateWithoutIdentityPredicate(IQueryParameterType theParam,
																					 String theResourceName,
																					 String theParamName,
																					 DatePredicateBuilder theFrom,
																					 SearchFilterParser.CompareOperation theOperation,
																					 RequestPartitionId theRequestPartitionId) {

		Condition p;
		if (theParam instanceof DateParam) {
			DateParam date = (DateParam) theParam;
			if (!date.isEmpty()) {
				DateRangeParam range = new DateRangeParam(date);
				p = createPredicateDateFromRange(theFrom, range, theOperation);
			} else {
				// TODO: handle missing date param?
				p = null;
			}
		} else if (theParam instanceof DateRangeParam) {
			DateRangeParam range = (DateRangeParam) theParam;
			p = createPredicateDateFromRange(
				theFrom,
				range,
				theOperation);
		} else {
			throw new IllegalArgumentException("Invalid token type: " + theParam.getClass());
		}

		return p;
	}

	private Condition createPredicateDateFromRange(DatePredicateBuilder theFrom,
																  DateRangeParam theRange,
																  SearchFilterParser.CompareOperation theOperation) {
		Date lowerBoundInstant = theRange.getLowerBoundAsInstant();
		Date upperBoundInstant = theRange.getUpperBoundAsInstant();

		DateParam lowerBound = theRange.getLowerBound();
		DateParam upperBound = theRange.getUpperBound();
		Integer lowerBoundAsOrdinal = theRange.getLowerBoundAsDateInteger();
		Integer upperBoundAsOrdinal = theRange.getUpperBoundAsDateInteger();
		Comparable genericLowerBound;
		Comparable genericUpperBound;
		/**
		 * If all present search parameters are of DAY precision, and {@link ca.uhn.fhir.jpa.model.entity.ModelConfig#getUseOrdinalDatesForDayPrecisionSearches()} is true,
		 * then we attempt to use the ordinal field for date comparisons instead of the date field.
		 */
		boolean isOrdinalComparison = isNullOrDayPrecision(lowerBound) && isNullOrDayPrecision(upperBound) && myDaoConfig.getModelConfig().getUseOrdinalDatesForDayPrecisionSearches();

		Condition lt;
		Condition gt = null;
		Condition lb = null;
		Condition ub = null;
		DatePredicateBuilder.ColumnEnum lowValueField;
		DatePredicateBuilder.ColumnEnum highValueField;

		if (isOrdinalComparison) {
			lowValueField = DatePredicateBuilder.ColumnEnum.LOW_DATE_ORDINAL;
			highValueField = DatePredicateBuilder.ColumnEnum.HIGH_DATE_ORDINAL;
			genericLowerBound = lowerBoundAsOrdinal;
			genericUpperBound = upperBoundAsOrdinal;
		} else {
			lowValueField = DatePredicateBuilder.ColumnEnum.LOW;
			highValueField = DatePredicateBuilder.ColumnEnum.HIGH;
			genericLowerBound = lowerBoundInstant;
			genericUpperBound = upperBoundInstant;
		}

		if (theOperation == SearchFilterParser.CompareOperation.lt) {
			if (lowerBoundInstant == null) {
				throw new InvalidRequestException("lowerBound value not correctly specified for compare theOperation");
			}
			//im like 80% sure this should be ub and not lb, as it is an UPPER bound.
			lb = theFrom.createPredicate(lowValueField, ParamPrefixEnum.LESSTHAN, genericLowerBound);
		} else if (theOperation == SearchFilterParser.CompareOperation.le) {
			if (upperBoundInstant == null) {
				throw new InvalidRequestException("upperBound value not correctly specified for compare theOperation");
			}
			//im like 80% sure this should be ub and not lb, as it is an UPPER bound.
			lb = theFrom.createPredicate(highValueField, ParamPrefixEnum.LESSTHAN_OR_EQUALS, genericUpperBound);
		} else if (theOperation == SearchFilterParser.CompareOperation.gt) {
			if (upperBoundInstant == null) {
				throw new InvalidRequestException("upperBound value not correctly specified for compare theOperation");
			}
			lb = theFrom.createPredicate(highValueField, ParamPrefixEnum.GREATERTHAN, genericUpperBound);
		} else if (theOperation == SearchFilterParser.CompareOperation.ge) {
			if (lowerBoundInstant == null) {
				throw new InvalidRequestException("lowerBound value not correctly specified for compare theOperation");
			}
			lb = theFrom.createPredicate(lowValueField, ParamPrefixEnum.GREATERTHAN_OR_EQUALS, genericLowerBound);
		} else if (theOperation == SearchFilterParser.CompareOperation.ne) {
			if ((lowerBoundInstant == null) ||
				(upperBoundInstant == null)) {
				throw new InvalidRequestException("lowerBound and/or upperBound value not correctly specified for compare theOperation");
			}
			lt = theFrom.createPredicate(lowValueField, ParamPrefixEnum.LESSTHAN, genericLowerBound);
			gt = theFrom.createPredicate(highValueField, ParamPrefixEnum.GREATERTHAN, genericUpperBound);
			lb = ComboCondition.or(lt, gt);
		} else if ((theOperation == SearchFilterParser.CompareOperation.eq) || (theOperation == null)) {
			if (lowerBoundInstant != null) {
				gt = theFrom.createPredicate(lowValueField, ParamPrefixEnum.GREATERTHAN_OR_EQUALS, genericLowerBound);
				lt = theFrom.createPredicate(highValueField, ParamPrefixEnum.GREATERTHAN_OR_EQUALS, genericLowerBound);

				if (lowerBound.getPrefix() == ParamPrefixEnum.STARTS_AFTER || lowerBound.getPrefix() == ParamPrefixEnum.EQUAL) {
					lb = gt;
				} else {
					lb = ComboCondition.or(gt, lt);
				}
			}

			if (upperBoundInstant != null) {
				gt = theFrom.createPredicate(lowValueField, ParamPrefixEnum.LESSTHAN_OR_EQUALS, genericUpperBound);
				lt = theFrom.createPredicate(highValueField, ParamPrefixEnum.LESSTHAN_OR_EQUALS, genericUpperBound);


				if (theRange.getUpperBound().getPrefix() == ParamPrefixEnum.ENDS_BEFORE || theRange.getUpperBound().getPrefix() == ParamPrefixEnum.EQUAL) {
					ub = lt;
				} else {
					ub = ComboCondition.or(gt, lt);
				}
			}
		} else {
			throw new InvalidRequestException(String.format("Unsupported operator specified, operator=%s",
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

	private boolean isNullOrDayPrecision(DateParam theDateParam) {
		return theDateParam == null || theDateParam.getPrecision().ordinal() == TemporalPrecisionEnum.DAY.ordinal();
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
				throw new IllegalArgumentException();
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
