package ca.uhn.fhir.rest.server;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

public abstract class BasePagingProvider implements IPagingProvider {

	private int myDefaultPageSize = 10;
	private int myMaximumPageSize = 50;

	public BasePagingProvider() {
		super();
	}

	@Override
	public int getDefaultPageSize() {
		return myDefaultPageSize;
	}

	@Override
	public int getMaximumPageSize() {
		return myMaximumPageSize;
	}

	public BasePagingProvider setDefaultPageSize(int theDefaultPageSize) {
		Validate.isTrue(theDefaultPageSize > 0, "size must be greater than 0");
		myDefaultPageSize = theDefaultPageSize;
		return this;
	}

	public BasePagingProvider setMaximumPageSize(int theMaximumPageSize) {
		Validate.isTrue(theMaximumPageSize > 0, "size must be greater than 0");
		myMaximumPageSize = theMaximumPageSize;
		return this;
	}

}
