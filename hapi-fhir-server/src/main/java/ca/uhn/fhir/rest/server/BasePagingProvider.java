package ca.uhn.fhir.rest.server;

/*
 * #%L
 * HAPI FHIR - Server Framework
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

public abstract class BasePagingProvider implements IPagingProvider {

	public static final int DEFAULT_DEFAULT_PAGE_SIZE = 10;
	public static final int DEFAULT_MAX_PAGE_SIZE = 50;

	private int myDefaultPageSize = DEFAULT_DEFAULT_PAGE_SIZE;
	private int myMaximumPageSize = DEFAULT_MAX_PAGE_SIZE;

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
