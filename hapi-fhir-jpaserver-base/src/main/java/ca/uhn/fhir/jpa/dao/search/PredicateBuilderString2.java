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
import ca.uhn.fhir.jpa.dao.predicate.IPredicateBuilder;
import ca.uhn.fhir.jpa.dao.predicate.SearchFilterParser;
import ca.uhn.fhir.jpa.dao.search.sql.SearchSqlBuilder;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.model.api.IPrimitiveDatatype;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import ca.uhn.fhir.util.StringUtil;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.ComboCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
class PredicateBuilderString2 extends BasePredicateBuilder implements IPredicateBuilder {
	PredicateBuilderString2(SearchBuilder2 theSearchBuilder) {
		super(theSearchBuilder);
	}

	@Override
	public Predicate addPredicate(String theResourceName,
											RuntimeSearchParam theSearchParam,
											List<? extends IQueryParameterType> theList,
											SearchFilterParser.CompareOperation theOperation,
											From<?, ResourceLink> theLinkJoin, RequestPartitionId theRequestPartitionId) {

		SearchSqlBuilder.StringIndexTable join = getSqlBuilder().addStringSelector();

		if (theList.get(0).getMissing() != null) {
			addPredicateParamMissingForNonReference(theResourceName, theSearchParam.getName(), theList.get(0).getMissing(), join, theRequestPartitionId);
			return null;
		}

		addPartitionIdPredicate(theRequestPartitionId, join, null);

		List<Condition> codePredicates = new ArrayList<>();
		for (IQueryParameterType nextOr : theList) {
			Condition singleCode = createPredicateString(nextOr, theResourceName, theSearchParam, myCriteriaBuilder, join, theOperation, theRequestPartitionId);
			codePredicates.add(singleCode);
		}

		ComboCondition orCondition = ComboCondition.or(codePredicates.toArray(new Condition[0]));
		getSqlBuilder().addPredicate(orCondition);

		return null;
	}

	public Condition createPredicateString(IQueryParameterType theParameter,
														String theResourceName,
														RuntimeSearchParam theSearchParam,
														CriteriaBuilder theBuilder, // FIXME: remove
														SearchSqlBuilder.StringIndexTable theFrom,
														RequestPartitionId theRequestPartitionId) {
		return createPredicateString(theParameter,
			theResourceName,
			theSearchParam,
			theBuilder,
			theFrom,
			null,
                theRequestPartitionId);
	}

	private Condition createPredicateString(IQueryParameterType theParameter,
														 String theResourceName,
														 RuntimeSearchParam theSearchParam,
														 CriteriaBuilder theBuilder999, // FIXME: remove
														 SearchSqlBuilder.StringIndexTable theFrom,
														 SearchFilterParser.CompareOperation operation,
														 RequestPartitionId theRequestPartitionId) {
		String rawSearchTerm;
		String paramName = theSearchParam.getName();
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
			} else {
				rawSearchTerm = theSearchParam.encode(rawSearchTerm);
			}
		} else if (theParameter instanceof IPrimitiveDatatype<?>) {
			IPrimitiveDatatype<?> id = (IPrimitiveDatatype<?>) theParameter;
			rawSearchTerm = id.getValueAsString();
		} else {
			throw new IllegalArgumentException("Invalid token type: " + theParameter.getClass());
		}

		if (rawSearchTerm.length() > ResourceIndexedSearchParamString.MAX_LENGTH) {
			throw new InvalidRequestException("Parameter[" + paramName + "] has length (" + rawSearchTerm.length() + ") that is longer than maximum allowed ("
				+ ResourceIndexedSearchParamString.MAX_LENGTH + "): " + rawSearchTerm);
		}

		boolean exactMatch = theParameter instanceof StringParam && ((StringParam) theParameter).isExact();
		if (exactMatch) {
			// Exact match
			return theFrom.createPredicateExact(paramName, rawSearchTerm);
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

			Condition predicate;
			if ((operation == null) ||
				(operation == SearchFilterParser.CompareOperation.sw)) {
				predicate = theFrom.createPredicateNormalLike(paramName, normalizedString, likeExpression);
			} else if ((operation == SearchFilterParser.CompareOperation.ew) || (operation == SearchFilterParser.CompareOperation.co)) {
				predicate = theFrom.createPredicateLikeExpressionOnly(paramName, likeExpression, false);
			} else if (operation == SearchFilterParser.CompareOperation.eq) {
				predicate = theFrom.createPredicateNormal(paramName, normalizedString);
			} else if (operation == SearchFilterParser.CompareOperation.ne) {
				predicate = theFrom.createPredicateLikeExpressionOnly(paramName, likeExpression, true);
			} else {
				throw new IllegalArgumentException("Don't yet know how to handle operation " + operation + " on a string");
			}

			return predicate;
		}
	}
}
