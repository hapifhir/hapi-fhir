package ca.uhn.fhir.jpa.dao.predicate;

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

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.dao.LegacySearchBuilder;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamDate;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamQuantity;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.server.RequestDetails;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Subquery;
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

	public PredicateBuilder(LegacySearchBuilder theSearchBuilder, PredicateBuilderFactory thePredicateBuilderFactory) {
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

	void addPredicateCoords(String theResourceName, RuntimeSearchParam theSearchParam, List<? extends IQueryParameterType> theNextAnd, RequestPartitionId theRequestPartitionId) {
		myPredicateBuilderCoords.addPredicate(theResourceName, theSearchParam, theNextAnd, null, theRequestPartitionId);
	}

	Predicate addPredicateDate(String theResourceName, RuntimeSearchParam theSearchParam, List<? extends IQueryParameterType> theNextAnd, SearchFilterParser.CompareOperation theOperation, RequestPartitionId theRequestPartitionId) {
		return myPredicateBuilderDate.addPredicate(theResourceName, theSearchParam, theNextAnd, theOperation, theRequestPartitionId);
	}

	Predicate addPredicateNumber(String theResourceName, RuntimeSearchParam theSearchParam, List<? extends IQueryParameterType> theNextAnd, SearchFilterParser.CompareOperation theOperation, RequestPartitionId theRequestPartitionId) {
		return myPredicateBuilderNumber.addPredicate(theResourceName, theSearchParam, theNextAnd, theOperation, theRequestPartitionId);
	}

	Predicate addPredicateQuantity(String theResourceName, RuntimeSearchParam theSearchParam, List<? extends IQueryParameterType> theNextAnd, SearchFilterParser.CompareOperation theOperation, RequestPartitionId theRequestPartitionId) {
		return myPredicateBuilderQuantity.addPredicate(theResourceName, theSearchParam, theNextAnd, theOperation, theRequestPartitionId);
	}

	void addPredicateString(String theResourceName, RuntimeSearchParam theSearchParam, List<? extends IQueryParameterType> theNextAnd, RequestPartitionId theRequestPartitionId) {
		myPredicateBuilderString.addPredicate(theResourceName, theSearchParam, theNextAnd, SearchFilterParser.CompareOperation.sw, theRequestPartitionId);
	}

	Predicate addPredicateString(String theResourceName,RuntimeSearchParam theSearchParam, List<? extends IQueryParameterType> theNextAnd, SearchFilterParser.CompareOperation theOperation, RequestPartitionId theRequestPartitionId) {
		return myPredicateBuilderString.addPredicate(theResourceName, theSearchParam, theNextAnd, theOperation, theRequestPartitionId);
	}

	void addPredicateTag(List<List<IQueryParameterType>> theAndOrParams, String theParamName, RequestPartitionId theRequestPartitionId) {
		myPredicateBuilderTag.addPredicateTag(theAndOrParams, theParamName, theRequestPartitionId);
	}

	Predicate addPredicateToken(String theResourceName, RuntimeSearchParam theSearchParam, List<? extends IQueryParameterType> theNextAnd, SearchFilterParser.CompareOperation theOperation, RequestPartitionId theRequestPartitionId) {
		return myPredicateBuilderToken.addPredicate(theResourceName, theSearchParam, theNextAnd, theOperation, theRequestPartitionId);
	}

	Predicate addPredicateUri(String theResourceName, RuntimeSearchParam theSearchParam, List<? extends IQueryParameterType> theSingletonList, SearchFilterParser.CompareOperation theOperation, RequestPartitionId theRequestPartitionId) {
		return myPredicateBuilderUri.addPredicate(theResourceName, theSearchParam, theSingletonList, theOperation, theRequestPartitionId);
	}

	public void searchForIdsWithAndOr(String theResourceName, String theNextParamName, List<List<IQueryParameterType>> theAndOrParams, RequestDetails theRequest, RequestPartitionId theRequestPartitionId) {
		myPredicateBuilderReference.searchForIdsWithAndOr(theResourceName, theNextParamName, theAndOrParams, theRequest, theRequestPartitionId);
	}

	Subquery<Long> createLinkSubquery(String theParameterName, String theTargetResourceType, ArrayList<IQueryParameterType> theOrValues, RequestDetails theRequest, RequestPartitionId theRequestPartitionId) {
		return myPredicateBuilderReference.createLinkSubquery(theParameterName, theTargetResourceType, theOrValues, theRequest, theRequestPartitionId);
	}

	Predicate createResourceLinkPathPredicate(String theTargetResourceType, String theParamReference, Join<?, ResourceLink> theJoin) {
		return myPredicateBuilderReference.createResourceLinkPathPredicate(theTargetResourceType, theParamReference, theJoin);
	}

	void addPredicateResourceId(List<List<IQueryParameterType>> theAndOrParams, String theResourceName, RequestPartitionId theRequestPartitionId) {
		myPredicateBuilderResourceId.addPredicateResourceId(theAndOrParams, theResourceName, null, theRequestPartitionId);
	}

	public Predicate addPredicateResourceId(List<List<IQueryParameterType>> theValues, String theResourceName, SearchFilterParser.CompareOperation theOperation, RequestPartitionId theRequestPartitionId) {
		return myPredicateBuilderResourceId.addPredicateResourceId(theValues, theResourceName, theOperation, theRequestPartitionId);
	}

	Predicate createPredicateString(IQueryParameterType theLeftValue, String theResourceName, RuntimeSearchParam theSearchParam, CriteriaBuilder theBuilder, From<ResourceIndexedSearchParamString, ResourceIndexedSearchParamString> theStringJoin, RequestPartitionId theRequestPartitionId) {
		return myPredicateBuilderString.createPredicateString(theLeftValue, theResourceName, theSearchParam, theBuilder, theStringJoin, theRequestPartitionId);
	}

	Collection<Predicate> createPredicateToken(List<IQueryParameterType> theTokens, String theResourceName, RuntimeSearchParam theSearchParam, CriteriaBuilder theBuilder, From<ResourceIndexedSearchParamToken, ResourceIndexedSearchParamToken> theTokenJoin, RequestPartitionId theRequestPartitionId) {
		return myPredicateBuilderToken.createPredicateToken(theTokens, theResourceName, theSearchParam, theBuilder, theTokenJoin, theRequestPartitionId);
	}

	Predicate createPredicateDate(IQueryParameterType theLeftValue, String theResourceName, String theName, CriteriaBuilder theBuilder, From<ResourceIndexedSearchParamDate, ResourceIndexedSearchParamDate> theDateJoin, RequestPartitionId theRequestPartitionId) {
		return myPredicateBuilderDate.createPredicateDate(theLeftValue, theResourceName, theName, theBuilder, theDateJoin, theRequestPartitionId);
	}

	Predicate createPredicateQuantity(IQueryParameterType theLeftValue, String theResourceName, String theName, CriteriaBuilder theBuilder, From<ResourceIndexedSearchParamQuantity, ResourceIndexedSearchParamQuantity> theDateJoin, RequestPartitionId theRequestPartitionId) {
		return myPredicateBuilderQuantity.createPredicateQuantity(theLeftValue, theResourceName, theName, theBuilder, theDateJoin, theRequestPartitionId);
	}
}
