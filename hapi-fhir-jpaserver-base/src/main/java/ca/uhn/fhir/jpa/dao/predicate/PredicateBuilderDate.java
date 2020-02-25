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

import ca.uhn.fhir.jpa.dao.SearchBuilder;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamDate;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
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
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Scope("prototype")
public class PredicateBuilderDate extends BasePredicateBuilder implements IPredicateBuilder {
	private static final Logger ourLog = LoggerFactory.getLogger(PredicateBuilderDate.class);

	PredicateBuilderDate(SearchBuilder theSearchBuilder) {
		super(theSearchBuilder);
	}

	@Override
	public Predicate addPredicate(String theResourceName,
											String theParamName,
											List<? extends IQueryParameterType> theList,
											SearchFilterParser.CompareOperation operation) {

		Join<ResourceTable, ResourceIndexedSearchParamDate> join = createJoin(SearchBuilderJoinEnum.DATE, theParamName);

		if (theList.get(0).getMissing() != null) {
			Boolean missing = theList.get(0).getMissing();
			addPredicateParamMissing(theResourceName, theParamName, missing, join);
			return null;
		}

		List<Predicate> codePredicates = new ArrayList<>();
		for (IQueryParameterType nextOr : theList) {
			IQueryParameterType params = nextOr;
			Predicate p = createPredicateDate(params,
				theResourceName,
				theParamName,
				myBuilder,
				join,
				operation);
			codePredicates.add(p);
		}

		Predicate orPredicates = myBuilder.or(toArray(codePredicates));
		myQueryRoot.addPredicate(orPredicates);
		return orPredicates;
	}

	public Predicate createPredicateDate(IQueryParameterType theParam,
													 String theResourceName,
													 String theParamName,
													 CriteriaBuilder theBuilder,
													 From<?, ResourceIndexedSearchParamDate> theFrom) {
		return createPredicateDate(theParam,
			theResourceName,
			theParamName,
			theBuilder,
			theFrom,
			null);
	}

