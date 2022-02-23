package ca.uhn.fhir.jpa.model.entity;

/*-
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

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDate;

@Embeddable
public class PartitionablePartitionId implements Cloneable {

	static final String PARTITION_ID = "PARTITION_ID";

	@Column(name = PARTITION_ID, nullable = true, insertable = true, updatable = false)
	private Integer myPartitionId;
	@Column(name = "PARTITION_DATE", nullable = true, insertable = true, updatable = false)
	private LocalDate myPartitionDate;

	/**
	 * Constructor
	 */
	public PartitionablePartitionId() {
		super();
	}

	/**
	 * Constructor
	 */
	public PartitionablePartitionId(@Nullable Integer thePartitionId, @Nullable LocalDate thePartitionDate) {
		setPartitionId(thePartitionId);
		setPartitionDate(thePartitionDate);
	}

	@Nullable
	public Integer getPartitionId() {
		return myPartitionId;
	}

	public PartitionablePartitionId setPartitionId(@Nullable Integer thePartitionId) {
		myPartitionId = thePartitionId;
		return this;
	}

	@Override
	public boolean equals(Object theO) {
		if (!(theO instanceof PartitionablePartitionId)) {
			return false;
		}

		PartitionablePartitionId that = (PartitionablePartitionId) theO;
		return new EqualsBuilder().append(myPartitionId, that.myPartitionId).append(myPartitionDate, that.myPartitionDate).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(myPartitionId).append(myPartitionDate).toHashCode();
	}

	@Nullable
	public LocalDate getPartitionDate() {
		return myPartitionDate;
	}

	public PartitionablePartitionId setPartitionDate(@Nullable LocalDate thePartitionDate) {
		myPartitionDate = thePartitionDate;
		return this;
	}

	@SuppressWarnings({"CloneDoesntDeclareCloneNotSupportedException", "MethodDoesntCallSuperMethod"})
	@Override
	protected PartitionablePartitionId clone() {
		return new PartitionablePartitionId()
			.setPartitionId(getPartitionId())
			.setPartitionDate(getPartitionDate());
	}

	public RequestPartitionId toPartitionId() {
		return RequestPartitionId.fromPartitionId(getPartitionId(), getPartitionDate());
	}

	@Override
	public String toString() {
		return "PartitionablePartitionId{" +
			"myPartitionId=" + myPartitionId +
			", myPartitionDate=" + myPartitionDate +
			'}';
	}

	@Nonnull
	public static RequestPartitionId toRequestPartitionId(@Nullable PartitionablePartitionId theRequestPartitionId) {
		if (theRequestPartitionId != null) {
			return theRequestPartitionId.toPartitionId();
		} else {
			return RequestPartitionId.defaultPartition();
		}
	}
}
