package ca.uhn.fhir.rest.server.interceptor.s13n.standardizers;

/*-
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

import java.util.Objects;

class Range {

	private int myStart;
	private int myEnd;

	public Range(int theStart, int theEnd) {
		this.myStart = theStart;
		this.myEnd = theEnd;
	}

	public boolean isInRange(int theNum) {
		return theNum >= getStart() && theNum <= getEnd();
	}

	public int getStart() {
		return myStart;
	}

	public int getEnd() {
		return myEnd;
	}

	@Override
	public boolean equals(Object theObject) {
		if (this == theObject) {
			return true;
		}

		if (theObject == null || getClass() != theObject.getClass()) {
			return false;
		}

		Range range = (Range) theObject;
		return myStart == range.myStart && myEnd == range.myEnd;
	}

	@Override
	public int hashCode() {
		return Objects.hash(myStart, myEnd);
	}

	@Override
	public String toString() {
		return String.format("[%s, %s]", getStart(), getEnd());
	}
}
