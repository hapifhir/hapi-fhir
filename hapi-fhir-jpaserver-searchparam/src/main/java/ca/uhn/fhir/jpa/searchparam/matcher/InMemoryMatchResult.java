/*-
 * #%L
 * HAPI FHIR Search Parameters
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.searchparam.matcher;

import java.util.List;

public class InMemoryMatchResult {
	public static final String PARSE_FAIL = "Failed to translate parse query string";
	public static final String STANDARD_PARAMETER = "Standard parameters not supported";
	public static final String CHAIN = "Chained parameters are not supported";
	public static final String PARAM = "Parameter not supported";
	public static final String QUALIFIER = "Qualified parameter not supported";
	public static final String LOCATION_NEAR = "Location.position near not supported";

	private final boolean myMatch;
	/**
	 * True if it is expected that a search will be performed in-memory
	 */
	private final boolean mySupported;
	/**
	 * if mySupported is false, then the parameter responsible for in-memory search not being supported
	 */
	private final String myUnsupportedParameter;
	/**
	 * if mySupported is false, then the reason in-memory search is not supported
	 */
	private final String myUnsupportedReason;
	/**
	 * Only used by CompositeInMemoryDaoSubscriptionMatcher to track whether we had to go
	 * out to the database to resolve the match.
	 */
	private boolean myInMemory = false;

	private InMemoryMatchResult(boolean theMatch) {
		this.myMatch = theMatch;
		this.mySupported = true;
		this.myUnsupportedParameter = null;
		this.myUnsupportedReason = null;
	}

	private InMemoryMatchResult(String theUnsupportedParameter, String theUnsupportedReason) {
		myMatch = false;
		mySupported = false;
		myUnsupportedParameter = theUnsupportedParameter;
		myUnsupportedReason = theUnsupportedReason;
	}

	public static InMemoryMatchResult successfulMatch() {
		return new InMemoryMatchResult(true);
	}

	public static InMemoryMatchResult fromBoolean(boolean theMatched) {
		return new InMemoryMatchResult(theMatched);
	}

	public static InMemoryMatchResult unsupportedFromReason(String theUnsupportedReason) {
		return new InMemoryMatchResult(null, theUnsupportedReason);
	}

	public static InMemoryMatchResult unsupportedFromParameterAndReason(
			String theUnsupportedParameter, String theUnsupportedReason) {
		return new InMemoryMatchResult(theUnsupportedParameter, theUnsupportedReason);
	}

	public static InMemoryMatchResult noMatch() {
		return new InMemoryMatchResult(false);
	}

	public boolean supported() {
		return mySupported;
	}

	public boolean matched() {
		return myMatch;
	}

	public String getUnsupportedReason() {
		if (myUnsupportedParameter != null) {
			return "Parameter: <" + myUnsupportedParameter + "> Reason: " + myUnsupportedReason;
		}
		return myUnsupportedReason;
	}

	public boolean isInMemory() {
		return myInMemory;
	}

	public void setInMemory(boolean theInMemory) {
		myInMemory = theInMemory;
	}

	public static InMemoryMatchResult and(InMemoryMatchResult theLeft, InMemoryMatchResult theRight) {
		if (theLeft == null) {
			return theRight;
		}
		if (theRight == null) {
			return theLeft;
		}
		if (theLeft.supported() && theRight.supported()) {
			return InMemoryMatchResult.fromBoolean(theLeft.matched() && theRight.matched());
		}
		if (!theLeft.supported() && !theRight.supported()) {
			return InMemoryMatchResult.unsupportedFromReason(
					List.of(theLeft.getUnsupportedReason(), theRight.getUnsupportedReason())
							.toString());
		}
		if (!theLeft.supported()) {
			return theLeft;
		}
		return theRight;
	}

	public static InMemoryMatchResult or(InMemoryMatchResult theLeft, InMemoryMatchResult theRight) {
		if (theLeft == null) {
			return theRight;
		}
		if (theRight == null) {
			return theLeft;
		}
		if (theLeft.matched() || theRight.matched()) {
			return InMemoryMatchResult.successfulMatch();
		}
		if (!theLeft.supported() && !theRight.supported()) {
			return InMemoryMatchResult.unsupportedFromReason(
					List.of(theLeft.getUnsupportedReason(), theRight.getUnsupportedReason())
							.toString());
		}
		if (!theLeft.supported()) {
			return theLeft;
		}
		return theRight;
	}
}
