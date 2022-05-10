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
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;

import javax.persistence.criteria.AbstractQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.Date;
import java.util.List;

class QueryRootEntryResourceTable extends QueryRootEntry {

	private final CriteriaBuilder myCriteriaBuilder;
	private final AbstractQuery<Long> myQuery;
	private final SearchParameterMap mySearchParameterMap;
	private final RequestPartitionId myRequestPartitionId;
	private final String myResourceType;

	/**
	 * This method will ddd a predicate to make sure we only include non-deleted resources, and only include
	 * resources of the right type.
	 *
	 * If we have any joins to index tables, we get this behaviour already guaranteed so we don't
	 * need an explicit predicate for it.
	 */
	@Override
	AbstractQuery<Long> pop() {

		if (!isHasImplicitTypeSelection()) {
			if (mySearchParameterMap.getEverythingMode() == null) {
				addPredicate(myCriteriaBuilder.equal(getRoot().get("myResourceType"), myResourceType));
			}
			addPredicate(myCriteriaBuilder.isNull(getRoot().get("myDeleted")));
			if (!myRequestPartitionId.isAllPartitions()) {
				if (!myRequestPartitionId.isDefaultPartition()) {
					addPredicate(getRoot().get("myPartitionIdValue").as(Integer.class).in(myRequestPartitionId.getPartitionIds()));
				} else {
					addPredicate(myCriteriaBuilder.isNull(getRoot().get("myPartitionIdValue").as(Integer.class)));
				}
			}
		}

		return super.pop();
	}

	private final Root<ResourceTable> myResourceTableRoot;

	/**
	 * Root query constructor
	 */
	QueryRootEntryResourceTable(CriteriaBuilder theCriteriaBuilder, boolean theDistinct, boolean theCountQuery, SearchParameterMap theSearchParameterMap, String theResourceType, RequestPartitionId theRequestPartitionId) {
		super(theCriteriaBuilder);
		myCriteriaBuilder = theCriteriaBuilder;
		mySearchParameterMap = theSearchParameterMap;
		myRequestPartitionId = theRequestPartitionId;
		myResourceType = theResourceType;

		CriteriaQuery<Long> query = myCriteriaBuilder.createQuery(Long.class);
		myResourceTableRoot = query.from(ResourceTable.class);

		if (theCountQuery) {
			query.multiselect(myCriteriaBuilder.countDistinct(myResourceTableRoot));
		} else if (theDistinct) {
			query.distinct(true).multiselect(get("myId").as(Long.class));
		} else {
			query.multiselect(get("myId").as(Long.class));
		}
		myQuery = query;
	}

	/**
	 * Subquery constructor
	 */
	QueryRootEntryResourceTable(CriteriaBuilder theCriteriaBuilder, QueryRootEntry theParent, SearchParameterMap theSearchParameterMap, String theResourceType, RequestPartitionId theRequestPartitionId) {
		super(theCriteriaBuilder);
		myCriteriaBuilder = theCriteriaBuilder;
		mySearchParameterMap = theSearchParameterMap;
		myRequestPartitionId = theRequestPartitionId;
		myResourceType = theResourceType;

		AbstractQuery<Long> queryRoot = theParent.getQueryRoot();
		Subquery<Long> query = queryRoot.subquery(Long.class);
		myQuery = query;
		myResourceTableRoot = myQuery.from(ResourceTable.class);
		query.select(myResourceTableRoot.get("myId").as(Long.class));
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
		Join<?,?> join = null;
		switch (theType) {
			case DATE:
				join = myResourceTableRoot.join("myParamsDate", JoinType.LEFT);
				break;
			case NUMBER:
				join = myResourceTableRoot.join("myParamsNumber", JoinType.LEFT);
				break;
			case QUANTITY:
				join = myResourceTableRoot.join("myParamsQuantity", JoinType.LEFT);
				break;
			case REFERENCE:
				join = myResourceTableRoot.join("myResourceLinks", JoinType.LEFT);
				break;
			case STRING:
				join = myResourceTableRoot.join("myParamsString", JoinType.LEFT);
				break;
			case URI:
				join = myResourceTableRoot.join("myParamsUri", JoinType.LEFT);
				break;
			case TOKEN:
				join = myResourceTableRoot.join("myParamsToken", JoinType.LEFT);
				break;
			case COORDS:
				join = myResourceTableRoot.join("myParamsCoords", JoinType.LEFT);
				break;
			case HAS:
				join = myResourceTableRoot.join("myResourceLinksAsTarget", JoinType.LEFT);
				break;
			case PROVENANCE:
				join = myResourceTableRoot.join("myProvenance", JoinType.LEFT);
				break;
			case FORCED_ID:
				join = myResourceTableRoot.join("myForcedId", JoinType.LEFT);
				break;
			case PRESENCE:
				join = myResourceTableRoot.join("mySearchParamPresents", JoinType.LEFT);
				break;
			case COMPOSITE_UNIQUE:
				join = myResourceTableRoot.join("myParamsComboStringUnique", JoinType.LEFT);
				break;
			case RESOURCE_TAGS:
				join = myResourceTableRoot.join("myTags", JoinType.LEFT);
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
	Root<ResourceTable> getRoot() {
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
