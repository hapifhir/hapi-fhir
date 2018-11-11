package ca.uhn.fhir.jpa.entity;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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

import com.google.common.annotations.VisibleForTesting;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "HFJ_RES_REINDEX_JOB")
public class ResourceReindexJobEntity implements Serializable {
	@Id
	@SequenceGenerator(name = "SEQ_RES_REINDEX_JOB", sequenceName = "SEQ_RES_REINDEX_JOB")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_RES_REINDEX_JOB")
	@Column(name = "PID")
	private Long myId;
	@Column(name = "RES_TYPE", nullable = true)
	private String myResourceType;
	/**
	 * Inclusive
	 */
	@Column(name = "UPDATE_THRESHOLD_HIGH", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date myThresholdHigh;
	@Column(name = "JOB_DELETED", nullable = false)
	private boolean myDeleted;
	/**
	 * Inclusive
	 */
	@Column(name = "UPDATE_THRESHOLD_LOW", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date myThresholdLow;
	@Column(name = "SUSPENDED_UNTIL", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date mySuspendedUntil;

	public Date getSuspendedUntil() {
		return mySuspendedUntil;
	}

	public void setSuspendedUntil(Date theSuspendedUntil) {
		mySuspendedUntil = theSuspendedUntil;
	}

	/**
	 * Inclusive
	 */
	public Date getThresholdLow() {
		return myThresholdLow;
	}

	/**
	 * Inclusive
	 */
	public void setThresholdLow(Date theThresholdLow) {
		myThresholdLow = theThresholdLow;
	}

	public String getResourceType() {
		return myResourceType;
	}

	public void setResourceType(String theResourceType) {
		myResourceType = theResourceType;
	}

	/**
	 * Inclusive
	 */
	public Date getThresholdHigh() {
		return myThresholdHigh;
	}

	/**
	 * Inclusive
	 */
	public void setThresholdHigh(Date theThresholdHigh) {
		myThresholdHigh = theThresholdHigh;
	}

	public Long getId() {
		return myId;
	}

	@VisibleForTesting
	public void setIdForUnitTest(long theId) {
		myId = theId;
	}

	public void setDeleted(boolean theDeleted) {
		myDeleted = theDeleted;
	}
}
