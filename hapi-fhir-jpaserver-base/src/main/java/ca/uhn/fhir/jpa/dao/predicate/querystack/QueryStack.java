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

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.dao.predicate.SearchBuilderJoinEnum;
import ca.uhn.fhir.jpa.dao.predicate.SearchBuilderJoinKey;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamDate;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import org.apache.commons.lang3.Validate;

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
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * This class represents a SQL SELECT statement that is selecting for resource PIDs, ie.
 * the <code>RES_ID</code> column on the <code>HFJ_RESOURCE</code> ({@link ca.uhn.fhir.jpa.model.entity.ResourceTable})
 * table.
 * <p>
 * We add predicates (WHERE A=B) to it, and can join other tables to it as well. At the root of the query
 * we are typically doing a <code>select RES_ID from HFJ_RESOURCE where (....)</code> and this class
 * is used to build the <i>where</i> clause. In the case of subqueries though, we may be performing a
 * select on a different table since many tables have a column with a FK dependency on RES_ID.
 * </p>
 */
public class QueryStack {

	private final Stack<QueryRootEntry> myQueryRootStack = new Stack<>();
	private final CriteriaBuilder myCriteriaBuilder;
	private final SearchParameterMap mySearchParameterMap;
	private final RequestPartitionId myRequestPartitionId;
	private final String myResourceType;

	/**
	 * Constructor
	 */
	public QueryStack(CriteriaBuilder theCriteriaBuilder, String theResourceType, SearchParameterMap theSearchParameterMap, RequestPartitionId theRequestPartitionId) {
		assert theCriteriaBuilder != null;
		assert isNotBlank(theResourceType);
		assert theSearchParameterMap != null;
		assert theRequestPartitionId != null;
		myCriteriaBuilder = theCriteriaBuilder;
		mySearchParameterMap = theSearchParameterMap;
		myRequestPartitionId = theRequestPartitionId;
		myResourceType = theResourceType;
	}

	/**
	 * Add a new <code>select RES_ID from HFJ_RESOURCE</code> to the stack. All predicates added to the {@literal QueryRootStack}
	 * will be added to this select clause until {@link #pop()} is called.
	 * <p>
	 * This method must only be called when the stack is empty.
	 * </p>
	 */
	public void pushResourceTableQuery() {
		assert myQueryRootStack.isEmpty();
		myQueryRootStack.push(new QueryRootEntryResourceTable(myCriteriaBuilder, false, false, mySearchParameterMap, myResourceType, myRequestPartitionId));
	}

	/**
	 * Add a new <code>select DISTINCT RES_ID from HFJ_RESOURCE</code> to the stack. All predicates added to the {@literal QueryRootStack}
	 * will be added to this select clause until {@link #pop()} is called.
	 * <p>
	 * This method must only be called when the stack is empty.
	 * </p>
	 */
	public void pushResourceTableDistinctQuery() {
		assert myQueryRootStack.isEmpty();
		myQueryRootStack.push(new QueryRootEntryResourceTable(myCriteriaBuilder, true, false, mySearchParameterMap, myResourceType, myRequestPartitionId));
	}

	/**
	 * Add a new <code>select count(RES_ID) from HFJ_RESOURCE</code> to the stack. All predicates added to the {@literal QueryRootStack}
	 * will be added to this select clause until {@link #pop()} is called.
	 * <p>
	 * This method must only be called when the stack is empty.
	 * </p>
	 */
	public void pushResourceTableCountQuery() {
		assert myQueryRootStack.isEmpty();
		myQueryRootStack.push(new QueryRootEntryResourceTable(myCriteriaBuilder, false, true, mySearchParameterMap, myResourceType, myRequestPartitionId));
	}

	/**
	 * Add a new <code>select RES_ID from HFJ_RESOURCE</code> to the stack. All predicates added to the {@literal QueryRootStack}
	 * will be added to this select clause until {@link #pop()} is called.
	 * <p>
	 * This method must only be called when the stack is NOT empty.
	 * </p>
	 */
	public void pushResourceTableSubQuery(String theResourceType) {
		assert !myQueryRootStack.isEmpty();
		myQueryRootStack.push(new QueryRootEntryResourceTable(myCriteriaBuilder, top(), mySearchParameterMap, theResourceType, myRequestPartitionId));
	}

	/**
	 * Add a new <code>select RES_ID from (....)</code> to the stack, where the specific table being selected on will be
	 * determined based on the first call to {@link #createJoin(SearchBuilderJoinEnum, String)}. All predicates added
	 * to the {@literal QueryRootStack} will be added to this select clause until {@link #pop()} is called.
	 * <p>
	 * This method must only be called when the stack is NOT empty.
	 * </p>
	 */
	public void pushIndexTableSubQuery() {
		assert !myQueryRootStack.isEmpty();
		myQueryRootStack.push(new QueryRootEntryIndexTable(myCriteriaBuilder, top()));
	}

