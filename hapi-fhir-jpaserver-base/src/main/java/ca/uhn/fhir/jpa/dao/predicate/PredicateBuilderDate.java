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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Scope("prototype")
public class PredicateBuilderDate extends BasePredicateBuilder implements IPredicateBuilder {
	private static final Logger ourLog = LoggerFactory.getLogger(PredicateBuilderDate.class);

	private Map<String, Join<ResourceTable, ResourceIndexedSearchParamDate>> myJoinMap;

	PredicateBuilderDate(SearchBuilder theSearchBuilder) {
		super(theSearchBuilder);
	}

	@Override
	public Predicate addPredicate(String theResourceName,
											String theParamName,
											List<? extends IQueryParameterType> theList,
											SearchFilterParser.CompareOperation operation) {

		boolean newJoin = false;
		if (myJoinMap == null) {
			myJoinMap = new HashMap<>();
		}
		String key = theResourceName + " " + theParamName;

		Join<ResourceTable, ResourceIndexedSearchParamDate> join = myJoinMap.get(key);
		if (join == null) {
			join = createJoin(SearchBuilderJoinEnum.DATE, theParamName);
			myJoinMap.put(key, join);
			newJoin = true;
		}

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
				myCriteriaBuilder,
				join,
				operation);
			codePredicates.add(p);
		}

		Predicate orPredicates = myCriteriaBuilder.or(toArray(codePredicates));

		if (newJoin) {
			Predicate identityAndValuePredicate = combineParamIndexPredicateWithParamNamePredicate(theResourceName, theParamName, join, orPredicates);
			myQueryRoot.addPredicate(identityAndValuePredicate);
		} else {
			myQueryRoot.addPredicate(orPredicates);
		}

		return orPredicates;
	}

	public Predicate createPredicateDate(IQueryParameterType theParam,
													 String theResourceName,
													 String theParamName,
													 CriteriaBuilder theBuilder,
													 From<?, ResourceIndexedSearchParamDate> theFrom) {
		Predicate predicateDate = createPredicateDate(theParam,
			theResourceName,
			theParamName,
			theBuilder,
			theFrom,
			null);
		return combineParamIndexPredicateWithParamNamePredicate(theResourceName, theParamName, theFrom, predicateDate);
	}

	private Predicate createPredicateDate(IQueryParameterType theParam,
													  String theResourceName,
													  String theParamName,
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

	private Predicate createPredicateDateFromRange(CriteriaBuilder theBuilder,
																  From<?, ResourceIndexedSearchParamDate> theFrom,
																  DateRangeParam theRange,
																  SearchFilterParser.CompareOperation operation) {
		Date lowerBound = theRange.getLowerBoundAsInstant();
		Date upperBound = theRange.getUpperBoundAsInstant();
		Predicate lt = null;
		Predicate gt = null;
		Predicate lb = null;
		Predicate ub = null;

		if (operation == SearchFilterParser.CompareOperation.lt) {
			if (lowerBound == null) {
				throw new InvalidRequestException("lowerBound value not correctly specified for compare operation");
			}
			lb = theBuilder.lessThan(theFrom.get("myValueLow"), lowerBound);
		} else if (operation == SearchFilterParser.CompareOperation.le) {
			if (upperBound == null) {
				throw new InvalidRequestException("upperBound value not correctly specified for compare operation");
			}
			lb = theBuilder.lessThanOrEqualTo(theFrom.get("myValueHigh"), upperBound);
		} else if (operation == SearchFilterParser.CompareOperation.gt) {
			if (upperBound == null) {
				throw new InvalidRequestException("upperBound value not correctly specified for compare operation");
			}
			lb = theBuilder.greaterThan(theFrom.get("myValueHigh"), upperBound);
		} else if (operation == SearchFilterParser.CompareOperation.ge) {
			if (lowerBound == null) {
				throw new InvalidRequestException("lowerBound value not correctly specified for compare operation");
			}
			lb = theBuilder.greaterThanOrEqualTo(theFrom.get("myValueLow"), lowerBound);
		} else if (operation == SearchFilterParser.CompareOperation.ne) {
			if ((lowerBound == null) ||
				(upperBound == null)) {
				throw new InvalidRequestException("lowerBound and/or upperBound value not correctly specified for compare operation");
			}
			/*Predicate*/
			lt = theBuilder.lessThanOrEqualTo(theFrom.get("myValueLow"), lowerBound);
			/*Predicate*/
			gt = theBuilder.greaterThanOrEqualTo(theFrom.get("myValueHigh"), upperBound);
			lb = theBuilder.or(lt,
				gt);
		} else if ((operation == SearchFilterParser.CompareOperation.eq) ||
			(operation == null)) {
			if (lowerBound != null) {
				/*Predicate*/
				gt = theBuilder.greaterThanOrEqualTo(theFrom.get("myValueLow"), lowerBound);
				/*Predicate*/
				lt = theBuilder.greaterThanOrEqualTo(theFrom.get("myValueHigh"), lowerBound);
				if (theRange.getLowerBound().getPrefix() == ParamPrefixEnum.STARTS_AFTER || theRange.getLowerBound().getPrefix() == ParamPrefixEnum.EQUAL) {
					lb = gt;
				} else {
					lb = theBuilder.or(gt, lt);
				}
			}

			if (upperBound != null) {
				/*Predicate*/
				gt = theBuilder.lessThanOrEqualTo(theFrom.get("myValueLow"), upperBound);
				/*Predicate*/
				lt = theBuilder.lessThanOrEqualTo(theFrom.get("myValueHigh"), upperBound);
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

		ourLog.trace("Date range is {} - {}", lowerBound, upperBound);

		if (lb != null && ub != null) {
			return (theBuilder.and(lb, ub));
		} else if (lb != null) {
			return (lb);
		} else {
			return (ub);
		}
	}
}
