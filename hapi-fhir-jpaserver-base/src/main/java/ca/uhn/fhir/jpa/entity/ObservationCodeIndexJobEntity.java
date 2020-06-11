package ca.uhn.fhir.jpa.entity;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.rest.api.Constants;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "HFJ_OBS_CDE_INDEX_JOB")
public class ObservationCodeIndexJobEntity implements Serializable {
	@Id
	@SequenceGenerator(name = "SEQ_OBS_CDE_INDEX_JOB", sequenceName = "SEQ_OBS_CDE_INDEX_JOB")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_OBS_CDE_INDEX_JOB")
	@Column(name = "PID")
	private Long myId;
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

	@Column(name = "INDEX_COUNT", nullable = true)
	private Integer myIndexCount;

	public Integer getIndexCount() {
		return myIndexCount;
	}

	public void setIndexCount(Integer theReindexCount) {
		myIndexCount = theReindexCount;
	}

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
		Date retVal = myThresholdLow;
		if (retVal != null) {
			retVal = new Date(retVal.getTime());
		}
		return retVal;
	}

	/**
	 * Inclusive
	 */
	public void setThresholdLow(Date theThresholdLow) {
		myThresholdLow = theThresholdLow;
	}

	/**
	 * Inclusive
	 */
	public Date getThresholdHigh() {
		Date retVal = myThresholdHigh;
		if (retVal != null) {
			retVal = new Date(retVal.getTime());
		}
		return retVal;
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

	@Override
	public String toString() {
		ToStringBuilder b = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
			.append("id", myId)
			.append("thresholdLow", myThresholdLow)
			.append("thresholdHigh", myThresholdHigh);
		if (myDeleted) {
			b.append("deleted", myDeleted);
		}
		if (mySuspendedUntil != null) {
			b.append("suspendedUntil", mySuspendedUntil);
		}
		return b.toString();
	}
}
