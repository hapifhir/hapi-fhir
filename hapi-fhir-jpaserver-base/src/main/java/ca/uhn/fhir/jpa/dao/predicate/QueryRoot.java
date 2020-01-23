package ca.uhn.fhir.jpa.dao.predicate;

import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamDate;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;

import javax.persistence.criteria.*;
import java.util.List;
import java.util.Stack;

public class QueryRoot {
	private final Stack<QueryRootEntry> myQueryRootStack = new Stack<>();

	public void push(Root<ResourceTable> theResourceTableRoot) {
		myQueryRootStack.push(new QueryRootEntry(theResourceTableRoot));
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
		top().putIndex(theKey, theJoin);
	}

	void clearPredicates() {
		top().clearPredicates();
	}

	// FIXME KHS don't leak
	List<Predicate> getPredicates() {
		return top().getPredicates();
	}
}
