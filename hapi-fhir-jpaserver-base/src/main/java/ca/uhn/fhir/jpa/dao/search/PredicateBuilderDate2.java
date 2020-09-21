package ca.uhn.fhir.jpa.dao.search;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.dao.predicate.SearchFilterParser;
import ca.uhn.fhir.jpa.dao.search.sql.DateIndexTable;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import com.healthmarketscience.sqlbuilder.ComboCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Scope("prototype")
public class PredicateBuilderDate2 extends BasePredicateBuilder2 implements IPredicateBuilder2 {
	private static final Logger ourLog = LoggerFactory.getLogger(PredicateBuilderDate2.class);

	private Map<String, DateIndexTable> myJoinMap;

	public PredicateBuilderDate2(SearchBuilder2 theSearchBuilder) {
		super(theSearchBuilder);
	}

	@Override
	public Condition addPredicate(String theResourceName,
											RuntimeSearchParam theSearchParam,
											List<? extends IQueryParameterType> theList,
											SearchFilterParser.CompareOperation theOperation,
											From<?, ResourceLink> theLinkJoin, RequestPartitionId theRequestPartitionId) {

		String paramName = theSearchParam.getName();
		boolean newJoin = false;
		if (myJoinMap == null) {
			myJoinMap = new HashMap<>();
		}
		String key = theResourceName + " " + paramName;

		DateIndexTable join = myJoinMap.get(key);
		if (join == null) {
			join = getSqlBuilder().addDateSelector();
			myJoinMap.put(key, join);
			newJoin = true;
		}

		if (theList.get(0).getMissing() != null) {
			Boolean missing = theList.get(0).getMissing();
			addPredicateParamMissingForNonReference(theResourceName, paramName, missing, join, theRequestPartitionId);
			return null;
		}

		List<Condition> codePredicates = new ArrayList<>();

		for (IQueryParameterType nextOr : theList) {
			Condition p = createPredicateDate(nextOr, myCriteriaBuilder, join, theOperation);
			codePredicates.add(p);
		}

		Condition orPredicates = ComboCondition.or(codePredicates.toArray(new Condition[0]));

		if (newJoin) {
			Condition identityAndValuePredicate = combineParamIndexPredicateWithParamNamePredicate(theResourceName, paramName, join, orPredicates, theRequestPartitionId);
			getSqlBuilder().addPredicate(identityAndValuePredicate);
		} else {
			getSqlBuilder().addPredicate(orPredicates);
		}

		return orPredicates;
	}

	public Condition createPredicateDate(IQueryParameterType theParam,
													 String theResourceName,
													 String theParamName,
													 CriteriaBuilder theBuilder,
													 DateIndexTable theFrom,
													 RequestPartitionId theRequestPartitionId) {
		Condition predicateDate = createPredicateDate(theParam,
			theBuilder,
			theFrom,
			null
		);
		return combineParamIndexPredicateWithParamNamePredicate(theResourceName, theParamName, theFrom, predicateDate, theRequestPartitionId);
	}

	private Condition createPredicateDate(IQueryParameterType theParam,
													  CriteriaBuilder theBuilder,
													  DateIndexTable theFrom,
													  SearchFilterParser.CompareOperation theOperation) {

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
}
