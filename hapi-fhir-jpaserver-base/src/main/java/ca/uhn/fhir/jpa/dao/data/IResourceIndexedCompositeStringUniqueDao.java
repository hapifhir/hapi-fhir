package ca.uhn.fhir.jpa.dao.data;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

import ca.uhn.fhir.jpa.model.entity.ResourceIndexedCompositeStringUnique;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IResourceIndexedCompositeStringUniqueDao extends JpaRepository<ResourceIndexedCompositeStringUnique, Long> {

	@Query("SELECT r FROM ResourceIndexedCompositeStringUnique r WHERE r.myIndexString = :str")
	ResourceIndexedCompositeStringUnique findByQueryString(@Param("str") String theQueryString);

	@Query("SELECT r FROM ResourceIndexedCompositeStringUnique r WHERE r.myResourceId = :resId")
	List<ResourceIndexedCompositeStringUnique> findAllForResourceIdForUnitTest(@Param("resId") Long theResourceId);

	@Modifying
	@Query("delete from ResourceIndexedCompositeStringUnique t WHERE t.myResourceId = :resid")
	void deleteByResourceId(@Param("resid") Long theResourcePid);
}
