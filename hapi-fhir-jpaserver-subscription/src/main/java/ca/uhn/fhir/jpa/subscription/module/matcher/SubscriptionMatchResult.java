package ca.uhn.fhir.jpa.subscription.module.matcher;

/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

public class SubscriptionMatchResult {
	public static final String PARSE_FAIL = "Failed to translate parse query string";
	public static final String STANDARD_PARAMETER = "Standard parameters not supported";
	public static final String PREFIX = "Prefixes not supported";
	public static final String CHAIN = "Chained references are not supported";
	public static final String PARAM = "Param not supported";

	private final boolean myMatch;
	private final boolean mySupported;
	private final String myUnsupportedParameter;
	private final String myUnsupportedReason;

	private boolean myInMemory = false;

	private SubscriptionMatchResult(boolean theMatch) {
		this.myMatch = theMatch;
		this.mySupported = true;
		this.myUnsupportedParameter = null;
		this.myUnsupportedReason = null;
	}

	private SubscriptionMatchResult(String theUnsupportedParameter, String theUnsupportedReason) {
		this.myMatch = false;
		this.mySupported = false;
		this.myUnsupportedParameter = theUnsupportedParameter;
		this.myUnsupportedReason = theUnsupportedReason;
	}

	public static SubscriptionMatchResult successfulMatch() {
		return new SubscriptionMatchResult(true);
	}

	public static SubscriptionMatchResult fromBoolean(boolean theMatched) {
		return new SubscriptionMatchResult(theMatched);
	}

	public static SubscriptionMatchResult unsupportedFromReason(String theUnsupportedReason) {
		return new SubscriptionMatchResult(null, theUnsupportedReason);
	}

	public static SubscriptionMatchResult unsupportedFromParameterAndReason(String theUnsupportedParameter, String theUnsupportedReason) {
		return new SubscriptionMatchResult(theUnsupportedParameter, theUnsupportedReason);
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
}
