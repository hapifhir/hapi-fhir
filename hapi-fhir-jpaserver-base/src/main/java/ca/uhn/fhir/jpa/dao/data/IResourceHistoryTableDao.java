package ca.uhn.fhir.jpa.dao.data;

import java.util.Date;

import javax.persistence.TemporalType;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.query.Param;

import ca.uhn.fhir.jpa.entity.ResourceHistoryTable;

public interface IResourceHistoryTableDao extends JpaRepository<ResourceHistoryTable, Long> {
	//@formatter:off
	
	@Query("SELECT COUNT(*) FROM ResourceHistoryTable t WHERE t.myUpdated >= :cutoff")
	int countForAllResourceTypes(
		@Temporal(value=TemporalType.TIMESTAMP) @Param("cutoff") Date theCutoff
		);
	
	@Query("SELECT COUNT(*) FROM ResourceHistoryTable t WHERE t.myResourceId = :id AND t.myUpdated >= :cutoff")
	int countForResourceInstance(
		@Param("id") Long theId,
		@Temporal(value=TemporalType.TIMESTAMP) @Param("cutoff") Date theCutoff
		);

	@Query("SELECT COUNT(*) FROM ResourceHistoryTable t WHERE t.myResourceType = :type AND t.myUpdated >= :cutoff")
	int countForResourceType(
		@Param("type") String theType,
		@Temporal(value=TemporalType.TIMESTAMP) @Param("cutoff") Date theCutoff
		);

	@Query("SELECT COUNT(*) FROM ResourceHistoryTable t")
	int countForAllResourceTypes(
		);
	
	@Query("SELECT COUNT(*) FROM ResourceHistoryTable t WHERE t.myResourceId = :id")
	int countForResourceInstance(
		@Param("id") Long theId
		);

	@Query("SELECT COUNT(*) FROM ResourceHistoryTable t WHERE t.myResourceType = :type")
	int countForResourceType(
		@Param("type") String theType
		);

//	@Query("SELECT t FROM ResourceHistoryTable t WHERE t.myUpdated >= :cutoff ORDER BY t.myUpdated DESC")
//	List<ResourceHistoryTable> findForAllResourceTypes(
//		@Temporal(value=TemporalType.TIMESTAMP) @Param("cutoff") Date theCutoff, 
//		Pageable thePageable);
//
//	@Query("SELECT t FROM ResourceHistoryTable t WHERE t.myResourceId = :id AND t.myUpdated >= :cutoff ORDER BY t.myUpdated DESC")
//	List<ResourceHistoryTable> findForResourceInstance(
//		@Param("id") Long theId,
//		@Temporal(value=TemporalType.TIMESTAMP) @Param("cutoff") Date theCutoff, 
//		Pageable thePageable);
//
//	@Query("SELECT t FROM ResourceHistoryTable t WHERE t.myResourceType = :type AND t.myUpdated >= :cutoff ORDER BY t.myUpdated DESC")
//	List<ResourceHistoryTable> findForResourceType(
//		@Param("type") String theType,
//		@Temporal(value=TemporalType.TIMESTAMP) @Param("cutoff") Date theCutoff, 
//		Pageable thePageable);
//
//	@Query("SELECT t FROM ResourceHistoryTable t ORDER BY t.myUpdated DESC")
//	List<ResourceHistoryTable> findForAllResourceTypes(
//		Pageable thePageable);
//
//	@Query("SELECT t FROM ResourceHistoryTable t WHERE t.myResourceId = :id ORDER BY t.myUpdated DESC")
//	List<ResourceHistoryTable> findForResourceInstance(
//		@Param("id") Long theId,
//		Pageable thePageable);
//
//	@Query("SELECT t FROM ResourceHistoryTable t WHERE t.myResourceType = :type ORDER BY t.myUpdated DESC")
//	List<ResourceHistoryTable> findForResourceType(
//		@Param("type") String theType,
//		Pageable thePageable);

	@Query("SELECT t FROM ResourceHistoryTable t WHERE t.myResourceId = :id AND t.myResourceVersion = :version")
	ResourceHistoryTable findForIdAndVersion(@Param("id") long theId, @Param("version") long theVersion);
	
	//@formatter:on
}
