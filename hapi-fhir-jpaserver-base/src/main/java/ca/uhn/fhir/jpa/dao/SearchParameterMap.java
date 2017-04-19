package ca.uhn.fhir.jpa.dao;

import static java.util.Collections.newSetFromMap;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import ca.uhn.fhir.jpa.dao.SearchParameterMap.QueryParameterTypeComparator;
import ca.uhn.fhir.model.api.IQueryParameterAnd;
import ca.uhn.fhir.model.api.IQueryParameterOr;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.util.ObjectUtil;

public class SearchParameterMap extends LinkedHashMap<String, List<List<? extends IQueryParameterType>>> {

	public class QueryParameterTypeComparator implements Comparator<IQueryParameterType> {

		@Override
		public int compare(IQueryParameterType theO1, IQueryParameterType theO2) {
			int retVal = 0;
			
			if (theO1.getMissing() == null && theO2.getMissing() == null) {
				// neither are missing
			} else if (theO1.getMissing() == null) {
				retVal = -1;
			} else if (theO2.getMissing() == null) {
				retVal = 1;
			} else if (!ObjectUtil.equals(theO1.getMissing(), theO2.getMissing())) {
				
			}
			
			return retVal;
		}

	}

	private static final long serialVersionUID = 1L;

	private Integer myCount;
	private EverythingModeEnum myEverythingMode = null;
	private Set<Include> myIncludes;
	private DateRangeParam myLastUpdated;
	private Integer myLoadSynchronousUpTo;
	private Set<Include> myRevIncludes;
	private SortSpec mySort;

	private boolean myLoadSynchronous;

	/**
	 * Constructor
	 */
	public SearchParameterMap() {
		// nothing
	}

	/**
	 * Constructor
	 */
	public SearchParameterMap(String theName, IQueryParameterType theParam) {
		add(theName, theParam);
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

	public SearchParameterMap add(String theName, DateParam theDateParam) {
		add(theName, (IQueryParameterOr<?>)theDateParam);
		return this;
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

	public void addRevInclude(Include theInclude) {
		getRevIncludes().add(theInclude);
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

	public Set<Include> getRevIncludes() {
		if (myRevIncludes == null) {
			myRevIncludes = new HashSet<Include>();
		}
		return myRevIncludes;
	}

	public SortSpec getSort() {
		return mySort;
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
	 * more than X results
	 */
	public boolean isLoadSynchronous() {
		return myLoadSynchronous;
	}

	/**
	 * @deprecated As of HAPI FHIR 2.4 this method no longer does anything
	 */
	@Deprecated
	public void setPersistResults(boolean thePersistResults) {
		// does nothing as of HAPI FHIR 2.4
	}

	public void setRevIncludes(Set<Include> theRevIncludes) {
		myRevIncludes = theRevIncludes;
	}

	public void setSort(SortSpec theSort) {
		mySort = theSort;
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

	public String toQueryString() {
		StringBuilder b = new StringBuilder();
		
		ArrayList<String> keys = new ArrayList<String>(keySet());
		Collections.sort(keys);
		for (String nextKey : keys) {
			
			List<List<? extends IQueryParameterType>> nextValuesAnds = get(nextKey);
			for (List<? extends IQueryParameterType> nextValuesAnd : nextValuesAnds) {
				for (List<? extends IQueryParameterType> nextValuesOrs : nextValuesAnds) {
					
					nextValuesOrs = new ArrayList<IQueryParameterType>(nextValuesOrs);
					Collections.sort(nextValuesOrs, new QueryParameterTypeComparator());
					
				}
			}
			
		}
	}

}
