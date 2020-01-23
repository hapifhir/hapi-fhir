package ca.uhn.fhir.jpa.dao.predicate;

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
		myPredicateBuilderToken = thePredicateBuilderFactory.newPredicateBuilderToken(theSearchBuilder);
		myPredicateBuilderUri = thePredicateBuilderFactory.newPredicateBuilderUri(theSearchBuilder);
	}

	void addPredicateCoords(String theResourceName, String theParamName, List<? extends IQueryParameterType> theNextAnd) {
		// FIXME KHS consolidate these predicate builders (e.g. all these mathods should have the same name)
		myPredicateBuilderCoords.addPredicateCoords(theResourceName, theParamName, theNextAnd);
	}

	private void addPredicateDate(String theResourceName, String theParamName, List<? extends IQueryParameterType> theNextAnd) {
		myPredicateBuilderDate.addPredicateDate(theResourceName, theParamName, theNextAnd, null);
	}

	Predicate addPredicateDate(String theResourceName, String theParamName, List<? extends IQueryParameterType> theNextAnd, SearchFilterParser.CompareOperation theOperation) {
		return myPredicateBuilderDate.addPredicateDate(theResourceName, theParamName, theNextAnd, theOperation);
	}

	private void addPredicateNumber(String theResourceName, String theParamName, List<? extends IQueryParameterType> theNextAnd) {
		myPredicateBuilderNumber.addPredicateNumber(theResourceName, theParamName, theNextAnd, null);
	}

	Predicate addPredicateNumber(String theResourceName, String theParamName, List<? extends IQueryParameterType> theNextAnd, SearchFilterParser.CompareOperation theOperation) {
		return myPredicateBuilderNumber.addPredicateNumber(theResourceName, theParamName, theNextAnd, theOperation);
	}

	private void addPredicateQuantity(String theResourceName, String theParamName, List<? extends IQueryParameterType> theNextAnd) {
		myPredicateBuilderQuantity.addPredicateQuantity(theResourceName, theParamName, theNextAnd, null);
	}

	Predicate addPredicateQuantity(String theResourceName, String theParamName, List<? extends IQueryParameterType> theNextAnd, SearchFilterParser.CompareOperation theOperation) {
		return myPredicateBuilderQuantity.addPredicateQuantity(theResourceName, theParamName, theNextAnd, theOperation);
	}

	private Predicate addPredicateReference(String theResourceName, String theParamName, List<? extends IQueryParameterType> theList, RequestDetails theRequest) {
		return myPredicateBuilderReference.addPredicateReference(theResourceName, theParamName, theList, null, theRequest);
	}

	void addPredicateString(String theResourceName, String theParamName, List<? extends IQueryParameterType> theNextAnd) {
		myPredicateBuilderString.addPredicateString(theResourceName, theParamName, theNextAnd, SearchFilterParser.CompareOperation.sw);
	}

	Predicate addPredicateString(String theResourceName, String theParamName, List<? extends IQueryParameterType> theNextAnd, SearchFilterParser.CompareOperation theOperation) {
		return myPredicateBuilderString.addPredicateString(theResourceName, theParamName, theNextAnd, theOperation);
	}

	void addPredicateTag(List<List<IQueryParameterType>> theAndOrParams, String theParamName) {
		myPredicateBuilderTag.addPredicateTag(theAndOrParams, theParamName);
	}

	private void addPredicateToken(String theResourceName, String theParamName, List<? extends IQueryParameterType> theNextAnd) {
		myPredicateBuilderToken.addPredicateToken(theResourceName, theParamName, theNextAnd, null);
	}

	Predicate addPredicateToken(String theResourceName, String theParamName, List<? extends IQueryParameterType> theNextAnd, SearchFilterParser.CompareOperation theOperation) {
		return myPredicateBuilderToken.addPredicateToken(theResourceName, theParamName, theNextAnd, theOperation);
	}

	private void addPredicateUri(String theResourceName, String theParamName, List<? extends IQueryParameterType> theNextAnd) {
		myPredicateBuilderUri.addPredicateUri(theResourceName, theParamName, theNextAnd, SearchFilterParser.CompareOperation.eq);
	}

	Predicate addPredicateUri(String theResourceName, String theName, List<? extends IQueryParameterType> theSingletonList, SearchFilterParser.CompareOperation theOperation) {
		return myPredicateBuilderUri.addPredicateUri(theResourceName, theName, theSingletonList, theOperation);
	}

	public void searchForIdsWithAndOr(String theResourceName, String theNextParamName, List<List<IQueryParameterType>> theAndOrParams, RequestDetails theRequest) {
		myPredicateBuilderReference.searchForIdsWithAndOr(theResourceName, theNextParamName, theAndOrParams, theRequest);
	}

	Subquery<Long> createLinkSubquery(boolean theFoundChainMatch, String theParameterName, String theTargetResourceType, ArrayList<IQueryParameterType> theOrValues, RequestDetails theRequest) {
		return myPredicateBuilderReference.createLinkSubquery(theFoundChainMatch, theParameterName, theTargetResourceType, theOrValues, theRequest);
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
