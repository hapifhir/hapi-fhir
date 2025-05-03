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

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;

import java.util.Objects;

import static ca.uhn.fhir.rest.api.Constants.MAX_RESOURCE_NAME_LENGTH;

@Entity
@Table(
		name = "HFJ_RESOURCE_TYPE",
		uniqueConstraints = @UniqueConstraint(name = "IDX_RES_TYPE_NAME", columnNames = "RES_TYPE"))
public class ResourceTypeEntity {

	@Id
	@SequenceGenerator(name = "SEQ_RESOURCE_TYPE", sequenceName = "SEQ_RESOURCE_TYPE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_RESOURCE_TYPE")
	@Column(name = "RES_TYPE_ID")
	private Short myResourceTypeId;

	@FullTextField
	@Column(name = "RES_TYPE", nullable = false, length = MAX_RESOURCE_NAME_LENGTH)
	private String myResourceType;

	public Short getResourceTypeId() {
		return myResourceTypeId;
	}

	public void setResourceTypeId(Short myResourceTypeId) {
		this.myResourceTypeId = myResourceTypeId;
	}

	public String getResourceType() {
		return myResourceType;
	}

	public void setResourceType(String myResourceType) {
		this.myResourceType = myResourceType;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		ResourceTypeEntity that = (ResourceTypeEntity) o;
		return Objects.equals(myResourceTypeId, that.myResourceTypeId)
				&& Objects.equals(myResourceType, that.myResourceType);
	}

	@Override
	public int hashCode() {
		return Objects.hash(myResourceTypeId, myResourceType);
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("ResourceTypeEntity[");
		b.append("resourceTypeId=").append(myResourceTypeId);
		b.append(", resourceType=").append(myResourceType);
		b.append("]");
		return b.toString();
	}
}
