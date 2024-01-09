/*-
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
package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.dao.predicate.SearchFilterParser;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.entity.SearchInclude;
import ca.uhn.fhir.jpa.entity.SearchTypeEnum;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.search.SearchStatusEnum;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.ComboCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.InCondition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.apache.commons.collections4.bidimap.UnmodifiableBidiMap;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

public class QueryParameterUtils {
	private static final Logger ourLog = LoggerFactory.getLogger(QueryParameterUtils.class);
	public static final int DEFAULT_SYNC_SIZE = 250;

	private static final BidiMap<SearchFilterParser.CompareOperation, ParamPrefixEnum> ourCompareOperationToParamPrefix;
	public static final Condition[] EMPTY_CONDITION_ARRAY = new Condition[0];

	static {
		DualHashBidiMap<SearchFilterParser.CompareOperation, ParamPrefixEnum> compareOperationToParamPrefix =
				new DualHashBidiMap<>();
		compareOperationToParamPrefix.put(SearchFilterParser.CompareOperation.ap, ParamPrefixEnum.APPROXIMATE);
		compareOperationToParamPrefix.put(SearchFilterParser.CompareOperation.eq, ParamPrefixEnum.EQUAL);
		compareOperationToParamPrefix.put(SearchFilterParser.CompareOperation.gt, ParamPrefixEnum.GREATERTHAN);
		compareOperationToParamPrefix.put(
				SearchFilterParser.CompareOperation.ge, ParamPrefixEnum.GREATERTHAN_OR_EQUALS);
		compareOperationToParamPrefix.put(SearchFilterParser.CompareOperation.lt, ParamPrefixEnum.LESSTHAN);
		compareOperationToParamPrefix.put(SearchFilterParser.CompareOperation.le, ParamPrefixEnum.LESSTHAN_OR_EQUALS);
		compareOperationToParamPrefix.put(SearchFilterParser.CompareOperation.ne, ParamPrefixEnum.NOT_EQUAL);
		compareOperationToParamPrefix.put(SearchFilterParser.CompareOperation.eb, ParamPrefixEnum.ENDS_BEFORE);
		compareOperationToParamPrefix.put(SearchFilterParser.CompareOperation.sa, ParamPrefixEnum.STARTS_AFTER);
		ourCompareOperationToParamPrefix = UnmodifiableBidiMap.unmodifiableBidiMap(compareOperationToParamPrefix);
	}

	@Nullable
	public static Condition toAndPredicate(List<Condition> theAndPredicates) {
		List<Condition> andPredicates =
				theAndPredicates.stream().filter(Objects::nonNull).collect(Collectors.toList());
		if (andPredicates.size() == 0) {
			return null;
		} else if (andPredicates.size() == 1) {
			return andPredicates.get(0);
		} else {
			return ComboCondition.and(andPredicates.toArray(EMPTY_CONDITION_ARRAY));
		}
	}

	@Nullable
	public static Condition toOrPredicate(List<Condition> theOrPredicates) {
		List<Condition> orPredicates =
				theOrPredicates.stream().filter(t -> t != null).collect(Collectors.toList());
		if (orPredicates.size() == 0) {
			return null;
		} else if (orPredicates.size() == 1) {
			return orPredicates.get(0);
		} else {
			return ComboCondition.or(orPredicates.toArray(EMPTY_CONDITION_ARRAY));
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
	public static Condition toEqualToOrInPredicate(
			DbColumn theColumn, List<String> theValuePlaceholders, boolean theInverse) {
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
		return ObjectUtils.defaultIfNull(retVal, SearchFilterParser.CompareOperation.eq);
	}

	public static ParamPrefixEnum fromOperation(SearchFilterParser.CompareOperation thePrefix) {
		ParamPrefixEnum retVal = null;
		if (thePrefix != null && ourCompareOperationToParamPrefix.containsKey(thePrefix)) {
			retVal = ourCompareOperationToParamPrefix.get(thePrefix);
		}
		return ObjectUtils.defaultIfNull(retVal, ParamPrefixEnum.EQUAL);
	}

	public static String getChainedPart(String parameter) {
		return parameter.substring(parameter.indexOf(".") + 1);
	}

	public static String getParamNameWithPrefix(String theSpnamePrefix, String theParamName) {

		if (StringUtils.isBlank(theSpnamePrefix)) return theParamName;

		return theSpnamePrefix + "." + theParamName;
	}

	public static Predicate[] toPredicateArray(List<Predicate> thePredicates) {
		return thePredicates.toArray(new Predicate[0]);
	}

	private static List<Predicate> createLastUpdatedPredicates(
			final DateRangeParam theLastUpdated, CriteriaBuilder builder, From<?, ResourceTable> from) {
		List<Predicate> lastUpdatedPredicates = new ArrayList<>();
		if (theLastUpdated != null) {
			if (theLastUpdated.getLowerBoundAsInstant() != null) {
				ourLog.debug("LastUpdated lower bound: {}", new InstantDt(theLastUpdated.getLowerBoundAsInstant()));
				Predicate predicateLower =
						builder.greaterThanOrEqualTo(from.get("myUpdated"), theLastUpdated.getLowerBoundAsInstant());
				lastUpdatedPredicates.add(predicateLower);
			}
			if (theLastUpdated.getUpperBoundAsInstant() != null) {
				Predicate predicateUpper =
						builder.lessThanOrEqualTo(from.get("myUpdated"), theLastUpdated.getUpperBoundAsInstant());
				lastUpdatedPredicates.add(predicateUpper);
			}
		}
		return lastUpdatedPredicates;
	}

	public static void verifySearchHasntFailedOrThrowInternalErrorException(Search theSearch) {
		if (theSearch.getStatus() == SearchStatusEnum.FAILED) {
			Integer status = theSearch.getFailureCode();
			status = defaultIfNull(status, 500);

			String message = theSearch.getFailureMessage();
			throw BaseServerResponseException.newInstance(status, message);
		}
	}

	public static void populateSearchEntity(
			SearchParameterMap theParams,
			String theResourceType,
			String theSearchUuid,
			String theQueryString,
			Search theSearch,
			RequestPartitionId theRequestPartitionId) {
		theSearch.setDeleted(false);
		theSearch.setUuid(theSearchUuid);
		theSearch.setCreated(new Date());
		theSearch.setTotalCount(null);
		theSearch.setNumFound(0);
		theSearch.setPreferredPageSize(theParams.getCount());
		theSearch.setSearchType(
				theParams.getEverythingMode() != null ? SearchTypeEnum.EVERYTHING : SearchTypeEnum.SEARCH);
		theSearch.setLastUpdated(theParams.getLastUpdated());
		theSearch.setResourceType(theResourceType);
		theSearch.setStatus(SearchStatusEnum.LOADING);
		theSearch.setSearchQueryString(theQueryString, theRequestPartitionId);

		if (theParams.hasIncludes()) {
			for (Include next : theParams.getIncludes()) {
				theSearch.addInclude(new SearchInclude(theSearch, next.getValue(), false, next.isRecurse()));
			}
		}

		for (Include next : theParams.getRevIncludes()) {
			theSearch.addInclude(new SearchInclude(theSearch, next.getValue(), true, next.isRecurse()));
		}
	}
}
