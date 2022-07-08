package ca.uhn.fhir.context.support;

/*-
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import org.apache.commons.lang3.Validate;

/**
 * Options for ValueSet expansion
 *
 * @see IValidationSupport
 */
public class ValueSetExpansionOptions {

	private boolean myFailOnMissingCodeSystem = true;
	private int myCount = 1000;
	private int myOffset = 0;
	private boolean myIncludeHierarchy;
	private String myFilter;

	public String getFilter() {
		return myFilter;
	}

	public ValueSetExpansionOptions setFilter(String theFilter) {
		myFilter = theFilter;
		return this;
	}

	/**
	 * The number of codes to return.
	 * <p>
	 * Default is 1000
	 * </p>
	 */
	public int getCount() {
		return myCount;
	}

	/**
	 * The number of codes to return.
	 * <p>
	 * Default is 1000
	 * </p>
	 */
	public ValueSetExpansionOptions setCount(int theCount) {
		Validate.isTrue(theCount >= 0, "theCount must be >= 0");
		myCount = theCount;
		return this;
	}

	/**
	 * The code index to start at (i.e the individual code index, not the page number)
	 */
	public int getOffset() {
		return myOffset;
	}

	/**
	 * The code index to start at (i.e the individual code index, not the page number)
	 */
	public ValueSetExpansionOptions setOffset(int theOffset) {
		Validate.isTrue(theOffset >= 0, "theOffset must be >= 0");
		myOffset = theOffset;
		return this;
	}

	/**
	 * Should the expansion fail if a codesystem is referenced by the valueset, but
	 * it can not be found?
	 * <p>
	 * Default is <code>true</code>
	 * </p>
	 */
	public boolean isFailOnMissingCodeSystem() {
		return myFailOnMissingCodeSystem;
	}

	/**
	 * Should the expansion fail if a codesystem is referenced by the valueset, but
	 * it can not be found?
	 * <p>
	 * Default is <code>true</code>
	 * </p>
	 */
	public ValueSetExpansionOptions setFailOnMissingCodeSystem(boolean theFailOnMissingCodeSystem) {
		myFailOnMissingCodeSystem = theFailOnMissingCodeSystem;
		return this;
	}

	public boolean isIncludeHierarchy() {
		return myIncludeHierarchy;
	}

	public void setIncludeHierarchy(boolean theIncludeHierarchy) {
		myIncludeHierarchy = theIncludeHierarchy;
	}

	public static ValueSetExpansionOptions forOffsetAndCount(int theOffset, int theCount) {
		return new ValueSetExpansionOptions()
			.setOffset(theOffset)
			.setCount(theCount);
	}
}
