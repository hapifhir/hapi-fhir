package ca.uhn.fhir.jpa.dao.data;

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

import ca.uhn.fhir.jpa.model.entity.IPersistedResourceModifiedMessage;
import ca.uhn.fhir.jpa.model.entity.PersistedResourceModifiedMessageEntityPK;
import ca.uhn.fhir.jpa.model.entity.ResourceModifiedEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface IResourceModifiedDao
		extends JpaRepository<ResourceModifiedEntity, PersistedResourceModifiedMessageEntityPK>,
				IHapiFhirJpaRepository {

	@Query("SELECT r FROM ResourceModifiedEntity r ORDER BY r.myCreatedTime ASC")
	Page<IPersistedResourceModifiedMessage> findAllOrderedByCreatedTime(Pageable thePage);

	@Modifying
	@Query("delete from ResourceModifiedEntity r where r.myResourceModifiedEntityPK =:pk")
	int removeById(@Param("pk") PersistedResourceModifiedMessageEntityPK thePK);

	/**
	 * Delete a whole batch of rows with a single statement so that draining a page of
	 * HFJ_RESOURCE_MODIFIED costs one database round trip instead of one per row.
	 *
	 * @param thePKs the primary keys of the rows to delete
	 * @return the number of rows deleted
	 */
	@Modifying
	@Query("delete from ResourceModifiedEntity r where r.myResourceModifiedEntityPK in :pks")
	int removeByPks(@Param("pks") Collection<PersistedResourceModifiedMessageEntityPK> thePKs);
}
