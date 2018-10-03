package ca.uhn.fhir.jpa.dao.data;

import ca.uhn.fhir.jpa.entity.Search;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Date;

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
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

public interface ISearchDao extends JpaRepository<Search, Long> {

	@Query("SELECT s FROM Search s WHERE s.myUuid = :uuid")
	Search findByUuid(@Param("uuid") String theUuid);

	@Query("SELECT s.myId FROM Search s WHERE s.mySearchLastReturned < :cutoff")
	Slice<Long> findWhereLastReturnedBefore(@Param("cutoff") Date theCutoff, Pageable thePage);

//	@Query("SELECT s FROM Search s WHERE s.myCreated < :cutoff")
//	public Collection<Search> findWhereCreatedBefore(@Param("cutoff") Date theCutoff);

	@Query("SELECT s FROM Search s WHERE s.myResourceType = :type AND mySearchQueryStringHash = :hash AND s.myCreated > :cutoff AND s.myDeleted = false")
	Collection<Search> find(@Param("type") String theResourceType, @Param("hash") int theHashCode, @Param("cutoff") Date theCreatedCutoff);

	@Modifying
	@Query("UPDATE Search s SET s.mySearchLastReturned = :last WHERE s.myId = :pid")
	void updateSearchLastReturned(@Param("pid") long thePid, @Param("last") Date theDate);

	@Modifying
	@Query("UPDATE Search s SET s.myDeleted = :deleted WHERE s.myId = :pid")
	void updateDeleted(@Param("pid") Long thePid, @Param("deleted") boolean theDeleted);

	@Modifying
	@Query("DELETE FROM Search s WHERE s.myId = :pid")
	void deleteByPid(@Param("pid") Long theId);
}
