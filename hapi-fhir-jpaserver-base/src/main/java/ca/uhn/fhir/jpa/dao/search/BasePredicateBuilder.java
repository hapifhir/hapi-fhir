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
import ca.uhn.fhir.jpa.dao.predicate.SearchFilterParser;
import ca.uhn.fhir.jpa.dao.predicate.SearchFuzzUtil;
import ca.uhn.fhir.jpa.dao.search.querystack.QueryStack2;
import ca.uhn.fhir.jpa.dao.search.sql.BaseIndexTable;
import ca.uhn.fhir.jpa.dao.search.sql.BaseSearchParamIndexTable;
import ca.uhn.fhir.jpa.dao.search.sql.NumberIndexTable;
import ca.uhn.fhir.jpa.dao.search.sql.SearchParamPresenceTable;
import ca.uhn.fhir.jpa.dao.search.sql.SearchSqlBuilder;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import com.healthmarketscience.sqlbuilder.Condition;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

public abstract class BasePredicateBuilder {
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

		SearchParamPresenceTable join = getSqlBuilder().addSearchParamPresenceSelector();
		addPartitionIdPredicate(theRequestPartitionId, join, null);
		join.addPredicatePresence(theParamName, theMissing);

	}

	void addPredicateParamMissingForNonReference(String theResourceName, String theParamName, boolean theMissing, BaseSearchParamIndexTable theJoin, RequestPartitionId theRequestPartitionId) {
		if (!theRequestPartitionId.isAllPartitions()) {
			theJoin.addPartitionIdPredicate(theRequestPartitionId.getPartitionId());
		}

		theJoin.addPartitionMissing(theResourceName, theParamName, theMissing);
	}

	// FIXME: remove
	void addPredicateParamMissingForNonReference(String theResourceName, String theParamName, boolean theMissing, From<?, ?> theJoin, RequestPartitionId theRequestPartitionId) {
		throw new UnsupportedOperationException();
	}

	Condition combineParamIndexPredicateWithParamNamePredicate(String theResourceName, String theParamName, BaseSearchParamIndexTable theFrom, Condition thePredicate, RequestPartitionId theRequestPartitionId) {
		return theFrom.combineParamIndexPredicateWithParamNamePredicate(theResourceName, theParamName, thePredicate, theRequestPartitionId);
	}

	public PartitionSettings getPartitionSettings() {
		return myPartitionSettings;
	}


	// FIXME: Can this be added to the constructor of the index tables instead since we just call it everywhere anyhow..
	void addPartitionIdPredicate(RequestPartitionId theRequestPartitionId, BaseIndexTable theJoin, List<Predicate> theCodePredicates) {
		if (!theRequestPartitionId.isAllPartitions()) {
			Integer partitionId = theRequestPartitionId.getPartitionId();
			theJoin.addPartitionIdPredicate(partitionId);
		}
	}

	// FIXME: remove
	void addPartitionIdPredicate(RequestPartitionId theRequestPartitionId, From<?, ?> theJoin, List<Predicate> theCodePredicates) {
		throw new UnsupportedOperationException();
	}

	public static String createLeftAndRightMatchLikeExpression(String likeExpression) {
		return "%" + likeExpression.replace("%", "[%]") + "%";
	}

	public static String createLeftMatchLikeExpression(String likeExpression) {
		return likeExpression.replace("%", "[%]") + "%";
	}

	public static String createRightMatchLikeExpression(String likeExpression) {
		return "%" + likeExpression.replace("%", "[%]");
	}

	static Predicate[] toArray(List<Predicate> thePredicates) {
		return thePredicates.toArray(new Predicate[0]);
	}
}
