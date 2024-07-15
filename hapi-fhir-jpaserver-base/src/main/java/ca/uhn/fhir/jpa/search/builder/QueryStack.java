/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.search.builder;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.exception.TokenParamFormatInvalidRequestException;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.BaseStorageDao;
import ca.uhn.fhir.jpa.dao.predicate.SearchFilterParser;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.NormalizedQuantitySearchLevel;
import ca.uhn.fhir.jpa.model.entity.TagTypeEnum;
import ca.uhn.fhir.jpa.model.util.UcumServiceUtil;
import ca.uhn.fhir.jpa.search.builder.models.MissingParameterQueryParams;
import ca.uhn.fhir.jpa.search.builder.models.MissingQueryParameterPredicateParams;
import ca.uhn.fhir.jpa.search.builder.models.PredicateBuilderCacheKey;
import ca.uhn.fhir.jpa.search.builder.models.PredicateBuilderCacheLookupResult;
import ca.uhn.fhir.jpa.search.builder.models.PredicateBuilderTypeEnum;
import ca.uhn.fhir.jpa.search.builder.predicate.BaseJoiningPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.BaseQuantityPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.BaseSearchParamPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.ComboNonUniqueSearchParameterPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.ComboUniqueSearchParameterPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.CoordsPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.DatePredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.ICanMakeMissingParamPredicate;
import ca.uhn.fhir.jpa.search.builder.predicate.NumberPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.ParsedLocationParam;
import ca.uhn.fhir.jpa.search.builder.predicate.ResourceIdPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.ResourceLinkPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.ResourceTablePredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.SearchParamPresentPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.SourcePredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.StringPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.TagPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.TokenPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.predicate.UriPredicateBuilder;
import ca.uhn.fhir.jpa.search.builder.sql.PredicateBuilderFactory;
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
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.CompositeParam;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.HasParam;
import ca.uhn.fhir.rest.param.NumberParam;
import ca.uhn.fhir.rest.param.QuantityParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.SpecialParam;
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
import com.healthmarketscience.sqlbuilder.SelectQuery;
import com.healthmarketscience.sqlbuilder.SetOperationQuery;
import com.healthmarketscience.sqlbuilder.Subquery;
import com.healthmarketscience.sqlbuilder.UnionQuery;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.search.builder.QueryStack.SearchForIdsParams.with;
import static ca.uhn.fhir.jpa.util.QueryParameterUtils.fromOperation;
import static ca.uhn.fhir.jpa.util.QueryParameterUtils.getChainedPart;
import static ca.uhn.fhir.jpa.util.QueryParameterUtils.getParamNameWithPrefix;
import static ca.uhn.fhir.jpa.util.QueryParameterUtils.toAndPredicate;
import static ca.uhn.fhir.jpa.util.QueryParameterUtils.toEqualToOrInPredicate;
import static ca.uhn.fhir.jpa.util.QueryParameterUtils.toOperation;
import static ca.uhn.fhir.jpa.util.QueryParameterUtils.toOrPredicate;
import static ca.uhn.fhir.rest.api.Constants.PARAM_HAS;
import static ca.uhn.fhir.rest.api.Constants.PARAM_ID;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.split;

public class QueryStack {

	private static final Logger ourLog = LoggerFactory.getLogger(QueryStack.class);
	public static final String LOCATION_POSITION = "Location.position";
	private static final Pattern PATTERN_DOT_AND_ALL_AFTER = Pattern.compile("\\..*");

	private final FhirContext myFhirContext;
	private final SearchQueryBuilder mySqlBuilder;
	private final SearchParameterMap mySearchParameters;
	private final ISearchParamRegistry mySearchParamRegistry;
	private final PartitionSettings myPartitionSettings;
	private final JpaStorageSettings myStorageSettings;
	private final EnumSet<PredicateBuilderTypeEnum> myReusePredicateBuilderTypes;
	private Map<PredicateBuilderCacheKey, BaseJoiningPredicateBuilder> myJoinMap;
	private Map<String, BaseJoiningPredicateBuilder> myParamNameToPredicateBuilderMap;
	// used for _offset queries with sort, should be removed once the fix is applied to the async path too.
	private boolean myUseAggregate;

	/**
	 * Constructor
	 */
	public QueryStack(
			SearchParameterMap theSearchParameters,
			JpaStorageSettings theStorageSettings,
			FhirContext theFhirContext,
			SearchQueryBuilder theSqlBuilder,
			ISearchParamRegistry theSearchParamRegistry,
			PartitionSettings thePartitionSettings) {
		this(
				theSearchParameters,
				theStorageSettings,
				theFhirContext,
				theSqlBuilder,
				theSearchParamRegistry,
				thePartitionSettings,
				EnumSet.of(PredicateBuilderTypeEnum.DATE));
	}

	/**
	 * Constructor
	 */
	private QueryStack(
			SearchParameterMap theSearchParameters,
			JpaStorageSettings theStorageSettings,
			FhirContext theFhirContext,
			SearchQueryBuilder theSqlBuilder,
			ISearchParamRegistry theSearchParamRegistry,
			PartitionSettings thePartitionSettings,
			EnumSet<PredicateBuilderTypeEnum> theReusePredicateBuilderTypes) {
		myPartitionSettings = thePartitionSettings;
		assert theSearchParameters != null;
		assert theStorageSettings != null;
		assert theFhirContext != null;
		assert theSqlBuilder != null;

		mySearchParameters = theSearchParameters;
		myStorageSettings = theStorageSettings;
		myFhirContext = theFhirContext;
		mySqlBuilder = theSqlBuilder;
		mySearchParamRegistry = theSearchParamRegistry;
		myReusePredicateBuilderTypes = theReusePredicateBuilderTypes;
	}

	public void addSortOnCoordsNear(String theParamName, boolean theAscending, SearchParameterMap theParams) {
		boolean handled = false;
		if (myParamNameToPredicateBuilderMap != null) {
			BaseJoiningPredicateBuilder builder = myParamNameToPredicateBuilderMap.get(theParamName);
			if (builder instanceof CoordsPredicateBuilder) {
				CoordsPredicateBuilder coordsBuilder = (CoordsPredicateBuilder) builder;

				List<List<IQueryParameterType>> params = theParams.get(theParamName);
				if (params.size() > 0 && params.get(0).size() > 0) {
					IQueryParameterType param = params.get(0).get(0);
					ParsedLocationParam location = ParsedLocationParam.from(theParams, param);
					double latitudeValue = location.getLatitudeValue();
					double longitudeValue = location.getLongitudeValue();
					mySqlBuilder.addSortCoordsNear(coordsBuilder, latitudeValue, longitudeValue, theAscending);
					handled = true;
				}
			}
		}

		if (!handled) {
			String msg = myFhirContext
					.getLocalizer()
					.getMessageSanitized(QueryStack.class, "cantSortOnCoordParamWithoutValues", theParamName);
			throw new InvalidRequestException(Msg.code(2307) + msg);
		}
	}

	public void addSortOnDate(String theResourceName, String theParamName, boolean theAscending) {
		BaseJoiningPredicateBuilder firstPredicateBuilder = mySqlBuilder.getOrCreateFirstPredicateBuilder();
		DatePredicateBuilder datePredicateBuilder = mySqlBuilder.createDatePredicateBuilder();

		Condition hashIdentityPredicate =
				datePredicateBuilder.createHashIdentityPredicate(theResourceName, theParamName);

		addSortCustomJoin(firstPredicateBuilder, datePredicateBuilder, hashIdentityPredicate);

		mySqlBuilder.addSortDate(datePredicateBuilder.getColumnValueLow(), theAscending, myUseAggregate);
	}

	public void addSortOnLastUpdated(boolean theAscending) {
		ResourceTablePredicateBuilder resourceTablePredicateBuilder;
		BaseJoiningPredicateBuilder firstPredicateBuilder = mySqlBuilder.getOrCreateFirstPredicateBuilder();
		if (firstPredicateBuilder instanceof ResourceTablePredicateBuilder) {
			resourceTablePredicateBuilder = (ResourceTablePredicateBuilder) firstPredicateBuilder;
		} else {
			resourceTablePredicateBuilder =
					mySqlBuilder.addResourceTablePredicateBuilder(firstPredicateBuilder.getResourceIdColumn());
		}
		mySqlBuilder.addSortDate(resourceTablePredicateBuilder.getColumnLastUpdated(), theAscending, myUseAggregate);
	}

	public void addSortOnNumber(String theResourceName, String theParamName, boolean theAscending) {
		BaseJoiningPredicateBuilder firstPredicateBuilder = mySqlBuilder.getOrCreateFirstPredicateBuilder();
		NumberPredicateBuilder numberPredicateBuilder = mySqlBuilder.createNumberPredicateBuilder();

		Condition hashIdentityPredicate =
				numberPredicateBuilder.createHashIdentityPredicate(theResourceName, theParamName);

		addSortCustomJoin(firstPredicateBuilder, numberPredicateBuilder, hashIdentityPredicate);

		mySqlBuilder.addSortNumeric(numberPredicateBuilder.getColumnValue(), theAscending, myUseAggregate);
	}

	public void addSortOnQuantity(String theResourceName, String theParamName, boolean theAscending) {
		BaseJoiningPredicateBuilder firstPredicateBuilder = mySqlBuilder.getOrCreateFirstPredicateBuilder();

		BaseQuantityPredicateBuilder quantityPredicateBuilder = mySqlBuilder.createQuantityPredicateBuilder();

		Condition hashIdentityPredicate =
				quantityPredicateBuilder.createHashIdentityPredicate(theResourceName, theParamName);

		addSortCustomJoin(firstPredicateBuilder, quantityPredicateBuilder, hashIdentityPredicate);

		mySqlBuilder.addSortNumeric(quantityPredicateBuilder.getColumnValue(), theAscending, myUseAggregate);
	}

	public void addSortOnResourceId(boolean theAscending) {
		ResourceTablePredicateBuilder resourceTablePredicateBuilder;
		BaseJoiningPredicateBuilder firstPredicateBuilder = mySqlBuilder.getOrCreateFirstPredicateBuilder();
		if (firstPredicateBuilder instanceof ResourceTablePredicateBuilder) {
			resourceTablePredicateBuilder = (ResourceTablePredicateBuilder) firstPredicateBuilder;
		} else {
			resourceTablePredicateBuilder =
					mySqlBuilder.addResourceTablePredicateBuilder(firstPredicateBuilder.getResourceIdColumn());
		}
		mySqlBuilder.addSortString(resourceTablePredicateBuilder.getColumnFhirId(), theAscending, myUseAggregate);
	}

	/** Sort on RES_ID -- used to break ties for reliable sort */
	public void addSortOnResourcePID(boolean theAscending) {
		BaseJoiningPredicateBuilder predicateBuilder = mySqlBuilder.getOrCreateFirstPredicateBuilder();
		mySqlBuilder.addSortString(predicateBuilder.getResourceIdColumn(), theAscending);
	}

