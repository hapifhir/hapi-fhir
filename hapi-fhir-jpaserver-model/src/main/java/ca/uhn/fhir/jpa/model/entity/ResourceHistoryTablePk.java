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

import ca.uhn.fhir.rest.api.server.storage.IResourceVersionPersistentId;
import ca.uhn.hapi.fhir.sql.hibernatesvc.PartitionedIdProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ResourceHistoryTablePk implements IResourceVersionPersistentId, Serializable {

	@GenericGenerator(
			name = "SEQ_RESOURCE_HISTORY_ID",
			type = ca.uhn.fhir.jpa.model.dialect.HapiSequenceStyleGenerator.class)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_RESOURCE_HISTORY_ID")
	@Column(name = "PID")
	private Long myVersionId;

	@PartitionedIdProperty
	@Column(name = PartitionablePartitionId.PARTITION_ID)
	private Integer myPartitionIdValue;

	@Override
	public boolean equals(Object theO) {
		if (this == theO) {
			return true;
		}
		if (!(theO instanceof ResourceHistoryTablePk)) {
			return false;
		}
		ResourceHistoryTablePk that = (ResourceHistoryTablePk) theO;
		return Objects.equals(myVersionId, that.myVersionId)
				&& Objects.equals(myPartitionIdValue, that.myPartitionIdValue);
	}

	@Override
	public int hashCode() {
		return Objects.hash(myVersionId, myPartitionIdValue);
	}

	public void setPartitionIdValue(Integer thePartitionIdValue) {
		myPartitionIdValue = thePartitionIdValue;
	}

	public Long getId() {
		return myVersionId;
	}

	public Integer getPartitionId() {
		return myPartitionIdValue;
	}

	public IdAndPartitionId asIdAndPartitionId() {
		return new IdAndPartitionId(getId(), getPartitionId());
	}

	@Override
	public String toString() {
		return myVersionId + "/" + myPartitionIdValue;
	}
}
