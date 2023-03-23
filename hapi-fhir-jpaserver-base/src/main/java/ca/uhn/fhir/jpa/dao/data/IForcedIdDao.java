/*
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.dao.data;

import ca.uhn.fhir.jpa.dao.data.custom.IForcedIdQueries;
import ca.uhn.fhir.jpa.model.entity.ForcedId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IForcedIdDao extends JpaRepository<ForcedId, Long>, IHapiFhirJpaRepository, IForcedIdQueries {

	@Query("SELECT f FROM ForcedId f WHERE f.myResourcePid IN (:resource_pids)")
	List<ForcedId> findAllByResourcePid(@Param("resource_pids") List<Long> theResourcePids);

	@Query("SELECT f FROM ForcedId f WHERE f.myResourcePid = :resource_pid")
	Optional<ForcedId> findByResourcePid(@Param("resource_pid") Long theResourcePid);

	@Modifying
	@Query("DELETE FROM ForcedId t WHERE t.myId = :pid")
	void deleteByPid(@Param("pid") Long theId);
}
