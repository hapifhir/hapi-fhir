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

import ca.uhn.fhir.jpa.model.entity.ResourceSearchUrlEntity;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.hibernate.Timeouts;
import org.hibernate.jpa.SpecHints;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface IResourceSearchUrlDao extends JpaRepository<ResourceSearchUrlEntity, Long>, IHapiFhirJpaRepository {

	/**
	 * Selects the resource PIDs of up to {@code theLimit} entries created before {@code theCutoff} and
	 * write-locks those rows until the enclosing transaction ends. Rows already locked by another
	 * transaction (an ingestion write clearing its own entry, or a concurrent cleanup on another node)
	 * are skipped instead of waited on: {@code WITH (UPDLOCK, ROWLOCK, READPAST)} on MSSQL,
	 * {@code FOR UPDATE SKIP LOCKED} on Postgres and Oracle. On databases without skip-locked support
	 * the rows are locked without skipping.
	 */
	@Query("SELECT s.myResourcePid FROM ResourceSearchUrlEntity s WHERE s.myCreatedTime < :cutoff")
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@QueryHints(@QueryHint(name = SpecHints.HINT_SPEC_LOCK_TIMEOUT, value = "" + Timeouts.SKIP_LOCKED_MILLI))
	List<Long> findStaleIdsSkipLocked(@Param("cutoff") Date theCutoff, Limit theLimit);

	@Modifying
	@Query("DELETE FROM ResourceSearchUrlEntity s WHERE (s.myResourcePid = :resID)")
	int deleteByResId(@Param("resID") long resId);

	@Modifying
	@Query("DELETE FROM ResourceSearchUrlEntity s WHERE s.myResourcePid IN (:resIDS)")
	int deleteByResIds(@Param("resIDS") List<Long> theIds);
}
