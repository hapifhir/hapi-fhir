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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.dao.predicate.SearchBuilderJoinEnum;
import ca.uhn.fhir.jpa.model.entity.BaseResourceIndex;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamCoords;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamDate;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamNumber;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamQuantity;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamUri;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import org.apache.commons.lang3.Validate;

import javax.persistence.criteria.AbstractQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.Date;
import java.util.List;

public class QueryRootEntryIndexTable extends QueryRootEntry {
	private final Subquery<Long> myQuery;
	private Root<? extends BaseResourceIndex> myRoot;
	private SearchBuilderJoinEnum myParamType;
	private Expression<Long> myResourcePidColumn;

	public QueryRootEntryIndexTable(CriteriaBuilder theCriteriaBuilder, QueryRootEntry theParent) {
		super(theCriteriaBuilder);

		AbstractQuery<Long> queryRoot = theParent.getQueryRoot();
		myQuery = queryRoot.subquery(Long.class);
	}

	@Override
	void orderBy(List<Order> theOrders) {
		throw new IllegalStateException(Msg.code(1072));
	}

	@Override
	Expression<Date> getLastUpdatedColumn() {
		return getRoot().get("myUpdated").as(Date.class);
	}

	@Override
	<T> From<?, T> createJoin(SearchBuilderJoinEnum theType, String theSearchParameterName) {
		if (myParamType == null) {
			switch (theType) {
				case REFERENCE:
					myRoot = myQuery.from(ResourceLink.class);
					myResourcePidColumn = myRoot.get("mySourceResourcePid").as(Long.class);
					myParamType = SearchBuilderJoinEnum.REFERENCE;
					break;
				case NUMBER:
					myRoot = myQuery.from(ResourceIndexedSearchParamNumber.class);
					myResourcePidColumn = myRoot.get("myResourcePid").as(Long.class);
					myParamType = SearchBuilderJoinEnum.NUMBER;
					break;
				case DATE:
					myRoot = myQuery.from(ResourceIndexedSearchParamDate.class);
					myResourcePidColumn = myRoot.get("myResourcePid").as(Long.class);
					myParamType = SearchBuilderJoinEnum.DATE;
					break;
				case STRING:
					myRoot = myQuery.from(ResourceIndexedSearchParamString.class);
					myResourcePidColumn = myRoot.get("myResourcePid").as(Long.class);
					myParamType = SearchBuilderJoinEnum.STRING;
					break;
				case TOKEN:
					myRoot = myQuery.from(ResourceIndexedSearchParamToken.class);
					myResourcePidColumn = myRoot.get("myResourcePid").as(Long.class);
					myParamType = SearchBuilderJoinEnum.TOKEN;
					break;
				case QUANTITY:
					myRoot = myQuery.from(ResourceIndexedSearchParamQuantity.class);
					myResourcePidColumn = myRoot.get("myResourcePid").as(Long.class);
					myParamType = SearchBuilderJoinEnum.QUANTITY;
					break;
				case URI:
					myRoot = myQuery.from(ResourceIndexedSearchParamUri.class);
					myResourcePidColumn = myRoot.get("myResourcePid").as(Long.class);
					myParamType = SearchBuilderJoinEnum.URI;
					break;
				case COORDS:
					myRoot = myQuery.from(ResourceIndexedSearchParamCoords.class);
					myResourcePidColumn = myRoot.get("myResourcePid").as(Long.class);
					myParamType = SearchBuilderJoinEnum.COORDS;
					break;
				default:
					throw new IllegalStateException(Msg.code(1073));
			}

			myQuery.select(myResourcePidColumn);
		}

		Validate.isTrue(theType == myParamType, "Wanted %s but got %s for %s", myParamType, theType, theSearchParameterName);
		return (From<?, T>) myRoot;
	}

	@Override
	AbstractQuery<Long> getQueryRoot() {
		Validate.isTrue(myQuery != null);
		return myQuery;
	}

	@Override
	Root<?> getRoot() {
		Validate.isTrue(myRoot != null);
		return myRoot;
	}

	@Override
	public Expression<Long> getResourcePidColumn() {
		Validate.isTrue(myResourcePidColumn != null);
		return myResourcePidColumn;
	}

	@Override
	public Subquery<Long> subqueryForTagNegation() {
		throw new IllegalStateException(Msg.code(1074));
	}
}
