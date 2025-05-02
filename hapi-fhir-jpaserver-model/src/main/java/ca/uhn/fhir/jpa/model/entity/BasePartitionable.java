/*-
 * #%L
 * HAPI FHIR JPA Model
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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

import ca.uhn.hapi.fhir.sql.hibernatesvc.PartitionedIdProperty;
import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * This is the base class for entities with partitioning that does NOT include Hibernate Envers logging.
 * <p>
 * If your entity needs Envers auditing, please have it extend {@link AuditableBasePartitionable} instead.
 */
@MappedSuperclass
public abstract class BasePartitionable implements Serializable {

	@SuppressWarnings("unused")
	@Id
	@PartitionedIdProperty
	@Column(name = PartitionablePartitionId.PARTITION_ID)
	Integer myPartitionIdValue;

	@SuppressWarnings("unused")
	@Column(name = PartitionablePartitionId.PARTITION_DATE, updatable = false, nullable = true)
	private LocalDate myPartitionDateValue;

	@Nonnull
	public PartitionablePartitionId getPartitionId() {
		return PartitionablePartitionId.with(myPartitionIdValue, myPartitionDateValue);
	}

	public void setPartitionId(PartitionablePartitionId thePartitionId) {
		myPartitionIdValue = thePartitionId.getPartitionId();
		myPartitionDateValue = thePartitionId.getPartitionDate();
	}

	@Override
	public String toString() {
		return "BasePartitionable{" + "myPartitionIdValue="
				+ myPartitionIdValue + ", myPartitionDateValue="
				+ myPartitionDateValue + '}';
	}
}
