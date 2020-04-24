package ca.uhn.fhir.jpa.dao.data;

import java.util.Collection;
import java.util.Date;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.entity.SearchParamPresent;

public interface ISearchParamPresentDao extends JpaRepository<SearchParamPresent, Long> {

	@Query("SELECT s FROM SearchParamPresent s WHERE s.myResource = :res")
	Collection<SearchParamPresent> findAllForResource(@Param("res") ResourceTable theResource);

	@Modifying
	@Query("delete from SearchParamPresent t WHERE t.myResourcePid = :resid")
	void deleteByResourceId(@Param("resid") Long theResourcePid);

}
