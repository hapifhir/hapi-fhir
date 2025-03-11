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

import ca.uhn.fhir.jpa.entity.SearchResult;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface ISearchResultDao extends JpaRepository<SearchResult, Long>, IHapiFhirJpaRepository {

	@Query(
			value =
					"SELECT r.myResourcePartitionId,r.myResourcePid FROM SearchResult r WHERE r.mySearchPid = :search ORDER BY r.myOrder ASC")
	Slice<Object[]> findWithSearchPid(@Param("search") Long theSearchPid, Pageable thePage);

	@Query(value = "SELECT r.myResourcePartitionId,r.myResourcePid FROM SearchResult r WHERE r.mySearchPid = :search")
	List<Object[]> findWithSearchPidOrderIndependent(@Param("search") Long theSearchPid);

	@Modifying
	@Query("DELETE FROM SearchResult s WHERE s.mySearchPid IN :searchIds")
	@CanIgnoreReturnValue
	int deleteBySearchIds(@Param("searchIds") Collection<Long> theSearchIds);

	@Modifying
	@Query(
			"DELETE FROM SearchResult s WHERE s.mySearchPid = :searchId and s.myOrder >= :rangeStart and s.myOrder <= :rangeEnd")
	@CanIgnoreReturnValue
	int deleteBySearchIdInRange(
			@Param("searchId") Long theSearchId,
			@Param("rangeStart") int theRangeStart,
			@Param("rangeEnd") int theRangeEnd);

	@Query("SELECT count(r) FROM SearchResult r WHERE r.mySearchPid = :search")
	int countForSearch(@Param("search") Long theSearchPid);

	/**
	 * Converts a response from {@link #findWithSearchPid(Long, Pageable)} to
	 * a List of JpaPid objects
	 */
	static List<JpaPid> toJpaPidList(List<Object[]> theArrays) {
		List<JpaPid> retVal = new ArrayList<>(theArrays.size());
		for (Object[] next : theArrays) {
			Integer partitionId = (Integer) next[0];
			Long resourcePid = (Long) next[1];
			retVal.add(JpaPid.fromId(resourcePid, partitionId));
		}
		return retVal;
	}
}
