package ca.uhn.fhir.jpa.dao;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import ca.uhn.fhir.model.api.IQueryParameterAnd;
import ca.uhn.fhir.model.api.IQueryParameterOr;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.param.DateRangeParam;

public class SearchParameterMap extends HashMap<String, List<List<? extends IQueryParameterType>>> {

	private static final long serialVersionUID = 1L;

	private Integer myCount;
	private Set<Include> myIncludes;
	private DateRangeParam myLastUpdated;
	private Set<Include> myRevIncludes;

	private SortSpec mySort;

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

	public void add(String theName, IQueryParameterType theParam) {
		if (theParam == null) {
			return;
		}
		if (!containsKey(theName)) {
			put(theName, new ArrayList<List<? extends IQueryParameterType>>());
		}
		ArrayList<IQueryParameterType> list = new ArrayList<IQueryParameterType>();
		list.add(theParam);
		get(theName).add(list);
	}

	public void addInclude(Include theInclude) {
		getIncludes().add(theInclude);
	}

	public Integer getCount() {
		return myCount;
	}

	public Set<Include> getIncludes() {
		if (myIncludes == null) {
			myIncludes = new HashSet<Include>();
		}
		return myIncludes;
	}

	public DateRangeParam getLastUpdated() {
		return myLastUpdated;
	}

	public Set<Include> getRevIncludes() {
		return myRevIncludes;
	}

	public SortSpec getSort() {
		return mySort;
	}

	public void setCount(Integer theCount) {
		myCount = theCount;
	}

	public void setIncludes(Set<Include> theIncludes) {
		myIncludes = theIncludes;
	}

	public void setLastUpdated(DateRangeParam theLastUpdated) {
		myLastUpdated = theLastUpdated;
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

}