	public void addSortOnResourceLink(
			String theResourceName,
			String theReferenceTargetType,
			String theParamName,
			String theChain,
			boolean theAscending,
			SearchParameterMap theParams) {
		BaseJoiningPredicateBuilder firstPredicateBuilder = mySqlBuilder.getOrCreateFirstPredicateBuilder();
		ResourceLinkPredicateBuilder resourceLinkPredicateBuilder = mySqlBuilder.createReferencePredicateBuilder(this);

		Condition pathPredicate =
				resourceLinkPredicateBuilder.createPredicateSourcePaths(theResourceName, theParamName);

		addSortCustomJoin(firstPredicateBuilder, resourceLinkPredicateBuilder, pathPredicate);

		if (isBlank(theChain)) {
			mySqlBuilder.addSortNumeric(
					resourceLinkPredicateBuilder.getColumnTargetResourceId(), theAscending, myUseAggregate);
			return;
		}

		String targetType = null;
		RuntimeSearchParam param = mySearchParamRegistry.getActiveSearchParam(theResourceName, theParamName);
		if (theReferenceTargetType != null) {
			targetType = theReferenceTargetType;
		} else if (param.getTargets().size() > 1) {
			throw new InvalidRequestException(Msg.code(2287) + "Unable to sort on a chained parameter from '"
					+ theParamName + "' as this parameter has multiple target types. Please specify the target type.");
		} else if (param.getTargets().size() == 1) {
			targetType = param.getTargets().iterator().next();
		}

		if (isBlank(targetType)) {
			throw new InvalidRequestException(
					Msg.code(2288) + "Unable to sort on a chained parameter from '" + theParamName
							+ "' as this parameter as this parameter does not define a target type. Please specify the target type.");
		}

		RuntimeSearchParam targetSearchParameter = mySearchParamRegistry.getActiveSearchParam(targetType, theChain);
		if (targetSearchParameter == null) {
			Collection<String> validSearchParameterNames =
					mySearchParamRegistry.getActiveSearchParams(targetType).values().stream()
							.filter(t -> t.getParamType() == RestSearchParameterTypeEnum.STRING
									|| t.getParamType() == RestSearchParameterTypeEnum.TOKEN
									|| t.getParamType() == RestSearchParameterTypeEnum.DATE)
							.map(RuntimeSearchParam::getName)
							.sorted()
							.distinct()
							.collect(Collectors.toList());
			String msg = myFhirContext
					.getLocalizer()
					.getMessageSanitized(
							BaseStorageDao.class,
							"invalidSortParameter",
							theChain,
							targetType,
							validSearchParameterNames);
			throw new InvalidRequestException(Msg.code(2289) + msg);
		}

		// add a left-outer join to a predicate for the target type, then sort on value columns(s).
		switch (targetSearchParameter.getParamType()) {
			case STRING:
				StringPredicateBuilder stringPredicateBuilder = mySqlBuilder.createStringPredicateBuilder();
				addSortCustomJoin(
						resourceLinkPredicateBuilder.getColumnTargetResourceId(),
						stringPredicateBuilder,
						stringPredicateBuilder.createHashIdentityPredicate(targetType, theChain));

				mySqlBuilder.addSortString(
						stringPredicateBuilder.getColumnValueNormalized(), theAscending, myUseAggregate);
				return;

			case TOKEN:
				TokenPredicateBuilder tokenPredicateBuilder = mySqlBuilder.createTokenPredicateBuilder();
				addSortCustomJoin(
						resourceLinkPredicateBuilder.getColumnTargetResourceId(),
						tokenPredicateBuilder,
						tokenPredicateBuilder.createHashIdentityPredicate(targetType, theChain));

				mySqlBuilder.addSortString(tokenPredicateBuilder.getColumnSystem(), theAscending, myUseAggregate);
				mySqlBuilder.addSortString(tokenPredicateBuilder.getColumnValue(), theAscending, myUseAggregate);
				return;

			case DATE:
				DatePredicateBuilder datePredicateBuilder = mySqlBuilder.createDatePredicateBuilder();
				addSortCustomJoin(
						resourceLinkPredicateBuilder.getColumnTargetResourceId(),
						datePredicateBuilder,
						datePredicateBuilder.createHashIdentityPredicate(targetType, theChain));

				mySqlBuilder.addSortDate(datePredicateBuilder.getColumnValueLow(), theAscending, myUseAggregate);
				return;

				/*
				 * Note that many of the options below aren't implemented because they
				 * don't seem useful to me, but they could theoretically be implemented
				 * if someone ever needed them. I'm not sure why you'd want to do a chained
				 * sort on a target that was a reference or a quantity, but if someone needed
				 * that we could implement it here.
				 */
			case SPECIAL: {
				if (LOCATION_POSITION.equals(targetSearchParameter.getPath())) {
					List<List<IQueryParameterType>> params = theParams.get(theParamName);
					if (params != null && !params.isEmpty() && !params.get(0).isEmpty()) {
						IQueryParameterType locationParam = params.get(0).get(0);
						final SpecialParam specialParam =
								new SpecialParam().setValue(locationParam.getValueAsQueryToken(myFhirContext));
						ParsedLocationParam location = ParsedLocationParam.from(theParams, specialParam);
						double latitudeValue = location.getLatitudeValue();
						double longitudeValue = location.getLongitudeValue();
						final CoordsPredicateBuilder coordsPredicateBuilder = mySqlBuilder.addCoordsPredicateBuilder(
								resourceLinkPredicateBuilder.getColumnTargetResourceId());
						mySqlBuilder.addSortCoordsNear(
								coordsPredicateBuilder, latitudeValue, longitudeValue, theAscending);
					} else {
						String msg = myFhirContext
								.getLocalizer()
								.getMessageSanitized(
										QueryStack.class, "cantSortOnCoordParamWithoutValues", theParamName);
						throw new InvalidRequestException(Msg.code(2497) + msg);
					}
					return;
				}
			}
			case NUMBER:
			case REFERENCE:
			case COMPOSITE:
			case QUANTITY:
			case URI:
			case HAS:

			default:
				throw new InvalidRequestException(Msg.code(2290) + "Unable to sort on a chained parameter "
						+ theParamName + "." + theChain + " as this parameter. Can not sort on chains of target type: "
						+ targetSearchParameter.getParamType().name());
		}
	}

	public void addSortOnString(String theResourceName, String theParamName, boolean theAscending) {
		BaseJoiningPredicateBuilder firstPredicateBuilder = mySqlBuilder.getOrCreateFirstPredicateBuilder();

		StringPredicateBuilder stringPredicateBuilder = mySqlBuilder.createStringPredicateBuilder();
		Condition hashIdentityPredicate =
				stringPredicateBuilder.createHashIdentityPredicate(theResourceName, theParamName);

		addSortCustomJoin(firstPredicateBuilder, stringPredicateBuilder, hashIdentityPredicate);

		mySqlBuilder.addSortString(stringPredicateBuilder.getColumnValueNormalized(), theAscending, myUseAggregate);
	}

	public void addSortOnToken(String theResourceName, String theParamName, boolean theAscending) {
		BaseJoiningPredicateBuilder firstPredicateBuilder = mySqlBuilder.getOrCreateFirstPredicateBuilder();

		TokenPredicateBuilder tokenPredicateBuilder = mySqlBuilder.createTokenPredicateBuilder();
		Condition hashIdentityPredicate =
				tokenPredicateBuilder.createHashIdentityPredicate(theResourceName, theParamName);

		addSortCustomJoin(firstPredicateBuilder, tokenPredicateBuilder, hashIdentityPredicate);

		mySqlBuilder.addSortString(tokenPredicateBuilder.getColumnSystem(), theAscending, myUseAggregate);
		mySqlBuilder.addSortString(tokenPredicateBuilder.getColumnValue(), theAscending, myUseAggregate);
	}

	public void addSortOnUri(String theResourceName, String theParamName, boolean theAscending) {
		BaseJoiningPredicateBuilder firstPredicateBuilder = mySqlBuilder.getOrCreateFirstPredicateBuilder();

		UriPredicateBuilder uriPredicateBuilder = mySqlBuilder.createUriPredicateBuilder();
		Condition hashIdentityPredicate =
				uriPredicateBuilder.createHashIdentityPredicate(theResourceName, theParamName);

		addSortCustomJoin(firstPredicateBuilder, uriPredicateBuilder, hashIdentityPredicate);

		mySqlBuilder.addSortString(uriPredicateBuilder.getColumnValue(), theAscending, myUseAggregate);
	}

	private void addSortCustomJoin(
			BaseJoiningPredicateBuilder theFromJoiningPredicateBuilder,
			BaseJoiningPredicateBuilder theToJoiningPredicateBuilder,
			Condition theCondition) {
		addSortCustomJoin(
				theFromJoiningPredicateBuilder.getResourceIdColumn(), theToJoiningPredicateBuilder, theCondition);
	}

	private void addSortCustomJoin(
			DbColumn theFromDbColumn,
			BaseJoiningPredicateBuilder theToJoiningPredicateBuilder,
			Condition theCondition) {
		ComboCondition onCondition =
				mySqlBuilder.createOnCondition(theFromDbColumn, theToJoiningPredicateBuilder.getResourceIdColumn());

		if (theCondition != null) {
			onCondition.addCondition(theCondition);
		}

		mySqlBuilder.addCustomJoin(
				SelectQuery.JoinType.LEFT_OUTER,
				theFromDbColumn.getTable(),
				theToJoiningPredicateBuilder.getTable(),
				onCondition);
	}

	public void setUseAggregate(boolean theUseAggregate) {
		myUseAggregate = theUseAggregate;
	}

	@SuppressWarnings("unchecked")
	private <T extends BaseJoiningPredicateBuilder> PredicateBuilderCacheLookupResult<T> createOrReusePredicateBuilder(
			PredicateBuilderTypeEnum theType,
			DbColumn theSourceJoinColumn,
			String theParamName,
			Supplier<T> theFactoryMethod) {
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

		if (theType == PredicateBuilderTypeEnum.COORDS) {
			if (myParamNameToPredicateBuilderMap == null) {
				myParamNameToPredicateBuilderMap = new HashMap<>();
			}
			myParamNameToPredicateBuilderMap.put(theParamName, retVal);
		}

		return new PredicateBuilderCacheLookupResult<>(cacheHit, (T) retVal);
	}

	private Condition createPredicateComposite(
			@Nullable DbColumn theSourceJoinColumn,
			String theResourceName,
			String theSpnamePrefix,
			RuntimeSearchParam theParamDef,
			List<? extends IQueryParameterType> theNextAnd,
			RequestPartitionId theRequestPartitionId) {
		return createPredicateComposite(
				theSourceJoinColumn,
				theResourceName,
				theSpnamePrefix,
				theParamDef,
				theNextAnd,
				theRequestPartitionId,
				mySqlBuilder);
	}

	private Condition createPredicateComposite(
			@Nullable DbColumn theSourceJoinColumn,
			String theResourceName,
			String theSpnamePrefix,
			RuntimeSearchParam theParamDef,
			List<? extends IQueryParameterType> theNextAnd,
			RequestPartitionId theRequestPartitionId,
			SearchQueryBuilder theSqlBuilder) {

		Condition orCondidtion = null;
		for (IQueryParameterType next : theNextAnd) {

			if (!(next instanceof CompositeParam<?, ?>)) {
				throw new InvalidRequestException(Msg.code(1203) + "Invalid type for composite param (must be "
						+ CompositeParam.class.getSimpleName() + ": " + next.getClass());
			}
			CompositeParam<?, ?> cp = (CompositeParam<?, ?>) next;

			List<RuntimeSearchParam> componentParams =
					JpaParamUtil.resolveComponentParameters(mySearchParamRegistry, theParamDef);
			RuntimeSearchParam left = componentParams.get(0);
			IQueryParameterType leftValue = cp.getLeftValue();
			Condition leftPredicate = createPredicateCompositePart(
					theSourceJoinColumn,
					theResourceName,
					theSpnamePrefix,
					left,
					leftValue,
					theRequestPartitionId,
					theSqlBuilder);

			RuntimeSearchParam right = componentParams.get(1);
			IQueryParameterType rightValue = cp.getRightValue();
			Condition rightPredicate = createPredicateCompositePart(
					theSourceJoinColumn,
					theResourceName,
					theSpnamePrefix,
					right,
					rightValue,
					theRequestPartitionId,
					theSqlBuilder);

			Condition andCondition = toAndPredicate(leftPredicate, rightPredicate);

			if (orCondidtion == null) {
				orCondidtion = toOrPredicate(andCondition);
			} else {
				orCondidtion = toOrPredicate(orCondidtion, andCondition);
			}
		}

		return orCondidtion;
	}

	private Condition createPredicateCompositePart(
			@Nullable DbColumn theSourceJoinColumn,
			String theResourceName,
			String theSpnamePrefix,
			RuntimeSearchParam theParam,
			IQueryParameterType theParamValue,
			RequestPartitionId theRequestPartitionId,
			SearchQueryBuilder theSqlBuilder) {

		switch (theParam.getParamType()) {
			case STRING: {
				return createPredicateString(
						theSourceJoinColumn,
						theResourceName,
						theSpnamePrefix,
						theParam,
						Collections.singletonList(theParamValue),
						null,
						theRequestPartitionId,
						theSqlBuilder);
			}
			case TOKEN: {
				return createPredicateToken(
						theSourceJoinColumn,
						theResourceName,
						theSpnamePrefix,
						theParam,
						Collections.singletonList(theParamValue),
						null,
						theRequestPartitionId,
						theSqlBuilder);
			}
			case DATE: {
				return createPredicateDate(
						theSourceJoinColumn,
						theResourceName,
						theSpnamePrefix,
						theParam,
						Collections.singletonList(theParamValue),
						toOperation(((DateParam) theParamValue).getPrefix()),
						theRequestPartitionId,
						theSqlBuilder);
			}
			case QUANTITY: {
				return createPredicateQuantity(
						theSourceJoinColumn,
						theResourceName,
						theSpnamePrefix,
						theParam,
						Collections.singletonList(theParamValue),
						null,
						theRequestPartitionId,
						theSqlBuilder);
			}
			case NUMBER:
			case REFERENCE:
			case COMPOSITE:
			case URI:
			case HAS:
			case SPECIAL:
			default:
				throw new InvalidRequestException(Msg.code(1204)
						+ "Don't know how to handle composite parameter with type of " + theParam.getParamType());
		}
	}

	private Condition createMissingParameterQuery(MissingParameterQueryParams theParams) {
		if (theParams.getParamType() == RestSearchParameterTypeEnum.COMPOSITE) {
			ourLog.error("Cannot create missing parameter query for a composite parameter.");
			return null;
		} else if (theParams.getParamType() == RestSearchParameterTypeEnum.REFERENCE) {
			if (isEligibleForEmbeddedChainedResourceSearch(
							theParams.getResourceType(), theParams.getParamName(), theParams.getQueryParameterTypes())
					.supportsUplifted()) {
				ourLog.error("Cannot construct missing query parameter search for ContainedResource REFERENCE search.");
				return null;
			}
		}

		// TODO - Change this when we have HFJ_SPIDX_MISSING table
		/**
		 * How we search depends on if the
		 * {@link JpaStorageSettings#getIndexMissingFields()} property
		 * is Enabled or Disabled.
		 *
		 * If it is, we will use the SP_MISSING values set into the various
		 * SP_INDX_X tables and search on those ("old" search).
		 *
		 * If it is not set, however, we will try and construct a query that
		 * looks for missing SearchParameters in the SP_IDX_* tables ("new" search).
		 *
		 * You cannot mix and match, however (SP_MISSING is not in HASH_IDENTITY information).
		 * So setting (or unsetting) the IndexMissingFields
		 * property should always be followed up with a /$reindex call.
		 *
		 * ---
		 *
		 * Current limitations:
		 * Checking if a row exists ("new" search) for a given missing field in an SP_INDX_* table
		 * (ie, :missing=true) is slow when there are many resources in the table. (Defaults to
		 * a table scan, since HASH_IDENTITY isn't part of the index).
		 *
		 * However, the "old" search method was slow for the reverse: when looking for resources
		 * that do not have a missing field (:missing=false) for much the same reason.
		 */
		SearchQueryBuilder sqlBuilder = theParams.getSqlBuilder();
		if (myStorageSettings.getIndexMissingFields() == JpaStorageSettings.IndexEnabledEnum.DISABLED) {
			// new search
			return createMissingPredicateForUnindexedMissingFields(theParams, sqlBuilder);
		} else {
			// old search
			return createMissingPredicateForIndexedMissingFields(theParams, sqlBuilder);
		}
	}

