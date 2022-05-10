package ca.uhn.fhir.jpa.entity;

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

import ca.uhn.fhir.interceptor.model.RequestPartitionId;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "HFJ_PARTITION", uniqueConstraints = {
	@UniqueConstraint(name = "IDX_PART_NAME", columnNames = {"PART_NAME"})
})
public class PartitionEntity {

	public static final int MAX_NAME_LENGTH = 200;
	public static final int MAX_DESC_LENGTH = 200;

	/**
	 * Note that unlike most PID columns in HAPI FHIR JPA, this one is an Integer, and isn't
	 * auto assigned.
	 */
	@Id
	@Column(name = "PART_ID", nullable = false)
	private Integer myId;
	@Column(name = "PART_NAME", length = MAX_NAME_LENGTH, nullable = false)
	private String myName;
	@Column(name = "PART_DESC", length = MAX_DESC_LENGTH, nullable = true)
	private String myDescription;

	public Integer getId() {
		return myId;
	}

	public PartitionEntity setId(Integer theId) {
		myId = theId;
		return this;
	}

	public String getName() {
		return myName;
	}

	public PartitionEntity setName(String theName) {
		myName = theName;
		return this;
	}

	public String getDescription() {
		return myDescription;
	}

	public void setDescription(String theDescription) {
		myDescription = theDescription;
	}

	public RequestPartitionId toRequestPartitionId() {
		return RequestPartitionId.fromPartitionIdAndName(getId(), getName());
	}
}
