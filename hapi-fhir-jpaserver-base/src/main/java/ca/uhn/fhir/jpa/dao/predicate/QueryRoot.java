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
import org.apache.commons.lang3.Validate;

import javax.persistence.criteria.*;
import java.util.Date;
import java.util.List;
import java.util.Stack;

public class QueryRoot {
	private final Stack<QueryRootEntry> myQueryRootStack = new Stack<>();
	private final CriteriaBuilder myCriteriaBuilder;
	private boolean myHasIndexJoins;

	public QueryRoot(CriteriaBuilder theCriteriaBuilder) {
		myCriteriaBuilder = theCriteriaBuilder;
	}

	private QueryRootEntry top() {
		Validate.isTrue(!myQueryRootStack.empty());
		return myQueryRootStack.peek();
	}

	public AbstractQuery<Long> pop() {
		QueryRootEntry element = myQueryRootStack.pop();
		return element.pop();
	}

	// FIXME: remove
//	public Root<ResourceTable> getRoot() {
//		return top().getRoot();
//	}

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

	// FIXME: remove
//	<T> Subquery<T> subquery(Class<T> theClass) {
//		return top().subquery(theClass);
//	}

	public boolean hasIndexJoins() {
		return myHasIndexJoins;
	}

	public void setHasIndexJoins() {
		myHasIndexJoins = true;
	}

	public void clearHasIndexJoins() {
		myHasIndexJoins = false;
	}

	<T> From<?, T> createJoin(SearchBuilderJoinEnum theType, String theSearchParameterName) {
		return top().createJoin(theType, theSearchParameterName);
	}


	// FIXME: remove
//	public void setJoinStrategyForCurrentStackTop(IJoinStrategy theJoinStrategy) {
//		top().setJoinStrategy(theJoinStrategy);
//	}

	public void pushResourceTableQuery() {
		myQueryRootStack.push(new QueryRootEntryResourceTable(myCriteriaBuilder, false));
	}

	public void pushResourceTableCountQuery() {
		myQueryRootStack.push(new QueryRootEntryResourceTable(myCriteriaBuilder, true));
	}

	public void pushResourceTableSubQuery() {
		myQueryRootStack.push(new QueryRootEntryResourceTable(myCriteriaBuilder, top()));
	}

	public boolean isEmpty() {
		return myQueryRootStack.isEmpty();
	}

	public void orderBy(List<Order> theOrders) {
		top().orderBy(theOrders);
	}

	public Expression<Date> getLastUpdatedColumn() {
		return top().getLastUpdatedColumn();
	}

	/**
	 * This class should avoid leaking the internal query root, but we need to do so for how composite search params are
	 * currently implemented. These only half work in the first place so I'm not going to worry about the fact that
	 * they rely on a leaky abstraction right now.. But when we get around to implementing composites properly,
	 * let's not continue this. JA 2020-05-12
	 */
	public Root<ResourceTable> getRootForComposite() {
		return top().getRootForComposite();
	}

	public Expression<Long> getResourcePidColumn() {
		return top().getResourcePidColumn();
	}

	public Subquery<Long> subqueryForTagNegation() {
		return top().subqueryForTagNegation();
	}
}
