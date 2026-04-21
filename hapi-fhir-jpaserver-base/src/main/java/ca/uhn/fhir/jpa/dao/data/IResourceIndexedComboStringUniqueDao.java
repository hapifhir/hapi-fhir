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

import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedComboStringUnique;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IResourceIndexedComboStringUniqueDao extends JpaRepository<ResourceIndexedComboStringUnique, Long> {

	@Query(
			"SELECT r FROM ResourceIndexedComboStringUnique r JOIN FETCH r.myResource WHERE r.myPartitionIdValue IS NULL AND r.myIndexString IN (:str)")
	List<ResourceIndexedComboStringUnique> findByQueryStringNullPartitionAndFetchResource(
			@Param("str") List<String> theQueryStrings);

	@Query(
			"SELECT r FROM ResourceIndexedComboStringUnique r JOIN FETCH r.myResource WHERE r.myPartitionIdValue = :pid AND r.myIndexString IN (:str)")
	List<ResourceIndexedComboStringUnique> findByQueryStringAndFetchResource(
			@Param("pid") Integer thePartitionId, @Param("str") List<String> theQueryStrings);

	@Query("SELECT r FROM ResourceIndexedComboStringUnique r WHERE r.myResource.myPid = :resId")
	List<ResourceIndexedComboStringUnique> findAllForResourceIdForUnitTest(@Param("resId") JpaPid theResourceId);

	@Modifying
	@Query("delete from ResourceIndexedComboStringUnique t WHERE t.myResource.myPid = :resid")
	void deleteByResourceId(@Param("resid") JpaPid theResourcePid);
}
