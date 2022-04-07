package ca.uhn.fhir.jpa.delete;

/*-
 * #%L
 * HAPI FHIR JPA Server
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

public class DeleteConflictOutcome {

	private int myShouldRetryCount;

	public int getShouldRetryCount() {
		return myShouldRetryCount;
	}

	public DeleteConflictOutcome setShouldRetryCount(int theShouldRetryCount) {
		Validate.isTrue(theShouldRetryCount >= 0, "theShouldRetryCount must not be negative");
		myShouldRetryCount = theShouldRetryCount;
		return this;
	}

}
