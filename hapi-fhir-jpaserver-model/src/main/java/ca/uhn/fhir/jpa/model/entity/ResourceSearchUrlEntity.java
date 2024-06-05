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
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.util.Date;

import static ca.uhn.fhir.jpa.model.entity.ResourceSearchUrlWithPartitionEntityPK.RES_SEARCH_URL_COLUMN_NAME;
import static ca.uhn.fhir.jpa.model.entity.ResourceSearchUrlWithPartitionEntityPK.RES_SEARCH_URL_LENGTH;

/**
 * This entity is used to enforce uniqueness on a given search URL being
 * used as a conditional operation URL, e.g. a conditional create or a
 * conditional update. When we perform a conditional operation that is
 * creating a new resource, we store an entity with the conditional URL
 * in this table. The URL is the PK of the table, so the database
 * ensures that two concurrent threads don't accidentally create two
 * resources with the same conditional URL.
 */
@Entity
@Table(
		name = "HFJ_RES_SEARCH_URL",
		indexes = {
			@Index(name = "IDX_RESSEARCHURL_RES", columnList = "RES_ID"),
			@Index(name = "IDX_RESSEARCHURL_TIME", columnList = "CREATED_TIME")
		})
public class ResourceSearchUrlEntity {

	@Id
	@Column(name = RES_SEARCH_URL_COLUMN_NAME, length = RES_SEARCH_URL_LENGTH, nullable = false)
	private String mySearchUrl;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
			name = "RES_ID",
			nullable = false,
			updatable = false,
			foreignKey = @ForeignKey(name = "FK_RES_SEARCH_URL_RESOURCE"))
	private ResourceTable myResourceTable;

	@Column(name = "RES_ID", updatable = false, nullable = false, insertable = false)
	private Long myResourcePid;

	@Column(name = "CREATED_TIME", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date myCreatedTime;

	public static ResourceSearchUrlEntity from(String theUrl, ResourceTable theResourceTable) {
		return new ResourceSearchUrlEntity()
				.setResourceTable(theResourceTable)
				.setSearchUrl(theUrl)
				.setCreatedTime(new Date());
	}

	public Long getResourcePid() {
		if (myResourcePid != null) {
			return myResourcePid;
		}
		return myResourceTable.getResourceId();
	}

	public ResourceSearchUrlEntity setResourcePid(Long theResourcePid) {
		myResourcePid = theResourcePid;
		return this;
	}

	public ResourceTable getResourceTable() {
		return myResourceTable;
	}

	public ResourceSearchUrlEntity setResourceTable(ResourceTable myResourceTable) {
		this.myResourceTable = myResourceTable;
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
		return mySearchUrl;
	}

	public ResourceSearchUrlEntity setSearchUrl(String theSearchUrl) {
		mySearchUrl = theSearchUrl;
		return this;
	}
}
