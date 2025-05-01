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

import ca.uhn.fhir.jpa.model.dao.JpaPid;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

/**
 * This entity is used to enforce uniqueness on a given search URL being
 * used as a conditional operation URL, e.g. a conditional create or a
 * conditional update. When we perform a conditional operation that is
 * creating a new resource, we store an entity with the conditional URL
 * in this table. The URL is the PK of the table, so the database
 * ensures that two concurrent threads don't accidentally create two
 * resources with the same conditional URL.
 * <p>
 * Note that this entity is partitioned, but does not always respect the
 * partition ID of the parent resource entity (it may just use a
 * hardcoded partition ID of -1 depending on configuration). As a result
 * we don't have a FK relationship. This table only contains short-lived
 * rows that get cleaned up and purged periodically, so it should never
 * grow terribly large anyhow.
 */
@Entity
@Table(
		name = "HFJ_RES_SEARCH_URL",
		indexes = {
			@Index(name = "IDX_RESSEARCHURL_RES", columnList = "RES_ID"),
			@Index(name = "IDX_RESSEARCHURL_TIME", columnList = "CREATED_TIME")
		})
public class ResourceSearchUrlEntity {

	public static final String RES_SEARCH_URL_COLUMN_NAME = "RES_SEARCH_URL";
	public static final String PARTITION_ID = "PARTITION_ID";

	@EmbeddedId
	private ResourceSearchUrlEntityPK myPk;

	/*
	 * Note: We previously had a foreign key here, but it's just not possible for this to still
	 * work with partition IDs in the PKs since non-partitioned mode currently depends on the
	 * partition ID being a part of the PK and it necessarily has to be stripped out if we're
	 * stripping out others. So we'll leave this without a FK relationship, which does increase
	 * the possibility of dangling records in this table but that's probably an ok compromise.
	 *
	 * Ultimately records in this table get cleaned up based on their CREATED_TIME anyhow, so
	 * it's really not a big deal to not have a FK relationship here.
	 */

	@Column(name = "RES_ID", updatable = false, nullable = false, insertable = true)
	private Long myResourcePid;

	@Column(name = "PARTITION_ID", updatable = false, nullable = false, insertable = false)
	private Integer myPartitionIdValue;

	@Column(name = "PARTITION_DATE", nullable = true, insertable = true, updatable = false)
	private LocalDate myPartitionDate;

	@Column(name = "CREATED_TIME", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date myCreatedTime;

	public static ResourceSearchUrlEntity from(
			String theUrl, ResourceTable theResourceTable, boolean theSearchUrlDuplicateAcrossPartitionsEnabled) {

		return new ResourceSearchUrlEntity()
				.setPk(ResourceSearchUrlEntityPK.from(
						theUrl, theResourceTable, theSearchUrlDuplicateAcrossPartitionsEnabled))
				.setPartitionDate(Optional.ofNullable(theResourceTable.getPartitionId())
						.map(PartitionablePartitionId::getPartitionDate)
						.orElse(null))
				.setResourceTable(theResourceTable)
				.setCreatedTime(new Date());
	}

	public ResourceSearchUrlEntityPK getPk() {
		return myPk;
	}

	public ResourceSearchUrlEntity setPk(ResourceSearchUrlEntityPK thePk) {
		myPk = thePk;
		return this;
	}

	public JpaPid getResourcePid() {
		return JpaPid.fromId(myResourcePid, myPartitionIdValue);
	}

	public ResourceSearchUrlEntity setResourcePid(Long theResourcePid) {
		myResourcePid = theResourcePid;
		return this;
	}

	public ResourceSearchUrlEntity setResourceTable(ResourceTable theResourceTable) {
		this.myResourcePid = theResourceTable.getId().getId();
		this.myPartitionIdValue = theResourceTable.getPartitionId().getPartitionId();
		return this;
	}

	public Date getCreatedTime() {
		return myCreatedTime;
	}

	public ResourceSearchUrlEntity setCreatedTime(Date theCreatedTime) {
		myCreatedTime = theCreatedTime;
		return this;
	}

	public String getSearchUrl() {
		return myPk.getSearchUrl();
	}

	public Integer getPartitionId() {
		return myPk.getPartitionId();
	}

	public LocalDate getPartitionDate() {
		return myPartitionDate;
	}

	public ResourceSearchUrlEntity setPartitionDate(LocalDate thePartitionDate) {
		myPartitionDate = thePartitionDate;
		return this;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("searchUrl", getPk().getSearchUrl())
				.append("partitionId", myPartitionIdValue)
				.append("resourcePid", myResourcePid)
				.toString();
	}
}
