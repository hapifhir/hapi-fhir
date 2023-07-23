/*-
 * #%L
 * HAPI FHIR JPA Model
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(
		name = "HFJ_RES_SEARCH_URL",
		indexes = {
			@Index(name = "IDX_RESSEARCHURL_RES", columnList = "RES_ID"),
			@Index(name = "IDX_RESSEARCHURL_TIME", columnList = "CREATED_TIME")
		})
public class ResourceSearchUrlEntity {

	public static final String RES_SEARCH_URL_COLUMN_NAME = "RES_SEARCH_URL";

	public static final int RES_SEARCH_URL_LENGTH = 768;

	@Id
	@Column(name = RES_SEARCH_URL_COLUMN_NAME, length = RES_SEARCH_URL_LENGTH, nullable = false)
	private String mySearchUrl;

	@Column(name = "RES_ID", updatable = false, nullable = false)
	private Long myResourcePid;

	@Column(name = "CREATED_TIME", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date myCreatedTime;

	public static ResourceSearchUrlEntity from(String theUrl, Long theId) {
		return new ResourceSearchUrlEntity()
				.setResourcePid(theId)
				.setSearchUrl(theUrl)
				.setCreatedTime(new Date());
	}

	public Long getResourcePid() {
		return myResourcePid;
	}

	public ResourceSearchUrlEntity setResourcePid(Long theResourcePid) {
		myResourcePid = theResourcePid;
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