	/**
	 * Old way of searching.
	 * Missing values must be indexed!
	 */
	private Condition createMissingPredicateForIndexedMissingFields(
			MissingParameterQueryParams theParams, SearchQueryBuilder sqlBuilder) {
		PredicateBuilderTypeEnum predicateType = null;
		Supplier<? extends BaseJoiningPredicateBuilder> supplier = null;
		switch (theParams.getParamType()) {
			case STRING:
				predicateType = PredicateBuilderTypeEnum.STRING;
				supplier = () -> sqlBuilder.addStringPredicateBuilder(theParams.getSourceJoinColumn());
				break;
			case NUMBER:
				predicateType = PredicateBuilderTypeEnum.NUMBER;
				supplier = () -> sqlBuilder.addNumberPredicateBuilder(theParams.getSourceJoinColumn());
				break;
			case DATE:
				predicateType = PredicateBuilderTypeEnum.DATE;
				supplier = () -> sqlBuilder.addDatePredicateBuilder(theParams.getSourceJoinColumn());
				break;
			case TOKEN:
				predicateType = PredicateBuilderTypeEnum.TOKEN;
				supplier = () -> sqlBuilder.addTokenPredicateBuilder(theParams.getSourceJoinColumn());
				break;
			case QUANTITY:
				predicateType = PredicateBuilderTypeEnum.QUANTITY;
				supplier = () -> sqlBuilder.addQuantityPredicateBuilder(theParams.getSourceJoinColumn());
				break;
			case REFERENCE:
			case URI:
				// we expect these values, but the pattern is slightly different;
				// see below
				break;
			case HAS:
			case SPECIAL:
				predicateType = PredicateBuilderTypeEnum.COORDS;
				supplier = () -> sqlBuilder.addCoordsPredicateBuilder(theParams.getSourceJoinColumn());
				break;
			case COMPOSITE:
			default:
				break;
		}

		if (supplier != null) {
			BaseSearchParamPredicateBuilder join = (BaseSearchParamPredicateBuilder) createOrReusePredicateBuilder(
							predicateType, theParams.getSourceJoinColumn(), theParams.getParamName(), supplier)
					.getResult();

			return join.createPredicateParamMissingForNonReference(
					theParams.getResourceType(),
					theParams.getParamName(),
					theParams.isMissing(),
					theParams.getRequestPartitionId());
		} else {
			if (theParams.getParamType() == RestSearchParameterTypeEnum.REFERENCE) {
				SearchParamPresentPredicateBuilder join =
						sqlBuilder.addSearchParamPresentPredicateBuilder(theParams.getSourceJoinColumn());
				return join.createPredicateParamMissingForReference(
						theParams.getResourceType(),
						theParams.getParamName(),
						theParams.isMissing(),
						theParams.getRequestPartitionId());
			} else if (theParams.getParamType() == RestSearchParameterTypeEnum.URI) {
				UriPredicateBuilder join = sqlBuilder.addUriPredicateBuilder(theParams.getSourceJoinColumn());
				return join.createPredicateParamMissingForNonReference(
						theParams.getResourceType(),
						theParams.getParamName(),
						theParams.isMissing(),
						theParams.getRequestPartitionId());
			} else {
				// we don't expect to see this
				ourLog.error("Invalid param type " + theParams.getParamType().name());
				return null;
			}
		}
	}

	/**
	 * New way of searching for missing fields.
	 * Missing values must not indexed!
	 */
	private Condition createMissingPredicateForUnindexedMissingFields(
			MissingParameterQueryParams theParams, SearchQueryBuilder sqlBuilder) {
		ResourceTablePredicateBuilder table = sqlBuilder.getOrCreateResourceTablePredicateBuilder();

		ICanMakeMissingParamPredicate innerQuery = PredicateBuilderFactory.createPredicateBuilderForParamType(
				theParams.getParamType(), theParams.getSqlBuilder(), this);

		return innerQuery.createPredicateParamMissingValue(new MissingQueryParameterPredicateParams(
				table, theParams.isMissing(), theParams.getParamName(), theParams.getRequestPartitionId()));
	}

	public Condition createPredicateCoords(
			@Nullable DbColumn theSourceJoinColumn,
			String theResourceName,
			String theSpnamePrefix,
			RuntimeSearchParam theSearchParam,
			List<? extends IQueryParameterType> theList,
			RequestPartitionId theRequestPartitionId,
			SearchQueryBuilder theSqlBuilder) {
		Boolean isMissing = theList.get(0).getMissing();
		if (isMissing != null) {
			String paramName = getParamNameWithPrefix(theSpnamePrefix, theSearchParam.getName());

			return createMissingParameterQuery(new MissingParameterQueryParams(
					theSqlBuilder,
					theSearchParam.getParamType(),
					theList,
					paramName,
					theResourceName,
					theSourceJoinColumn,
					theRequestPartitionId));
		} else {
			CoordsPredicateBuilder predicateBuilder = createOrReusePredicateBuilder(
							PredicateBuilderTypeEnum.COORDS,
							theSourceJoinColumn,
							theSearchParam.getName(),
							() -> mySqlBuilder.addCoordsPredicateBuilder(theSourceJoinColumn))
					.getResult();

			List<Condition> codePredicates = new ArrayList<>();
			for (IQueryParameterType nextOr : theList) {
				Condition singleCode = predicateBuilder.createPredicateCoords(
						mySearchParameters,
						nextOr,
						theResourceName,
						theSearchParam,
						predicateBuilder,
						theRequestPartitionId);
				codePredicates.add(singleCode);
			}

			return predicateBuilder.combineWithRequestPartitionIdPredicate(
					theRequestPartitionId, ComboCondition.or(codePredicates.toArray(new Condition[0])));
		}
	}

	public Condition createPredicateDate(
			@Nullable DbColumn theSourceJoinColumn,
			String theResourceName,
			String theSpnamePrefix,
			RuntimeSearchParam theSearchParam,
			List<? extends IQueryParameterType> theList,
			SearchFilterParser.CompareOperation theOperation,
			RequestPartitionId theRequestPartitionId) {
		return createPredicateDate(
				theSourceJoinColumn,
				theResourceName,
				theSpnamePrefix,
				theSearchParam,
				theList,
				theOperation,
				theRequestPartitionId,
				mySqlBuilder);
	}

	public Condition createPredicateDate(
			@Nullable DbColumn theSourceJoinColumn,
			String theResourceName,
			String theSpnamePrefix,
			RuntimeSearchParam theSearchParam,
			List<? extends IQueryParameterType> theList,
			SearchFilterParser.CompareOperation theOperation,
			RequestPartitionId theRequestPartitionId,
			SearchQueryBuilder theSqlBuilder) {
		String paramName = getParamNameWithPrefix(theSpnamePrefix, theSearchParam.getName());

		Boolean isMissing = theList.get(0).getMissing();
		if (isMissing != null) {
			return createMissingParameterQuery(new MissingParameterQueryParams(
					theSqlBuilder,
					theSearchParam.getParamType(),
					theList,
					paramName,
					theResourceName,
					theSourceJoinColumn,
					theRequestPartitionId));
		} else {
			PredicateBuilderCacheLookupResult<DatePredicateBuilder> predicateBuilderLookupResult =
					createOrReusePredicateBuilder(
							PredicateBuilderTypeEnum.DATE,
							theSourceJoinColumn,
							paramName,
							() -> theSqlBuilder.addDatePredicateBuilder(theSourceJoinColumn));
			DatePredicateBuilder predicateBuilder = predicateBuilderLookupResult.getResult();
			boolean cacheHit = predicateBuilderLookupResult.isCacheHit();

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
	}

	private Condition createPredicateFilter(
			QueryStack theQueryStack3,
			SearchFilterParser.BaseFilter theFilter,
			String theResourceName,
			RequestDetails theRequest,
			RequestPartitionId theRequestPartitionId) {

		if (theFilter instanceof SearchFilterParser.FilterParameter) {
			return createPredicateFilter(
					theQueryStack3,
					(SearchFilterParser.FilterParameter) theFilter,
					theResourceName,
					theRequest,
					theRequestPartitionId);
		} else if (theFilter instanceof SearchFilterParser.FilterLogical) {
			// Left side
			Condition xPredicate = createPredicateFilter(
					theQueryStack3,
					((SearchFilterParser.FilterLogical) theFilter).getFilter1(),
					theResourceName,
					theRequest,
					theRequestPartitionId);

			// Right side
			Condition yPredicate = createPredicateFilter(
					theQueryStack3,
					((SearchFilterParser.FilterLogical) theFilter).getFilter2(),
					theResourceName,
					theRequest,
					theRequestPartitionId);

			if (((SearchFilterParser.FilterLogical) theFilter).getOperation()
					== SearchFilterParser.FilterLogicalOperation.and) {
				return ComboCondition.and(xPredicate, yPredicate);
			} else if (((SearchFilterParser.FilterLogical) theFilter).getOperation()
					== SearchFilterParser.FilterLogicalOperation.or) {
				return ComboCondition.or(xPredicate, yPredicate);
			} else {
				// Shouldn't happen
				throw new InvalidRequestException(Msg.code(1205) + "Don't know how to handle operation "
						+ ((SearchFilterParser.FilterLogical) theFilter).getOperation());
			}
		} else {
			return createPredicateFilter(
					theQueryStack3,
					((SearchFilterParser.FilterParameterGroup) theFilter).getContained(),
					theResourceName,
					theRequest,
					theRequestPartitionId);
		}
	}

	private Condition createPredicateFilter(
			QueryStack theQueryStack3,
			SearchFilterParser.FilterParameter theFilter,
			String theResourceName,
			RequestDetails theRequest,
			RequestPartitionId theRequestPartitionId) {

		String paramName = theFilter.getParamPath().getName();

		switch (paramName) {
			case IAnyResource.SP_RES_ID: {
				TokenParam param = new TokenParam();
				param.setValueAsQueryToken(null, null, null, theFilter.getValue());
				return theQueryStack3.createPredicateResourceId(
						null,
						Collections.singletonList(Collections.singletonList(param)),
						theResourceName,
						theFilter.getOperation(),
						theRequestPartitionId);
			}
			case Constants.PARAM_SOURCE: {
				TokenParam param = new TokenParam();
				param.setValueAsQueryToken(null, null, null, theFilter.getValue());
				return createPredicateSource(null, Collections.singletonList(param));
			}
			default:
				RuntimeSearchParam searchParam = mySearchParamRegistry.getActiveSearchParam(theResourceName, paramName);
				if (searchParam == null) {
					Collection<String> validNames =
							mySearchParamRegistry.getValidSearchParameterNamesIncludingMeta(theResourceName);
					String msg = myFhirContext
							.getLocalizer()
							.getMessageSanitized(
									BaseStorageDao.class,
									"invalidSearchParameter",
									paramName,
									theResourceName,
									validNames);
					throw new InvalidRequestException(Msg.code(1206) + msg);
				}
				RestSearchParameterTypeEnum typeEnum = searchParam.getParamType();
				if (typeEnum == RestSearchParameterTypeEnum.URI) {
					return theQueryStack3.createPredicateUri(
							null,
							theResourceName,
							null,
							searchParam,
							Collections.singletonList(new UriParam(theFilter.getValue())),
							theFilter.getOperation(),
							theRequest,
							theRequestPartitionId);
				} else if (typeEnum == RestSearchParameterTypeEnum.STRING) {
					return theQueryStack3.createPredicateString(
							null,
							theResourceName,
							null,
							searchParam,
							Collections.singletonList(new StringParam(theFilter.getValue())),
							theFilter.getOperation(),
							theRequestPartitionId);
				} else if (typeEnum == RestSearchParameterTypeEnum.DATE) {
					return theQueryStack3.createPredicateDate(
							null,
							theResourceName,
							null,
							searchParam,
							Collections.singletonList(
									new DateParam(fromOperation(theFilter.getOperation()), theFilter.getValue())),
							theFilter.getOperation(),
							theRequestPartitionId);
				} else if (typeEnum == RestSearchParameterTypeEnum.NUMBER) {
					return theQueryStack3.createPredicateNumber(
							null,
							theResourceName,
							null,
							searchParam,
							Collections.singletonList(new NumberParam(theFilter.getValue())),
							theFilter.getOperation(),
							theRequestPartitionId);
				} else if (typeEnum == RestSearchParameterTypeEnum.REFERENCE) {
					SearchFilterParser.CompareOperation operation = theFilter.getOperation();
					String resourceType =
							null; // The value can either have (Patient/123) or not have (123) a resource type, either
					// way it's not needed here
					String chain = (theFilter.getParamPath().getNext() != null)
							? theFilter.getParamPath().getNext().toString()
							: null;
					String value = theFilter.getValue();
					ReferenceParam referenceParam = new ReferenceParam(resourceType, chain, value);
					return theQueryStack3.createPredicateReference(
							null,
							theResourceName,
							paramName,
							new ArrayList<>(),
							Collections.singletonList(referenceParam),
							operation,
							theRequest,
							theRequestPartitionId);
				} else if (typeEnum == RestSearchParameterTypeEnum.QUANTITY) {
					return theQueryStack3.createPredicateQuantity(
							null,
							theResourceName,
							null,
							searchParam,
							Collections.singletonList(new QuantityParam(theFilter.getValue())),
							theFilter.getOperation(),
							theRequestPartitionId);
				} else if (typeEnum == RestSearchParameterTypeEnum.COMPOSITE) {
					throw new InvalidRequestException(Msg.code(1207)
							+ "Composite search parameters not currently supported with _filter clauses");
				} else if (typeEnum == RestSearchParameterTypeEnum.TOKEN) {
					TokenParam param = new TokenParam();
					param.setValueAsQueryToken(null, null, null, theFilter.getValue());
					return theQueryStack3.createPredicateToken(
							null,
							theResourceName,
							null,
							searchParam,
							Collections.singletonList(param),
							theFilter.getOperation(),
							theRequestPartitionId);
				}
				break;
		}
		return null;
	}

	private Condition createPredicateHas(
			@Nullable DbColumn theSourceJoinColumn,
			String theResourceType,
			List<List<IQueryParameterType>> theHasParameters,
			RequestDetails theRequest,
			RequestPartitionId theRequestPartitionId) {

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
				paramName = PATTERN_DOT_AND_ALL_AFTER.matcher(parameterName).replaceAll("");
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

				ourLog.trace("Handling double _has query: {}", paramName);

				String qualifier = paramName.substring(4);
				for (IQueryParameterType next : nextOrList) {
					HasParam nextHasParam = new HasParam();
					nextHasParam.setValueAsQueryToken(
							myFhirContext, PARAM_HAS, qualifier, next.getValueAsQueryToken(myFhirContext));
					orValues.add(nextHasParam);
				}

			} else if (paramName.equals(PARAM_ID)) {

				for (IQueryParameterType next : nextOrList) {
					orValues.add(new TokenParam(next.getValueAsQueryToken(myFhirContext)));
				}

			} else {

				// Ensure that the name of the search param
				// (e.g. the `code` in Patient?_has:Observation:subject:code=sys|val)
				// exists on the target resource type.
				RuntimeSearchParam owningParameterDef =
						mySearchParamRegistry.getRuntimeSearchParam(targetResourceType, paramName);

				// Ensure that the name of the back-referenced search param on the target (e.g. the `subject` in
				// Patient?_has:Observation:subject:code=sys|val)
				// exists on the target resource, or in the top-level Resource resource.
				mySearchParamRegistry.getRuntimeSearchParam(targetResourceType, paramReference);

				IQueryParameterAnd<?> parsedParam = JpaParamUtil.parseQueryParams(
						mySearchParamRegistry, myFhirContext, owningParameterDef, paramName, parameters);

				for (IQueryParameterOr<?> next : parsedParam.getValuesAsQueryTokens()) {
					orValues.addAll(next.getValuesAsQueryTokens());
				}
			}

			// Handle internal chain inside the has.
			if (parameterName.contains(".")) {
				// Previously, for some unknown reason, we were calling getChainedPart() twice.  This broke the _has
				// then chain, then _has use case by effectively cutting off the second part of the chain and
				// missing one iteration of the recursive call to build the query.
				// So, for example, for
				// Practitioner?_has:ExplanationOfBenefit:care-team:coverage.payor._has:List:item:_id=list1
				// instead of passing " payor._has:List:item:_id=list1" to the next recursion, the second call to
				// getChainedPart() was wrongly removing "payor." and passing down "_has:List:item:_id=list1" instead.
				// This resulted in running incorrect SQL with nonsensical join that resulted in 0 results.
				// However, after running the pipeline,  I've concluded there's no use case at all for the
				// double call to "getChainedPart()", which is why there's no conditional logic at all to make a double
				// call to getChainedPart().
				final String chainedPart = getChainedPart(parameterName);

				orValues.stream()
						.filter(qp -> qp instanceof ReferenceParam)
						.map(qp -> (ReferenceParam) qp)
						.forEach(rp -> rp.setChain(chainedPart));

				parameterName = parameterName.substring(0, parameterName.indexOf('.'));
			}

			int colonIndex = parameterName.indexOf(':');
			if (colonIndex != -1) {
				parameterName = parameterName.substring(0, colonIndex);
			}

			ResourceLinkPredicateBuilder resourceLinkTableJoin =
					mySqlBuilder.addReferencePredicateBuilderReversed(this, theSourceJoinColumn);
			Condition partitionPredicate = resourceLinkTableJoin.createPartitionIdPredicate(theRequestPartitionId);

			List<String> paths = resourceLinkTableJoin.createResourceLinkPaths(
					targetResourceType, paramReference, new ArrayList<>());
			if (CollectionUtils.isEmpty(paths)) {
				throw new InvalidRequestException(Msg.code(2305) + "Reference field does not exist: " + paramReference);
			}

			Condition typePredicate = BinaryCondition.equalTo(
					resourceLinkTableJoin.getColumnTargetResourceType(),
					mySqlBuilder.generatePlaceholder(theResourceType));
			Condition pathPredicate = toEqualToOrInPredicate(
					resourceLinkTableJoin.getColumnSourcePath(), mySqlBuilder.generatePlaceholders(paths));

			Condition linkedPredicate =
					searchForIdsWithAndOr(with().setSourceJoinColumn(resourceLinkTableJoin.getColumnSrcResourceId())
							.setResourceName(targetResourceType)
							.setParamName(parameterName)
							.setAndOrParams(Collections.singletonList(orValues))
							.setRequest(theRequest)
							.setRequestPartitionId(theRequestPartitionId));

			andPredicates.add(toAndPredicate(partitionPredicate, pathPredicate, typePredicate, linkedPredicate));
		}

