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
package ca.uhn.fhir.jpa.dao.data;

import ca.uhn.fhir.jpa.entity.MdmMatchClaimEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Date;
import java.util.List;

// Created by claude-opus-5-5
public interface IMdmMatchClaimJpaRepository extends JpaRepository<MdmMatchClaimEntity, Long>, IHapiFhirJpaRepository {

	/**
	 * @return rows of {@code [claimHash, claimToken]} for existing claims on the given hashes
	 */
	@Query("SELECT c.myClaimHash, c.myClaimToken FROM MdmMatchClaimEntity c WHERE c.myClaimHash IN (:hashes)")
	List<Object[]> findTokens(@Param("hashes") Collection<Long> theHashes);

	@Modifying
	@Query("DELETE FROM MdmMatchClaimEntity c WHERE c.myClaimHash = :hash AND c.myClaimToken = :token")
	int deleteByHashAndToken(@Param("hash") long theHash, @Param("token") long theToken);

	@Query("SELECT c.myClaimHash FROM MdmMatchClaimEntity c WHERE c.myCreatedTime < :cutoff")
	Slice<Long> findStaleHashes(@Param("cutoff") Date theCutoff, Pageable thePageable);

	@Modifying
	@Query("DELETE FROM MdmMatchClaimEntity c WHERE c.myClaimHash IN (:hashes) AND c.myCreatedTime < :cutoff")
	int deleteStale(@Param("hashes") List<Long> theHashes, @Param("cutoff") Date theCutoff);
}
