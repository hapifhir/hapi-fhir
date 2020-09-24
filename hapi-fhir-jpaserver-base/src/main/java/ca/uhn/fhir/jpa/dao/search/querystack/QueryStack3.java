package ca.uhn.fhir.jpa.dao.search.querystack;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirResourceDao;
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
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.extractor.BaseSearchParamExtractor;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.NumberParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.param.QuantityParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import com.healthmarketscience.sqlbuilder.ComboCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import org.hl7.fhir.instance.model.api.IAnyResource;

import javax.annotation.Nullable;
import javax.persistence.criteria.From;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class QueryStack3 {

	private final ModelConfig myModelConfig;
	private final FhirContext myFhirContext;
	private final SearchSqlBuilder mySqlBuilder;
	private final SearchParameterMap mySearchParameters;
	private final ISearchParamRegistry mySearchParamRegistry;
	private final PartitionSettings myPartitionSettings;
	// FIXME: rename to add "date" to the name
	private Map<String, DateIndexTable> myJoinMap;

	/**
	 * Constructor
	 */
	public QueryStack3(SearchParameterMap theSearchParameters, ModelConfig theModelConfig, FhirContext theFhirContext, SearchSqlBuilder theSqlBuilder, ISearchParamRegistry theSearchParamRegistry, PartitionSettings thePartitionSettings) {
		myPartitionSettings = thePartitionSettings;
		assert theSearchParameters != null;
		assert theModelConfig != null;
		assert theFhirContext != null;
		assert theSqlBuilder != null;

		mySearchParameters = theSearchParameters;
		myModelConfig = theModelConfig;
		myFhirContext = theFhirContext;
		mySqlBuilder = theSqlBuilder;
		mySearchParamRegistry = theSearchParamRegistry;
	}

	public Condition addPredicateCoords(@Nullable DbColumn theSourceJoinColumn,
													String theResourceName,
													RuntimeSearchParam theSearchParam,
													List<? extends IQueryParameterType> theList,
													SearchFilterParser.CompareOperation theOperation,
													RequestPartitionId theRequestPartitionId) {

		CoordsIndexTable join = mySqlBuilder.addCoordsSelector(theSourceJoinColumn);

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


	public Condition addPredicateDate(@Nullable DbColumn theSourceJoinColumn,
												 String theResourceName,
												 RuntimeSearchParam theSearchParam,
												 List<? extends IQueryParameterType> theList,
												 SearchFilterParser.CompareOperation theOperation,
												 RequestPartitionId theRequestPartitionId) {

		String paramName = theSearchParam.getName();
		boolean newJoin = false;
		if (myJoinMap == null) {
			myJoinMap = new HashMap<>();
		}
		String key = theResourceName + " " + paramName;

		DateIndexTable join = myJoinMap.get(key);
		if (join == null) {
			join = mySqlBuilder.addDateSelector(theSourceJoinColumn);
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


	public Condition addPredicateNumber(@Nullable DbColumn theSourceJoinColumn,
													String theResourceName,
													RuntimeSearchParam theSearchParam,
													List<? extends IQueryParameterType> theList,
													SearchFilterParser.CompareOperation theOperation,
													RequestPartitionId theRequestPartitionId) {

		NumberIndexTable join = mySqlBuilder.addNumberSelector(theSourceJoinColumn);

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


	public Condition addPredicateQuantity(@Nullable DbColumn theSourceJoinColumn,
													  String theResourceName,
													  RuntimeSearchParam theSearchParam,
													  List<? extends IQueryParameterType> theList,
													  SearchFilterParser.CompareOperation theOperation,
													  RequestPartitionId theRequestPartitionId) {

		QuantityIndexTable join = mySqlBuilder.addQuantity(theSourceJoinColumn);

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


	public Condition addPredicateReference(@Nullable DbColumn theSourceJoinColumn,
														String theResourceName,
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

		ResourceLinkIndexTable join = mySqlBuilder.addReferenceSelector(this, theSourceJoinColumn);
		return join.addPredicate(theRequest, theResourceName, theParamName, theList, theOperation, theRequestPartitionId);
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


	public Condition addPredicateString(@Nullable DbColumn theSourceJoinColumn,
													String theResourceName,
													RuntimeSearchParam theSearchParam,
													List<? extends IQueryParameterType> theList,
													SearchFilterParser.CompareOperation theOperation,
													RequestPartitionId theRequestPartitionId) {

		StringIndexTable join = mySqlBuilder.addStringSelector(theSourceJoinColumn);

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


	public void addPredicateTag(@Nullable DbColumn theSourceJoinColumn, List<List<IQueryParameterType>> theAndOrParams, String theParamName, RequestPartitionId theRequestPartitionId) {

		TagPredicateBuilder3 join = mySqlBuilder.addTagSelector(theSourceJoinColumn);
		join.addPredicateTag(theAndOrParams, theParamName, theRequestPartitionId);


	}


	public Condition addPredicateToken(@Nullable DbColumn theSourceJoinColumn,
												  String theResourceName,
												  RuntimeSearchParam theSearchParam,
												  List<? extends IQueryParameterType> theList,
												  SearchFilterParser.CompareOperation theOperation,
												  RequestPartitionId theRequestPartitionId) {

		if (theList.get(0).getMissing() != null) {
			addPredicateParamMissingForNonReference(theResourceName, theSearchParam.getName(), theList.get(0).getMissing(), mySqlBuilder.addTokenSelector(theSourceJoinColumn), theRequestPartitionId);
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

		TokenIndexTable join = mySqlBuilder.addTokenSelector(theSourceJoinColumn);
		addPartitionIdPredicate(theRequestPartitionId, join);
		Condition predicate = join.createPredicateToken(tokens, theResourceName, theSearchParam, null, join, theOperation, theRequestPartitionId);

		if (predicate != null) {
			mySqlBuilder.addPredicate(predicate);
		}

		return predicate;
	}


	public Condition addPredicateUri(@Nullable DbColumn theSourceJoinColumn,
												String theResourceName,
												RuntimeSearchParam theSearchParam,
												List<? extends IQueryParameterType> theList,
												SearchFilterParser.CompareOperation theOperation,
												RequestPartitionId theRequestPartitionId) {

		String paramName = theSearchParam.getName();
		UriIndexTable join = mySqlBuilder.addUriSelector(theSourceJoinColumn);

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


	public void searchForIdsWithAndOr(@Nullable DbColumn theSourceJoinColumn, String theResourceName, String theParamName, List<List<IQueryParameterType>> theAndOrParams, RequestDetails theRequest, RequestPartitionId theRequestPartitionId) {

		if (theAndOrParams.isEmpty()) {
			return;
		}

		switch (theParamName) {
			case IAnyResource.SP_RES_ID:
				addPredicateResourceId(theAndOrParams, theResourceName, null, theRequestPartitionId);
				break;

			case IAnyResource.SP_RES_LANGUAGE:
				// FIXME: implement
//				addPredicateLanguage(theAndOrParams, null);
				break;

			case Constants.PARAM_HAS:
				// FIXME: implement
//				addPredicateHas(theResourceName, theAndOrParams, theRequest, theRequestPartitionId);
				break;

			case Constants.PARAM_TAG:
			case Constants.PARAM_PROFILE:
			case Constants.PARAM_SECURITY:
				addPredicateTag(theSourceJoinColumn, theAndOrParams, theParamName, theRequestPartitionId);
				break;

			case Constants.PARAM_SOURCE:
				// FIXME: implement
//				addPredicateSource(theAndOrParams, theRequest);
				break;

			default:

				RuntimeSearchParam nextParamDef = mySearchParamRegistry.getActiveSearchParam(theResourceName, theParamName);
				if (nextParamDef != null) {

					if (myPartitionSettings.isPartitioningEnabled() && myPartitionSettings.isIncludePartitionInSearchHashes()) {
						if (theRequestPartitionId.isAllPartitions()) {
							throw new PreconditionFailedException("This server is not configured to support search against all partitions");
						}
					}

					switch (nextParamDef.getParamType()) {
						case DATE:
							for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
								addPredicateDate(theSourceJoinColumn, theResourceName, nextParamDef, nextAnd, null, theRequestPartitionId);
							}
							break;
						case QUANTITY:
							for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
								SearchFilterParser.CompareOperation operation = null;
								if (nextAnd.size() > 0) {
									QuantityParam param = (QuantityParam) nextAnd.get(0);
									operation = toOperation(param.getPrefix());
								}
								addPredicateQuantity(theSourceJoinColumn, theResourceName, nextParamDef, nextAnd, operation, theRequestPartitionId);
							}
							break;
						case REFERENCE:
							for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
								addPredicateReference(theSourceJoinColumn, theResourceName, theParamName, nextAnd, null, theRequest, theRequestPartitionId);
							}
							break;
						case STRING:
							for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
								addPredicateString(theSourceJoinColumn, theResourceName, nextParamDef, nextAnd, SearchFilterParser.CompareOperation.sw,  theRequestPartitionId);
							}
							break;
						case TOKEN:
							for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
								if ("Location.position".equals(nextParamDef.getPath())) {
									addPredicateCoords(theSourceJoinColumn, theResourceName, nextParamDef, nextAnd, null,  theRequestPartitionId);
								} else {
									addPredicateToken(theSourceJoinColumn, theResourceName, nextParamDef, nextAnd, null, theRequestPartitionId);
								}
							}
							break;
						case NUMBER:
							for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
								addPredicateNumber(theSourceJoinColumn, theResourceName, nextParamDef, nextAnd, null,  theRequestPartitionId);
							}
							break;
						case COMPOSITE:
							for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
								// FIXME: implement
//								addPredicateComposite(theResourceName, nextParamDef, nextAnd, theRequestPartitionId);
							}
							break;
						case URI:
							for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
								addPredicateUri(theSourceJoinColumn, theResourceName, nextParamDef, nextAnd, SearchFilterParser.CompareOperation.eq,  theRequestPartitionId);
							}
							break;
						case HAS:
						case SPECIAL:
							for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
								if ("Location.position".equals(nextParamDef.getPath())) {
									addPredicateCoords(theSourceJoinColumn, theResourceName, nextParamDef, nextAnd, null,  theRequestPartitionId);
								}
							}
							break;
					}
				} else {
					if (Constants.PARAM_CONTENT.equals(theParamName) || Constants.PARAM_TEXT.equals(theParamName)) {
						// These are handled later
					} else if (Constants.PARAM_FILTER.equals(theParamName)) {
						// FIXME: implement
//
//						// Parse the predicates enumerated in the _filter separated by AND or OR...
//						if (theAndOrParams.get(0).get(0) instanceof StringParam) {
//							String filterString = ((StringParam) theAndOrParams.get(0).get(0)).getValue();
//							SearchFilterParser.Filter filter;
//							try {
//								filter = SearchFilterParser.parse(filterString);
//							} catch (SearchFilterParser.FilterSyntaxException theE) {
//								throw new InvalidRequestException("Error parsing _filter syntax: " + theE.getMessage());
//							}
//							if (filter != null) {
//
//								if (!myDaoConfig.isFilterParameterEnabled()) {
//									throw new InvalidRequestException(Constants.PARAM_FILTER + " parameter is disabled on this server");
//								}
//
//								// TODO: we clear the predicates below because the filter builds up
//								// its own collection of predicates. It'd probably be good at some
//								// point to do something more fancy...
//								ArrayList<Predicate> holdPredicates = new ArrayList<>(myQueryStack.getPredicates());
//
//								Condition filterPredicate = processFilter(filter, theResourceName, theRequest, theRequestPartitionId);
//								myQueryStack.clearPredicates();
//								myQueryStack.addPredicates(holdPredicates);
//
//								mySqlBuilder.addPredicate(filterPredicate);
//
//								// Because filters can have an OR at the root, we never know for sure that we haven't done an optimized
//								// search that doesn't check the resource type. This could be improved in the future, but for now it's
//								// safest to just clear this flag. The test "testRetrieveDifferentTypeEq" will fail if we don't clear
//								// this here.
//								myQueryStack.clearHasImplicitTypeSelection();
//							}
//						}

					} else {
						String validNames = new TreeSet<>(mySearchParamRegistry.getActiveSearchParams(theResourceName).keySet()).toString();
						String msg = myFhirContext.getLocalizer().getMessageSanitized(BaseHapiFhirResourceDao.class, "invalidSearchParameter", theParamName, theResourceName, validNames);
						throw new InvalidRequestException(msg);
					}
				}
				break;
		}
	}


	private static SearchFilterParser.CompareOperation toOperation(ParamPrefixEnum thePrefix) {
		if (thePrefix != null) {
			switch (thePrefix) {
				case APPROXIMATE:
					return SearchFilterParser.CompareOperation.ap;
				case EQUAL:
					return SearchFilterParser.CompareOperation.eq;
				case GREATERTHAN:
					return SearchFilterParser.CompareOperation.gt;
				case GREATERTHAN_OR_EQUALS:
					return SearchFilterParser.CompareOperation.ge;
				case LESSTHAN:
					return SearchFilterParser.CompareOperation.lt;
				case LESSTHAN_OR_EQUALS:
					return SearchFilterParser.CompareOperation.le;
				case NOT_EQUAL:
					return SearchFilterParser.CompareOperation.ne;
			}
		}
		return SearchFilterParser.CompareOperation.eq;
	}


}
