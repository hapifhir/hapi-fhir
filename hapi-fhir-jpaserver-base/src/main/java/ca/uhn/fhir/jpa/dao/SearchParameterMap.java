package ca.uhn.fhir.jpa.dao;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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
import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.rest.api.*;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.util.ObjectUtil;
import ca.uhn.fhir.util.UrlUtil;

public class SearchParameterMap extends LinkedHashMap<String, List<List<? extends IQueryParameterType>>> {

	private static final long serialVersionUID = 1L;

	private Integer myCount;
	private EverythingModeEnum myEverythingMode = null;
	private Set<Include> myIncludes;
	private DateRangeParam myLastUpdated;
	private boolean myLoadSynchronous;
	private Integer myLoadSynchronousUpTo;
	private Set<Include> myRevIncludes;
	private SortSpec mySort;
	
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

	public SearchParameterMap add(String theName, DateParam theDateParam) {
		add(theName, (IQueryParameterOr<?>) theDateParam);
		return this;
	}

	public void add(String theName, IQueryParameterAnd<?> theAnd) {
		if (theAnd == null) {
			return;
		}
		if (!containsKey(theName)) {
			put(theName, new ArrayList<List<? extends IQueryParameterType>>());
		}

		for (IQueryParameterOr<?> next : theAnd.getValuesAsQueryTokens()) {
			if (next == null) {
				continue;
			}
			get(theName).add(next.getValuesAsQueryTokens());
		}
	}

	public void add(String theName, IQueryParameterOr<?> theOr) {
		if (theOr == null) {
			return;
		}
		if (!containsKey(theName)) {
			put(theName, new ArrayList<List<? extends IQueryParameterType>>());
		}

		get(theName).add(theOr.getValuesAsQueryTokens());
	}

	public SearchParameterMap add(String theName, IQueryParameterType theParam) {
		assert !Constants.PARAM_LASTUPDATED.equals(theName); // this has it's own field in the map

		if (theParam == null) {
			return this;
		}
		if (!containsKey(theName)) {
			put(theName, new ArrayList<List<? extends IQueryParameterType>>());
		}
		ArrayList<IQueryParameterType> list = new ArrayList<IQueryParameterType>();
		list.add(theParam);
		get(theName).add(list);

		return this;
	}

	public void addInclude(Include theInclude) {
		getIncludes().add(theInclude);
	}

	private void addLastUpdateParam(StringBuilder b, DateParam date) {
		if (date != null && isNotBlank(date.getValueAsString())) {
			addUrlParamSeparator(b);
			b.append(Constants.PARAM_LASTUPDATED);
			b.append('=');
			b.append(date.getValueAsString());
		}
	}

	public void addRevInclude(Include theInclude) {
		getRevIncludes().add(theInclude);
	}