	private Predicate createPredicateDate(IQueryParameterType theParam,
													  String theResourceName,
													  String theParamName,
													  CriteriaBuilder theBuilder,
													  From<?, ResourceIndexedSearchParamDate> theFrom,
													  SearchFilterParser.CompareOperation operation) {

		Predicate p;
		if (theParam instanceof DateParam) {
			DateParam date = (DateParam) theParam;
			if (!date.isEmpty()) {
				DateRangeParam range = new DateRangeParam(date);
				p = createPredicateDateFromRange(theBuilder,
					theFrom,
					range,
					operation);
			} else {
				// TODO: handle missing date param?
				p = null;
			}
		} else if (theParam instanceof DateRangeParam) {
			DateRangeParam range = (DateRangeParam) theParam;
			p = createPredicateDateFromRange(theBuilder,
				theFrom,
				range,
				operation);
		} else {
			throw new IllegalArgumentException("Invalid token type: " + theParam.getClass());
		}

		return combineParamIndexPredicateWithParamNamePredicate(theResourceName, theParamName, theFrom, p);
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
		boolean isOrdinalComparison = isNullOrDayPrecision(lowerBound) && isNullOrDayPrecision(upperBound);
		Predicate lt = null;
		Predicate gt = null;
		Predicate lb = null;
		Predicate ub = null;

		if (operation == SearchFilterParser.CompareOperation.lt) {
			if (lowerBoundInstant == null) {
				throw new InvalidRequestException("lowerBound value not correctly specified for compare operation");
			}
			//im like 80% sure this should be ub and not lb, as it is an UPPER bound.
			if (isOrdinalComparison) {
				lb = theBuilder.lessThan(theFrom.get("myValueLowDateOrdinal"), theRange.getLowerBoundAsDateOrdinal());
			} else {
				lb = theBuilder.lessThan(theFrom.get("myValueLow"), lowerBoundInstant);
			}
		} else if (operation == SearchFilterParser.CompareOperation.le) {
			if (upperBoundInstant == null) {
				throw new InvalidRequestException("upperBound value not correctly specified for compare operation");
			}
			//im like 80% sure this should be ub and not lb, as it is an UPPER bound.
			if (isOrdinalComparison) {
				lb = theBuilder.lessThanOrEqualTo(theFrom.get("myValueHighDateOrdinal"), theRange.getUpperBoundAsDateOrdinal());
			} else {
				lb = theBuilder.lessThanOrEqualTo(theFrom.get("myValueHigh"), upperBoundInstant);
			}
		} else if (operation == SearchFilterParser.CompareOperation.gt) {
			if (upperBoundInstant == null) {
				throw new InvalidRequestException("upperBound value not correctly specified for compare operation");
			}
			if (isOrdinalComparison) {
				lb = theBuilder.greaterThan(theFrom.get("myValueHighDateOrdinal"), theRange.getUpperBoundAsDateOrdinal());
			} else {
				lb = theBuilder.greaterThan(theFrom.get("myValueHigh"), upperBoundInstant);
			}
			} else if (operation == SearchFilterParser.CompareOperation.ge) {
			if (lowerBoundInstant == null) {
				throw new InvalidRequestException("lowerBound value not correctly specified for compare operation");
			}
			if (isOrdinalComparison) {
				lb = theBuilder.greaterThanOrEqualTo(theFrom.get("myValueLowDateOrdinal"), theRange.getLowerBoundAsDateOrdinal());
			} else {
				lb = theBuilder.greaterThanOrEqualTo(theFrom.get("myValueLow"), lowerBoundInstant);
			}
		} else if (operation == SearchFilterParser.CompareOperation.ne) {
			if ((lowerBoundInstant == null) ||
				(upperBoundInstant == null)) {
				throw new InvalidRequestException("lowerBound and/or upperBound value not correctly specified for compare operation");
			}
			if (isOrdinalComparison){
				lt = theBuilder.lessThanOrEqualTo(theFrom.get("myValueLowDateOrdinal"), theRange.getLowerBoundAsDateOrdinal());
				gt = theBuilder.greaterThanOrEqualTo(theFrom.get("myValueHighDateOrdinal"), theRange.getUpperBoundAsDateOrdinal());
			} else {
				lt = theBuilder.lessThanOrEqualTo(theFrom.get("myValueLow"), lowerBoundInstant);
				gt = theBuilder.greaterThanOrEqualTo(theFrom.get("myValueHigh"), upperBoundInstant);
			}
			lb = theBuilder.or(lt,
				gt);
		} else if ((operation == SearchFilterParser.CompareOperation.eq) || (operation == null)) {
			if (lowerBoundInstant != null) {
				if (isOrdinalComparison) {
					gt = theBuilder.greaterThanOrEqualTo(theFrom.get("myValueLowDateOrdinal"), theRange.getLowerBoundAsDateOrdinal());
					lt = theBuilder.greaterThanOrEqualTo(theFrom.get("myValueHighDateOrdinal"), theRange.getLowerBoundAsDateOrdinal());
					//also try a strict equality here.
				}
				else {
					gt = theBuilder.greaterThanOrEqualTo(theFrom.get("myValueLow"), lowerBoundInstant);
					lt = theBuilder.greaterThanOrEqualTo(theFrom.get("myValueHigh"), lowerBoundInstant);
				}
				if (lowerBound.getPrefix() == ParamPrefixEnum.STARTS_AFTER || lowerBound.getPrefix() == ParamPrefixEnum.EQUAL) {
					lb = gt;
				} else {
					lb = theBuilder.or(gt, lt);
				}
			}

			if (upperBoundInstant != null) {
				if (isOrdinalComparison) {
					gt = theBuilder.lessThanOrEqualTo(theFrom.get("myValueLowDateOrdinal"), theRange.getUpperBoundAsDateOrdinal());
					lt = theBuilder.lessThanOrEqualTo(theFrom.get("myValueHighDateOrdinal"), theRange.getUpperBoundAsDateOrdinal());
				} else {
					gt = theBuilder.lessThanOrEqualTo(theFrom.get("myValueLow"), upperBoundInstant);
					lt = theBuilder.lessThanOrEqualTo(theFrom.get("myValueHigh"), upperBoundInstant);
				}
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
			ourLog.trace("Ordinal date range is {} - {} ", theRange.getLowerBoundAsDateOrdinal(), theRange.getUpperBoundAsDateOrdinal());
		}
		ourLog.trace("Date range is {} - {}", lowerBoundInstant, upperBoundInstant);

		if (lb != null && ub != null) {
			return (theBuilder.and(lb, ub));
		} else if (lb != null) {
			return (lb);
		} else {
			return (ub);
		}
	}
}
