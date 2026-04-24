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

import ca.uhn.fhir.jpa.model.dialect.HapiFhirH2Dialect;
import ca.uhn.fhir.jpa.model.dialect.HapiFhirOracleDialect;
import ca.uhn.fhir.jpa.model.dialect.HapiFhirPostgresDialect;
import ca.uhn.fhir.jpa.model.dialect.HapiFhirSQLServerDialect;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.DialectOverride;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.SQLInsert;

import java.io.Serializable;
import java.util.Objects;

/**
 * Global, deduplicated token-index row: one entry per unique
 * {@code (resourceType, paramName, system, value)} hashed into {@code HASH_SYS_AND_VALUE}. The
 * per-resource link lives in {@link ResourceIndexedSearchParamTokenCommonRes}.
 *
 * <p>Concurrent inserts race on the same PK, so the per-dialect
 * {@link org.hibernate.annotations.DialectOverride.SQLInsert} annotations swap in upserts that
 * ignore conflicts. Marked {@link org.hibernate.annotations.Immutable} — the PK is a content hash,
 * so an UPDATE is never needed.
 *
 * <p><b>Bind order</b> in each dialect's SQL must match Hibernate's default for {@code @SQLInsert}:
 * non-{@code @Id} fields alphabetical by Java name, {@code @Id} last
 * ({@code HASH_IDENTITY, HASH_VALUE, SYSTEM_ID, SP_VALUE, HASH_SYS_AND_VALUE}). Reorder SQL and
 * fields together.
 */
@Entity
@Immutable
@Table(
		name = ResourceIndexedSearchParamTokenCommon.HFJ_SPIDX2_TOKEN_COMMON,
		indexes = {
			@Index(name = "IDX_SP2_TOKEN_COMMON_SYSTEM", columnList = "SYSTEM_ID,HASH_SYS_AND_VALUE"),
			@Index(name = "IDX_SP2_TOKEN_COMMON_VALUE", columnList = "HASH_VALUE,HASH_SYS_AND_VALUE"),
			@Index(name = "IDX_SP2_TOKEN_COMMON_IDENTITY", columnList = "HASH_IDENTITY,HASH_SYS_AND_VALUE")
		})
@DialectOverride.SQLInsert(
		dialect = HapiFhirH2Dialect.class,
		override =
				@SQLInsert(
						sql =
								"""
MERGE INTO HFJ_SPIDX2_TOKEN_COMMON target
USING (VALUES (?,?,?,?,?)) AS src (HASH_IDENTITY, HASH_VALUE, SYSTEM_ID, SP_VALUE, HASH_SYS_AND_VALUE)
ON (target.HASH_SYS_AND_VALUE = src.HASH_SYS_AND_VALUE)
WHEN NOT MATCHED THEN
INSERT (HASH_IDENTITY, HASH_VALUE, SYSTEM_ID, SP_VALUE, HASH_SYS_AND_VALUE)
VALUES (src.HASH_IDENTITY, src.HASH_VALUE, src.SYSTEM_ID, src.SP_VALUE, src.HASH_SYS_AND_VALUE)
"""))
@DialectOverride.SQLInsert(
		dialect = HapiFhirPostgresDialect.class,
		override =
				@SQLInsert(
						sql =
								"""
INSERT INTO HFJ_SPIDX2_TOKEN_COMMON (HASH_IDENTITY, HASH_VALUE, SYSTEM_ID, SP_VALUE, HASH_SYS_AND_VALUE)
VALUES(?,?,?,?,?) ON CONFLICT DO NOTHING
"""))
@DialectOverride.SQLInsert(
		dialect = HapiFhirOracleDialect.class,
		override =
				@SQLInsert(
						sql =
								"""
MERGE INTO HFJ_SPIDX2_TOKEN_COMMON target
USING (SELECT ? AS HASH_IDENTITY, ? AS HASH_VALUE, ? AS SYSTEM_ID, ? AS SP_VALUE, ? AS HASH_SYS_AND_VALUE FROM DUAL) src
ON (target.HASH_SYS_AND_VALUE = src.HASH_SYS_AND_VALUE)
WHEN NOT MATCHED THEN
INSERT (HASH_IDENTITY, HASH_VALUE, SYSTEM_ID, SP_VALUE, HASH_SYS_AND_VALUE)
VALUES (src.HASH_IDENTITY, src.HASH_VALUE, src.SYSTEM_ID, src.SP_VALUE, src.HASH_SYS_AND_VALUE)
"""))
@DialectOverride.SQLInsert(
		dialect = HapiFhirSQLServerDialect.class,
		override =
				@SQLInsert(
						sql =
								"""
INSERT INTO HFJ_SPIDX2_TOKEN_COMMON (HASH_IDENTITY, HASH_VALUE, SYSTEM_ID, SP_VALUE, HASH_SYS_AND_VALUE)
SELECT src.HASH_IDENTITY, src.HASH_VALUE, src.SYSTEM_ID, src.SP_VALUE, src.HASH_SYS_AND_VALUE
FROM (VALUES (?, ?, ?, ?, ?)) AS src (HASH_IDENTITY, HASH_VALUE, SYSTEM_ID, SP_VALUE, HASH_SYS_AND_VALUE)
WHERE NOT EXISTS (
	SELECT 1 FROM HFJ_SPIDX2_TOKEN_COMMON WITH (UPDLOCK, HOLDLOCK) WHERE HASH_SYS_AND_VALUE = src.HASH_SYS_AND_VALUE
)
"""))
public class ResourceIndexedSearchParamTokenCommon implements Serializable {
	public static final String HFJ_SPIDX2_TOKEN_COMMON = "HFJ_SPIDX2_TOKEN_COMMON";
	public static final int MAX_LENGTH = 200;

