package ca.uhn.fhir.jpa.dao.predicate;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirDao;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.dao.SearchBuilder;
import ca.uhn.fhir.jpa.model.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamDate;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.entity.SearchParamPresent;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;

abstract class BasePredicateBuilder {
	private static final Logger ourLog = LoggerFactory.getLogger(BasePredicateBuilder.class);
	@Autowired
	FhirContext myContext;
	@Autowired
	DaoConfig myDaoConfig;

	boolean myDontUseHashesForSearch;
	final BaseHapiFhirDao<?> myCallingDao;
	final CriteriaBuilder myBuilder;
	final Root<ResourceTable> myResourceTableRoot;
	final IndexJoins myIndexJoins;
	final ArrayList<Predicate> myPredicates;
	final Class<? extends IBaseResource> myResourceType;
	final String myResourceName;
	final AbstractQuery<Long> myResourceTableQuery;
	final SearchParameterMap myParams;

	// FIXME KHS autowire with lookup
	BasePredicateBuilder(SearchBuilder theSearchBuilder) {
		myCallingDao = theSearchBuilder.getCallingDao();
		myBuilder = theSearchBuilder.getBuilder();
		myResourceTableRoot = theSearchBuilder.getResourceTableRoot();
		myIndexJoins = theSearchBuilder.getIndexJoins();
		myPredicates = theSearchBuilder.getPredicates();
		myResourceType = theSearchBuilder.getResourceType();
		myResourceName = theSearchBuilder.getResourceName();
		myResourceTableQuery = theSearchBuilder.getResourceTableQuery();
		myParams = theSearchBuilder.getParams();
	}

	@PostConstruct
	private void postConstruct() {
		myDontUseHashesForSearch = myDaoConfig.getDisableHashBasedSearches();
	}

	@SuppressWarnings("unchecked")
	<T> Join<ResourceTable, T> createJoin(SearchBuilderJoinEnum theType, String theSearchParameterName) {
		Join<ResourceTable, ResourceIndexedSearchParamDate> join = null;
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
		}

		SearchBuilderJoinKey key = new SearchBuilderJoinKey(theSearchParameterName, theType);
		myIndexJoins.put(key, join);

