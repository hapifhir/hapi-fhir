package ca.uhn.fhir.jpa.searchparam;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IQueryParameterAnd;
import ca.uhn.fhir.model.api.IQueryParameterOr;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.SearchContainedModeEnum;
import ca.uhn.fhir.rest.api.SearchTotalModeEnum;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.param.QuantityParam;
import ca.uhn.fhir.rest.param.TokenParamModifier;
import ca.uhn.fhir.util.ObjectUtil;
import ca.uhn.fhir.util.UrlUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static ca.uhn.fhir.rest.param.ParamPrefixEnum.*;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/*
 * #%L
 * HAPI FHIR Search Parameters
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

public class SearchParameterMap implements Serializable {
	public static final Integer INTEGER_0 = 0;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchParameterMap.class);
	private static final long serialVersionUID = 1L;
	private final HashMap<String, List<List<IQueryParameterType>>> mySearchParameterMap = new LinkedHashMap<>();
	private Integer myCount;
	private Integer myOffset;
	private EverythingModeEnum myEverythingMode = null;
	private Set<Include> myIncludes;
	private DateRangeParam myLastUpdated;
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

	/**
	 * Constructor
	 */
	public SearchParameterMap() {
		super();
	}

	/**
	 * Creates and returns a copy of this map
	 */
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
		map.setLastUpdated(getLastUpdated());
		map.setLoadSynchronous(isLoadSynchronous());
		map.setNearDistanceParam(getNearDistanceParam());
		map.setLoadSynchronousUpTo(getLoadSynchronousUpTo());
		map.setOffset(getOffset());
		map.setSearchContainedMode(getSearchContainedMode());

		for (Map.Entry<String, List<List<IQueryParameterType>>> entry : mySearchParameterMap.entrySet()) {
			List<List<IQueryParameterType>> andParams = entry.getValue();
			List<List<IQueryParameterType>> newAndParams = new ArrayList<>();
			for(List<IQueryParameterType> orParams: andParams) {
				List<IQueryParameterType> newOrParams = new ArrayList<>(orParams);
				newAndParams.add(newOrParams);
			}
			map.put(entry.getKey(), newAndParams);
		}



		return map;
	}

	/**
	 * Constructor
	 */
	public SearchParameterMap(String theName, IQueryParameterType theParam) {
		add(theName, theParam);
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

	public SearchParameterMap add(String theName, IQueryParameterAnd<?> theAnd) {
		if (theAnd == null) {
			return this;
		}
		if (!containsKey(theName)) {
			put(theName, new ArrayList<>());
		}

		for (IQueryParameterOr<?> next : theAnd.getValuesAsQueryTokens()) {
			if (next == null) {
				continue;
			}
			get(theName).add((List<IQueryParameterType>) next.getValuesAsQueryTokens());
		}
		return this;
	}

	public SearchParameterMap add(String theName, IQueryParameterOr<?> theOr) {
		if (theOr == null) {
			return this;
		}
		if (!containsKey(theName)) {
			put(theName, new ArrayList<>());
		}

		get(theName).add((List<IQueryParameterType>) theOr.getValuesAsQueryTokens());
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
		if (!containsKey(theName)) {
			put(theName, new ArrayList<>());
		}
		ArrayList<IQueryParameterType> list = new ArrayList<>();
		list.add(theParam);
		get(theName).add(list);

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
			b.append('=');
			b.append(UrlUtil.escapeUrlParam(nextInclude.getParamType()));
			b.append(':');
			b.append(UrlUtil.escapeUrlParam(nextInclude.getParamName()));
			if (isNotBlank(nextInclude.getParamTargetType())) {
				b.append(':');
				b.append(nextInclude.getParamTargetType());
			}
		}
	}

	private void addUrlParamSeparator(StringBuilder theB) {
		if (theB.length() == 0) {
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
	 * Returns null if there is no last updated value
	 */
	public DateRangeParam getLastUpdated() {
		if (myLastUpdated != null) {
			if (myLastUpdated.isEmpty()) {
				myLastUpdated = null;
			}
		}
		return myLastUpdated;
	}

	public void setLastUpdated(DateRangeParam theLastUpdated) {
		myLastUpdated = theLastUpdated;
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
	 * This will only return true if all parameters have no modifier of any kind
	 */
	public boolean isAllParametersHaveNoModifier() {
		for (List<List<IQueryParameterType>> nextParamName : values()) {
			for (List<IQueryParameterType> nextAnd : nextParamName) {
				for (IQueryParameterType nextOr : nextAnd) {
					if (isNotBlank(nextOr.getQueryParameterQualifier())) {
						return false;
					}
				}
			}
		}
		return true;
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
	public String toNormalizedQueryString(FhirContext theCtx) {
		StringBuilder b = new StringBuilder();

		ArrayList<String> keys = new ArrayList<>(keySet());
		Collections.sort(keys);
		for (String nextKey : keys) {

			List<List<IQueryParameterType>> nextValuesAndsIn = get(nextKey);
			List<List<IQueryParameterType>> nextValuesAndsOut = new ArrayList<>();

			for (List<? extends IQueryParameterType> nextValuesAndIn : nextValuesAndsIn) {

				List<IQueryParameterType> nextValuesOrsOut = new ArrayList<>();

				nextValuesOrsOut.addAll(nextValuesAndIn);

				nextValuesOrsOut.sort(new QueryParameterTypeComparator(theCtx));

				if (nextValuesOrsOut.size() > 0) {
					nextValuesAndsOut.add(nextValuesOrsOut);
				}

			} // for AND

			nextValuesAndsOut.sort(new QueryParameterOrComparator(theCtx));

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
					String valueAsQueryToken = nextValueOr.getValueAsQueryToken(theCtx);
					b.append(UrlUtil.escapeUrlParam(valueAsQueryToken));
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

		if (getLastUpdated() != null) {
			DateParam lb = getLastUpdated().getLowerBound();
			DateParam ub = getLastUpdated().getUpperBound();

			if (isNotEqualsComparator(lb, ub)) {
				addLastUpdateParam(b, NOT_EQUAL, getLastUpdated().getLowerBound());
			} else {
				addLastUpdateParam(b, GREATERTHAN_OR_EQUALS, lb);
				addLastUpdateParam(b, LESSTHAN_OR_EQUALS, ub);
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

		//Contained mode
		//For some reason, instead of null here, we default to false. That said, ommitting it is identical to setting it to false.
		if (getSearchContainedMode() != SearchContainedModeEnum.FALSE) {
			addUrlParamSeparator(b);
			b.append(Constants.PARAM_CONTAINED);
			b.append("=");
			b.append(getSearchContainedMode().getCode());
		}

		if (b.length() == 0) {
			b.append('?');
		}

		return b.toString();
	}

	private boolean isNotEqualsComparator(DateParam theLowerBound, DateParam theUpperBound) {
		return theLowerBound != null && theUpperBound != null && theLowerBound.getPrefix().equals(NOT_EQUAL) && theUpperBound.getPrefix().equals(NOT_EQUAL);
	}

	/**
	 * @since 5.5.0
	 */
	public boolean hasIncludes() {
		return myIncludes != null && !myIncludes.isEmpty();
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
		for (Map.Entry<String, List<List<IQueryParameterType>>> nextParamEntry : this.entrySet()) {
			String nextParamName = nextParamEntry.getKey();
			List<List<IQueryParameterType>> andOrParams = nextParamEntry.getValue();
			cleanParameter(nextParamName, andOrParams);
		}
	}

	/*
	 * Given a particular named parameter, e.g. `name`, iterate over AndOrParams and remove any which are empty.
	 */
	private void cleanParameter(String theParamName, List<List<IQueryParameterType>> theAndOrParams) {
		theAndOrParams
			.forEach(
				orList -> {
					List<IQueryParameterType> emptyParameters = orList.stream()
						.filter(nextOr -> nextOr.getMissing() == null)
						.filter(nextOr -> nextOr instanceof QuantityParam)
						.filter(nextOr -> isBlank(((QuantityParam) nextOr).getValueAsString()))
						.collect(Collectors.toList());

					ourLog.debug("Ignoring empty parameter: {}", theParamName);
					orList.removeAll(emptyParameters);
				}
			);
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
	 * @param theName the query parameter key
	 * @param theModifier the qualifier you want to remove - nullable for unmodified params.
	 *
	 * @return an And/Or List of Query Parameters matching the qualifier.
	 */
	public List<List<IQueryParameterType>> removeByNameAndModifier(String theName, String theModifier) {
		theModifier = StringUtils.defaultString(theModifier, "");

		List<List<IQueryParameterType>> remainderParameters = new ArrayList<>();
		List<List<IQueryParameterType>> matchingParameters = new ArrayList<>();

		// pull all of them out, partition by match against the qualifier
		List<List<IQueryParameterType>> andList = mySearchParameterMap.remove(theName);
		if (andList != null) {
			for (List<IQueryParameterType> orList : andList) {
				if (!orList.isEmpty() &&
					StringUtils.defaultString(orList.get(0).getQueryParameterQualifier(), "")
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

	public List<List<IQueryParameterType>> removeByNameAndModifier(String theName, @Nonnull TokenParamModifier theModifier) {
		return removeByNameAndModifier(theName, theModifier.getValue());
	}

	/**
	 * For each search parameter in the map, extract any which have the given qualifier.
	 * e.g. Take the url: Observation?code:text=abc&code=123&code:text=def&reason:text=somereason
	 *
	 * If we call this function with `:text`, it will return a map that looks like:
	 *
	 * code -> [[code:text=abc], [code:text=def]]
	 * reason -> [[reason:text=somereason]]
	 *
	 * and the remaining search parameters in the map will be:
	 *
	 * code -> [[code=123]]
	 *
	 * @param theQualifier
	 * @return
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
		if (theSearchContainedMode == null) {
			mySearchContainedMode = SearchContainedModeEnum.FALSE;
		} else {
			this.mySearchContainedMode = theSearchContainedMode;
		}
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

	public static class IncludeComparator implements Comparator<Include> {

		@Override
		public int compare(Include theO1, Include theO2) {
			int retVal = StringUtils.compare(theO1.getParamType(), theO2.getParamType());
			if (retVal == 0) {
				retVal = StringUtils.compare(theO1.getParamName(), theO2.getParamName());
			}
			if (retVal == 0) {
				retVal = StringUtils.compare(theO1.getParamTargetType(), theO2.getParamTargetType());
			}
			return retVal;
		}

	}

	public static class QueryParameterOrComparator implements Comparator<List<IQueryParameterType>> {
		private final FhirContext myCtx;

		QueryParameterOrComparator(FhirContext theCtx) {
			myCtx = theCtx;
		}

		@Override
		public int compare(List<IQueryParameterType> theO1, List<IQueryParameterType> theO2) {
			// These lists will never be empty
			return SearchParameterMap.compare(myCtx, theO1.get(0), theO2.get(0));
		}

	}

	public static class QueryParameterTypeComparator implements Comparator<IQueryParameterType> {

		private final FhirContext myCtx;

		QueryParameterTypeComparator(FhirContext theCtx) {
			myCtx = theCtx;
		}

		@Override
		public int compare(IQueryParameterType theO1, IQueryParameterType theO2) {
			return SearchParameterMap.compare(myCtx, theO1, theO2);
		}

	}

	private static int compare(FhirContext theCtx, IQueryParameterType theO1, IQueryParameterType theO2) {
		int retVal;
		if (theO1.getMissing() == null && theO2.getMissing() == null) {
			retVal = 0;
		} else if (theO1.getMissing() == null) {
			retVal = -1;
		} else if (theO2.getMissing() == null) {
			retVal = 1;
		} else if (ObjectUtil.equals(theO1.getMissing(), theO2.getMissing())) {
			retVal = 0;
		} else {
			if (theO1.getMissing()) {
				retVal = 1;
			} else {
				retVal = -1;
			}
		}

		if (retVal == 0) {
			String q1 = theO1.getQueryParameterQualifier();
			String q2 = theO2.getQueryParameterQualifier();
			retVal = StringUtils.compare(q1, q2);
		}

		if (retVal == 0) {
			String v1 = theO1.getValueAsQueryToken(theCtx);
			String v2 = theO2.getValueAsQueryToken(theCtx);
			retVal = StringUtils.compare(v1, v2);
		}
		return retVal;
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


}
