package ca.uhn.fhir.jpa.model.entity;

/*-
 * #%L
 * HAPI FHIR JPA Model
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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.envers.RevisionType;

import java.util.Date;
import java.util.Objects;

public class EnversRevision {
	private final RevisionType myRevisionType;
	private final long myRevisionNumber;
	private final Date myRevisionTimestamp;

	public EnversRevision(RevisionType theRevisionType, long theRevisionNumber, Date theRevisionTimestamp) {
		myRevisionType = theRevisionType;
		myRevisionNumber = theRevisionNumber;
		myRevisionTimestamp = theRevisionTimestamp;
	}

	public RevisionType getRevisionType() {
		return myRevisionType;
	}

	public long getRevisionNumber() {
		return myRevisionNumber;
	}

	public Date getRevisionTimestamp() {
		return myRevisionTimestamp;
	}

	@Override
	public boolean equals(Object theO) {

		if (this == theO) return true;
		if (theO == null || getClass() != theO.getClass()) return false;
		final EnversRevision that = (EnversRevision) theO;
		return myRevisionNumber == that.myRevisionNumber
				&& myRevisionTimestamp == that.myRevisionTimestamp
				&& myRevisionType == that.myRevisionType;
	}

	@Override
	public int hashCode() {
		return Objects.hash(myRevisionType, myRevisionNumber, myRevisionTimestamp);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("myRevisionType", myRevisionType)
				.append("myRevisionNumber", myRevisionNumber)
				.append("myRevisionTimestamp", myRevisionTimestamp)
				.toString();
	}
}
