/*-
 * #%L
 * HAPI FHIR JPA Model
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.model.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Objects;

/**
 * This class is used to store the PID for a given table, and if we're running in
 * database partition mode, it also stores the partition ID.
 */
public class IdAndPartitionId implements Serializable {
	private Long myId;
	private Integer myPartitionIdValue;

	public IdAndPartitionId(Long theId, Integer thePartitionId) {
		myId = theId;
		myPartitionIdValue = thePartitionId;
	}

	public IdAndPartitionId() {
		// nothing
	}

	public IdAndPartitionId(Long theId) {
		myId = theId;
	}

	public Long getId() {
		return myId;
	}

	public void setId(Long theId) {
		myId = theId;
	}

	public void setPartitionIdValue(Integer thePartitionIdValue) {
		myPartitionIdValue = thePartitionIdValue;
	}

	public Integer getPartitionIdValue() {
		return myPartitionIdValue;
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) return true;
		if (!(theO instanceof IdAndPartitionId)) return false;
		IdAndPartitionId that = (IdAndPartitionId) theO;
		return Objects.equals(myId, that.myId) && Objects.equals(myPartitionIdValue, that.myPartitionIdValue);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("id", myId)
				.append("partitionId", myPartitionIdValue)
				.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(myId, myPartitionIdValue);
	}

	public static IdAndPartitionId forId(Long theId, BasePartitionable thePartitionable) {
		IdAndPartitionId retVal = new IdAndPartitionId();
		retVal.setId(theId);
		retVal.setPartitionIdValue(thePartitionable.getPartitionId().getPartitionId());
		return retVal;
	}
}
