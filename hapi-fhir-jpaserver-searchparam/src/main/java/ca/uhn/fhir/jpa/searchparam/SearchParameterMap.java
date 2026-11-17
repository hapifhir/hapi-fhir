/*
 * #%L
 * HAPI FHIR JPA - Search Parameters
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.searchparam;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.searchparam.util.JpaParamUtil;
import ca.uhn.fhir.model.api.IQueryParameterAnd;
import ca.uhn.fhir.model.api.IQueryParameterOr;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.repository.IRepository;
import ca.uhn.fhir.repository.IRepositoryRestQueryBuilder;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.QualifiedParamList;
import ca.uhn.fhir.rest.api.SearchContainedModeEnum;
import ca.uhn.fhir.rest.api.SearchIncludeDeletedEnum;
import ca.uhn.fhir.rest.api.SearchTotalModeEnum;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.rest.param.DateAndListParam;
import ca.uhn.fhir.rest.param.DateOrListParam;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.param.QuantityParam;
import ca.uhn.fhir.rest.param.TokenParamModifier;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.UrlUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.Strings;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static ca.uhn.fhir.rest.param.ParamPrefixEnum.GREATERTHAN_OR_EQUALS;
import static ca.uhn.fhir.rest.param.ParamPrefixEnum.LESSTHAN_OR_EQUALS;
import static ca.uhn.fhir.rest.param.ParamPrefixEnum.NOT_EQUAL;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class SearchParameterMap implements Serializable, IRepository.IRepositoryRestQueryContributor {
	public static final Integer INTEGER_0 = 0;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchParameterMap.class);

	@Serial
	private static final long serialVersionUID = 1L;

	private final HashMap<String, List<List<IQueryParameterType>>> mySearchParameterMap = new LinkedHashMap<>();
	private Integer myCount;
	private Integer myOffset;
	private EverythingModeEnum myEverythingMode = null;
	private Set<Include> myIncludes;
	/**
	 * The <code>_lastUpdated</code> filter: AND groups of OR alternatives, without empty values or groups.
	 * <code>null</code> means no filter.
	 */
	private transient DateAndListParam myLastUpdatedAndList;
	/**
	 * Only used as a carrier during Java serialization (it is <code>null</code> otherwise), so that the serialized
	 * form stays compatible with the previous <code>DateRangeParam myLastUpdated</code> field.
	 */
	private Serializable myLastUpdated;

	private DateRangeParam myCompartmentLastUpdated;
	private boolean myLoadSynchronous;
	private Integer myLoadSynchronousUpTo;
	private Set<Include> myRevIncludes;
	private SortSpec mySort;
	private SummaryEnum mySummaryMode;
	private SearchTotalModeEnum mySearchTotalMode;
	private QuantityParam myNearDistanceParam;
	private boolean myLastN;
	private Integer myLastNMax;
	private boolean myDeleteExpunge;
	private SearchContainedModeEnum mySearchContainedMode = SearchContainedModeEnum.FALSE;
	private SearchIncludeDeletedEnum mySearchIncludeDeletedMode;

	/**
	 * Constructor
	 */
	public SearchParameterMap() {
		super();
	}

	/**
	 * Constructor
	 */
	public SearchParameterMap(String theName, IQueryParameterType theParam) {
		add(theName, theParam);
	}

	/**
	 * Creates and returns a copy of this map
	 */
	@SuppressWarnings("MethodDoesntCallSuperMethod")
	@JsonIgnore
	@Override
	public SearchParameterMap clone() {
		SearchParameterMap map = new SearchParameterMap();
		map.setSummaryMode(getSummaryMode());
		map.setSort(getSort());
		map.setSearchTotalMode(getSearchTotalMode());
		map.setRevIncludes(getRevIncludes());
		map.setIncludes(getIncludes());
		map.setEverythingMode(getEverythingMode());
		map.setCount(getCount());
		map.setDeleteExpunge(isDeleteExpunge());
		map.setLastN(isLastN());
		map.setLastNMax(getLastNMax());
		map.myLastUpdatedAndList = copyLastUpdatedAndList(myLastUpdatedAndList);
		map.setCompartmentLastUpdated(getCompartmentLastUpdated());
		map.setLoadSynchronous(isLoadSynchronous());
		map.setNearDistanceParam(getNearDistanceParam());
		map.setLoadSynchronousUpTo(getLoadSynchronousUpTo());
		map.setOffset(getOffset());
		map.setSearchContainedMode(getSearchContainedMode());
		map.setSearchIncludeDeletedMode(getSearchIncludeDeletedMode());

		for (Map.Entry<String, List<List<IQueryParameterType>>> entry : mySearchParameterMap.entrySet()) {
			List<List<IQueryParameterType>> andParams = entry.getValue();
			List<List<IQueryParameterType>> newAndParams = new ArrayList<>();
			for (List<IQueryParameterType> orParams : andParams) {
				List<IQueryParameterType> newOrParams = new ArrayList<>(orParams);
				newAndParams.add(newOrParams);
			}
			map.put(entry.getKey(), newAndParams);
		}

		return map;
	}

	public SummaryEnum getSummaryMode() {
		return mySummaryMode;
	}

	public void setSummaryMode(SummaryEnum theSummaryMode) {
		mySummaryMode = theSummaryMode;
	}

	public SearchTotalModeEnum getSearchTotalMode() {
		return mySearchTotalMode;
	}

	public void setSearchTotalMode(SearchTotalModeEnum theSearchTotalMode) {
		mySearchTotalMode = theSearchTotalMode;
	}

	public SearchParameterMap add(String theName, DateParam theDateParam) {
		add(theName, (IQueryParameterOr<?>) theDateParam);
		return this;
	}

	@SuppressWarnings("unchecked")
	public SearchParameterMap add(String theName, IQueryParameterAnd<?> theAnd) {
		if (theAnd == null) {
			return this;
		}

		if (theAnd instanceof DateRangeParam dateRangeParam) {
			if (dateRangeParam.getLowerBound() != null
					&& !dateRangeParam.getLowerBound().isEmpty()
					&& dateRangeParam.getUpperBound() != null
					&& !dateRangeParam.isEmpty()
					&& dateRangeParam.getLowerBound().equals(dateRangeParam.getUpperBound())) {
				add(theName, dateRangeParam.getLowerBound());
				return this;
			}
		}

		List<List<IQueryParameterType>> paramList = getOrCreate(theName);
		for (IQueryParameterOr<?> next : theAnd.getValuesAsQueryTokens()) {
			if (next == null) {
				continue;
			}
			paramList.add((List<IQueryParameterType>) next.getValuesAsQueryTokens());
		}

		return this;
	}

	private List<List<IQueryParameterType>> getOrCreate(String theName) {
		return mySearchParameterMap.computeIfAbsent(theName, k -> new ArrayList<>());
	}

	@SuppressWarnings("unchecked")
	@Nonnull
	public SearchParameterMap add(@Nonnull String theName, @Nullable IQueryParameterOr<?> theOr) {
		List<IQueryParameterType> orList =
				theOr != null ? (List<IQueryParameterType>) theOr.getValuesAsQueryTokens() : null;
		return addOrList(theName, orList);
	}

	/**
	 * Adds a list of parameters to the map, treating them as OR predicates
	 */
	@SuppressWarnings("unchecked")
	@Nonnull
	public SearchParameterMap addOrList(
			@Nonnull String theName, @Nullable List<? extends IQueryParameterType> theOrList) {
		if (theOrList == null) {
			return this;
		}

		getOrCreate(theName).add((List<IQueryParameterType>) theOrList);

		return this;
	}

	public Collection<List<List<IQueryParameterType>>> values() {
		return mySearchParameterMap.values();
	}

	public SearchParameterMap add(String theName, IQueryParameterType theParam) {
		assert !Constants.PARAM_LASTUPDATED.equals(theName); // this has it's own field in the map

		if (theParam == null) {
			return this;
		}
		ArrayList<IQueryParameterType> list = new ArrayList<>();
		list.add(theParam);
		getOrCreate(theName).add(list);

		return this;
	}

	public SearchParameterMap addInclude(Include theInclude) {
		getIncludes().add(theInclude);
		return this;
	}

	private void addLastUpdateParam(StringBuilder theBuilder, ParamPrefixEnum thePrefix, DateParam theDateParam) {
		if (theDateParam != null && isNotBlank(theDateParam.getValueAsString())) {
			addUrlParamSeparator(theBuilder);
			theBuilder.append(Constants.PARAM_LASTUPDATED);
			theBuilder.append('=');
			theBuilder.append(thePrefix.getValue());
			theBuilder.append(theDateParam.getValueAsString());
		}
	}

	private void addCompartmentLastUpdatedParam(
			StringBuilder theBuilder, ParamPrefixEnum thePrefix, DateParam theDateParam) {
		if (theDateParam != null && isNotBlank(theDateParam.getValueAsString())) {
			addUrlParamSeparator(theBuilder);
			theBuilder.append(Constants.PARAM_COMPARTMENT_LAST_UPDATED);
			theBuilder.append('=');
			theBuilder.append(thePrefix.getValue());
			theBuilder.append(theDateParam.getValueAsString());
		}
	}

	public SearchParameterMap addRevInclude(Include theInclude) {
		getRevIncludes().add(theInclude);
		return this;
	}

	private void addUrlIncludeParams(StringBuilder b, String paramName, Set<Include> theList) {
		ArrayList<Include> list = new ArrayList<>(theList);

		list.sort(new IncludeComparator());
		for (Include nextInclude : list) {
			addUrlParamSeparator(b);
			b.append(paramName);
			if (nextInclude.isRecurse()) {
				b.append(Constants.PARAM_INCLUDE_QUALIFIER_RECURSE);
			}
			b.append('=');
			if (Constants.INCLUDE_STAR.equals(nextInclude.getValue())) {
				b.append(Constants.INCLUDE_STAR);
			} else {
				b.append(UrlUtil.escapeUrlParam(nextInclude.getParamType()));
				b.append(':');
				b.append(UrlUtil.escapeUrlParam(nextInclude.getParamName()));
				if (isNotBlank(nextInclude.getParamTargetType())) {
					b.append(':');
					b.append(nextInclude.getParamTargetType());
				}
			}
		}
	}

	private void addUrlParamSeparator(StringBuilder theB) {
		if (theB.isEmpty()) {
			theB.append('?');
		} else {
			theB.append('&');
		}
	}

	public Integer getCount() {
		return myCount;
	}

	public SearchParameterMap setCount(Integer theCount) {
		myCount = theCount;
		return this;
	}

	public Integer getOffset() {
		return myOffset;
	}

	public void setOffset(Integer theOffset) {
		myOffset = theOffset;
	}

	public EverythingModeEnum getEverythingMode() {
		return myEverythingMode;
	}

	public void setEverythingMode(EverythingModeEnum theConsolidateMatches) {
		myEverythingMode = theConsolidateMatches;
	}

	public Set<Include> getIncludes() {
		if (myIncludes == null) {
			myIncludes = new HashSet<>();
		}
		return myIncludes;
	}

	public void setIncludes(Set<Include> theIncludes) {
		myIncludes = theIncludes;
	}

	/**
	 * Returns null if there is no last updated value.
	 * <p>
	 * A <b>new</b> instance is returned on every call: changing it does not change this map. Use
	 * {@link #setLastUpdated(DateRangeParam)} to change the filter.
	 * </p>
	 * <p>
	 * If the <code>_lastUpdated</code> filter combines values in a way that can not be expressed as a single
	 * range (e.g. <code>_lastUpdated=gt2020&amp;_lastUpdated=gt2021</code> or <code>_lastUpdated=lt2020,gt2021</code>)
	 * this method throws an {@link InvalidRequestException} instead of returning a partial range. Use
	 * {@link #isLastUpdatedRepresentableAsRange()} to test for that, {@link #hasLastUpdated()} to test for the
	 * presence of a filter, and {@link #getLastUpdatedAndList()} to obtain the complete expression.
	 * </p>
	 *
	 * @throws InvalidRequestException If the filter can not be expressed as a single range
	 */
	public DateRangeParam getLastUpdated() {
		if (myLastUpdatedAndList == null) {
			return null;
		}
		DateRangeParam retVal = toLastUpdatedRangeOrNull();
		if (retVal == null) {
			throw new InvalidRequestException(Msg.code(3075) + "The " + Constants.PARAM_LASTUPDATED
					+ " parameter combines values with AND/OR in a way that can not be expressed as a single date range"
					+ " and is not supported by this operation. Use SearchParameterMap#getLastUpdatedAndList()"
					+ " to access the complete expression.");
		}
		return retVal;
	}

	/**
	 * Sets the <code>_lastUpdated</code> filter from a range. The filter is stored as an AND list: a single
	 * <code>eq</code>, <code>ne</code> or unprefixed value used for both bounds is stored as one value, and otherwise
	 * every bound that has a value is stored as one value. In the latter case a bound with the <code>eq</code>,
	 * <code>ne</code> or no prefix is stored as <code>ge</code> (lower bound) or <code>le</code> (upper bound), which
	 * matches the same instants. A bound without a value (e.g. <code>:missing</code>) is ignored.
	 *
	 * @param theLastUpdated The range, or <code>null</code> to clear the filter
	 * @throws IllegalArgumentException If the range has two different <code>ne</code> bounds, or a bound with a prefix
	 *                                  of the wrong side (e.g. a lower bound with <code>lt</code>), as these can not
	 *                                  be expressed exactly
	 */
	public void setLastUpdated(DateRangeParam theLastUpdated) {
		if (theLastUpdated == null) {
			myLastUpdatedAndList = null;
			return;
		}

		DateParam lower = theLastUpdated.getLowerBound();
		DateParam upper = theLastUpdated.getUpperBound();
		boolean hasLower = lower != null && lower.getValue() != null;
		boolean hasUpper = upper != null && upper.getValue() != null;

		List<DateParam> values = new ArrayList<>(2);
		if (hasLower
				&& hasUpper
				&& Objects.equals(lower.getPrefix(), upper.getPrefix())
				&& (lower.getPrefix() == null
						|| lower.getPrefix() == ParamPrefixEnum.EQUAL
						|| lower.getPrefix() == ParamPrefixEnum.NOT_EQUAL)
				&& Objects.equals(lower.getValueAsString(), upper.getValueAsString())) {
			// eq, ne and unprefixed values are stored as a range with the same value on both bounds
			values.add(new DateParam(lower.getPrefix(), lower.getValueAsString()));
		} else {
			if (hasLower
					&& hasUpper
					&& lower.getPrefix() == ParamPrefixEnum.NOT_EQUAL
					&& upper.getPrefix() == ParamPrefixEnum.NOT_EQUAL) {
				throw new IllegalArgumentException(Msg.code(3077) + "Can not set " + Constants.PARAM_LASTUPDATED
						+ " to a range with two different bounds that both have the ne prefix: " + theLastUpdated);
			}
			if (hasLower) {
				values.add(toBoundValue(lower, true, theLastUpdated));
			}
			if (hasUpper) {
				values.add(toBoundValue(upper, false, theLastUpdated));
			}
		}

		if (values.isEmpty()) {
			myLastUpdatedAndList = null;
			return;
		}
		DateAndListParam andList = new DateAndListParam();
		for (DateParam next : values) {
			andList.addAnd(new DateOrListParam().addOr(next));
		}
		myLastUpdatedAndList = andList;
	}

	private static DateParam toBoundValue(DateParam theBound, boolean theLower, DateRangeParam theRange) {
		ParamPrefixEnum prefix = theBound.getPrefix();
		boolean neutral = prefix == null || prefix == ParamPrefixEnum.EQUAL || prefix == ParamPrefixEnum.NOT_EQUAL;
		boolean rightSide = theLower
				? (prefix == ParamPrefixEnum.GREATERTHAN
						|| prefix == ParamPrefixEnum.GREATERTHAN_OR_EQUALS
						|| prefix == ParamPrefixEnum.STARTS_AFTER)
				: (prefix == ParamPrefixEnum.LESSTHAN
						|| prefix == ParamPrefixEnum.LESSTHAN_OR_EQUALS
						|| prefix == ParamPrefixEnum.ENDS_BEFORE);
		if (!neutral && !rightSide) {
			throw new IllegalArgumentException(Msg.code(3078) + "Can not set " + Constants.PARAM_LASTUPDATED
					+ " to a range whose " + (theLower ? "lower" : "upper") + " bound has the prefix "
					+ prefix.getValue() + ": " + theRange);
		}
		ParamPrefixEnum newPrefix = neutral
				? (theLower ? ParamPrefixEnum.GREATERTHAN_OR_EQUALS : ParamPrefixEnum.LESSTHAN_OR_EQUALS)
				: prefix;
		return new DateParam(newPrefix, theBound.getValueAsString());
	}

	/**
	 * Returns <code>true</code> if there is a <code>_lastUpdated</code> filter, whether it is expressed as a
	 * range or as a combination that can not be expressed as a range.
	 */
	@JsonIgnore
	public boolean hasLastUpdated() {
		return myLastUpdatedAndList != null;
	}

	/**
	 * Returns <code>true</code> if there is no <code>_lastUpdated</code> filter, or if the filter can be expressed as
	 * a single range, i.e. {@link #getLastUpdated()} does not throw.
	 */
	@JsonIgnore
	public boolean isLastUpdatedRepresentableAsRange() {
		return myLastUpdatedAndList == null || toLastUpdatedRangeOrNull() != null;
	}

	/**
	 * Sets the <code>_lastUpdated</code> filter from a full AND/OR expression. Each OR alternative keeps its own
	 * prefix. The expression is copied. Empty values are ignored, and an OR group that contains a value without an
	 * instant (such as <code>:missing</code>) is ignored as a whole.
	 *
	 * @param theLastUpdated The expression, or <code>null</code> to clear the filter
	 * @throws InvalidRequestException If a value can not be used in a <code>_lastUpdated</code> search (e.g. the
	 *                                 <code>ap</code> prefix)
	 */
	public void setLastUpdatedAndList(DateAndListParam theLastUpdated) {
		if (theLastUpdated == null) {
			myLastUpdatedAndList = null;
			return;
		}
		DateAndListParam copy = new DateAndListParam();
		for (DateOrListParam nextOr : theLastUpdated.getValuesAsQueryTokens()) {
			List<DateParam> group = JpaParamUtil.toEffectiveLastUpdatedGroup(nextOr.getValuesAsQueryTokens());
			if (group == null) {
				continue;
			}
			DateOrListParam orCopy = new DateOrListParam();
			for (DateParam next : group) {
				orCopy.addOr(copyDateParam(next));
			}
			copy.addAnd(orCopy);
		}
		myLastUpdatedAndList = copy.getValuesAsQueryTokens().isEmpty() ? null : copy;
	}

	/**
	 * Returns a copy of the complete <code>_lastUpdated</code> expression, or <code>null</code> if there is no
	 * filter.
	 */
	@JsonIgnore
	@Nullable
	public DateAndListParam getLastUpdatedAndList() {
		return copyLastUpdatedAndList(myLastUpdatedAndList);
	}

	private static DateParam copyDateParam(DateParam theDateParam) {
		DateParam copy = new DateParam();
		copy.setValueAsQueryToken(null, Constants.PARAM_LASTUPDATED, null, theDateParam.getValueAsQueryToken());
		return copy;
	}

	@Nullable
	private static DateAndListParam copyLastUpdatedAndList(@Nullable DateAndListParam theAndList) {
		if (theAndList == null) {
			return null;
		}
		DateAndListParam retVal = new DateAndListParam();
		for (DateOrListParam nextOr : theAndList.getValuesAsQueryTokens()) {
			DateOrListParam orCopy = new DateOrListParam();
			for (DateParam next : nextOr.getValuesAsQueryTokens()) {
				orCopy.addOr(copyDateParam(next));
			}
			retVal.addAnd(orCopy);
		}
		return retVal;
	}

	/**
	 * Returns a new range for the current filter using the same parser as for a search URL, or <code>null</code> if
	 * there is no filter or if the parser does not accept it.
	 */
	@Nullable
	private DateRangeParam toLastUpdatedRangeOrNull() {
		if (myLastUpdatedAndList == null) {
			return null;
		}
		List<QualifiedParamList> tokens = new ArrayList<>();
		for (DateOrListParam nextOr : myLastUpdatedAndList.getValuesAsQueryTokens()) {
			QualifiedParamList list = new QualifiedParamList();
			for (DateParam next : nextOr.getValuesAsQueryTokens()) {
				list.add(next.getValueAsQueryToken());
			}
			tokens.add(list);
		}
		DateRangeParam retVal = new DateRangeParam();
		try {
			retVal.setValuesAsQueryTokens(null, Constants.PARAM_LASTUPDATED, tokens);
		} catch (InvalidRequestException e) {
			return null;
		}
		return retVal;
	}

	/**
	 * Canonical form of the filter: AND groups sorted, alternatives within each group sorted, each alternative as
	 * <code>[prefix][value]</code> with its original prefix. The canonicalization is lexical: values are kept as
	 * written, so equal instants written with different offsets produce different forms. At worst this causes a
	 * redundant search, never wrong results.
	 */
	private List<String> getLastUpdatedCanonicalForm() {
		if (myLastUpdatedAndList == null) {
			return Collections.emptyList();
		}
		List<String> retVal = new ArrayList<>();
		for (DateOrListParam nextOr : myLastUpdatedAndList.getValuesAsQueryTokens()) {
			List<String> group = new ArrayList<>();
			for (DateParam next : nextOr.getValuesAsQueryTokens()) {
				group.add(next.getValueAsQueryToken());
			}
			Collections.sort(group);
			retVal.add(String.join(",", group));
		}
		Collections.sort(retVal);
		return retVal;
	}

	@Serial
	private synchronized void writeObject(ObjectOutputStream theOut) throws IOException {
		try {
			if (isLastUpdatedRepresentableAsRange()) {
				myLastUpdated = getLastUpdated();
			} else {
				myLastUpdated = copyLastUpdatedAndList(myLastUpdatedAndList);
			}
			theOut.defaultWriteObject();
		} finally {
			myLastUpdated = null;
		}
	}

	@Serial
	private void readObject(ObjectInputStream theIn) throws IOException, ClassNotFoundException {
		theIn.defaultReadObject();
		Serializable carrier = myLastUpdated;
		myLastUpdated = null;
		myLastUpdatedAndList = null;
		if (carrier instanceof DateRangeParam range) {
			setLastUpdated(range);
		} else if (carrier instanceof DateAndListParam andList) {
			setLastUpdatedAndList(andList);
		}
	}

	/**
	 * Returns null if there is no compartment last updated value
	 */
	public DateRangeParam getCompartmentLastUpdated() {
		if (myCompartmentLastUpdated == null || myCompartmentLastUpdated.isEmpty()) {
			return null;
		}
		return myCompartmentLastUpdated;
	}

	public void setCompartmentLastUpdated(DateRangeParam theCompartmentLastUpdated) {
		myCompartmentLastUpdated = theCompartmentLastUpdated;
	}

	/**
	 * If set, tells the server to load these results synchronously, and not to load
	 * more than X results
	 */
	public Integer getLoadSynchronousUpTo() {
		return myLoadSynchronousUpTo;
	}

	/**
	 * If set, tells the server to load these results synchronously, and not to load
	 * more than X results. Note that setting this to a value will also set
	 * {@link #setLoadSynchronous(boolean)} to true
	 */
	public SearchParameterMap setLoadSynchronousUpTo(Integer theLoadSynchronousUpTo) {
		myLoadSynchronousUpTo = theLoadSynchronousUpTo;
		if (myLoadSynchronousUpTo != null) {
			setLoadSynchronous(true);
		}
		return this;
	}

	public Set<Include> getRevIncludes() {
		if (myRevIncludes == null) {
			myRevIncludes = new HashSet<>();
		}
		return myRevIncludes;
	}

	public void setRevIncludes(Set<Include> theRevIncludes) {
		myRevIncludes = theRevIncludes;
	}

	public SortSpec getSort() {
		return mySort;
	}

	public SearchParameterMap setSort(SortSpec theSort) {
		mySort = theSort;
		return this;
	}

	/**
	 * If set, tells the server to load these results synchronously, and not to load
	 * more than X results
	 */
	public boolean isLoadSynchronous() {
		return myLoadSynchronous;
	}

	/**
	 * If set, tells the server to load these results synchronously, and not to load
	 * more than X results
	 */
	public SearchParameterMap setLoadSynchronous(boolean theLoadSynchronous) {
		myLoadSynchronous = theLoadSynchronous;
		return this;
	}

	/**
	 * If set, tells the server to use an Elasticsearch query to generate a list of
	 * Resource IDs for the LastN operation
	 */
	public boolean isLastN() {
		return myLastN;
	}

	/**
	 * If set, tells the server to use an Elasticsearch query to generate a list of
	 * Resource IDs for the LastN operation
	 */
	public SearchParameterMap setLastN(boolean theLastN) {
		myLastN = theLastN;
		return this;
	}

	/**
	 * If set, tells the server the maximum number of observations to return for each
	 * observation code in the result set of a lastn operation
	 */
	public Integer getLastNMax() {
		return myLastNMax;
	}

	/**
	 * If set, tells the server the maximum number of observations to return for each
	 * observation code in the result set of a lastn operation
	 */
	public SearchParameterMap setLastNMax(Integer theLastNMax) {
		myLastNMax = theLastNMax;
		return this;
	}

	/**
	 * @deprecated Use {@link #toNormalizedQueryString()} instead.
	 */
	@Deprecated(since = "8.6.0", forRemoval = true)
	public String toNormalizedQueryString(FhirContext theCtx) {
		return toNormalizedQueryString();
	}

	/**
	 * This method creates a URL query string representation of the parameters in this
	 * object, excluding the part before the parameters, e.g.
	 * <p>
	 * <code>?name=smith&amp;_sort=Patient:family</code>
	 * </p>
	 * <p>
	 * This method <b>excludes</b> the <code>_count</code> parameter,
	 * as it doesn't affect the substance of the results returned
	 * </p>
	 */
	public String toNormalizedQueryString() {
		StringBuilder b = new StringBuilder();

		ArrayList<String> keys = new ArrayList<>(keySet());
		Collections.sort(keys);
		for (String nextKey : keys) {

			List<List<IQueryParameterType>> nextValuesAndsIn = get(nextKey);
			List<List<IQueryParameterType>> nextValuesAndsOut = new ArrayList<>();

			for (List<? extends IQueryParameterType> nextValuesAndIn : nextValuesAndsIn) {

				List<IQueryParameterType> nextValuesOrsOut = new ArrayList<>(nextValuesAndIn);

				nextValuesOrsOut.sort(new QueryParameterTypeComparator());

				if (!nextValuesOrsOut.isEmpty()) {
					nextValuesAndsOut.add(nextValuesOrsOut);
				}
			} // for AND

			nextValuesAndsOut.sort(new QueryParameterOrComparator());

			for (List<IQueryParameterType> nextValuesAnd : nextValuesAndsOut) {
				addUrlParamSeparator(b);
				IQueryParameterType firstValue = nextValuesAnd.get(0);
				b.append(UrlUtil.escapeUrlParam(nextKey));

				if (firstValue.getMissing() != null) {
					b.append(Constants.PARAMQUALIFIER_MISSING);
					b.append('=');
					if (firstValue.getMissing()) {
						b.append(Constants.PARAMQUALIFIER_MISSING_TRUE);
					} else {
						b.append(Constants.PARAMQUALIFIER_MISSING_FALSE);
					}
					continue;
				}

				if (isNotBlank(firstValue.getQueryParameterQualifier())) {
					b.append(firstValue.getQueryParameterQualifier());
				}

				b.append('=');

				for (int i = 0; i < nextValuesAnd.size(); i++) {
					IQueryParameterType nextValueOr = nextValuesAnd.get(i);
					if (i > 0) {
						b.append(',');
					}
					String valueAsQueryToken = nextValueOr.getValueAsQueryToken();
					valueAsQueryToken = defaultString(valueAsQueryToken);
					b.append(UrlUtil.escapeUrlParam(valueAsQueryToken, false));
				}
			}
		} // for keys

		SortSpec sort = getSort();
		boolean first = true;
		while (sort != null) {

			if (isNotBlank(sort.getParamName())) {
				if (first) {
					addUrlParamSeparator(b);
					b.append(Constants.PARAM_SORT);
					b.append('=');
					first = false;
				} else {
					b.append(',');
				}
				if (sort.getOrder() == SortOrderEnum.DESC) {
					b.append('-');
				}
				b.append(sort.getParamName());
			}

			Validate.isTrue(sort != sort.getChain()); // just in case, shouldn't happen
			sort = sort.getChain();
		}

		if (hasIncludes()) {
			addUrlIncludeParams(b, Constants.PARAM_INCLUDE, getIncludes());
		}
		addUrlIncludeParams(b, Constants.PARAM_REVINCLUDE, getRevIncludes());

		if (myLastUpdatedAndList != null) {
			DateRangeParam lastUpdated = toLastUpdatedRangeOrNull();
			if (lastUpdated != null) {
				DateParam lb = lastUpdated.getLowerBound();
				DateParam ub = lastUpdated.getUpperBound();

				if (isNotEqualsComparator(lb, ub)) {
					addLastUpdateParam(b, NOT_EQUAL, lastUpdated.getLowerBound());
				} else {
					addLastUpdateParam(b, GREATERTHAN_OR_EQUALS, lb);
					addLastUpdateParam(b, LESSTHAN_OR_EQUALS, ub);
				}
			} else {
				// Values keep their original prefixes, and are sorted so that the output doesn't depend on order
				for (String group : getLastUpdatedCanonicalForm()) {
					addUrlParamSeparator(b);
					b.append(Constants.PARAM_LASTUPDATED);
					b.append('=');
					b.append(group);
				}
			}
		}

		if (getCompartmentLastUpdated() != null) {
			DateParam ccLb = getCompartmentLastUpdated().getLowerBound();
			DateParam ccUb = getCompartmentLastUpdated().getUpperBound();

			if (isNotEqualsComparator(ccLb, ccUb)) {
				addCompartmentLastUpdatedParam(
						b, NOT_EQUAL, getCompartmentLastUpdated().getLowerBound());
			} else {
				addCompartmentLastUpdatedParam(b, GREATERTHAN_OR_EQUALS, ccLb);
				addCompartmentLastUpdatedParam(b, LESSTHAN_OR_EQUALS, ccUb);
			}
		}

		if (getCount() != null) {
			addUrlParamSeparator(b);
			b.append(Constants.PARAM_COUNT);
			b.append('=');
			b.append(getCount());
		}

		if (getOffset() != null) {
			addUrlParamSeparator(b);
			b.append(Constants.PARAM_OFFSET);
			b.append('=');
			b.append(getOffset());
		}

		// Summary mode (_summary)
		if (getSummaryMode() != null) {
			addUrlParamSeparator(b);
			b.append(Constants.PARAM_SUMMARY);
			b.append('=');
			b.append(getSummaryMode().getCode());
		}

		// Search count mode (_total)
		if (getSearchTotalMode() != null) {
			addUrlParamSeparator(b);
			b.append(Constants.PARAM_SEARCH_TOTAL_MODE);
			b.append('=');
			b.append(getSearchTotalMode().getCode());
		}

		// Contained mode
		// For some reason, instead of null here, we default to false. That said, ommitting it is identical to setting
		// it to false.
		if (getSearchContainedMode() != SearchContainedModeEnum.FALSE) {
			addUrlParamSeparator(b);
			b.append(Constants.PARAM_CONTAINED);
			b.append("=");
			b.append(getSearchContainedMode().getCode());
		}

		if (getSearchIncludeDeletedMode() != null) {
			addUrlParamSeparator(b);
			b.append(Constants.PARAM_INCLUDE_DELETED);
			b.append("=");
			b.append(getSearchIncludeDeletedMode().getCode());
		}

		if (b.isEmpty()) {
			b.append('?');
		}

		return b.toString();
	}

	/**
	 * Configure a query with the current settings.
	 * This is an adaptor method to allow this class to be used in IRepository.search().
	 * with repository implementations that don't use SearchParameterMap.
	 * @param theBuilder the builder to configure with our settings
	 */
	@Override
	public void contributeToQuery(IRepositoryRestQueryBuilder theBuilder) {
		SearchParameterMapContributor.contributeToBuilder(this, theBuilder);
	}

	private boolean isNotEqualsComparator(DateParam theLowerBound, DateParam theUpperBound) {
		return theLowerBound != null
				&& theUpperBound != null
				&& NOT_EQUAL.equals(theLowerBound.getPrefix())
				&& NOT_EQUAL.equals(theUpperBound.getPrefix());
	}

	/**
	 * @since 5.5.0
	 */
	public boolean hasIncludes() {
		return myIncludes != null && !myIncludes.isEmpty();
	}

	/**
	 * @since 6.2.0
	 */
	public boolean hasRevIncludes() {
		return myRevIncludes != null && !myRevIncludes.isEmpty();
	}

	@Override
	public String toString() {
		ToStringBuilder b = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		if (!isEmpty()) {
			b.append("params", mySearchParameterMap);
		}
		if (!getIncludes().isEmpty()) {
			b.append("includes", getIncludes());
		}
		return b.toString();
	}

	public void clean() {
		for (Iterator<Map.Entry<String, List<List<IQueryParameterType>>>> iterator =
						this.entrySet().iterator();
				iterator.hasNext(); ) {
			Map.Entry<String, List<List<IQueryParameterType>>> nextParamEntry = iterator.next();
			String nextParamName = nextParamEntry.getKey();
			List<List<IQueryParameterType>> andOrParams = nextParamEntry.getValue();
			cleanParameter(nextParamName, andOrParams);

			if (andOrParams.isEmpty()) {
				iterator.remove();
			}
		}
	}

	/*
	 * Given a particular named parameter, e.g. `name`, iterate over AndOrParams and remove any which are empty.
	 */
	private void cleanParameter(String theParamName, List<List<IQueryParameterType>> theAndOrParams) {
		theAndOrParams.forEach(orList -> {
			List<IQueryParameterType> emptyParameters = orList.stream()
					.filter(nextOr -> nextOr.getMissing() == null)
					.filter(nextOr -> nextOr instanceof QuantityParam)
					.filter(nextOr -> isBlank(((QuantityParam) nextOr).getValueAsString()))
					.toList();

			ourLog.debug("Ignoring empty parameter: {}", theParamName);
			orList.removeAll(emptyParameters);
		});
		theAndOrParams.removeIf(List::isEmpty);
	}

	public QuantityParam getNearDistanceParam() {
		return myNearDistanceParam;
	}

	public void setNearDistanceParam(QuantityParam theQuantityParam) {
		myNearDistanceParam = theQuantityParam;
	}

	public boolean isWantOnlyCount() {
		return SummaryEnum.COUNT.equals(getSummaryMode()) || INTEGER_0.equals(getCount());
	}

	public boolean isDeleteExpunge() {
		return myDeleteExpunge;
	}

	public SearchParameterMap setDeleteExpunge(boolean theDeleteExpunge) {
		myDeleteExpunge = theDeleteExpunge;
		return this;
	}

	public List<List<IQueryParameterType>> get(String theName) {
		return mySearchParameterMap.get(theName);
	}

	public void put(String theName, List<List<IQueryParameterType>> theParams) {
		mySearchParameterMap.put(theName, theParams);
	}

	public boolean containsKey(String theName) {
		return mySearchParameterMap.containsKey(theName);
	}

	public Set<String> keySet() {
		return mySearchParameterMap.keySet();
	}

	public boolean isEmpty() {
		return mySearchParameterMap.isEmpty();
	}

	// Wrapper methods

	public Set<Map.Entry<String, List<List<IQueryParameterType>>>> entrySet() {
		return mySearchParameterMap.entrySet();
	}

	public List<List<IQueryParameterType>> remove(String theName) {
		return mySearchParameterMap.remove(theName);
	}

	/**
	 * Variant of removeByNameAndModifier for unmodified params.
	 *
	 * @param theName the query parameter key
	 * @return an And/Or List of Query Parameters matching the name with no modifier.
	 */
	public List<List<IQueryParameterType>> removeByNameUnmodified(String theName) {
		return this.removeByNameAndModifier(theName, "");
	}

	/**
	 * Given a search parameter name and modifier (e.g. :text),
	 * get and remove all Search Parameters matching this name and modifier
	 *
	 * @param theName     the query parameter key
	 * @param theModifier the qualifier you want to remove - nullable for unmodified params.
	 * @return an And/Or List of Query Parameters matching the qualifier.
	 */
	public List<List<IQueryParameterType>> removeByNameAndModifier(String theName, String theModifier) {
		theModifier = Objects.toString(theModifier, "");

		List<List<IQueryParameterType>> remainderParameters = new ArrayList<>();
		List<List<IQueryParameterType>> matchingParameters = new ArrayList<>();

		// pull all of them out, partition by match against the qualifier
		List<List<IQueryParameterType>> andList = mySearchParameterMap.remove(theName);
		if (andList != null) {
			for (List<IQueryParameterType> orList : andList) {
				if (!orList.isEmpty()
						&& Objects.toString(orList.get(0).getQueryParameterQualifier(), "")
								.equals(theModifier)) {
					matchingParameters.add(orList);
				} else {
					remainderParameters.add(orList);
				}
			}
		}

		// put the unmatched back in.
		if (!remainderParameters.isEmpty()) {
			mySearchParameterMap.put(theName, remainderParameters);
		}
		return matchingParameters;
	}

	public List<List<IQueryParameterType>> removeByNameAndModifier(
			String theName, @Nonnull TokenParamModifier theModifier) {
		return removeByNameAndModifier(theName, theModifier.getValue());
	}

	/**
	 * For each search parameter in the map, extract any which have the given qualifier.
	 * e.g. Take the url: {@code Observation?code:text=abc&code=123&code:text=def&reason:text=somereason}
	 * <p>
	 * If we call this function with `:text`, it will return a map that looks like:
	 * <p>
	 * code -> [[code:text=abc], [code:text=def]]
	 * reason -> [[reason:text=somereason]]
	 * <p>
	 * and the remaining search parameters in the map will be:
	 * <p>
	 * code -> [[code=123]]
	 */
	public Map<String, List<List<IQueryParameterType>>> removeByQualifier(String theQualifier) {

		Map<String, List<List<IQueryParameterType>>> retVal = new HashMap<>();
		Set<String> parameterNames = mySearchParameterMap.keySet();
		for (String parameterName : parameterNames) {
			List<List<IQueryParameterType>> paramsWithQualifier = removeByNameAndModifier(parameterName, theQualifier);
			retVal.put(parameterName, paramsWithQualifier);
		}

		return retVal;
	}

	public Map<String, List<List<IQueryParameterType>>> removeByQualifier(@Nonnull TokenParamModifier theModifier) {
		return removeByQualifier(theModifier.getValue());
	}

	public int size() {
		return mySearchParameterMap.size();
	}

	public SearchContainedModeEnum getSearchContainedMode() {
		return mySearchContainedMode;
	}

	public void setSearchContainedMode(SearchContainedModeEnum theSearchContainedMode) {
		this.mySearchContainedMode = Objects.requireNonNullElse(theSearchContainedMode, SearchContainedModeEnum.FALSE);
	}

	public SearchIncludeDeletedEnum getSearchIncludeDeletedMode() {
		return mySearchIncludeDeletedMode;
	}

	public void setSearchIncludeDeletedMode(SearchIncludeDeletedEnum theSearchIncludeDeletedMode) {
		this.mySearchIncludeDeletedMode = theSearchIncludeDeletedMode;
	}

	/**
	 * Returns true if {@link #getOffset()} and {@link #getCount()} both return a non null response
	 *
	 * @since 5.5.0
	 */
	public boolean isOffsetQuery() {
		return getOffset() != null && getCount() != null;
	}

	public enum EverythingModeEnum {
		/*
		 * Don't reorder! We rely on the ordinals
		 */
		ENCOUNTER_INSTANCE(false, true, true),
		ENCOUNTER_TYPE(false, true, false),
		PATIENT_INSTANCE(true, false, true),
		PATIENT_TYPE(true, false, false);

		private final boolean myEncounter;

		private final boolean myInstance;

		private final boolean myPatient;

		EverythingModeEnum(boolean thePatient, boolean theEncounter, boolean theInstance) {
			assert thePatient ^ theEncounter;
			myPatient = thePatient;
			myEncounter = theEncounter;
			myInstance = theInstance;
		}

		public boolean isEncounter() {
			return myEncounter;
		}

		public boolean isInstance() {
			return myInstance;
		}

		public boolean isPatient() {
			return myPatient;
		}
	}

	static int compare(IQueryParameterType theO1, IQueryParameterType theO2) {
		CompareToBuilder b = new CompareToBuilder();
		b.append(theO1.getMissing(), theO2.getMissing());
		b.append(theO1.getQueryParameterQualifier(), theO2.getQueryParameterQualifier());
		if (b.toComparison() == 0) {
			b.append(theO1.getValueAsQueryToken(), theO2.getValueAsQueryToken());
		}

		return b.toComparison();
	}

	public static SearchParameterMap newSynchronous() {
		SearchParameterMap retVal = new SearchParameterMap();
		retVal.setLoadSynchronous(true);
		return retVal;
	}

	public static SearchParameterMap newSynchronous(String theName, IQueryParameterType theParam) {
		SearchParameterMap retVal = new SearchParameterMap();
		retVal.setLoadSynchronous(true);
		retVal.add(theName, theParam);
		return retVal;
	}

	public static class IncludeComparator implements Comparator<Include> {

		@Override
		public int compare(Include theO1, Include theO2) {
			int retVal = Strings.CS.compare(theO1.getParamType(), theO2.getParamType());
			if (retVal == 0) {
				retVal = Strings.CS.compare(theO1.getParamName(), theO2.getParamName());
			}
			if (retVal == 0) {
				retVal = Strings.CS.compare(theO1.getParamTargetType(), theO2.getParamTargetType());
			}
			return retVal;
		}
	}

	public static class QueryParameterOrComparator implements Comparator<List<IQueryParameterType>> {

		/**
		 * Constructor
		 */
		QueryParameterOrComparator() {
			super();
		}

		@Override
		public int compare(List<IQueryParameterType> theO1, List<IQueryParameterType> theO2) {
			// These lists will never be empty
			return SearchParameterMap.compare(theO1.get(0), theO2.get(0));
		}
	}

	public static class QueryParameterTypeComparator implements Comparator<IQueryParameterType> {

		/**
		 * Constructor
		 */
		QueryParameterTypeComparator() {
			super();
		}

		@Override
		public int compare(IQueryParameterType theO1, IQueryParameterType theO2) {
			return SearchParameterMap.compare(theO1, theO2);
		}
	}

	public List<SortSpec> getAllChainsInOrder() {
		final List<SortSpec> allChainsInOrder = new ArrayList<>();
		for (SortSpec sortSpec = getSort(); sortSpec != null; sortSpec = sortSpec.getChain()) {
			allChainsInOrder.add(sortSpec);
		}

		return Collections.unmodifiableList(allChainsInOrder);
	}

	@Override
	public boolean equals(Object theO) {
		if (!(theO instanceof SearchParameterMap that)) return false;
		return myLoadSynchronous == that.myLoadSynchronous
				&& myLastN == that.myLastN
				&& myDeleteExpunge == that.myDeleteExpunge
				&& Objects.equals(mySearchParameterMap, that.mySearchParameterMap)
				&& Objects.equals(myCount, that.myCount)
				&& Objects.equals(myOffset, that.myOffset)
				&& myEverythingMode == that.myEverythingMode
				&& Objects.equals(myIncludes, that.myIncludes)
				&& Objects.equals(getLastUpdatedCanonicalForm(), that.getLastUpdatedCanonicalForm())
				&& Objects.equals(myCompartmentLastUpdated, that.myCompartmentLastUpdated)
				&& Objects.equals(myLoadSynchronousUpTo, that.myLoadSynchronousUpTo)
				&& Objects.equals(myRevIncludes, that.myRevIncludes)
				&& Objects.equals(mySort, that.mySort)
				&& mySummaryMode == that.mySummaryMode
				&& mySearchTotalMode == that.mySearchTotalMode
				&& Objects.equals(myNearDistanceParam, that.myNearDistanceParam)
				&& Objects.equals(myLastNMax, that.myLastNMax)
				&& mySearchContainedMode == that.mySearchContainedMode
				&& mySearchIncludeDeletedMode == that.mySearchIncludeDeletedMode;
	}

	@Override
	public int hashCode() {
		return Objects.hash(
				mySearchParameterMap,
				myCount,
				myOffset,
				myEverythingMode,
				myIncludes,
				getLastUpdatedCanonicalForm(),
				myCompartmentLastUpdated,
				myLoadSynchronous,
				myLoadSynchronousUpTo,
				myRevIncludes,
				mySort,
				mySummaryMode,
				mySearchTotalMode,
				myNearDistanceParam,
				myLastN,
				myLastNMax,
				myDeleteExpunge,
				mySearchContainedMode,
				mySearchIncludeDeletedMode);
	}
}
