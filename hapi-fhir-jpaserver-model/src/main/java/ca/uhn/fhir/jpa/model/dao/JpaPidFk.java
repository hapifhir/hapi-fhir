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
package ca.uhn.fhir.jpa.model.dao;

import ca.uhn.fhir.jpa.model.entity.PartitionablePartitionId;
import ca.uhn.hapi.fhir.sql.hibernatesvc.PartitionedIdProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * This is a version of {@link JpaPid} which can be used as a FK, since it doesn't include
 * the {@literal @Id} or related annotations.
 */
@Embeddable
public class JpaPidFk implements Serializable {

	@Column(name = "RES_ID", nullable = false)
	private Long myId;

	@PartitionedIdProperty
	@Column(name = PartitionablePartitionId.PARTITION_ID, nullable = false)
	private Integer myPartitionIdValue;

	/**
	 * Note that equals and hashCode for this object only consider the ID and Partition ID because
	 * this class gets used as cache keys
	 */
	@Override
	public boolean equals(Object theO) {
		if (this == theO) {
			return true;
		}
		if (!(theO instanceof JpaPidFk)) {
			return false;
		}
		JpaPidFk jpaPid = (JpaPidFk) theO;
		return Objects.equals(myId, jpaPid.myId) && Objects.equals(myPartitionIdValue, jpaPid.myPartitionIdValue);
	}

	/**
	 * Note that equals and hashCode for this object only consider the ID and Partition ID because
	 * this class gets used as cache keys
	 */
	@Override
	public int hashCode() {
		return Objects.hash(myId, myPartitionIdValue);
	}

	public JpaPid toJpaPid() {
		return JpaPid.fromId(myId, myPartitionIdValue);
	}

	public void setId(Long theId) {
		myId = theId;
	}

	public void setPartitionId(Integer thePartitionId) {
		myPartitionIdValue = thePartitionId;
	}

	public static List<JpaPidFk> fromPids(List<JpaPid> thePids) {
		return thePids.stream().map(JpaPidFk::fromPid).collect(Collectors.toList());
	}

	public static JpaPidFk fromPid(JpaPid thePid) {
		JpaPidFk retVal = new JpaPidFk();
		retVal.setId(thePid.getId());
		retVal.setPartitionId(thePid.getPartitionId());
		return retVal;
	}

	public static JpaPidFk fromId(Long theId, Integer thePartitionId) {
		JpaPidFk retVal = new JpaPidFk();
		retVal.setId(theId);
		retVal.setPartitionId(thePartitionId);
		return retVal;
	}

	public static JpaPidFk fromId(Long theId) {
		return fromId(theId, null);
	}
}
