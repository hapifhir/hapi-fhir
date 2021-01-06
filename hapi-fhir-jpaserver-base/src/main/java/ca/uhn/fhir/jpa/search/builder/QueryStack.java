package ca.uhn.fhir.jpa.search.builder;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirResourceDao;
import ca.uhn.fhir.jpa.dao.LegacySearchBuilder;
import ca.uhn.fhir.jpa.dao.predicate.PredicateBuilderToken;
import ca.uhn.fhir.jpa.dao.predicate.SearchFilterParser;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.model.entity.TagTypeEnum;
import ca.uhn.fhir.jpa.search.builder.predicate.BaseJoiningPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.CompositeUniqueSearchParameterPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.CoordsPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.DatePredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.ForcedIdPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.NumberPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.QuantityPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.ResourceIdPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.ResourceLinkPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.ResourceTablePredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.SearchParamPresentPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.SourcePredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.StringPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.TagPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.TokenPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.UriPredicateBuilder;
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
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.CompositeParam;
import ca.uhn.fhir.rest.param.DateParam;
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
import com.healthmarketscience.sqlbuilder.OrderObject;
import com.healthmarketscience.sqlbuilder.SelectQuery;
import com.healthmarketscience.sqlbuilder.Subquery;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.tuple.Pair;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class QueryStack {

	private static final Logger ourLog = LoggerFactory.getLogger(QueryStack.class);
	private final ModelConfig myModelConfig;
	private final FhirContext myFhirContext;
	private final SearchQueryBuilder mySqlBuilder;
	private final SearchParameterMap mySearchParameters;
	private final ISearchParamRegistry mySearchParamRegistry;
	private final PartitionSettings myPartitionSettings;
	private final DaoConfig myDaoConfig;
	private final EnumSet<PredicateBuilderTypeEnum> myReusePredicateBuilderTypes;
	private Map<PredicateBuilderCacheKey, BaseJoiningPredicateBuilder> myJoinMap;

	/**
	 * Constructor
	 */
	public QueryStack(SearchParameterMap theSearchParameters, DaoConfig theDaoConfig, ModelConfig theModelConfig, FhirContext theFhirContext, SearchQueryBuilder theSqlBuilder, ISearchParamRegistry theSearchParamRegistry, PartitionSettings thePartitionSettings) {
		this(theSearchParameters, theDaoConfig, theModelConfig, theFhirContext, theSqlBuilder, theSearchParamRegistry, thePartitionSettings, EnumSet.of(PredicateBuilderTypeEnum.DATE));
	}

	/**
	 * Constructor
	 */
	private QueryStack(SearchParameterMap theSearchParameters, DaoConfig theDaoConfig, ModelConfig theModelConfig, FhirContext theFhirContext, SearchQueryBuilder theSqlBuilder, ISearchParamRegistry theSearchParamRegistry, PartitionSettings thePartitionSettings, EnumSet<PredicateBuilderTypeEnum> theReusePredicateBuilderTypes) {
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
		myReusePredicateBuilderTypes = theReusePredicateBuilderTypes;
	}

	public void addSortOnDate(String theResourceName, String theParamName, boolean theAscending) {
		BaseJoiningPredicateBuilder firstPredicateBuilder = mySqlBuilder.getOrCreateFirstPredicateBuilder();
		DatePredicateBuilder sortPredicateBuilder = mySqlBuilder.addDatePredicateBuilder(firstPredicateBuilder.getResourceIdColumn());

		Condition hashIdentityPredicate = sortPredicateBuilder.createHashIdentityPredicate(theResourceName, theParamName);
		mySqlBuilder.addPredicate(hashIdentityPredicate);
		mySqlBuilder.addSortDate(sortPredicateBuilder.getColumnValueLow(), theAscending);
	}

	public void addSortOnLastUpdated(boolean theAscending) {
		ResourceTablePredicateBuilder resourceTablePredicateBuilder;
		BaseJoiningPredicateBuilder firstPredicateBuilder = mySqlBuilder.getOrCreateFirstPredicateBuilder();
		if (firstPredicateBuilder instanceof ResourceTablePredicateBuilder) {
			resourceTablePredicateBuilder = (ResourceTablePredicateBuilder) firstPredicateBuilder;
		} else {
			resourceTablePredicateBuilder = mySqlBuilder.addResourceTablePredicateBuilder(firstPredicateBuilder.getResourceIdColumn());
		}
		mySqlBuilder.addSortDate(resourceTablePredicateBuilder.getColumnLastUpdated(), theAscending);
	}


	public void addSortOnNumber(String theResourceName, String theParamName, boolean theAscending) {
		BaseJoiningPredicateBuilder firstPredicateBuilder = mySqlBuilder.getOrCreateFirstPredicateBuilder();
		NumberPredicateBuilder sortPredicateBuilder = mySqlBuilder.addNumberPredicateBuilder(firstPredicateBuilder.getResourceIdColumn());

		Condition hashIdentityPredicate = sortPredicateBuilder.createHashIdentityPredicate(theResourceName, theParamName);
		mySqlBuilder.addPredicate(hashIdentityPredicate);
		mySqlBuilder.addSortNumeric(sortPredicateBuilder.getColumnValue(), theAscending);
	}

	public void addSortOnQuantity(String theResourceName, String theParamName, boolean theAscending) {
		BaseJoiningPredicateBuilder firstPredicateBuilder = mySqlBuilder.getOrCreateFirstPredicateBuilder();
		QuantityPredicateBuilder sortPredicateBuilder = mySqlBuilder.addQuantityPredicateBuilder(firstPredicateBuilder.getResourceIdColumn());

		Condition hashIdentityPredicate = sortPredicateBuilder.createHashIdentityPredicate(theResourceName, theParamName);
		mySqlBuilder.addPredicate(hashIdentityPredicate);
		mySqlBuilder.addSortNumeric(sortPredicateBuilder.getColumnValue(), theAscending);
	}

	public void addSortOnResourceId(boolean theAscending) {
		BaseJoiningPredicateBuilder firstPredicateBuilder = mySqlBuilder.getOrCreateFirstPredicateBuilder();
		ForcedIdPredicateBuilder sortPredicateBuilder = mySqlBuilder.addForcedIdPredicateBuilder(firstPredicateBuilder.getResourceIdColumn());
		if (!theAscending) {
			mySqlBuilder.addSortString(sortPredicateBuilder.getColumnForcedId(), false, OrderObject.NullOrder.FIRST);
		} else {
			mySqlBuilder.addSortString(sortPredicateBuilder.getColumnForcedId(), true);
		}
		mySqlBuilder.addSortNumeric(firstPredicateBuilder.getResourceIdColumn(), theAscending);

	}

	public void addSortOnResourceLink(String theResourceName, String theParamName, boolean theAscending) {
		BaseJoiningPredicateBuilder firstPredicateBuilder = mySqlBuilder.getOrCreateFirstPredicateBuilder();
		ResourceLinkPredicateBuilder sortPredicateBuilder = mySqlBuilder.addReferencePredicateBuilder(this, firstPredicateBuilder.getResourceIdColumn());

		Condition pathPredicate = sortPredicateBuilder.createPredicateSourcePaths(theResourceName, theParamName);
		mySqlBuilder.addPredicate(pathPredicate);
		mySqlBuilder.addSortNumeric(sortPredicateBuilder.getColumnTargetResourceId(), theAscending);
	}


	public void addSortOnString(String theResourceName, String theParamName, boolean theAscending) {
		BaseJoiningPredicateBuilder firstPredicateBuilder = mySqlBuilder.getOrCreateFirstPredicateBuilder();
		StringPredicateBuilder sortPredicateBuilder = mySqlBuilder.addStringPredicateBuilder(firstPredicateBuilder.getResourceIdColumn());

		Condition hashIdentityPredicate = sortPredicateBuilder.createHashIdentityPredicate(theResourceName, theParamName);
		mySqlBuilder.addPredicate(hashIdentityPredicate);
		mySqlBuilder.addSortString(sortPredicateBuilder.getColumnValueNormalized(), theAscending);
	}

	public void addSortOnToken(String theResourceName, String theParamName, boolean theAscending) {
		BaseJoiningPredicateBuilder firstPredicateBuilder = mySqlBuilder.getOrCreateFirstPredicateBuilder();
		TokenPredicateBuilder sortPredicateBuilder = mySqlBuilder.addTokenPredicateBuilder(firstPredicateBuilder.getResourceIdColumn());

		Condition hashIdentityPredicate = sortPredicateBuilder.createHashIdentityPredicate(theResourceName, theParamName);
		mySqlBuilder.addPredicate(hashIdentityPredicate);
		mySqlBuilder.addSortString(sortPredicateBuilder.getColumnSystem(), theAscending);
		mySqlBuilder.addSortString(sortPredicateBuilder.getColumnValue(), theAscending);
	}

	public void addSortOnUri(String theResourceName, String theParamName, boolean theAscending) {
		BaseJoiningPredicateBuilder firstPredicateBuilder = mySqlBuilder.getOrCreateFirstPredicateBuilder();
		UriPredicateBuilder sortPredicateBuilder = mySqlBuilder.addUriPredicateBuilder(firstPredicateBuilder.getResourceIdColumn());

		Condition hashIdentityPredicate = sortPredicateBuilder.createHashIdentityPredicate(theResourceName, theParamName);
		mySqlBuilder.addPredicate(hashIdentityPredicate);
		mySqlBuilder.addSortString(sortPredicateBuilder.getColumnValue(), theAscending);
	}


	@SuppressWarnings("unchecked")
	private <T extends BaseJoiningPredicateBuilder> PredicateBuilderCacheLookupResult<T> createOrReusePredicateBuilder(PredicateBuilderTypeEnum theType, DbColumn theSourceJoinColumn, String theParamName, Supplier<T> theFactoryMethod) {
		boolean cacheHit = false;
		BaseJoiningPredicateBuilder retVal;
		if (myReusePredicateBuilderTypes.contains(theType)) {
			PredicateBuilderCacheKey key = new PredicateBuilderCacheKey(theSourceJoinColumn, theType, theParamName);
			if (myJoinMap == null) {
				myJoinMap = new HashMap<>();
			}
			retVal = myJoinMap.get(key);
			if (retVal != null) {
				cacheHit = true;
			} else {
				retVal = theFactoryMethod.get();
				myJoinMap.put(key, retVal);
			}
		} else {
			retVal = theFactoryMethod.get();
		}
		return new PredicateBuilderCacheLookupResult<>(cacheHit, (T) retVal);
	}

 	private Condition createPredicateComposite(@Nullable DbColumn theSourceJoinColumn, String theResourceName, RuntimeSearchParam theParamDef, List<? extends IQueryParameterType> theNextAnd, RequestPartitionId theRequestPartitionId) {

 		Condition orCondidtion = null;
		for (IQueryParameterType next : theNextAnd) {
			
			if (!(next instanceof CompositeParam<?, ?>)) {
				throw new InvalidRequestException("Invalid type for composite param (must be " + CompositeParam.class.getSimpleName() + ": " + next.getClass());
			}
			CompositeParam<?, ?> cp = (CompositeParam<?, ?>) next;
	
			RuntimeSearchParam left = theParamDef.getCompositeOf().get(0);
			IQueryParameterType leftValue = cp.getLeftValue();
			Condition leftPredicate = createPredicateCompositePart(theSourceJoinColumn, theResourceName, left, leftValue, theRequestPartitionId);
	
			RuntimeSearchParam right = theParamDef.getCompositeOf().get(1);
			IQueryParameterType rightValue = cp.getRightValue();
			Condition rightPredicate = createPredicateCompositePart(theSourceJoinColumn, theResourceName, right, rightValue, theRequestPartitionId);
	
			Condition andCondition = toAndPredicate(leftPredicate, rightPredicate);
			
			if (orCondidtion == null) {
				orCondidtion = toOrPredicate(andCondition);
			} else {
				orCondidtion = toOrPredicate(orCondidtion, andCondition);
			}
		}
		
		return orCondidtion;
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
														RequestPartitionId theRequestPartitionId) {

		CoordsPredicateBuilder predicateBuilder = createOrReusePredicateBuilder(PredicateBuilderTypeEnum.COORDS, theSourceJoinColumn, theSearchParam.getName(), () -> mySqlBuilder.addCoordsPredicateBuilder(theSourceJoinColumn)).getResult();

		if (theList.get(0).getMissing() != null) {
			return predicateBuilder.createPredicateParamMissingForNonReference(theResourceName, theSearchParam.getName(), theList.get(0).getMissing(), theRequestPartitionId);
		}

		List<Condition> codePredicates = new ArrayList<>();
		for (IQueryParameterType nextOr : theList) {
			Condition singleCode = predicateBuilder.createPredicateCoords(mySearchParameters, nextOr, theResourceName, theSearchParam, predicateBuilder, theRequestPartitionId);
			codePredicates.add(singleCode);
		}

		return predicateBuilder.combineWithRequestPartitionIdPredicate(theRequestPartitionId, ComboCondition.or(codePredicates.toArray(new Condition[0])));
	}

	public Condition createPredicateDate(@Nullable DbColumn theSourceJoinColumn,
													 String theResourceName,
													 RuntimeSearchParam theSearchParam,
													 List<? extends IQueryParameterType> theList,
													 SearchFilterParser.CompareOperation theOperation,
													 RequestPartitionId theRequestPartitionId) {

		String paramName = theSearchParam.getName();

		PredicateBuilderCacheLookupResult<DatePredicateBuilder> predicateBuilderLookupResult = createOrReusePredicateBuilder(PredicateBuilderTypeEnum.DATE, theSourceJoinColumn, paramName, () -> mySqlBuilder.addDatePredicateBuilder(theSourceJoinColumn));
		DatePredicateBuilder predicateBuilder = predicateBuilderLookupResult.getResult();
		boolean cacheHit = predicateBuilderLookupResult.isCacheHit();

		if (theList.get(0).getMissing() != null) {
			Boolean missing = theList.get(0).getMissing();
			return predicateBuilder.createPredicateParamMissingForNonReference(theResourceName, paramName, missing, theRequestPartitionId);
		}

		List<Condition> codePredicates = new ArrayList<>();

		for (IQueryParameterType nextOr : theList) {
			Condition p = predicateBuilder.createPredicateDateWithoutIdentityPredicate(nextOr, theResourceName, paramName, predicateBuilder, theOperation, theRequestPartitionId);
			codePredicates.add(p);
		}

		Condition predicate = toOrPredicate(codePredicates);

		if (!cacheHit) {
			predicate = predicateBuilder.combineWithHashIdentityPredicate(theResourceName, paramName, predicate);
			predicate = predicateBuilder.combineWithRequestPartitionIdPredicate(theRequestPartitionId, predicate);
		}

		return predicate;

	}

	private Condition createPredicateFilter(QueryStack theQueryStack3, SearchFilterParser.Filter theFilter, String theResourceName, RequestDetails theRequest, RequestPartitionId theRequestPartitionId) {

		if (theFilter instanceof SearchFilterParser.FilterParameter) {
			return createPredicateFilter(theQueryStack3, (SearchFilterParser.FilterParameter) theFilter, theResourceName, theRequest, theRequestPartitionId);
		} else if (theFilter instanceof SearchFilterParser.FilterLogical) {
			// Left side
			Condition xPredicate = createPredicateFilter(theQueryStack3, ((SearchFilterParser.FilterLogical) theFilter).getFilter1(), theResourceName, theRequest, theRequestPartitionId);

			// Right side
			Condition yPredicate = createPredicateFilter(theQueryStack3, ((SearchFilterParser.FilterLogical) theFilter).getFilter2(), theResourceName, theRequest, theRequestPartitionId);

			if (((SearchFilterParser.FilterLogical) theFilter).getOperation() == SearchFilterParser.FilterLogicalOperation.and) {
				return ComboCondition.and(xPredicate, yPredicate);
			} else if (((SearchFilterParser.FilterLogical) theFilter).getOperation() == SearchFilterParser.FilterLogicalOperation.or) {
				return ComboCondition.or(xPredicate, yPredicate);
			} else {
				// Shouldn't happen
				throw new InvalidRequestException("Don't know how to handle operation " + ((SearchFilterParser.FilterLogical) theFilter).getOperation());
			}
		} else {
			return createPredicateFilter(theQueryStack3, ((SearchFilterParser.FilterParameterGroup) theFilter).getContained(), theResourceName, theRequest, theRequestPartitionId);
		}
	}

	private Condition createPredicateFilter(QueryStack theQueryStack3, SearchFilterParser.FilterParameter theFilter, String theResourceName, RequestDetails theRequest, RequestPartitionId theRequestPartitionId) {

		String paramName = theFilter.getParamPath().getName();

		if (paramName.equals(IAnyResource.SP_RES_ID)) {
			TokenParam param = new TokenParam();
			param.setValueAsQueryToken(null, null, null, theFilter.getValue());
			return theQueryStack3.createPredicateResourceId(null, Collections.singletonList(Collections.singletonList(param)), theResourceName, theFilter.getOperation(), theRequestPartitionId);
		} else if (paramName.equals(IAnyResource.SP_RES_LANGUAGE)) {
				return theQueryStack3.createPredicateLanguage(Collections.singletonList(Collections.singletonList(new StringParam(theFilter.getValue()))), theFilter.getOperation());
		} else if (paramName.equals(Constants.PARAM_SOURCE)) {
				TokenParam param = new TokenParam();
				param.setValueAsQueryToken(null, null, null, theFilter.getValue());
				return createPredicateSource(null, Collections.singletonList(param));
		} else {
			RuntimeSearchParam searchParam = mySearchParamRegistry.getActiveSearchParam(theResourceName, paramName);
			if (searchParam == null) {
				Collection<String> validNames = mySearchParamRegistry.getValidSearchParameterNamesIncludingMeta(theResourceName);
				String msg = myFhirContext.getLocalizer().getMessageSanitized(BaseHapiFhirResourceDao.class, "invalidSearchParameter", paramName, theResourceName, validNames);
				throw new InvalidRequestException(msg);
			}
			RestSearchParameterTypeEnum typeEnum = searchParam.getParamType();
			if (typeEnum == RestSearchParameterTypeEnum.URI) {
				return theQueryStack3.createPredicateUri(null, theResourceName, searchParam, Collections.singletonList(new UriParam(theFilter.getValue())), theFilter.getOperation(), theRequest, theRequestPartitionId);
			} else if (typeEnum == RestSearchParameterTypeEnum.STRING) {
				return theQueryStack3.createPredicateString(null, theResourceName, searchParam, Collections.singletonList(new StringParam(theFilter.getValue())), theFilter.getOperation(), theRequestPartitionId);
			} else if (typeEnum == RestSearchParameterTypeEnum.DATE) {
				return theQueryStack3.createPredicateDate(null, theResourceName, searchParam, Collections.singletonList(new DateParam(theFilter.getValue())), theFilter.getOperation(), theRequestPartitionId);
			} else if (typeEnum == RestSearchParameterTypeEnum.NUMBER) {
				return theQueryStack3.createPredicateNumber(null, theResourceName, searchParam, Collections.singletonList(new NumberParam(theFilter.getValue())), theFilter.getOperation(), theRequestPartitionId);
			} else if (typeEnum == RestSearchParameterTypeEnum.REFERENCE) {
				SearchFilterParser.CompareOperation operation = theFilter.getOperation();
				String resourceType = null; // The value can either have (Patient/123) or not have (123) a resource type, either way it's not needed here
				String chain = (theFilter.getParamPath().getNext() != null) ? theFilter.getParamPath().getNext().toString() : null;
				String value = theFilter.getValue();
				ReferenceParam referenceParam = new ReferenceParam(resourceType, chain, value);
				return theQueryStack3.createPredicateReference(null, theResourceName, paramName, Collections.singletonList(referenceParam), operation, theRequest, theRequestPartitionId);
			} else if (typeEnum == RestSearchParameterTypeEnum.QUANTITY) {
				return theQueryStack3.createPredicateQuantity(null, theResourceName, searchParam, Collections.singletonList(new QuantityParam(theFilter.getValue())), theFilter.getOperation(), theRequestPartitionId);
			} else if (typeEnum == RestSearchParameterTypeEnum.COMPOSITE) {
				throw new InvalidRequestException("Composite search parameters not currently supported with _filter clauses");
			} else if (typeEnum == RestSearchParameterTypeEnum.TOKEN) {
				TokenParam param = new TokenParam();
				param.setValueAsQueryToken(null,
					null,
					null,
					theFilter.getValue());
				return theQueryStack3.createPredicateToken(null, theResourceName, searchParam, Collections.singletonList(param), theFilter.getOperation(), theRequestPartitionId);
			}
		}
		return null;
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
				IQueryParameterAnd<?> parsedParam = ParameterUtil.parseQueryParams(myFhirContext, paramDef, paramName, parameters);

				for (IQueryParameterOr<?> next : parsedParam.getValuesAsQueryTokens()) {
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

			ResourceLinkPredicateBuilder join = mySqlBuilder.addReferencePredicateBuilderReversed(this, theSourceJoinColumn);
			Condition partitionPredicate = join.createPartitionIdPredicate(theRequestPartitionId);

			List<String> paths = join.createResourceLinkPaths(targetResourceType, paramReference);
			Condition typePredicate = BinaryCondition.equalTo(join.getColumnTargetResourceType(), mySqlBuilder.generatePlaceholder(theResourceType));
			Condition pathPredicate = toEqualToOrInPredicate(join.getColumnSourcePath(), mySqlBuilder.generatePlaceholders(paths));
			Condition linkedPredicate = searchForIdsWithAndOr(join.getColumnSrcResourceId(), targetResourceType, parameterName, Collections.singletonList(orValues), theRequest, theRequestPartitionId);
			andPredicates.add(toAndPredicate(partitionPredicate, pathPredicate, typePredicate, linkedPredicate));
		}

		return toAndPredicate(andPredicates);
	}

	public Condition createPredicateLanguage(List<List<IQueryParameterType>> theList, Object theOperation) {

		ResourceTablePredicateBuilder rootTable = mySqlBuilder.getOrCreateResourceTablePredicateBuilder();

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

		NumberPredicateBuilder join = createOrReusePredicateBuilder(PredicateBuilderTypeEnum.NUMBER, theSourceJoinColumn, theSearchParam.getName(), () -> mySqlBuilder.addNumberPredicateBuilder(theSourceJoinColumn)).getResult();

		if (theList.get(0).getMissing() != null) {
			return join.createPredicateParamMissingForNonReference(theResourceName, theSearchParam.getName(), theList.get(0).getMissing(), theRequestPartitionId);
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

				Condition predicate = join.createPredicateNumeric(theResourceName, theSearchParam.getName(), operation, value, theRequestPartitionId, nextOr);
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

		QuantityPredicateBuilder join = createOrReusePredicateBuilder(PredicateBuilderTypeEnum.QUANTITY, theSourceJoinColumn, theSearchParam.getName(), () -> mySqlBuilder.addQuantityPredicateBuilder(theSourceJoinColumn)).getResult();

		if (theList.get(0).getMissing() != null) {
			return join.createPredicateParamMissingForNonReference(theResourceName, theSearchParam.getName(), theList.get(0).getMissing(), theRequestPartitionId);
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
			SearchParamPresentPredicateBuilder join = mySqlBuilder.addSearchParamPresentPredicateBuilder(theSourceJoinColumn);
			return join.createPredicateParamMissingForReference(theResourceName, theParamName, theList.get(0).getMissing(), theRequestPartitionId);

		}

		ResourceLinkPredicateBuilder predicateBuilder = createOrReusePredicateBuilder(PredicateBuilderTypeEnum.REFERENCE, theSourceJoinColumn, theParamName, () -> mySqlBuilder.addReferencePredicateBuilder(this, theSourceJoinColumn)).getResult();
		return predicateBuilder.createPredicate(theRequest, theResourceName, theParamName, theList, theOperation, theRequestPartitionId);
	}

	@Nullable
	public Condition createPredicateResourceId(@Nullable DbColumn theSourceJoinColumn, List<List<IQueryParameterType>> theValues, String theResourceName, SearchFilterParser.CompareOperation theOperation, RequestPartitionId theRequestPartitionId) {
		ResourceIdPredicateBuilder builder = mySqlBuilder.newResourceIdBuilder();
		return builder.createPredicateResourceId(theSourceJoinColumn, theResourceName, theValues, theOperation, theRequestPartitionId);
	}

	private Condition createPredicateSourceForAndList(@Nullable DbColumn theSourceJoinColumn, List<List<IQueryParameterType>> theAndOrParams) {
		List<Condition> andPredicates = new ArrayList<>(theAndOrParams.size());
		for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
			andPredicates.add(createPredicateSource(theSourceJoinColumn, nextAnd));
		}
		return toAndPredicate(andPredicates);
	}

	private Condition createPredicateSource(@Nullable DbColumn theSourceJoinColumn, List<? extends IQueryParameterType> theList) {
		if (myDaoConfig.getStoreMetaSourceInformation() == DaoConfig.StoreMetaSourceInformationEnum.NONE) {
			String msg = myFhirContext.getLocalizer().getMessage(LegacySearchBuilder.class, "sourceParamDisabled");
			throw new InvalidRequestException(msg);
		}

		SourcePredicateBuilder join = createOrReusePredicateBuilder(PredicateBuilderTypeEnum.SOURCE, theSourceJoinColumn, Constants.PARAM_SOURCE, () -> mySqlBuilder.addSourcePredicateBuilder(theSourceJoinColumn)).getResult();

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

		StringPredicateBuilder join = createOrReusePredicateBuilder(PredicateBuilderTypeEnum.STRING, theSourceJoinColumn, theSearchParam.getName(), () -> mySqlBuilder.addStringPredicateBuilder(theSourceJoinColumn)).getResult();

		if (theList.get(0).getMissing() != null) {
			return join.createPredicateParamMissingForNonReference(theResourceName, theSearchParam.getName(), theList.get(0).getMissing(), theRequestPartitionId);
		}

		List<Condition> codePredicates = new ArrayList<>();
		for (IQueryParameterType nextOr : theList) {
			Condition singleCode = join.createPredicateString(nextOr, theResourceName, theSearchParam, join, theOperation);
			codePredicates.add(singleCode);
		}

		return join.combineWithRequestPartitionIdPredicate(theRequestPartitionId, toOrPredicate(codePredicates));
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
			BaseJoiningPredicateBuilder join;
			if (paramInverted) {

				SearchQueryBuilder sqlBuilder = mySqlBuilder.newChildSqlBuilder();
				TagPredicateBuilder tagSelector = sqlBuilder.addTagPredicateBuilder(null);
				sqlBuilder.addPredicate(tagSelector.createPredicateTag(tagType, tokens, theParamName, theRequestPartitionId));
				SelectQuery sql = sqlBuilder.getSelect();

				join = mySqlBuilder.getOrCreateFirstPredicateBuilder();
				Expression subSelect = new Subquery(sql);
				tagPredicate = new InCondition(join.getResourceIdColumn(), subSelect).setNegate(true);

			} else {
				// Tag table can't be a query root because it will include deleted resources, and can't select by resource type
				mySqlBuilder.getOrCreateFirstPredicateBuilder();

				TagPredicateBuilder tagJoin = createOrReusePredicateBuilder(PredicateBuilderTypeEnum.TAG, theSourceJoinColumn, theParamName, () -> mySqlBuilder.addTagPredicateBuilder(theSourceJoinColumn)).getResult();
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

		List<IQueryParameterType> tokens = new ArrayList<>();
		for (IQueryParameterType nextOr : theList) {

			if (nextOr instanceof TokenParam) {
				if (!((TokenParam) nextOr).isEmpty()) {
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

					tokens.add(nextOr);

				}

			} else {

				tokens.add(nextOr);
			}

		}

		if (tokens.isEmpty()) {
			return null;
		}

		TokenPredicateBuilder join = createOrReusePredicateBuilder(PredicateBuilderTypeEnum.TOKEN, theSourceJoinColumn, theSearchParam.getName(), () -> mySqlBuilder.addTokenPredicateBuilder(theSourceJoinColumn)).getResult();

		if (theList.get(0).getMissing() != null) {
			return join.createPredicateParamMissingForNonReference(theResourceName, theSearchParam.getName(), theList.get(0).getMissing(), theRequestPartitionId);
		}

		Condition predicate = join.createPredicateToken(tokens, theResourceName, theSearchParam, theOperation, theRequestPartitionId);
		return join.combineWithRequestPartitionIdPredicate(theRequestPartitionId, predicate);
	}

	public Condition createPredicateUri(@Nullable DbColumn theSourceJoinColumn,
													String theResourceName,
													RuntimeSearchParam theSearchParam,
													List<? extends IQueryParameterType> theList,
													SearchFilterParser.CompareOperation theOperation,
													RequestDetails theRequestDetails,
													RequestPartitionId theRequestPartitionId) {

		String paramName = theSearchParam.getName();
		UriPredicateBuilder join = mySqlBuilder.addUriPredicateBuilder(theSourceJoinColumn);

		if (theList.get(0).getMissing() != null) {
			return join.createPredicateParamMissingForNonReference(theResourceName, paramName, theList.get(0).getMissing(), theRequestPartitionId);
		}

		Condition predicate = join.addPredicate(theList, paramName, theOperation, theRequestDetails);
		return join.combineWithRequestPartitionIdPredicate(theRequestPartitionId, predicate);
	}

	public QueryStack newChildQueryFactoryWithFullBuilderReuse() {
		return new QueryStack(mySearchParameters, myDaoConfig, myModelConfig, myFhirContext, mySqlBuilder, mySearchParamRegistry, myPartitionSettings, EnumSet.allOf(PredicateBuilderTypeEnum.class));
	}

	@Nullable
	public Condition searchForIdsWithAndOr(@Nullable DbColumn theSourceJoinColumn, String theResourceName, String theParamName, List<List<IQueryParameterType>> theAndOrParams, RequestDetails theRequest, RequestPartitionId theRequestPartitionId) {

		if (theAndOrParams.isEmpty()) {
			return null;
		}

		switch (theParamName) {
			case IAnyResource.SP_RES_ID:
				return createPredicateResourceId(theSourceJoinColumn, theAndOrParams, theResourceName, null, theRequestPartitionId);

			case IAnyResource.SP_RES_LANGUAGE:
				return createPredicateLanguage(theAndOrParams, null);

			case Constants.PARAM_HAS:
				return createPredicateHas(theSourceJoinColumn, theResourceName, theAndOrParams, theRequest, theRequestPartitionId);

			case Constants.PARAM_TAG:
			case Constants.PARAM_PROFILE:
			case Constants.PARAM_SECURITY:
				return createPredicateTag(theSourceJoinColumn, theAndOrParams, theParamName, theRequestPartitionId);

			case Constants.PARAM_SOURCE:
				return createPredicateSourceForAndList(theSourceJoinColumn, theAndOrParams);

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
									andPredicates.add(createPredicateCoords(theSourceJoinColumn, theResourceName, nextParamDef, nextAnd, theRequestPartitionId));
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
								andPredicates.add(createPredicateUri(theSourceJoinColumn, theResourceName, nextParamDef, nextAnd, SearchFilterParser.CompareOperation.eq, theRequest, theRequestPartitionId));
							}
							break;
						case HAS:
						case SPECIAL:
							for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
								if ("Location.position".equals(nextParamDef.getPath())) {
									andPredicates.add(createPredicateCoords(theSourceJoinColumn, theResourceName, nextParamDef, nextAnd, theRequestPartitionId));
								}
							}
							break;
					}
				} else {
					// These are handled later
					if (!Constants.PARAM_CONTENT.equals(theParamName) && !Constants.PARAM_TEXT.equals(theParamName)) {
						if (Constants.PARAM_FILTER.equals(theParamName)) {

							// Parse the predicates enumerated in the _filter separated by AND or OR...
							if (theAndOrParams.get(0).get(0) instanceof StringParam) {
								String filterString = ((StringParam) theAndOrParams.get(0).get(0)).getValue();
								SearchFilterParser.Filter filter;
								try {
									filter = SearchFilterParser.parse(filterString);
								} catch (SearchFilterParser.FilterSyntaxException theE) {
									throw new InvalidRequestException("Error parsing _filter syntax: " + theE.getMessage());
								}
								if (filter != null) {

									if (!myDaoConfig.isFilterParameterEnabled()) {
										throw new InvalidRequestException(Constants.PARAM_FILTER + " parameter is disabled on this server");
									}

									Condition predicate = createPredicateFilter(this, filter, theResourceName, theRequest, theRequestPartitionId);
									if (predicate != null) {
										mySqlBuilder.addPredicate(predicate);
									}
								}
							}

						} else {
							String msg = myFhirContext.getLocalizer().getMessageSanitized(BaseHapiFhirResourceDao.class, "invalidSearchParameter", theParamName, theResourceName, mySearchParamRegistry.getValidSearchParameterNamesIncludingMeta(theResourceName));
							throw new InvalidRequestException(msg);
						}
					}
				}

				return toAndPredicate(andPredicates);

		}
	}

	public void addPredicateCompositeUnique(String theIndexString, RequestPartitionId theRequestPartitionId) {
		CompositeUniqueSearchParameterPredicateBuilder predicateBuilder = mySqlBuilder.addCompositeUniquePredicateBuilder();
		Condition predicate = predicateBuilder.createPredicateIndexString(theRequestPartitionId, theIndexString);
		mySqlBuilder.addPredicate(predicate);
	}

	public void addPredicateEverythingOperation(String theResourceName, Long theTargetPid) {
		ResourceLinkPredicateBuilder table = mySqlBuilder.addReferencePredicateBuilder(this, null);
		Condition predicate = table.createEverythingPredicate(theResourceName, theTargetPid);
		mySqlBuilder.addPredicate(predicate);
	}

	private enum PredicateBuilderTypeEnum {
		DATE, COORDS, NUMBER, QUANTITY, REFERENCE, SOURCE, STRING, TOKEN, TAG
	}

	private static class PredicateBuilderCacheLookupResult<T extends BaseJoiningPredicateBuilder> {
		private final boolean myCacheHit;
		private final T myResult;

		private PredicateBuilderCacheLookupResult(boolean theCacheHit, T theResult) {
			myCacheHit = theCacheHit;
			myResult = theResult;
		}

		public boolean isCacheHit() {
			return myCacheHit;
		}

		public T getResult() {
			return myResult;
		}
	}

	private static class PredicateBuilderCacheKey {
		private final DbColumn myDbColumn;
		private final PredicateBuilderTypeEnum myType;
		private final String myParamName;
		private final int myHashCode;

		private PredicateBuilderCacheKey(DbColumn theDbColumn, PredicateBuilderTypeEnum theType, String theParamName) {
			myDbColumn = theDbColumn;
			myType = theType;
			myParamName = theParamName;
			myHashCode = new HashCodeBuilder().append(myDbColumn).append(myType).append(myParamName).toHashCode();
		}

		@Override
		public boolean equals(Object theO) {
			if (this == theO) {
				return true;
			}

			if (theO == null || getClass() != theO.getClass()) {
				return false;
			}

			PredicateBuilderCacheKey that = (PredicateBuilderCacheKey) theO;

			return new EqualsBuilder()
				.append(myDbColumn, that.myDbColumn)
				.append(myType, that.myType)
				.append(myParamName, that.myParamName)
				.isEquals();
		}

		@Override
		public int hashCode() {
			return myHashCode;
		}
	}

	@Nullable
	public static Condition toAndPredicate(List<Condition> theAndPredicates) {
		List<Condition> andPredicates = theAndPredicates.stream().filter(t -> t != null).collect(Collectors.toList());
		if (andPredicates.size() == 0) {
			return null;
		} else if (andPredicates.size() == 1) {
			return andPredicates.get(0);
		} else {
			return ComboCondition.and(andPredicates.toArray(new Condition[0]));
		}
	}

	@Nullable
	public static Condition toOrPredicate(List<Condition> theOrPredicates) {
		List<Condition> orPredicates = theOrPredicates.stream().filter(t -> t != null).collect(Collectors.toList());
		if (orPredicates.size() == 0) {
			return null;
		} else if (orPredicates.size() == 1) {
			return orPredicates.get(0);
		} else {
			return ComboCondition.or(orPredicates.toArray(new Condition[0]));
		}
	}

	@Nullable
	public static Condition toOrPredicate(Condition... theOrPredicates) {
		return toOrPredicate(Arrays.asList(theOrPredicates));
	}

	@Nullable
	public static Condition toAndPredicate(Condition... theAndPredicates) {
		return toAndPredicate(Arrays.asList(theAndPredicates));
	}

	@Nonnull
	public static Condition toEqualToOrInPredicate(DbColumn theColumn, List<String> theValuePlaceholders, boolean theInverse) {
		if (theInverse) {
			return toNotEqualToOrNotInPredicate(theColumn, theValuePlaceholders);
		} else {
			return toEqualToOrInPredicate(theColumn, theValuePlaceholders);
		}
	}

	@Nonnull
	public static Condition toEqualToOrInPredicate(DbColumn theColumn, List<String> theValuePlaceholders) {
		if (theValuePlaceholders.size() == 1) {
			return BinaryCondition.equalTo(theColumn, theValuePlaceholders.get(0));
		}
		return new InCondition(theColumn, theValuePlaceholders);
	}

	@Nonnull
	public static Condition toNotEqualToOrNotInPredicate(DbColumn theColumn, List<String> theValuePlaceholders) {
		if (theValuePlaceholders.size() == 1) {
			return BinaryCondition.notEqualTo(theColumn, theValuePlaceholders.get(0));
		}
		return new InCondition(theColumn, theValuePlaceholders).setNegate(true);
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
				case ENDS_BEFORE:
					return SearchFilterParser.CompareOperation.eb;
				case STARTS_AFTER:
					return SearchFilterParser.CompareOperation.sa;
			}
		}
		return SearchFilterParser.CompareOperation.eq;
	}

	private static String getChainedPart(String parameter) {
		return parameter.substring(parameter.indexOf(".") + 1);
	}

}
