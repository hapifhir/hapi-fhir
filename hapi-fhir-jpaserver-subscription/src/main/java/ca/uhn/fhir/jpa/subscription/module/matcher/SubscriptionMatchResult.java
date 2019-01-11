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
	private final boolean myMatch;
	private final boolean mySupported;
	private final String myUnsupportedParameter;
	private final String myUnsupportedReason;
	private final String myMatcherShortName;

	public SubscriptionMatchResult(boolean theMatch, String theMatcherShortName) {
		this.myMatch = theMatch;
		this.mySupported = true;
		this.myUnsupportedParameter = null;
		this.myUnsupportedReason = null;
		this.myMatcherShortName = theMatcherShortName;
	}

	public SubscriptionMatchResult(String theUnsupportedParameter, String theMatcherShortName) {
		this.myMatch = false;
		this.mySupported = false;
		this.myUnsupportedParameter = theUnsupportedParameter;
		this.myUnsupportedReason = "Parameter not supported";
		this.myMatcherShortName = theMatcherShortName;
	}

	public SubscriptionMatchResult(String theUnsupportedParameter, String theUnsupportedReason, String theMatcherShortName) {
		this.myMatch = false;
		this.mySupported = false;
		this.myUnsupportedParameter = theUnsupportedParameter;
		this.myUnsupportedReason = theUnsupportedReason;
		this.myMatcherShortName = theMatcherShortName;
	}

	public boolean supported() {
		return mySupported;
	}

	public boolean matched() {
		return myMatch;
	}

	public String getUnsupportedReason() {
		return "Parameter: <" + myUnsupportedParameter + "> Reason: " + myUnsupportedReason;
	}

	/**
	 * Returns a short name of the matcher that generated this
	 * response, for use in logging
	 */
	public String matcherShortName() {
		return myMatcherShortName;
	}
}
