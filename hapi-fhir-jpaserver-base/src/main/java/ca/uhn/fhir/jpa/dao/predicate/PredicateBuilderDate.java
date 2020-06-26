package ca.uhn.fhir.jpa.dao.predicate;

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
import ca.uhn.fhir.jpa.dao.SearchBuilder;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamDate;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Scope("prototype")
public class PredicateBuilderDate extends BasePredicateBuilder implements IPredicateBuilder {
	private static final Logger ourLog = LoggerFactory.getLogger(PredicateBuilderDate.class);

	private Map<String, From<?, ResourceIndexedSearchParamDate>> myJoinMap;

	PredicateBuilderDate(SearchBuilder theSearchBuilder) {
		super(theSearchBuilder);
	}

	@Override
	public Predicate addPredicate(String theResourceName,
											RuntimeSearchParam theSearchParam,
											List<? extends IQueryParameterType> theList,
											SearchFilterParser.CompareOperation operation,
											RequestPartitionId theRequestPartitionId) {

		String paramName = theSearchParam.getName();
		boolean newJoin = false;
		if (myJoinMap == null) {
			myJoinMap = new HashMap<>();
		}
		String key = theResourceName + " " + paramName;

		From<?, ResourceIndexedSearchParamDate> join = myJoinMap.get(key);
		if (join == null) {
			join = myQueryStack.createJoin(SearchBuilderJoinEnum.DATE, paramName);
			myJoinMap.put(key, join);
			newJoin = true;
		}

		if (theList.get(0).getMissing() != null) {
			Boolean missing = theList.get(0).getMissing();
			addPredicateParamMissingForNonReference(theResourceName, paramName, missing, join, theRequestPartitionId);
			return null;
		}

		List<Predicate> codePredicates = new ArrayList<>();

		for (IQueryParameterType nextOr : theList) {
			Predicate p = createPredicateDate(nextOr,
				myCriteriaBuilder,
				join,
				operation
			);
			codePredicates.add(p);
		}

		Predicate orPredicates = myCriteriaBuilder.or(toArray(codePredicates));

		if (newJoin) {
			Predicate identityAndValuePredicate = combineParamIndexPredicateWithParamNamePredicate(theResourceName, paramName, join, orPredicates, theRequestPartitionId);
			myQueryStack.addPredicateWithImplicitTypeSelection(identityAndValuePredicate);
		} else {
			myQueryStack.addPredicateWithImplicitTypeSelection(orPredicates);
		}

		return orPredicates;
	}

	public Predicate createPredicateDate(IQueryParameterType theParam,
													 String theResourceName,
													 String theParamName,
													 CriteriaBuilder theBuilder,
													 From<?, ResourceIndexedSearchParamDate> theFrom,
													 RequestPartitionId theRequestPartitionId) {
		Predicate predicateDate = createPredicateDate(theParam,
			theBuilder,
			theFrom,
			null
		);
		return combineParamIndexPredicateWithParamNamePredicate(theResourceName, theParamName, theFrom, predicateDate, theRequestPartitionId);
	}

	private Predicate createPredicateDate(IQueryParameterType theParam,
													  CriteriaBuilder theBuilder,
													  From<?, ResourceIndexedSearchParamDate> theFrom,
													  SearchFilterParser.CompareOperation theOperation) {

		Predicate p;
		if (theParam instanceof DateParam) {
			DateParam date = (DateParam) theParam;
			if (!date.isEmpty()) {
				DateRangeParam range = new DateRangeParam(date);
				p = createPredicateDateFromRange(theBuilder,
					theFrom,
					range,
					theOperation);
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

	private Predicate createPredicateDateFromRange(CriteriaBuilder theBuilder,
																  From<?, ResourceIndexedSearchParamDate> theFrom,
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

		Predicate lt = null;
		Predicate gt = null;
		Predicate lb = null;
		Predicate ub = null;
		String lowValueField;
		String highValueField;

		if (isOrdinalComparison) {
			lowValueField = "myValueLowDateOrdinal";
			highValueField = "myValueHighDateOrdinal";
			genericLowerBound = lowerBoundAsOrdinal;
			genericUpperBound = upperBoundAsOrdinal;
		} else {
			lowValueField = "myValueLow";
			highValueField = "myValueHigh";
			genericLowerBound = lowerBoundInstant;
			genericUpperBound = upperBoundInstant;
		}

		if (operation == SearchFilterParser.CompareOperation.lt) {
			if (lowerBoundInstant == null) {
				throw new InvalidRequestException("lowerBound value not correctly specified for compare operation");
			}
			//im like 80% sure this should be ub and not lb, as it is an UPPER bound.
			lb = theBuilder.lessThan(theFrom.get(lowValueField), genericLowerBound);
		} else if (operation == SearchFilterParser.CompareOperation.le) {
			if (upperBoundInstant == null) {
				throw new InvalidRequestException("upperBound value not correctly specified for compare operation");
			}
			//im like 80% sure this should be ub and not lb, as it is an UPPER bound.
			lb = theBuilder.lessThanOrEqualTo(theFrom.get(highValueField), genericUpperBound);
		} else if (operation == SearchFilterParser.CompareOperation.gt) {
			if (upperBoundInstant == null) {
				throw new InvalidRequestException("upperBound value not correctly specified for compare operation");
			}
			lb = theBuilder.greaterThan(theFrom.get(highValueField), genericUpperBound);
			} else if (operation == SearchFilterParser.CompareOperation.ge) {
				if (lowerBoundInstant == null) {
					throw new InvalidRequestException("lowerBound value not correctly specified for compare operation");
				}
				lb = theBuilder.greaterThanOrEqualTo(theFrom.get(lowValueField), genericLowerBound);
			} else if (operation == SearchFilterParser.CompareOperation.ne) {
			if ((lowerBoundInstant == null) ||
				(upperBoundInstant == null)) {
				throw new InvalidRequestException("lowerBound and/or upperBound value not correctly specified for compare operation");
			}
			lt = theBuilder.lessThan(theFrom.get(lowValueField), genericLowerBound);
			gt = theBuilder.greaterThan(theFrom.get(highValueField), genericUpperBound);
			lb = theBuilder.or(lt,
				gt);
		} else if ((operation == SearchFilterParser.CompareOperation.eq) || (operation == null)) {
			if (lowerBoundInstant != null) {
				gt = theBuilder.greaterThanOrEqualTo(theFrom.get(lowValueField), genericLowerBound);
				lt = theBuilder.greaterThanOrEqualTo(theFrom.get(highValueField), genericLowerBound);
				if (lowerBound.getPrefix() == ParamPrefixEnum.STARTS_AFTER || lowerBound.getPrefix() == ParamPrefixEnum.EQUAL) {
					lb = gt;
				} else {
					lb = theBuilder.or(gt, lt);
				}
			}

			if (upperBoundInstant != null) {
				gt = theBuilder.lessThanOrEqualTo(theFrom.get(lowValueField), genericUpperBound);
				lt = theBuilder.lessThanOrEqualTo(theFrom.get(highValueField), genericUpperBound);


				if (theRange.getUpperBound().getPrefix() == ParamPrefixEnum.ENDS_BEFORE || theRange.getUpperBound().getPrefix() == ParamPrefixEnum.EQUAL) {
					ub = lt;
				} else {
					ub = theBuilder.or(gt, lt);
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
			return (theBuilder.and(lb, ub));
		} else if (lb != null) {
			return (lb);
		} else {
			return (ub);
		}
	}
}
