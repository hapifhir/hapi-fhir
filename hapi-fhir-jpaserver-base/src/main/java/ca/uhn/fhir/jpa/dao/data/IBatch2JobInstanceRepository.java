package ca.uhn.fhir.jpa.dao.data;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.jpa.entity.Batch2JobInstanceEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IBatch2JobInstanceRepository extends JpaRepository<Batch2JobInstanceEntity, String>, IHapiFhirJpaRepository {

	@Modifying
	@Query("UPDATE Batch2JobInstanceEntity e SET e.myStatus = :status WHERE e.myId = :id")
	void updateInstanceStatus(@Param("id") String theInstanceId, @Param("status") StatusEnum theInProgress);

	@Query("SELECT e FROM Batch2JobInstanceEntity e ORDER BY e.myCreateTime ASC")
	List<Batch2JobInstanceEntity> fetchAll(Pageable thePageRequest);

	@Modifying
	@Query("UPDATE Batch2JobInstanceEntity e SET e.myCancelled = :cancelled WHERE e.myId = :id")
	void updateInstanceCancelled(@Param("id") String theInstanceId, @Param("cancelled") boolean theCancelled);
}
