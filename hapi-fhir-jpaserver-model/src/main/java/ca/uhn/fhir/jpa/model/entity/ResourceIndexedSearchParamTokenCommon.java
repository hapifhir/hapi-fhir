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

import ca.uhn.fhir.jpa.model.dialect.HapiFhirOracleDialect;
import ca.uhn.fhir.jpa.model.dialect.HapiFhirPostgresDialect;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import org.hibernate.annotations.DialectOverride;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.SQLInsert;

import java.io.Serializable;

@Entity
@Immutable
@Table(
		name = ResourceIndexedSearchParamTokenCommon.HFJ_SPIDX2_TOKEN_COMMON,
		indexes = {
			@Index(name = "IDX_SP_TOKEN_COMMON_SYSTEM", columnList = "SYSTEM_ID,HASH_SYS_AND_VALUE"),
			@Index(name = "IDX_SP_TOKEN_COMMON_VALUE", columnList = "HASH_VALUE,HASH_SYS_AND_VALUE"),
			@Index(name = "IDX_SP_TOKEN_IDENTITY", columnList = "HASH_IDENTITY,HASH_SYS_AND_VALUE")
		})
@DialectOverride.SQLInserts({
	@DialectOverride.SQLInsert(
		dialect = HapiFhirPostgresDialect.class,
		override = @SQLInsert(sql = """
						insert into HFJ_SPIDX2_TOKEN_COMMON (HASH_SYS_AND_VALUE, SYSTEM_ID, SP_VALUE, HASH_IDENTITY, HASH_VALUE) 
						values(?,?,?,?,?) 
						on conflict do nothing
						""")),
	@DialectOverride.SQLInsert(
		dialect = HapiFhirOracleDialect.class,
		override = @SQLInsert(sql = """
						MERGE INTO HFJ_SPIDX2_TOKEN_COMMON target
						USING (SELECT ? AS HASH_SYS_AND_VALUE, ? AS SYSTEM_ID, ? AS SP_VALUE, ? AS HASH_IDENTITY, ? AS HASH_VALUE FROM DUAL) src
						ON (target.HASH_SYS_AND_VALUE = src.HASH_SYS_AND_VALUE)
						WHEN NOT MATCHED THEN
						  INSERT (HASH_SYS_AND_VALUE, SYSTEM_ID, SP_VALUE, HASH_IDENTITY, HASH_VALUE)
						  VALUES (src.HASH_SYS_AND_VALUE, src.SYSTEM_ID, src.SP_VALUE, src.HASH_IDENTITY, src.HASH_VALUE)
						""")),

})
public class ResourceIndexedSearchParamTokenCommon implements Serializable {
	public static final String HFJ_SPIDX2_TOKEN_COMMON = "HFJ_SPIDX2_TOKEN_COMMON";
	public static final int MAX_LENGTH = 200;

	@Id
	@Column(name = "HASH_SYS_AND_VALUE", nullable = false, updatable = false)
	private long myHashSystemAndValue;

	@Column(name = "SYSTEM_ID", nullable = false, updatable = false)
	private long mySystemId;

	@Column(name = "SP_VALUE", length = MAX_LENGTH, nullable = false, updatable = false)
	private String myValue;

	@Column(name = "HASH_IDENTITY", nullable = false, updatable = false)
	private long myHashIdentity;

	@Column(name = "HASH_VALUE", nullable = false, updatable = false)
	private long myHashValue;

	public ResourceIndexedSearchParamTokenCommon() {}
}
