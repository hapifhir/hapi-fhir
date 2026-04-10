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

import ca.uhn.fhir.jpa.model.dialect.HapiSequenceStyleGenerator;
import ca.uhn.hapi.fhir.sql.hibernatesvc.PartitionedIdProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;

@Entity
@Table(
		name = ResourceIndexedSearchParamTokenIdentifier.HFJ_SPIDX2_TOKEN_IDENTIFIER,
		indexes = {
			@Index(
					name = "IDX_SP_TOKEN_ID_HASH_SYSTEM",
					columnList = "HASH_IDENTITY,SP_SYSTEM_URL_ID,RES_ID,PARTITION_ID"),
			@Index(name = "IDX_SP_TOKEN_ID_HASH", columnList = "HASH_IDENTITY,RES_ID,PARTITION_ID,SP_SYSTEM_URL_ID"),
			@Index(
					name = "IDX_SP_TOKEN_ID_HASH_VALUE",
					columnList =
							"HASH_IDENTITY,HASH_VALUE,RES_ID,PARTITION_ID,SP_SYSTEM_URL_ID,TYPE_HASH_SYS_AND_VALUE"),
			@Index(name = "IDX_SP_TOKEN_ID_RES_ID", columnList = "RES_ID")
		})
@IdClass(IdAndPartitionId.class)
public class ResourceIndexedSearchParamTokenIdentifier implements Serializable {
	public static final String HFJ_SPIDX2_TOKEN_IDENTIFIER = "HFJ_SPIDX2_TOKEN_IDENTIFIER";
	public static final int MAX_LENGTH = 768;

	@Id
	@GenericGenerator(name = "SEQ_SPIDX2_TOKEN_IDENTIFIER", type = HapiSequenceStyleGenerator.class)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_SPIDX2_TOKEN_IDENTIFIER")
	@Column(name = "SP_ID")
	private Long myId;

	// TODO: move this to new BasePartitionable class (without PartitionDate) ?
	@Id
	@PartitionedIdProperty
	@Column(name = "PARTITION_ID", nullable = true)
	private Integer myPartitionId;

	@Column(name = "RES_ID", nullable = false)
	private long myResourceId;

	@Column(name = "HASH_IDENTITY", nullable = false)
	private long myHashIdentity;

	@Column(name = "SP_SYSTEM_URL_ID", nullable = false)
	private long mySystemUrlId;

	@Column(name = "SP_VALUE", length = MAX_LENGTH, nullable = false)
	private String myValue;

	@Column(name = "HASH_VALUE", nullable = false)
	private long myHashValue;

	@Column(name = "TYPE_HASH_SYS_AND_VALUE", nullable = true)
	private Long myTypeHashSystemAndValue;

	public ResourceIndexedSearchParamTokenIdentifier() {}
}
