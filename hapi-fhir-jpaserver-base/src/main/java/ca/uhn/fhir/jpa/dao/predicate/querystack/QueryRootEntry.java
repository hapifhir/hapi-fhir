package ca.uhn.fhir.jpa.dao.predicate.querystack;

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

import ca.uhn.fhir.jpa.dao.predicate.IndexJoins;
import ca.uhn.fhir.jpa.dao.predicate.SearchBuilderJoinEnum;
import ca.uhn.fhir.jpa.dao.predicate.SearchBuilderJoinKey;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamDate;

import javax.persistence.criteria.AbstractQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

abstract class QueryRootEntry {
	private final ArrayList<Predicate> myPredicates = new ArrayList<>();
	private final IndexJoins myIndexJoins = new IndexJoins();
	private final CriteriaBuilder myCriteriaBuilder;
	private boolean myHasImplicitTypeSelection;
	private Map<String, From<?, ResourceIndexedSearchParamDate>> myJoinMap;

	QueryRootEntry(CriteriaBuilder theCriteriaBuilder) {
		myCriteriaBuilder = theCriteriaBuilder;
	}

	boolean isHasImplicitTypeSelection() {
		return myHasImplicitTypeSelection;
	}

	void setHasImplicitTypeSelection(boolean theHasImplicitTypeSelection) {
		myHasImplicitTypeSelection = theHasImplicitTypeSelection;
	}

	Optional<Join<?, ?>> getIndexJoin(SearchBuilderJoinKey theKey) {
		return Optional.ofNullable(myIndexJoins.get(theKey));
	}

	void addPredicate(Predicate thePredicate) {
		myPredicates.add(thePredicate);
	}

	void addPredicates(List<Predicate> thePredicates) {
		myPredicates.addAll(thePredicates);
	}

	Predicate addNeverMatchingPredicate() {
		Predicate predicate = myCriteriaBuilder.equal(getResourcePidColumn(), -1L);
		clearPredicates();
		addPredicate(predicate);
		return predicate;
	}

	Predicate[] getPredicateArray() {
		return myPredicates.toArray(new Predicate[0]);
	}

	void putIndex(SearchBuilderJoinKey theKey, Join<?, ?> theJoin) {
		myIndexJoins.put(theKey, theJoin);
	}

	void clearPredicates() {
		myPredicates.clear();
	}

	List<Predicate> getPredicates() {
		return Collections.unmodifiableList(myPredicates);
	}

	<Y> Path<Y> get(String theAttributeName) {
		return getRoot().get(theAttributeName);
	}

	AbstractQuery<Long> pop() {
		Predicate[] predicateArray = getPredicateArray();
		if (predicateArray.length == 1) {
			getQueryRoot().where(predicateArray[0]);
		} else {
			getQueryRoot().where(myCriteriaBuilder.and(predicateArray));
		}

		return getQueryRoot();
	}

	public Map<String, From<?, ResourceIndexedSearchParamDate>> getJoinMap() {
		Map<String, From<?, ResourceIndexedSearchParamDate>> retVal = myJoinMap;
		if (retVal==null) {
			retVal = new HashMap<>();
			myJoinMap = retVal;
		}
		return retVal;
	}

	abstract void orderBy(List<Order> theOrders);

	abstract Expression<Date> getLastUpdatedColumn();

	abstract <T> From<?, T> createJoin(SearchBuilderJoinEnum theType, String theSearchParameterName);

	abstract AbstractQuery<Long> getQueryRoot();

	abstract Root<?> getRoot();

	abstract Expression<Long> getResourcePidColumn();

	abstract Subquery<Long> subqueryForTagNegation();
}
