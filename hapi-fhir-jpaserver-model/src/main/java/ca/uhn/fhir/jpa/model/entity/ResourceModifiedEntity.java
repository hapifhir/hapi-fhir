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

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.io.Serializable;
import java.util.Date;

/**
 * This class describes how a resourceModifiedMessage is stored for later processing in the event where
 * submission to the subscription processing pipeline would fail.  The persisted message does not include a
 * payload (resource) as an in-memory version of the same message would.  Instead, it points to a payload
 * through the entity primary key {@link PersistedResourceModifiedMessageEntityPK} which is composed
 * of the resource Pid and current version.
 */
@Entity
@Table(name = "HFJ_RESOURCE_MODIFIED")
public class ResourceModifiedEntity implements IPersistedResourceModifiedMessage, Serializable {

	public static final int MESSAGE_LENGTH = 4000;

	@EmbeddedId
	private PersistedResourceModifiedMessageEntityPK myResourceModifiedEntityPK;

	@Column(name = "SUMMARY_MESSAGE", length = MESSAGE_LENGTH, nullable = false)
	private String mySummaryResourceModifiedMessage;

	@Column(name = "CREATED_TIME", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date myCreatedTime;

	@Column(name = "RESOURCE_TYPE", length = ResourceTable.RESTYPE_LEN, nullable = false)
	private String myResourceType;

	public PersistedResourceModifiedMessageEntityPK getResourceModifiedEntityPK() {
		return myResourceModifiedEntityPK;
	}

	public ResourceModifiedEntity setResourceModifiedEntityPK(
			PersistedResourceModifiedMessageEntityPK theResourceModifiedEntityPK) {
		myResourceModifiedEntityPK = theResourceModifiedEntityPK;
		return this;
	}

	@Override
	public String getResourceType() {
		return myResourceType;
	}

	public ResourceModifiedEntity setResourceType(String theResourceType) {
		myResourceType = theResourceType;
		return this;
	}

	@Override
	public Date getCreatedTime() {
		return myCreatedTime;
	}

	public void setCreatedTime(Date theCreatedTime) {
		myCreatedTime = theCreatedTime;
	}

	public String getSummaryResourceModifiedMessage() {
		return mySummaryResourceModifiedMessage;
	}

	public ResourceModifiedEntity setSummaryResourceModifiedMessage(String theSummaryResourceModifiedMessage) {
		mySummaryResourceModifiedMessage = theSummaryResourceModifiedMessage;
		return this;
	}

	@Override
	public IPersistedResourceModifiedMessagePK getPersistedResourceModifiedMessagePk() {
		return myResourceModifiedEntityPK;
	}
}
