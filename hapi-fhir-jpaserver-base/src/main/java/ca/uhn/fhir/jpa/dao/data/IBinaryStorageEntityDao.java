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

import ca.uhn.fhir.jpa.model.entity.BinaryStorageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IBinaryStorageEntityDao extends JpaRepository<BinaryStorageEntity, String>, IHapiFhirJpaRepository {

	@Query("SELECT e FROM BinaryStorageEntity e WHERE e.myBlobId = :blob_id AND e.myResourceId = :resource_id")
	Optional<BinaryStorageEntity> findByIdAndResourceId(@Param("blob_id") String theBlobId, @Param("resource_id") String theResourceId);

	@Modifying
	@Query("DELETE FROM BinaryStorageEntity t WHERE t.myBlobId = :pid")
	void deleteByPid(@Param("pid") String theId);
}