		return toAndPredicate(andPredicates);
	}

	public Condition createPredicateNumber(
			@Nullable DbColumn theSourceJoinColumn,
			String theResourceName,
			String theSpnamePrefix,
			RuntimeSearchParam theSearchParam,
			List<? extends IQueryParameterType> theList,
			SearchFilterParser.CompareOperation theOperation,
			RequestPartitionId theRequestPartitionId) {
		return createPredicateNumber(
				theSourceJoinColumn,
				theResourceName,
				theSpnamePrefix,
				theSearchParam,
				theList,
				theOperation,
				theRequestPartitionId,
				mySqlBuilder);
	}

	public Condition createPredicateNumber(
			@Nullable DbColumn theSourceJoinColumn,
			String theResourceName,
			String theSpnamePrefix,
			RuntimeSearchParam theSearchParam,
			List<? extends IQueryParameterType> theList,
			SearchFilterParser.CompareOperation theOperation,
			RequestPartitionId theRequestPartitionId,
			SearchQueryBuilder theSqlBuilder) {

		String paramName = getParamNameWithPrefix(theSpnamePrefix, theSearchParam.getName());

		Boolean isMissing = theList.get(0).getMissing();
		if (isMissing != null) {
			return createMissingParameterQuery(new MissingParameterQueryParams(
					theSqlBuilder,
					theSearchParam.getParamType(),
					theList,
					paramName,
					theResourceName,
					theSourceJoinColumn,
					theRequestPartitionId));
		} else {
			NumberPredicateBuilder join = createOrReusePredicateBuilder(
							PredicateBuilderTypeEnum.NUMBER,
							theSourceJoinColumn,
							paramName,
							() -> theSqlBuilder.addNumberPredicateBuilder(theSourceJoinColumn))
					.getResult();

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

					Condition predicate = join.createPredicateNumeric(
							theResourceName, paramName, operation, value, theRequestPartitionId, nextOr);
					codePredicates.add(predicate);

				} else {
					throw new IllegalArgumentException(Msg.code(1211) + "Invalid token type: " + nextOr.getClass());
				}
			}

			return join.combineWithRequestPartitionIdPredicate(
					theRequestPartitionId, ComboCondition.or(codePredicates.toArray(new Condition[0])));
		}
	}

	public Condition createPredicateQuantity(
			@Nullable DbColumn theSourceJoinColumn,
			String theResourceName,
			String theSpnamePrefix,
			RuntimeSearchParam theSearchParam,
			List<? extends IQueryParameterType> theList,
			SearchFilterParser.CompareOperation theOperation,
			RequestPartitionId theRequestPartitionId) {
		return createPredicateQuantity(
				theSourceJoinColumn,
				theResourceName,
				theSpnamePrefix,
				theSearchParam,
				theList,
				theOperation,
				theRequestPartitionId,
				mySqlBuilder);
	}

	public Condition createPredicateQuantity(
			@Nullable DbColumn theSourceJoinColumn,
			String theResourceName,
			String theSpnamePrefix,
			RuntimeSearchParam theSearchParam,
			List<? extends IQueryParameterType> theList,
			SearchFilterParser.CompareOperation theOperation,
			RequestPartitionId theRequestPartitionId,
			SearchQueryBuilder theSqlBuilder) {

		String paramName = getParamNameWithPrefix(theSpnamePrefix, theSearchParam.getName());

		Boolean isMissing = theList.get(0).getMissing();
		if (isMissing != null) {
			return createMissingParameterQuery(new MissingParameterQueryParams(
					theSqlBuilder,
					theSearchParam.getParamType(),
					theList,
					paramName,
					theResourceName,
					theSourceJoinColumn,
					theRequestPartitionId));
		} else {
			List<QuantityParam> quantityParams =
					theList.stream().map(t -> QuantityParam.toQuantityParam(t)).collect(Collectors.toList());

			BaseQuantityPredicateBuilder join = null;
			boolean normalizedSearchEnabled = myStorageSettings
					.getNormalizedQuantitySearchLevel()
					.equals(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_SEARCH_SUPPORTED);
			if (normalizedSearchEnabled) {
				List<QuantityParam> normalizedQuantityParams = quantityParams.stream()
						.map(t -> UcumServiceUtil.toCanonicalQuantityOrNull(t))
						.filter(t -> t != null)
						.collect(Collectors.toList());

				if (normalizedQuantityParams.size() == quantityParams.size()) {
					join = createOrReusePredicateBuilder(
									PredicateBuilderTypeEnum.QUANTITY,
									theSourceJoinColumn,
									paramName,
									() -> theSqlBuilder.addQuantityNormalizedPredicateBuilder(theSourceJoinColumn))
							.getResult();
					quantityParams = normalizedQuantityParams;
				}
			}

			if (join == null) {
				join = createOrReusePredicateBuilder(
								PredicateBuilderTypeEnum.QUANTITY,
								theSourceJoinColumn,
								paramName,
								() -> theSqlBuilder.addQuantityPredicateBuilder(theSourceJoinColumn))
						.getResult();
			}

			List<Condition> codePredicates = new ArrayList<>();
			for (QuantityParam nextOr : quantityParams) {
				Condition singleCode = join.createPredicateQuantity(
						nextOr, theResourceName, paramName, null, join, theOperation, theRequestPartitionId);
				codePredicates.add(singleCode);
			}

			return join.combineWithRequestPartitionIdPredicate(
					theRequestPartitionId, ComboCondition.or(codePredicates.toArray(new Condition[0])));
		}
	}

	public Condition createPredicateReference(
			@Nullable DbColumn theSourceJoinColumn,
			String theResourceName,
			String theParamName,
			List<String> theQualifiers,
			List<? extends IQueryParameterType> theList,
			SearchFilterParser.CompareOperation theOperation,
			RequestDetails theRequest,
			RequestPartitionId theRequestPartitionId) {
		return createPredicateReference(
				theSourceJoinColumn,
				theResourceName,
				theParamName,
				theQualifiers,
				theList,
				theOperation,
				theRequest,
				theRequestPartitionId,
				mySqlBuilder);
	}

	public Condition createPredicateReference(
			@Nullable DbColumn theSourceJoinColumn,
			String theResourceName,
			String theParamName,
			List<String> theQualifiers,
			List<? extends IQueryParameterType> theList,
			SearchFilterParser.CompareOperation theOperation,
			RequestDetails theRequest,
			RequestPartitionId theRequestPartitionId,
			SearchQueryBuilder theSqlBuilder) {

		if ((theOperation != null)
				&& (theOperation != SearchFilterParser.CompareOperation.eq)
				&& (theOperation != SearchFilterParser.CompareOperation.ne)) {
			throw new InvalidRequestException(
					Msg.code(1212)
							+ "Invalid operator specified for reference predicate.  Supported operators for reference predicate are \"eq\" and \"ne\".");
		}

		Boolean isMissing = theList.get(0).getMissing();
		if (isMissing != null) {
			return createMissingParameterQuery(new MissingParameterQueryParams(
					theSqlBuilder,
					RestSearchParameterTypeEnum.REFERENCE,
					theList,
					theParamName,
					theResourceName,
					theSourceJoinColumn,
					theRequestPartitionId));
		} else {
			ResourceLinkPredicateBuilder predicateBuilder = createOrReusePredicateBuilder(
							PredicateBuilderTypeEnum.REFERENCE,
							theSourceJoinColumn,
							theParamName,
							() -> theSqlBuilder.addReferencePredicateBuilder(this, theSourceJoinColumn))
					.getResult();
			return predicateBuilder.createPredicate(
					theRequest,
					theResourceName,
					theParamName,
					theQualifiers,
					theList,
					theOperation,
					theRequestPartitionId);
		}
	}

	public void addGrouping() {
		BaseJoiningPredicateBuilder firstPredicateBuilder = mySqlBuilder.getOrCreateFirstPredicateBuilder();
		mySqlBuilder.getSelect().addGroupings(firstPredicateBuilder.getResourceIdColumn());
	}

	public void addOrdering() {
		BaseJoiningPredicateBuilder firstPredicateBuilder = mySqlBuilder.getOrCreateFirstPredicateBuilder();
		mySqlBuilder.getSelect().addOrderings(firstPredicateBuilder.getResourceIdColumn());
	}

	public Condition createPredicateReferenceForEmbeddedChainedSearchResource(
			@Nullable DbColumn theSourceJoinColumn,
			String theResourceName,
			RuntimeSearchParam theSearchParam,
			List<? extends IQueryParameterType> theList,
			SearchFilterParser.CompareOperation theOperation,
			RequestDetails theRequest,
			RequestPartitionId theRequestPartitionId,
			EmbeddedChainedSearchModeEnum theEmbeddedChainedSearchModeEnum) {

		boolean wantChainedAndNormal =
				theEmbeddedChainedSearchModeEnum == EmbeddedChainedSearchModeEnum.UPLIFTED_AND_REF_JOIN;

		// A bit of a hack, but we need to turn off cache reuse while in this method so that we don't try to reuse
		// builders across different subselects
		EnumSet<PredicateBuilderTypeEnum> cachedReusePredicateBuilderTypes =
				EnumSet.copyOf(myReusePredicateBuilderTypes);
		if (wantChainedAndNormal) {
			myReusePredicateBuilderTypes.clear();
		}

		ReferenceChainExtractor chainExtractor = new ReferenceChainExtractor();
		chainExtractor.deriveChains(theResourceName, theSearchParam, theList);
		Map<List<ChainElement>, Set<LeafNodeDefinition>> chains = chainExtractor.getChains();

		Map<List<String>, Set<LeafNodeDefinition>> referenceLinks = Maps.newHashMap();
		for (List<ChainElement> nextChain : chains.keySet()) {
			Set<LeafNodeDefinition> leafNodes = chains.get(nextChain);

			collateChainedSearchOptions(referenceLinks, nextChain, leafNodes, theEmbeddedChainedSearchModeEnum);
		}

		UnionQuery union = null;
		List<Condition> predicates = null;
		if (wantChainedAndNormal) {
			union = new UnionQuery(SetOperationQuery.Type.UNION_ALL);
		} else {
			predicates = new ArrayList<>();
		}

		predicates = new ArrayList<>();
		for (List<String> nextReferenceLink : referenceLinks.keySet()) {
			for (LeafNodeDefinition leafNodeDefinition : referenceLinks.get(nextReferenceLink)) {
				SearchQueryBuilder builder;
				if (wantChainedAndNormal) {
					builder = mySqlBuilder.newChildSqlBuilder();
				} else {
					builder = mySqlBuilder;
				}

				DbColumn previousJoinColumn = null;

				// Create a reference link predicates to the subselect for every link but the last one
				for (String nextLink : nextReferenceLink) {
					// We don't want to call createPredicateReference() here, because the whole point is to avoid the
					// recursion.
					// TODO: Are we missing any important business logic from that method? All tests are passing.
					ResourceLinkPredicateBuilder resourceLinkPredicateBuilder =
							builder.addReferencePredicateBuilder(this, previousJoinColumn);
					builder.addPredicate(
							resourceLinkPredicateBuilder.createPredicateSourcePaths(Lists.newArrayList(nextLink)));
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

				if (wantChainedAndNormal) {
					builder.addPredicate(containedCondition);
					union.addQueries(builder.getSelect());
				} else {
					predicates.add(containedCondition);
				}
			}
		}

		Condition retVal;
		if (wantChainedAndNormal) {

			if (theSourceJoinColumn == null) {
				retVal = new InCondition(
						mySqlBuilder.getOrCreateFirstPredicateBuilder(false).getResourceIdColumn(), union);
			} else {
				// -- for the resource link, need join with target_resource_id
				retVal = new InCondition(theSourceJoinColumn, union);
			}

		} else {

			retVal = toOrPredicate(predicates);
		}

		// restore the state of this collection to turn caching back on before we exit
		myReusePredicateBuilderTypes.addAll(cachedReusePredicateBuilderTypes);
		return retVal;
	}

	private void collateChainedSearchOptions(
			Map<List<String>, Set<LeafNodeDefinition>> referenceLinks,
			List<ChainElement> nextChain,
			Set<LeafNodeDefinition> leafNodes,
			EmbeddedChainedSearchModeEnum theEmbeddedChainedSearchModeEnum) {
		// Manually collapse the chain using all possible variants of contained resource patterns.
		// This is a bit excruciating to extend beyond three references. Do we want to find a way to automate this
		// someday?
		// Note: the first element in each chain is assumed to be discrete. This may need to change when we add proper
		// support for `_contained`
		if (nextChain.size() == 1) {
			// discrete -> discrete
			if (theEmbeddedChainedSearchModeEnum == EmbeddedChainedSearchModeEnum.UPLIFTED_AND_REF_JOIN) {
				// If !theWantChainedAndNormal that means we're only processing refchains
				// so the discrete -> contained case is the only one that applies
				updateMapOfReferenceLinks(
						referenceLinks, Lists.newArrayList(nextChain.get(0).getPath()), leafNodes);
			}

			// discrete -> contained
			RuntimeSearchParam firstParamDefinition =
					leafNodes.iterator().next().getParamDefinition();
			updateMapOfReferenceLinks(
					referenceLinks,
					Lists.newArrayList(),
					leafNodes.stream()
							.map(t -> t.withPathPrefix(
									nextChain.get(0).getResourceType(),
									nextChain.get(0).getSearchParameterName()))
							// When we're handling discrete->contained the differences between search
							// parameters don't matter. E.g. if we're processing "subject.name=foo"
							// the name could be Patient:name or Group:name but it doesn't actually
							// matter that these are different since in this case both of these end
							// up being an identical search in the string table for "subject.name".
							.map(t -> t.withParam(firstParamDefinition))
							.collect(Collectors.toSet()));
		} else if (nextChain.size() == 2) {
			// discrete -> discrete -> discrete
			updateMapOfReferenceLinks(
					referenceLinks,
					Lists.newArrayList(
							nextChain.get(0).getPath(), nextChain.get(1).getPath()),
					leafNodes);
			// discrete -> discrete -> contained
			updateMapOfReferenceLinks(
					referenceLinks,
					Lists.newArrayList(nextChain.get(0).getPath()),
					leafNodes.stream()
							.map(t -> t.withPathPrefix(
									nextChain.get(1).getResourceType(),
									nextChain.get(1).getSearchParameterName()))
							.collect(Collectors.toSet()));
			// discrete -> contained -> discrete
			updateMapOfReferenceLinks(
					referenceLinks,
					Lists.newArrayList(mergePaths(
							nextChain.get(0).getPath(), nextChain.get(1).getPath())),
					leafNodes);
			if (myStorageSettings.isIndexOnContainedResourcesRecursively()) {
				// discrete -> contained -> contained
				updateMapOfReferenceLinks(
						referenceLinks,
						Lists.newArrayList(),
						leafNodes.stream()
								.map(t -> t.withPathPrefix(
										nextChain.get(0).getResourceType(),
										nextChain.get(0).getSearchParameterName() + "."
												+ nextChain.get(1).getSearchParameterName()))
								.collect(Collectors.toSet()));
			}
		} else if (nextChain.size() == 3) {
			// discrete -> discrete -> discrete -> discrete
			updateMapOfReferenceLinks(
					referenceLinks,
					Lists.newArrayList(
							nextChain.get(0).getPath(),
							nextChain.get(1).getPath(),
							nextChain.get(2).getPath()),
					leafNodes);
			// discrete -> discrete -> discrete -> contained
			updateMapOfReferenceLinks(
					referenceLinks,
					Lists.newArrayList(
							nextChain.get(0).getPath(), nextChain.get(1).getPath()),
					leafNodes.stream()
							.map(t -> t.withPathPrefix(
									nextChain.get(2).getResourceType(),
									nextChain.get(2).getSearchParameterName()))
							.collect(Collectors.toSet()));
			// discrete -> discrete -> contained -> discrete
			updateMapOfReferenceLinks(
					referenceLinks,
					Lists.newArrayList(
							nextChain.get(0).getPath(),
							mergePaths(
									nextChain.get(1).getPath(), nextChain.get(2).getPath())),
					leafNodes);
			// discrete -> contained -> discrete -> discrete
			updateMapOfReferenceLinks(
					referenceLinks,
					Lists.newArrayList(
							mergePaths(
									nextChain.get(0).getPath(), nextChain.get(1).getPath()),
							nextChain.get(2).getPath()),
					leafNodes);
			// discrete -> contained -> discrete -> contained
			updateMapOfReferenceLinks(
					referenceLinks,
					Lists.newArrayList(mergePaths(
							nextChain.get(0).getPath(), nextChain.get(1).getPath())),
					leafNodes.stream()
							.map(t -> t.withPathPrefix(
									nextChain.get(2).getResourceType(),
									nextChain.get(2).getSearchParameterName()))
							.collect(Collectors.toSet()));
			if (myStorageSettings.isIndexOnContainedResourcesRecursively()) {
				// discrete -> contained -> contained -> discrete
				updateMapOfReferenceLinks(
						referenceLinks,
						Lists.newArrayList(mergePaths(
								nextChain.get(0).getPath(),
								nextChain.get(1).getPath(),
								nextChain.get(2).getPath())),
						leafNodes);
				// discrete -> discrete -> contained -> contained
				updateMapOfReferenceLinks(
						referenceLinks,
						Lists.newArrayList(nextChain.get(0).getPath()),
						leafNodes.stream()
								.map(t -> t.withPathPrefix(
										nextChain.get(1).getResourceType(),
										nextChain.get(1).getSearchParameterName() + "."
												+ nextChain.get(2).getSearchParameterName()))
								.collect(Collectors.toSet()));
				// discrete -> contained -> contained -> contained
				updateMapOfReferenceLinks(
						referenceLinks,
						Lists.newArrayList(),
						leafNodes.stream()
								.map(t -> t.withPathPrefix(
										nextChain.get(0).getResourceType(),
										nextChain.get(0).getSearchParameterName() + "."
												+ nextChain.get(1).getSearchParameterName() + "."
												+ nextChain.get(2).getSearchParameterName()))
								.collect(Collectors.toSet()));
			}
		} else {
			// TODO: the chain is too long, it isn't practical to hard-code all the possible patterns. If anyone ever
			// needs this, we should revisit the approach
			throw new InvalidRequestException(Msg.code(2011)
					+ "The search chain is too long. Only chains of up to three references are supported.");
		}
	}

	private void updateMapOfReferenceLinks(
			Map<List<String>, Set<LeafNodeDefinition>> theReferenceLinksMap,
			ArrayList<String> thePath,
			Set<LeafNodeDefinition> theLeafNodesToAdd) {
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

	private Condition createIndexPredicate(
			DbColumn theSourceJoinColumn,
			String theResourceName,
			String theSpnamePrefix,
			String theParamName,
			RuntimeSearchParam theParamDefinition,
			ArrayList<IQueryParameterType> theOrValues,
			SearchFilterParser.CompareOperation theOperation,
			List<String> theQualifiers,
			RequestDetails theRequest,
			RequestPartitionId theRequestPartitionId,
			SearchQueryBuilder theSqlBuilder) {
		Condition containedCondition;

		switch (theParamDefinition.getParamType()) {
			case DATE:
				containedCondition = createPredicateDate(
						theSourceJoinColumn,
						theResourceName,
						theSpnamePrefix,
						theParamDefinition,
						theOrValues,
						theOperation,
						theRequestPartitionId,
						theSqlBuilder);
				break;
			case NUMBER:
				containedCondition = createPredicateNumber(
						theSourceJoinColumn,
						theResourceName,
						theSpnamePrefix,
						theParamDefinition,
						theOrValues,
						theOperation,
						theRequestPartitionId,
						theSqlBuilder);
				break;
			case QUANTITY:
				containedCondition = createPredicateQuantity(
						theSourceJoinColumn,
						theResourceName,
						theSpnamePrefix,
						theParamDefinition,
						theOrValues,
						theOperation,
						theRequestPartitionId,
						theSqlBuilder);
				break;
			case STRING:
				containedCondition = createPredicateString(
						theSourceJoinColumn,
						theResourceName,
						theSpnamePrefix,
						theParamDefinition,
						theOrValues,
						theOperation,
						theRequestPartitionId,
						theSqlBuilder);
				break;
			case TOKEN:
				containedCondition = createPredicateToken(
						theSourceJoinColumn,
						theResourceName,
						theSpnamePrefix,
						theParamDefinition,
						theOrValues,
						theOperation,
						theRequestPartitionId,
						theSqlBuilder);
				break;
			case COMPOSITE:
				containedCondition = createPredicateComposite(
						theSourceJoinColumn,
						theResourceName,
						theSpnamePrefix,
						theParamDefinition,
						theOrValues,
						theRequestPartitionId,
						theSqlBuilder);
				break;
			case URI:
				containedCondition = createPredicateUri(
						theSourceJoinColumn,
						theResourceName,
						theSpnamePrefix,
						theParamDefinition,
						theOrValues,
						theOperation,
						theRequest,
						theRequestPartitionId,
						theSqlBuilder);
				break;
			case REFERENCE:
				containedCondition = createPredicateReference(
						theSourceJoinColumn,
						theResourceName,
						isBlank(theSpnamePrefix) ? theParamName : theSpnamePrefix + "." + theParamName,
						theQualifiers,
						theOrValues,
						theOperation,
						theRequest,
						theRequestPartitionId,
						theSqlBuilder);
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
	public Condition createPredicateResourceId(
			@Nullable DbColumn theSourceJoinColumn,
			List<List<IQueryParameterType>> theValues,
			String theResourceName,
			SearchFilterParser.CompareOperation theOperation,
			RequestPartitionId theRequestPartitionId) {
		ResourceIdPredicateBuilder builder = mySqlBuilder.newResourceIdBuilder();
		return builder.createPredicateResourceId(
				theSourceJoinColumn, theResourceName, theValues, theOperation, theRequestPartitionId);
	}

	private Condition createPredicateSourceForAndList(
			@Nullable DbColumn theSourceJoinColumn, List<List<IQueryParameterType>> theAndOrParams) {
		mySqlBuilder.getOrCreateFirstPredicateBuilder();

		List<Condition> andPredicates = new ArrayList<>(theAndOrParams.size());
		for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
			andPredicates.add(createPredicateSource(theSourceJoinColumn, nextAnd));
		}
		return toAndPredicate(andPredicates);
	}

	private Condition createPredicateSource(
			@Nullable DbColumn theSourceJoinColumn, List<? extends IQueryParameterType> theList) {
		if (myStorageSettings.getStoreMetaSourceInformation()
				== JpaStorageSettings.StoreMetaSourceInformationEnum.NONE) {
			String msg = myFhirContext.getLocalizer().getMessage(QueryStack.class, "sourceParamDisabled");
			throw new InvalidRequestException(Msg.code(1216) + msg);
		}

		List<Condition> orPredicates = new ArrayList<>();

		// :missing=true modifier processing requires "LEFT JOIN" with HFJ_RESOURCE table to return correct results
		// if both sourceUri and requestId are not populated for the resource
		Optional<? extends IQueryParameterType> isMissingSourceOptional = theList.stream()
				.filter(nextParameter -> nextParameter.getMissing() != null && nextParameter.getMissing())
				.findFirst();

		if (isMissingSourceOptional.isPresent()) {
			SourcePredicateBuilder join =
					getSourcePredicateBuilder(theSourceJoinColumn, SelectQuery.JoinType.LEFT_OUTER);
			orPredicates.add(join.createPredicateMissingSourceUri());
			return toOrPredicate(orPredicates);
		}
		// for all other cases we use "INNER JOIN" to match search parameters
		SourcePredicateBuilder join = getSourcePredicateBuilder(theSourceJoinColumn, SelectQuery.JoinType.INNER);

		for (IQueryParameterType nextParameter : theList) {
			SourceParam sourceParameter = new SourceParam(nextParameter.getValueAsQueryToken(myFhirContext));
			String sourceUri = sourceParameter.getSourceUri();
			String requestId = sourceParameter.getRequestId();
			if (isNotBlank(sourceUri) && isNotBlank(requestId)) {
				orPredicates.add(toAndPredicate(
						join.createPredicateSourceUri(sourceUri), join.createPredicateRequestId(requestId)));
			} else if (isNotBlank(sourceUri)) {
				orPredicates.add(
						join.createPredicateSourceUriWithModifiers(nextParameter, myStorageSettings, sourceUri));
			} else if (isNotBlank(requestId)) {
				orPredicates.add(join.createPredicateRequestId(requestId));
			}
		}

		return toOrPredicate(orPredicates);
	}

	private SourcePredicateBuilder getSourcePredicateBuilder(
			@Nullable DbColumn theSourceJoinColumn, SelectQuery.JoinType theJoinType) {
		return createOrReusePredicateBuilder(
						PredicateBuilderTypeEnum.SOURCE,
						theSourceJoinColumn,
						Constants.PARAM_SOURCE,
						() -> mySqlBuilder.addSourcePredicateBuilder(theSourceJoinColumn, theJoinType))
				.getResult();
	}

	public Condition createPredicateString(
			@Nullable DbColumn theSourceJoinColumn,
			String theResourceName,
			String theSpnamePrefix,
			RuntimeSearchParam theSearchParam,
			List<? extends IQueryParameterType> theList,
			SearchFilterParser.CompareOperation theOperation,
			RequestPartitionId theRequestPartitionId) {
		return createPredicateString(
				theSourceJoinColumn,
				theResourceName,
				theSpnamePrefix,
				theSearchParam,
				theList,
				theOperation,
				theRequestPartitionId,
				mySqlBuilder);
	}

	public Condition createPredicateString(
			@Nullable DbColumn theSourceJoinColumn,
			String theResourceName,
			String theSpnamePrefix,
			RuntimeSearchParam theSearchParam,
			List<? extends IQueryParameterType> theList,
			SearchFilterParser.CompareOperation theOperation,
			RequestPartitionId theRequestPartitionId,
			SearchQueryBuilder theSqlBuilder) {
		Boolean isMissing = theList.get(0).getMissing();
		String paramName = getParamNameWithPrefix(theSpnamePrefix, theSearchParam.getName());

		if (isMissing != null) {
			return createMissingParameterQuery(new MissingParameterQueryParams(
					theSqlBuilder,
					theSearchParam.getParamType(),
					theList,
					paramName,
					theResourceName,
					theSourceJoinColumn,
					theRequestPartitionId));
		}

		StringPredicateBuilder join = createOrReusePredicateBuilder(
						PredicateBuilderTypeEnum.STRING,
						theSourceJoinColumn,
						paramName,
						() -> theSqlBuilder.addStringPredicateBuilder(theSourceJoinColumn))
				.getResult();

		List<Condition> codePredicates = new ArrayList<>();
		for (IQueryParameterType nextOr : theList) {
			Condition singleCode = join.createPredicateString(
					nextOr, theResourceName, theSpnamePrefix, theSearchParam, join, theOperation);
			codePredicates.add(singleCode);
		}

		return join.combineWithRequestPartitionIdPredicate(theRequestPartitionId, toOrPredicate(codePredicates));
	}

	public Condition createPredicateTag(
			@Nullable DbColumn theSourceJoinColumn,
			List<List<IQueryParameterType>> theList,
			String theParamName,
			RequestPartitionId theRequestPartitionId) {
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
			if (!checkHaveTags(nextAndParams, theParamName)) {
				continue;
			}

			List<Triple<String, String, String>> tokens = Lists.newArrayList();
			boolean paramInverted = populateTokens(tokens, nextAndParams);
			if (tokens.isEmpty()) {
				continue;
			}

			Condition tagPredicate;
			BaseJoiningPredicateBuilder join;
			if (paramInverted) {

				SearchQueryBuilder sqlBuilder = mySqlBuilder.newChildSqlBuilder();
				TagPredicateBuilder tagSelector = sqlBuilder.addTagPredicateBuilder(null);
				sqlBuilder.addPredicate(
						tagSelector.createPredicateTag(tagType, tokens, theParamName, theRequestPartitionId));
				SelectQuery sql = sqlBuilder.getSelect();

				join = mySqlBuilder.getOrCreateFirstPredicateBuilder();
				Expression subSelect = new Subquery(sql);
				tagPredicate = new InCondition(join.getResourceIdColumn(), subSelect).setNegate(true);

			} else {
				// Tag table can't be a query root because it will include deleted resources, and can't select by
				// resource type
				mySqlBuilder.getOrCreateFirstPredicateBuilder();

				TagPredicateBuilder tagJoin = createOrReusePredicateBuilder(
								PredicateBuilderTypeEnum.TAG,
								theSourceJoinColumn,
								theParamName,
								() -> mySqlBuilder.addTagPredicateBuilder(theSourceJoinColumn))
						.getResult();
				tagPredicate = tagJoin.createPredicateTag(tagType, tokens, theParamName, theRequestPartitionId);
				join = tagJoin;
			}

			andPredicates.add(join.combineWithRequestPartitionIdPredicate(theRequestPartitionId, tagPredicate));
		}

		return toAndPredicate(andPredicates);
	}

	private boolean populateTokens(
			List<Triple<String, String, String>> theTokens, List<? extends IQueryParameterType> theAndParams) {
		boolean paramInverted = false;

		for (IQueryParameterType nextOrParam : theAndParams) {
			String code;
			String system;
			if (nextOrParam instanceof TokenParam) {
				TokenParam nextParam = (TokenParam) nextOrParam;
				code = nextParam.getValue();
				system = nextParam.getSystem();
				if (nextParam.getModifier() == TokenParamModifier.NOT) {
					paramInverted = true;
				}
			} else {
				UriParam nextParam = (UriParam) nextOrParam;
				code = nextParam.getValue();
				system = null;
			}

			if (isNotBlank(code)) {
				theTokens.add(Triple.of(system, nextOrParam.getQueryParameterQualifier(), code));
			}
		}
		return paramInverted;
	}

	private boolean checkHaveTags(List<? extends IQueryParameterType> theParams, String theParamName) {
		for (IQueryParameterType nextParamUncasted : theParams) {
			if (nextParamUncasted instanceof TokenParam) {
				TokenParam nextParam = (TokenParam) nextParamUncasted;
				if (isNotBlank(nextParam.getValue())) {
					return true;
				}
				if (isNotBlank(nextParam.getSystem())) {
					throw new TokenParamFormatInvalidRequestException(
							Msg.code(1218), theParamName, nextParam.getValueAsQueryToken(myFhirContext));
				}
			}

			UriParam nextParam = (UriParam) nextParamUncasted;
			if (isNotBlank(nextParam.getValue())) {
				return true;
			}
		}

		return false;
	}

	public Condition createPredicateToken(
			@Nullable DbColumn theSourceJoinColumn,
			String theResourceName,
			String theSpnamePrefix,
			RuntimeSearchParam theSearchParam,
			List<? extends IQueryParameterType> theList,
			SearchFilterParser.CompareOperation theOperation,
			RequestPartitionId theRequestPartitionId) {
		return createPredicateToken(
				theSourceJoinColumn,
				theResourceName,
				theSpnamePrefix,
				theSearchParam,
				theList,
				theOperation,
				theRequestPartitionId,
				mySqlBuilder);
	}

	public Condition createPredicateToken(
			@Nullable DbColumn theSourceJoinColumn,
			String theResourceName,
			String theSpnamePrefix,
			RuntimeSearchParam theSearchParam,
			List<? extends IQueryParameterType> theList,
			SearchFilterParser.CompareOperation theOperation,
			RequestPartitionId theRequestPartitionId,
			SearchQueryBuilder theSqlBuilder) {

		List<IQueryParameterType> tokens = new ArrayList<>();

		boolean paramInverted = false;
		TokenParamModifier modifier;

		for (IQueryParameterType nextOr : theList) {
			if (nextOr instanceof TokenParam) {
				if (!((TokenParam) nextOr).isEmpty()) {
					TokenParam id = (TokenParam) nextOr;
					if (id.isText()) {

						// Check whether the :text modifier is actually enabled here
						boolean tokenTextIndexingEnabled =
								BaseSearchParamExtractor.tokenTextIndexingEnabledForSearchParam(
										myStorageSettings, theSearchParam);
						if (!tokenTextIndexingEnabled) {
							String msg;
							if (myStorageSettings.isSuppressStringIndexingInTokens()) {
								msg = myFhirContext
										.getLocalizer()
										.getMessage(QueryStack.class, "textModifierDisabledForServer");
							} else {
								msg = myFhirContext
										.getLocalizer()
										.getMessage(QueryStack.class, "textModifierDisabledForSearchParam");
							}
							throw new MethodNotAllowedException(Msg.code(1219) + msg);
						}
						return createPredicateString(
								theSourceJoinColumn,
								theResourceName,
								theSpnamePrefix,
								theSearchParam,
								theList,
								null,
								theRequestPartitionId,
								theSqlBuilder);
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
			sqlBuilder.addPredicate(tokenSelector.createPredicateToken(
					tokens, theResourceName, theSpnamePrefix, theSearchParam, theRequestPartitionId));
			SelectQuery sql = sqlBuilder.getSelect();
			Expression subSelect = new Subquery(sql);

			join = theSqlBuilder.getOrCreateFirstPredicateBuilder();

			if (theSourceJoinColumn == null) {
				predicate = new InCondition(join.getResourceIdColumn(), subSelect).setNegate(true);
			} else {
				// -- for the resource link, need join with target_resource_id
				predicate = new InCondition(theSourceJoinColumn, subSelect).setNegate(true);
			}

		} else {
			Boolean isMissing = theList.get(0).getMissing();
			if (isMissing != null) {
				return createMissingParameterQuery(new MissingParameterQueryParams(
						theSqlBuilder,
						theSearchParam.getParamType(),
						theList,
						paramName,
						theResourceName,
						theSourceJoinColumn,
						theRequestPartitionId));
			}

			TokenPredicateBuilder tokenJoin = createOrReusePredicateBuilder(
							PredicateBuilderTypeEnum.TOKEN,
							theSourceJoinColumn,
							paramName,
							() -> theSqlBuilder.addTokenPredicateBuilder(theSourceJoinColumn))
					.getResult();

			predicate = tokenJoin.createPredicateToken(
					tokens, theResourceName, theSpnamePrefix, theSearchParam, theOperation, theRequestPartitionId);
			join = tokenJoin;
		}

		return join.combineWithRequestPartitionIdPredicate(theRequestPartitionId, predicate);
	}

	public Condition createPredicateUri(
			@Nullable DbColumn theSourceJoinColumn,
			String theResourceName,
			String theSpnamePrefix,
			RuntimeSearchParam theSearchParam,
			List<? extends IQueryParameterType> theList,
			SearchFilterParser.CompareOperation theOperation,
			RequestDetails theRequestDetails,
			RequestPartitionId theRequestPartitionId) {
		return createPredicateUri(
				theSourceJoinColumn,
				theResourceName,
				theSpnamePrefix,
				theSearchParam,
				theList,
				theOperation,
				theRequestDetails,
				theRequestPartitionId,
				mySqlBuilder);
	}

	public Condition createPredicateUri(
			@Nullable DbColumn theSourceJoinColumn,
			String theResourceName,
			String theSpnamePrefix,
			RuntimeSearchParam theSearchParam,
			List<? extends IQueryParameterType> theList,
			SearchFilterParser.CompareOperation theOperation,
			RequestDetails theRequestDetails,
			RequestPartitionId theRequestPartitionId,
			SearchQueryBuilder theSqlBuilder) {

		String paramName = getParamNameWithPrefix(theSpnamePrefix, theSearchParam.getName());

		Boolean isMissing = theList.get(0).getMissing();
		if (isMissing != null) {
			return createMissingParameterQuery(new MissingParameterQueryParams(
					theSqlBuilder,
					theSearchParam.getParamType(),
					theList,
					paramName,
					theResourceName,
					theSourceJoinColumn,
					theRequestPartitionId));
		} else {
			UriPredicateBuilder join = theSqlBuilder.addUriPredicateBuilder(theSourceJoinColumn);

			Condition predicate = join.addPredicate(theList, paramName, theOperation, theRequestDetails);
			return join.combineWithRequestPartitionIdPredicate(theRequestPartitionId, predicate);
		}
	}

	public QueryStack newChildQueryFactoryWithFullBuilderReuse() {
		return new QueryStack(
				mySearchParameters,
				myStorageSettings,
				myFhirContext,
				mySqlBuilder,
				mySearchParamRegistry,
				myPartitionSettings,
				EnumSet.allOf(PredicateBuilderTypeEnum.class));
	}

	@Nullable
	public Condition searchForIdsWithAndOr(SearchForIdsParams theSearchForIdsParams) {

		if (theSearchForIdsParams.myAndOrParams.isEmpty()) {
			return null;
		}

		switch (theSearchForIdsParams.myParamName) {
			case IAnyResource.SP_RES_ID:
				return createPredicateResourceId(
						theSearchForIdsParams.mySourceJoinColumn,
						theSearchForIdsParams.myAndOrParams,
						theSearchForIdsParams.myResourceName,
						null,
						theSearchForIdsParams.myRequestPartitionId);

			case Constants.PARAM_PID:
				return createPredicateResourcePID(
						theSearchForIdsParams.mySourceJoinColumn, theSearchForIdsParams.myAndOrParams);

			case PARAM_HAS:
				return createPredicateHas(
						theSearchForIdsParams.mySourceJoinColumn,
						theSearchForIdsParams.myResourceName,
						theSearchForIdsParams.myAndOrParams,
						theSearchForIdsParams.myRequest,
						theSearchForIdsParams.myRequestPartitionId);

			case Constants.PARAM_TAG:
			case Constants.PARAM_PROFILE:
			case Constants.PARAM_SECURITY:
				if (myStorageSettings.getTagStorageMode() == JpaStorageSettings.TagStorageModeEnum.INLINE) {
					return createPredicateSearchParameter(
							theSearchForIdsParams.mySourceJoinColumn,
							theSearchForIdsParams.myResourceName,
							theSearchForIdsParams.myParamName,
							theSearchForIdsParams.myAndOrParams,
							theSearchForIdsParams.myRequest,
							theSearchForIdsParams.myRequestPartitionId);
				} else {
					return createPredicateTag(
							theSearchForIdsParams.mySourceJoinColumn,
							theSearchForIdsParams.myAndOrParams,
							theSearchForIdsParams.myParamName,
							theSearchForIdsParams.myRequestPartitionId);
				}

			case Constants.PARAM_SOURCE:
				return createPredicateSourceForAndList(
						theSearchForIdsParams.mySourceJoinColumn, theSearchForIdsParams.myAndOrParams);

			case Constants.PARAM_LASTUPDATED:
				// this case statement handles a _lastUpdated query as part of a reverse search
				// only (/Patient?_has:Encounter:patient:_lastUpdated=ge2023-10-24).
				// performing a _lastUpdated query on a resource (/Patient?_lastUpdated=eq2023-10-24)
				// is handled in {@link SearchBuilder#createChunkedQuery}.
				return createReverseSearchPredicateLastUpdated(
						theSearchForIdsParams.myAndOrParams, theSearchForIdsParams.mySourceJoinColumn);

			default:
				return createPredicateSearchParameter(
						theSearchForIdsParams.mySourceJoinColumn,
						theSearchForIdsParams.myResourceName,
						theSearchForIdsParams.myParamName,
						theSearchForIdsParams.myAndOrParams,
						theSearchForIdsParams.myRequest,
						theSearchForIdsParams.myRequestPartitionId);
		}
	}

	/**
	 * Raw match on RES_ID
	 */
	private Condition createPredicateResourcePID(
			DbColumn theSourceJoinColumn, List<List<IQueryParameterType>> theAndOrParams) {

		DbColumn pidColumn = theSourceJoinColumn;

		if (pidColumn == null) {
			BaseJoiningPredicateBuilder predicateBuilder = mySqlBuilder.getOrCreateFirstPredicateBuilder();
			pidColumn = predicateBuilder.getResourceIdColumn();
		}

		// we don't support any modifiers for now
		Set<Long> pids = theAndOrParams.stream()
				.map(orList -> orList.stream()
						.map(v -> v.getValueAsQueryToken(myFhirContext))
						.map(Long::valueOf)
						.collect(Collectors.toSet()))
				.reduce(Sets::intersection)
				.orElse(Set.of());

		if (pids.isEmpty()) {
			mySqlBuilder.setMatchNothing();
			return null;
		}

		return toEqualToOrInPredicate(pidColumn, mySqlBuilder.generatePlaceholders(pids));
	}

	private Condition createReverseSearchPredicateLastUpdated(
			List<List<IQueryParameterType>> theAndOrParams, DbColumn theSourceColumn) {

		ResourceTablePredicateBuilder resourceTableJoin =
				mySqlBuilder.addResourceTablePredicateBuilder(theSourceColumn);

		List<Condition> andPredicates = new ArrayList<>(theAndOrParams.size());

		for (List<IQueryParameterType> aList : theAndOrParams) {
			if (!aList.isEmpty()) {
				DateParam dateParam = (DateParam) aList.get(0);
				DateRangeParam dateRangeParam = new DateRangeParam(dateParam);
				Condition aCondition = mySqlBuilder.addPredicateLastUpdated(dateRangeParam, resourceTableJoin);
				andPredicates.add(aCondition);
			}
		}

		return toAndPredicate(andPredicates);
	}

	@Nullable
	private Condition createPredicateSearchParameter(
			@Nullable DbColumn theSourceJoinColumn,
			String theResourceName,
			String theParamName,
			List<List<IQueryParameterType>> theAndOrParams,
			RequestDetails theRequest,
			RequestPartitionId theRequestPartitionId) {
		List<Condition> andPredicates = new ArrayList<>();
		RuntimeSearchParam nextParamDef = mySearchParamRegistry.getActiveSearchParam(theResourceName, theParamName);
		if (nextParamDef != null) {

			if (myPartitionSettings.isPartitioningEnabled() && myPartitionSettings.isIncludePartitionInSearchHashes()) {
				if (theRequestPartitionId.isAllPartitions()) {
					throw new PreconditionFailedException(
							Msg.code(1220) + "This server is not configured to support search against all partitions");
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
						andPredicates.add(createPredicateDate(
								theSourceJoinColumn,
								theResourceName,
								null,
								nextParamDef,
								nextAnd,
								operation,
								theRequestPartitionId));
					}
					break;
				case QUANTITY:
					for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
						SearchFilterParser.CompareOperation operation = null;
						if (nextAnd.size() > 0) {
							QuantityParam param = (QuantityParam) nextAnd.get(0);
							operation = toOperation(param.getPrefix());
						}
						andPredicates.add(createPredicateQuantity(
								theSourceJoinColumn,
								theResourceName,
								null,
								nextParamDef,
								nextAnd,
								operation,
								theRequestPartitionId));
					}
					break;
				case REFERENCE:
					for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {

						// Handle Search Parameters where the name is a full chain
						// (e.g. SearchParameter with name=composition.patient.identifier)
						if (handleFullyChainedParameter(
								theSourceJoinColumn,
								theResourceName,
								theParamName,
								theRequest,
								theRequestPartitionId,
								andPredicates,
								nextAnd)) {
							continue;
						}

						EmbeddedChainedSearchModeEnum embeddedChainedSearchModeEnum =
								isEligibleForEmbeddedChainedResourceSearch(theResourceName, theParamName, nextAnd);
						if (embeddedChainedSearchModeEnum == EmbeddedChainedSearchModeEnum.REF_JOIN_ONLY) {
							andPredicates.add(createPredicateReference(
									theSourceJoinColumn,
									theResourceName,
									theParamName,
									new ArrayList<>(),
									nextAnd,
									null,
									theRequest,
									theRequestPartitionId));
						} else {
							andPredicates.add(createPredicateReferenceForEmbeddedChainedSearchResource(
									theSourceJoinColumn,
									theResourceName,
									nextParamDef,
									nextAnd,
									null,
									theRequest,
									theRequestPartitionId,
									embeddedChainedSearchModeEnum));
						}
					}
					break;
				case STRING:
					for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
						andPredicates.add(createPredicateString(
								theSourceJoinColumn,
								theResourceName,
								null,
								nextParamDef,
								nextAnd,
								SearchFilterParser.CompareOperation.sw,
								theRequestPartitionId));
					}
					break;
				case TOKEN:
					for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
						if (LOCATION_POSITION.equals(nextParamDef.getPath())) {
							andPredicates.add(createPredicateCoords(
									theSourceJoinColumn,
									theResourceName,
									null,
									nextParamDef,
									nextAnd,
									theRequestPartitionId,
									mySqlBuilder));
						} else {
							andPredicates.add(createPredicateToken(
									theSourceJoinColumn,
									theResourceName,
									null,
									nextParamDef,
									nextAnd,
									null,
									theRequestPartitionId));
						}
					}
					break;
				case NUMBER:
					for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
						andPredicates.add(createPredicateNumber(
								theSourceJoinColumn,
								theResourceName,
								null,
								nextParamDef,
								nextAnd,
								null,
								theRequestPartitionId));
					}
					break;
				case COMPOSITE:
					for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
						andPredicates.add(createPredicateComposite(
								theSourceJoinColumn,
								theResourceName,
								null,
								nextParamDef,
								nextAnd,
								theRequestPartitionId));
					}
					break;
				case URI:
					for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
						andPredicates.add(createPredicateUri(
								theSourceJoinColumn,
								theResourceName,
								null,
								nextParamDef,
								nextAnd,
								SearchFilterParser.CompareOperation.eq,
								theRequest,
								theRequestPartitionId));
					}
					break;
				case HAS:
				case SPECIAL:
					for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
						if (LOCATION_POSITION.equals(nextParamDef.getPath())) {
							andPredicates.add(createPredicateCoords(
									theSourceJoinColumn,
									theResourceName,
									null,
									nextParamDef,
									nextAnd,
									theRequestPartitionId,
									mySqlBuilder));
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
						String filterString =
								((StringParam) theAndOrParams.get(0).get(0)).getValue();
						SearchFilterParser.BaseFilter filter;
						try {
							filter = SearchFilterParser.parse(filterString);
						} catch (SearchFilterParser.FilterSyntaxException theE) {
							throw new InvalidRequestException(
									Msg.code(1221) + "Error parsing _filter syntax: " + theE.getMessage());
						}
						if (filter != null) {

							if (!myStorageSettings.isFilterParameterEnabled()) {
								throw new InvalidRequestException(Msg.code(1222) + Constants.PARAM_FILTER
										+ " parameter is disabled on this server");
							}

							Condition predicate = createPredicateFilter(
									this, filter, theResourceName, theRequest, theRequestPartitionId);
							if (predicate != null) {
								mySqlBuilder.addPredicate(predicate);
							}
						}
					}

				} else {
					String msg = myFhirContext
							.getLocalizer()
							.getMessageSanitized(
									BaseStorageDao.class,
									"invalidSearchParameter",
									theParamName,
									theResourceName,
									mySearchParamRegistry.getValidSearchParameterNamesIncludingMeta(theResourceName));
					throw new InvalidRequestException(Msg.code(1223) + msg);
				}
			}
		}

		return toAndPredicate(andPredicates);
	}

	/**
	 * This method handles the case of Search Parameters where the name/code
	 * in the SP is a full chain expression. Normally to handle an expression
	 * like <code>Observation?subject.name=foo</code> are handled by a SP
	 * with a type of REFERENCE where the name is "subject". That is not
	 * handled here. On the other hand, if the SP has a name value containing
	 * the full chain (e.g. "subject.name") we handle that here.
	 *
	 * @return Returns {@literal true} if the search parameter was handled
	 * by this method
	 */
	private boolean handleFullyChainedParameter(
			@Nullable DbColumn theSourceJoinColumn,
			String theResourceName,
			String theParamName,
			RequestDetails theRequest,
			RequestPartitionId theRequestPartitionId,
			List<Condition> andPredicates,
			List<? extends IQueryParameterType> nextAnd) {
		if (!nextAnd.isEmpty() && nextAnd.get(0) instanceof ReferenceParam) {
			ReferenceParam param = (ReferenceParam) nextAnd.get(0);
			if (isNotBlank(param.getChain())) {
				String fullName = theParamName + "." + param.getChain();
				RuntimeSearchParam fullChainParam =
						mySearchParamRegistry.getActiveSearchParam(theResourceName, fullName);
				if (fullChainParam != null) {
					List<IQueryParameterType> swappedParamTypes = nextAnd.stream()
							.map(t -> newParameterInstance(fullChainParam, null, t.getValueAsQueryToken(myFhirContext)))
							.collect(Collectors.toList());
					List<List<IQueryParameterType>> params = List.of(swappedParamTypes);
					Condition predicate = createPredicateSearchParameter(
							theSourceJoinColumn, theResourceName, fullName, params, theRequest, theRequestPartitionId);
					andPredicates.add(predicate);
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * When searching using a chained search expression (e.g. "Patient?organization.name=foo")
	 * we have a few options:
	 * <ul>
	 * <li>
	 *    A. If we want to match only {@link ca.uhn.fhir.jpa.model.entity.ResourceLink} for
	 *    paramName="organization" with a join on {@link ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamString}
	 *    with paramName="name", that's {@link EmbeddedChainedSearchModeEnum#REF_JOIN_ONLY}
	 *    which is the standard searching case. Let's guess that 99.9% of all searches work
	 *    this way.
	 * </ul>
	 * <li>
	 *    B. If we want to match only {@link ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamString}
	 *    with paramName="organization.name", that's {@link EmbeddedChainedSearchModeEnum#UPLIFTED_ONLY}.
	 *    We only do this if there is an uplifted refchain declared on the "organization"
	 *    search parameter for the "name" search parameter, and contained indexing is disabled.
	 *    This kind of index can come from indexing normal references where the search parameter
	 * 	has an uplifted refchain declared, and it can also come from indexing contained resources.
	 * 	For both of these cases, the actual index in the database is identical. But the important
	 *    difference is that when you're searching for contained resources you also want to
	 *    search for normal references. When you're searching for explicit refchains, no normal
	 *    indexes matter because they'd be a duplicate of the uplifted refchain.
	 * </li>
	 * <li>
	 *    C. We can also do both and return a union of the two, using
	 *    {@link EmbeddedChainedSearchModeEnum#UPLIFTED_AND_REF_JOIN}. We do that if contained
	 *    resource indexing is enabled since we have to assume there may be indexes
	 *    on "organization" for both contained and non-contained Organization.
	 *    resources.
	 * </li>
	 */
	private EmbeddedChainedSearchModeEnum isEligibleForEmbeddedChainedResourceSearch(
			String theResourceType, String theParameterName, List<? extends IQueryParameterType> theParameter) {
		boolean indexOnContainedResources = myStorageSettings.isIndexOnContainedResources();
		boolean indexOnUpliftedRefchains = myStorageSettings.isIndexOnUpliftedRefchains();

		if (!indexOnContainedResources && !indexOnUpliftedRefchains) {
			return EmbeddedChainedSearchModeEnum.REF_JOIN_ONLY;
		}

		boolean haveUpliftCandidates = theParameter.stream()
				.filter(t -> t instanceof ReferenceParam)
				.map(t -> ((ReferenceParam) t).getChain())
				.filter(StringUtils::isNotBlank)
				// Chains on _has can't be indexed for contained searches - At least not yet. It's not clear to me if we
				// ever want to support this, it would be really hard to do.
				.filter(t -> !t.startsWith(PARAM_HAS + ":"))
				.anyMatch(t -> {
					if (indexOnContainedResources) {
						return true;
					}
					RuntimeSearchParam param =
							mySearchParamRegistry.getActiveSearchParam(theResourceType, theParameterName);
					return param != null && param.hasUpliftRefchain(t);
				});

		if (haveUpliftCandidates) {
			if (indexOnContainedResources) {
				return EmbeddedChainedSearchModeEnum.UPLIFTED_AND_REF_JOIN;
			}
			return EmbeddedChainedSearchModeEnum.UPLIFTED_ONLY;
		} else {
			return EmbeddedChainedSearchModeEnum.REF_JOIN_ONLY;
		}
	}

	public void addPredicateCompositeUnique(List<String> theIndexStrings, RequestPartitionId theRequestPartitionId) {
		ComboUniqueSearchParameterPredicateBuilder predicateBuilder = mySqlBuilder.addComboUniquePredicateBuilder();
		Condition predicate = predicateBuilder.createPredicateIndexString(theRequestPartitionId, theIndexStrings);
		mySqlBuilder.addPredicate(predicate);
	}

	public void addPredicateCompositeNonUnique(List<String> theIndexStrings, RequestPartitionId theRequestPartitionId) {
		ComboNonUniqueSearchParameterPredicateBuilder predicateBuilder =
				mySqlBuilder.addComboNonUniquePredicateBuilder();
		Condition predicate = predicateBuilder.createPredicateHashComplete(theRequestPartitionId, theIndexStrings);
		mySqlBuilder.addPredicate(predicate);
	}

	// expand out the pids
	public void addPredicateEverythingOperation(
			String theResourceName, List<String> theTypeSourceResourceNames, Long... theTargetPids) {
		ResourceLinkPredicateBuilder table = mySqlBuilder.addReferencePredicateBuilder(this, null);
		Condition predicate =
				table.createEverythingPredicate(theResourceName, theTypeSourceResourceNames, theTargetPids);
		mySqlBuilder.addPredicate(predicate);
		mySqlBuilder.getSelect().setIsDistinct(true);
	}

	public IQueryParameterType newParameterInstance(
			RuntimeSearchParam theParam, String theQualifier, String theValueAsQueryToken) {
		IQueryParameterType qp = newParameterInstance(theParam);

		qp.setValueAsQueryToken(myFhirContext, theParam.getName(), theQualifier, theValueAsQueryToken);
		return qp;
	}

	private IQueryParameterType newParameterInstance(RuntimeSearchParam theParam) {

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
				List<RuntimeSearchParam> compositeOf =
						JpaParamUtil.resolveComponentParameters(mySearchParamRegistry, theParam);
				if (compositeOf.size() != 2) {
					throw new InternalErrorException(Msg.code(1224) + "Parameter " + theParam.getName() + " has "
							+ compositeOf.size() + " composite parts. Don't know how handlt this.");
				}
				IQueryParameterType leftParam = newParameterInstance(compositeOf.get(0));
				IQueryParameterType rightParam = newParameterInstance(compositeOf.get(1));
				qp = new CompositeParam<>(leftParam, rightParam);
				break;
			case URI:
				qp = new UriParam();
				break;
			case REFERENCE:
				qp = new ReferenceParam();
				break;
			case SPECIAL:
				qp = new SpecialParam();
				break;
			case HAS:
			default:
				throw new InvalidRequestException(
						Msg.code(1225) + "The search type: " + theParam.getParamType() + " is not supported.");
		}
		return qp;
	}

	/**
	 * @see #isEligibleForEmbeddedChainedResourceSearch(String, String, List) for an explanation of the values in this enum
	 */
	enum EmbeddedChainedSearchModeEnum {
		UPLIFTED_ONLY(true),
		UPLIFTED_AND_REF_JOIN(true),
		REF_JOIN_ONLY(false);

		private final boolean mySupportsUplifted;

		EmbeddedChainedSearchModeEnum(boolean theSupportsUplifted) {
			mySupportsUplifted = theSupportsUplifted;
		}

		public boolean supportsUplifted() {
			return mySupportsUplifted;
		}
	}

	private static final class ChainElement {
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

		public String getPath() {
			return myPath;
		}

		public String getSearchParameterName() {
			return mySearchParameterName;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			ChainElement that = (ChainElement) o;
			return myResourceType.equals(that.myResourceType)
					&& mySearchParameterName.equals(that.mySearchParameterName)
					&& myPath.equals(that.myPath);
		}

		@Override
		public int hashCode() {
			return Objects.hash(myResourceType, mySearchParameterName, myPath);
		}
	}

	private class ReferenceChainExtractor {
		private final Map<List<ChainElement>, Set<LeafNodeDefinition>> myChains = Maps.newHashMap();

		public Map<List<ChainElement>, Set<LeafNodeDefinition>> getChains() {
			return myChains;
		}

		private boolean isReferenceParamValid(ReferenceParam theReferenceParam) {
			return split(theReferenceParam.getChain(), '.').length <= 3;
		}

		private List<String> extractPaths(String theResourceType, RuntimeSearchParam theSearchParam) {
			List<String> pathsForType = theSearchParam.getPathsSplit().stream()
					.map(String::trim)
					.filter(t -> (t.startsWith(theResourceType) || t.startsWith("(" + theResourceType)))
					.collect(Collectors.toList());
			if (pathsForType.isEmpty()) {
				ourLog.warn(
						"Search parameter {} does not have a path for resource type {}.",
						theSearchParam.getName(),
						theResourceType);
			}

			return pathsForType;
		}

		public void deriveChains(
				String theResourceType,
				RuntimeSearchParam theSearchParam,
				List<? extends IQueryParameterType> theList) {
			List<String> paths = extractPaths(theResourceType, theSearchParam);
			for (String path : paths) {
				List<ChainElement> searchParams = Lists.newArrayList();
				searchParams.add(new ChainElement(theResourceType, theSearchParam.getName(), path));
				for (IQueryParameterType nextOr : theList) {
					String targetValue = nextOr.getValueAsQueryToken(myFhirContext);
					if (nextOr instanceof ReferenceParam) {
						ReferenceParam referenceParam = (ReferenceParam) nextOr;
						if (!isReferenceParamValid(referenceParam)) {
							throw new InvalidRequestException(Msg.code(2007) + "The search chain "
									+ theSearchParam.getName() + "." + referenceParam.getChain()
									+ " is too long. Only chains up to three references are supported.");
						}

						String targetChain = referenceParam.getChain();
						List<String> qualifiers = Lists.newArrayList(referenceParam.getResourceType());

						processNextLinkInChain(
								searchParams,
								theSearchParam,
								targetChain,
								targetValue,
								qualifiers,
								referenceParam.getResourceType());
					}
				}
			}
		}

		private void processNextLinkInChain(
				List<ChainElement> theSearchParams,
				RuntimeSearchParam thePreviousSearchParam,
				String theChain,
				String theTargetValue,
				List<String> theQualifiers,
				String theResourceType) {

			String nextParamName = theChain;
			String nextChain = null;
			String nextQualifier = null;
			int linkIndex = theChain.indexOf('.');
			if (linkIndex != -1) {
				nextParamName = theChain.substring(0, linkIndex);
				nextChain = theChain.substring(linkIndex + 1);
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
				if (isBlank(theResourceType) || theResourceType.equals(nextTarget)) {
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
							IQueryParameterType qp = newParameterInstance(nextSearchParam);
							qp.setValueAsQueryToken(myFhirContext, nextSearchParam.getName(), null, theTargetValue);
							orValues.add(qp);
						}

						Set<LeafNodeDefinition> leafNodes = myChains.get(theSearchParams);
						if (leafNodes == null) {
							leafNodes = Sets.newHashSet();
							myChains.put(theSearchParams, leafNodes);
						}
						leafNodes.add(new LeafNodeDefinition(
								nextSearchParam, orValues, nextTarget, nextParamName, "", qualifiersBranch));
					} else {
						List<String> nextPaths = extractPaths(nextTarget, nextSearchParam);
						for (String nextPath : nextPaths) {
							List<ChainElement> searchParamBranch = Lists.newArrayList();
							searchParamBranch.addAll(theSearchParams);

							searchParamBranch.add(new ChainElement(nextTarget, nextSearchParam.getName(), nextPath));
							processNextLinkInChain(
									searchParamBranch,
									nextSearchParam,
									nextChain,
									theTargetValue,
									qualifiersBranch,
									nextQualifier);
						}
					}
				}
			}
			if (!searchParamFound) {
				throw new InvalidRequestException(Msg.code(1214)
						+ myFhirContext
								.getLocalizer()
								.getMessage(
										BaseStorageDao.class,
										"invalidParameterChain",
										thePreviousSearchParam.getName() + '.' + theChain));
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

		public LeafNodeDefinition(
				RuntimeSearchParam theParamDefinition,
				ArrayList<IQueryParameterType> theOrValues,
				String theLeafTarget,
				String theLeafParamName,
				String theLeafPathPrefix,
				List<String> theQualifiers) {
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
			return new LeafNodeDefinition(
					myParamDefinition, myOrValues, theResourceType, myLeafParamName, theName, myQualifiers);
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			LeafNodeDefinition that = (LeafNodeDefinition) o;
			return Objects.equals(myParamDefinition, that.myParamDefinition)
					&& Objects.equals(myOrValues, that.myOrValues)
					&& Objects.equals(myLeafTarget, that.myLeafTarget)
					&& Objects.equals(myLeafParamName, that.myLeafParamName)
					&& Objects.equals(myLeafPathPrefix, that.myLeafPathPrefix)
					&& Objects.equals(myQualifiers, that.myQualifiers);
		}

		@Override
		public int hashCode() {
			return Objects.hash(
					myParamDefinition, myOrValues, myLeafTarget, myLeafParamName, myLeafPathPrefix, myQualifiers);
		}

		/**
		 * Return a copy of this object with the given {@link RuntimeSearchParam}
		 * but all other values unchanged.
		 */
		public LeafNodeDefinition withParam(RuntimeSearchParam theParamDefinition) {
			return new LeafNodeDefinition(
					theParamDefinition, myOrValues, myLeafTarget, myLeafParamName, myLeafPathPrefix, myQualifiers);
		}
	}

	public static class SearchForIdsParams {
		DbColumn mySourceJoinColumn;
		String myResourceName;
		String myParamName;
		List<List<IQueryParameterType>> myAndOrParams;
		RequestDetails myRequest;
		RequestPartitionId myRequestPartitionId;
		ResourceTablePredicateBuilder myResourceTablePredicateBuilder;

		public static SearchForIdsParams with() {
			return new SearchForIdsParams();
		}

		public DbColumn getSourceJoinColumn() {
			return mySourceJoinColumn;
		}

		public SearchForIdsParams setSourceJoinColumn(DbColumn theSourceJoinColumn) {
			mySourceJoinColumn = theSourceJoinColumn;
			return this;
		}

		public String getResourceName() {
			return myResourceName;
		}

		public SearchForIdsParams setResourceName(String theResourceName) {
			myResourceName = theResourceName;
			return this;
		}

		public String getParamName() {
			return myParamName;
		}

		public SearchForIdsParams setParamName(String theParamName) {
			myParamName = theParamName;
			return this;
		}

		public List<List<IQueryParameterType>> getAndOrParams() {
			return myAndOrParams;
		}

		public SearchForIdsParams setAndOrParams(List<List<IQueryParameterType>> theAndOrParams) {
			myAndOrParams = theAndOrParams;
			return this;
		}

		public RequestDetails getRequest() {
			return myRequest;
		}

		public SearchForIdsParams setRequest(RequestDetails theRequest) {
			myRequest = theRequest;
			return this;
		}

		public RequestPartitionId getRequestPartitionId() {
			return myRequestPartitionId;
		}

		public SearchForIdsParams setRequestPartitionId(RequestPartitionId theRequestPartitionId) {
			myRequestPartitionId = theRequestPartitionId;
			return this;
		}

		public ResourceTablePredicateBuilder getResourceTablePredicateBuilder() {
			return myResourceTablePredicateBuilder;
		}

		public SearchForIdsParams setResourceTablePredicateBuilder(
				ResourceTablePredicateBuilder theResourceTablePredicateBuilder) {
			myResourceTablePredicateBuilder = theResourceTablePredicateBuilder;
			return this;
		}
	}
}
