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

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.io.Serializable;

/**
 * This is a copy of (@link {@link BasePartitionable} used ONLY for entities that are audited by Hibernate Envers.
 * <p>
 * The reason for this is Envers will generate _AUD table schema for all auditable tables, even those marked as {@link NotAudited}.
 * <p>
 * Should we make more entities envers auditable in the future, they would need to extend this class and not {@link BasePartitionable}.
 */
@Audited
@MappedSuperclass
public class AuditableBasePartitionable implements Serializable {
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
}
