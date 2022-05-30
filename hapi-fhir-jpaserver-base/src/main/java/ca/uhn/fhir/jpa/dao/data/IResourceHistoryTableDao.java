package ca.uhn.fhir.jpa.dao.data;

import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/*
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

public interface IResourceHistoryTableDao extends JpaRepository<ResourceHistoryTable, Long>, IHapiFhirJpaRepository {

	/**
	 * This is really only intended for unit tests - There can be many versions of resources in
	 * the real world, use a pageable query for real uses.
	 */
	@Query("SELECT t FROM ResourceHistoryTable t WHERE t.myResourceId = :resId ORDER BY t.myResourceVersion ASC")
	List<ResourceHistoryTable> findAllVersionsForResourceIdInOrder(@Param("resId") Long theId);


	@Query("SELECT t FROM ResourceHistoryTable t LEFT OUTER JOIN FETCH t.myProvenance WHERE t.myResourceId = :id AND t.myResourceVersion = :version")
	ResourceHistoryTable findForIdAndVersionAndFetchProvenance(@Param("id") long theId, @Param("version") long theVersion);

	@Query("SELECT t.myId FROM ResourceHistoryTable t WHERE t.myResourceId = :resId AND t.myResourceVersion != :dontWantVersion")
	Slice<Long> findForResourceId(Pageable thePage, @Param("resId") Long theId, @Param("dontWantVersion") Long theDontWantVersion);

	@Query("" +
		"SELECT v.myId FROM ResourceHistoryTable v " +
		"LEFT OUTER JOIN ResourceTable t ON (v.myResourceId = t.myId) " +
		"WHERE v.myResourceVersion != t.myVersion AND " +
		"t.myId = :resId")
	Slice<Long> findIdsOfPreviousVersionsOfResourceId(Pageable thePage, @Param("resId") Long theResourceId);

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

	@Modifying
	@Query("UPDATE ResourceHistoryTable r SET r.myResourceVersion = :newVersion WHERE r.myResourceId = :id AND r.myResourceVersion = :oldVersion")
	void updateVersion(@Param("id") long theId, @Param("oldVersion") long theOldVersion, @Param("newVersion") long theNewVersion);

	@Modifying
	@Query("DELETE FROM ResourceHistoryTable t WHERE t.myId = :pid")
	void deleteByPid(@Param("pid") Long theId);
}
