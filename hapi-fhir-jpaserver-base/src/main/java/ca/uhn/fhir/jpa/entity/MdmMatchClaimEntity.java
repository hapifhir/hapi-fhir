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
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * A match claim taken by an MDM unit of work. The database rejects a second concurrent claim on the same
 * key, which forces MDM units of work that could affect each other into a serial order. Rows are purged
 * once they are older than the configured retention.
 *
 * @see ca.uhn.fhir.mdm.dao.IMdmMatchClaimSvc
 */
@Entity
@Table(
		name = MdmMatchClaimEntity.TABLE_NAME,
		indexes = {@Index(name = MdmMatchClaimEntity.IDX_CREATED_TIME, columnList = "CREATED_TIME")})
// Created by claude-opus-5-5
public class MdmMatchClaimEntity {

	public static final String TABLE_NAME = "MPI_MATCH_CLAIM";
	public static final String IDX_CREATED_TIME = "IDX_MPI_MATCHCLAIM_TIME";
	public static final int CLAIM_TYPE_LENGTH = 10;
	public static final int CLAIM_KEY_LENGTH = 200;

	@EmbeddedId
	private MdmMatchClaimEntityPK myPk;

	@Column(name = "CLAIM_TYPE", length = CLAIM_TYPE_LENGTH, nullable = false)
	private String myClaimType;

	/**
	 * The canonical key text, for diagnostics only. Uniqueness is enforced on the hash.
	 */
	@Column(name = "CLAIM_KEY", length = CLAIM_KEY_LENGTH, nullable = true)
	private String myClaimKey;

	@Column(name = "CLAIM_TOKEN", nullable = false)
	private Long myClaimToken;

	@Column(name = "CLAIMANT_RES_ID", nullable = true)
	private Long myClaimantResourceId;

	@Column(name = "CREATED_TIME", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date myCreatedTime;

	public MdmMatchClaimEntityPK getPk() {
		return myPk;
	}

	public MdmMatchClaimEntity setPk(MdmMatchClaimEntityPK thePk) {
		myPk = thePk;
		return this;
	}

	public String getClaimType() {
		return myClaimType;
	}

	public MdmMatchClaimEntity setClaimType(String theClaimType) {
		myClaimType = theClaimType;
		return this;
	}

	public String getClaimKey() {
		return myClaimKey;
	}

	public MdmMatchClaimEntity setClaimKey(String theClaimKey) {
		myClaimKey = StringUtils.left(theClaimKey, CLAIM_KEY_LENGTH);
		return this;
	}

	public Long getClaimToken() {
		return myClaimToken;
	}

	public MdmMatchClaimEntity setClaimToken(Long theClaimToken) {
		myClaimToken = theClaimToken;
		return this;
	}

	public Long getClaimantResourceId() {
		return myClaimantResourceId;
	}

	public MdmMatchClaimEntity setClaimantResourceId(Long theClaimantResourceId) {
		myClaimantResourceId = theClaimantResourceId;
		return this;
	}

	public Date getCreatedTime() {
		return myCreatedTime;
	}

	public MdmMatchClaimEntity setCreatedTime(Date theCreatedTime) {
		myCreatedTime = theCreatedTime;
		return this;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("pk", myPk)
				.append("claimKey", myClaimKey)
				.append("claimToken", myClaimToken)
				.append("claimant", myClaimantResourceId)
				.toString();
	}

	/**
	 * Multi-column primary key for {@link MdmMatchClaimEntity}.
	 */
	@Embeddable
	public static class MdmMatchClaimEntityPK implements Serializable {
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
}