	/**
	 * This method must be called once all predicates have been added
	 */
	public AbstractQuery<Long> pop() {
		QueryRootEntry element = myQueryRootStack.pop();
		return element.pop();
	}

	/**
	 * Creates a new SQL join from the current select statement to another table, using the resource PID as the
	 * joining key
	 */
	public <T> From<?, T> createJoin(SearchBuilderJoinEnum theType, String theSearchParameterName) {
		return top().createJoin(theType, theSearchParameterName);
	}

	/**
	 * Returns a join that was previously created by a call to {@link #createJoin(SearchBuilderJoinEnum, String)},
	 * if one exists for the given key.
	 */
	public Optional<Join<?, ?>> getExistingJoin(SearchBuilderJoinKey theKey) {
		return top().getIndexJoin(theKey);
	}

	/**
	 * Gets an attribute (aka a column) from the current select statement.
	 *
	 * @param theAttributeName Must be the name of a java field for the entity/table being selected on
	 */
	public <Y> Path<Y> get(String theAttributeName) {
		return top().get(theAttributeName);
	}

	/**
	 * Adds a predicate to the current select statement
	 */
	public void addPredicate(Predicate thePredicate) {
		top().addPredicate(thePredicate);
	}

	/**
	 * Adds a predicate and marks it as having implicit type selection in it. In other words, call this method if a
	 * this predicate will ensure:
	 * <ul>
	 *    <li>Only Resource PIDs for the correct resource type will be selected</li>
	 *    <li>Only Resource PIDs for non-deleted resources will be selected</li>
	 * </ul>
	 * Setting this flag is a performance optimization, since it avoids the need for us to explicitly
	 * add predicates for the two conditions above.
	 */
	public void addPredicateWithImplicitTypeSelection(Predicate thePredicate) {
		setHasImplicitTypeSelection();
		addPredicate(thePredicate);
	}

	/**
	 * Adds predicates and marks them as having implicit type selection in it. In other words, call this method if a
	 * this predicate will ensure:
	 * <ul>
	 *    <li>Only Resource PIDs for the correct resource type will be selected</li>
	 *    <li>Only Resource PIDs for non-deleted resources will be selected</li>
	 * </ul>
	 * Setting this flag is a performance optimization, since it avoids the need for us to explicitly
	 * add predicates for the two conditions above.
	 */
	public void addPredicatesWithImplicitTypeSelection(List<Predicate> thePredicates) {
		setHasImplicitTypeSelection();
		addPredicates(thePredicates);
	}

	/**
	 * Adds predicate(s) to the current select statement
	 */
	public void addPredicates(List<Predicate> thePredicates) {
		top().addPredicates(thePredicates);
	}

	/**
	 * Clear all predicates from the current select statement
	 */
	public void clearPredicates() {
		top().clearPredicates();
	}

	/**
	 * Fetch all the current predicates
	 * <p>
	 * TODO This should really be package protected, but it is called externally in one spot - We need to clean that up
	 * at some point.
	 */
	public List<Predicate> getPredicates() {
		return top().getPredicates();
	}

	private void setHasImplicitTypeSelection() {
		top().setHasImplicitTypeSelection(true);
	}

	/**
	 * @see #setHasImplicitTypeSelection()
	 */
	public void clearHasImplicitTypeSelection() {
		top().setHasImplicitTypeSelection(false);
	}

	public boolean isEmpty() {
		return myQueryRootStack.isEmpty();
	}

	/**
	 * Add an SQL <code>order by</code> expression
	 */
	public void orderBy(List<Order> theOrders) {
		top().orderBy(theOrders);
	}

	/**
	 * Fetch the column for the current table root that corresponds to the resource's lastUpdated time
	 */
	public Expression<Date> getLastUpdatedColumn() {
		return top().getLastUpdatedColumn();
	}

	/**
	 * Fetch the column for the current table root that corresponds to the resource's PID
	 */
	public Expression<Long> getResourcePidColumn() {
		return top().getResourcePidColumn();
	}

	public Subquery<Long> subqueryForTagNegation() {
		return top().subqueryForTagNegation();
	}

	private QueryRootEntry top() {
		Validate.isTrue(!myQueryRootStack.empty());
		return myQueryRootStack.peek();
	}

	/**
	 * TODO This class should avoid leaking the internal query root, but we need to do so for how composite search params are
	 * currently implemented. These only half work in the first place so I'm not going to worry about the fact that
	 * they rely on a leaky abstraction right now.. But when we get around to implementing composites properly,
	 * let's not continue this. JA 2020-05-12
	 */
	public Root<?> getRootForComposite() {
		return top().getRoot();
	}


	/**
	 * Add a predicate that will never match any resources
	 */
	public Predicate addNeverMatchingPredicate() {
		return top().addNeverMatchingPredicate();
	}

	public Map<String, From<?, ResourceIndexedSearchParamDate>> getJoinMap() {
		return top().getJoinMap();
	}

}
