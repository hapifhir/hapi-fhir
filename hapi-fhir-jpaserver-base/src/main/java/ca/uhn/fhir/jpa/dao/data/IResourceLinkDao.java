/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.model.primitive.IdDt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.stream.Stream;

public interface IResourceLinkDao extends JpaRepository<ResourceLink, Long>, IHapiFhirJpaRepository {

	@Modifying
	@Query("DELETE FROM ResourceLink t WHERE t.mySourceResource.myPid = :resId")
	void deleteByResourceId(@Param("resId") JpaPid theResourcePid);

	@Query("SELECT t FROM ResourceLink t WHERE t.mySourceResource.myPid = :resId")
	List<ResourceLink> findAllForSourceResourceId(@Param("resId") JpaPid thePatientId);

	@Query("SELECT t FROM ResourceLink t WHERE t.myTargetResource.myPid in :resIds")
	List<ResourceLink> findWithTargetPidIn(@Param("resIds") List<JpaPid> thePids);

	/**
	 * Loads a collection of ResourceLink entities by PID, but also  eagerly fetches
	 * the target resources and their forced IDs
	 */
	@Query("SELECT t FROM ResourceLink t LEFT JOIN FETCH t.myTargetResource tr WHERE t.myId in :pids")
	List<ResourceLink> findByPidAndFetchTargetDetails(@Param("pids") List<Long> thePids);

	/**
	 * Stream Resource Ids of all resources that have a reference to the provided resource id
	 *
	 * @param theTargetResourceType the resource type part of the id
	 * @param theTargetResourceFhirId the value part of the id
	 * @return
	 */
	@Query(
			"SELECT DISTINCT new ca.uhn.fhir.model.primitive.IdDt(t.mySourceResourceType, t.mySourceResource.myFhirId) FROM ResourceLink t WHERE t.myTargetResourceType = :resourceType AND t.myTargetResource.myFhirId = :resourceFhirId")
	Stream<IdDt> streamSourceIdsForTargetFhirId(
			@Param("resourceType") String theTargetResourceType,
			@Param("resourceFhirId") String theTargetResourceFhirId);

	/**
	 * Count the number of resources that have a reference to the provided resource id
	 *
	 * @param theTargetResourceType the resource type part of the id
	 * @param theTargetResourceFhirId the value part of the id
	 * @return
	 */
	@Query(
			"SELECT COUNT(DISTINCT t.mySourceResourcePid) FROM ResourceLink t WHERE t.myTargetResourceType = :resourceType AND t.myTargetResource.myFhirId = :resourceFhirId")
	Integer countResourcesTargetingFhirTypeAndFhirId(
			@Param("resourceType") String theTargetResourceType,
			@Param("resourceFhirId") String theTargetResourceFhirId);
}
