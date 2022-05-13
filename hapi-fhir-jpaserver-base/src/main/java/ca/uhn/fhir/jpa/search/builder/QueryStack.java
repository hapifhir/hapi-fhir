package ca.uhn.fhir.jpa.search.builder;

/*
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.BaseStorageDao;
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
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.ComboCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.Expression;
import com.healthmarketscience.sqlbuilder.InCondition;
import com.healthmarketscience.sqlbuilder.OrderObject;
import com.healthmarketscience.sqlbuilder.SelectQuery;
import com.healthmarketscience.sqlbuilder.SetOperationQuery;
import com.healthmarketscience.sqlbuilder.Subquery;
import com.healthmarketscience.sqlbuilder.UnionQuery;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.apache.commons.collections4.bidimap.UnmodifiableBidiMap;
import org.apache.commons.lang3.StringUtils;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.split;

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

		Condition pathPredicate = sortPredicateBuilder.createPredicateSourcePaths(theResourceName, theParamName, new ArrayList<>());
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
		return createPredicateComposite(theSourceJoinColumn, theResourceName, theSpnamePrefix, theParamDef, theNextAnd, theRequestPartitionId, mySqlBuilder);
	}

	private Condition createPredicateComposite(@Nullable DbColumn theSourceJoinColumn, String theResourceName, String theSpnamePrefix, RuntimeSearchParam theParamDef, List<? extends IQueryParameterType> theNextAnd, RequestPartitionId theRequestPartitionId, SearchQueryBuilder theSqlBuilder) {

		Condition orCondidtion = null;
		for (IQueryParameterType next : theNextAnd) {

			if (!(next instanceof CompositeParam<?, ?>)) {
				throw new InvalidRequestException(Msg.code(1203) + "Invalid type for composite param (must be " + CompositeParam.class.getSimpleName() + ": " + next.getClass());
			}
			CompositeParam<?, ?> cp = (CompositeParam<?, ?>) next;

			List<RuntimeSearchParam> componentParams = JpaParamUtil.resolveComponentParameters(mySearchParamRegistry, theParamDef);
			RuntimeSearchParam left = componentParams.get(0);
			IQueryParameterType leftValue = cp.getLeftValue();
			Condition leftPredicate = createPredicateCompositePart(theSourceJoinColumn, theResourceName, theSpnamePrefix, left, leftValue, theRequestPartitionId, theSqlBuilder);

			RuntimeSearchParam right = componentParams.get(1);
			IQueryParameterType rightValue = cp.getRightValue();
			Condition rightPredicate = createPredicateCompositePart(theSourceJoinColumn, theResourceName, theSpnamePrefix, right, rightValue, theRequestPartitionId, theSqlBuilder);

			Condition andCondition = toAndPredicate(leftPredicate, rightPredicate);

			if (orCondidtion == null) {
				orCondidtion = toOrPredicate(andCondition);
			} else {
				orCondidtion = toOrPredicate(orCondidtion, andCondition);
			}
		}

		return orCondidtion;
	}

	private Condition createPredicateCompositePart(@Nullable DbColumn theSourceJoinColumn, String theResourceName, String theSpnamePrefix, RuntimeSearchParam theParam, IQueryParameterType theParamValue, RequestPartitionId theRequestPartitionId, SearchQueryBuilder theSqlBuilder) {

		switch (theParam.getParamType()) {
			case STRING: {
				return createPredicateString(theSourceJoinColumn, theResourceName, theSpnamePrefix, theParam, Collections.singletonList(theParamValue), null, theRequestPartitionId, theSqlBuilder);
			}
			case TOKEN: {
				return createPredicateToken(theSourceJoinColumn, theResourceName, theSpnamePrefix, theParam, Collections.singletonList(theParamValue), null, theRequestPartitionId, theSqlBuilder);
			}
			case DATE: {
				return createPredicateDate(theSourceJoinColumn, theResourceName, theSpnamePrefix, theParam, Collections.singletonList(theParamValue), toOperation(((DateParam) theParamValue).getPrefix()), theRequestPartitionId, theSqlBuilder);
			}
			case QUANTITY: {
				return createPredicateQuantity(theSourceJoinColumn, theResourceName, theSpnamePrefix, theParam, Collections.singletonList(theParamValue), null, theRequestPartitionId, theSqlBuilder);
			}
			case NUMBER:
			case REFERENCE:
			case COMPOSITE:
			case URI:
			case HAS:
			case SPECIAL:
			default:
				throw new InvalidRequestException(Msg.code(1204) + "Don't know how to handle composite parameter with type of " + theParam.getParamType());
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
		return createPredicateDate(theSourceJoinColumn, theResourceName, theSpnamePrefix, theSearchParam, theList, theOperation, theRequestPartitionId, mySqlBuilder);
	}
	public Condition createPredicateDate(@Nullable DbColumn theSourceJoinColumn, String theResourceName,
													 String theSpnamePrefix, RuntimeSearchParam theSearchParam, List<? extends IQueryParameterType> theList,
													 SearchFilterParser.CompareOperation theOperation, RequestPartitionId theRequestPartitionId, SearchQueryBuilder theSqlBuilder) {

		String paramName = getParamNameWithPrefix(theSpnamePrefix, theSearchParam.getName());

		PredicateBuilderCacheLookupResult<DatePredicateBuilder> predicateBuilderLookupResult = createOrReusePredicateBuilder(PredicateBuilderTypeEnum.DATE, theSourceJoinColumn, paramName, () -> theSqlBuilder.addDatePredicateBuilder(theSourceJoinColumn));
		DatePredicateBuilder predicateBuilder = predicateBuilderLookupResult.getResult();
		boolean cacheHit = predicateBuilderLookupResult.isCacheHit();

		if (theList.get(0).getMissing() != null) {
			Boolean missing = theList.get(0).getMissing();
			return predicateBuilder.createPredicateParamMissingForNonReference(theResourceName, paramName, missing, theRequestPartitionId);
		}

		List<Condition> codePredicates = new ArrayList<>();

		for (IQueryParameterType nextOr : theList) {
			Condition p = predicateBuilder.createPredicateDateWithoutIdentityPredicate(nextOr, theOperation);
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
				throw new InvalidRequestException(Msg.code(1205) + "Don't know how to handle operation " + ((SearchFilterParser.FilterLogical) theFilter).getOperation());
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
			case Constants.PARAM_SOURCE: {
				TokenParam param = new TokenParam();
				param.setValueAsQueryToken(null, null, null, theFilter.getValue());
				return createPredicateSource(null, Collections.singletonList(param));
			}
			default:
				RuntimeSearchParam searchParam = mySearchParamRegistry.getActiveSearchParam(theResourceName, paramName);
				if (searchParam == null) {
					Collection<String> validNames = mySearchParamRegistry.getValidSearchParameterNamesIncludingMeta(theResourceName);
					String msg = myFhirContext.getLocalizer().getMessageSanitized(BaseStorageDao.class, "invalidSearchParameter", paramName, theResourceName, validNames);
					throw new InvalidRequestException(Msg.code(1206) + msg);
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
					return theQueryStack3.createPredicateReference(null, theResourceName, paramName, new ArrayList<>(), Collections.singletonList(referenceParam), operation, theRequest, theRequestPartitionId);
				} else if (typeEnum == RestSearchParameterTypeEnum.QUANTITY) {
					return theQueryStack3.createPredicateQuantity(null, theResourceName, null, searchParam, Collections.singletonList(new QuantityParam(theFilter.getValue())), theFilter.getOperation(), theRequestPartitionId);
				} else if (typeEnum == RestSearchParameterTypeEnum.COMPOSITE) {
					throw new InvalidRequestException(Msg.code(1207) + "Composite search parameters not currently supported with _filter clauses");
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
				throw new InvalidRequestException(Msg.code(1208) + "Invalid resource type: " + targetResourceType);
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
				RuntimeSearchParam owningParameterDef = mySearchParamRegistry.getRuntimeSearchParam(targetResourceType, paramName);

				//Ensure that the name of the back-referenced search param on the target (e.g. the `subject` in Patient?_has:Observation:subject:code=sys|val)
				//exists on the target resource, or in the top-level Resource resource.
				mySearchParamRegistry.getRuntimeSearchParam(targetResourceType, paramReference);


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

			List<String> paths = join.createResourceLinkPaths(targetResourceType, paramReference, new ArrayList<>());
			Condition typePredicate = BinaryCondition.equalTo(join.getColumnTargetResourceType(), mySqlBuilder.generatePlaceholder(theResourceType));
			Condition pathPredicate = toEqualToOrInPredicate(join.getColumnSourcePath(), mySqlBuilder.generatePlaceholders(paths));
			Condition linkedPredicate = searchForIdsWithAndOr(join.getColumnSrcResourceId(), targetResourceType, parameterName, Collections.singletonList(orValues), theRequest, theRequestPartitionId, SearchContainedModeEnum.FALSE);
			andPredicates.add(toAndPredicate(partitionPredicate, pathPredicate, typePredicate, linkedPredicate));
		}

		return toAndPredicate(andPredicates);
	}

	public Condition createPredicateNumber(@Nullable DbColumn theSourceJoinColumn, String theResourceName,
														String theSpnamePrefix, RuntimeSearchParam theSearchParam, List<? extends IQueryParameterType> theList,
														SearchFilterParser.CompareOperation theOperation, RequestPartitionId theRequestPartitionId) {
		return createPredicateNumber(theSourceJoinColumn, theResourceName, theSpnamePrefix, theSearchParam, theList, theOperation, theRequestPartitionId, mySqlBuilder);
	}

	public Condition createPredicateNumber(@Nullable DbColumn theSourceJoinColumn, String theResourceName,
														String theSpnamePrefix, RuntimeSearchParam theSearchParam, List<? extends IQueryParameterType> theList,
														SearchFilterParser.CompareOperation theOperation, RequestPartitionId theRequestPartitionId, SearchQueryBuilder theSqlBuilder) {

		String paramName = getParamNameWithPrefix(theSpnamePrefix, theSearchParam.getName());

		NumberPredicateBuilder join = createOrReusePredicateBuilder(PredicateBuilderTypeEnum.NUMBER, theSourceJoinColumn, paramName, () -> theSqlBuilder.addNumberPredicateBuilder(theSourceJoinColumn)).getResult();

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
				throw new IllegalArgumentException(Msg.code(1211) + "Invalid token type: " + nextOr.getClass());
			}

		}

		return join.combineWithRequestPartitionIdPredicate(theRequestPartitionId, ComboCondition.or(codePredicates.toArray(new Condition[0])));
	}

	public Condition createPredicateQuantity(@Nullable DbColumn theSourceJoinColumn, String theResourceName,
														  String theSpnamePrefix, RuntimeSearchParam theSearchParam, List<? extends IQueryParameterType> theList,
														  SearchFilterParser.CompareOperation theOperation, RequestPartitionId theRequestPartitionId) {
		return createPredicateQuantity(theSourceJoinColumn, theResourceName, theSpnamePrefix, theSearchParam, theList, theOperation, theRequestPartitionId, mySqlBuilder);
	}

	public Condition createPredicateQuantity(@Nullable DbColumn theSourceJoinColumn, String theResourceName,
														  String theSpnamePrefix, RuntimeSearchParam theSearchParam, List<? extends IQueryParameterType> theList,
														  SearchFilterParser.CompareOperation theOperation, RequestPartitionId theRequestPartitionId, SearchQueryBuilder theSqlBuilder) {

		String paramName = getParamNameWithPrefix(theSpnamePrefix, theSearchParam.getName());

		if (theList.get(0).getMissing() != null) {
			QuantityBasePredicateBuilder join = createOrReusePredicateBuilder(PredicateBuilderTypeEnum.QUANTITY, theSourceJoinColumn, theSearchParam.getName(), () -> theSqlBuilder.addQuantityPredicateBuilder(theSourceJoinColumn)).getResult();
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
				join = createOrReusePredicateBuilder(PredicateBuilderTypeEnum.QUANTITY, theSourceJoinColumn, paramName, () -> theSqlBuilder.addQuantityNormalizedPredicateBuilder(theSourceJoinColumn)).getResult();
				quantityParams = normalizedQuantityParams;
			}
		}

		if (join == null) {
			join = createOrReusePredicateBuilder(PredicateBuilderTypeEnum.QUANTITY, theSourceJoinColumn, paramName, () -> theSqlBuilder.addQuantityPredicateBuilder(theSourceJoinColumn)).getResult();
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
															List<String> theQualifiers,
															List<? extends IQueryParameterType> theList,
															SearchFilterParser.CompareOperation theOperation,
															RequestDetails theRequest,
															RequestPartitionId theRequestPartitionId) {
		return createPredicateReference(theSourceJoinColumn, theResourceName, theParamName, theQualifiers, theList, theOperation, theRequest, theRequestPartitionId, mySqlBuilder);
	}

	public Condition createPredicateReference(@Nullable DbColumn theSourceJoinColumn,
															String theResourceName,
															String theParamName,
															List<String> theQualifiers,
															List<? extends IQueryParameterType> theList,
															SearchFilterParser.CompareOperation theOperation,
															RequestDetails theRequest,
															RequestPartitionId theRequestPartitionId, SearchQueryBuilder theSqlBuilder) {

		if ((theOperation != null) &&
			(theOperation != SearchFilterParser.CompareOperation.eq) &&
			(theOperation != SearchFilterParser.CompareOperation.ne)) {
			throw new InvalidRequestException(Msg.code(1212) + "Invalid operator specified for reference predicate.  Supported operators for reference predicate are \"eq\" and \"ne\".");
		}

		if (theList.get(0).getMissing() != null) {
			SearchParamPresentPredicateBuilder join = theSqlBuilder.addSearchParamPresentPredicateBuilder(theSourceJoinColumn);
			return join.createPredicateParamMissingForReference(theResourceName, theParamName, theList.get(0).getMissing(), theRequestPartitionId);

		}

		ResourceLinkPredicateBuilder predicateBuilder = createOrReusePredicateBuilder(PredicateBuilderTypeEnum.REFERENCE, theSourceJoinColumn, theParamName, () -> theSqlBuilder.addReferencePredicateBuilder(this, theSourceJoinColumn)).getResult();
		return predicateBuilder.createPredicate(theRequest, theResourceName, theParamName, theQualifiers, theList, theOperation, theRequestPartitionId);
	}

	private class ChainElement {
		private final String myResourceType;
		private final String mySearchParameterName;
		private final String myPath;

		public ChainElement(String theResourceType, String theSearchParameterName, String thePath) {
			this.myResourceType = theResourceType;
			this.mySearchParameterName = theSearchParameterName;
			this.myPath = thePath;
		}

		public String getResourceType() {
			return myResourceType;
		}

		public String getPath() { return myPath; }
		
		public String getSearchParameterName() { return mySearchParameterName; }

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			ChainElement that = (ChainElement) o;
			return myResourceType.equals(that.myResourceType) && mySearchParameterName.equals(that.mySearchParameterName) && myPath.equals(that.myPath);
		}

		@Override
		public int hashCode() {
			return Objects.hash(myResourceType, mySearchParameterName, myPath);
		}
	}

	private class ReferenceChainExtractor {
		private final Map<List<ChainElement>,Set<LeafNodeDefinition>> myChains = Maps.newHashMap();

		public Map<List<ChainElement>,Set<LeafNodeDefinition>> getChains() { return myChains; }

		private boolean isReferenceParamValid(ReferenceParam theReferenceParam) {
			return split(theReferenceParam.getChain(), '.').length <= 3;
		}

		private List<String> extractPaths(String theResourceType, RuntimeSearchParam theSearchParam) {
			List<String> pathsForType = theSearchParam.getPathsSplit().stream()
				.map(String::trim)
				.filter(t -> t.startsWith(theResourceType))
				.collect(Collectors.toList());
			if (pathsForType.isEmpty()) {
				ourLog.warn("Search parameter {} does not have a path for resource type {}.", theSearchParam.getName(), theResourceType);
			}

			return pathsForType;
		}

		public void deriveChains(String theResourceType, RuntimeSearchParam theSearchParam, List<? extends IQueryParameterType> theList) {
			List<String> paths = extractPaths(theResourceType, theSearchParam);
			for (String path : paths) {
				List<ChainElement> searchParams = Lists.newArrayList();
				searchParams.add(new ChainElement(theResourceType, theSearchParam.getName(), path));
				for (IQueryParameterType nextOr : theList) {
					String targetValue = nextOr.getValueAsQueryToken(myFhirContext);
					if (nextOr instanceof ReferenceParam) {
						ReferenceParam referenceParam = (ReferenceParam) nextOr;

						if (!isReferenceParamValid(referenceParam)) {
							throw new InvalidRequestException(Msg.code(2007) +
								"The search chain " + theSearchParam.getName() + "." + referenceParam.getChain() +
								" is too long. Only chains up to three references are supported.");
						}

						String targetChain = referenceParam.getChain();
						List<String> qualifiers = Lists.newArrayList(referenceParam.getResourceType());

						processNextLinkInChain(searchParams, theSearchParam, targetChain, targetValue, qualifiers, referenceParam.getResourceType());

					}
				}
			}
		}

		private void processNextLinkInChain(List<ChainElement> theSearchParams, RuntimeSearchParam thePreviousSearchParam, String theChain, String theTargetValue, List<String> theQualifiers, String theResourceType) {

			String nextParamName = theChain;
			String nextChain = null;
			String nextQualifier = null;
			int linkIndex = theChain.indexOf('.');
			if (linkIndex != -1) {
				nextParamName = theChain.substring(0, linkIndex);
				nextChain = theChain.substring(linkIndex+1);
			}

			int qualifierIndex = nextParamName.indexOf(':');
			if (qualifierIndex != -1) {
				nextParamName = nextParamName.substring(0, qualifierIndex);
				nextQualifier = nextParamName.substring(qualifierIndex);
			}

			List<String> qualifiersBranch = Lists.newArrayList();
			qualifiersBranch.addAll(theQualifiers);
			qualifiersBranch.add(nextQualifier);

			boolean searchParamFound = false;
			for (String nextTarget : thePreviousSearchParam.getTargets()) {
				RuntimeSearchParam nextSearchParam = null;
				if (StringUtils.isBlank(theResourceType) || theResourceType.equals(nextTarget)) {
					nextSearchParam = mySearchParamRegistry.getActiveSearchParam(nextTarget, nextParamName);
				}
				if (nextSearchParam != null) {
					searchParamFound = true;
					// If we find a search param on this resource type for this parameter name, keep iterating
					//  Otherwise, abandon this branch and carry on to the next one
					if (StringUtils.isEmpty(nextChain)) {
						// We've reached the end of the chain
						ArrayList<IQueryParameterType> orValues = Lists.newArrayList();

						if (RestSearchParameterTypeEnum.REFERENCE.equals(nextSearchParam.getParamType())) {
							orValues.add(new ReferenceParam(nextQualifier, "", theTargetValue));
						} else {
							IQueryParameterType qp = toParameterType(nextSearchParam);
							qp.setValueAsQueryToken(myFhirContext, nextSearchParam.getName(), null, theTargetValue);
							orValues.add(qp);
						}

						Set<LeafNodeDefinition> leafNodes = myChains.get(theSearchParams);
						if (leafNodes == null) {
							leafNodes = Sets.newHashSet();
							myChains.put(theSearchParams, leafNodes);
						}
						leafNodes.add(new LeafNodeDefinition(nextSearchParam, orValues, nextTarget, nextParamName, "", qualifiersBranch));
					} else {
						List<String> nextPaths = extractPaths(nextTarget, nextSearchParam);
						for (String nextPath : nextPaths) {
							List<ChainElement> searchParamBranch = Lists.newArrayList();
							searchParamBranch.addAll(theSearchParams);

							searchParamBranch.add(new ChainElement(nextTarget, nextSearchParam.getName(), nextPath));
							processNextLinkInChain(searchParamBranch, nextSearchParam, nextChain, theTargetValue, qualifiersBranch, nextQualifier);
						}
					}
				}
			}
			if (!searchParamFound) {
				throw new InvalidRequestException(Msg.code(1214) + myFhirContext.getLocalizer().getMessage(BaseStorageDao.class, "invalidParameterChain", thePreviousSearchParam.getName() + '.' + theChain));
			}
		}
	}

	private static class LeafNodeDefinition {
		private final RuntimeSearchParam myParamDefinition;
		private final ArrayList<IQueryParameterType> myOrValues;
		private final String myLeafTarget;
		private final String myLeafParamName;
		private final String myLeafPathPrefix;
		private final List<String> myQualifiers;

		public LeafNodeDefinition(RuntimeSearchParam theParamDefinition, ArrayList<IQueryParameterType> theOrValues, String theLeafTarget, String theLeafParamName, String theLeafPathPrefix, List<String> theQualifiers) {
			myParamDefinition = theParamDefinition;
			myOrValues = theOrValues;
			myLeafTarget = theLeafTarget;
			myLeafParamName = theLeafParamName;
			myLeafPathPrefix = theLeafPathPrefix;
			myQualifiers = theQualifiers;
		}

		public RuntimeSearchParam getParamDefinition() {
			return myParamDefinition;
		}

		public ArrayList<IQueryParameterType> getOrValues() {
			return myOrValues;
		}

		public String getLeafTarget() {
			return myLeafTarget;
		}

		public String getLeafParamName() {
			return myLeafParamName;
		}

		public String getLeafPathPrefix() {
			return myLeafPathPrefix;
		}

		public List<String> getQualifiers() {
			return myQualifiers;
		}

		public LeafNodeDefinition withPathPrefix(String theResourceType, String theName) {
			return new LeafNodeDefinition(myParamDefinition, myOrValues, theResourceType, myLeafParamName, theName, myQualifiers);
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			LeafNodeDefinition that = (LeafNodeDefinition) o;
			return Objects.equals(myParamDefinition, that.myParamDefinition) && Objects.equals(myOrValues, that.myOrValues) && Objects.equals(myLeafTarget, that.myLeafTarget) && Objects.equals(myLeafParamName, that.myLeafParamName) && Objects.equals(myLeafPathPrefix, that.myLeafPathPrefix) && Objects.equals(myQualifiers, that.myQualifiers);
		}

		@Override
		public int hashCode() {
			return Objects.hash(myParamDefinition, myOrValues, myLeafTarget, myLeafParamName, myLeafPathPrefix, myQualifiers);
		}
	}

	public Condition createPredicateReferenceForContainedResource(@Nullable DbColumn theSourceJoinColumn,
																					  String theResourceName, RuntimeSearchParam theSearchParam,
																					  List<? extends IQueryParameterType> theList, SearchFilterParser.CompareOperation theOperation,
																					  RequestDetails theRequest, RequestPartitionId theRequestPartitionId) {
		// A bit of a hack, but we need to turn off cache reuse while in this method so that we don't try to reuse builders across different subselects
		EnumSet<PredicateBuilderTypeEnum> cachedReusePredicateBuilderTypes = EnumSet.copyOf(myReusePredicateBuilderTypes);
		myReusePredicateBuilderTypes.clear();

		UnionQuery union = new UnionQuery(SetOperationQuery.Type.UNION);

		ReferenceChainExtractor chainExtractor = new ReferenceChainExtractor();
		chainExtractor.deriveChains(theResourceName, theSearchParam, theList);
		Map<List<ChainElement>,Set<LeafNodeDefinition>> chains = chainExtractor.getChains();

		Map<List<String>,Set<LeafNodeDefinition>> referenceLinks = Maps.newHashMap();
		for (List<ChainElement> nextChain : chains.keySet()) {
			Set<LeafNodeDefinition> leafNodes = chains.get(nextChain);

			collateChainedSearchOptions(referenceLinks, nextChain, leafNodes);
		}

		for (List<String> nextReferenceLink: referenceLinks.keySet()) {
			for (LeafNodeDefinition leafNodeDefinition : referenceLinks.get(nextReferenceLink)) {
				SearchQueryBuilder builder = mySqlBuilder.newChildSqlBuilder();
				DbColumn previousJoinColumn = null;

				// Create a reference link predicate to the subselect for every link but the last one
				for (String nextLink : nextReferenceLink) {
					// We don't want to call createPredicateReference() here, because the whole point is to avoid the recursion.
					// TODO: Are we missing any important business logic from that method? All tests are passing.
					ResourceLinkPredicateBuilder resourceLinkPredicateBuilder = builder.addReferencePredicateBuilder(this, previousJoinColumn);
					builder.addPredicate(resourceLinkPredicateBuilder.createPredicateSourcePaths(Lists.newArrayList(nextLink)));
					previousJoinColumn = resourceLinkPredicateBuilder.getColumnTargetResourceId();
				}

				Condition containedCondition = createIndexPredicate(
					previousJoinColumn,
					leafNodeDefinition.getLeafTarget(),
					leafNodeDefinition.getLeafPathPrefix(),
					leafNodeDefinition.getLeafParamName(),
					leafNodeDefinition.getParamDefinition(),
					leafNodeDefinition.getOrValues(),
					theOperation,
					leafNodeDefinition.getQualifiers(),
					theRequest,
					theRequestPartitionId,
					builder);

				builder.addPredicate(containedCondition);

				union.addQueries(builder.getSelect());
			}
		}

		InCondition inCondition;
		if (theSourceJoinColumn == null) {
			inCondition = new InCondition(mySqlBuilder.getOrCreateFirstPredicateBuilder(false).getResourceIdColumn(), union);
		} else {
			//-- for the resource link, need join with target_resource_id
			inCondition = new InCondition(theSourceJoinColumn, union);
		}

		// restore the state of this collection to turn caching back on before we exit
		myReusePredicateBuilderTypes.addAll(cachedReusePredicateBuilderTypes);
		return inCondition;
	}

	private void collateChainedSearchOptions(Map<List<String>, Set<LeafNodeDefinition>> referenceLinks, List<ChainElement> nextChain, Set<LeafNodeDefinition> leafNodes) {
		// Manually collapse the chain using all possible variants of contained resource patterns.
		// This is a bit excruciating to extend beyond three references. Do we want to find a way to automate this someday?
		// Note: the first element in each chain is assumed to be discrete. This may need to change when we add proper support for `_contained`
		if (nextChain.size() == 1) {
			// discrete -> discrete
			updateMapOfReferenceLinks(referenceLinks, Lists.newArrayList(nextChain.get(0).getPath()), leafNodes);
			// discrete -> contained
			updateMapOfReferenceLinks(referenceLinks, Lists.newArrayList(),
				leafNodes
					.stream()
					.map(t -> t.withPathPrefix(nextChain.get(0).getResourceType(), nextChain.get(0).getSearchParameterName()))
					.collect(Collectors.toSet()));
		} else if (nextChain.size() == 2) {
			// discrete -> discrete -> discrete
			updateMapOfReferenceLinks(referenceLinks, Lists.newArrayList(nextChain.get(0).getPath(), nextChain.get(1).getPath()), leafNodes);
			// discrete -> discrete -> contained
			updateMapOfReferenceLinks(referenceLinks, Lists.newArrayList(nextChain.get(0).getPath()),
				leafNodes
					.stream()
					.map(t -> t.withPathPrefix(nextChain.get(1).getResourceType(), nextChain.get(1).getSearchParameterName()))
					.collect(Collectors.toSet()));
			// discrete -> contained -> discrete
			updateMapOfReferenceLinks(referenceLinks, Lists.newArrayList(mergePaths(nextChain.get(0).getPath(), nextChain.get(1).getPath())), leafNodes);
			if (myModelConfig.isIndexOnContainedResourcesRecursively()) {
				// discrete -> contained -> contained
				updateMapOfReferenceLinks(referenceLinks, Lists.newArrayList(),
					leafNodes
						.stream()
						.map(t -> t.withPathPrefix(nextChain.get(0).getResourceType(), nextChain.get(0).getSearchParameterName() + "." + nextChain.get(1).getSearchParameterName()))
						.collect(Collectors.toSet()));
			}
		} else if (nextChain.size() == 3) {
			// discrete -> discrete -> discrete -> discrete
			updateMapOfReferenceLinks(referenceLinks, Lists.newArrayList(nextChain.get(0).getPath(), nextChain.get(1).getPath(), nextChain.get(2).getPath()), leafNodes);
			// discrete -> discrete -> discrete -> contained
			updateMapOfReferenceLinks(referenceLinks, Lists.newArrayList(nextChain.get(0).getPath(), nextChain.get(1).getPath()),
				leafNodes
					.stream()
					.map(t -> t.withPathPrefix(nextChain.get(2).getResourceType(), nextChain.get(2).getSearchParameterName()))
					.collect(Collectors.toSet()));
			// discrete -> discrete -> contained -> discrete
			updateMapOfReferenceLinks(referenceLinks, Lists.newArrayList(nextChain.get(0).getPath(), mergePaths(nextChain.get(1).getPath(), nextChain.get(2).getPath())), leafNodes);
			// discrete -> contained -> discrete -> discrete
			updateMapOfReferenceLinks(referenceLinks, Lists.newArrayList(mergePaths(nextChain.get(0).getPath(), nextChain.get(1).getPath()), nextChain.get(2).getPath()), leafNodes);
			// discrete -> contained -> discrete -> contained
			updateMapOfReferenceLinks(referenceLinks, Lists.newArrayList(mergePaths(nextChain.get(0).getPath(), nextChain.get(1).getPath())),
				leafNodes
					.stream()
					.map(t -> t.withPathPrefix(nextChain.get(2).getResourceType(), nextChain.get(2).getSearchParameterName()))
					.collect(Collectors.toSet()));
			if (myModelConfig.isIndexOnContainedResourcesRecursively()) {
				// discrete -> contained -> contained -> discrete
				updateMapOfReferenceLinks(referenceLinks, Lists.newArrayList(mergePaths(nextChain.get(0).getPath(), nextChain.get(1).getPath(), nextChain.get(2).getPath())), leafNodes);
				// discrete -> discrete -> contained -> contained
				updateMapOfReferenceLinks(referenceLinks, Lists.newArrayList(nextChain.get(0).getPath()),
					leafNodes
						.stream()
						.map(t -> t.withPathPrefix(nextChain.get(1).getResourceType(), nextChain.get(1).getSearchParameterName() + "." + nextChain.get(2).getSearchParameterName()))
						.collect(Collectors.toSet()));
				// discrete -> contained -> contained -> contained
				updateMapOfReferenceLinks(referenceLinks, Lists.newArrayList(),
					leafNodes
						.stream()
						.map(t -> t.withPathPrefix(nextChain.get(0).getResourceType(), nextChain.get(0).getSearchParameterName() + "." + nextChain.get(1).getSearchParameterName() + "." + nextChain.get(2).getSearchParameterName()))
						.collect(Collectors.toSet()));
			}
		} else {
			// TODO: the chain is too long, it isn't practical to hard-code all the possible patterns. If anyone ever needs this, we should revisit the approach
			throw new InvalidRequestException(Msg.code(2011) +
				"The search chain is too long. Only chains of up to three references are supported.");
		}
	}

	private void updateMapOfReferenceLinks(Map<List<String>, Set<LeafNodeDefinition>> theReferenceLinksMap, ArrayList<String> thePath, Set<LeafNodeDefinition> theLeafNodesToAdd) {
		Set<LeafNodeDefinition> leafNodes = theReferenceLinksMap.get(thePath);
		if (leafNodes == null) {
			leafNodes = Sets.newHashSet();
			theReferenceLinksMap.put(thePath, leafNodes);
		}
		leafNodes.addAll(theLeafNodesToAdd);
	}

	private String mergePaths(String... paths) {
		String result = "";
		for (String nextPath : paths) {
			int separatorIndex = nextPath.indexOf('.');
			if (StringUtils.isEmpty(result)) {
				result = nextPath;
			} else {
				result = result + nextPath.substring(separatorIndex);
			}
		}
		return result;
	}

	private Condition createIndexPredicate(DbColumn theSourceJoinColumn, String theResourceName, String theSpnamePrefix, String theParamName, RuntimeSearchParam theParamDefinition, ArrayList<IQueryParameterType> theOrValues, SearchFilterParser.CompareOperation theOperation, List<String> theQualifiers, RequestDetails theRequest, RequestPartitionId theRequestPartitionId, SearchQueryBuilder theSqlBuilder) {
		Condition containedCondition;

		switch (theParamDefinition.getParamType()) {
			case DATE:
				containedCondition = createPredicateDate(theSourceJoinColumn, theResourceName, theSpnamePrefix, theParamDefinition,
					theOrValues, theOperation, theRequestPartitionId, theSqlBuilder);
				break;
			case NUMBER:
				containedCondition = createPredicateNumber(theSourceJoinColumn, theResourceName, theSpnamePrefix, theParamDefinition,
					theOrValues, theOperation, theRequestPartitionId, theSqlBuilder);
				break;
			case QUANTITY:
				containedCondition = createPredicateQuantity(theSourceJoinColumn, theResourceName, theSpnamePrefix, theParamDefinition,
					theOrValues, theOperation, theRequestPartitionId, theSqlBuilder);
				break;
			case STRING:
				containedCondition = createPredicateString(theSourceJoinColumn, theResourceName, theSpnamePrefix, theParamDefinition,
					theOrValues, theOperation, theRequestPartitionId, theSqlBuilder);
				break;
			case TOKEN:
				containedCondition = createPredicateToken(theSourceJoinColumn, theResourceName, theSpnamePrefix, theParamDefinition,
					theOrValues, theOperation, theRequestPartitionId, theSqlBuilder);
				break;
			case COMPOSITE:
				containedCondition = createPredicateComposite(theSourceJoinColumn, theResourceName, theSpnamePrefix, theParamDefinition,
					theOrValues, theRequestPartitionId, theSqlBuilder);
				break;
			case URI:
				containedCondition = createPredicateUri(theSourceJoinColumn, theResourceName, theSpnamePrefix, theParamDefinition,
					theOrValues, theOperation, theRequest, theRequestPartitionId, theSqlBuilder);
				break;
			case REFERENCE:
				containedCondition = createPredicateReference(theSourceJoinColumn, theResourceName, StringUtils.isBlank(theSpnamePrefix) ? theParamName : theSpnamePrefix + "." + theParamName, theQualifiers,
					theOrValues, theOperation, theRequest, theRequestPartitionId, theSqlBuilder);
				break;
			case HAS:
			case SPECIAL:
			default:
				throw new InvalidRequestException(
					Msg.code(1215) + "The search type:" + theParamDefinition.getParamType() + " is not supported.");
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
			throw new InvalidRequestException(Msg.code(1216) + msg);
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
		return createPredicateString(theSourceJoinColumn, theResourceName, theSpnamePrefix, theSearchParam, theList, theOperation, theRequestPartitionId, mySqlBuilder);
	}

	public Condition createPredicateString(@Nullable DbColumn theSourceJoinColumn, String theResourceName,
														String theSpnamePrefix, RuntimeSearchParam theSearchParam, List<? extends IQueryParameterType> theList,
														SearchFilterParser.CompareOperation theOperation, RequestPartitionId theRequestPartitionId,
														SearchQueryBuilder theSqlBuilder) {

		String paramName = getParamNameWithPrefix(theSpnamePrefix, theSearchParam.getName());

		StringPredicateBuilder join = createOrReusePredicateBuilder(PredicateBuilderTypeEnum.STRING, theSourceJoinColumn, paramName, () -> theSqlBuilder.addStringPredicateBuilder(theSourceJoinColumn)).getResult();

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
			throw new IllegalArgumentException(Msg.code(1217) + "Param name: " + theParamName); // shouldn't happen
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
						throw new InvalidRequestException(Msg.code(1218) + "Invalid " + theParamName + " parameter (must supply a value/code and not just a system): " + nextParam.getValueAsQueryToken(myFhirContext));
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
		return createPredicateToken(theSourceJoinColumn, theResourceName, theSpnamePrefix, theSearchParam, theList, theOperation, theRequestPartitionId, mySqlBuilder);
	}

	public Condition createPredicateToken(@Nullable DbColumn theSourceJoinColumn, String theResourceName,
													  String theSpnamePrefix, RuntimeSearchParam theSearchParam, List<? extends IQueryParameterType> theList,
													  SearchFilterParser.CompareOperation theOperation, RequestPartitionId theRequestPartitionId, SearchQueryBuilder theSqlBuilder) {

		List<IQueryParameterType> tokens = new ArrayList<>(); 
		
		boolean paramInverted = false;
		TokenParamModifier modifier;
		
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
							throw new MethodNotAllowedException(Msg.code(1219) + msg);
						}

						return createPredicateString(theSourceJoinColumn, theResourceName, theSpnamePrefix, theSearchParam, theList, null, theRequestPartitionId, theSqlBuilder);
					} 
					
					modifier = id.getModifier();
					// for :not modifier, create a token and remove the :not modifier
					if (modifier == TokenParamModifier.NOT) {
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
			SearchQueryBuilder sqlBuilder = theSqlBuilder.newChildSqlBuilder();
			TokenPredicateBuilder tokenSelector = sqlBuilder.addTokenPredicateBuilder(null);
			sqlBuilder.addPredicate(tokenSelector.createPredicateToken(tokens, theResourceName, theSpnamePrefix, theSearchParam, theRequestPartitionId));
			SelectQuery sql = sqlBuilder.getSelect();
			Expression subSelect = new Subquery(sql);
			
			join = theSqlBuilder.getOrCreateFirstPredicateBuilder();
			
			if (theSourceJoinColumn == null) {
				predicate = new InCondition(join.getResourceIdColumn(), subSelect).setNegate(true);
			} else {
				//-- for the resource link, need join with target_resource_id
			    predicate = new InCondition(theSourceJoinColumn, subSelect).setNegate(true);
			}
						
		} else {
		
			TokenPredicateBuilder tokenJoin = createOrReusePredicateBuilder(PredicateBuilderTypeEnum.TOKEN, theSourceJoinColumn, paramName, () -> theSqlBuilder.addTokenPredicateBuilder(theSourceJoinColumn)).getResult();

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
		return createPredicateUri(theSourceJoinColumn, theResourceName, theSpnamePrefix, theSearchParam, theList, theOperation, theRequestDetails, theRequestPartitionId, mySqlBuilder);
	}

	public Condition createPredicateUri(@Nullable DbColumn theSourceJoinColumn, String theResourceName,
													String theSpnamePrefix, RuntimeSearchParam theSearchParam, List<? extends IQueryParameterType> theList,
													SearchFilterParser.CompareOperation theOperation, RequestDetails theRequestDetails,
													RequestPartitionId theRequestPartitionId, SearchQueryBuilder theSqlBuilder) {

		String paramName = getParamNameWithPrefix(theSpnamePrefix, theSearchParam.getName());

		UriPredicateBuilder join = theSqlBuilder.addUriPredicateBuilder(theSourceJoinColumn);

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

			case Constants.PARAM_HAS:
				return createPredicateHas(theSourceJoinColumn, theResourceName, theAndOrParams, theRequest, theRequestPartitionId);

			case Constants.PARAM_TAG:
			case Constants.PARAM_PROFILE:
			case Constants.PARAM_SECURITY:
				if (myDaoConfig.getTagStorageMode() == DaoConfig.TagStorageModeEnum.INLINE) {
					return createPredicateSearchParameter(theSourceJoinColumn, theResourceName, theParamName, theAndOrParams, theRequest, theRequestPartitionId);
				} else {
					return createPredicateTag(theSourceJoinColumn, theAndOrParams, theParamName, theRequestPartitionId);
				}

			case Constants.PARAM_SOURCE:
				return createPredicateSourceForAndList(theSourceJoinColumn, theAndOrParams);

			default:
				return createPredicateSearchParameter(theSourceJoinColumn, theResourceName, theParamName, theAndOrParams, theRequest, theRequestPartitionId);

		}

	}

	@Nullable
	private Condition createPredicateSearchParameter(@Nullable DbColumn theSourceJoinColumn, String theResourceName, String theParamName, List<List<IQueryParameterType>> theAndOrParams, RequestDetails theRequest, RequestPartitionId theRequestPartitionId) {
		List<Condition> andPredicates = new ArrayList<>();
		RuntimeSearchParam nextParamDef = mySearchParamRegistry.getActiveSearchParam(theResourceName, theParamName);
		if (nextParamDef != null) {

			if (myPartitionSettings.isPartitioningEnabled() && myPartitionSettings.isIncludePartitionInSearchHashes()) {
				if (theRequestPartitionId.isAllPartitions()) {
					throw new PreconditionFailedException(Msg.code(1220) + "This server is not configured to support search against all partitions");
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
						if (isEligibleForContainedResourceSearch(nextAnd)) {
							andPredicates.add(createPredicateReferenceForContainedResource(theSourceJoinColumn, theResourceName, nextParamDef, nextAnd, null, theRequest, theRequestPartitionId));
						} else {
							andPredicates.add(createPredicateReference(theSourceJoinColumn, theResourceName, theParamName, new ArrayList<>(), nextAnd, null, theRequest, theRequestPartitionId));
						}
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
							throw new InvalidRequestException(Msg.code(1221) + "Error parsing _filter syntax: " + theE.getMessage());
						}
						if (filter != null) {

							if (!myDaoConfig.isFilterParameterEnabled()) {
								throw new InvalidRequestException(Msg.code(1222) + Constants.PARAM_FILTER + " parameter is disabled on this server");
							}

							Condition predicate = createPredicateFilter(this, filter, theResourceName, theRequest, theRequestPartitionId);
							if (predicate != null) {
								mySqlBuilder.addPredicate(predicate);
							}
						}
					}

				} else {
					String msg = myFhirContext.getLocalizer().getMessageSanitized(BaseStorageDao.class, "invalidSearchParameter", theParamName, theResourceName, mySearchParamRegistry.getValidSearchParameterNamesIncludingMeta(theResourceName));
					throw new InvalidRequestException(Msg.code(1223) + msg);
				}
			}
		}

		return toAndPredicate(andPredicates);
	}

	private boolean isEligibleForContainedResourceSearch(List<? extends IQueryParameterType> nextAnd) {
		return myModelConfig.isIndexOnContainedResources() &&
			nextAnd.stream()
				.filter(t -> t instanceof ReferenceParam)
				.map(t -> ((ReferenceParam) t).getChain())
				.anyMatch(StringUtils::isNotBlank);
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


	// expand out the pids
	public void addPredicateEverythingOperation(String theResourceName, Long... theTargetPids) {
		ResourceLinkPredicateBuilder table = mySqlBuilder.addReferencePredicateBuilder(this, null);
		Condition predicate = table.createEverythingPredicate(theResourceName, theTargetPids);
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
					throw new InternalErrorException(Msg.code(1224) + "Parameter " + theParam.getName() + " has " + compositeOf.size() + " composite parts. Don't know how handlt this.");
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
				throw new InvalidRequestException(Msg.code(1225) + "The search type: " + theParam.getParamType() + " is not supported.");
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
