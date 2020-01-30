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

import javax.persistence.criteria.*;
import java.util.List;
import java.util.Stack;

public class QueryRoot {
	private final Stack<QueryRootEntry> myQueryRootStack = new Stack<>();
	private boolean myHasIndexJoins;

	public void push(AbstractQuery<Long> theResourceTableQuery) {
		myQueryRootStack.push(new QueryRootEntry(theResourceTableQuery));
	}

	private QueryRootEntry top() {
		return myQueryRootStack.peek();
	}

	void pop() {
		myQueryRootStack.pop();
	}

	public Root<ResourceTable> getRoot() {
		return top().getRoot();
	}

	public <Y> Path<Y> get(String theAttributeName) {
		return top().get(theAttributeName);
	}

	public <Y> Join<ResourceTable, Y> join(String theAttributeName, JoinType theJoinType) {
		return top().join(theAttributeName, theJoinType);
	}

	public Join<?,?> getIndexJoin(SearchBuilderJoinKey theKey) {
		return top().getIndexJoin(theKey);
	}

	public void addPredicate(Predicate thePredicate) {
		top().addPredicate(thePredicate);
	}

	public void addPredicates(List<Predicate> thePredicates) {
		top().addPredicates(thePredicates);
	}

	public Predicate[] getPredicateArray() {
		return top().getPredicateArray();
	}

	void putIndex(SearchBuilderJoinKey theKey, Join<ResourceTable, ResourceIndexedSearchParamDate> theJoin) {
		myHasIndexJoins = true;
		top().putIndex(theKey, theJoin);
	}

	void clearPredicates() {
		top().clearPredicates();
	}

	List<Predicate> getPredicates() {
		return top().getPredicates();
	}

	public void where(Predicate theAnd) {
		top().where(theAnd);
	}

	<T> Subquery<T> subquery(Class<T> theClass) {
		return top().subquery(theClass);
	}

	public boolean hasIndexJoins() {
		return myHasIndexJoins;
	}

	public void setHasIndexJoins(boolean theHasIndexJoins) {
		myHasIndexJoins = true;
	}
}
