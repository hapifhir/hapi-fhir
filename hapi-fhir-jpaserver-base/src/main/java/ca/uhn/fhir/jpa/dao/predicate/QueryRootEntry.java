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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QueryRootEntry {
	private final AbstractQuery<Long> myResourceTableQuery;
	private final Root<ResourceTable> myResourceTableRoot;
	private final ArrayList<Predicate> myPredicates = new ArrayList<>();
	private final IndexJoins myIndexJoins = new IndexJoins();

	public QueryRootEntry(AbstractQuery<Long> theResourceTableQuery) {
		myResourceTableQuery = theResourceTableQuery;
		myResourceTableRoot = theResourceTableQuery.from(ResourceTable.class);
	}

	public Root<ResourceTable> getRoot() {
		return myResourceTableRoot;
	}

	public <Y> Path<Y> get(String theAttributeName) {
		return myResourceTableRoot.get(theAttributeName);
	}

	public <Y> Join<ResourceTable, Y> join(String theAttributeName, JoinType theJoinType) {
		return myResourceTableRoot.join(theAttributeName, theJoinType);
	}

	public Join<?,?> getIndexJoin(SearchBuilderJoinKey theKey) {
		return myIndexJoins.get(theKey);
	}

	public void addPredicate(Predicate thePredicate) {
		myPredicates.add(thePredicate);
	}

	public void addPredicates(List<Predicate> thePredicates) {
		myPredicates.addAll(thePredicates);
	}

	public Predicate[] getPredicateArray() {
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

	public void where(Predicate theAnd) {
		myResourceTableQuery.where(theAnd);
	}

	<T> Subquery<T> subquery(Class<T> theClass) {
		return myResourceTableQuery.subquery(theClass);
	}
}
