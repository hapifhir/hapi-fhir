/*
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
package ca.uhn.fhir.jpa.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.ColumnDefault;

/**
 * The old way we handled client-assigned resource ids.
 * Replaced by {@link ResourceTable#myFhirId}.
 * @deprecated This is unused, and only kept for history and upgrade migration testing.
 */
@Entity()
@Table(name = ForcedId.HFJ_FORCED_ID)
@Deprecated(since = "7.1", forRemoval = true)
class ForcedId extends BasePartitionable {

	public static final int MAX_FORCED_ID_LENGTH = 100;
	public static final String IDX_FORCEDID_TYPE_FID = "IDX_FORCEDID_TYPE_FID";
	public static final String HFJ_FORCED_ID = "HFJ_FORCED_ID";

	@Column(name = "FORCED_ID", nullable = false, length = MAX_FORCED_ID_LENGTH, updatable = false)
	private String myForcedId;

	@SequenceGenerator(name = "SEQ_FORCEDID_ID", sequenceName = "SEQ_FORCEDID_ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_FORCEDID_ID")
	@Id
	@Column(name = "PID")
	private Long myId;

	@Column(name = "RESOURCE_PID", nullable = false, updatable = false, insertable = false)
	private Long myResourcePid;

	// This is updatable=true because it was added in 1.6 and needs to be set.. At some
	// point we should remove the default and make it not updatable
	@ColumnDefault("''")
	@Column(name = "RESOURCE_TYPE", nullable = true, length = 100, updatable = true)
	private String myResourceType;

	/**
	 * Constructor
	 */
	public ForcedId() {
		super();
	}

	public String getForcedId() {
		return myForcedId;
	}

	public void setForcedId(String theForcedId) {
		myForcedId = theForcedId;
	}

	public String getResourceType() {
		return myResourceType;
	}

	public void setResourceType(String theResourceType) {
		myResourceType = theResourceType;
	}

	public Long getId() {
		return myId;
	}

	public Long getResourceId() {
		return myResourcePid;
	}

	@Override
	public String toString() {
		ToStringBuilder b = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		b.append("pid", myId);
		if (getPartitionId() != null) {
			b.append("partitionId", getPartitionId().getPartitionId());
		}
		b.append("resourceType", myResourceType);
		b.append("forcedId", myForcedId);
		b.append("resourcePid", myResourcePid);
		return b.toString();
	}

	public String asTypedFhirResourceId() {
		return getResourceType() + "/" + getForcedId();
	}
}
