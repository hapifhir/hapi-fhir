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
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

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

	/**
	 * Hash of the canonical claim key. Uniqueness of the claim is enforced on this column.
	 */
	@Id
	@Column(name = "CLAIM_HASH", nullable = false)
	private Long myClaimHash;

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

	public Long getClaimHash() {
		return myClaimHash;
	}

	public MdmMatchClaimEntity setClaimHash(Long theClaimHash) {
		myClaimHash = theClaimHash;
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
				.append("claimHash", myClaimHash)
				.append("claimKey", myClaimKey)
				.append("claimToken", myClaimToken)
				.append("claimant", myClaimantResourceId)
				.toString();
	}
}
