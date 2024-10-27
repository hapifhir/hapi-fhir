/*-
 * #%L
 * HAPI FHIR Storage api
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
package ca.uhn.fhir.jpa.model.search;

public enum SearchStatusEnum {

	/**
	 * The search is currently actively working
	 */
	LOADING(false),
	/**
	 * The search has loaded a set of results and has stopped searching because it
	 * reached an appropriate threshold
	 */
	PASSCMPLET(false),
	/**
	 * The search completed normally and loaded all of the results it as permitted to
	 * load
	 */
	FINISHED(true),
	/**
	 * The search failed and will not continue
	 */
	FAILED(true),
	/**
	 * The search has been expired and will be expunged shortly
	 */
	GONE(true);

	private final boolean myDone;

	SearchStatusEnum(boolean theDone) {
		myDone = theDone;
	}

	/**
	 * Returns true if no more work will happen for this search (finished, failed, gone)
	 */
	public boolean isDone() {
		return myDone;
	}
}
