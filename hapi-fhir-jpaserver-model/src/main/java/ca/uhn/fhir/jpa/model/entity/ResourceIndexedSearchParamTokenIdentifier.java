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
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.Objects;

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
							"HASH_IDENTITY,HASH_VALUE,RES_ID,PARTITION_ID,SP_SYSTEM_URL_ID,TYPE_HASH_SYS_AND_VALUE")
		})
@IdClass(ResIdSpIdAndPartitionId.class)
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
	private Integer myPartitionIdValue;

	@Id
	@Column(name = "RES_ID", nullable = false)
	private Long myResourceId;

	@Column(name = "HASH_IDENTITY", nullable = false)
	private long myHashIdentity;

	@Column(name = "SP_SYSTEM_URL_ID", nullable = true)
	private Long mySystemUrlId;

	@Column(name = "SP_VALUE", length = MAX_LENGTH, nullable = false)
	private String myValue;

	@Column(name = "HASH_VALUE", nullable = true)
	private Long myHashValue;

	@Column(name = "TYPE_HASH_SYS_AND_VALUE", nullable = true)
	private Long myTypeHashSystemAndValue;

	// Required for Hibernate to insert HFJ_RESOURCE rows before HFJ_SPIDX2_TOKEN_COMMON_RES rows
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumns(
			value = {
				@JoinColumn(
						name = "RES_ID",
						referencedColumnName = "RES_ID",
						insertable = false,
						updatable = false,
						nullable = false),
				@JoinColumn(
						name = "PARTITION_ID",
						referencedColumnName = "PARTITION_ID",
						insertable = false,
						updatable = false,
						nullable = false)
			},
			foreignKey = @ForeignKey(name = "FK_SP2_TOKEN_IDENTIFIER_RES"))
	private ResourceTable myResource;

	public ResourceIndexedSearchParamTokenIdentifier() {}

	public ResourceIndexedSearchParamTokenIdentifier(
			Integer thePartitionId,
			Long theResourceId,
			long theHashIdentity,
			Long theSystemUrlId,
			String theValue,
			Long theHashValue,
			Long theTypeHashSystemAndValue) {
		myPartitionIdValue = thePartitionId;
		myResourceId = theResourceId;
		myHashIdentity = theHashIdentity;
		mySystemUrlId = theSystemUrlId;
		myValue = theValue;
		myHashValue = theHashValue;
		myTypeHashSystemAndValue = theTypeHashSystemAndValue;
	}

	public ResourceIndexedSearchParamTokenIdentifier setResource(ResourceTable theResource) {
		myResource = theResource;
		return this;
	}

	public Long getId() {
		return myId;
	}

	public Integer getPartitionId() {
		return myPartitionIdValue;
	}

	public Long getResourceId() {
		return myResourceId;
	}

	public long getHashIdentity() {
		return myHashIdentity;
	}

	public Long getSystemUrlId() {
		return mySystemUrlId;
	}

	public String getValue() {
		return myValue;
	}

	public Long getHashValue() {
		return myHashValue;
	}

	public Long getTypeHashSystemAndValue() {
		return myTypeHashSystemAndValue;
	}

	/**
	 * Content-based equality, excluding the generated {@code SP_ID}. Used by the synchronizer's
	 * set-subtract diff to detect stale rows on resource update.
	 */
	@Override
	public boolean equals(Object theO) {
		if (this == theO) return true;
		if (!(theO instanceof ResourceIndexedSearchParamTokenIdentifier that)) return false;
		return Objects.equals(myResourceId, that.myResourceId)
				&& myHashIdentity == that.myHashIdentity
				&& myHashValue == that.myHashValue
				&& Objects.equals(mySystemUrlId, that.mySystemUrlId)
				&& Objects.equals(myPartitionIdValue, that.myPartitionIdValue)
				&& Objects.equals(myValue, that.myValue)
				&& Objects.equals(myTypeHashSystemAndValue, that.myTypeHashSystemAndValue);
	}

	@Override
	public int hashCode() {
		return Objects.hash(
				myResourceId,
				myPartitionIdValue,
				myHashIdentity,
				mySystemUrlId,
				myValue,
				myHashValue,
				myTypeHashSystemAndValue);
	}
}
