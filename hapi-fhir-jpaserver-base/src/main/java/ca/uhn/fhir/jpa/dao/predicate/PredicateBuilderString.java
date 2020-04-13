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

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.SearchBuilder;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.util.StringNormalizer;
import ca.uhn.fhir.model.api.IPrimitiveDatatype;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
class PredicateBuilderString extends BasePredicateBuilder implements IPredicateBuilder {
	@Autowired
	DaoConfig myDaoConfig;

	PredicateBuilderString(SearchBuilder theSearchBuilder) {
		super(theSearchBuilder);
	}

	@Override
	public Predicate addPredicate(String theResourceName,
											String theParamName,
											List<? extends IQueryParameterType> theList,
											SearchFilterParser.CompareOperation operation) {

		Join<ResourceTable, ResourceIndexedSearchParamString> join = createJoin(SearchBuilderJoinEnum.STRING, theParamName);

		if (theList.get(0).getMissing() != null) {
			addPredicateParamMissing(theResourceName, theParamName, theList.get(0).getMissing(), join);
			return null;
		}

		List<Predicate> codePredicates = new ArrayList<>();
		for (IQueryParameterType nextOr : theList) {
			IQueryParameterType theParameter = nextOr;
			Predicate singleCode = createPredicateString(theParameter,
				theResourceName,
				theParamName,
                    myCriteriaBuilder,
				join,
				operation);
			codePredicates.add(singleCode);
		}

		Predicate retVal = myCriteriaBuilder.or(toArray(codePredicates));
		myQueryRoot.addPredicate(retVal);
		return retVal;
	}

	public Predicate createPredicateString(IQueryParameterType theParameter,
														String theResourceName,
														String theParamName,
														CriteriaBuilder theBuilder,
														From<?, ResourceIndexedSearchParamString> theFrom) {
		return createPredicateString(theParameter,
			theResourceName,
			theParamName,
			theBuilder,
			theFrom,
			null);
	}