	@Column(name = "HASH_IDENTITY", nullable = false, updatable = false)
	private long myHashIdentity;

	@Column(name = "HASH_VALUE", nullable = false, updatable = false)
	private long myHashValue;

	@Column(name = "SYSTEM_ID", nullable = true, updatable = false)
	private Long mySystemId;

	@Column(name = "SP_VALUE", length = MAX_LENGTH, nullable = false, updatable = false)
	private String myValue;

	@Id
	@Column(name = "HASH_SYS_AND_VALUE", nullable = false, updatable = false)
	private long myHashSystemAndValue;

	public ResourceIndexedSearchParamTokenCommon() {}

	public long getHashSystemAndValue() {
		return myHashSystemAndValue;
	}

	public ResourceIndexedSearchParamTokenCommon setHashSystemAndValue(long theHashSystemAndValue) {
		myHashSystemAndValue = theHashSystemAndValue;
		return this;
	}

	public Long getSystemId() {
		return mySystemId;
	}

	public ResourceIndexedSearchParamTokenCommon setSystemId(Long theSystemId) {
		mySystemId = theSystemId;
		return this;
	}

	public String getValue() {
		return myValue;
	}

	public ResourceIndexedSearchParamTokenCommon setValue(String theValue) {
		myValue = theValue;
		return this;
	}

	public long getHashIdentity() {
		return myHashIdentity;
	}

	public ResourceIndexedSearchParamTokenCommon setHashIdentity(long theHashIdentity) {
		myHashIdentity = theHashIdentity;
		return this;
	}

	public long getHashValue() {
		return myHashValue;
	}

	public ResourceIndexedSearchParamTokenCommon setHashValue(long theHashValue) {
		myHashValue = theHashValue;
		return this;
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) return true;
		if (!(theO instanceof ResourceIndexedSearchParamTokenCommon that)) return false;
		return myHashSystemAndValue == that.myHashSystemAndValue
				&& myHashIdentity == that.myHashIdentity
				&& myHashValue == that.myHashValue
				&& Objects.equals(mySystemId, that.mySystemId)
				&& Objects.equals(myValue, that.myValue);
	}

	@Override
	public int hashCode() {
		return Objects.hash(myHashSystemAndValue, myHashIdentity, myHashValue, mySystemId, myValue);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("hashSysAndValue", myHashSystemAndValue)
				.append("hashIdentity", myHashIdentity)
				.append("hashValue", myHashValue)
				.append("systemId", mySystemId)
				.append("value", myValue)
				.toString();
	}
}
