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

import ca.uhn.fhir.jpa.model.entity.ResourceIndexedComboStringUnique;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IResourceIndexedComboStringUniqueDao extends JpaRepository<ResourceIndexedComboStringUnique, Long> {

	@Query("SELECT r FROM ResourceIndexedComboStringUnique r WHERE r.myIndexString = :str")
	ResourceIndexedComboStringUnique findByQueryString(@Param("str") String theQueryString);

	@Query("SELECT r FROM ResourceIndexedComboStringUnique r WHERE r.myResourceId = :resId")
	List<ResourceIndexedComboStringUnique> findAllForResourceIdForUnitTest(@Param("resId") Long theResourceId);

	@Modifying
	@Query("delete from ResourceIndexedComboStringUnique t WHERE t.myResourceId = :resid")
	void deleteByResourceId(@Param("resid") Long theResourcePid);
}
