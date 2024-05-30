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
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

@Embeddable
public class ResourceSearchUrlWithPartitionEntityPK implements Serializable {
	public static final String RESSEARCHURLPARTITTION_ID = "RESSEARCHURLPARTITTION_ID ";
	public static final String RES_SEARCH_URL_COLUMN_NAME = "RES_SEARCH_URL";
	public static final String PARTITION_ID = "PARTITION_ID";
	// LUKETODO:  Error executing DDL "create index IDX_RESSEARCHURL_RES on HFJ_RES_SEARCH_URL_PARTITION_ID (RES_ID)"
	// via JDBC [Index "IDX_RESSEARCHURL_RES" already exists;]

	public static final int RES_SEARCH_URL_LENGTH = 768;
	private static final long serialVersionUID = 1L;

	@Column(name = RES_SEARCH_URL_COLUMN_NAME, length = RES_SEARCH_URL_LENGTH, nullable = false)
	// Weird field name isto ensure that this the first key in the index
	private String my_A_SearchUrl;

	@Column(name = PARTITION_ID, nullable = false, insertable = true, updatable = false)
	// Weird field name isto ensure that this the second key in the index
	private Integer my_B_PartitionId;

	// LUKETODO:  add a new config key to toggle the partition_id behaviour
	public ResourceSearchUrlWithPartitionEntityPK() {}

	public static ResourceSearchUrlWithPartitionEntityPK from(String theSearchUrl, ResourceTable theResourceTable) {
		return new ResourceSearchUrlWithPartitionEntityPK(
				theSearchUrl,
				Optional.ofNullable(theResourceTable.getPartitionId())
						.map(PartitionablePartitionId::getPartitionId)
						.orElse(-1));
	}

	public ResourceSearchUrlWithPartitionEntityPK(String theSearchUrl, int thePartitionId) {
		my_A_SearchUrl = theSearchUrl;
		my_B_PartitionId = thePartitionId;
	}

	public String getSearchUrl() {
		return my_A_SearchUrl;
	}

	public void setSearchUrl(String theMy_A_SearchUrl) {
		my_A_SearchUrl = theMy_A_SearchUrl;
	}

	public Integer getPartitionId() {
		return my_B_PartitionId;
	}

	public void setPartitionId(Integer theMy_B_PartitionId) {
		my_B_PartitionId = theMy_B_PartitionId;
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) {
			return true;
		}
		if (theO == null || getClass() != theO.getClass()) {
			return false;
		}
		ResourceSearchUrlWithPartitionEntityPK that = (ResourceSearchUrlWithPartitionEntityPK) theO;
		return Objects.equals(my_A_SearchUrl, that.my_A_SearchUrl)
				&& Objects.equals(my_B_PartitionId, that.my_B_PartitionId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(my_A_SearchUrl, my_B_PartitionId);
	}
}
