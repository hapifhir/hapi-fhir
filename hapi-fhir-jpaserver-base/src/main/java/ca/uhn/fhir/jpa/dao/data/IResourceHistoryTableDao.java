package ca.uhn.fhir.jpa.dao.data;

import java.util.Collection;
import java.util.Date;

import javax.persistence.TemporalType;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.query.Param;

import ca.uhn.fhir.jpa.entity.ResourceHistoryTable;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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

public interface IResourceHistoryTableDao extends JpaRepository<ResourceHistoryTable, Long> {

	@Query("SELECT COUNT(*) FROM ResourceHistoryTable t WHERE t.myUpdated >= :cutoff")
	int countForAllResourceTypes(
		@Temporal(value = TemporalType.TIMESTAMP) @Param("cutoff") Date theCutoff
	);

	@Query("SELECT COUNT(*) FROM ResourceHistoryTable t")
	int countForAllResourceTypes(
	);

	@Query("SELECT COUNT(*) FROM ResourceHistoryTable t WHERE t.myResourceId = :id AND t.myUpdated >= :cutoff")
	int countForResourceInstance(
		@Param("id") Long theId,
		@Temporal(value = TemporalType.TIMESTAMP) @Param("cutoff") Date theCutoff
	);

	@Query("SELECT COUNT(*) FROM ResourceHistoryTable t WHERE t.myResourceId = :id")
	int countForResourceInstance(
		@Param("id") Long theId
	);

	@Query("SELECT COUNT(*) FROM ResourceHistoryTable t WHERE t.myResourceType = :type AND t.myUpdated >= :cutoff")
	int countForResourceType(
		@Param("type") String theType,
		@Temporal(value = TemporalType.TIMESTAMP) @Param("cutoff") Date theCutoff
	);

	@Query("SELECT COUNT(*) FROM ResourceHistoryTable t WHERE t.myResourceType = :type")
	int countForResourceType(
		@Param("type") String theType
	);

	@Query("SELECT t FROM ResourceHistoryTable t WHERE t.myResourceId = :id AND t.myResourceVersion = :version")
	ResourceHistoryTable findForIdAndVersion(@Param("id") long theId, @Param("version") long theVersion);

	@Query("SELECT t.myId FROM ResourceHistoryTable t WHERE t.myResourceId = :resId AND t.myResourceVersion != :dontWantVersion")
	Slice<Long> findForResourceId(Pageable thePage, @Param("resId") Long theId, @Param("dontWantVersion") Long theDontWantVersion);

	@Query("" +
		"SELECT v.myId FROM ResourceHistoryTable v " +
		"LEFT OUTER JOIN ResourceTable t ON (v.myResourceId = t.myId) " +
		"WHERE v.myResourceVersion != t.myVersion AND " +
		"t.myResourceType = :restype")
	Slice<Long> findIdsOfPreviousVersionsOfResources(Pageable thePage, @Param("restype") String theResourceName);

	@Query("" +
		"SELECT v.myId FROM ResourceHistoryTable v " +
		"LEFT OUTER JOIN ResourceTable t ON (v.myResourceId = t.myId) " +
		"WHERE v.myResourceVersion != t.myVersion")
	Slice<Long> findIdsOfPreviousVersionsOfResources(Pageable thePage);
	
	@Query("" + 
		"SELECT h FROM ResourceHistoryTable h " + 
		"INNER JOIN ResourceTable r ON (r.myId = h.myResourceId and r.myVersion = h.myResourceVersion) " + 
		"WHERE r.myId in (:pids)")
	Collection<ResourceHistoryTable> findByResourceIds(@Param("pids") Collection<Long> pids);
}
