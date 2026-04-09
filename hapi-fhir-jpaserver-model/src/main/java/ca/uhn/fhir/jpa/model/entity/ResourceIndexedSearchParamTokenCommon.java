package ca.uhn.fhir.jpa.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
/*
 * #%L
 * HAPI FHIR JPA Model
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
import java.io.Serializable;

@Entity
@Table(name = ResourceIndexedSearchParamTokenCommon.HFJ_SPIDX2_TOKEN_COMMON, indexes = {
	@Index(name = "IDX_SP_TOKEN_COMMON_SYSTEM", columnList = "SYSTEM_ID,HASH_SYS_AND_VALUE"),
	@Index(name = "IDX_SP_TOKEN_COMMON_VALUE", columnList = "HASH_VALUE,HASH_SYS_AND_VALUE"),
	@Index(name = "IDX_SP_TOKEN_IDENTITY", columnList = "HASH_IDENTITY,HASH_SYS_AND_VALUE")
})
public class ResourceIndexedSearchParamTokenCommon implements Serializable {
	public static final String HFJ_SPIDX2_TOKEN_COMMON = "HFJ_SPIDX2_TOKEN_COMMON";
	public static final int MAX_LENGTH = 200;

	@Id
	@Column(name = "HASH_SYS_AND_VALUE", nullable = false)
	private long myHashSystemAndValue;

	@Column(name = "SYSTEM_ID", nullable = false)
	private long mySystemId;

	@Column(name = "SP_VALUE", length = MAX_LENGTH, nullable = true)
	private String myValue;

	@Column(name = "HASH_IDENTITY", nullable = false)
	private long myHashIdentity;

	@Column(name = "HASH_VALUE", nullable = false)
	private long myHashValue;

	public ResourceIndexedSearchParamTokenCommon() {
	}

}
