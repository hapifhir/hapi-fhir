package ca.uhn.fhir.jpa.model.entity;

/*
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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity()
@Table(name = ForcedId.HFJ_FORCED_ID, uniqueConstraints = {
	@UniqueConstraint(name = "IDX_FORCEDID_RESID", columnNames = {"RESOURCE_PID"}),
	/*
	 * This index is called IDX_FORCEDID_TYPE_FID and guarantees
	 * uniqueness of RESOURCE_TYPE,FORCED_ID. This doesn't make sense
	 * for partitioned servers, so we replace it on those servers
	 * with IDX_FORCEDID_TYPE_PFID covering
	 * PARTITION_ID,RESOURCE_TYPE,FORCED_ID
	 */
	@UniqueConstraint(name = ForcedId.IDX_FORCEDID_TYPE_FID, columnNames = {"RESOURCE_TYPE", "FORCED_ID"})
}, indexes = {
	/*
	 * NB: We previously had indexes named
	 * - IDX_FORCEDID_TYPE_FORCEDID
	 * - IDX_FORCEDID_TYPE_RESID
	 * so don't reuse these names
	 */
	@Index(name = "IDX_FORCEID_FID", columnList = "FORCED_ID"),
	//@Index(name = "IDX_FORCEID_RESID", columnList = "RESOURCE_PID"),
	//TODO GGG potentiall add a type + res_id index here, specifically for deletion?
})
public class ForcedId extends BasePartitionable {

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

	@JoinColumn(name = "RESOURCE_PID", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "FK_FORCEDID_RESOURCE"))
	@OneToOne(fetch = FetchType.LAZY)
	private ResourceTable myResource;

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

	public void setResource(ResourceTable theResource) {
		myResource = theResource;
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
}
