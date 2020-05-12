package ca.uhn.fhir.jpa.dao.predicate;

import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamDate;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;

import javax.persistence.criteria.AbstractQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.Date;
import java.util.List;

class QueryRootEntryResourceTable extends QueryRootEntry {


	private final CriteriaBuilder myCriteriaBuilder;
	private final AbstractQuery<Long> myQuery;
	private final Root<ResourceTable> myResourceTableRoot;

	/**
	 * Root query constructor
	 */
	QueryRootEntryResourceTable(CriteriaBuilder theCriteriaBuilder, boolean theCountQuery) {
		myCriteriaBuilder = theCriteriaBuilder;

		CriteriaQuery<Long> query = myCriteriaBuilder.createQuery(Long.class);
		myResourceTableRoot = query.from(ResourceTable.class);

		if (theCountQuery) {
			query.multiselect(get("myId").as(Long.class));
		} else {
			query.multiselect(myCriteriaBuilder.countDistinct(myResourceTableRoot));
		}
		myQuery = query;
	}

	/**
	 * Subquery constructor
	 */
	QueryRootEntryResourceTable(CriteriaBuilder theCriteriaBuilder, QueryRootEntry theParent) {
		myCriteriaBuilder = theCriteriaBuilder;

		AbstractQuery<Long> queryRoot = theParent.getQueryRoot();
		myQuery = queryRoot.subquery(Long.class);
		myResourceTableRoot = myQuery.from(ResourceTable.class);
	}


	@Override
	<Y> Path<Y> get(String theAttributeName) {
		return myResourceTableRoot.get(theAttributeName);
	}

	@Override
	<Y> Join<ResourceTable, Y> join(String theAttributeName, JoinType theJoinType) {
		return myResourceTableRoot.join(theAttributeName, theJoinType);
	}

	@Override
	AbstractQuery<Long> pop() {
		Predicate[] predicateArray = getPredicateArray();
		if (predicateArray.length == 1) {
			myQuery.where(predicateArray[0]);
		} else {
			myQuery.where(myCriteriaBuilder.and(predicateArray));
		}

		return myQuery;
	}

	@Override
	void orderBy(List<Order> theOrders) {
		assert myQuery instanceof CriteriaQuery;

		((CriteriaQuery<?>)myQuery).orderBy(theOrders);
	}

	@Override
	Expression<Date> getLastUpdatedColumn() {
		return myResourceTableRoot.get("myUpdated").as(Date.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	<T> From<?, T> createJoin(SearchBuilderJoinEnum theType, String theSearchParameterName) {
		Join<ResourceTable, ResourceIndexedSearchParamDate> join = null;
		switch (theType) {
			case DATE:
				join = join("myParamsDate", JoinType.LEFT);
				break;
			case NUMBER:
				join = join("myParamsNumber", JoinType.LEFT);
				break;
			case QUANTITY:
				join = join("myParamsQuantity", JoinType.LEFT);
				break;
			case REFERENCE:
				join = join("myResourceLinks", JoinType.LEFT);
				break;
			case STRING:
				join = join("myParamsString", JoinType.LEFT);
				break;
			case URI:
				join = join("myParamsUri", JoinType.LEFT);
				break;
			case TOKEN:
				join = join("myParamsToken", JoinType.LEFT);
				break;
			case COORDS:
				join = join("myParamsCoords", JoinType.LEFT);
				break;
		}

		SearchBuilderJoinKey key = new SearchBuilderJoinKey(theSearchParameterName, theType);
		putIndex(key, join);

		return (From<?, T>) join;
	}

	@Override
	AbstractQuery<Long> getQueryRoot() {
		return myQuery;
	}

	@Override
	Root<ResourceTable> getRootForComposite() {
		return myResourceTableRoot;
	}

	@Override
	public Expression<Long> getResourcePidColumn() {
		return myResourceTableRoot.get("myId").as(Long.class);
	}

	@Override
	public Subquery<Long> subqueryForTagNegation() {
		return myQuery.subquery(Long.class);
	}


}
