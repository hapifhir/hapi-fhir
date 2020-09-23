package ca.uhn.fhir.jpa.dao.search.querystack;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.dao.predicate.PredicateBuilderToken;
import ca.uhn.fhir.jpa.dao.predicate.SearchFilterParser;
import ca.uhn.fhir.jpa.dao.search.sql.BaseIndexTable;
import ca.uhn.fhir.jpa.dao.search.sql.BaseSearchParamIndexTable;
import ca.uhn.fhir.jpa.dao.search.sql.CoordsIndexTable;
import ca.uhn.fhir.jpa.dao.search.sql.DateIndexTable;
import ca.uhn.fhir.jpa.dao.search.sql.NumberIndexTable;
import ca.uhn.fhir.jpa.dao.search.sql.QuantityIndexTable;
import ca.uhn.fhir.jpa.dao.search.sql.ResourceIdPredicateBuilder3;
import ca.uhn.fhir.jpa.dao.search.sql.ResourceLinkIndexTable;
import ca.uhn.fhir.jpa.dao.search.sql.SearchSqlBuilder;
import ca.uhn.fhir.jpa.dao.search.sql.StringIndexTable;
import ca.uhn.fhir.jpa.dao.search.sql.TagPredicateBuilder3;
import ca.uhn.fhir.jpa.dao.search.sql.TokenIndexTable;
import ca.uhn.fhir.jpa.dao.search.sql.UriIndexTable;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.extractor.BaseSearchParamExtractor;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.NumberParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import com.healthmarketscience.sqlbuilder.ComboCondition;
import com.healthmarketscience.sqlbuilder.Condition;

