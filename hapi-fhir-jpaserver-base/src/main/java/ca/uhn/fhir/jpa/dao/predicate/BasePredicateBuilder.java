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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.dao.IDao;
import ca.uhn.fhir.jpa.dao.SearchBuilder;
import ca.uhn.fhir.jpa.model.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamDate;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.entity.SearchParamPresent;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

abstract class BasePredicateBuilder {
	private static final Logger ourLog = LoggerFactory.getLogger(BasePredicateBuilder.class);
	@Autowired
	FhirContext myContext;
	@Autowired
	DaoConfig myDaoConfig;

	boolean myDontUseHashesForSearch;
	final IDao myCallingDao;
	final CriteriaBuilder myBuilder;
	final QueryRoot myQueryRoot;
	final Class<? extends IBaseResource> myResourceType;
	final String myResourceName;
	final SearchParameterMap myParams;

	BasePredicateBuilder(SearchBuilder theSearchBuilder) {
		myCallingDao = theSearchBuilder.getCallingDao();
		myBuilder = theSearchBuilder.getBuilder();
		myQueryRoot = theSearchBuilder.getQueryRoot();
		myResourceType = theSearchBuilder.getResourceType();
		myResourceName = theSearchBuilder.getResourceName();
		myParams = theSearchBuilder.getParams();
	}

	@PostConstruct
	private void postConstruct() {
		myDontUseHashesForSearch = myDaoConfig.getDisableHashBasedSearches();
	}

	@SuppressWarnings("unchecked")
	<T> Join<ResourceTable, T> createJoin(SearchBuilderJoinEnum theType, String theSearchParameterName) {
		Join<ResourceTable, ResourceIndexedSearchParamDate> join = null;
		switch (theType) {
			case DATE:
				join = myQueryRoot.join("myParamsDate", JoinType.LEFT);
				break;
			case NUMBER:
				join = myQueryRoot.join("myParamsNumber", JoinType.LEFT);
				break;
			case QUANTITY:
				join = myQueryRoot.join("myParamsQuantity", JoinType.LEFT);
				break;
			case REFERENCE:
				join = myQueryRoot.join("myResourceLinks", JoinType.LEFT);
				break;
			case STRING:
				join = myQueryRoot.join("myParamsString", JoinType.LEFT);
				break;
			case URI:
				join = myQueryRoot.join("myParamsUri", JoinType.LEFT);
				break;
			case TOKEN:
				join = myQueryRoot.join("myParamsToken", JoinType.LEFT);
				break;
			case COORDS:
				join = myQueryRoot.join("myParamsCoords", JoinType.LEFT);
				break;
		}

		SearchBuilderJoinKey key = new SearchBuilderJoinKey(theSearchParameterName, theType);
		myQueryRoot.putIndex(key, join);

		return (Join<ResourceTable, T>) join;
	}

	void addPredicateParamMissing(String theResourceName, String theParamName, boolean theMissing) {
//		if (myDontUseHashesForSearch) {
//			Join<ResourceTable, SearchParamPresent> paramPresentJoin = myQueryRoot.join("mySearchParamPresents", JoinType.LEFT);
//			Join<Object, Object> paramJoin = paramPresentJoin.join("mySearchParam", JoinType.LEFT);
//
//			myQueryRoot.addPredicate(myBuilder.equal(paramJoin.get("myResourceName"), theResourceName));
//			myQueryRoot.addPredicate(myBuilder.equal(paramJoin.get("myParamName"), theParamName));
//			myQueryRoot.addPredicate(myBuilder.equal(paramPresentJoin.get("myPresent"), !theMissing));
//		}

		Join<ResourceTable, SearchParamPresent> paramPresentJoin = myQueryRoot.join("mySearchParamPresents", JoinType.LEFT);

		Expression<Long> hashPresence = paramPresentJoin.get("myHashPresence").as(Long.class);
		Long hash = SearchParamPresent.calculateHashPresence(theResourceName, theParamName, !theMissing);
		myQueryRoot.addPredicate(myBuilder.equal(hashPresence, hash));
	}

