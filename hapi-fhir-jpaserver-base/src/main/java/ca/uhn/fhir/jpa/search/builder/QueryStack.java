package ca.uhn.fhir.jpa.search.builder;

/*
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
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirResourceDao;
import ca.uhn.fhir.jpa.dao.LegacySearchBuilder;
import ca.uhn.fhir.jpa.dao.predicate.PredicateBuilderToken;
import ca.uhn.fhir.jpa.dao.predicate.SearchFilterParser;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.model.entity.NormalizedQuantitySearchLevel;
import ca.uhn.fhir.jpa.model.entity.TagTypeEnum;
import ca.uhn.fhir.jpa.model.util.UcumServiceUtil;
import ca.uhn.fhir.jpa.search.builder.predicate.BaseJoiningPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.ComboNonUniqueSearchParameterPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.ComboUniqueSearchParameterPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.CoordsPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.DatePredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.ForcedIdPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.NumberPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.QuantityBasePredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.ResourceIdPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.ResourceLinkPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.ResourceTablePredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.SearchParamPresentPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.SourcePredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.StringPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.TagPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.TokenPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.UriPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryBuilder;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.extractor.BaseSearchParamExtractor;
import ca.uhn.fhir.jpa.searchparam.util.JpaParamUtil;
import ca.uhn.fhir.jpa.searchparam.util.SourceParam;
import ca.uhn.fhir.model.api.IQueryParameterAnd;
import ca.uhn.fhir.model.api.IQueryParameterOr;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.QualifiedParamList;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.api.SearchContainedModeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.CompositeParam;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.HasParam;
import ca.uhn.fhir.rest.param.NumberParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
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
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
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
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.apache.commons.collections4.bidimap.UnmodifiableBidiMap;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class QueryStack {

	private static final Logger ourLog = LoggerFactory.getLogger(QueryStack.class);
	private static final BidiMap<SearchFilterParser.CompareOperation, ParamPrefixEnum> ourCompareOperationToParamPrefix;

	static {
		DualHashBidiMap<SearchFilterParser.CompareOperation, ParamPrefixEnum> compareOperationToParamPrefix = new DualHashBidiMap<>();
		compareOperationToParamPrefix.put(SearchFilterParser.CompareOperation.ap, ParamPrefixEnum.APPROXIMATE);
		compareOperationToParamPrefix.put(SearchFilterParser.CompareOperation.eq, ParamPrefixEnum.EQUAL);
		compareOperationToParamPrefix.put(SearchFilterParser.CompareOperation.gt, ParamPrefixEnum.GREATERTHAN);
		compareOperationToParamPrefix.put(SearchFilterParser.CompareOperation.ge, ParamPrefixEnum.GREATERTHAN_OR_EQUALS);
		compareOperationToParamPrefix.put(SearchFilterParser.CompareOperation.lt, ParamPrefixEnum.LESSTHAN);
		compareOperationToParamPrefix.put(SearchFilterParser.CompareOperation.le, ParamPrefixEnum.LESSTHAN_OR_EQUALS);
		compareOperationToParamPrefix.put(SearchFilterParser.CompareOperation.ne, ParamPrefixEnum.NOT_EQUAL);
		compareOperationToParamPrefix.put(SearchFilterParser.CompareOperation.eb, ParamPrefixEnum.ENDS_BEFORE);
		compareOperationToParamPrefix.put(SearchFilterParser.CompareOperation.sa, ParamPrefixEnum.STARTS_AFTER);
		ourCompareOperationToParamPrefix = UnmodifiableBidiMap.unmodifiableBidiMap(compareOperationToParamPrefix);
	}

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

		QuantityBasePredicateBuilder sortPredicateBuilder;
		sortPredicateBuilder = mySqlBuilder.addQuantityPredicateBuilder(firstPredicateBuilder.getResourceIdColumn());

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

	private Condition createPredicateComposite(@Nullable DbColumn theSourceJoinColumn, String theResourceName, String theSpnamePrefix, RuntimeSearchParam theParamDef, List<? extends IQueryParameterType> theNextAnd, RequestPartitionId theRequestPartitionId) {

		Condition orCondidtion = null;
		for (IQueryParameterType next : theNextAnd) {

			if (!(next instanceof CompositeParam<?, ?>)) {
				throw new InvalidRequestException("Invalid type for composite param (must be " + CompositeParam.class.getSimpleName() + ": " + next.getClass());
			}
			CompositeParam<?, ?> cp = (CompositeParam<?, ?>) next;

			List<RuntimeSearchParam> componentParams = JpaParamUtil.resolveComponentParameters(mySearchParamRegistry, theParamDef);
			RuntimeSearchParam left = componentParams.get(0);
			IQueryParameterType leftValue = cp.getLeftValue();
			Condition leftPredicate = createPredicateCompositePart(theSourceJoinColumn, theResourceName, theSpnamePrefix, left, leftValue, theRequestPartitionId);

			RuntimeSearchParam right = componentParams.get(1);
			IQueryParameterType rightValue = cp.getRightValue();
			Condition rightPredicate = createPredicateCompositePart(theSourceJoinColumn, theResourceName, theSpnamePrefix, right, rightValue, theRequestPartitionId);

			Condition andCondition = toAndPredicate(leftPredicate, rightPredicate);

			if (orCondidtion == null) {
				orCondidtion = toOrPredicate(andCondition);
			} else {
				orCondidtion = toOrPredicate(orCondidtion, andCondition);
			}
		}

		return orCondidtion;
	}

	private Condition createPredicateCompositePart(@Nullable DbColumn theSourceJoinColumn, String theResourceName, String theSpnamePrefix, RuntimeSearchParam theParam, IQueryParameterType theParamValue, RequestPartitionId theRequestPartitionId) {

		switch (theParam.getParamType()) {
			case STRING: {
				return createPredicateString(theSourceJoinColumn, theResourceName, theSpnamePrefix, theParam, Collections.singletonList(theParamValue), null, theRequestPartitionId);
			}
			case TOKEN: {
				return createPredicateToken(theSourceJoinColumn, theResourceName, theSpnamePrefix, theParam, Collections.singletonList(theParamValue), null, theRequestPartitionId);
			}
			case DATE: {
				return createPredicateDate(theSourceJoinColumn, theResourceName, theSpnamePrefix, theParam, Collections.singletonList(theParamValue), toOperation(((DateParam) theParamValue).getPrefix()), theRequestPartitionId);
			}
			case QUANTITY: {
				return createPredicateQuantity(theSourceJoinColumn, theResourceName, theSpnamePrefix, theParam, Collections.singletonList(theParamValue), null, theRequestPartitionId);
			}
			case NUMBER:
			case REFERENCE:
			case COMPOSITE:
			case URI:
			case HAS:
			case SPECIAL:
			default:
				throw new InvalidRequestException("Don't know how to handle composite parameter with type of " + theParam.getParamType());
		}

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

	public Condition createPredicateDate(@Nullable DbColumn theSourceJoinColumn, String theResourceName,
													 String theSpnamePrefix, RuntimeSearchParam theSearchParam, List<? extends IQueryParameterType> theList,
													 SearchFilterParser.CompareOperation theOperation, RequestPartitionId theRequestPartitionId) {

		String paramName = getParamNameWithPrefix(theSpnamePrefix, theSearchParam.getName());

		PredicateBuilderCacheLookupResult<DatePredicateBuilder> predicateBuilderLookupResult = createOrReusePredicateBuilder(PredicateBuilderTypeEnum.DATE, theSourceJoinColumn, paramName, () -> mySqlBuilder.addDatePredicateBuilder(theSourceJoinColumn));
		DatePredicateBuilder predicateBuilder = predicateBuilderLookupResult.getResult();
		boolean cacheHit = predicateBuilderLookupResult.isCacheHit();

		if (theList.get(0).getMissing() != null) {
			Boolean missing = theList.get(0).getMissing();
			return predicateBuilder.createPredicateParamMissingForNonReference(theResourceName, paramName, missing, theRequestPartitionId);
		}

		List<Condition> codePredicates = new ArrayList<>();

		for (IQueryParameterType nextOr : theList) {
			Condition p = predicateBuilder.createPredicateDateWithoutIdentityPredicate(nextOr, predicateBuilder, theOperation);
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

		switch (paramName) {
			case IAnyResource.SP_RES_ID: {
				TokenParam param = new TokenParam();
				param.setValueAsQueryToken(null, null, null, theFilter.getValue());
				return theQueryStack3.createPredicateResourceId(null, Collections.singletonList(Collections.singletonList(param)), theResourceName, theFilter.getOperation(), theRequestPartitionId);
			}
			case IAnyResource.SP_RES_LANGUAGE: {
				return theQueryStack3.createPredicateLanguage(Collections.singletonList(Collections.singletonList(new StringParam(theFilter.getValue()))), theFilter.getOperation());
			}
			case Constants.PARAM_SOURCE: {
				TokenParam param = new TokenParam();
				param.setValueAsQueryToken(null, null, null, theFilter.getValue());
				return createPredicateSource(null, Collections.singletonList(param));
			}
			default:
				RuntimeSearchParam searchParam = mySearchParamRegistry.getActiveSearchParam(theResourceName, paramName);
				if (searchParam == null) {
					Collection<String> validNames = mySearchParamRegistry.getValidSearchParameterNamesIncludingMeta(theResourceName);
					String msg = myFhirContext.getLocalizer().getMessageSanitized(BaseHapiFhirResourceDao.class, "invalidSearchParameter", paramName, theResourceName, validNames);
					throw new InvalidRequestException(msg);
				}
				RestSearchParameterTypeEnum typeEnum = searchParam.getParamType();
				if (typeEnum == RestSearchParameterTypeEnum.URI) {
					return theQueryStack3.createPredicateUri(null, theResourceName, null, searchParam, Collections.singletonList(new UriParam(theFilter.getValue())), theFilter.getOperation(), theRequest, theRequestPartitionId);
				} else if (typeEnum == RestSearchParameterTypeEnum.STRING) {
					return theQueryStack3.createPredicateString(null, theResourceName, null, searchParam, Collections.singletonList(new StringParam(theFilter.getValue())), theFilter.getOperation(), theRequestPartitionId);
				} else if (typeEnum == RestSearchParameterTypeEnum.DATE) {
					return theQueryStack3.createPredicateDate(null, theResourceName, null, searchParam, Collections.singletonList(new DateParam(fromOperation(theFilter.getOperation()), theFilter.getValue())), theFilter.getOperation(), theRequestPartitionId);
				} else if (typeEnum == RestSearchParameterTypeEnum.NUMBER) {
					return theQueryStack3.createPredicateNumber(null, theResourceName, null, searchParam, Collections.singletonList(new NumberParam(theFilter.getValue())), theFilter.getOperation(), theRequestPartitionId);
				} else if (typeEnum == RestSearchParameterTypeEnum.REFERENCE) {
					SearchFilterParser.CompareOperation operation = theFilter.getOperation();
					String resourceType = null; // The value can either have (Patient/123) or not have (123) a resource type, either way it's not needed here
					String chain = (theFilter.getParamPath().getNext() != null) ? theFilter.getParamPath().getNext().toString() : null;
					String value = theFilter.getValue();
					ReferenceParam referenceParam = new ReferenceParam(resourceType, chain, value);
					return theQueryStack3.createPredicateReference(null, theResourceName, paramName, Collections.singletonList(referenceParam), operation, theRequest, theRequestPartitionId);
				} else if (typeEnum == RestSearchParameterTypeEnum.QUANTITY) {
					return theQueryStack3.createPredicateQuantity(null, theResourceName, null, searchParam, Collections.singletonList(new QuantityParam(theFilter.getValue())), theFilter.getOperation(), theRequestPartitionId);
				} else if (typeEnum == RestSearchParameterTypeEnum.COMPOSITE) {
					throw new InvalidRequestException("Composite search parameters not currently supported with _filter clauses");
				} else if (typeEnum == RestSearchParameterTypeEnum.TOKEN) {
					TokenParam param = new TokenParam();
					param.setValueAsQueryToken(null,
						null,
						null,
						theFilter.getValue());
					return theQueryStack3.createPredicateToken(null, theResourceName, null, searchParam, Collections.singletonList(param), theFilter.getOperation(), theRequestPartitionId);
				}
				break;
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

			try {
				myFhirContext.getResourceDefinition(targetResourceType);
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
				RuntimeSearchParam owningParameterDef = mySearchParamRegistry.getActiveSearchParam(targetResourceType, paramName);
				if (owningParameterDef == null) {
					throw new InvalidRequestException("Unknown parameter name: " + targetResourceType + ':' + parameterName);
				}

				//Ensure that the name of the back-referenced search param on the target (e.g. the `subject` in Patient?_has:Observation:subject:code=sys|val)
				//exists on the target resource.
				RuntimeSearchParam joiningParameterDef = mySearchParamRegistry.getActiveSearchParam(targetResourceType, paramReference);
				if (joiningParameterDef == null) {
					throw new InvalidRequestException("Unknown parameter name: " + targetResourceType + ':' + paramReference);
				}

				IQueryParameterAnd<?> parsedParam = JpaParamUtil.parseQueryParams(mySearchParamRegistry, myFhirContext, owningParameterDef, paramName, parameters);

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
			Condition linkedPredicate = searchForIdsWithAndOr(join.getColumnSrcResourceId(), targetResourceType, parameterName, Collections.singletonList(orValues), theRequest, theRequestPartitionId, SearchContainedModeEnum.FALSE);
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

	public Condition createPredicateNumber(@Nullable DbColumn theSourceJoinColumn, String theResourceName,
														String theSpnamePrefix, RuntimeSearchParam theSearchParam, List<? extends IQueryParameterType> theList,
														SearchFilterParser.CompareOperation theOperation, RequestPartitionId theRequestPartitionId) {

		String paramName = getParamNameWithPrefix(theSpnamePrefix, theSearchParam.getName());

		NumberPredicateBuilder join = createOrReusePredicateBuilder(PredicateBuilderTypeEnum.NUMBER, theSourceJoinColumn, paramName, () -> mySqlBuilder.addNumberPredicateBuilder(theSourceJoinColumn)).getResult();

		if (theList.get(0).getMissing() != null) {
			return join.createPredicateParamMissingForNonReference(theResourceName, paramName, theList.get(0).getMissing(), theRequestPartitionId);
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


				Condition predicate = join.createPredicateNumeric(theResourceName, paramName, operation, value, theRequestPartitionId, nextOr);
				codePredicates.add(predicate);

			} else {
				throw new IllegalArgumentException("Invalid token type: " + nextOr.getClass());
			}

		}

		return join.combineWithRequestPartitionIdPredicate(theRequestPartitionId, ComboCondition.or(codePredicates.toArray(new Condition[0])));
	}

	public Condition createPredicateQuantity(@Nullable DbColumn theSourceJoinColumn, String theResourceName,
														  String theSpnamePrefix, RuntimeSearchParam theSearchParam, List<? extends IQueryParameterType> theList,
														  SearchFilterParser.CompareOperation theOperation, RequestPartitionId theRequestPartitionId) {

		String paramName = getParamNameWithPrefix(theSpnamePrefix, theSearchParam.getName());

		if (theList.get(0).getMissing() != null) {
			QuantityBasePredicateBuilder join = createOrReusePredicateBuilder(PredicateBuilderTypeEnum.QUANTITY, theSourceJoinColumn, theSearchParam.getName(), () -> mySqlBuilder.addQuantityPredicateBuilder(theSourceJoinColumn)).getResult();
			return join.createPredicateParamMissingForNonReference(theResourceName, paramName, theList.get(0).getMissing(), theRequestPartitionId);
		}

		List<QuantityParam> quantityParams = theList
			.stream()
			.map(t -> QuantityParam.toQuantityParam(t))
			.collect(Collectors.toList());

		QuantityBasePredicateBuilder join = null;
		boolean normalizedSearchEnabled = myModelConfig.getNormalizedQuantitySearchLevel().equals(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_SEARCH_SUPPORTED);
		if (normalizedSearchEnabled) {
			List<QuantityParam> normalizedQuantityParams = quantityParams
				.stream()
				.map(t -> UcumServiceUtil.toCanonicalQuantityOrNull(t))
				.filter(t -> t != null)
				.collect(Collectors.toList());

			if (normalizedQuantityParams.size() == quantityParams.size()) {
				join = createOrReusePredicateBuilder(PredicateBuilderTypeEnum.QUANTITY, theSourceJoinColumn, paramName, () -> mySqlBuilder.addQuantityNormalizedPredicateBuilder(theSourceJoinColumn)).getResult();
				quantityParams = normalizedQuantityParams;
			}
		}

		if (join == null) {
			join = createOrReusePredicateBuilder(PredicateBuilderTypeEnum.QUANTITY, theSourceJoinColumn, paramName, () -> mySqlBuilder.addQuantityPredicateBuilder(theSourceJoinColumn)).getResult();
		}

		List<Condition> codePredicates = new ArrayList<>();
		for (QuantityParam nextOr : quantityParams) {
			Condition singleCode = join.createPredicateQuantity(nextOr, theResourceName, paramName, null, join, theOperation, theRequestPartitionId);
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

	private Condition createPredicateReferenceForContainedResource(@Nullable DbColumn theSourceJoinColumn,
																						String theResourceName, String theParamName, RuntimeSearchParam theSearchParam,
																						List<? extends IQueryParameterType> theList, SearchFilterParser.CompareOperation theOperation,
																						RequestDetails theRequest, RequestPartitionId theRequestPartitionId) {

		String spnamePrefix = theParamName;

		String targetChain = null;
		String targetParamName = null;
		String targetQualifier = null;
		String targetValue = null;

		RuntimeSearchParam targetParamDefinition = null;

		ArrayList<IQueryParameterType> orValues = Lists.newArrayList();
		IQueryParameterType qp = null;

		for (int orIdx = 0; orIdx < theList.size(); orIdx++) {

			IQueryParameterType nextOr = theList.get(orIdx);

			if (nextOr instanceof ReferenceParam) {

				ReferenceParam referenceParam = (ReferenceParam) nextOr;

				// 1. Find out the parameter, qualifier and the value
				targetChain = referenceParam.getChain();
				targetParamName = targetChain;
				targetValue = nextOr.getValueAsQueryToken(myFhirContext);

				int qualifierIndex = targetChain.indexOf(':');
				if (qualifierIndex != -1) {
					targetParamName = targetChain.substring(0, qualifierIndex);
					targetQualifier = targetChain.substring(qualifierIndex);
				}

				// 2. find out the data type
				if (targetParamDefinition == null) {
					Iterator<String> it = theSearchParam.getTargets().iterator();
					while (it.hasNext()) {
						targetParamDefinition = mySearchParamRegistry.getActiveSearchParam(it.next(), targetParamName);
						if (targetParamDefinition != null)
							break;
					}
				}

				if (targetParamDefinition == null) {
					throw new InvalidRequestException("Unknown search parameter name: " + theSearchParam.getName() + '.' + targetParamName + ".");
				}

				qp = toParameterType(targetParamDefinition);
				qp.setValueAsQueryToken(myFhirContext, targetParamName, targetQualifier, targetValue);
				orValues.add(qp);
			}
		}

		if (targetParamDefinition == null) {
			throw new InvalidRequestException("Unknown search parameter name: " + theSearchParam.getName() + ".");
		}

		// 3. create the query
		Condition containedCondition = null;

		switch (targetParamDefinition.getParamType()) {
			case DATE:
				containedCondition = createPredicateDate(null, theResourceName, spnamePrefix, targetParamDefinition,
					orValues, theOperation, theRequestPartitionId);
				break;
			case NUMBER:
				containedCondition = createPredicateNumber(null, theResourceName, spnamePrefix, targetParamDefinition,
					orValues, theOperation, theRequestPartitionId);
				break;
			case QUANTITY:
				containedCondition = createPredicateQuantity(null, theResourceName, spnamePrefix, targetParamDefinition,
					orValues, theOperation, theRequestPartitionId);
				break;
			case STRING:
				containedCondition = createPredicateString(null, theResourceName, spnamePrefix, targetParamDefinition,
					orValues, theOperation, theRequestPartitionId);
				break;
			case TOKEN:
				containedCondition = createPredicateToken(null, theResourceName, spnamePrefix, targetParamDefinition,
					orValues, theOperation, theRequestPartitionId);
				break;
			case COMPOSITE:
				containedCondition = createPredicateComposite(null, theResourceName, spnamePrefix, targetParamDefinition,
					orValues, theRequestPartitionId);
				break;
			case URI:
				containedCondition = createPredicateUri(null, theResourceName, spnamePrefix, targetParamDefinition,
					orValues, theOperation, theRequest, theRequestPartitionId);
				break;
			case HAS:
			case REFERENCE:
			case SPECIAL:
			default:
				throw new InvalidRequestException(
					"The search type:" + targetParamDefinition.getParamType() + " is not supported.");
		}

		return containedCondition;
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

	public Condition createPredicateString(@Nullable DbColumn theSourceJoinColumn, String theResourceName,
														String theSpnamePrefix, RuntimeSearchParam theSearchParam, List<? extends IQueryParameterType> theList,
														SearchFilterParser.CompareOperation theOperation, RequestPartitionId theRequestPartitionId) {

		String paramName = getParamNameWithPrefix(theSpnamePrefix, theSearchParam.getName());

		StringPredicateBuilder join = createOrReusePredicateBuilder(PredicateBuilderTypeEnum.STRING, theSourceJoinColumn, paramName, () -> mySqlBuilder.addStringPredicateBuilder(theSourceJoinColumn)).getResult();

		if (theList.get(0).getMissing() != null) {
			return join.createPredicateParamMissingForNonReference(theResourceName, paramName, theList.get(0).getMissing(), theRequestPartitionId);
		}

		List<Condition> codePredicates = new ArrayList<>();
		for (IQueryParameterType nextOr : theList) {
			Condition singleCode = join.createPredicateString(nextOr, theResourceName, theSpnamePrefix, theSearchParam, join, theOperation);
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

	public Condition createPredicateToken(@Nullable DbColumn theSourceJoinColumn, String theResourceName,
													  String theSpnamePrefix, RuntimeSearchParam theSearchParam, List<? extends IQueryParameterType> theList,
													  SearchFilterParser.CompareOperation theOperation, RequestPartitionId theRequestPartitionId) {

		List<IQueryParameterType> tokens = new ArrayList<>(); 
		
		boolean paramInverted = false;
		TokenParamModifier modifier = null;
		
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

						return createPredicateString(theSourceJoinColumn, theResourceName, theSpnamePrefix, theSearchParam, theList, null, theRequestPartitionId);
					} 
					
					modifier = id.getModifier();
					// for :not modifier, create a token and remove the :not modifier
					if (modifier != null && modifier == TokenParamModifier.NOT) {
						tokens.add(new TokenParam(((TokenParam) nextOr).getSystem(), ((TokenParam) nextOr).getValue()));
						paramInverted = true;
					} else {
						tokens.add(nextOr);
					}
				}
			} else {
				tokens.add(nextOr);
			}
		}

		if (tokens.isEmpty()) {
			return null;
		}

		String paramName = getParamNameWithPrefix(theSpnamePrefix, theSearchParam.getName());
		Condition predicate;
		BaseJoiningPredicateBuilder join;
		
		if (paramInverted) {
			SearchQueryBuilder sqlBuilder = mySqlBuilder.newChildSqlBuilder();
			TokenPredicateBuilder tokenSelector = sqlBuilder.addTokenPredicateBuilder(null);
			sqlBuilder.addPredicate(tokenSelector.createPredicateToken(tokens, theResourceName, theSpnamePrefix, theSearchParam, theRequestPartitionId));
			SelectQuery sql = sqlBuilder.getSelect();
			Expression subSelect = new Subquery(sql);
			
			join = mySqlBuilder.getOrCreateFirstPredicateBuilder();
			
			if (theSourceJoinColumn == null) {
				predicate = new InCondition(join.getResourceIdColumn(), subSelect).setNegate(true);
			} else {
				//-- for the resource link, need join with target_resource_id
			    predicate = new InCondition(theSourceJoinColumn, subSelect).setNegate(true);
			}
			
		} else {
		
			TokenPredicateBuilder tokenJoin = createOrReusePredicateBuilder(PredicateBuilderTypeEnum.TOKEN, theSourceJoinColumn, paramName, () -> mySqlBuilder.addTokenPredicateBuilder(theSourceJoinColumn)).getResult();

			if (theList.get(0).getMissing() != null) {
				return tokenJoin.createPredicateParamMissingForNonReference(theResourceName, paramName, theList.get(0).getMissing(), theRequestPartitionId);
			}

			predicate = tokenJoin.createPredicateToken(tokens, theResourceName, theSpnamePrefix, theSearchParam, theOperation, theRequestPartitionId);
			join = tokenJoin; 
		} 
		
		return join.combineWithRequestPartitionIdPredicate(theRequestPartitionId, predicate);
	}

	public Condition createPredicateUri(@Nullable DbColumn theSourceJoinColumn, String theResourceName,
													String theSpnamePrefix, RuntimeSearchParam theSearchParam, List<? extends IQueryParameterType> theList,
													SearchFilterParser.CompareOperation theOperation, RequestDetails theRequestDetails,
													RequestPartitionId theRequestPartitionId) {

		String paramName = getParamNameWithPrefix(theSpnamePrefix, theSearchParam.getName());

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
	public Condition searchForIdsWithAndOr(@Nullable DbColumn theSourceJoinColumn, String theResourceName, String theParamName, List<List<IQueryParameterType>> theAndOrParams, RequestDetails theRequest, RequestPartitionId theRequestPartitionId, SearchContainedModeEnum theSearchContainedMode) {

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
				if (myDaoConfig.getTagStorageMode() == DaoConfig.TagStorageModeEnum.INLINE) {
					return createPredicateSearchParameter(theSourceJoinColumn, theResourceName, theParamName, theAndOrParams, theRequest, theRequestPartitionId, theSearchContainedMode);
				} else {
					return createPredicateTag(theSourceJoinColumn, theAndOrParams, theParamName, theRequestPartitionId);
				}

			case Constants.PARAM_SOURCE:
				return createPredicateSourceForAndList(theSourceJoinColumn, theAndOrParams);

			default:
				return createPredicateSearchParameter(theSourceJoinColumn, theResourceName, theParamName, theAndOrParams, theRequest, theRequestPartitionId, theSearchContainedMode);

		}

	}

	@Nullable
	private Condition createPredicateSearchParameter(@Nullable DbColumn theSourceJoinColumn, String theResourceName, String theParamName, List<List<IQueryParameterType>> theAndOrParams, RequestDetails theRequest, RequestPartitionId theRequestPartitionId, SearchContainedModeEnum theSearchContainedMode) {
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
						// FT: 2021-01-18 use operation 'gt', 'ge', 'le' or 'lt'
						// to create the predicateDate instead of generic one with operation = null
						SearchFilterParser.CompareOperation operation = null;
						if (nextAnd.size() > 0) {
							DateParam param = (DateParam) nextAnd.get(0);
							operation = toOperation(param.getPrefix());
						}
						andPredicates.add(createPredicateDate(theSourceJoinColumn, theResourceName, null, nextParamDef, nextAnd, operation, theRequestPartitionId));
						//andPredicates.add(createPredicateDate(theSourceJoinColumn, theResourceName, nextParamDef, nextAnd, null, theRequestPartitionId));
					}
					break;
				case QUANTITY:
					for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
						SearchFilterParser.CompareOperation operation = null;
						if (nextAnd.size() > 0) {
							QuantityParam param = (QuantityParam) nextAnd.get(0);
							operation = toOperation(param.getPrefix());
						}
						andPredicates.add(createPredicateQuantity(theSourceJoinColumn, theResourceName, null, nextParamDef, nextAnd, operation, theRequestPartitionId));
					}
					break;
				case REFERENCE:
					for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
						if (theSearchContainedMode.equals(SearchContainedModeEnum.TRUE))
							andPredicates.add(createPredicateReferenceForContainedResource(theSourceJoinColumn, theResourceName, theParamName, nextParamDef, nextAnd, null, theRequest, theRequestPartitionId));
						else
							andPredicates.add(createPredicateReference(theSourceJoinColumn, theResourceName, theParamName, nextAnd, null, theRequest, theRequestPartitionId));
					}
					break;
				case STRING:
					for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
						andPredicates.add(createPredicateString(theSourceJoinColumn, theResourceName, null, nextParamDef, nextAnd, SearchFilterParser.CompareOperation.sw, theRequestPartitionId));
					}
					break;
				case TOKEN:
					for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
						if ("Location.position".equals(nextParamDef.getPath())) {
							andPredicates.add(createPredicateCoords(theSourceJoinColumn, theResourceName, nextParamDef, nextAnd, theRequestPartitionId));
						} else {
							andPredicates.add(createPredicateToken(theSourceJoinColumn, theResourceName, null, nextParamDef, nextAnd, null, theRequestPartitionId));
						}
					}
					break;
				case NUMBER:
					for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
						andPredicates.add(createPredicateNumber(theSourceJoinColumn, theResourceName, null, nextParamDef, nextAnd, null, theRequestPartitionId));
					}
					break;
				case COMPOSITE:
					for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
						andPredicates.add(createPredicateComposite(theSourceJoinColumn, theResourceName, null, nextParamDef, nextAnd, theRequestPartitionId));
					}
					break;
				case URI:
					for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
						andPredicates.add(createPredicateUri(theSourceJoinColumn, theResourceName, null, nextParamDef, nextAnd, SearchFilterParser.CompareOperation.eq, theRequest, theRequestPartitionId));
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

	public void addPredicateCompositeUnique(String theIndexString, RequestPartitionId theRequestPartitionId) {
		ComboUniqueSearchParameterPredicateBuilder predicateBuilder = mySqlBuilder.addComboUniquePredicateBuilder();
		Condition predicate = predicateBuilder.createPredicateIndexString(theRequestPartitionId, theIndexString);
		mySqlBuilder.addPredicate(predicate);
	}

	public void addPredicateCompositeNonUnique(String theIndexString, RequestPartitionId theRequestPartitionId) {
		ComboNonUniqueSearchParameterPredicateBuilder predicateBuilder = mySqlBuilder.addComboNonUniquePredicateBuilder();
		Condition predicate = predicateBuilder.createPredicateHashComplete(theRequestPartitionId, theIndexString);
		mySqlBuilder.addPredicate(predicate);
	}


	public void addPredicateEverythingOperation(String theResourceName, Long theTargetPid) {
		ResourceLinkPredicateBuilder table = mySqlBuilder.addReferencePredicateBuilder(this, null);
		Condition predicate = table.createEverythingPredicate(theResourceName, theTargetPid);
		mySqlBuilder.addPredicate(predicate);
	}

	private IQueryParameterType toParameterType(RuntimeSearchParam theParam) {

		IQueryParameterType qp;
		switch (theParam.getParamType()) {
			case DATE:
				qp = new DateParam();
				break;
			case NUMBER:
				qp = new NumberParam();
				break;
			case QUANTITY:
				qp = new QuantityParam();
				break;
			case STRING:
				qp = new StringParam();
				break;
			case TOKEN:
				qp = new TokenParam();
				break;
			case COMPOSITE:
				List<RuntimeSearchParam> compositeOf = JpaParamUtil.resolveComponentParameters(mySearchParamRegistry, theParam);
				if (compositeOf.size() != 2) {
					throw new InternalErrorException("Parameter " + theParam.getName() + " has " + compositeOf.size() + " composite parts. Don't know how handlt this.");
				}
				IQueryParameterType leftParam = toParameterType(compositeOf.get(0));
				IQueryParameterType rightParam = toParameterType(compositeOf.get(1));
				qp = new CompositeParam<>(leftParam, rightParam);
				break;
			case URI:
				qp = new UriParam();
				break;
			case HAS:
			case REFERENCE:
			case SPECIAL:
			default:
				throw new InvalidRequestException("The search type: " + theParam.getParamType() + " is not supported.");
		}
		return qp;
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
		SearchFilterParser.CompareOperation retVal = null;
		if (thePrefix != null && ourCompareOperationToParamPrefix.containsValue(thePrefix)) {
			retVal = ourCompareOperationToParamPrefix.getKey(thePrefix);
		}
		return defaultIfNull(retVal, SearchFilterParser.CompareOperation.eq);
	}

	public static ParamPrefixEnum fromOperation(SearchFilterParser.CompareOperation thePrefix) {
		ParamPrefixEnum retVal = null;
		if (thePrefix != null && ourCompareOperationToParamPrefix.containsKey(thePrefix)) {
			retVal = ourCompareOperationToParamPrefix.get(thePrefix);
		}
		return defaultIfNull(retVal, ParamPrefixEnum.EQUAL);
	}

	private static String getChainedPart(String parameter) {
		return parameter.substring(parameter.indexOf(".") + 1);
	}

	public static String getParamNameWithPrefix(String theSpnamePrefix, String theParamName) {

		if (isBlank(theSpnamePrefix))
			return theParamName;

		return theSpnamePrefix + "." + theParamName;
	}
}
