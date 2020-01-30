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

import ca.uhn.fhir.jpa.dao.SearchBuilder;
import ca.uhn.fhir.jpa.model.entity.*;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.server.RequestDetails;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PredicateBuilder {
	private final PredicateBuilderCoords myPredicateBuilderCoords;
	private final PredicateBuilderDate myPredicateBuilderDate;
	private final PredicateBuilderNumber myPredicateBuilderNumber;
	private final PredicateBuilderQuantity myPredicateBuilderQuantity;
	private final PredicateBuilderReference myPredicateBuilderReference;
	private final PredicateBuilderResourceId myPredicateBuilderResourceId;
	private final PredicateBuilderString myPredicateBuilderString;
	private final PredicateBuilderTag myPredicateBuilderTag;
	private final PredicateBuilderToken myPredicateBuilderToken;
	private final PredicateBuilderUri myPredicateBuilderUri;

	public PredicateBuilder(SearchBuilder theSearchBuilder, PredicateBuilderFactory thePredicateBuilderFactory) {
		myPredicateBuilderCoords = thePredicateBuilderFactory.newPredicateBuilderCoords(theSearchBuilder);
		myPredicateBuilderDate = thePredicateBuilderFactory.newPredicateBuilderDate(theSearchBuilder);
		myPredicateBuilderNumber = thePredicateBuilderFactory.newPredicateBuilderNumber(theSearchBuilder);
		myPredicateBuilderQuantity = thePredicateBuilderFactory.newPredicateBuilderQuantity(theSearchBuilder);
		myPredicateBuilderReference = thePredicateBuilderFactory.newPredicateBuilderReference(theSearchBuilder, this);
		myPredicateBuilderResourceId = thePredicateBuilderFactory.newPredicateBuilderResourceId(theSearchBuilder);
		myPredicateBuilderString = thePredicateBuilderFactory.newPredicateBuilderString(theSearchBuilder);
		myPredicateBuilderTag = thePredicateBuilderFactory.newPredicateBuilderTag(theSearchBuilder);
		myPredicateBuilderToken = thePredicateBuilderFactory.newPredicateBuilderToken(theSearchBuilder, this);
		myPredicateBuilderUri = thePredicateBuilderFactory.newPredicateBuilderUri(theSearchBuilder);
	}

	void addPredicateCoords(String theResourceName, String theParamName, List<? extends IQueryParameterType> theNextAnd) {
		myPredicateBuilderCoords.addPredicate(theResourceName, theParamName, theNextAnd, null);
	}

	Predicate addPredicateDate(String theResourceName, String theParamName, List<? extends IQueryParameterType> theNextAnd, SearchFilterParser.CompareOperation theOperation) {
		return myPredicateBuilderDate.addPredicate(theResourceName, theParamName, theNextAnd, theOperation);
	}

	Predicate addPredicateNumber(String theResourceName, String theParamName, List<? extends IQueryParameterType> theNextAnd, SearchFilterParser.CompareOperation theOperation) {
		return myPredicateBuilderNumber.addPredicate(theResourceName, theParamName, theNextAnd, theOperation);
	}

	Predicate addPredicateQuantity(String theResourceName, String theParamName, List<? extends IQueryParameterType> theNextAnd, SearchFilterParser.CompareOperation theOperation) {
		return myPredicateBuilderQuantity.addPredicate(theResourceName, theParamName, theNextAnd, theOperation);
	}

	void addPredicateString(String theResourceName, String theParamName, List<? extends IQueryParameterType> theNextAnd) {
		myPredicateBuilderString.addPredicate(theResourceName, theParamName, theNextAnd, SearchFilterParser.CompareOperation.sw);
	}

	Predicate addPredicateString(String theResourceName, String theParamName, List<? extends IQueryParameterType> theNextAnd, SearchFilterParser.CompareOperation theOperation) {
		return myPredicateBuilderString.addPredicate(theResourceName, theParamName, theNextAnd, theOperation);
	}

	void addPredicateTag(List<List<IQueryParameterType>> theAndOrParams, String theParamName) {
		myPredicateBuilderTag.addPredicateTag(theAndOrParams, theParamName);
	}

	Predicate addPredicateToken(String theResourceName, String theParamName, List<? extends IQueryParameterType> theNextAnd, SearchFilterParser.CompareOperation theOperation) {
		return myPredicateBuilderToken.addPredicate(theResourceName, theParamName, theNextAnd, theOperation);
	}

	Predicate addPredicateUri(String theResourceName, String theName, List<? extends IQueryParameterType> theSingletonList, SearchFilterParser.CompareOperation theOperation) {
		return myPredicateBuilderUri.addPredicate(theResourceName, theName, theSingletonList, theOperation);
	}

	public void searchForIdsWithAndOr(String theResourceName, String theNextParamName, List<List<IQueryParameterType>> theAndOrParams, RequestDetails theRequest) {
		myPredicateBuilderReference.searchForIdsWithAndOr(theResourceName, theNextParamName, theAndOrParams, theRequest);
	}

	Subquery<Long> createLinkSubquery(String theParameterName, String theTargetResourceType, ArrayList<IQueryParameterType> theOrValues, RequestDetails theRequest) {
		return myPredicateBuilderReference.createLinkSubquery(true, theParameterName, theTargetResourceType, theOrValues, theRequest);
	}

	Predicate createResourceLinkPathPredicate(String theTargetResourceType, String theParamReference, Join<ResourceTable, ResourceLink> theJoin) {
		return myPredicateBuilderReference.createResourceLinkPathPredicate(theTargetResourceType, theParamReference, theJoin);
	}

	void addPredicateResourceId(List<List<IQueryParameterType>> theAndOrParams, String theResourceName, RequestDetails theRequest) {
		myPredicateBuilderResourceId.addPredicateResourceId(theAndOrParams, theResourceName, null, theRequest);
	}

	public Predicate addPredicateResourceId(List<List<IQueryParameterType>> theValues, String theResourceName, SearchFilterParser.CompareOperation theOperation, RequestDetails theRequest) {
		return myPredicateBuilderResourceId.addPredicateResourceId(theValues, theResourceName, theOperation, theRequest);
	}

	Predicate createPredicateString(IQueryParameterType theLeftValue, String theResourceName, String theName, CriteriaBuilder theBuilder, From<ResourceIndexedSearchParamString, ResourceIndexedSearchParamString> theStringJoin) {
		return myPredicateBuilderString.createPredicateString(theLeftValue, theResourceName, theName, theBuilder, theStringJoin);
	}

	Collection<Predicate> createPredicateToken(List<IQueryParameterType> theTokens, String theResourceName, String theName, CriteriaBuilder theBuilder, From<ResourceIndexedSearchParamToken, ResourceIndexedSearchParamToken> theTokenJoin) {
		return myPredicateBuilderToken.createPredicateToken(theTokens, theResourceName, theName, theBuilder, theTokenJoin);
	}

	Predicate createPredicateDate(IQueryParameterType theLeftValue, String theResourceName, String theName, CriteriaBuilder theBuilder, From<ResourceIndexedSearchParamDate, ResourceIndexedSearchParamDate> theDateJoin) {
		return myPredicateBuilderDate.createPredicateDate(theLeftValue, theResourceName, theName, theBuilder, theDateJoin);
	}

	Predicate createPredicateQuantity(IQueryParameterType theLeftValue, String theResourceName, String theName, CriteriaBuilder theBuilder, From<ResourceIndexedSearchParamQuantity, ResourceIndexedSearchParamQuantity> theDateJoin) {
		return myPredicateBuilderQuantity.createPredicateQuantity(theLeftValue, theResourceName, theName, theBuilder, theDateJoin);
	}
}