	private Predicate createPredicateString(IQueryParameterType theParameter,
														 String theResourceName,
														 String theParamName,
														 CriteriaBuilder theBuilder,
														 From<?, ResourceIndexedSearchParamString> theFrom,
														 SearchFilterParser.CompareOperation operation) {
		String rawSearchTerm;
		if (theParameter instanceof TokenParam) {
			TokenParam id = (TokenParam) theParameter;
			if (!id.isText()) {
				throw new IllegalStateException("Trying to process a text search on a non-text token parameter");
			}
			rawSearchTerm = id.getValue();
		} else if (theParameter instanceof StringParam) {
			StringParam id = (StringParam) theParameter;
			rawSearchTerm = id.getValue();
			if (id.isContains()) {
				if (!myDaoConfig.isAllowContainsSearches()) {
					throw new MethodNotAllowedException(":contains modifier is disabled on this server");
				}
			}
		} else if (theParameter instanceof IPrimitiveDatatype<?>) {
			IPrimitiveDatatype<?> id = (IPrimitiveDatatype<?>) theParameter;
			rawSearchTerm = id.getValueAsString();
		} else {
			throw new IllegalArgumentException("Invalid token type: " + theParameter.getClass());
		}

		if (rawSearchTerm.length() > ResourceIndexedSearchParamString.MAX_LENGTH) {
			throw new InvalidRequestException("Parameter[" + theParamName + "] has length (" + rawSearchTerm.length() + ") that is longer than maximum allowed ("
				+ ResourceIndexedSearchParamString.MAX_LENGTH + "): " + rawSearchTerm);
		}

		if (myDontUseHashesForSearch) {
			String likeExpression = StringNormalizer.normalizeString(rawSearchTerm);
			if (myDaoConfig.isAllowContainsSearches()) {
				if (theParameter instanceof StringParam) {
					if (((StringParam) theParameter).isContains()) {
						likeExpression = createLeftAndRightMatchLikeExpression(likeExpression);
					} else {
						likeExpression = createLeftMatchLikeExpression(likeExpression);
					}
				} else {
					likeExpression = createLeftMatchLikeExpression(likeExpression);
				}
			} else {
				likeExpression = createLeftMatchLikeExpression(likeExpression);
			}

			Predicate singleCode = theBuilder.like(theFrom.get("myValueNormalized").as(String.class), likeExpression);
			if (theParameter instanceof StringParam && ((StringParam) theParameter).isExact()) {
				Predicate exactCode = theBuilder.equal(theFrom.get("myValueExact"), rawSearchTerm);
				singleCode = theBuilder.and(singleCode, exactCode);
			}

			return combineParamIndexPredicateWithParamNamePredicate(theResourceName, theParamName, theFrom, singleCode);
		}
		boolean exactMatch = theParameter instanceof StringParam && ((StringParam) theParameter).isExact();
		if (exactMatch) {
			// Exact match
			Long hash = ResourceIndexedSearchParamString.calculateHashExact(theResourceName, theParamName, rawSearchTerm);
			return theBuilder.equal(theFrom.get("myHashExact").as(Long.class), hash);
		} else {
			// Normalized Match
			String normalizedString = StringNormalizer.normalizeString(rawSearchTerm);
			String likeExpression;
			if ((theParameter instanceof StringParam) &&
				(((((StringParam) theParameter).isContains()) &&
					(myDaoConfig.isAllowContainsSearches())) ||
					(operation == SearchFilterParser.CompareOperation.co))) {
				likeExpression = createLeftAndRightMatchLikeExpression(normalizedString);
			} else if ((operation != SearchFilterParser.CompareOperation.ne) &&
				(operation != SearchFilterParser.CompareOperation.gt) &&
				(operation != SearchFilterParser.CompareOperation.lt) &&
				(operation != SearchFilterParser.CompareOperation.ge) &&
				(operation != SearchFilterParser.CompareOperation.le)) {
				if (operation == SearchFilterParser.CompareOperation.ew) {
					likeExpression = createRightMatchLikeExpression(normalizedString);
				} else {
					likeExpression = createLeftMatchLikeExpression(normalizedString);
				}
			} else {
				likeExpression = normalizedString;
			}

			Predicate predicate;
			if ((operation == null) ||
				(operation == SearchFilterParser.CompareOperation.sw)) {
				Long hash = ResourceIndexedSearchParamString.calculateHashNormalized(myDaoConfig.getModelConfig(), theResourceName, theParamName, normalizedString);
				Predicate hashCode = theBuilder.equal(theFrom.get("myHashNormalizedPrefix").as(Long.class), hash);
				Predicate singleCode = theBuilder.like(theFrom.get("myValueNormalized").as(String.class), likeExpression);
				predicate = theBuilder.and(hashCode, singleCode);
			} else if ((operation == SearchFilterParser.CompareOperation.ew) ||
				(operation == SearchFilterParser.CompareOperation.co)) {
				Predicate singleCode = theBuilder.like(theFrom.get("myValueNormalized").as(String.class), likeExpression);
				predicate = combineParamIndexPredicateWithParamNamePredicate(theResourceName, theParamName, theFrom, singleCode);
			} else if (operation == SearchFilterParser.CompareOperation.eq) {
				Long hash = ResourceIndexedSearchParamString.calculateHashNormalized(myDaoConfig.getModelConfig(), theResourceName, theParamName, normalizedString);
				Predicate hashCode = theBuilder.equal(theFrom.get("myHashNormalizedPrefix").as(Long.class), hash);
				Predicate singleCode = theBuilder.like(theFrom.get("myValueNormalized").as(String.class), normalizedString);
				predicate = theBuilder.and(hashCode, singleCode);
			} else if (operation == SearchFilterParser.CompareOperation.ne) {
				Predicate singleCode = theBuilder.notEqual(theFrom.get("myValueNormalized").as(String.class), likeExpression);
				predicate = combineParamIndexPredicateWithParamNamePredicate(theResourceName, theParamName, theFrom, singleCode);
			} else if (operation == SearchFilterParser.CompareOperation.gt) {
				Predicate singleCode = theBuilder.greaterThan(theFrom.get("myValueNormalized").as(String.class), likeExpression);
				predicate = combineParamIndexPredicateWithParamNamePredicate(theResourceName, theParamName, theFrom, singleCode);
			} else if (operation == SearchFilterParser.CompareOperation.lt) {
				Predicate singleCode = theBuilder.lessThan(theFrom.get("myValueNormalized").as(String.class), likeExpression);
				predicate = combineParamIndexPredicateWithParamNamePredicate(theResourceName, theParamName, theFrom, singleCode);
			} else if (operation == SearchFilterParser.CompareOperation.ge) {
				Predicate singleCode = theBuilder.greaterThanOrEqualTo(theFrom.get("myValueNormalized").as(String.class), likeExpression);
				predicate = combineParamIndexPredicateWithParamNamePredicate(theResourceName, theParamName, theFrom, singleCode);
			} else if (operation == SearchFilterParser.CompareOperation.le) {
				Predicate singleCode = theBuilder.lessThanOrEqualTo(theFrom.get("myValueNormalized").as(String.class), likeExpression);
				predicate = combineParamIndexPredicateWithParamNamePredicate(theResourceName, theParamName, theFrom, singleCode);
			} else {
				throw new IllegalArgumentException("Don't yet know how to handle operation " + operation + " on a string");
			}

			return predicate;
		}
	}
}
