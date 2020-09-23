package ca.uhn.fhir.jpa.dao.search.sql;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.predicate.SearchFilterParser;
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

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Date;

public class DateIndexTable extends BaseSearchParamIndexTable {

	private static final Logger ourLog = LoggerFactory.getLogger(DateIndexTable.class);
	private final DbColumn myColumnValueHigh;
	private final DbColumn myColumnValueLow;
	private final DbColumn myColumnValueLowDateOrdinal;
	private final DbColumn myColumnValueHighDateOrdinal;

	@Autowired
	private DaoConfig myDaoConfig;

	/**
	 * Constructor
	 */
	public DateIndexTable(SearchSqlBuilder theSearchSqlBuilder) {
		super(theSearchSqlBuilder, theSearchSqlBuilder.addTable("HFJ_SPIDX_DATE"));

		myColumnValueLow = getTable().addColumn("SP_VALUE_LOW");
		myColumnValueHigh = getTable().addColumn("SP_VALUE_HIGH");
		myColumnValueLowDateOrdinal = getTable().addColumn("SP_VALUE_LOW_DATE_ORDINAL");
		myColumnValueHighDateOrdinal = getTable().addColumn("SP_VALUE_HIGH_DATE_ORDINAL");
	}


	public Condition createPredicateDate(IQueryParameterType theParam,
													 String theResourceName,
													 String theParamName,
													 CriteriaBuilder theBuilder,
													 DateIndexTable theFrom,
													 SearchFilterParser.CompareOperation theOperation,
													 RequestPartitionId theRequestPartitionId) {

		Condition p;
		if (theParam instanceof DateParam) {
			DateParam date = (DateParam) theParam;
			if (!date.isEmpty()) {
				DateRangeParam range = new DateRangeParam(date);
				p = createPredicateDateFromRange(theBuilder, theFrom, range, theOperation);
			} else {
				// TODO: handle missing date param?
				p = null;
			}
		} else if (theParam instanceof DateRangeParam) {
			DateRangeParam range = (DateRangeParam) theParam;
			p = createPredicateDateFromRange(theBuilder,
				theFrom,
				range,
				theOperation);
		} else {
			throw new IllegalArgumentException("Invalid token type: " + theParam.getClass());
		}

		return p;
	}

	private boolean isNullOrDayPrecision(DateParam theDateParam) {
		return theDateParam == null || theDateParam.getPrecision().ordinal() == TemporalPrecisionEnum.DAY.ordinal();
	}

	private Condition createPredicateDateFromRange(CriteriaBuilder theBuilder,
																  DateIndexTable theFrom,
																  DateRangeParam theRange,
																  SearchFilterParser.CompareOperation operation) {
		Date lowerBoundInstant = theRange.getLowerBoundAsInstant();
		Date upperBoundInstant = theRange.getUpperBoundAsInstant();

		DateParam lowerBound = theRange.getLowerBound();
		DateParam upperBound = theRange.getUpperBound();
		Integer lowerBoundAsOrdinal = theRange.getLowerBoundAsDateInteger();
		Integer upperBoundAsOrdinal = theRange.getUpperBoundAsDateInteger();
		Comparable genericLowerBound;
		Comparable genericUpperBound;
		/**
		 * If all present search parameters are of DAY precision, and {@link DaoConfig#getUseOrdinalDatesForDayPrecisionSearches()} is true,
		 * then we attempt to use the ordinal field for date comparisons instead of the date field.
		 */
		boolean isOrdinalComparison = isNullOrDayPrecision(lowerBound) && isNullOrDayPrecision(upperBound) && myDaoConfig.getModelConfig().getUseOrdinalDatesForDayPrecisionSearches();

		Condition lt;
		Condition gt = null;
		Condition lb = null;
		Condition ub = null;
		DateIndexTable.ColumnEnum lowValueField;
		DateIndexTable.ColumnEnum highValueField;

		if (isOrdinalComparison) {
			lowValueField = DateIndexTable.ColumnEnum.LOW_DATE_ORDINAL;
			highValueField = DateIndexTable.ColumnEnum.HIGH_DATE_ORDINAL;
			genericLowerBound = lowerBoundAsOrdinal;
			genericUpperBound = upperBoundAsOrdinal;
		} else {
			lowValueField = DateIndexTable.ColumnEnum.LOW;
			highValueField = DateIndexTable.ColumnEnum.HIGH;
			genericLowerBound = lowerBoundInstant;
			genericUpperBound = upperBoundInstant;
		}

		if (operation == SearchFilterParser.CompareOperation.lt) {
			if (lowerBoundInstant == null) {
				throw new InvalidRequestException("lowerBound value not correctly specified for compare operation");
			}
			//im like 80% sure this should be ub and not lb, as it is an UPPER bound.
			lb = theFrom.createPredicate(lowValueField, ParamPrefixEnum.LESSTHAN, genericLowerBound);
		} else if (operation == SearchFilterParser.CompareOperation.le) {
			if (upperBoundInstant == null) {
				throw new InvalidRequestException("upperBound value not correctly specified for compare operation");
			}
			//im like 80% sure this should be ub and not lb, as it is an UPPER bound.
			lb = theFrom.createPredicate(highValueField, ParamPrefixEnum.LESSTHAN_OR_EQUALS, genericUpperBound);
		} else if (operation == SearchFilterParser.CompareOperation.gt) {
			if (upperBoundInstant == null) {
				throw new InvalidRequestException("upperBound value not correctly specified for compare operation");
			}
			lb = theFrom.createPredicate(highValueField, ParamPrefixEnum.GREATERTHAN, genericUpperBound);
		} else if (operation == SearchFilterParser.CompareOperation.ge) {
			if (lowerBoundInstant == null) {
				throw new InvalidRequestException("lowerBound value not correctly specified for compare operation");
			}
			lb = theFrom.createPredicate(lowValueField, ParamPrefixEnum.GREATERTHAN_OR_EQUALS, genericLowerBound);
		} else if (operation == SearchFilterParser.CompareOperation.ne) {
			if ((lowerBoundInstant == null) ||
				(upperBoundInstant == null)) {
				throw new InvalidRequestException("lowerBound and/or upperBound value not correctly specified for compare operation");
			}
			lt = theFrom.createPredicate(lowValueField, ParamPrefixEnum.LESSTHAN, genericLowerBound);
			gt = theFrom.createPredicate(highValueField, ParamPrefixEnum.GREATERTHAN, genericUpperBound);
			lb = ComboCondition.or(lt, gt);
		} else if ((operation == SearchFilterParser.CompareOperation.eq) || (operation == null)) {
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
				operation.name()));
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
