/*
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.dao.data;

import ca.uhn.fhir.jpa.model.entity.ForcedId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Legacy forced_id implementation.
 *
 * @deprecated we now have a fhir_id column directly on HFJ_RESOURCE.
 * No runtime code should query this table except for deletions by PK.
 * To be deleted in 2024 (zero-downtime).
 */
@Deprecated(since = "6.10")
@Repository
public interface IForcedIdDao extends JpaRepository<ForcedId, Long>, IHapiFhirJpaRepository {

	@Modifying
	@Query("DELETE FROM ForcedId t WHERE t.myId = :pid")
	void deleteByPid(@Param("pid") Long theId);
}