	private void addUrlIncludeParams(StringBuilder b, String paramName, Set<Include> theList) {
		ArrayList<Include> list = new ArrayList<Include>(theList);
		
		Collections.sort(list, new IncludeComparator());
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

	public EverythingModeEnum getEverythingMode() {
		return myEverythingMode;
	}

	public Set<Include> getIncludes() {
		if (myIncludes == null) {
			myIncludes = new HashSet<Include>();
		}
		return myIncludes;
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

	/**
	 * Returns null if there is no last updated value, and removes the lastupdated
	 * value from this map
	 */
	public DateRangeParam getLastUpdatedAndRemove() {
		DateRangeParam retVal = getLastUpdated();
		myLastUpdated = null;
		return retVal;
	}

	/**
	 * If set, tells the server to load these results synchronously, and not to load
	 * more than X results
	 */
	public Integer getLoadSynchronousUpTo() {
		return myLoadSynchronousUpTo;
	}

	public Set<Include> getRevIncludes() {
		if (myRevIncludes == null) {
			myRevIncludes = new HashSet<>();
		}
		return myRevIncludes;
	}

	public SortSpec getSort() {
		return mySort;
	}

	/**
	 * This will only return true if all parameters have no modifier of any kind
	 */
	public boolean isAllParametersHaveNoModifier() {
		for (List<List<? extends IQueryParameterType>> nextParamName : values()) {
			for (List<? extends IQueryParameterType> nextAnd : nextParamName) {
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

	public void setCount(Integer theCount) {
		myCount = theCount;
	}

	public void setEverythingMode(EverythingModeEnum theConsolidateMatches) {
		myEverythingMode = theConsolidateMatches;
	}

	public void setIncludes(Set<Include> theIncludes) {
		myIncludes = theIncludes;
	}

	public void setLastUpdated(DateRangeParam theLastUpdated) {
		myLastUpdated = theLastUpdated;
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

	public void setRevIncludes(Set<Include> theRevIncludes) {
		myRevIncludes = theRevIncludes;
	}

	public void setSort(SortSpec theSort) {
		mySort = theSort;
	}

	public String toNormalizedQueryString(FhirContext theCtx) {
		StringBuilder b = new StringBuilder();

		ArrayList<String> keys = new ArrayList<String>(keySet());
		Collections.sort(keys);
		for (String nextKey : keys) {

			List<List<? extends IQueryParameterType>> nextValuesAndsIn = get(nextKey);
			List<List<IQueryParameterType>> nextValuesAndsOut = new ArrayList<List<IQueryParameterType>>();

			for (List<? extends IQueryParameterType> nextValuesAndIn : nextValuesAndsIn) {

				List<IQueryParameterType> nextValuesOrsOut = new ArrayList<IQueryParameterType>();
				for (IQueryParameterType nextValueOrIn : nextValuesAndIn) {
					if (nextValueOrIn.getMissing() != null || isNotBlank(nextValueOrIn.getValueAsQueryToken(theCtx))) {
						nextValuesOrsOut.add(nextValueOrIn);
					}
				}
				
				Collections.sort(nextValuesOrsOut, new QueryParameterTypeComparator(theCtx));

				if (nextValuesOrsOut.size() > 0) {
					nextValuesAndsOut.add(nextValuesOrsOut);
				}

			} // for AND

			Collections.sort(nextValuesAndsOut, new QueryParameterOrComparator(theCtx));
			
			for (List<IQueryParameterType> nextValuesAnd : nextValuesAndsOut) {
				addUrlParamSeparator(b);
				IQueryParameterType firstValue = nextValuesAnd.get(0);
				b.append(UrlUtil.escapeUrlParam(nextKey));

				if (firstValue.getMissing() != null) {
					b.append(Constants.PARAMQUALIFIER_MISSING);
					b.append('=');
					if (firstValue.getMissing()) {
						b.append(Constants.PARAMQUALIFIER_MISSING_TRUE);
					}else {
						b.append(Constants.PARAMQUALIFIER_MISSING_FALSE);
					}
					continue;
				}
				
				if (isNotBlank(firstValue.getQueryParameterQualifier())){
					b.append(firstValue.getQueryParameterQualifier());
				}
				
				b.append('=');
				
				for (int i = 0; i < nextValuesAnd.size(); i++) {
					IQueryParameterType nextValueOr = nextValuesAnd.get(i);
					if (i > 0) {
						b.append(',');
					}
					String valueAsQueryToken = nextValueOr.getValueAsQueryToken(theCtx);
//					b.append(ParameterUtil.escapeAndUrlEncode(valueAsQueryToken));
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
		
		addUrlIncludeParams(b, Constants.PARAM_INCLUDE, getIncludes());
		addUrlIncludeParams(b, Constants.PARAM_REVINCLUDE, getRevIncludes());
		
		if (getLastUpdated() != null) {
			DateParam lb = getLastUpdated().getLowerBound();
			addLastUpdateParam(b, lb);
			DateParam ub = getLastUpdated().getUpperBound();
			addLastUpdateParam(b, ub);
		}
		
		if (getCount() != null) {
			addUrlParamSeparator(b);
			b.append(Constants.PARAM_COUNT);
			b.append('=');
			b.append(getCount());
		}
		
		if (b.length() == 0) {
			b.append('?');
		}
		
		return b.toString();
	}

	@Override
	public String toString() {
		ToStringBuilder b = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		if (isEmpty() == false) {
			b.append("params", super.toString());
		}
		if (getIncludes().isEmpty() == false) {
			b.append("includes", getIncludes());
		}
		return b.toString();
	}

	static int compare(FhirContext theCtx, IQueryParameterType theO1, IQueryParameterType theO2) {
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
			if (theO1.getMissing().booleanValue()) {
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

	public enum EverythingModeEnum {
		/*
		 * Don't reorder! We rely on the ordinals
		 */
		ENCOUNTER_INSTANCE(false, true, true), ENCOUNTER_TYPE(false, true, false), PATIENT_INSTANCE(true, false, true), PATIENT_TYPE(true, false, false);

		private final boolean myEncounter;

		private final boolean myInstance;

		private final boolean myPatient;

		private EverythingModeEnum(boolean thePatient, boolean theEncounter, boolean theInstance) {
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

	public class IncludeComparator implements Comparator<Include> {

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

	public class QueryParameterOrComparator implements Comparator<List<IQueryParameterType>> {
		private final FhirContext myCtx;

		public QueryParameterOrComparator(FhirContext theCtx) {
			myCtx = theCtx;
		}

		@Override
		public int compare(List<IQueryParameterType> theO1, List<IQueryParameterType> theO2) {
			// These lists will never be empty
			return SearchParameterMap.compare(myCtx, theO1.get(0), theO2.get(0));
		}

	}

	public class QueryParameterTypeComparator implements Comparator<IQueryParameterType> {

		private final FhirContext myCtx;

		public QueryParameterTypeComparator(FhirContext theCtx) {
			myCtx = theCtx;
		}

		@Override
		public int compare(IQueryParameterType theO1, IQueryParameterType theO2) {
			return SearchParameterMap.compare(myCtx, theO1, theO2);
		}

	}

}
