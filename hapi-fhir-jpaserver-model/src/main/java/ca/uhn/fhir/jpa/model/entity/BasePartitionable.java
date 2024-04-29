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
package ca.uhn.fhir.jpa.model.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.MappedSuperclass;

import java.io.Serializable;

/**
 * This is the base class for entities with partitioning that does NOT include Hibernate Envers logging.
 * <p>
 * If your entity needs Envers auditing, please have it extend {@link AuditableBasePartitionable} instead.
 */
@MappedSuperclass
public abstract class BasePartitionable implements Serializable {

	@Embedded
	private PartitionablePartitionId myPartitionId;

	/**
	 * This is here to support queries only, do not set this field directly
	 */
	@SuppressWarnings("unused")
	@Column(name = PartitionablePartitionId.PARTITION_ID, insertable = false, updatable = false, nullable = true)
	private Integer myPartitionIdValue;

	@Nullable
	public PartitionablePartitionId getPartitionId() {
		return myPartitionId;
	}

	public void setPartitionId(PartitionablePartitionId thePartitionId) {
		myPartitionId = thePartitionId;
	}

	@Override
	public String toString() {
		return "BasePartitionable{" + "myPartitionId="
				+ myPartitionId + ", myPartitionIdValue="
				+ myPartitionIdValue + '}';
	}
}
