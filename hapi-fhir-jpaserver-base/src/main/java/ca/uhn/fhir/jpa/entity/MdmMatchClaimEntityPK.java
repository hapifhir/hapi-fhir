/*-
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Multi-column primary key for {@link MdmMatchClaimEntity}.
 */
@Embeddable
// Created by claude-opus-5-5
public class MdmMatchClaimEntityPK implements Serializable {
	public static final String CLAIM_HASH_COLUMN_NAME = "CLAIM_HASH";
	public static final String PARTITION_ID_COLUMN_NAME = "PARTITION_ID";

	private static final long serialVersionUID = 1L;

	@Column(name = CLAIM_HASH_COLUMN_NAME, nullable = false)
	// Weird field name is to ensure that this is the first key in the index
	private Long my_A_ClaimHash;

	@Column(name = PARTITION_ID_COLUMN_NAME, nullable = false)
	// Weird field name is to ensure that this is the second key in the index
	private Integer my_B_PartitionId;

	public MdmMatchClaimEntityPK() {}

	public MdmMatchClaimEntityPK(long theClaimHash, int thePartitionId) {
		my_A_ClaimHash = theClaimHash;
		my_B_PartitionId = thePartitionId;
	}

	public Long getClaimHash() {
		return my_A_ClaimHash;
	}

	public Integer getPartitionId() {
		return my_B_PartitionId;
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) {
			return true;
		}
		if (theO == null || getClass() != theO.getClass()) {
			return false;
		}
		MdmMatchClaimEntityPK that = (MdmMatchClaimEntityPK) theO;
		return Objects.equals(my_A_ClaimHash, that.my_A_ClaimHash)
				&& Objects.equals(my_B_PartitionId, that.my_B_PartitionId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(my_A_ClaimHash, my_B_PartitionId);
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", MdmMatchClaimEntityPK.class.getSimpleName() + "[", "]")
				.add("claimHash=" + my_A_ClaimHash)
				.add("partitionId=" + my_B_PartitionId)
				.toString();
	}
}
