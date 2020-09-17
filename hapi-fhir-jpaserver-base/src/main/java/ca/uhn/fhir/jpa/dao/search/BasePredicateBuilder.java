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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.SearchBuilder;
import ca.uhn.fhir.jpa.dao.predicate.SearchBuilderJoinEnum;
import ca.uhn.fhir.jpa.dao.predicate.SearchFuzzUtil;
import ca.uhn.fhir.jpa.dao.search.querystack.QueryStack2;
import ca.uhn.fhir.jpa.dao.search.sql.SearchSqlBuilder;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.jpa.model.entity.SearchParamPresent;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;

abstract class BasePredicateBuilder {
	private static final Logger ourLog = LoggerFactory.getLogger(BasePredicateBuilder.class);
	final CriteriaBuilder myCriteriaBuilder;
	final QueryStack2 myQueryStack;
	final Class<? extends IBaseResource> myResourceType;
	final String myResourceName;
	final SearchParameterMap myParams;
	@Autowired
	FhirContext myContext;
	@Autowired
	DaoConfig myDaoConfig;
	@Autowired
	private PartitionSettings myPartitionSettings;
	private SearchSqlBuilder mySearchSqlBuilder;

	BasePredicateBuilder(SearchBuilder2 theSearchBuilder) {
		myCriteriaBuilder = theSearchBuilder.getBuilder();
		myQueryStack = theSearchBuilder.getQueryStack();
		myResourceType = theSearchBuilder.getResourceType();
		myResourceName = theSearchBuilder.getResourceName();
		myParams = theSearchBuilder.getParams();
		mySearchSqlBuilder = theSearchBuilder.getSearchSqlBuilder();
		assert mySearchSqlBuilder != null;
	}

	public SearchSqlBuilder getSqlBuilder() {
		return mySearchSqlBuilder;
	}

	void addPredicateParamMissingForReference(String theResourceName, String theParamName, boolean theMissing, RequestPartitionId theRequestPartitionId) {

		SearchSqlBuilder.SearchParamPresenceTable join = getSqlBuilder().addSearchParamPresenceSelector();
		addPartitionIdPredicate(theRequestPartitionId, join, null);
		join.addPredicatePresence(theParamName, theMissing);

	}

	void addPredicateParamMissingForNonReference(String theResourceName, String theParamName, boolean theMissing, SearchSqlBuilder.BaseSearchParamIndexTable theJoin, RequestPartitionId theRequestPartitionId) {
		if (!theRequestPartitionId.isAllPartitions()) {
			theJoin.addPartitionIdPredicate(theRequestPartitionId.getPartitionId());
		}

		theJoin.addPartitionMissing(theResourceName, theParamName, theMissing);
	}

	// FIXME: remove
	void addPredicateParamMissingForNonReference(String theResourceName, String theParamName, boolean theMissing, From<?,?> theJoin, RequestPartitionId theRequestPartitionId) {
		throw new UnsupportedOperationException();
	}

	Predicate combineParamIndexPredicateWithParamNamePredicate(String theResourceName, String theParamName, From<?, ? extends BaseResourceIndexedSearchParam> theFrom, Predicate thePredicate, RequestPartitionId theRequestPartitionId) {
		List<Predicate> andPredicates = new ArrayList<>();
		addPartitionIdPredicate(theRequestPartitionId, theFrom, andPredicates);

		long hashIdentity = BaseResourceIndexedSearchParam.calculateHashIdentity(myPartitionSettings, theRequestPartitionId, theResourceName, theParamName);
		Predicate hashIdentityPredicate = myCriteriaBuilder.equal(theFrom.get("myHashIdentity"), hashIdentity);
		andPredicates.add(hashIdentityPredicate);
		andPredicates.add(thePredicate);

		return myCriteriaBuilder.and(toArray(andPredicates));
	}

	public PartitionSettings getPartitionSettings() {
		return myPartitionSettings;
	}

	Predicate createPredicateNumeric(String theResourceName,
												String theParamName,
												From<?, ? extends BaseResourceIndexedSearchParam> theFrom,
												CriteriaBuilder builder,
												IQueryParameterType theParam,
												ParamPrefixEnum thePrefix,
												BigDecimal theValue,
												final Expression<BigDecimal> thePath,
												String invalidMessageName, RequestPartitionId theRequestPartitionId) {
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
		return combineParamIndexPredicateWithParamNamePredicate(theResourceName, theParamName, theFrom, num, theRequestPartitionId);
	}

	// FIXME: get rid of unused 3rd param
	void addPartitionIdPredicate(RequestPartitionId theRequestPartitionId, SearchSqlBuilder.BaseIndexTable theJoin, List<Predicate> theCodePredicates) {
		if (!theRequestPartitionId.isAllPartitions()) {
			Integer partitionId = theRequestPartitionId.getPartitionId();
			theJoin.addPartitionIdPredicate(partitionId);
		}
	}

	// FIXME: remove
	void addPartitionIdPredicate(RequestPartitionId theRequestPartitionId, From<?,?> theJoin, List<Predicate> theCodePredicates) {
		throw new UnsupportedOperationException();
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
