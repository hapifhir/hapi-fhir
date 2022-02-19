package ca.uhn.fhir.jpa.model.search;

/*-
 * #%L
 * HAPI FHIR JPA Model
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

import java.util.Date;

class DateSearchIndexData {
	private final Date myLowerBoundDate;
	private final int myLowerBoundOrdinal;
	private final Date myUpperBoundDate;
	private final int myUpperBoundOrdinal;

	DateSearchIndexData(Date theLowerBoundDate, int theLowerBoundOrdinal, Date theUpperBoundDate, int theUpperBoundOrdinal) {
		myLowerBoundDate = theLowerBoundDate;
		myLowerBoundOrdinal = theLowerBoundOrdinal;
		myUpperBoundDate = theUpperBoundDate;
		myUpperBoundOrdinal = theUpperBoundOrdinal;
	}

	public Date getLowerBoundDate() {
		return myLowerBoundDate;
	}

	public int getLowerBoundOrdinal() {
		return myLowerBoundOrdinal;
	}

	public Date getUpperBoundDate() {
		return myUpperBoundDate;
	}

	public int getUpperBoundOrdinal() {
		return myUpperBoundOrdinal;
	}
}
