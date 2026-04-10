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
package ca.uhn.fhir.jpa.model.entity;

import ca.uhn.hapi.fhir.sql.hibernatesvc.PartitionedIdProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import java.io.Serializable;

@Entity
@Table(
		name = ResourceIndexedSearchParamTokenCommonRes.HFJ_SPIDX2_TOKEN_COMMON_RES,
		indexes = {
			@Index(name = "HFJ_SPIDX2_TOKEN_COMMON_RES_HASH", columnList = "HASH_SYS_AND_VALUE, RES_ID, PARTITION_ID"),
		})
@IdClass(ResIdPartitionIdAndHashSystemAndValue.class)
public class ResourceIndexedSearchParamTokenCommonRes implements Serializable {
	public static final String HFJ_SPIDX2_TOKEN_COMMON_RES = "HFJ_SPIDX2_TOKEN_COMMON_RES";

	@Id
	@Column(name = "RES_ID", nullable = false)
	private Long myResourceId;

	@Id
	@Column(name = "HASH_SYS_AND_VALUE", nullable = false)
	private long myHashSystemAndValue;

	// TODO: move this to new BasePartitionable class (without PartitionDate) ?
	@Id
	@PartitionedIdProperty
	@Column(name = "PARTITION_ID", nullable = true)
	private Integer myPartitionId;
}