import javax.annotation.Nullable;
import javax.persistence.criteria.From;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryStack3 {

	private final ModelConfig myModelConfig;
	private final FhirContext myFhirContext;
	private final SearchSqlBuilder mySqlBuilder;
	private final SearchParameterMap mySearchParameters;
	// FIXME: rename to add "date" to the name
	private Map<String, DateIndexTable> myJoinMap;

	/**
	 * Constructor
	 */
	public QueryStack3(SearchParameterMap theSearchParameters, ModelConfig theModelConfig, FhirContext theFhirContext, SearchSqlBuilder theSqlBuilder) {
		assert theSearchParameters != null;
		assert theModelConfig != null;
		assert theFhirContext != null;
		assert theSqlBuilder != null;

		mySearchParameters = theSearchParameters;
		myModelConfig = theModelConfig;
		myFhirContext = theFhirContext;
		mySqlBuilder = theSqlBuilder;
	}

	public Condition addPredicateCoords(String theResourceName,
													RuntimeSearchParam theSearchParam,
													List<? extends IQueryParameterType> theList,
													SearchFilterParser.CompareOperation theOperation,
													From<?, ResourceLink> theLinkJoin, RequestPartitionId theRequestPartitionId) {

		CoordsIndexTable join = mySqlBuilder.addCoordsSelector();

		if (theList.get(0).getMissing() != null) {
			addPredicateParamMissingForNonReference(theResourceName, theSearchParam.getName(), theList.get(0).getMissing(), join, theRequestPartitionId);
			return null;
		}

		addPartitionIdPredicate(theRequestPartitionId, join);

		List<Condition> codePredicates = new ArrayList<>();
		for (IQueryParameterType nextOr : theList) {
			Condition singleCode = join.createPredicateCoords(mySearchParameters, nextOr, theResourceName, theSearchParam, null, join, theRequestPartitionId);
			codePredicates.add(singleCode);
		}

		Condition retVal = ComboCondition.or(codePredicates.toArray(new Condition[0]));
		mySqlBuilder.addPredicate(retVal);
		return retVal;
	}


	public Condition addPredicateDate(String theResourceName,
												 RuntimeSearchParam theSearchParam,
												 List<? extends IQueryParameterType> theList,
												 SearchFilterParser.CompareOperation theOperation,
												 From<?, ResourceLink> theLinkJoin, RequestPartitionId theRequestPartitionId) {

		String paramName = theSearchParam.getName();
		boolean newJoin = false;
		if (myJoinMap == null) {
			myJoinMap = new HashMap<>();
		}
		String key = theResourceName + " " + paramName;

		DateIndexTable join = myJoinMap.get(key);
		if (join == null) {
			join = mySqlBuilder.addDateSelector();
			myJoinMap.put(key, join);
			newJoin = true;
		}

		if (theList.get(0).getMissing() != null) {
			Boolean missing = theList.get(0).getMissing();
			addPredicateParamMissingForNonReference(theResourceName, paramName, missing, join, theRequestPartitionId);
			return null;
		}

		List<Condition> codePredicates = new ArrayList<>();

		for (IQueryParameterType nextOr : theList) {
			Condition p = join.createPredicateDate(nextOr, theResourceName, paramName, null, join, theOperation, theRequestPartitionId);
			codePredicates.add(p);
		}

		Condition orPredicates = ComboCondition.or(codePredicates.toArray(new Condition[0]));

		if (newJoin) {
			Condition identityAndValuePredicate = join.combineParamIndexPredicateWithParamNamePredicate(theResourceName, paramName, orPredicates, theRequestPartitionId);
			mySqlBuilder.addPredicate(identityAndValuePredicate);
		} else {
			mySqlBuilder.addPredicate(orPredicates);
		}

		return orPredicates;
	}


	public Condition addPredicateNumber(String theResourceName,
													RuntimeSearchParam theSearchParam,
													List<? extends IQueryParameterType> theList,
													SearchFilterParser.CompareOperation theOperation,
													From<?, ResourceLink> theLinkJoin, RequestPartitionId theRequestPartitionId) {

		NumberIndexTable join = mySqlBuilder.addNumberSelector();

		if (theList.get(0).getMissing() != null) {
			addPredicateParamMissingForNonReference(theResourceName, theSearchParam.getName(), theList.get(0).getMissing(), join, theRequestPartitionId);
			return null;
		}

		addPartitionIdPredicate(theRequestPartitionId, join);

		List<Condition> codePredicates = new ArrayList<>();
		for (IQueryParameterType nextOr : theList) {

			if (nextOr instanceof NumberParam) {
				NumberParam param = (NumberParam) nextOr;

				BigDecimal value = param.getValue();
				if (value == null) {
					continue;
				}

				Condition predicate = join.createPredicateNumeric(theResourceName, theSearchParam.getName(), join, null, nextOr, theOperation, value, null, null, theRequestPartitionId);
				codePredicates.add(predicate);

			} else {
				throw new IllegalArgumentException("Invalid token type: " + nextOr.getClass());
			}

		}

		Condition predicate = ComboCondition.or(codePredicates.toArray(new Condition[0]));
		mySqlBuilder.addPredicate(predicate);
		return predicate;
	}


	public Condition addPredicateQuantity(String theResourceName,
													  RuntimeSearchParam theSearchParam,
													  List<? extends IQueryParameterType> theList,
													  SearchFilterParser.CompareOperation theOperation,
													  From<?, ResourceLink> theLinkJoin,
													  RequestPartitionId theRequestPartitionId) {

		QuantityIndexTable join = mySqlBuilder.addQuantity();

		if (theList.get(0).getMissing() != null) {
			addPredicateParamMissingForNonReference(theResourceName, theSearchParam.getName(), theList.get(0).getMissing(), join, theRequestPartitionId);
			return null;
		}

		addPartitionIdPredicate(theRequestPartitionId, join);

		List<Condition> codePredicates = new ArrayList<>();
		for (IQueryParameterType nextOr : theList) {
			Condition singleCode = join.createPredicateQuantity(nextOr, theResourceName, theSearchParam.getName(), null, join, theOperation, theRequestPartitionId);
			codePredicates.add(singleCode);
		}

		Condition retVal = ComboCondition.or(codePredicates.toArray(new Condition[0]));

		mySqlBuilder.addPredicate(retVal);

		return retVal;
	}


	public Condition addPredicateReference(String theResourceName,
														String theParamName,
														List<? extends IQueryParameterType> theList,
														SearchFilterParser.CompareOperation theOperation,
														RequestDetails theRequest,
														RequestPartitionId theRequestPartitionId) {

		// This just to ensure the chain has been split correctly
		assert theParamName.contains(".") == false;

		if ((theOperation != null) &&
			(theOperation != SearchFilterParser.CompareOperation.eq) &&
			(theOperation != SearchFilterParser.CompareOperation.ne)) {
			throw new InvalidRequestException("Invalid operator specified for reference predicate.  Supported operators for reference predicate are \"eq\" and \"ne\".");
		}

		if (theList.get(0).getMissing() != null) {
			// FIXME: implement
			throw new UnsupportedOperationException();
//			addPredicateParamMissingForReference(theResourceName, theParamName, theList.get(0).getMissing(), theRequestPartitionId);
//			return null;
		}

		ResourceLinkIndexTable join = mySqlBuilder.addReferenceSelector();
		return join.addPredicate(theRequest, theParamName, theList, theOperation, theRequestPartitionId);
	}


	@Nullable
	public Condition addPredicateResourceId(List<List<IQueryParameterType>> theValues, String theResourceName, SearchFilterParser.CompareOperation theOperation, RequestPartitionId theRequestPartitionId) {

		ResourceIdPredicateBuilder3 builder = mySqlBuilder.newResourceIdBuilder();
		Condition predicate = builder.createPredicateResourceId(theResourceName, theValues, theOperation, theRequestPartitionId);

		if (predicate != null) {
			mySqlBuilder.addPredicate(predicate);
		}

		return predicate;
	}


	public Condition addPredicateString(String theResourceName,
													RuntimeSearchParam theSearchParam,
													List<? extends IQueryParameterType> theList,
													SearchFilterParser.CompareOperation theOperation,
													From<?, ResourceLink> theLinkJoin, RequestPartitionId theRequestPartitionId) {

		StringIndexTable join = mySqlBuilder.addStringSelector();

		if (theList.get(0).getMissing() != null) {
			addPredicateParamMissingForNonReference(theResourceName, theSearchParam.getName(), theList.get(0).getMissing(), join, theRequestPartitionId);
			return null;
		}

		addPartitionIdPredicate(theRequestPartitionId, join);

		List<Condition> codePredicates = new ArrayList<>();
		for (IQueryParameterType nextOr : theList) {
			Condition singleCode = join.createPredicateString(nextOr, theResourceName, theSearchParam, null, join, theOperation, theRequestPartitionId);
			codePredicates.add(singleCode);
		}

		ComboCondition orCondition = ComboCondition.or(codePredicates.toArray(new Condition[0]));
		mySqlBuilder.addPredicate(orCondition);

		return null;
	}


	public void addPredicateTag(List<List<IQueryParameterType>> theAndOrParams, String theParamName, RequestPartitionId theRequestPartitionId) {

		TagPredicateBuilder3 join = mySqlBuilder.addTagSelector();
		join.addPredicateTag(theAndOrParams, theParamName, theRequestPartitionId);


	}


	public Condition addPredicateToken(String theResourceName,
												  RuntimeSearchParam theSearchParam,
												  List<? extends IQueryParameterType> theList,
												  SearchFilterParser.CompareOperation theOperation,
												  RequestPartitionId theRequestPartitionId) {

		if (theList.get(0).getMissing() != null) {
			addPredicateParamMissingForNonReference(theResourceName, theSearchParam.getName(), theList.get(0).getMissing(), mySqlBuilder.addTokenSelector(), theRequestPartitionId);
			return null;
		}

		List<IQueryParameterType> tokens = new ArrayList<>();
		for (IQueryParameterType nextOr : theList) {

			if (nextOr instanceof TokenParam) {
				TokenParam id = (TokenParam) nextOr;
				if (id.isText()) {

					// Check whether the :text modifier is actually enabled here
					boolean tokenTextIndexingEnabled = BaseSearchParamExtractor.tokenTextIndexingEnabledForSearchParam(myModelConfig, theSearchParam);
					if (!tokenTextIndexingEnabled) {
						String msg;
						if (myModelConfig.isSuppressStringIndexingInTokens()) {
							msg = myFhirContext.getLocalizer().getMessage(PredicateBuilderToken.class, "textModifierDisabledForServer");
						} else {
							msg = myFhirContext.getLocalizer().getMessage(PredicateBuilderToken.class, "textModifierDisabledForSearchParam");
						}
						throw new MethodNotAllowedException(msg);
					}

					// FIXME: restore
					throw new UnsupportedOperationException();
//					myPredicateBuilder.addLinkPredicateString(theResourceName, theSearchParam, theList, theLinkJoin, theRequestPartitionId);
//					break;
				}
			}

			tokens.add(nextOr);
		}

		if (tokens.isEmpty()) {
			return null;
		}

		TokenIndexTable join = mySqlBuilder.addTokenSelector();
		addPartitionIdPredicate(theRequestPartitionId, join);
		Condition predicate = join.createPredicateToken(tokens, theResourceName, theSearchParam, null, join, theOperation, theRequestPartitionId);

		if (predicate != null) {
			mySqlBuilder.addPredicate(predicate);
		}

		return predicate;
	}


	public Condition addPredicateUri(String theResourceName,
												RuntimeSearchParam theSearchParam,
												List<? extends IQueryParameterType> theList,
												SearchFilterParser.CompareOperation theOperation,
												From<?, ResourceLink> theLinkJoin, RequestPartitionId theRequestPartitionId) {

		String paramName = theSearchParam.getName();
		UriIndexTable join = mySqlBuilder.addUriSelector();

		if (theList.get(0).getMissing() != null) {
			addPredicateParamMissingForNonReference(theResourceName, paramName, theList.get(0).getMissing(), join, theRequestPartitionId);
			return null;
		}

		addPartitionIdPredicate(theRequestPartitionId, join);

		Condition outerPredicate = join.addPredicate(theList, paramName, theOperation);

		mySqlBuilder.addPredicate(outerPredicate);

		return outerPredicate;
	}


	private void addPredicateParamMissingForNonReference(String theResourceName, String theParamName, Boolean theMissing, BaseSearchParamIndexTable theJoin, RequestPartitionId theRequestPartitionId) {
		// FIXME: implement
		throw new UnsupportedOperationException();
	}


	// FIXME: Can this be added to the constructor of the index tables instead since we just call it everywhere anyhow..
	void addPartitionIdPredicate(RequestPartitionId theRequestPartitionId, BaseIndexTable theJoin) {
		if (!theRequestPartitionId.isAllPartitions()) {
			Integer partitionId = theRequestPartitionId.getPartitionId();
			theJoin.addPartitionIdPredicate(partitionId);
		}
	}


}
