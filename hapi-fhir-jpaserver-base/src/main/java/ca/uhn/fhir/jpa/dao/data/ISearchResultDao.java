package ca.uhn.fhir.jpa.dao.data;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.entity.SearchResult;

public interface ISearchResultDao  extends JpaRepository<SearchResult, Long> {
	
	@Query(value="SELECT r FROM SearchResult r WHERE r.mySearch = :search")
	Collection<SearchResult> findWithSearchUuid(@Param("search") Search theSearch);
	
	@Query(value="SELECT r FROM SearchResult r WHERE r.mySearch = :search ORDER BY r.myOrder ASC")
	Page<SearchResult> findWithSearchUuid(@Param("search") Search theSearch, Pageable thePage);

	@Modifying
	@Query(value="DELETE FROM SearchResult r WHERE r.mySearchPid = :search")
	void deleteForSearch(@Param("search") Long theSearchPid);
}
