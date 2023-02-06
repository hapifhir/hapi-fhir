package ca.uhn.fhir.jpa.model.entity;

/*-
 * #%L
 * HAPI FHIR JPA Model
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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

import ca.uhn.fhir.rest.server.messaging.BaseResourceModifiedMessage;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "HFJ_RESOURCE_MODIFIED",
	indexes = {
		@Index(name = "IDX_RESOURCE_MODIFIED_CREATED_TIME", columnList = "CREATED_TIME ASC")
	},
	uniqueConstraints = {
		@UniqueConstraint(name = "IDX_RESOURCE_MODIFIED_UNIQUE_ID_VER", columnNames = {"RES_ID", "RES_VER"})
	})
public class ResourceModifiedEntity implements Serializable {

	public static final int GENERIC_LENGTH = 100;
	public static final int REQ_PARTITION_ID_LENGTH = 100;

	private static final long serialVersionUID = -6808876995278327794L;

	@Id
	@SequenceGenerator(name = "SEQ_RES_MOD_PID", sequenceName = "SEQ_RES_MOD_PID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_RES_MOD_PID")
	@Column(name = "PID")
	private Long myId;

	@Column(name = "RES_ID", insertable = false, updatable = false, nullable = false)
	private Long myResourcePid;

	@Column(name = "RES_VER", nullable = false)
	private long myResourceVersion;

	@Column(name = "CREATED_TIME", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date myCreatedTime;

	@Column(name = "OPERATION_TYPE", nullable = false, length = 100)
	@Enumerated(EnumType.STRING)
	private BaseResourceModifiedMessage.OperationTypeEnum myOperationType;

	@Column(name = "RES_TX_GUID", length = GENERIC_LENGTH)
	private String myResourceTransactionGuid;

	@Column(name = "REQ_PARTITION_ID", length = REQ_PARTITION_ID_LENGTH)
	private String myRequestPartitionId;

	public void setId(Long theId) {
		myId = theId;
	}

	public Long getId() {
		return myId;
	}

	public Long getResourcePid() {
		return myResourcePid;
	}

	public void setResourcePid(Long theResourcePid) {
		myResourcePid = theResourcePid;
	}

	public long getResourceVersion() {
		return myResourceVersion;
	}

	public void setResourceVersion(long theResourceVersion) {
		myResourceVersion = theResourceVersion;
	}

	public Date getCreatedTime() {
		return myCreatedTime;
	}

	public void setCreatedTime(Date theCreatedTime) {
		myCreatedTime = theCreatedTime;
	}

	public BaseResourceModifiedMessage.OperationTypeEnum getOperationType() {
		return myOperationType;
	}

	public void setOperationType(BaseResourceModifiedMessage.OperationTypeEnum theOperationType) {
		myOperationType = theOperationType;
	}

	public String getResourceTransactionGuid() {
		return myResourceTransactionGuid;
	}

	public void setResourceTransactionGuid(String theResourceTransactionGuid) {
		myResourceTransactionGuid = theResourceTransactionGuid;
	}

	public String getRequestPartitionId() {
		return myRequestPartitionId;
	}

	public void setRequestPartitionId(String theRequestPartitionId) {
		myRequestPartitionId = theRequestPartitionId;
	}

}


