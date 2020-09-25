package ca.uhn.fhir.jpa.dao.search.querystack;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirResourceDao;
import ca.uhn.fhir.jpa.dao.SearchBuilder;
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
import ca.uhn.fhir.jpa.dao.search.sql.ResourceSqlTable;
import ca.uhn.fhir.jpa.dao.search.sql.SearchSqlBuilder;
import ca.uhn.fhir.jpa.dao.search.sql.SourcePredicateBuilder;
import ca.uhn.fhir.jpa.dao.search.sql.StringIndexTable;
import ca.uhn.fhir.jpa.dao.search.sql.TagPredicateBuilder;
import ca.uhn.fhir.jpa.dao.search.sql.TokenIndexTable;
import ca.uhn.fhir.jpa.dao.search.sql.UriIndexTable;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.model.entity.TagTypeEnum;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.extractor.BaseSearchParamExtractor;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.jpa.searchparam.util.SourceParam;
import ca.uhn.fhir.model.api.IQueryParameterAnd;
import ca.uhn.fhir.model.api.IQueryParameterOr;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.QualifiedParamList;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.CompositeParam;
import ca.uhn.fhir.rest.param.HasParam;
import ca.uhn.fhir.rest.param.NumberParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.param.ParameterUtil;
import ca.uhn.fhir.rest.param.QuantityParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.TokenParamModifier;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import com.google.common.collect.Lists;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.ComboCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.Expression;
import com.healthmarketscience.sqlbuilder.InCondition;
import com.healthmarketscience.sqlbuilder.SelectQuery;
import com.healthmarketscience.sqlbuilder.Subquery;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import org.apache.commons.lang3.tuple.Pair;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class QueryStack3 {

	private static final Logger ourLog = LoggerFactory.getLogger(QueryStack3.class);
	private final ModelConfig myModelConfig;
	private final FhirContext myFhirContext;
	private final SearchSqlBuilder mySqlBuilder;
	private final SearchParameterMap mySearchParameters;
	private final ISearchParamRegistry mySearchParamRegistry;
	private final PartitionSettings myPartitionSettings;
	private final DaoConfig myDaoConfig;
	// FIXME: rename to add "date" to the name
	private Map<String, DateIndexTable> myJoinMap;

	/**
	 * Constructor
	 */
	public QueryStack3(SearchParameterMap theSearchParameters, DaoConfig theDaoConfig, ModelConfig theModelConfig, FhirContext theFhirContext, SearchSqlBuilder theSqlBuilder, ISearchParamRegistry theSearchParamRegistry, PartitionSettings thePartitionSettings) {
		myPartitionSettings = thePartitionSettings;
		assert theSearchParameters != null;
		assert theDaoConfig != null;
		assert theModelConfig != null;
		assert theFhirContext != null;
		assert theSqlBuilder != null;

		mySearchParameters = theSearchParameters;
		myDaoConfig = theDaoConfig;
		myModelConfig = theModelConfig;
		myFhirContext = theFhirContext;
		mySqlBuilder = theSqlBuilder;
		mySearchParamRegistry = theSearchParamRegistry;
	}



	private Condition createPredicateComposite(@Nullable DbColumn theSourceJoinColumn, String theResourceName, RuntimeSearchParam theParamDef, List<? extends IQueryParameterType> theNextAnd, RequestPartitionId theRequestPartitionId) {
		// TODO: fail if missing is set for a composite query

		IQueryParameterType or = theNextAnd.get(0);
		if (!(or instanceof CompositeParam<?, ?>)) {
			throw new InvalidRequestException("Invalid type for composite param (must be " + CompositeParam.class.getSimpleName() + ": " + or.getClass());
		}
		CompositeParam<?, ?> cp = (CompositeParam<?, ?>) or;

		RuntimeSearchParam left = theParamDef.getCompositeOf().get(0);
		IQueryParameterType leftValue = cp.getLeftValue();
		Condition leftPredicate = createPredicateCompositePart(theSourceJoinColumn, theResourceName, left, leftValue, theRequestPartitionId);

		RuntimeSearchParam right = theParamDef.getCompositeOf().get(1);
		IQueryParameterType rightValue = cp.getRightValue();
		Condition rightPredicate = createPredicateCompositePart(theSourceJoinColumn, theResourceName, right, rightValue, theRequestPartitionId);

		return toAndPredicate(leftPredicate, rightPredicate);
	}

	private Condition createPredicateCompositePart(@Nullable DbColumn theSourceJoinColumn, String theResourceName, RuntimeSearchParam theParam, IQueryParameterType theParamValue, RequestPartitionId theRequestPartitionId) {

		switch (theParam.getParamType()) {
			case STRING: {
				return createPredicateString(theSourceJoinColumn, theResourceName, theParam, Collections.singletonList(theParamValue), null, theRequestPartitionId);
			}
			case TOKEN: {
				return createPredicateToken(theSourceJoinColumn, theResourceName, theParam, Collections.singletonList(theParamValue), null, theRequestPartitionId);
			}
			case DATE: {
				return createPredicateDate(theSourceJoinColumn, theResourceName, theParam, Collections.singletonList(theParamValue), null, theRequestPartitionId);
			}
			case QUANTITY: {
				return createPredicateQuantity(theSourceJoinColumn, theResourceName, theParam, Collections.singletonList(theParamValue), null, theRequestPartitionId);
			}
		}

		throw new InvalidRequestException("Don't know how to handle composite parameter with type of " + theParam.getParamType());
	}


	public Condition createPredicateCoords(@Nullable DbColumn theSourceJoinColumn,
														String theResourceName,
														RuntimeSearchParam theSearchParam,
														List<? extends IQueryParameterType> theList,
														SearchFilterParser.CompareOperation theOperation,
														RequestPartitionId theRequestPartitionId) {

		CoordsIndexTable join = mySqlBuilder.addCoordsSelector(theSourceJoinColumn);

		if (theList.get(0).getMissing() != null) {
			return createPredicateParamMissingForNonReference(theResourceName, theSearchParam.getName(), theList.get(0).getMissing(), join, theRequestPartitionId);
		}

		List<Condition> codePredicates = new ArrayList<>();
		for (IQueryParameterType nextOr : theList) {
			Condition singleCode = join.createPredicateCoords(mySearchParameters, nextOr, theResourceName, theSearchParam, join, theRequestPartitionId);
			codePredicates.add(singleCode);
		}

		return join.combineWithRequestPartitionIdPredicate(theRequestPartitionId, ComboCondition.or(codePredicates.toArray(new Condition[0])));
	}

	public Condition createPredicateDate(@Nullable DbColumn theSourceJoinColumn,
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

		DateIndexTable join;
		if (theSourceJoinColumn != null) {
			join = mySqlBuilder.addDateSelector(theSourceJoinColumn);
			newJoin = true;
		} else {
			join = myJoinMap.get(key);
			if (join == null) {
				join = mySqlBuilder.addDateSelector(theSourceJoinColumn);
				myJoinMap.put(key, join);
				newJoin = true;
			}
		}

		if (theList.get(0).getMissing() != null) {
			Boolean missing = theList.get(0).getMissing();
			return createPredicateParamMissingForNonReference(theResourceName, paramName, missing, join, theRequestPartitionId);
		}

		List<Condition> codePredicates = new ArrayList<>();

		for (IQueryParameterType nextOr : theList) {
			Condition p = join.createPredicateDateWithoutIdentityPredicate(nextOr, theResourceName, paramName, join, theOperation, theRequestPartitionId);
			codePredicates.add(p);
		}

		Condition predicate = toOrPredicate(codePredicates);

		if (newJoin) {
			predicate = join.combineParamIndexPredicateWithParamNamePredicate(theResourceName, paramName, predicate, theRequestPartitionId);
			predicate = join.combineWithRequestPartitionIdPredicate(theRequestPartitionId, predicate);
		}

		return predicate;

	}

	private Condition createPredicateHas(@Nullable DbColumn theSourceJoinColumn, String theResourceType, List<List<IQueryParameterType>> theHasParameters, RequestDetails theRequest, RequestPartitionId theRequestPartitionId) {

		List<Condition> andPredicates = new ArrayList<>();
		for (List<? extends IQueryParameterType> nextOrList : theHasParameters) {

			String targetResourceType = null;
			String paramReference = null;
			String parameterName = null;

			String paramName = null;
			List<QualifiedParamList> parameters = new ArrayList<>();
			for (IQueryParameterType nextParam : nextOrList) {
				HasParam next = (HasParam) nextParam;
				targetResourceType = next.getTargetResourceType();
				paramReference = next.getReferenceFieldName();
				parameterName = next.getParameterName();
				paramName = parameterName.replaceAll("\\..*", "");
				parameters.add(QualifiedParamList.singleton(null, next.getValueAsQueryToken(myFhirContext)));
			}

			if (paramName == null) {
				continue;
			}

			RuntimeResourceDefinition targetResourceDefinition;
			try {
				targetResourceDefinition = myFhirContext.getResourceDefinition(targetResourceType);
			} catch (DataFormatException e) {
				throw new InvalidRequestException("Invalid resource type: " + targetResourceType);
			}

			ArrayList<IQueryParameterType> orValues = Lists.newArrayList();

			if (paramName.startsWith("_has:")) {

				ourLog.trace("Handing double _has query: {}", paramName);

				String qualifier = paramName.substring(4);
				paramName = Constants.PARAM_HAS;
				for (IQueryParameterType next : nextOrList) {
					HasParam nextHasParam = new HasParam();
					nextHasParam.setValueAsQueryToken(myFhirContext, Constants.PARAM_HAS, qualifier, next.getValueAsQueryToken(myFhirContext));
					orValues.add(nextHasParam);
				}

			} else {

				//Ensure that the name of the search param
				// (e.g. the `code` in Patient?_has:Observation:subject:code=sys|val)
				// exists on the target resource type.
				RuntimeSearchParam owningParameterDef = mySearchParamRegistry.getSearchParamByName(targetResourceDefinition, paramName);
				if (owningParameterDef == null) {
					throw new InvalidRequestException("Unknown parameter name: " + targetResourceType + ':' + parameterName);
				}

				//Ensure that the name of the back-referenced search param on the target (e.g. the `subject` in Patient?_has:Observation:subject:code=sys|val)
				//exists on the target resource.
				owningParameterDef = mySearchParamRegistry.getSearchParamByName(targetResourceDefinition, paramReference);
				if (owningParameterDef == null) {
					throw new InvalidRequestException("Unknown parameter name: " + targetResourceType + ':' + paramReference);
				}

				RuntimeSearchParam paramDef = mySearchParamRegistry.getSearchParamByName(targetResourceDefinition, paramName);
				IQueryParameterAnd<IQueryParameterOr<IQueryParameterType>> parsedParam = (IQueryParameterAnd<IQueryParameterOr<IQueryParameterType>>) ParameterUtil.parseQueryParams(myFhirContext, paramDef, paramName, parameters);

				for (IQueryParameterOr<IQueryParameterType> next : parsedParam.getValuesAsQueryTokens()) {
					orValues.addAll(next.getValuesAsQueryTokens());
				}

			}

			//Handle internal chain inside the has.
			if (parameterName.contains(".")) {
				String chainedPartOfParameter = getChainedPart(parameterName);
				orValues.stream()
					.filter(qp -> qp instanceof ReferenceParam)
					.map(qp -> (ReferenceParam) qp)
					.forEach(rp -> rp.setChain(getChainedPart(chainedPartOfParameter)));

				parameterName = parameterName.substring(0, parameterName.indexOf('.'));
			}

			int colonIndex = parameterName.indexOf(':');
			if (colonIndex != -1) {
				parameterName = parameterName.substring(0, colonIndex);
			}

			ResourceLinkIndexTable join = mySqlBuilder.addReferenceSelectorReversed(this, theSourceJoinColumn);
			List<String> paths = join.createResourceLinkPaths(targetResourceType, paramReference);
			Condition typePredicate = BinaryCondition.equalTo(join.getColumnTargetResourceType(), mySqlBuilder.generatePlaceholder(theResourceType));
			Condition pathPredicate = new InCondition(join.getColumnSourcePath(), mySqlBuilder.generatePlaceholders(paths));
			Condition linkedPredicate = searchForIdsWithAndOr(join.getColumnSrcResourceId(), targetResourceType, parameterName, Collections.singletonList(orValues), theRequest, theRequestPartitionId);
			andPredicates.add(toAndPredicate(pathPredicate, typePredicate, linkedPredicate));
		}

		return toAndPredicate(andPredicates);
	}


	public Condition createPredicateLanguage(List<List<IQueryParameterType>> theList, Object theOperation) {

		ResourceSqlTable rootTable = mySqlBuilder.getOrCreateResourceTableRoot();

		List<Condition> predicates = new ArrayList<>();
		for (List<? extends IQueryParameterType> nextList : theList) {

			Set<String> values = new HashSet<>();
			for (IQueryParameterType next : nextList) {
				if (next instanceof StringParam) {
					String nextValue = ((StringParam) next).getValue();
					if (isBlank(nextValue)) {
						continue;
					}
					values.add(nextValue);
				} else {
					throw new InternalErrorException("Language parameter must be of type " + StringParam.class.getCanonicalName() + " - Got " + next.getClass().getCanonicalName());
				}
			}

			if (values.isEmpty()) {
				continue;
			}

			if ((theOperation == null) ||
				(theOperation == SearchFilterParser.CompareOperation.eq)) {
				predicates.add(rootTable.createLanguagePredicate(values, false));
			} else if (theOperation == SearchFilterParser.CompareOperation.ne) {
				predicates.add(rootTable.createLanguagePredicate(values, true));
			} else {
				throw new InvalidRequestException("Unsupported operator specified in language query, only \"eq\" and \"ne\" are supported");
			}

		}

		return toAndPredicate(predicates);
	}


	public Condition createPredicateNumber(@Nullable DbColumn theSourceJoinColumn,
														String theResourceName,
														RuntimeSearchParam theSearchParam,
														List<? extends IQueryParameterType> theList,
														SearchFilterParser.CompareOperation theOperation,
														RequestPartitionId theRequestPartitionId) {

		NumberIndexTable join = mySqlBuilder.addNumberSelector(theSourceJoinColumn);

		if (theList.get(0).getMissing() != null) {
			return createPredicateParamMissingForNonReference(theResourceName, theSearchParam.getName(), theList.get(0).getMissing(), join, theRequestPartitionId);
		}

		List<Condition> codePredicates = new ArrayList<>();
		for (IQueryParameterType nextOr : theList) {

			if (nextOr instanceof NumberParam) {
				NumberParam param = (NumberParam) nextOr;

				BigDecimal value = param.getValue();
				if (value == null) {
					continue;
				}

				SearchFilterParser.CompareOperation operation = theOperation;
				if (operation == null) {
					operation = toOperation(param.getPrefix());
				}

				Condition predicate = join.createPredicateNumeric(theResourceName, theSearchParam.getName(), join, null, nextOr, operation, value, null, null, theRequestPartitionId);
				codePredicates.add(predicate);

			} else {
				throw new IllegalArgumentException("Invalid token type: " + nextOr.getClass());
			}

		}

		return join.combineWithRequestPartitionIdPredicate(theRequestPartitionId, ComboCondition.or(codePredicates.toArray(new Condition[0])));
	}


	public Condition createPredicateQuantity(@Nullable DbColumn theSourceJoinColumn,
														  String theResourceName,
														  RuntimeSearchParam theSearchParam,
														  List<? extends IQueryParameterType> theList,
														  SearchFilterParser.CompareOperation theOperation,
														  RequestPartitionId theRequestPartitionId) {

		QuantityIndexTable join = mySqlBuilder.addQuantity(theSourceJoinColumn);

		if (theList.get(0).getMissing() != null) {
			return createPredicateParamMissingForNonReference(theResourceName, theSearchParam.getName(), theList.get(0).getMissing(), join, theRequestPartitionId);
		}

		List<Condition> codePredicates = new ArrayList<>();
		for (IQueryParameterType nextOr : theList) {
			Condition singleCode = join.createPredicateQuantity(nextOr, theResourceName, theSearchParam.getName(), null, join, theOperation, theRequestPartitionId);
			codePredicates.add(singleCode);
		}

		return join.combineWithRequestPartitionIdPredicate(theRequestPartitionId, ComboCondition.or(codePredicates.toArray(new Condition[0])));
	}


	public Condition createPredicateReference(@Nullable DbColumn theSourceJoinColumn,
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
		return join.createPredicate(theRequest, theResourceName, theParamName, theList, theOperation, theRequestPartitionId);
	}


	@Nullable
	public Condition createPredicateResourceId(List<List<IQueryParameterType>> theValues, String theResourceName, SearchFilterParser.CompareOperation theOperation, RequestPartitionId theRequestPartitionId) {
		ResourceIdPredicateBuilder3 builder = mySqlBuilder.newResourceIdBuilder();
		return builder.createPredicateResourceId(theResourceName, theValues, theOperation, theRequestPartitionId);
	}


	private Condition createPredicateSource(@Nullable DbColumn theSourceJoinColumn, List<List<IQueryParameterType>> theAndOrParams, RequestDetails theRequest) {
		List<Condition> andPredicates = new ArrayList<>(theAndOrParams.size());
		for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
			andPredicates.add(addPredicateSource(theSourceJoinColumn, nextAnd, SearchFilterParser.CompareOperation.eq, theRequest));
		}
		return toAndPredicate(andPredicates);
	}

	private Condition addPredicateSource(@Nullable DbColumn theSourceJoinColumn, List<? extends IQueryParameterType> theList, SearchFilterParser.CompareOperation theOperation, RequestDetails theRequest) {
		// Required for now
		assert theOperation == SearchFilterParser.CompareOperation.eq;

		if (myDaoConfig.getStoreMetaSourceInformation() == DaoConfig.StoreMetaSourceInformationEnum.NONE) {
			String msg = myFhirContext.getLocalizer().getMessage(SearchBuilder.class, "sourceParamDisabled");
			throw new InvalidRequestException(msg);
		}

		SourcePredicateBuilder join = mySqlBuilder.addSourcePredicateBuilder(theSourceJoinColumn);

		List<Condition> orPredicates = new ArrayList<>();
		for (IQueryParameterType nextParameter : theList) {
			SourceParam sourceParameter = new SourceParam(nextParameter.getValueAsQueryToken(myFhirContext));
			String sourceUri = sourceParameter.getSourceUri();
			String requestId = sourceParameter.getRequestId();
			if (isNotBlank(sourceUri) && isNotBlank(requestId)) {
				orPredicates.add(toAndPredicate(
					join.createPredicateSourceUri(sourceUri),
					join.createPredicateRequestId(requestId)
				));
			} else if (isNotBlank(sourceUri)) {
				orPredicates.add(join.createPredicateSourceUri(sourceUri));
			} else if (isNotBlank(requestId)) {
				orPredicates.add(join.createPredicateRequestId(requestId));
			}
		}

		return toOrPredicate(orPredicates);
	}




	public Condition createPredicateString(@Nullable DbColumn theSourceJoinColumn,
														String theResourceName,
														RuntimeSearchParam theSearchParam,
														List<? extends IQueryParameterType> theList,
														SearchFilterParser.CompareOperation theOperation,
														RequestPartitionId theRequestPartitionId) {

		StringIndexTable join = mySqlBuilder.addStringSelector(theSourceJoinColumn);

		if (theList.get(0).getMissing() != null) {
			return createPredicateParamMissingForNonReference(theResourceName, theSearchParam.getName(), theList.get(0).getMissing(), join, theRequestPartitionId);
		}

		List<Condition> codePredicates = new ArrayList<>();
		for (IQueryParameterType nextOr : theList) {
			Condition singleCode = join.createPredicateString(nextOr, theResourceName, theSearchParam, join, theOperation);
			codePredicates.add(singleCode);
		}

		return join.combineWithRequestPartitionIdPredicate(theRequestPartitionId, ComboCondition.or(codePredicates.toArray(new Condition[0])));
	}


	public Condition createPredicateTag(@Nullable DbColumn theSourceJoinColumn, List<List<IQueryParameterType>> theList, String theParamName, RequestPartitionId theRequestPartitionId) {
		TagTypeEnum tagType;
		if (Constants.PARAM_TAG.equals(theParamName)) {
			tagType = TagTypeEnum.TAG;
		} else if (Constants.PARAM_PROFILE.equals(theParamName)) {
			tagType = TagTypeEnum.PROFILE;
		} else if (Constants.PARAM_SECURITY.equals(theParamName)) {
			tagType = TagTypeEnum.SECURITY_LABEL;
		} else {
			throw new IllegalArgumentException("Param name: " + theParamName); // shouldn't happen
		}

		List<Pair<String, String>> notTags = Lists.newArrayList();
		for (List<? extends IQueryParameterType> nextAndParams : theList) {
			for (IQueryParameterType nextOrParams : nextAndParams) {
				if (nextOrParams instanceof TokenParam) {
					TokenParam param = (TokenParam) nextOrParams;
					if (param.getModifier() == TokenParamModifier.NOT) {
						if (isNotBlank(param.getSystem()) || isNotBlank(param.getValue())) {
							notTags.add(Pair.of(param.getSystem(), param.getValue()));
						}
					}
				}
			}
		}

		List<Condition> andPredicates = new ArrayList<>();
		for (List<? extends IQueryParameterType> nextAndParams : theList) {
			boolean haveTags = false;
			for (IQueryParameterType nextParamUncasted : nextAndParams) {
				if (nextParamUncasted instanceof TokenParam) {
					TokenParam nextParam = (TokenParam) nextParamUncasted;
					if (isNotBlank(nextParam.getValue())) {
						haveTags = true;
					} else if (isNotBlank(nextParam.getSystem())) {
						throw new InvalidRequestException("Invalid " + theParamName + " parameter (must supply a value/code and not just a system): " + nextParam.getValueAsQueryToken(myFhirContext));
					}
				} else {
					UriParam nextParam = (UriParam) nextParamUncasted;
					if (isNotBlank(nextParam.getValue())) {
						haveTags = true;
					}
				}
			}
			if (!haveTags) {
				continue;
			}

			boolean paramInverted = false;
			List<Pair<String, String>> tokens = Lists.newArrayList();
			for (IQueryParameterType nextOrParams : nextAndParams) {
				String code;
				String system;
				if (nextOrParams instanceof TokenParam) {
					TokenParam nextParam = (TokenParam) nextOrParams;
					code = nextParam.getValue();
					system = nextParam.getSystem();
					if (nextParam.getModifier() == TokenParamModifier.NOT) {
						paramInverted = true;
					}
				} else {
					UriParam nextParam = (UriParam) nextOrParams;
					code = nextParam.getValue();
					system = null;
				}

				if (isNotBlank(code)) {
					tokens.add(Pair.of(system, code));
				}
			}

			if (tokens.isEmpty()) {
				continue;
			}

			Condition tagPredicate;
			BaseIndexTable join;
			if (paramInverted) {

				SearchSqlBuilder sqlBuilder = mySqlBuilder.newChildSqlBuilder();
				TagPredicateBuilder tagSelector = sqlBuilder.addTagSelector(null);
				sqlBuilder.addPredicate_(tagSelector.createPredicateTag(tagType, tokens, theParamName, theRequestPartitionId));
				SelectQuery sql = sqlBuilder.getSelect();

				join = mySqlBuilder.getOrCreateQueryRootTable();
				Expression subSelect = new Subquery(sql);
				tagPredicate = new InCondition(join.getResourceIdColumn(), subSelect).setNegate(true);

			} else {
				TagPredicateBuilder tagJoin = mySqlBuilder.addTagSelector(theSourceJoinColumn);
				tagPredicate = tagJoin.createPredicateTag(tagType, tokens, theParamName, theRequestPartitionId);
				join = tagJoin;
			}

			andPredicates.add(join.combineWithRequestPartitionIdPredicate(theRequestPartitionId, tagPredicate));
		}

		return toAndPredicate(andPredicates);
	}

	public Condition createPredicateToken(@Nullable DbColumn theSourceJoinColumn,
													  String theResourceName,
													  RuntimeSearchParam theSearchParam,
													  List<? extends IQueryParameterType> theList,
													  SearchFilterParser.CompareOperation theOperation,
													  RequestPartitionId theRequestPartitionId) {

		if (theList.get(0).getMissing() != null) {
			return createPredicateParamMissingForNonReference(theResourceName, theSearchParam.getName(), theList.get(0).getMissing(), mySqlBuilder.addTokenSelector(theSourceJoinColumn), theRequestPartitionId);
		}

		SearchFilterParser.CompareOperation operation = theOperation;
		List<IQueryParameterType> tokens = new ArrayList<>();
		for (IQueryParameterType nextOr : theList) {

			if (nextOr instanceof TokenParam && !((TokenParam) nextOr).isEmpty()) {
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

					return createPredicateString(theSourceJoinColumn, theResourceName, theSearchParam, theList, null, theRequestPartitionId);
				}

			}

			tokens.add(nextOr);
		}

		if (tokens.isEmpty()) {
			return null;
		}

		TokenIndexTable join = mySqlBuilder.addTokenSelector(theSourceJoinColumn);
		Condition predicate = join.createPredicateToken(tokens, theResourceName, theSearchParam, operation, theRequestPartitionId);
		return join.combineWithRequestPartitionIdPredicate(theRequestPartitionId, predicate);
	}

	public Condition createPredicateUri(@Nullable DbColumn theSourceJoinColumn,
													String theResourceName,
													RuntimeSearchParam theSearchParam,
													List<? extends IQueryParameterType> theList,
													SearchFilterParser.CompareOperation theOperation,
													RequestPartitionId theRequestPartitionId) {

		String paramName = theSearchParam.getName();
		UriIndexTable join = mySqlBuilder.addUriSelector(theSourceJoinColumn);

		if (theList.get(0).getMissing() != null) {
			return createPredicateParamMissingForNonReference(theResourceName, paramName, theList.get(0).getMissing(), join, theRequestPartitionId);
		}

		Condition predicate = join.addPredicate(theList, paramName, theOperation);
		return join.combineWithRequestPartitionIdPredicate(theRequestPartitionId, predicate);
	}

	private Condition createPredicateParamMissingForNonReference(String theResourceName, String theParamName, Boolean theMissing, BaseSearchParamIndexTable theJoin, RequestPartitionId theRequestPartitionId) {
		// FIXME: implement
		throw new UnsupportedOperationException();
	}

	@Nullable
	public Condition searchForIdsWithAndOr(@Nullable DbColumn theSourceJoinColumn, String theResourceName, String theParamName, List<List<IQueryParameterType>> theAndOrParams, RequestDetails theRequest, RequestPartitionId theRequestPartitionId) {

		if (theAndOrParams.isEmpty()) {
			return null;
		}

		switch (theParamName) {
			case IAnyResource.SP_RES_ID:
				return createPredicateResourceId(theAndOrParams, theResourceName, null, theRequestPartitionId);

			case IAnyResource.SP_RES_LANGUAGE:
				return createPredicateLanguage(theAndOrParams, null);

			case Constants.PARAM_HAS:
				return createPredicateHas(theSourceJoinColumn, theResourceName, theAndOrParams, theRequest, theRequestPartitionId);

			case Constants.PARAM_TAG:
			case Constants.PARAM_PROFILE:
			case Constants.PARAM_SECURITY:
				return createPredicateTag(theSourceJoinColumn, theAndOrParams, theParamName, theRequestPartitionId);

			case Constants.PARAM_SOURCE:
				return createPredicateSource(theSourceJoinColumn, theAndOrParams, theRequest);

			default:

				List<Condition> andPredicates = new ArrayList<>();
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
								andPredicates.add(createPredicateDate(theSourceJoinColumn, theResourceName, nextParamDef, nextAnd, null, theRequestPartitionId));
							}
							break;
						case QUANTITY:
							for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
								SearchFilterParser.CompareOperation operation = null;
								if (nextAnd.size() > 0) {
									QuantityParam param = (QuantityParam) nextAnd.get(0);
									operation = toOperation(param.getPrefix());
								}
								andPredicates.add(createPredicateQuantity(theSourceJoinColumn, theResourceName, nextParamDef, nextAnd, operation, theRequestPartitionId));
							}
							break;
						case REFERENCE:
							for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
								andPredicates.add(createPredicateReference(theSourceJoinColumn, theResourceName, theParamName, nextAnd, null, theRequest, theRequestPartitionId));
							}
							break;
						case STRING:
							for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
								andPredicates.add(createPredicateString(theSourceJoinColumn, theResourceName, nextParamDef, nextAnd, SearchFilterParser.CompareOperation.sw, theRequestPartitionId));
							}
							break;
						case TOKEN:
							for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
								if ("Location.position".equals(nextParamDef.getPath())) {
									andPredicates.add(createPredicateCoords(theSourceJoinColumn, theResourceName, nextParamDef, nextAnd, null, theRequestPartitionId));
								} else {
									andPredicates.add(createPredicateToken(theSourceJoinColumn, theResourceName, nextParamDef, nextAnd, null, theRequestPartitionId));
								}
							}
							break;
						case NUMBER:
							for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
								andPredicates.add(createPredicateNumber(theSourceJoinColumn, theResourceName, nextParamDef, nextAnd, null, theRequestPartitionId));
							}
							break;
						case COMPOSITE:
							for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
								andPredicates.add(createPredicateComposite(theSourceJoinColumn, theResourceName, nextParamDef, nextAnd, theRequestPartitionId));
							}
							break;
						case URI:
							for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
								andPredicates.add(createPredicateUri(theSourceJoinColumn, theResourceName, nextParamDef, nextAnd, SearchFilterParser.CompareOperation.eq, theRequestPartitionId));
							}
							break;
						case HAS:
						case SPECIAL:
							for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
								if ("Location.position".equals(nextParamDef.getPath())) {
									andPredicates.add(createPredicateCoords(theSourceJoinColumn, theResourceName, nextParamDef, nextAnd, null, theRequestPartitionId));
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

				return toAndPredicate(andPredicates);

		}
	}

	public void addPredicateEverythingOperation(String theResourceName, Long theTargetPid) {
		ResourceLinkIndexTable table = mySqlBuilder.addReferenceSelector(this, null);
		Condition predicate = table.createEverythingPredicate(theResourceName, theTargetPid);
		mySqlBuilder.addPredicate_(predicate);
	}

	public static Condition toAndPredicate(List<Condition> theAndPredicates) {
		theAndPredicates.removeIf(t -> t == null);
		if (theAndPredicates.size() == 0) {
			return null;
		} else if (theAndPredicates.size() == 1) {
			return theAndPredicates.get(0);
		} else {
			return ComboCondition.and(theAndPredicates.toArray(new Condition[0]));
		}
	}

	public static Condition toOrPredicate(List<Condition> theOrPredicates) {
		theOrPredicates.removeIf(t -> t == null);
		if (theOrPredicates.size() == 0) {
			return null;
		} else if (theOrPredicates.size() == 1) {
			return theOrPredicates.get(0);
		} else {
			return ComboCondition.or(theOrPredicates.toArray(new Condition[0]));
		}
	}

	public static Condition toOrPredicate(Condition... theOrPredicates) {
		return toOrPredicate(Arrays.asList(theOrPredicates));
	}

	public static Condition toAndPredicate(Condition... theAndPredicates) {
		return toAndPredicate(Arrays.asList(theAndPredicates));
	}

	public static SearchFilterParser.CompareOperation toOperation(ParamPrefixEnum thePrefix) {
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


	private static String getChainedPart(String parameter) {
		return parameter.substring(parameter.indexOf(".") + 1);
	}


}
