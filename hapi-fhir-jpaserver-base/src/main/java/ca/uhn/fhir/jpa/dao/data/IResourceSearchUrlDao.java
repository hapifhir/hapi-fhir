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
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface IResourceSearchUrlDao extends JpaRepository<ResourceSearchUrlEntity, Long>, IHapiFhirJpaRepository {

	/**
	 * Deletes up to {@code theLimit} entries created before {@code theCutoff}, oldest first, in a single
	 * statement. Hibernate renders the row limit in each vendor's own syntax ({@code TOP} on MSSQL,
	 * {@code FETCH FIRST} elsewhere). It requires an {@code ORDER BY} with a limit in a subquery;
	 * ordering by {@code CREATED_TIME} follows the {@code IDX_RESSEARCHURL_TIME} index the filter uses.
	 *
	 * @return the number of entries deleted
	 */
	@Modifying
	@Query("DELETE FROM ResourceSearchUrlEntity s WHERE s.myResourcePid IN ("
			+ "SELECT s2.myResourcePid FROM ResourceSearchUrlEntity s2 WHERE s2.myCreatedTime < :cutoff "
			+ "ORDER BY s2.myCreatedTime LIMIT :limit)")
	int deleteStaleEntries(@Param("cutoff") Date theCutoff, @Param("limit") int theLimit);

	@Modifying
	@Query("DELETE FROM ResourceSearchUrlEntity s WHERE (s.myResourcePid = :resID)")
	int deleteByResId(@Param("resID") long resId);

	@Modifying
	@Query("DELETE FROM ResourceSearchUrlEntity s WHERE s.myResourcePid IN (:resIDS)")
	int deleteByResIds(@Param("resIDS") List<Long> theIds);
}
