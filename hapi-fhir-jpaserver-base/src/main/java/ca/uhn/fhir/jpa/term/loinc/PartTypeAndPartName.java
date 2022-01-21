package ca.uhn.fhir.jpa.term.loinc;

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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class PartTypeAndPartName {
	private final String myPartType;
	private final String myPartName;

	public PartTypeAndPartName(String thePartType, String thePartName) {
		myPartType = thePartType;
		myPartName = thePartName;
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) {
			return true;
		}

		if (theO == null || getClass() != theO.getClass()) {
			return false;
		}

		PartTypeAndPartName that = (PartTypeAndPartName) theO;

		return new EqualsBuilder()
			.append(myPartType, that.myPartType)
			.append(myPartName, that.myPartName)
			.isEquals();
	}

	public String getPartName() {
		return myPartName;
	}

	public String getPartType() {
		return myPartType;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
			.append(myPartType)
			.append(myPartName)
			.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("partType", myPartType)
			.append("partName", myPartName)
			.toString();
	}
}
