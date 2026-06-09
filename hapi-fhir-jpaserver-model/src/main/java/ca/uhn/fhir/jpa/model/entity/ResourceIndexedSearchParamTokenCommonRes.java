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

import ca.uhn.hapi.fhir.sql.hibernatesvc.OracleIOT;
import ca.uhn.hapi.fhir.sql.hibernatesvc.PartitionedIdProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.Objects;

@OracleIOT(nonPartitionedCompressionLevel = 1, partitionedCompressionLevel = 2)
@Entity
@Table(
		name = ResourceIndexedSearchParamTokenCommonRes.HFJ_SPIDX2_TOKEN_COMMON_RES,
		indexes = {
			@Index(name = "IDX_SP2_TOKEN_COMMON_RES_HASH", columnList = "HASH_SYS_AND_VALUE, RES_ID, PARTITION_ID"),
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
			foreignKey = @ForeignKey(name = "FK_SP2_TOKEN_COMMON_RES"))
	private ResourceTable myResource;

	public ResourceIndexedSearchParamTokenCommonRes() {}

	public ResourceIndexedSearchParamTokenCommonRes(
			Long theResourceId, Integer thePartitionId, long theHashSystemAndValue) {
		myResourceId = theResourceId;
		myPartitionId = thePartitionId;
		myHashSystemAndValue = theHashSystemAndValue;
	}

	public ResourceIndexedSearchParamTokenCommonRes setResource(ResourceTable theResource) {
		myResource = theResource;
		return this;
	}

	public Long getResourceId() {
		return myResourceId;
	}

	public Integer getPartitionId() {
		return myPartitionId;
	}

	public long getHashSystemAndValue() {
		return myHashSystemAndValue;
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) return true;
		if (!(theO instanceof ResourceIndexedSearchParamTokenCommonRes that)) return false;
		return myHashSystemAndValue == that.myHashSystemAndValue
				&& Objects.equals(myResourceId, that.myResourceId)
				&& Objects.equals(myPartitionId, that.myPartitionId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(myResourceId, myPartitionId, myHashSystemAndValue);
	}
}
