package ca.uhn.fhir.jpa.model.entity;

/*-
 * #%L
 * HAPI FHIR Model
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

import ca.uhn.fhir.interceptor.model.RequestPartitionId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@MappedSuperclass
public class BasePartitionable implements Serializable {

	@Embedded
	private PartitionablePartitionId myPartitionId;

	/**
	 * This is here to support queries only, do not set this field directly
	 */
	@SuppressWarnings("unused")
	@Column(name = PartitionablePartitionId.PARTITION_ID, insertable = false, updatable = false, nullable = true)
	private Integer myPartitionIdValue;

	@Nonnull
	public RequestPartitionId getPartitionId() {
		if (myPartitionId != null) {
			return myPartitionId.toPartitionId();
		} else {
			return RequestPartitionId.defaultPartition();
		}
	}

	public void setPartitionId(@Nullable RequestPartitionId theRequestPartitionId) {
		if (theRequestPartitionId != null) {
			myPartitionId = new PartitionablePartitionId(theRequestPartitionId.getPartitionId(), theRequestPartitionId.getPartitionDate());
		} else {
			myPartitionId = null;
		}
	}


}
