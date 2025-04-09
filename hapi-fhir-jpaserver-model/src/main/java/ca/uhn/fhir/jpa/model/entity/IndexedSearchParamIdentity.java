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

import ca.uhn.fhir.rest.api.Constants;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;

/**
 * Stores unique hash identities along with the corresponding {@code sp_name} and {@code res_type} values.
 * This entity is populated during read, write, or update operations on the {@code HFJ_SPIDX_xxx} tables.
 */
@Entity
@Table(
		name = "HFJ_SPIDX_IDENTITY",
		uniqueConstraints = @UniqueConstraint(name = "IDX_HASH_IDENTITY", columnNames = "HASH_IDENTITY"))
public class IndexedSearchParamIdentity {

	public static final int SP_NAME_LENGTH = 256;

	@Id
	@SequenceGenerator(name = "SEQ_SPIDX_IDENTITY", sequenceName = "SEQ_SPIDX_IDENTITY", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_SPIDX_IDENTITY")
	@Column(name = "SP_IDENTITY_ID")
	private Integer mySpIdentityId;

	@Column(name = "HASH_IDENTITY", nullable = false)
	private Long myHashIdentity;

	@FullTextField
	@Column(name = "RES_TYPE", nullable = false, length = Constants.MAX_RESOURCE_NAME_LENGTH)
	private String myResourceType;

	@FullTextField
	@Column(name = "SP_NAME", nullable = false, length = SP_NAME_LENGTH)
	private String myParamName;

	public Integer getSpIdentityId() {
		return mySpIdentityId;
	}

	public void setSpIdentityId(Integer theSpIdentityId) {
		this.mySpIdentityId = theSpIdentityId;
	}

	public Long getHashIdentity() {
		return myHashIdentity;
	}

	public void setHashIdentity(Long theHashIdentity) {
		this.myHashIdentity = theHashIdentity;
	}

	public String getResourceType() {
		return myResourceType;
	}

	public void setResourceType(String theResourceType) {
		this.myResourceType = theResourceType;
	}

	public String getParamName() {
		return myParamName;
	}

	public void setParamName(String theParamName) {
		this.myParamName = theParamName;
	}
}
