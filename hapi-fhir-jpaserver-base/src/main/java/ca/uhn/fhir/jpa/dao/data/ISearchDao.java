package ca.uhn.fhir.jpa.dao.data;

import java.util.Collection;
import java.util.Date;

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
import org.springframework.data.repository.query.Param;

import ca.uhn.fhir.jpa.entity.Search;

public interface ISearchDao extends JpaRepository<Search, Long> {

	@Query("SELECT s FROM Search s WHERE s.myUuid = :uuid")
	public Search findByUuid(@Param("uuid") String theUuid);

	@Query("SELECT s FROM Search s WHERE s.myCreated < :cutoff")
	public Collection<Search> findWhereCreatedBefore(@Param("cutoff") Date theCutoff);

}
