package ca.uhn.fhir.jpa.dao.data;

import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.entity.SearchResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

public interface ISearchResultDao  extends JpaRepository<SearchResult, Long> {
	
	@Query(value="SELECT r.myResourcePid FROM SearchResult r WHERE r.mySearch = :search ORDER BY r.myOrder ASC")
	Page<Long> findWithSearchUuid(@Param("search") Search theSearch, Pageable thePage);

	@Query(value="SELECT r.myResourcePid FROM SearchResult r WHERE r.mySearch = :search ORDER BY r.myOrder ASC")
	List<Long> findWithSearchUuid(@Param("search") Search theSearch);

	@Query(value="SELECT r.myId FROM SearchResult r WHERE r.mySearchPid = :search")
	Slice<Long> findForSearch(Pageable thePage, @Param("search") Long theSearchPid);

	@Modifying
	@Query("DELETE FROM SearchResult s WHERE s.myResourcePid IN :ids")
	void deleteByResourceIds(@Param("ids") List<Long> theContent);

	@Modifying
	@Query("DELETE FROM SearchResult s WHERE s.myId IN :ids")
	void deleteByIds(@Param("ids") List<Long> theContent);
}
