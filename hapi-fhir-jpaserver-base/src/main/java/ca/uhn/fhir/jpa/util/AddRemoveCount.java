package ca.uhn.fhir.jpa.util;

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

public class AddRemoveCount {

	private int myAddCount;
	private int myRemoveCount;


	public void addToAddCount(int theCount) {
		myAddCount += theCount;
	}

	public void addToRemoveCount(int theCount) {
		myRemoveCount += theCount;
	}

	public int getAddCount() {
		return myAddCount;
	}

	public int getRemoveCount() {
		return myRemoveCount;
	}

	public boolean isEmpty() {
		return myAddCount > 0 || myRemoveCount > 0;
	}
}
