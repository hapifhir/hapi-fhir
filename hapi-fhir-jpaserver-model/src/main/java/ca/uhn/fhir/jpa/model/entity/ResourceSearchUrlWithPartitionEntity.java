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
package ca.uhn.fhir.jpa.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import java.util.StringJoiner;

import static ca.uhn.fhir.jpa.model.entity.ResourceSearchUrlWithPartitionEntity.IDX_RESSEARCHURLPARTITION_RES;

/**
 * This entity is used to enforce uniqueness on a given search URL being
 * used as a conditional operation URL, e.g. a conditional create or a
 * conditional update. When we perform a conditional operation that is
 * creating a new resource, we store an entity with the conditional URL
 * in this table. The URL and optional partition_id is the PK of the table,
 * so the database * ensures that two concurrent threads don't accidentally
 * create two * resources with the same conditional URL.  The partition ID
 * is the second part of the primary key and is a non-nullable field with
 * the caveat that a null equivalent value of -1 will be used in the case
 * that a partition_id is either null or the config does not active a new
 * feature that search_urls can be duplicated across non-null partitions.
 */
@Entity
@Table(
		name = "HFJ_RES_SEARCH_URL_PARTITION_ID",
		indexes = {
			@Index(name = IDX_RESSEARCHURLPARTITION_RES, columnList = "RES_ID"),
			@Index(name = "IDX_RESSEARCHURLPARTITION_TIME", columnList = "CREATED_TIME")
		})
public class ResourceSearchUrlWithPartitionEntity {
	public static final String IDX_RESSEARCHURLPARTITION_RES = "IDX_RESSEARCHURLPARTITION_RES";

	@EmbeddedId
	private ResourceSearchUrlWithPartitionEntityPK myPk;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
			name = "RES_ID",
			nullable = false,
			updatable = false,
			foreignKey = @ForeignKey(name = "FK_RES_SEARCH_URL_PARTITION_RESOURCE"))
	private ResourceTable myResourceTable;

	@Column(name = "RES_ID", updatable = false, nullable = false, insertable = false)
	private Long myResourcePid;

	@Column(name = "PARTITION_DATE", nullable = true, insertable = true, updatable = false)
	private LocalDate myPartitionDate;

	@Column(name = "CREATED_TIME", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date myCreatedTime;

	public static ResourceSearchUrlWithPartitionEntity from(String theUrl, ResourceTable theResourceTable, boolean theSearchUrlDuplicateAcrossPartitionsEnabled) {
		return new ResourceSearchUrlWithPartitionEntity()
				.setPk(ResourceSearchUrlWithPartitionEntityPK.from(theUrl, theResourceTable, theSearchUrlDuplicateAcrossPartitionsEnabled))
				.setPartitionDate(Optional.ofNullable(theResourceTable.getPartitionId())
						.map(PartitionablePartitionId::getPartitionDate)
						.orElse(null))
				.setResourceTable(theResourceTable)
				.setCreatedTime(new Date());
	}

	public ResourceSearchUrlWithPartitionEntityPK getPk() {
		return myPk;
	}

	public ResourceSearchUrlWithPartitionEntity setPk(ResourceSearchUrlWithPartitionEntityPK thePk) {
		myPk = thePk;
		return this;
	}

	public Long getResourcePid() {
		if (myResourcePid != null) {
			return myResourcePid;
		}
		return myResourceTable.getResourceId();
	}

	public ResourceSearchUrlWithPartitionEntity setResourcePid(Long theResourcePid) {
		myResourcePid = theResourcePid;
		return this;
	}

	public ResourceTable getResourceTable() {
		return myResourceTable;
	}

	public ResourceSearchUrlWithPartitionEntity setResourceTable(ResourceTable myResourceTable) {
		this.myResourceTable = myResourceTable;
		return this;
	}

	public Date getCreatedTime() {
		return myCreatedTime;
	}

	public ResourceSearchUrlWithPartitionEntity setCreatedTime(Date theCreatedTime) {
		myCreatedTime = theCreatedTime;
		return this;
	}

	public LocalDate getPartitionDate() {
		return myPartitionDate;
	}

	public ResourceSearchUrlWithPartitionEntity setPartitionDate(LocalDate thePartitionDate) {
		myPartitionDate = thePartitionDate;
		return this;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", ResourceSearchUrlWithPartitionEntity.class.getSimpleName() + "[", "]")
				.add("myPk=" + myPk)
				.add("myResourceTable=" + myResourceTable)
				.add("myResourcePid=" + myResourcePid)
				.add("myPartitionDate=" + myPartitionDate)
				.add("myCreatedTime=" + myCreatedTime)
				.toString();
	}
}
