/*-
 * #%L
 * HAPI FHIR - Core Library
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
package ca.uhn.fhir.context.support;

import java.util.Collection;
import java.util.Collections;

/**
 * Represents parameters which can be passed to the $lookup operation for codes.
 * @since 7.0.0
 */
public class LookupCodeRequest {
	private final String mySystem;
	private final String myCode;
	private String myDisplayLanguage;
	private Collection<String> myPropertyNames;

	/**
	 * @param theSystem                    The CodeSystem URL
	 * @param theCode                      The code
	 */
	public LookupCodeRequest(String theSystem, String theCode) {
		mySystem = theSystem;
		myCode = theCode;
	}

	/**
	 * @param theSystem                    The CodeSystem URL
	 * @param theCode                      The code
	 * @param theDisplayLanguage           Used to filter out the designation by the display language. To return all designation, set this value to <code>null</code>.
	 * @param thePropertyNames             The collection of properties to be returned in the output. If no properties are specified, the implementor chooses what to return.
	 */
	public LookupCodeRequest(
			String theSystem, String theCode, String theDisplayLanguage, Collection<String> thePropertyNames) {
		this(theSystem, theCode);
		myDisplayLanguage = theDisplayLanguage;
		myPropertyNames = thePropertyNames;
	}

	public String getSystem() {
		return mySystem;
	}

	public String getCode() {
		return myCode;
	}

	public String getDisplayLanguage() {
		return myDisplayLanguage;
	}

	public Collection<String> getPropertyNames() {
		if (myPropertyNames == null) {
			return Collections.emptyList();
		}
		return myPropertyNames;
	}
}