		return (Join<ResourceTable, T>) join;
	}

	void addPredicateParamMissing(String theResourceName, String theParamName, boolean theMissing) {
//		if (myDontUseHashesForSearch) {
//			Join<ResourceTable, SearchParamPresent> paramPresentJoin = myResourceTableRoot.join("mySearchParamPresents", JoinType.LEFT);
//			Join<Object, Object> paramJoin = paramPresentJoin.join("mySearchParam", JoinType.LEFT);
//
//			myPredicates.add(myBuilder.equal(paramJoin.get("myResourceName"), theResourceName));
//			myPredicates.add(myBuilder.equal(paramJoin.get("myParamName"), theParamName));
//			myPredicates.add(myBuilder.equal(paramPresentJoin.get("myPresent"), !theMissing));
//		}

		Join<ResourceTable, SearchParamPresent> paramPresentJoin = myResourceTableRoot.join("mySearchParamPresents", JoinType.LEFT);

		Expression<Long> hashPresence = paramPresentJoin.get("myHashPresence").as(Long.class);
		Long hash = SearchParamPresent.calculateHashPresence(theResourceName, theParamName, !theMissing);
		myPredicates.add(myBuilder.equal(hashPresence, hash));
	}

	void addPredicateParamMissing(String theResourceName, String theParamName, boolean theMissing, Join<ResourceTable, ? extends BaseResourceIndexedSearchParam> theJoin) {

		myPredicates.add(myBuilder.equal(theJoin.get("myResourceType"), theResourceName));
		myPredicates.add(myBuilder.equal(theJoin.get("myParamName"), theParamName));
		myPredicates.add(myBuilder.equal(theJoin.get("myMissing"), theMissing));
	}

	Predicate combineParamIndexPredicateWithParamNamePredicate(String theResourceName, String theParamName, From<?, ? extends BaseResourceIndexedSearchParam> theFrom, Predicate thePredicate) {
		if (myDontUseHashesForSearch) {
			Predicate resourceTypePredicate = myBuilder.equal(theFrom.get("myResourceType"), theResourceName);
			Predicate paramNamePredicate = myBuilder.equal(theFrom.get("myParamName"), theParamName);
			Predicate outerPredicate = myBuilder.and(resourceTypePredicate, paramNamePredicate, thePredicate);
			return outerPredicate;
		}

		long hashIdentity = BaseResourceIndexedSearchParam.calculateHashIdentity(theResourceName, theParamName);
		Predicate hashIdentityPredicate = myBuilder.equal(theFrom.get("myHashIdentity"), hashIdentity);
		return myBuilder.and(hashIdentityPredicate, thePredicate);
	}


	Predicate createPredicateNumeric(String theResourceName,
														  String theParamName,
														  From<?, ? extends BaseResourceIndexedSearchParam> theFrom,
														  CriteriaBuilder builder,
														  IQueryParameterType theParam,
														  ParamPrefixEnum thePrefix,
														  BigDecimal theValue,
														  final Expression<BigDecimal> thePath,
														  String invalidMessageName) {
		Predicate num;
		// Per discussions with Grahame Grieve and James Agnew on 11/13/19, modified logic for EQUAL and NOT_EQUAL operators below so as to
		//   use exact value matching.  The "fuzz amount" matching is still used with the APPROXIMATE operator.
		switch (thePrefix) {
			case GREATERTHAN:
				num = builder.gt(thePath, theValue);
				break;
			case GREATERTHAN_OR_EQUALS:
				num = builder.ge(thePath, theValue);
				break;
			case LESSTHAN:
				num = builder.lt(thePath, theValue);
				break;
			case LESSTHAN_OR_EQUALS:
				num = builder.le(thePath, theValue);
				break;
			case EQUAL:
				num = builder.equal(thePath, theValue);
				break;
			case NOT_EQUAL:
				num = builder.notEqual(thePath, theValue);
				break;
			case APPROXIMATE:
				BigDecimal mul = calculateFuzzAmount(thePrefix, theValue);
				BigDecimal low = theValue.subtract(mul, MathContext.DECIMAL64);
				BigDecimal high = theValue.add(mul, MathContext.DECIMAL64);
				Predicate lowPred;
				Predicate highPred;
				lowPred = builder.ge(thePath.as(BigDecimal.class), low);
				highPred = builder.le(thePath.as(BigDecimal.class), high);
				num = builder.and(lowPred, highPred);
				ourLog.trace("Searching for {} <= val <= {}", low, high);
				break;
			case ENDS_BEFORE:
			case STARTS_AFTER:
			default:
				String msg = myContext.getLocalizer().getMessage(SearchBuilder.class, invalidMessageName, thePrefix.getValue(), theParam.getValueAsQueryToken(myContext));
				throw new InvalidRequestException(msg);
		}

		if (theParamName == null) {
			return num;
		}
		return combineParamIndexPredicateWithParamNamePredicate(theResourceName, theParamName, theFrom, num);
	}

	/**
	 * Figures out the tolerance for a search. For example, if the user is searching for <code>4.00</code>, this method
	 * returns <code>0.005</code> because we shold actually match values which are
	 * <code>4 (+/-) 0.005</code> according to the FHIR specs.
	 */
	static BigDecimal calculateFuzzAmount(ParamPrefixEnum cmpValue, BigDecimal theValue) {
		if (cmpValue == ParamPrefixEnum.APPROXIMATE) {
			return theValue.multiply(new BigDecimal(0.1));
		} else {
			String plainString = theValue.toPlainString();
			int dotIdx = plainString.indexOf('.');
			if (dotIdx == -1) {
				return new BigDecimal(0.5);
			}

			int precision = plainString.length() - (dotIdx);
			double mul = Math.pow(10, -precision);
			double val = mul * 5.0d;
			return new BigDecimal(val);
		}
	}

	static String createLeftAndRightMatchLikeExpression(String likeExpression) {
		return "%" + likeExpression.replace("%", "[%]") + "%";
	}

	static String createLeftMatchLikeExpression(String likeExpression) {
		return likeExpression.replace("%", "[%]") + "%";
	}

	static String createRightMatchLikeExpression(String likeExpression) {
		return "%" + likeExpression.replace("%", "[%]");
	}

	static Predicate[] toArray(List<Predicate> thePredicates) {
		return thePredicates.toArray(new Predicate[0]);
	}
}
