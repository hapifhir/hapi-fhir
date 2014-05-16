package ca.uhn.fhir.rest.param;

/*
 * #%L
 * HAPI FHIR Library
 * %%
 * Copyright (C) 2014 University Health Network
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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import ca.uhn.fhir.model.api.IQueryParameterOr;
import ca.uhn.fhir.model.dstu.composite.CodingDt;

public class CodingListParam implements IQueryParameterOr, Iterable<CodingDt> {

	private List<CodingDt> myCodings = new ArrayList<CodingDt>();

	/**
	 * Constructor. Codings should be added, e.g. using {@link #add(CodingDt)}
	 */
	public CodingListParam() {
		// nothing
	}

	/**
	 * Constructor which accepts an array of codings
	 * 
	 * @param theCodings
	 *            The codings
	 */
	public CodingListParam(CodingDt... theCodings) {
		if (theCodings != null) {
			for (CodingDt next : theCodings) {
				add(next);
			}
		}
	}

	/**
	 * Constructor which accepts a code system and an array of codes in that system
	 * 
	 * @param theSystem
	 *            The code system
	 * @param theCodings
	 *            The codes
	 */
	public CodingListParam(String theSystem, String... theCodes) {
		if (theCodes != null) {
			for (String next : theCodes) {
				add(new CodingDt(theSystem, next));
			}
		}
	}

	/**
	 * Constructor which accepts a code system and a collection of codes in that system
	 * 
	 * @param theSystem
	 *            The code system
	 * @param theCodings
	 *            The codes
	 */
	public CodingListParam(String theSystem, Collection<String> theCodes) {
		if (theCodes != null) {
			for (String next : theCodes) {
				add(new CodingDt(theSystem, next));
			}
		}
	}

	/**
	 * Returns all Codings associated with this list
	 */
	public List<CodingDt> getCodings() {
		if (myCodings == null) {
			myCodings = new ArrayList<CodingDt>();
		}
		return myCodings;
	}

	@Override
	public List<String> getValuesAsQueryTokens() {
		ArrayList<String> retVal = new ArrayList<String>();
		for (CodingDt next : myCodings) {
			retVal.add(next.getValueAsQueryToken());
		}
		return retVal;
	}

	/**
	 * Returns all Codings associated with this list
	 */
	public void setCodings(List<CodingDt> theCodings) {
		myCodings = theCodings;
	}

	@Override
	public void setValuesAsQueryTokens(List<String> theParameters) {
		getCodings().clear();
		for (String string : theParameters) {
			CodingDt dt = new CodingDt();
			dt.setValueAsQueryToken(null, string);
			myCodings.add(dt);
		}
	}

	public void add(CodingDt theCodingDt) {
		myCodings.add(theCodingDt);
	}

	@Override
	public Iterator<CodingDt> iterator() {
		return getCodings().iterator();
	}

}
