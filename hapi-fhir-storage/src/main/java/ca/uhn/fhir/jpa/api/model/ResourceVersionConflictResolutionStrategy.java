package ca.uhn.fhir.jpa.api.model;

/*-
 * #%L
 * HAPI FHIR Storage api
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
 * @since 5.1.0
 */
public class ResourceVersionConflictResolutionStrategy {

	private int myMaxRetries;
	private boolean myRetry;

	public int getMaxRetries() {
		return myMaxRetries;
	}

	public void setMaxRetries(int theMaxRetries) {
		Validate.isTrue(theMaxRetries >= 0, "theRetryUpToMillis must not be negative");
		myMaxRetries = theMaxRetries;
	}

	public boolean isRetry() {
		return myRetry;
	}

	public void setRetry(boolean theRetry) {
		myRetry = theRetry;
	}
}
