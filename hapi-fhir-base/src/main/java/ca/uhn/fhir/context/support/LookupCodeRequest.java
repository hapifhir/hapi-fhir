/*-
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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

import jakarta.annotation.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * Represents parameters which can be passed to the $lookup operation for codes.
 * @since 7.0.0
 */
public class LookupCodeRequest {
	private final String mySystem;
	private final String myCode;
	private String myVersion;
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

	/**
	 * The code system version to look the code up in, or <code>null</code> for whichever version the
	 * implementation treats as current.
	 * <p>
	 * Naming the version here rather than packing it into {@link #getSystem()} as a
	 * <code>url|version</code> canonical is what lets an implementation which cannot resolve a specific
	 * version tell that a version was asked for at all, instead of silently answering from another one.
	 * </p>
	 *
	 * @since 8.14.0
	 */
	// Created by Claude Opus 5
	@Nullable
	public String getVersion() {
		return myVersion;
	}

	/**
	 * Names the code system version to look the code up in.
	 *
	 * @param theVersion The code system version, e.g. "<code>2.78</code>", or <code>null</code> for whichever version is current
	 * @return this, for chaining
	 * @since 8.14.0
	 */
	// Created by Claude Opus 5
	public LookupCodeRequest setVersion(@Nullable String theVersion) {
		myVersion = theVersion;
		return this;
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

	@Override
	public boolean equals(Object theO) {
		if (this == theO) return true;
		if (!(theO instanceof LookupCodeRequest)) return false;
		LookupCodeRequest that = (LookupCodeRequest) theO;
		return Objects.equals(mySystem, that.mySystem)
				&& Objects.equals(myCode, that.myCode)
				&& Objects.equals(myVersion, that.myVersion)
				&& Objects.equals(myDisplayLanguage, that.myDisplayLanguage)
				&& Objects.equals(myPropertyNames, that.myPropertyNames);
	}

	@Override
	public int hashCode() {
		return Objects.hash(mySystem, myCode, myVersion, myDisplayLanguage, myPropertyNames);
	}
}