	void addPredicateParamMissing(String theResourceName, String theParamName, boolean theMissing, Join<ResourceTable, ? extends BaseResourceIndexedSearchParam> theJoin) {

		myQueryRoot.addPredicate(myBuilder.equal(theJoin.get("myResourceType"), theResourceName));
		myQueryRoot.addPredicate(myBuilder.equal(theJoin.get("myParamName"), theParamName));
		myQueryRoot.addPredicate(myBuilder.equal(theJoin.get("myMissing"), theMissing));
	}

	Predicate combineParamIndexPredicateWithParamNamePredicate(String theResourceName, String theParamName, From<?, ? extends BaseResourceIndexedSearchParam> theFrom, Predicate thePredicate) {
		if (myDontUseHashesForSearch) {
			Predicate resourceTypePredicate = myBuilder.equal(theFrom.get("myResourceType"), theResourceName);
			Predicate paramNamePredicate = myBuilder.equal(theFrom.get("myParamName"), theParamName);
			Predicate outerPredicate = myBuilder.and(resourceTypePredicate, paramNamePredicate, thePredicate);
			return outerPredicate;
		}

		long hashIdentity = BaseResourceIndexedSearchParam.calculateHashIdentity(theResourceName, theParamName);
		Predicate hashIdentityPredicate = myBuilder.equal(theFrom.get("myHashIdentity"), hashIdentity);
		return myBuilder.and(hashIdentityPredicate, thePredicate);
	}

	Predicate createPredicateNumeric(String theResourceName,
												String theParamName,
												From<?, ? extends BaseResourceIndexedSearchParam> theFrom,
												CriteriaBuilder builder,
												IQueryParameterType theParam,
												ParamPrefixEnum thePrefix,
												BigDecimal theValue,
												final Expression<BigDecimal> thePath,
												String invalidMessageName) {
		Predicate num;
		// Per discussions with Grahame Grieve and James Agnew on 11/13/19, modified logic for EQUAL and NOT_EQUAL operators below so as to
		//   use exact value matching.  The "fuzz amount" matching is still used with the APPROXIMATE operator.
		switch (thePrefix) {
			case GREATERTHAN:
				num = builder.gt(thePath, theValue);
				break;
			case GREATERTHAN_OR_EQUALS:
				num = builder.ge(thePath, theValue);
				break;
			case LESSTHAN:
				num = builder.lt(thePath, theValue);
				break;
			case LESSTHAN_OR_EQUALS:
				num = builder.le(thePath, theValue);
				break;
			case EQUAL:
				num = builder.equal(thePath, theValue);
				break;
			case NOT_EQUAL:
				num = builder.notEqual(thePath, theValue);
				break;
			case APPROXIMATE:
				BigDecimal mul = SearchFuzzUtil.calculateFuzzAmount(thePrefix, theValue);
				BigDecimal low = theValue.subtract(mul, MathContext.DECIMAL64);
				BigDecimal high = theValue.add(mul, MathContext.DECIMAL64);
				Predicate lowPred;
				Predicate highPred;
				lowPred = builder.ge(thePath.as(BigDecimal.class), low);
				highPred = builder.le(thePath.as(BigDecimal.class), high);
				num = builder.and(lowPred, highPred);
				ourLog.trace("Searching for {} <= val <= {}", low, high);
				break;
			case ENDS_BEFORE:
			case STARTS_AFTER:
			default:
				String msg = myContext.getLocalizer().getMessage(SearchBuilder.class, invalidMessageName, thePrefix.getValue(), theParam.getValueAsQueryToken(myContext));
				throw new InvalidRequestException(msg);
		}

		if (theParamName == null) {
			return num;
		}
		return combineParamIndexPredicateWithParamNamePredicate(theResourceName, theParamName, theFrom, num);
	}

	static String createLeftAndRightMatchLikeExpression(String likeExpression) {
		return "%" + likeExpression.replace("%", "[%]") + "%";
	}

	static String createLeftMatchLikeExpression(String likeExpression) {
		return likeExpression.replace("%", "[%]") + "%";
	}

	static String createRightMatchLikeExpression(String likeExpression) {
		return "%" + likeExpression.replace("%", "[%]");
	}

	static Predicate[] toArray(List<Predicate> thePredicates) {
		return thePredicates.toArray(new Predicate[0]);
	}
}
