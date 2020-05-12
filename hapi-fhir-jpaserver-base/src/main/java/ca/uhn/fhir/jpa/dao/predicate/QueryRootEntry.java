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

import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamDate;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;

import javax.persistence.criteria.AbstractQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

abstract class QueryRootEntry {
	private final ArrayList<Predicate> myPredicates = new ArrayList<>();
	private final IndexJoins myIndexJoins = new IndexJoins();

	QueryRootEntry() {
	}

	abstract <Y> Path<Y> get(String theAttributeName);

	abstract <Y> Join<ResourceTable, Y> join(String theAttributeName, JoinType theJoinType);

	Join<?,?> getIndexJoin(SearchBuilderJoinKey theKey) {
		return myIndexJoins.get(theKey);
	}

	void addPredicate(Predicate thePredicate) {
		myPredicates.add(thePredicate);
	}

	void addPredicates(List<Predicate> thePredicates) {
		myPredicates.addAll(thePredicates);
	}

	Predicate[] getPredicateArray() {
		return myPredicates.toArray(new Predicate[0]);
	}

	void putIndex(SearchBuilderJoinKey theKey, Join<ResourceTable, ResourceIndexedSearchParamDate> theJoin) {
		myIndexJoins.put(theKey, theJoin);
	}

	void clearPredicates() {
		myPredicates.clear();
	}

	List<Predicate> getPredicates() {
		return Collections.unmodifiableList(myPredicates);
	}

	// FIXME: remove
//	<T> Subquery<T> subquery(Class<T> theClass) {
//		return myResourceTableQuery.subquery(theClass);
//	}
//
//	<T> From<?,T> createJoin(SearchBuilderJoinEnum theType, String theSearchParameterName) {
//		return myJoinStrategy.createJoin(theType, theSearchParameterName);
//	}

	abstract AbstractQuery<Long> pop();

	abstract void orderBy(List<Order> theOrders);

	abstract Expression<Date> getLastUpdatedColumn();

	abstract <T> From<?,T> createJoin(SearchBuilderJoinEnum theType, String theSearchParameterName);

	abstract AbstractQuery<Long> getQueryRoot();

	abstract Root<ResourceTable> getRootForComposite();

	public abstract Expression<Long> getResourcePidColumn();

	public abstract Subquery<Long> subqueryForTagNegation();
}
