package ca.uhn.fhir.jpa.dao.predicate;

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
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.dao.LegacySearchBuilder;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.model.api.IPrimitiveDatatype;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import ca.uhn.fhir.util.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class PredicateBuilderString extends BasePredicateBuilder implements IPredicateBuilder {
	public PredicateBuilderString(LegacySearchBuilder theSearchBuilder) {
		super(theSearchBuilder);
	}

	@Override
	public Predicate addPredicate(String theResourceName,
											RuntimeSearchParam theSearchParam,
											List<? extends IQueryParameterType> theList,
											SearchFilterParser.CompareOperation theOperation,
											RequestPartitionId theRequestPartitionId) {

		From<?, ResourceIndexedSearchParamString> join = myQueryStack.createJoin(SearchBuilderJoinEnum.STRING, theSearchParam.getName());

		if (theList.get(0).getMissing() != null) {
			addPredicateParamMissingForNonReference(theResourceName, theSearchParam.getName(), theList.get(0).getMissing(), join, theRequestPartitionId);
			return null;
		}

		List<Predicate> codePredicates = new ArrayList<>();
		addPartitionIdPredicate(theRequestPartitionId, join, codePredicates);

		for (IQueryParameterType nextOr : theList) {
			Predicate singleCode = createPredicateString(nextOr, theResourceName, theSearchParam, myCriteriaBuilder, join, theOperation, theRequestPartitionId);
			codePredicates.add(singleCode);
		}

		Predicate retVal = myCriteriaBuilder.or(toArray(codePredicates));

		myQueryStack.addPredicateWithImplicitTypeSelection(retVal);

		return retVal;
	}

	public Predicate createPredicateString(IQueryParameterType theParameter,
														String theResourceName,
														RuntimeSearchParam theSearchParam,
														CriteriaBuilder theBuilder,
														From<?, ResourceIndexedSearchParamString> theFrom,
														RequestPartitionId theRequestPartitionId) {
		return createPredicateString(theParameter,
			theResourceName,
			theSearchParam,
			theBuilder,
			theFrom,
			null,
                theRequestPartitionId);
	}

	private Predicate createPredicateString(IQueryParameterType theParameter,
														 String theResourceName,
														 RuntimeSearchParam theSearchParam,
														 CriteriaBuilder theBuilder,
														 From<?, ResourceIndexedSearchParamString> theFrom,
														 SearchFilterParser.CompareOperation operation,
														 RequestPartitionId theRequestPartitionId) {
		String rawSearchTerm;
		String paramName = theSearchParam.getName();
		if (theParameter instanceof TokenParam) {
			TokenParam id = (TokenParam) theParameter;
			if (!id.isText()) {
				throw new IllegalStateException(Msg.code(1047) + "Trying to process a text search on a non-text token parameter");
			}
			rawSearchTerm = id.getValue();
		} else if (theParameter instanceof StringParam) {
			StringParam id = (StringParam) theParameter;
			rawSearchTerm = id.getValue();
			if (id.isContains()) {
				if (!myDaoConfig.isAllowContainsSearches()) {
					throw new MethodNotAllowedException(Msg.code(1048) + ":contains modifier is disabled on this server");
				}
			} else {
				rawSearchTerm = theSearchParam.encode(rawSearchTerm);
			}
		} else if (theParameter instanceof IPrimitiveDatatype<?>) {
			IPrimitiveDatatype<?> id = (IPrimitiveDatatype<?>) theParameter;
			rawSearchTerm = id.getValueAsString();
		} else {
			throw new IllegalArgumentException(Msg.code(1049) + "Invalid token type: " + theParameter.getClass());
		}

		if (rawSearchTerm.length() > ResourceIndexedSearchParamString.MAX_LENGTH) {
			throw new InvalidRequestException(Msg.code(1050) + "Parameter[" + paramName + "] has length (" + rawSearchTerm.length() + ") that is longer than maximum allowed ("
				+ ResourceIndexedSearchParamString.MAX_LENGTH + "): " + rawSearchTerm);
		}

		if (myDontUseHashesForSearch) {
			String likeExpression = StringUtil.normalizeStringForSearchIndexing(rawSearchTerm);
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

			return combineParamIndexPredicateWithParamNamePredicate(theResourceName, paramName, theFrom, singleCode, theRequestPartitionId);
		}
		boolean exactMatch = theParameter instanceof StringParam && ((StringParam) theParameter).isExact();
		if (exactMatch) {
			// Exact match
			Long hash = ResourceIndexedSearchParamString.calculateHashExact(getPartitionSettings(), theRequestPartitionId, theResourceName, paramName, rawSearchTerm);
			return theBuilder.equal(theFrom.get("myHashExact").as(Long.class), hash);
		} else {
			// Normalized Match
			String normalizedString = StringUtil.normalizeStringForSearchIndexing(rawSearchTerm);
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
				Long hash = ResourceIndexedSearchParamString.calculateHashNormalized(getPartitionSettings(), theRequestPartitionId, myDaoConfig.getModelConfig(), theResourceName, paramName, normalizedString);
				Predicate hashCode = theBuilder.equal(theFrom.get("myHashNormalizedPrefix").as(Long.class), hash);
				Predicate singleCode = theBuilder.like(theFrom.get("myValueNormalized").as(String.class), likeExpression);
				predicate = theBuilder.and(hashCode, singleCode);
			} else if ((operation == SearchFilterParser.CompareOperation.ew) ||
				(operation == SearchFilterParser.CompareOperation.co)) {
				Predicate singleCode = theBuilder.like(theFrom.get("myValueNormalized").as(String.class), likeExpression);
				predicate = combineParamIndexPredicateWithParamNamePredicate(theResourceName, paramName, theFrom, singleCode, theRequestPartitionId);
			} else if (operation == SearchFilterParser.CompareOperation.eq) {
				Long hash = ResourceIndexedSearchParamString.calculateHashNormalized(getPartitionSettings(), theRequestPartitionId, myDaoConfig.getModelConfig(), theResourceName, paramName, normalizedString);
				Predicate hashCode = theBuilder.equal(theFrom.get("myHashNormalizedPrefix").as(Long.class), hash);
				Predicate singleCode = theBuilder.like(theFrom.get("myValueNormalized").as(String.class), normalizedString);
				predicate = theBuilder.and(hashCode, singleCode);
			} else if (operation == SearchFilterParser.CompareOperation.ne) {
				Predicate singleCode = theBuilder.notEqual(theFrom.get("myValueNormalized").as(String.class), likeExpression);
				predicate = combineParamIndexPredicateWithParamNamePredicate(theResourceName, paramName, theFrom, singleCode, theRequestPartitionId);
			} else if (operation == SearchFilterParser.CompareOperation.gt) {
				Predicate singleCode = theBuilder.greaterThan(theFrom.get("myValueNormalized").as(String.class), likeExpression);
				predicate = combineParamIndexPredicateWithParamNamePredicate(theResourceName, paramName, theFrom, singleCode, theRequestPartitionId);
			} else if (operation == SearchFilterParser.CompareOperation.lt) {
				Predicate singleCode = theBuilder.lessThan(theFrom.get("myValueNormalized").as(String.class), likeExpression);
				predicate = combineParamIndexPredicateWithParamNamePredicate(theResourceName, paramName, theFrom, singleCode, theRequestPartitionId);
			} else if (operation == SearchFilterParser.CompareOperation.ge) {
				Predicate singleCode = theBuilder.greaterThanOrEqualTo(theFrom.get("myValueNormalized").as(String.class), likeExpression);
				predicate = combineParamIndexPredicateWithParamNamePredicate(theResourceName, paramName, theFrom, singleCode, theRequestPartitionId);
			} else if (operation == SearchFilterParser.CompareOperation.le) {
				Predicate singleCode = theBuilder.lessThanOrEqualTo(theFrom.get("myValueNormalized").as(String.class), likeExpression);
				predicate = combineParamIndexPredicateWithParamNamePredicate(theResourceName, paramName, theFrom, singleCode, theRequestPartitionId);
			} else {
				throw new IllegalArgumentException(Msg.code(1051) + "Don't yet know how to handle operation " + operation + " on a string");
			}

			return predicate;
		}
	}
}
