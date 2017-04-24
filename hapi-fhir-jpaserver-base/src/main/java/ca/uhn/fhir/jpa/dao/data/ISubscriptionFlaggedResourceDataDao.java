package ca.uhn.fhir.jpa.dao.data;

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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ca.uhn.fhir.jpa.entity.SubscriptionFlaggedResource;

public interface ISubscriptionFlaggedResourceDataDao extends JpaRepository<SubscriptionFlaggedResource, Long> {

	@Query("SELECT r FROM SubscriptionFlaggedResource r WHERE r.mySubscription.myId = :id ORDER BY r.myId ASC")
   public Page<SubscriptionFlaggedResource> findAllBySubscriptionId(@Param("id") Long theId, Pageable thePage);

	@Modifying
	@Query("DELETE FROM SubscriptionFlaggedResource r WHERE r.mySubscription.myId = :id")
	public void deleteAllForSubscription(@Param("id") Long theSubscriptionId);

}
